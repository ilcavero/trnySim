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

import static org.junit.Assert.*;
import ilca.model.Match;
import ilca.utils.Utils;

import org.junit.Test;

public class SwissTournamentTest {

	@Test
	public void getNextMatch() {
		SwissTournament swissTournament = new SwissTournament(Utils.createTeamList(8), 5);
		TournamentState instance = swissTournament.startTournamentInstance();
		int i = 0;
		Match m =  instance.getNextMatch();
		while (m != null) {
			m.setResult(1, 0, 0, 0);
			i++;
			m = instance.getNextMatch();
		}
		assertEquals(20,  i);
	}

	@Test
	public void oddTournament() {
		SwissTournament swissTournament = new SwissTournament(Utils.createTeamList(9), 5);
		TournamentState instance = swissTournament.startTournamentInstance();
		int i = 0;
		Match m =  instance.getNextMatch();
		while (m != null) {
			m.setResult(1, 0, 0, 0);
			i++;
			m = instance.getNextMatch();
		}
		assertEquals(22,  i);
	}
}