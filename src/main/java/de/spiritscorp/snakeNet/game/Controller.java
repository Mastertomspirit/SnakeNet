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

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

import javax.swing.SwingUtilities;

import de.spiritscorp.snakeNet.Parameter;
import de.spiritscorp.snakeNet.game.util.Direction;

public final class Controller {
	
	public static final int GAME_WIDTH = 500;
	public static final int GAME_HEIGHT = 500;
	private final Controller controller;
	private final Model model;
	private final Parameter param;
	private View view;
	
	/**
	 * 
	 * @param param The parameter list for build a neuronal net 
	 */
	public Controller(final Parameter param){
		controller = this;
		this.param = param;
		model = new Model(controller);
		model.initGame();
		try {
				SwingUtilities.invokeAndWait(
					() -> {
						view = new View(controller);
					});
		} catch (InvocationTargetException | InterruptedException e) {	e.printStackTrace();	System.exit(0);}		
	}

	/**
	 * 
	 * @return The parameter list of the neuronal net formatted as string
	 */
	 final String [] getPrintableParam() {
		String updater = param.getUpdater().getClass().toString();
		updater = updater.substring(updater.lastIndexOf(".") + 1, updater.length());
		return new String[] {
				param.getSeed() + " SEED",
				param.getMaxEpochStep() + " MAX EPOCH STEP",
				param.getMaxStep() + " MAX STEPS",
				param.getExpRepMaxSize() + " EXP REP MAX SIZE",
				param.getBatchSize() + " BATCH SIZE", 
				param.getTargetDqnUpdateFreq() + " TARGET DQN UPDATE FREQ",
				param.getUpdateStart() + " UPDATE START",
				param.getRewardFactor() + " REWARD FACTOR",
				param.getGamma() + " GAMMA",
				param.getErrorClamp() + " ERROR CLAMP",
				param.getMinEpsilon() + " MIN EPSILON",
				param.getEpsilonNbStep() + " EPSILON NB STEP",
				param.isDoubleDQN() + " DOUBLE DQN", 
				param.getL2() +  " L2",
				param.getNumHiddenNodes() + " NUM HIDDEN NODES",
				param.getNumLayers() + " NUM LAYERS",
				updater + " UPDATER"
		};
	 }
	 
	final int getScore() {
		return model.getSnake().size() - 3;
	}
	
	final void updateFrame() {
		view.update();
	}
	
	final void setDirection(Direction direction) {
		model.setDirection(direction);
	}
	
	final void initGame() {
		model.initGame();
	}
	
	final boolean runStep() {
		return model.runStep();
	}

	final LinkedList<Snake> getSnake(){
		return model.getSnake();
	}
	
	final Food getFood() {
		return model.getFood();
	}

	final void dispose() {
		view.dispose();
	}
}
