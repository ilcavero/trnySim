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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of swiss tournament state. Games will be generated
 * lazily in as big as possible batches on each call to getNextMatch().
 * @author ilcavero
 *
 */
public class SwissTournamentState extends GroupTournamentState implements TournamentState {
	private boolean finished = false;
	private HashMap<MatchUp, Integer> matchUpCounter;

	SwissTournamentState(GroupTournament tournament) {
		initialize(tournament);
		matchUpCounter = new HashMap<MatchUp, Integer>();
		for (Team a : tournament.getTeams()) {
			for (Team b : tournament.getTeams()) {
				matchUpCounter.put(new MatchUp(a, b), 0);
			}
		}
		generateMatches();
	}

	@Override
	public Match getNextMatch() {
		generateMatches();
		return super.getNextMatch();
	}

	@Override
	void generateMatches() {
		if (finished || !unplayedMatches.isEmpty())
			return;
		List<TeamItem> sortedTeams = getSortedTeamItems();
		List<TeamItem> teamsToMatch = getTeamsToMatch();
		int maxEncounters = 1 + (teamsToMatch.isEmpty() ? 0 : teamsToMatch.get(0).getMatchCount()) / sortedTeams.size();
		while(!teamsToMatch.isEmpty()) {
			TeamItem currentTeam = teamsToMatch.get(0);
			int startingPosition = sortedTeams.indexOf(currentTeam);
			int sign = 1;
			int i = 1;
			int maxValue = Math.max(sortedTeams.size() - startingPosition, startingPosition + 1);
			while(i < maxValue) {
				int position = startingPosition + i * sign;
				if(0 <= position && position < sortedTeams.size()) {
					TeamItem matchedTeam = sortedTeams.get(position);
					MatchUp matchUpKey = new MatchUp(currentTeam.getTeam(), matchedTeam.getTeam());
					Integer count = matchUpCounter.get(matchUpKey);
					if (count <= maxEncounters && matchedTeam.getMatchCount() < tournament.getNumberOfRounds()) {
						Match match = new GroupTournamentMatch(this, currentTeam.getTeam(), matchedTeam.getTeam());
						matches.add(match);
						unplayedMatches.add(match);
						matchUpCounter.put(matchUpKey, count + 1);
						sortedTeams.remove(position);
						teamsToMatch.remove(matchedTeam);
						break;
					}
				}
				sign *= -1;
				if(sign == 1) {
					i++;
				}
			}
			sortedTeams.remove(currentTeam);
			teamsToMatch.remove(0);
		}
		if(unplayedMatches.isEmpty()) {
			finished = true;
		}
	}
	
	private List<TeamItem> getTeamsToMatch() {
		int minPlays = tournament.getNumberOfRounds() - 1;
		List<TeamItem> teamsToMatch = new LinkedList<TeamItem>();
		for (TeamItem teamItem : teamItems) {
			if(teamItem.getMatchCount() < minPlays) {
				teamsToMatch.clear();
				minPlays = teamItem.getMatchCount();
			}
			if(teamItem.getMatchCount() == minPlays) {
				teamsToMatch.add(teamItem);
			}
		}
		return teamsToMatch;
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	private static final class MatchUp {
		private Team a, b;

		public MatchUp(Team a, Team b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof MatchUp))
				return false;
			MatchUp o = (MatchUp) obj;
			return (o.a.equals(a) && o.b.equals(b)) || (o.a.equals(b) && o.b.equals(a));
		}

		@Override
		public int hashCode() {
			return a.hashCode() + b.hashCode();
		}
	}
}
