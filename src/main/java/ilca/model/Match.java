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
package ilca.model;

import ilca.utils.Observable;

/**
 * Simple representation of a sports match where only two teams play each other
 * and the goal is to have higher score than the opponent. This is a two-state
 * object (not played->played) with the transition triggered by the setResult
 * method; otherwise this is an immutable object.
 * 
 * @author ilcavero
 * 
 */
public final class Match extends Observable<Match> {
	private Team home, visit;
	private int scoreHome, scoreVisit;
	private int homeRating;
	private int visitRating;
	private boolean played = false;

	public Match(Team home, Team visit) {
		this.home = home;
		this.visit = visit;
	}

	public int getHomeRating() {
		return homeRating;
	}

	public int getVisitRating() {
		return visitRating;
	}

	public int getScoreHome() {
		return scoreHome;
	}

	public int getScoreVisit() {
		return scoreVisit;
	}

	public Team getHome() {
		return home;
	}

	public Team getVisit() {
		return visit;
	}

	public Team getWinner() {
		if (scoreHome > scoreVisit)
			return home;
		else if (scoreVisit > scoreHome)
			return visit;
		else
			return null;
	}

	public boolean isDraw() {
		if (scoreHome == scoreVisit)
			return true;
		else
			return false;
	}

	public Team getLoser() {
		if (scoreHome < scoreVisit)
			return home;
		else if (scoreVisit < scoreHome)
			return visit;
		else
			return null;
	}

	public void setResult(int scoreHome, int scoreVisit) {
		setResult(scoreHome, scoreVisit, 0, 0);
	}

	public void setResult(int scoreHome, int scoreVisit, int homeRating, int visitRating) {
		if (isPlayed())
			throw new IllegalStateException("Result already set");
		if (scoreHome < 0 || scoreVisit < 0)
			throw new IllegalArgumentException("negative score");
		this.scoreHome = scoreHome;
		this.scoreVisit = scoreVisit;
		this.homeRating = homeRating;
		this.visitRating = visitRating;
		this.played = true;
		notifyObservers(this);
		clearObservers();
	}

	public boolean isPlayed() {
		return played;
	}

	@Override
	public String toString() {
		return home + "-vs-" + visit;
	}
}
