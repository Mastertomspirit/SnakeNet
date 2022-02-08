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

import java.awt.Point;
import java.util.LinkedList;

import de.spiritscorp.snakeNet.game.Controller;
import de.spiritscorp.snakeNet.game.Food;
import de.spiritscorp.snakeNet.game.Snake;

public class GameStateUtil {

	private GameStateUtil() {}

	/**
	 * Check the current state for the given direction
	 * @param snake
	 * @param food
	 * @param direction
	 * @return <b>double</b> </br>The values depend on what is detected in this direction
	 */
	public static synchronized double getStateForDirection(LinkedList<Snake> snake, Food food, Direction direction) {
		Point nextMove[] = new Point[Vars.NEXT_STEP_LENGTH];
		int bounds = 0;
		
		switch(direction) {
		case UP:	
			for(int i = 0; i < nextMove.length; i++) {
				bounds += snake.getFirst().getBounds().height;
					nextMove[i] = new Point(snake.getFirst().getPosition().x, snake.getFirst().getPosition().y - bounds); 
			}
			break;
		case DOWN:				
			for(int i = 0; i < nextMove.length; i++) {
				bounds += snake.getFirst().getBounds().height;
				nextMove[i] = new Point(snake.getFirst().getPosition().x, snake.getFirst().getPosition().y + bounds); 
			}
			break;
		case LEFT:	
			for(int i = 0; i < nextMove.length; i++) {
				bounds += snake.getFirst().getBounds().width;
				nextMove[i] = new Point(snake.getFirst().getPosition().x - bounds, snake.getFirst().getPosition().y); 
			}
			break;
		case RIGHT:	
			for(int i = 0; i < nextMove.length; i++) {
				bounds += snake.getFirst().getBounds().width;
				nextMove[i] = new Point(snake.getFirst().getPosition().x + bounds, snake.getFirst().getPosition().y); 
			}
			break;
		}

		if(!nextMoveNotAble(snake, nextMove[0]))	return Vars.SNAKE_DIE_STATE;

		double nextFiveSteps = nextFiveSteps(nextMove, food.getPosition(), snake);
//		System.out.printf("Richtung: %s  Wert: %.5f%n", direction, nextFiveSteps);
		
		
		if(nextMove[0].equals(food.getPosition())) return Vars.SNAKE_EAT_STATE + nextFiveSteps;
				
		if(direction == Direction.LEFT || direction == Direction.RIGHT) {
			if(Math.abs(nextMove[0].x - food.getPosition().x) < Math.abs(snake.getFirst().getPosition().x - food.getPosition().x))	return Vars.SNAKE_NEAR_TO_FOOD + nextFiveSteps;
			else if(Math.abs(nextMove[0].x - food.getPosition().x) == Math.abs(snake.getFirst().getPosition().x - food.getPosition().x))	return 0 + nextFiveSteps;
			else	return Vars.SNAKE_NOT_NEAR_TO_FOOD + nextFiveSteps;
		}else {
			if(Math.abs(nextMove[0].y - food.getPosition().y) < Math.abs(snake.getFirst().getPosition().y - food.getPosition().y))	return Vars.SNAKE_NEAR_TO_FOOD + nextFiveSteps;
			else if(Math.abs(nextMove[0].y - food.getPosition().y) == Math.abs(snake.getFirst().getPosition().y - food.getPosition().y))	return 0 + nextFiveSteps;
			else	return Vars.SNAKE_NOT_NEAR_TO_FOOD + nextFiveSteps;
		}
	}

	/**
	 * Determined from all values the largest
	 * @param data The values to check
	 * @return <b>int</b> </br>The index where the value is
	 */
	public static synchronized int getMaxValueIndex(double[] data) {
		double maxValue = -1000.0;
		int max = 0;
		for(int i = 0; i< data.length; i++) {
			if(data[i] > maxValue) {
				maxValue = data[i];
				max = i;
			}
		}
		return max;
	}
	
	/**
	 * Check the next steps, depends on Vars.NEXT_STEP_LENGTH
	 * @param nextMove
	 * @param food
	 * @param snake
	 * @return <b>double</b> </br>The values depend on what is detected in this direction
	 */
	private static double nextFiveSteps(Point[] nextMove, Point food, LinkedList<Snake> snake) {
			double ret = 0;
			boolean hit = false;

			for(int i = 1; i < nextMove.length; i++) {
				if(nextMove[i].x <= 5 || nextMove[i].x + 5 >= Controller.GAME_WIDTH || nextMove[i].y <= 5 || nextMove[i].y + 5 >= Controller.GAME_HEIGHT) {
					ret += -(Vars.NEXT_STEP_NEGATIV / (i * 2.5));
					hit = true;
				}
				else if(nextMove[i].equals(food))	{
					ret += (Vars.NEXT_STEP_FOOD / i);
					hit = true;
				}
				
				if(!hit) {
					for(Snake boa : snake) {
						if(boa.getPosition().equals(nextMove[i])) {
							ret += -(Vars.NEXT_STEP_NEGATIV / i);
							hit = true;
						}
					}
				}
				
				if(!hit) {
					ret += (Vars.NEXT_STEP_POSITIV / i);
				}
				hit = false;
			}
		return ret;
	}
	
	/**
	 * Check about a collision for the next move
	 * @param snake
	 * @param nextMove
	 * @return <b>boolean</b> </br>true when the way is clear
	 */
	private static boolean nextMoveNotAble(LinkedList<Snake> snake, Point nextMove) {
			for(Snake s : snake) {
				if(s.getPosition().equals(nextMove)) return false;
			}
			if(snake.getFirst().getPosition().x <= 5 || snake.getFirst().getPosition().x >= Controller.GAME_WIDTH ||
			   snake.getFirst().getPosition().y <= 5 || snake.getFirst().getPosition().y >= Controller.GAME_HEIGHT)	return false;
		return true;
	}
}
