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
package ilca.model.tournaments;

import ilca.model.Match;
import ilca.model.Team;

/**
 * Represents the data associated with a team's standing in a league-like tournament.
 * @author ilcavero
 *
 */
class TeamItem {

	private Team team;
	private int matchCount, wins, draws, loses, goalsScored, goalsConceded;

	Team getTeam() {
		return team;
	}

	int getWins() {
		return wins;
	}

	int getDraws() {
		return draws;
	}

	int getLoses() {
		return loses;
	}

	int getPoints() {
		return wins * 3 + draws * 1;
	}

	int getGoalsScored() {
		return goalsScored;
	}

	int getGoalsConceded() {
		return goalsConceded;
	}

	int getMatchCount() {
		return matchCount;
	}
	
	void setTeam(Team team) {
		this.team = team;
	}

	void addResult(Match m) {
		boolean homeTeam;
		if(team.equals(m.getHome()))
			homeTeam = true;
		else if(team.equals(m.getVisit()))
			homeTeam = false;
		else
			throw new IllegalArgumentException("The match does not belong to the team in TeamItem");
		goalsScored += homeTeam ? m.getGoalsHome() : m.getGoalsVisit();	
		goalsConceded += homeTeam ? m.getGoalsVisit() : m.getGoalsHome();
		if (m.getGoalsHome() == m.getGoalsVisit()) {
			draws++;
		} else if (m.getGoalsHome() > m.getGoalsVisit()) {
			if(homeTeam)
				wins++;
			else
				loses++;
		} else {
			if(homeTeam)
				loses++;
			else
				wins++;
		}
		matchCount++;
	}

	void reset() {
		wins = 0;
		draws = 0;
		loses = 0;
		goalsScored = 0;
		goalsConceded = 0;
	}
}
