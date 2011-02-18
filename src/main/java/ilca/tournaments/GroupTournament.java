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

import ilca.model.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for league-like tournaments that maintain a standing of teams.
 * @author ilcavero
 *
 */
abstract class GroupTournament implements Tournament {

	private final int numberOfRounds;
	private final List<Team> teams;

	GroupTournament(List<Team> teams, int numberOfRounds) {
		if (numberOfRounds <= 0)
			throw new IllegalArgumentException("Number of rounds should be greater than zero");
		if (teams == null || teams.isEmpty())
			throw new IllegalArgumentException("No teams were added to the tournament");
		this.numberOfRounds = numberOfRounds;
		this.teams = new ArrayList<Team>();
		this.teams.addAll(teams);
	}

	@Override
	public List<Team> getTeams() {
		ArrayList<Team> dest = new ArrayList<Team>();
		dest.addAll(teams);
		return dest;
	}

	@Override
	public int getNumberOfRounds() {
		return numberOfRounds;
	}
}
