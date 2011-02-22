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
package ilca.tournaments;

import ilca.model.Match;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of swiss tournament state. Games will be generated
 * lazily in as big as possible batches on each call to getNextMatch().
 * @author ilcavero
 *
 */
public class SwissTournamentState extends GroupTournamentState implements TournamentState {
	private boolean finished = false;
	private Map<TeamItem, Map<TeamItem, Integer>> matchUpMatrix = new HashMap<TeamItem, Map<TeamItem,Integer>>();
	
	SwissTournamentState(GroupTournament tournament) {
		initialize(tournament);
		for (TeamItem ti : teamItems) {
			matchUpMatrix.put(ti, new HashMap<TeamItem, Integer>());
		}
		generateMatches();
	}

	@Override
	public Match getNextMatch() {
		generateMatches();
		return super.getNextMatch();
	}
	
	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	void generateMatches() {
		if (finished || !unplayedMatches.isEmpty())
			return;
		List<TeamItem> sortedTeams = getSortedTeamItems();
		List<TeamItem> teamsToMatch = getTeamsToMatch(sortedTeams);
		int unmatchedTeamsCounter = 0;
		int maxEncounters = 1 + (teamsToMatch.isEmpty() ? 0 : teamsToMatch.get(0).getMatchCount()) / (sortedTeams.size() - 1);
		nextTeam:
		while(teamsToMatch.size() > unmatchedTeamsCounter) {
			TeamItem currentTeam = teamsToMatch.get(unmatchedTeamsCounter);
			int startingPosition = sortedTeams.indexOf(currentTeam);
			int sign = 1;
			int i = 1;
			int maxValue = Math.max(sortedTeams.size() - startingPosition, startingPosition + 1);
			while(i < maxValue) {
				int position = startingPosition + i * sign;
				if(0 <= position && position < sortedTeams.size()) {
					TeamItem matchedTeam = sortedTeams.get(position);
					int count = getMatchUpCount(currentTeam, matchedTeam);
					if (count < maxEncounters && matchedTeam.getMatchCount() < tournament.getNumberOfRounds()) {
						createMatch(currentTeam, matchedTeam);
						setMatchUpCount(currentTeam, matchedTeam, count + 1);
						sortedTeams.remove(position);
						teamsToMatch.remove(matchedTeam);
						sortedTeams.remove(currentTeam);
						teamsToMatch.remove(unmatchedTeamsCounter);
						continue nextTeam;
					}
				}
				sign *= -1;
				if(sign == 1) {
					i++;
				}
			}
			sortedTeams.remove(currentTeam);
			unmatchedTeamsCounter++;
		}
		if(unplayedMatches.isEmpty()) {
			while(teamsToMatch.size() != 0 && teamsToMatch.size() != 1) {
				createMatch(teamsToMatch.get(0), teamsToMatch.get(1));
				teamsToMatch.remove(0);
				teamsToMatch.remove(0);
			}
			finished = true;
		}
	}
	
	private int getMatchUpCount(TeamItem tiA, TeamItem tiB) {
		if(tiB.getTeam().getName().compareTo(tiA.getTeam().getName()) > 0) {
			TeamItem temp = tiA;
			tiA = tiB;
			tiB = temp;
		}
		Map<TeamItem, Integer> counterForA = matchUpMatrix.get(tiA);
		Integer count = counterForA.get(tiB);
		if(count == null) {
			counterForA.put(tiB, 0);
			return 0;
		}
		return count;
	}

	private void setMatchUpCount(TeamItem tiA, TeamItem tiB, int newCount) {
		if(tiB.getTeam().getName().compareTo(tiA.getTeam().getName()) > 0) {
			TeamItem temp = tiA;
			tiA = tiB;
			tiB = temp;
		}
		matchUpMatrix.get(tiA).put(tiB, newCount);
	}

	private void createMatch(TeamItem teamA, TeamItem teamB) {
		Match match;
		if(teamA.getHomeMatchCount() <= teamB.getHomeMatchCount()) {
			match = new GroupTournamentMatch(this, teamA, teamB);
		} else {
			match = new GroupTournamentMatch(this, teamB, teamA);
		}
		matches.add(match);
		unplayedMatches.add(match);
	}
	
	private List<TeamItem> getTeamsToMatch(List<TeamItem> sortedteams) {
		int minPlays = tournament.getNumberOfRounds() - 1;
		List<TeamItem> teamsToMatch = new LinkedList<TeamItem>();
		for (TeamItem teamItem : sortedteams) {
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
}