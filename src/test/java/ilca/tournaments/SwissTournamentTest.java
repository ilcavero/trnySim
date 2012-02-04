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

import static org.junit.Assert.*;

import java.util.List;

import ilca.model.Match;
import ilca.model.Team;
import ilca.tournaments.SwissTournament;
import ilca.tournaments.TournamentState;
import ilca.utils.Utils;

import org.junit.Test;

public class SwissTournamentTest {

	@Test
	public void testEvenTournamentGeneratedMatchesCount() {
		SwissTournament swissTournament = new SwissTournament(Utils.createTeamList(8), 5);
		TournamentState instance = swissTournament.startTournamentInstance();
		int i = 0;
		Match m = instance.getNextMatch();
		while (m != null) {
			m.setResult(1, 0, 0, 0);
			i++;
			m = instance.getNextMatch();
		}
		assertEquals(20, i);
	}

	@Test
	public void testOddTournamentGeneratedMatchesCount() {
		SwissTournament swissTournament = new SwissTournament(Utils.createTeamList(9), 5);
		TournamentState instance = swissTournament.startTournamentInstance();
		int i = 0;
		Match m = instance.getNextMatch();
		while (m != null) {
			m.setResult(1, 0, 0, 0);
			i++;
			m = instance.getNextMatch();
		}
		assertEquals(22, i);
	}

	@Test
	public void testLongTournamentGeneratedMatchesCount() {
		SwissTournament swissTournament = new SwissTournament(Utils.createTeamList(8), 10);
		TournamentState instance = swissTournament.startTournamentInstance();
		int i = 0;
		Match m = instance.getNextMatch();
		while (m != null) {
			m.setResult(1, 0, 0, 0);
			i++;
			m = instance.getNextMatch();
		}
		assertEquals(40, i);
	}

	@Test
	public void testFollowsSwissRules() {
		List<Team> teams = Utils.createTeamList(8);
		int[] teamKeys = { 0, 1, 2, 3, 4, 5, 6, 7, 6, 4, 2, 0, 1, 3, 5, 7, 6, 2, 1, 5, 4, 0, 3, 7, 1, 6, 4, 2, 3, 5, 7,
				0, 1, 4, 3, 6, 7, 2, 5, 0, 7, 1, 3, 4, 5, 6, 0, 2 };
		SwissTournament swissTournament = new SwissTournament(teams, 6);
		TournamentState instance = swissTournament.startTournamentInstance();
		int i = 0;
		Match m = instance.getNextMatch();
		while (m != null) {
			assertEquals(teams.get(teamKeys[2 * i]), m.getHome());
			assertEquals(teams.get(teamKeys[2 * i + 1]), m.getVisit());
			m.setResult(1 + i, 0, 0, 0);
			i++;
			m = instance.getNextMatch();
		}
	}

	@Test
	public void testFollowsSwissRulesOddTournamentWithTwoRounds() {
		List<Team> teams = Utils.createTeamList(5);
		int[] teamKeys = { 0, 1, 2, 3, 4, 1, 4, 2, 3, 0, 3, 4, 2, 0, 1, 2, 1, 3, 0, 4, 1, 3, 0, 2, 4, 3, 1, 0, 2, 4, 2,
				1, 0, 4 };
		SwissTournament swissTournament = new SwissTournament(teams, 7);
		TournamentState instance = swissTournament.startTournamentInstance();
		int i = 0;
		Match m = instance.getNextMatch();
		while (m != null) {
			assertEquals(teams.get(teamKeys[2 * i]), m.getHome());
			assertEquals(teams.get(teamKeys[2 * i + 1]), m.getVisit());
			m.setResult(1 + i, 0, 0, 0);
			i++;
			m = instance.getNextMatch();
		}
	}

	@Test
	public void testIsFinished() throws Exception {
		SwissTournament swissTournament = new SwissTournament(Utils.createTeamList(4), 2);
		TournamentState instance = swissTournament.startTournamentInstance();
		assertFalse(instance.isFinished());
		instance.getNextMatch().setResult(1, 0, 0, 0);
		assertFalse(instance.isFinished());
		instance.getNextMatch().setResult(1, 0, 0, 0);
		assertFalse(instance.isFinished());
		instance.getNextMatch().setResult(1, 0, 0, 0);
		assertFalse(instance.isFinished());
		instance.getNextMatch().setResult(1, 0, 0, 0);
		assertTrue(instance.isFinished());
		assertNull(instance.getNextMatch());

	}
}
