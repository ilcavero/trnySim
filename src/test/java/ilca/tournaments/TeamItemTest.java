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

import org.junit.Assert;
import org.junit.Test;


public class TeamItemTest {
	
	@Test
	public void countMatchResults() {
		Team t0 = new Team("T0", 1000);
		Team t1 = new Team("T1", 1500);
		TeamItem ti = new TeamItem();
		ti.setTeam(t0);
		Match m1 = new Match(t0, t1);
		Match m2 = new Match(t1, t0);
		Match m3 = new Match(t1, t0);
		m1.setResult(2, 0, 0, 0);
		m2.setResult(5, 1, 10, 10);
		m3.setResult(1, 1, 10, 10);
		ti.addResult(m1);
		ti.addResult(m2);
		ti.addResult(m3);
		Assert.assertEquals(1, ti.getDraws());
		Assert.assertEquals(6, ti.getGoalsConceded());
		Assert.assertEquals(4, ti.getGoalsScored());
		Assert.assertEquals(1, ti.getHomeMatchCount());
		Assert.assertEquals(1, ti.getLoses());
		Assert.assertEquals(3, ti.getMatchCount());
		Assert.assertEquals(4, ti.getPoints());
		Assert.assertEquals(1, ti.getWins());
		Assert.assertEquals(t0, ti.getTeam());
	}

}
