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

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of league tournament state. Games will be generated on
 * construction.
 * 
 * @author ilcavero
 * 
 */
final class LeagueTournamentState extends GroupTournamentState {

	LeagueTournamentState(LeagueTournament tournament) {
		super(tournament);
		generateMatches();
	}

	@Override
	public boolean isFinished() {
		return unplayedMatches.size() == 0;
	}

	@Override
	void generateMatches() {
		if (!matches.isEmpty())
			return;
		int pivot = 0;
		List<TeamItem> teams = new ArrayList<TeamItem>(teamItems);
		int size = teams.size();
		int numberOfMatchdays = this.getTournament().getNumberOfRounds();
		if (size % 2 != 0) {
			numberOfMatchdays += numberOfMatchdays / (size - 1);
			teams.add(null);
			size++;
		}
		for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
			for (int i = 0; i < size / 2; i++) {
				TeamItem home = teams.get(i);
				TeamItem visit = teams.get(size - 1 - i);
				if (home == null || visit == null) {
					continue;
				}
				if (matchday % 2 != 0) {
					TeamItem temp = home;
					home = visit;
					visit = temp;
				}
				Match match = new Match(home.getTeam(), visit.getTeam());
				match.addObserver(new GroupTournamentMatchResultObserver(this, home, visit));
				matches.add(match);
				unplayedMatches.add(match);
			}
			rotateList(teams, pivot);
		}
	}

	private void rotateList(List<TeamItem> list, int pivot) {
		TeamItem temp = list.remove(pivot);
		TeamItem last = list.remove(list.size() - 1);
		list.add(0, last);
		list.add(pivot, temp);
	}
}
