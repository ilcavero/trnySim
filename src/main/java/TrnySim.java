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
import ilca.engine.MatchEngine;
import ilca.engine.ResultTally;
import ilca.model.Team;
import ilca.tournaments.LeagueTournament;
import ilca.tournaments.SwissTournament;

import java.util.ArrayList;
import java.util.Random;

/**
 * This is a demo class of how to set up a simulation.
 * 
 * @author ilcavero
 * 
 */
public class TrnySim {
	static final int TOURNAMENT_SIZE = 20, NUMBER_OF_MATCHES = 25, ANALYSIS_REPETITIONS = 50, RUN_REPETITIONS = 200;

	public static void main(String[] args) {
		ResultTally tally = new ResultTally();
		ResultTally tally2 = new ResultTally();
		long nanoTime = System.nanoTime();
		for (int j = 1; j < ANALYSIS_REPETITIONS; j++) {
			ArrayList<Team> teams = new ArrayList<Team>();
			MatchEngine sim = new MatchEngine(j * 37);
			Random r = new Random(j * 11);
			for (int i = 0; i < TOURNAMENT_SIZE; i++) {
				Team t = new Team("T" + i, (int) (1500 + r.nextDouble() * 1000));
				teams.add(t);
			}
			LeagueTournament tournament = new LeagueTournament(teams, NUMBER_OF_MATCHES);
			tally.addTally(sim.simulateTournament(tournament, RUN_REPETITIONS));
			SwissTournament swissTrny = new SwissTournament(teams, NUMBER_OF_MATCHES);
			tally2.addTally(sim.simulateTournament(swissTrny, RUN_REPETITIONS));
		}

		System.out.println("Rank difference mean: League:" + tally.getRankDifferenceMean() + " Swiss:"
				+ tally2.getRankDifferenceMean());
		System.out.println("Rank difference deviation: League:" + tally.getRankDifferenceStandardDeviation()
				+ " Swiss:" + tally2.getRankDifferenceStandardDeviation());
		System.out.println("percentage of correct rank: League:" + tally.getPercentageOfCorrectRank() + " Swiss:"
				+ tally2.getPercentageOfCorrectRank());
		System.out.println("percentage of correct first rank: League:" + tally.getPercentageOfCorrectRankForPosition(0)
				+ " Swiss:" + tally2.getPercentageOfCorrectRankForPosition(0));
		System.out.println("percentage of correct tenth rank: League:" + tally.getPercentageOfCorrectRankForPosition(9)
				+ " Swiss:" + tally2.getPercentageOfCorrectRankForPosition(9));
		System.out.println((System.nanoTime() - nanoTime) / 1000000 + "ms");
	}

}
