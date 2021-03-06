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

import ilca.math.IntegerSequence;
import ilca.math.NormalRandomSequence;
import ilca.model.Team;

/**
 * Sequence of ratings that a team will have during a tournament.
 * 
 * @author ilcavero
 * 
 */
class TeamPerformance {
	// TODO: this class has to be an abstract factory for sequences that you
	// parameterize
	// once, as it is is a bad wrapper for integer sequence, which is already a
	// wrapper to begin with.
	public static final int DEFAULT_PERFORMANCE_DEVIATION = 200;
	private IntegerSequence performanceGenerator;

	public TeamPerformance(IntegerSequence performanceGenerator) {
		if (performanceGenerator == null)
			throw new IllegalArgumentException("performanceGenerator is null");
		this.performanceGenerator = performanceGenerator;
	}

	public TeamPerformance(Team team, long seed) {
		if (team == null)
			throw new IllegalArgumentException("team is null");
		this.performanceGenerator = new NormalRandomSequence(seed, team.getRating(), DEFAULT_PERFORMANCE_DEVIATION);
	}

	public int getNextPerformance() {
		return performanceGenerator.nextInt();
	}
}
