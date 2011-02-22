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
import ilca.model.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Abstract class for the state of league-like tournaments.
 * 
 * @author ilcavero
 * 
 */
abstract class GroupTournamentState implements TournamentState {

	Tournament tournament;
	List<TeamItem> teamItems = new ArrayList<TeamItem>();
	List<Match> matches = new ArrayList<Match>();
	List<Match> unplayedMatches = new ArrayList<Match>();

	@Override
	public Tournament getTournament() {
		return tournament;
	}

	@Override
	public void reset() {
		for (TeamItem ti : teamItems)
			ti.reset();
		matches.clear();
		unplayedMatches.clear();
		generateMatches();
	}

	@Override
	public List<Team> getRankedTeams() {
		List<TeamItem> sortedTeamItems = getSortedTeamItems();
		List<Team> resultTeam = new ArrayList<Team>(sortedTeamItems.size());
		for (TeamItem teamItem : sortedTeamItems) {
			resultTeam.add(teamItem.getTeam());
		}
		return resultTeam;
	}

	@Override
	public Match getNextMatch() {
		if (unplayedMatches.isEmpty()) {
			return null;
		}
		return unplayedMatches.get(0);
	}

	@Override
	public List<Match> getKnownMatches() {
		ArrayList<Match> m = new ArrayList<Match>(matches);
		return m;
	}

	@Override
	public boolean isFinished() {
		return unplayedMatches.size() == 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("\tTEAM\trating\tW\tD\tL\tGS\tGA\tPts\n");
		List<TeamItem> teams = getSortedTeamItems();
		for (int i = 0; i < teams.size(); i++) {
			TeamItem ti = teams.get(i);
			sb.append((i + 1) + ".\t" + ti.getTeam().getName() + "\t" + ti.getTeam().getRating() + "\t" + ti.getWins()
					+ "\t" + ti.getDraws() + "\t" + ti.getLoses() + "\t" + ti.getGoalsScored() + "\t"
					+ ti.getGoalsConceded() + "\t" + ti.getPoints() + "\n");
		}
		sb.append("Matches:\n");
		for (Match m : matches) {
			sb.append(m.getHome().getName() + " " + m.getGoalsHome() + " : " + m.getVisit().getName() + " "
					+ m.getGoalsVisit() + "\n");
		}
		return sb.toString();
	}

	void initialize(GroupTournament tournament) {
		this.tournament = tournament;
		for (Team team : tournament.getTeams()) {
			TeamItem ti = new TeamItem();
			ti.setTeam(team);
			teamItems.add(ti);
		}
	}

	List<TeamItem> getSortedTeamItems() {
		Collections.sort(teamItems, new Comparator<TeamItem>() {

			@Override
			public int compare(TeamItem o1, TeamItem o2) {
				if (o1.getPoints() > o2.getPoints())
					return -1;
				if (o1.getPoints() < o2.getPoints())
					return 1;
				if (o1.getGoalsScored() - o1.getGoalsConceded() > o2.getGoalsScored() - o2.getGoalsConceded())
					return -1;
				if (o1.getGoalsScored() - o1.getGoalsConceded() < o2.getGoalsScored() - o2.getGoalsConceded())
					return 1;
				if (o1.getGoalsScored() > o2.getGoalsScored())
					return -1;
				if (o1.getGoalsScored() < o2.getGoalsScored())
					return 1;
				return 0;
			}

		});
		List<TeamItem> sortedTeamItemsCopy = new ArrayList<TeamItem>(teamItems);
		return sortedTeamItemsCopy;
	}

	abstract void generateMatches();
}