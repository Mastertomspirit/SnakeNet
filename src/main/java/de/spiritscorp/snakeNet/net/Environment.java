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

package de.spiritscorp.snakeNet.net;

import java.util.Arrays;

import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;

import de.spiritscorp.snakeNet.Main;
import de.spiritscorp.snakeNet.game.API;
import de.spiritscorp.snakeNet.game.util.Direction;
import de.spiritscorp.snakeNet.net.util.GameState;

public class Environment  implements MDP<GameState, Integer, DiscreteSpace>{

	private final API game;
	private final DiscreteSpace actionSpace = new DiscreteSpace(4);
	
	public Environment(final API game) {
		this.game = game;
	}

	@Override
	public ObservationSpace<GameState> getObservationSpace() {
		return new GameObservationSpace();
	}

	@Override
	public DiscreteSpace getActionSpace() {
		return actionSpace;
	}

	@Override
	public GameState reset() {
		return game.initializeGame();
	}

	@Override
	public void close() {}

	@Override
	public StepReply<GameState> step(final Integer action) {
		game.move(Direction.getDirection(action));
		Main.waitMs(0);
//		System.out.println(Arrays.toString(game.buildStateObservation().toArray()));
		return new StepReply<>(
				game.buildStateObservation(),
				game.calculateReward(Direction.getDirection(action)),
				isDone(),
				"SnakeNet"
				);
	}

	@Override
	public boolean isDone() {
		return !game.getGameRun();
	}

	@Override
	public MDP<GameState, Integer, DiscreteSpace> newInstance() {
		game.initializeGame();
		return new Environment(game);
	}
}
