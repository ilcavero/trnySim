/*******************************************************************************
 * Copyright (c) 2011 Ignacio Cavero.
 * 
 * This file is part of trnySim.
 * 
 * trnySim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * trnySim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with trnySim.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library.  Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 * 
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module.  An independent module is a module which is not derived from
 * or based on this library.  If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obligated to do so.  If you do not wish to do so, delete this
 * exception statement from your version.
 ******************************************************************************/
package ilca.engine;

import ilca.model.Match;
import ilca.model.Team;
import ilca.tournaments.Tournament;
import ilca.tournaments.TournamentState;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

/**
 * Implementation of match simulator based on the normal distribution of team performances,
 * a draw threshold and a ratings proportional score.
 * @author ilcavero
 *
 */
public class MatchEngine {
	private static final int RATING_DIFF_PER_SCORE_DIFF = 100;
	private static final int DRAW_THRESHOLD = 200;
	private Map<Team, TeamPerformance> performances = new Hashtable<Team, TeamPerformance>();
	private Random seedGenerator;

	public MatchEngine(long seed) {
		seedGenerator = new Random(seed);
	}

	public void simulateMatch(Match match) {
		if (match == null)
			throw new IllegalArgumentException("Match cannot be null");
		int homeGameRating = getTeamPerformance(match.getHome()).getNextPerformance();
		int visitGameRating = getTeamPerformance(match.getVisit()).getNextPerformance();
		int goalW = Math.abs(homeGameRating - visitGameRating) / RATING_DIFF_PER_SCORE_DIFF;
		int goalL = goalW / 2;
		if (Math.abs(homeGameRating - visitGameRating) < DRAW_THRESHOLD) {
			match.setResult(goalW, goalW, homeGameRating, visitGameRating);
		} else if (homeGameRating > visitGameRating) {
			match.setResult(goalW, goalL, homeGameRating, visitGameRating);
		} else {
			match.setResult(goalL, goalW, homeGameRating, visitGameRating);
		}
	}

	public ResultTally simulateTournament(Tournament tournament, int repetitions) {
		if (tournament == null)
			throw new IllegalArgumentException("tournament cannot be null");
		ResultTally tally = new ResultTally();
		for (int i = 0; i < repetitions; i++) {
			TournamentState instance = tournament.startTournamentInstance();
			performances.clear();
			for (Match m = instance.getNextMatch(); m != null; m = instance.getNextMatch()) {
				simulateMatch(m);
			}
			tally.addTournamentResult(instance);
		}
		return tally;
	}

	private TeamPerformance getTeamPerformance(Team team) {
		TeamPerformance teamPerformance = performances.get(team);
		if (teamPerformance == null)
			return addDefaultPerformance(team);
		return teamPerformance;
	}

	private TeamPerformance addDefaultPerformance(Team team) {
		TeamPerformance teamPerformance = new TeamPerformance(team, getNewSeed());
		performances.put(team, teamPerformance);
		return teamPerformance;
	}

	private long getNewSeed() {
		return seedGenerator.nextLong();
	}
}
