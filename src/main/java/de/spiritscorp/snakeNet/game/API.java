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
import de.spiritscorp.snakeNet.net.Action;
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
	
	public final GameState initializeGame(boolean trainStatus, int playgroundSize) {
		controller.initGame(trainStatus, playgroundSize);
		gameRun = true;
		return buildNewStateObservation(playgroundSize);
	}
	
	public final void move(final Action action) {
		stopGame++;
		currentDirection = action.toDirection(controller.getSnake().getFirst().getPosition(), controller.getSnake().get(1).getPosition());
		food = controller.getFood();
		controller.setDirection(action.toDirection(controller.getSnake().getFirst().getPosition(), controller.getSnake().get(1).getPosition()));
		gameRun = controller.runStep();
		if(!gameRun) {
			stopGame = 0;
			score = 0;
		}
		if(stopGame > 6000) {
			gameRun = false;
			score = 0;
			stopGame = 0;
		}
		if(controller.getScore() != score)	{
			score = controller.getScore();
			stopGame = 0;
		}
	}

	public final double calculateReward() {
		if(gameRun == false)	{
			food = controller.getFood();
			return Vars.REWARD_DIE;
		}
		if(!food.getPosition().equals(controller.getFood().getPosition())) return Vars.REWARD_EAT;
		return positionNearToFood();
	}
		
	public final GameState buildNewStateObservation(int playgroundSize) {
		Point head = controller.getSnake().getFirst().getPosition();
		Point neck = controller.getSnake().get(1).getPosition();
		return new GameState(new double[] {
				GameStateUtil.getStateForDirection(controller.getSnake(), controller.getFood(), Action.LEFT.toDirection(head, neck), playgroundSize),
				GameStateUtil.getStateForDirection(controller.getSnake(), controller.getFood(), Action.FORWARD.toDirection(head, neck), playgroundSize),
				GameStateUtil.getStateForDirection(controller.getSnake(), controller.getFood(), Action.RIGHT.toDirection(head, neck), playgroundSize)
		});
	}
		
	private double positionNearToFood() {
		Point nextPosition = controller.getSnake().getFirst().getPosition();
		Point previousPosition = controller.getSnake().get(1).getPosition();	
		if(currentDirection == Direction.LEFT || currentDirection == Direction.RIGHT) {
			if(Math.abs(nextPosition.x - food.getPosition().x) < Math.abs(previousPosition.x - food.getPosition().x))	return Vars.REWARD_NEAR_TO_FOOD;
			else if(Math.abs(nextPosition.x - food.getPosition().x) == Math.abs(previousPosition.x - food.getPosition().x)) return 0;
			else	return Vars.REWARD_NOT_NEAR_TO_FOOD;
		}else {
			if(Math.abs(nextPosition.y - food.getPosition().y) < Math.abs(previousPosition.y - food.getPosition().y))	return Vars.REWARD_NEAR_TO_FOOD;
			else if(Math.abs(nextPosition.y - food.getPosition().y) == Math.abs(previousPosition.y - food.getPosition().y)) return 0;
			else	return Vars.REWARD_NOT_NEAR_TO_FOOD;
		}
	}

	public final String getPrintabeleParam() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < controller.getPrintableParam().length; i++) {
			sb.append(controller.getPrintableParam()[i]);
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
	
	public final boolean getGameRun() {
		return gameRun;
	}

	public final int getScore() {
		return controller.getScore();
	}

	public final void dispose() {
		controller.dispose();
	}
}
