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

import static org.junit.Assert.*;
import org.junit.Test;

public class RandomSequenceTest {
	@Test
	public void sequenceRepetition() throws Exception {
		sequenceTester(new UniformRandomSequence(0, 0, 100));
		sequenceTester(new NormalRandomSequence(0, 50, 20));
	}

	private void sequenceTester(RandomSequence r) {
		int[][] results = new int[5][100];
		for (int i = 0; i < results.length; i++) {
			r.reset();
			for (int j = 0; j < results[i].length; j++) {
				results[i][j] = r.nextInt();
			}
		}
		for (int i = 0; i < results.length; i++) {
			for (int j = 0; j < results[0].length; j++) {
				assertEquals(results[0][j], results[i][j]);
			}
		}
	}

	@Test
	public void normalDistribution() {
		// Not mathematically rigorous. ;-)
		int stdDeviation = 5;
		int mean = 50;
		int pop = 10000;
		int count1µ = 0, count2µ = 0, count3µ = 0;
		NormalRandomSequence r = new NormalRandomSequence(1, mean, stdDeviation);
		r.reset();
		for (int i = 0; i < pop; i++) {
			int x = r.nextInt();
			if (x > mean - stdDeviation && x < mean + stdDeviation)
				count1µ++;
			else if (x > mean - 2 * stdDeviation && x < mean + 2 * stdDeviation)
				count2µ++;
			else
				count3µ++;
		}
		if (count1µ < pop * 0.62 || count1µ > pop * 0.75)
			fail();
		if (count2µ < pop * 0.20 || count2µ > pop * 0.32)
			fail();
		if (count3µ < pop * 0.01 || count3µ > pop * 0.05)
			fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void uniformDistributionArgumentsTest() {
		new UniformRandomSequence(0, 100, -100);
	}

	@Test
	public void uniformDistribution() {
		int pop = 10000;
		UniformRandomSequence r = new UniformRandomSequence(1, 0, 100);
		r.reset();
		int result[] = new int[pop];
		for (int i = 0; i < pop; i++) {
			result[i] = r.nextInt();
		}
		int c1 = 0, c2 = 0, c3 = 0;
		for (int i = 0; i < pop; i++) {
			if (result[i] < 0 || result[i] >= 100)
				fail();
			if (result[i] >= 0 && result[i] < 33)
				c1++;
			if (result[i] >= 33 && result[i] < 66)
				c2++;
			if (result[i] >= 66 && result[i] < 100)
				c3++;
		}
		assertTrue(c1 - c2 < pop * 0.1);
		assertTrue(c2 - c1 < pop * 0.1);
		assertTrue(c3 - c2 < pop * 0.1);
		assertTrue(c2 - c3 < pop * 0.1);
		assertTrue(c1 - c3 < pop * 0.1);
		assertTrue(c3 - c1 < pop * 0.1);
	}
}
