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
import ilca.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class LeagueTournamentTest {

	@Test
	public void countGeneratedMatches() {
		List<Team> teams = Utils.createTeamList(10);
		TournamentState league = new LeagueTournament(teams, 2).startTournamentInstance();
		Match m;
		int count = 0;
		while ((m = league.getNextMatch()) != null) {
			m.setResult(0, 0, 0, 0);
			count++;
		}
		Assert.assertEquals(90, count);
	}

	@Test
	public void uniqueMatches() {
		final int numberOfRounds = 3;
		final int size = 15;
		HashMap<Team, Integer> counter = new HashMap<Team, Integer>();
		List<Team> teams = Utils.createTeamList(size);
		for (int i = 0; i < size; i++) {
			counter.put(teams.get(i), 0);
		}
		TournamentState league = new LeagueTournament(teams, numberOfRounds).startTournamentInstance();
		Match m;
		while ((m = league.getNextMatch()) != null) {
			m.setResult(0, 0, 0, 0);
			counter.put(m.getHome(), counter.get(m.getHome()) + 1);
			counter.put(m.getVisit(), counter.get(m.getVisit()) + 1);
		}
		for (Iterator<Integer> iterator = counter.values().iterator(); iterator.hasNext();) {
			Integer count = iterator.next();
			Assert.assertEquals(numberOfRounds * (size - 1), count.intValue());
		}
	}

	@Test
	public void leagueRanking() throws Exception {
		List<Team> teams = Utils.createTeamList(6);
		TournamentState league = new LeagueTournament(teams, 2).startTournamentInstance();
		Match m;
		while ((m = league.getNextMatch()) != null) {
			if (m.getHome().getName().compareTo(m.getVisit().getName()) < 0)
				m.setResult(1, 0, 0, 0);
			else
				m.setResult(0, 1, 0, 0);
		}
		List<Team> rankedTeams = league.getRankedTeams();
		String previousName = "";
		for (Team team : rankedTeams) {
			if (team.getName().compareTo(previousName) <= 0)
				Assert.fail("Teams not well ranked:" + team.getName());
		}
	}

	@Test
	public void tieRanking() {
		Tournament league = new LeagueTournament(Utils.createTeamList(4), 1);
		TournamentState state = league.startTournamentInstance();
		state.getNextMatch().setResult(2, 0, 0, 0);// 1-4
		state.getNextMatch().setResult(4, 1, 0, 0);// 2-3
		state.getNextMatch().setResult(0, 4, 0, 0);// 3-1
		state.getNextMatch().setResult(3, 0, 0, 0);// 2-4
		state.getNextMatch().setResult(2, 2, 0, 0);// 1-2
		state.getNextMatch().setResult(1, 1, 0, 0);// 3-4
		List<Team> result = state.getRankedTeams();
		Assert.assertEquals("T1", result.get(0).getName());
		Assert.assertEquals("T0", result.get(1).getName());
		Assert.assertEquals("T3", result.get(2).getName());
		Assert.assertEquals("T2", result.get(3).getName());
	}

	@Test
	public void empyTournament() {
		LeagueTournament league = new LeagueTournament(new ArrayList<Team>(), 1);
		Assert.assertNull(league.startTournamentInstance().getNextMatch());
	}

	@Test(expected = IllegalArgumentException.class)
	public void noMatchdaysTournament() {
		new LeagueTournament(Utils.createTeamList(5), 0);
	}

	@Test(expected = IllegalStateException.class)
	public void canOnlySetResultOnce() {
		TournamentState trny = new LeagueTournament(Utils.createTeamList(5), 1).startTournamentInstance();
		Match match = trny.getNextMatch();
		match.setResult(0, 1, 0, 0);
		match.setResult(0, 1, 0, 0);
	}

	@Test
	public void getKnownMatches() {
		final int size = 5;
		TournamentState trny = new LeagueTournament(Utils.createTeamList(size), 1).startTournamentInstance();
		List<Match> knownMatches = trny.getKnownMatches();
		Assert.assertEquals(knownMatches.size(), size * 4 / 2);
	}

	@Test
	public void reset() {
		TournamentState trny = new LeagueTournament(Utils.createTeamList(5), 1).startTournamentInstance();
		Match nextMatch;
		while ((nextMatch = trny.getNextMatch()) != null) {
			nextMatch.setResult(1, 0, 0, 0);
		}
		Assert.assertTrue(trny.isFinished());
		trny.reset();
		Assert.assertTrue("Tournament was not reset properly", !trny.isFinished());
	}
}
