/*
 		Snake Net

		Copyright (c) 2022 Tom Spirit
		
		This program is free software; you can redistribute it and/or modify
		it under the terms of the GNU General Public License as published by
		the Free Software Foundation; either version 3 of the License, or
		(at your option) any later version.
		
		This program is distributed in the hope that it will be useful,
		but WITHOUT ANY WARRANTY; without even the implied warranty of
		MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
		GNU General Public License for more details.
		
		You should have received a copy of the GNU General Public License
		along with this program; if not, write to the Free Software Foundation,
		Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package de.spiritscorp.snakeNet.game.util;

public class Vars {

	private Vars() {}

	/**
	 * Number of games in a series
	 */
	public final static int ITERATIONS = 10;
	
	/**
	 * Length of the snakes view
	 */
	public final static int NEXT_STEP_LENGTH = 8;

	//		All values for the game state calculation 
	public final static double SNAKE_DIE_STATE = -15.0;
	public final static double SNAKE_EAT_STATE = 15.0;
	public final static double SNAKE_NEAR_TO_FOOD = 1.5;
	public final static double SNAKE_NOT_NEAR_TO_FOOD = -1.5;
	public final static double NEXT_STEP_POSITIV = 3.37;
	public final static double NEXT_STEP_NEGATIV = 3.76;
	public final static double NEXT_STEP_FOOD = 11.99;
	
	//		All values for the reward calculation
	public final static double REWARD_DIE = -100;
	public final static double REWARD_EAT = 100;
	public final static double REWARD_NEAR_TO_FOOD = 2.0;
	public final static double REWARD_NOT_NEAR_TO_FOOD = -3.0;
}
