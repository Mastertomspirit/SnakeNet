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

package de.spiritscorp.snakeNet.game;

import java.awt.Point;
import de.spiritscorp.snakeNet.game.util.Direction;
import de.spiritscorp.snakeNet.game.util.GameStateUtil;
import de.spiritscorp.snakeNet.game.util.Vars;
import de.spiritscorp.snakeNet.net.util.GameState;

public final class API {

	private final Controller controller;
	private boolean gameRun = true;
	private Direction currentDirection;
	private Food food;
	private int stopGame = 0;
	private int score = 0;
	
	public API(final Controller controller) {
		this.controller = controller;
	}
	
	public final GameState initializeGame() {
		controller.initGame();
		gameRun = true;
		return buildStateObservation();
	}
	
	public final void move(final Direction direction) {
		stopGame++;
		currentDirection = direction;
		food = controller.getFood();
		controller.setDirection(direction);
		gameRun = controller.runStep();
		if(!gameRun) {
			stopGame = 0;
			score = 0;
		}
		if(stopGame > 10000) {
			gameRun = false;
			score = 0;
			stopGame = 0;
		}
		if(controller.getScore() != score)	{
			score = controller.getScore();
			stopGame = 0;
		}
	}
	
	public final double calculateReward(final Direction direction, final Food food, final boolean gameRun) {
		this.gameRun = gameRun;
		currentDirection = direction;
		this.food = food;
		return calculateReward(direction);
	}

	public final double calculateReward(final Direction direction) {
		if(gameRun == false)	{
			food = controller.getFood();
			return Vars.REWARD_DIE;
		}
		if(!food.getPosition().equals(controller.getFood().getPosition())) return Vars.REWARD_EAT;
		return positionNearToFood();
	}
	
	public final GameState buildStateObservation() {
		return new GameState(new double[] {
				GameStateUtil.getStateForDirection(controller.getSnake(), controller.getFood(), Direction.UP),
				GameStateUtil.getStateForDirection(controller.getSnake(), controller.getFood(), Direction.DOWN),
				GameStateUtil.getStateForDirection(controller.getSnake(), controller.getFood(), Direction.LEFT),
				GameStateUtil.getStateForDirection(controller.getSnake(), controller.getFood(), Direction.RIGHT)
		});
	}
	
	public final boolean getGameRun() {
		return gameRun;
	}
		
	private double positionNearToFood() {
		Point nextPosition = controller.getSnake().getFirst().getPosition();
		Point previousPosition = controller.getSnake().get(1).getPosition();	
		if(currentDirection == Direction.LEFT || currentDirection == Direction.RIGHT) {
			if(Math.abs(nextPosition.x - food.getPosition().x) < Math.abs(previousPosition.x - food.getPosition().x))	return Vars.REWARD_NEAR_TO_FOOD;
			else	return Vars.REWARD_NOT_NEAR_TO_FOOD;
		}else {
			if(Math.abs(nextPosition.y - food.getPosition().y) < Math.abs(previousPosition.y - food.getPosition().y))	return Vars.REWARD_NEAR_TO_FOOD;
			else	return Vars.REWARD_NOT_NEAR_TO_FOOD;
		}
	}

	public final int getScore() {
		return controller.getScore();
	}

	public final void dispose() {
		controller.dispose();
	}

	public final String getPrintabeleParam() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < controller.getPrintableParam().length; i++) {
			sb.append(controller.getPrintableParam()[i]);
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
}
