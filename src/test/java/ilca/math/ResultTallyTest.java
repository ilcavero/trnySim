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
package ilca.math;

import ilca.engine.ResultTally;
import ilca.model.Match;
import ilca.model.Team;
import ilca.model.tournaments.Tournament;
import ilca.model.tournaments.TournamentState;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResultTallyTest {

	private ResultTally tally;
	private ResultTally tally2;

	@Before
	public void createDataSet() {
		final int numberOfTeams = 8;
		final int numberOfResults = 100;
		List<Team> teams = new ArrayList<Team>();
		for (int i = 0; i < numberOfTeams; i++) {
			Team t = new Team("T" + i, 1000 - i);
			teams.add(t);
		}
		tally = new ResultTally();
		for (int i = 0; i < numberOfResults; i++) {
			final List<Team> temp = new ArrayList<Team>();
			temp.addAll(teams);
			for (int j = 0; j < temp.size() - 1; j = j + 2) {
				Team tempTeam = temp.get(j);
				temp.set(j, temp.get(j + 1));
				temp.set(j + 1, tempTeam);
			}
			tally.addTournamentResult(new TournamentStateMock(temp));
		}
	}

	@Before
	public void createDataSet2() {
		final int numberOfTeams = 12;
		final int numberOfResults = 100;
		List<Team> teams = new ArrayList<Team>();
		for (int i = 0; i < numberOfTeams; i++) {
			Team t = new Team("T" + i, 1000 - i);
			teams.add(t);
		}
		tally2 = new ResultTally();
		for (int i = 0; i < numberOfResults; i++) {
			List<Team> temp = new ArrayList<Team>();
			temp.add(teams.get(9));
			temp.add(teams.get(6));
			temp.add(teams.get(0));
			temp.add(teams.get(7));
			temp.add(teams.get(4));
			temp.add(teams.get(11));
			temp.add(teams.get(3));
			temp.add(teams.get(10));
			temp.add(teams.get(5));
			temp.add(teams.get(8));
			temp.add(teams.get(2));
			temp.add(teams.get(1));
			tally2.addTournamentResult(new TournamentStateMock(temp));
		}
	}

	@Test
	public void getRankDifferenceStandardDeviation() {
		Assert.assertEquals(0, tally.getRankDifferenceStandardDeviation(), 0.01);
		Assert.assertEquals(3.04, tally2.getRankDifferenceStandardDeviation(), 0.01);
	}

	@Test
	public void getRankDifferenceMean() {
		Assert.assertEquals(1, tally.getRankDifferenceMean(), 0.01);
		Assert.assertEquals(4.5, tally2.getRankDifferenceMean(), 0.01);
	}

	@Test
	public void getRankDifferenceStandardDeviationForPosition() {
		Assert.assertEquals(0, tally.getRankDifferenceStandardDeviationForPosition(4), 0.01);
		Assert.assertEquals(0, tally2.getRankDifferenceStandardDeviationForPosition(1), 0.01);
	}

	@Test
	public void getRankDifferenceMeanForPosition() {
		Assert.assertEquals(1, tally.getRankDifferenceMeanForPosition(4), 0.01);
		Assert.assertEquals(3, tally2.getRankDifferenceMeanForPosition(10), 0.01);
	}

	@Test
	public void percentageOfCorrectRank() {
		Assert.assertEquals(0, tally.getPercentageOfCorrectRank(), 0.01);
		Assert.assertEquals(0.08, tally2.getPercentageOfCorrectRank(), 0.01);
	}

	@Test
	public void percentageOfCorrectRankForPosition() {
		Assert.assertEquals(0, tally.getPercentageOfCorrectRankForPosition(4), 0.01);
		Assert.assertEquals(0, tally2.getPercentageOfCorrectRankForPosition(5), 0.01);
		Assert.assertEquals(1, tally2.getPercentageOfCorrectRankForPosition(4), 0.01);
	}

	@Test(expected = IllegalArgumentException.class)
	public void unifinishedTournament() {
		ResultTally uTally = new ResultTally();
		TournamentState uTrny = new TournamentStateMock(null) {
			@Override
			public boolean isFinished() {
				return false;
			}
		};
		uTally.addTournamentResult(uTrny);
	}

	private class TournamentStateMock implements TournamentState {
		private final List<Team> temp;

		private TournamentStateMock(List<Team> temp) {
			this.temp = temp;
		}

		@Override
		public void reset() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isFinished() {
			return true;
		}

		@Override
		public List<Team> getRankedTeams() {
			return temp;
		}

		@Override
		public Match getNextMatch() {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<Match> getKnownMatches() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Tournament getTournament() {
			return null;
		}
	}
}
