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

import ilca.model.Team;
import ilca.tournaments.TournamentState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class that accumulates the analysis of tournament simulations results.
 * 
 * @author ilcavero
 * 
 */
public class ResultTally {

	private long sumSquareDiff, sumDiff;
	private long dataSize, correctPositionCount;
	private PositionTalliesHolder positionTalliesHolder;
	private boolean keepPositionTallies;

	public ResultTally() {
		this(true);
	}

	public ResultTally(boolean keepPositionTallies) {
		this.keepPositionTallies = keepPositionTallies;
		positionTalliesHolder = new PositionTalliesHolder();
	}

	public void addTournamentResult(TournamentState resultTournament) {
		if (resultTournament == null || resultTournament.isFinished() == false)
			throw new IllegalArgumentException("Cannot tally null or unfinished tournaments");
		List<Team> result = resultTournament.getRankedTeams();
		ArrayList<Team> sortedCopy = new ArrayList<Team>(result);
		Collections.sort(sortedCopy, new Comparator<Team>() {

			@Override
			public int compare(Team o1, Team o2) {
				int rating1 = o1.getRating();
				int rating2 = o2.getRating();
				if (rating1 > rating2)
					return -1;
				else if (rating1 < rating2)
					return 1;
				else
					return 0;
			}
		});
		for (int i = 0; i < sortedCopy.size(); i++) {
			int actual = result.indexOf(sortedCopy.get(i));
			int deserved = i;
			addDataPoint(actual, deserved);
		}
	}

	public void addTally(ResultTally otherTally) {
		sumSquareDiff += otherTally.sumSquareDiff;
		sumDiff += otherTally.sumDiff;
		dataSize += otherTally.dataSize;
		correctPositionCount += otherTally.correctPositionCount;
		if (keepPositionTallies == true) {
			for (int i = 0; i < otherTally.positionTalliesHolder.size(); i++) {
				positionTalliesHolder.getPositionTally(i)
						.addTally(otherTally.positionTalliesHolder.getPositionTally(i));
			}
		}
	}

	public void addDataPoint(int actualPosition, int deservedPosition) {
		sumSquareDiff += Math.pow(actualPosition - deservedPosition, 2);
		sumDiff += Math.abs(actualPosition - deservedPosition);
		dataSize++;
		if (actualPosition == deservedPosition) {
			correctPositionCount++;
		}
		if (keepPositionTallies == true) {
			positionTalliesHolder.getPositionTally(deservedPosition).addDataPoint(actualPosition, deservedPosition);
		}
	}

	public double getRankDifferenceStandardDeviation() {
		if (dataSize < 3)
			return Double.NaN;
		double mean = getRankDifferenceMean();
		// sum = sum(0,size, abs(actual-deserved) - mean)²) = sum(abs(act-des)²
		// - 2*abs(act-des)*mean + mean²)
		// = sum(abs(act-des)²) - sum(2*abs(act-des)*mean) + sum(mean²)
		// = sum(abs(act-des)²) - 2*mean*sum(abs(act-des)) + size*mean²
		double sum = sumSquareDiff - 2 * mean * sumDiff + Math.pow(mean, 2) * dataSize;
		long N = dataSize - 1;
		return Math.sqrt(sum / (double) N);
	}

	public double getRankDifferenceMean() {
		return (sumDiff / (double) dataSize);
	}

	public double getRankDifferenceStandardDeviationForPosition(int deservedPosition) {
		if (deservedPosition < 1)
			throw new IllegalArgumentException("deserved positions should be positive integers");
		return positionTalliesHolder.getPositionTally(deservedPosition).getRankDifferenceStandardDeviation();
	}

	public double getRankDifferenceMeanForPosition(int deservedPosition) {
		if (deservedPosition < 1)
			throw new IllegalArgumentException("deserved positions should be positive integers");
		return positionTalliesHolder.getPositionTally(deservedPosition).getRankDifferenceMean();
	}

	public double getPercentageOfCorrectRank() {
		return (correctPositionCount / (double) dataSize);
	}

	public double getPercentageOfCorrectRankForPosition(int deservedPosition) {
		if (deservedPosition < 0)
			throw new IllegalArgumentException("deserved positions should be positive integers or zero");
		return positionTalliesHolder.getPositionTally(deservedPosition).getPercentageOfCorrectRank();
	}

	private class PositionTalliesHolder {
		private List<ResultTally> positionSpecificTallies = new ArrayList<ResultTally>();

		public ResultTally getPositionTally(int position) {
			if (ResultTally.this.keepPositionTallies == false) {
				throw new IllegalStateException("No specific position tallies are being kept");
			}
			while (positionSpecificTallies.size() <= position) {
				positionSpecificTallies.add(new ResultTally(false));
			}
			return positionSpecificTallies.get(position);
		}

		public int size() {
			return positionSpecificTallies.size();
		}
	}
}
