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
 * Class to analyze the result of tournaments.
 * @author ilcavero
 *
 */
public class ResultTally {

	private List<ResultTally.ResultDataPoint> tally = new ArrayList<ResultTally.ResultDataPoint>();

	public void addTournamentResult(TournamentState resultTournament) {
		if (resultTournament.isFinished() == false)
			throw new IllegalArgumentException("Cannot tally unfinished tournaments");
		List<Team> result = resultTournament.getRankedTeams();
		ArrayList<Team> sortedCopy = new ArrayList<Team>();
		sortedCopy.addAll(result);
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
			ResultDataPoint p = new ResultDataPoint();
			p.actual = result.indexOf(sortedCopy.get(i));
			p.deserved = i;
			tally.add(p);
		}
	}

	public double getRankDifferenceStandardDeviation() {
		if (tally.size() < 3)
			return Double.NaN;
		double mean = getRankDifferenceMean();
		double sum = 0;
		for (ResultDataPoint dataPoint : tally) {
			sum += Math.pow((Math.abs(dataPoint.actual - dataPoint.deserved) - mean), 2);
		}
		long N = tally.size() - 1;
		return Math.sqrt(sum / (double) N);
	}

	public double getRankDifferenceMean() {
		long sum = 0;
		for (ResultDataPoint dataPoint : tally) {
			sum += Math.abs(dataPoint.actual - dataPoint.deserved);
		}
		return (sum / (double) tally.size());
	}

	public double getRankDifferenceStandardDeviationForPosition(int deservedPosition) {
		double mean = getRankDifferenceMeanForPosition(deservedPosition);
		double sum = 0;
		long count = 0;
		for (ResultDataPoint dataPoint : tally) {
			if (dataPoint.deserved == deservedPosition) {
				sum += Math.pow((Math.abs(dataPoint.actual - dataPoint.deserved) - mean), 2);
				count++;
			}
		}
		if (count < 3)
			return Double.NaN;
		return Math.sqrt(sum / (double) (count - 1));
	}

	public double getRankDifferenceMeanForPosition(int deservedPosition) {
		long sum = 0, count = 0;
		for (ResultDataPoint dataPoint : tally) {
			if (dataPoint.deserved == deservedPosition) {
				sum += Math.abs(dataPoint.actual - dataPoint.deserved);
				count++;
			}
		}
		return (sum / (double) count);
	}

	public double getPercentageOfCorrectRank() {
		long count = 0;
		for (ResultDataPoint dataPoint : tally) {
			if (dataPoint.actual == dataPoint.deserved)
				count++;
		}
		return count / (double) tally.size();
	}

	public double getPercentageOfCorrectRankForPosition(int deservedPosition) {
		long count = 0, total = 0;
		for (ResultDataPoint dataPoint : tally) {
			if (deservedPosition == dataPoint.actual && deservedPosition == dataPoint.deserved)
				count++;
			if (deservedPosition == dataPoint.deserved)
				total++;
		}
		return count / (double) total;
	}

	private class ResultDataPoint {
		int deserved;
		int actual;
	}
}
