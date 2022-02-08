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

package de.spiritscorp.snakeNet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;

import org.deeplearning4j.core.storage.StatsStorage;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.api.TrainingListener;
import org.deeplearning4j.optimize.listeners.PerformanceListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.model.storage.InMemoryStatsStorage;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.learning.config.Adam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spiritscorp.snakeNet.game.API;
import de.spiritscorp.snakeNet.game.Controller;
import de.spiritscorp.snakeNet.game.util.Direction;
import de.spiritscorp.snakeNet.game.util.GameStateUtil;
import de.spiritscorp.snakeNet.game.util.Vars;
import de.spiritscorp.snakeNet.net.Environment;
import de.spiritscorp.snakeNet.net.util.GameState;
import de.spiritscorp.snakeNet.net.util.NetworkUtil;

class GameKI {

    private static final Logger LOG = LoggerFactory.getLogger(GameKI.class);
    private final API game;
    private final Parameter param;
    private final Path userDir = Paths.get(System.getProperty("user.dir"));
    private final boolean autoML;
    private Path savePath;
    private int highscore = 0;
    private int average = 0;

    /**
     * Initialize for a single run and create a default hyperparameter list
     */
	GameKI() {
		this.savePath = null;
		this.autoML = false;
        this.param = new Parameter(
        						65418466L, 									//	SEED
	        					8000, 										//	MAX EPOCH STEP
	        					20000, 										//	MAX STEPS
	        					150000,										//	EXP REP MAX SIZE
	        					768,										//	BATCH SIZE
	        					100, 										//	TARGET DQN UPDATE FREQ
	        					15, 										//	UPDATE START
	        					0.01, 										//	REWARD FACTOR
	        					0.85, 										//	GAMMA
	        					1.0, 										//	ERROR CLAMP
	        					0.01f, 										//	MIN EPSILON
	        					500,										//	EPSILON NB STEP
	        					true,										//	DOUBLE DQN
	        					0.000325, 									//	L2
	        					new Adam(0.001, 0.9, 0.99, 0.0000001),		//	UPDATER
	        					128, 										//	NUM HIDDEN NODES
	        					3 											//	NUM LAYERS
	        					);
		this.game = new API(new Controller(param));
	}
	
	GameKI(Parameter param){
		this.savePath = null;
		this.param = param;
		this.autoML = false;
		this.game = new API(new Controller(param));
	}
	
	/**
	 * Initialize for autoML or for a single run with a given parameter list
	 * @param autoML 
	 * @param param
	 */
	GameKI(Path savePath, Parameter param) {
		this.savePath = savePath;
		this.autoML = true;
		this.param = param;
		this.game = new API(new Controller(param));
	}
	
	void trainNewModel() {
		
        final Thread thread = new Thread(() -> {

            // Create the training environment and add listeners, if the server is not in use
            final Environment mdp = new Environment(game);
            NetworkUtil.setParam(param);
            QLearningDiscreteDense<GameState> dql;
            
            if(AutoML.uiServer == null) {
            	AutoML.uiServer = UIServer.getInstance();
	        	StatsStorage stats = new InMemoryStatsStorage();
	        	AutoML.uiServer.attach(stats);
	        	TrainingListener l1 = new PerformanceListener(50);
	            TrainingListener l2 = new ScoreIterationListener(100);    
	            ArrayList<TrainingListener> listeners = new ArrayList<>();
	            listeners.add(l1);
	            listeners.add(l2);
	            dql = new QLearningDiscreteDense<>(
	                    mdp,
	                    NetworkUtil.buildDQNFactory(listeners),
 	                    NetworkUtil.buildConfig()
	            );
            }else {
            	 dql = new QLearningDiscreteDense<>(
 	                    mdp,
 	                    NetworkUtil.buildDQNFactory(),
 	                    NetworkUtil.buildConfig()
 	            );
            }
            
            // Start the training
            dql.train();
            
            // Close resources 
            mdp.close();
            if(AutoML.uiServer != null) {
	            try {
	            	AutoML.uiServer.stop();
				} catch (InterruptedException e1) {	e1.printStackTrace();}
            }
            
            // Save the network
            String networkName = "network-" + System.currentTimeMillis() + "_" + System.nanoTime() + ".zip";

        	try {
        		dql.getNeuralNet().save(networkName);
        	} catch (IOException e) {
        		LOG.error(e.getMessage(), e);
        	}
        	
            // Reset the game before evaluate the trained network
            game.initializeGame();
            evaluateNetwork(game, networkName);

            //	Save the network with highscore and average
            if(autoML) {
	        	try {
	        		long millis = System.currentTimeMillis();
	        		long nanos = System.nanoTime();
	        		Files.move(userDir.resolve(networkName), getPathToSave(millis, nanos, false), StandardCopyOption.REPLACE_EXISTING);
	        		Files.writeString(getPathToSave(millis, nanos, true), game.getPrintabeleParam(), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
				} catch (IOException e) {e.printStackTrace();}
	        }
            game.dispose();
        });

        thread.start();
        try {
			thread.join();
		} catch (InterruptedException e) {e.printStackTrace();}
	}

	/**
	 * Load a pretrained network and retrain it with the given parameter
	 * TODO takes the specific parameter list for that network		&&		change String networkName to File networkName	
	 * 
	 * @param networkName The file name where the network is found
	 */
	void preTrainedModel(String networkName) {
		final Environment mdp = new Environment(game);

		// Load the and create the network
		DQN dqn = null;
		try {
			dqn = DQN.load(networkName);
		} catch (IOException e) {e.printStackTrace();}
		NetworkUtil.setParam(param);
		QLearningDiscreteDense<GameState> net = new QLearningDiscreteDense<>(mdp, dqn, NetworkUtil.buildConfig());
	
		//	First reset the previous states
		net.getNeuralNet().reset();
		net.train();
		mdp.close();
		
        // Save network
        String randomNetworkName = "network-" + System.currentTimeMillis() + "_" + System.nanoTime() + "_retrained_Net.zip";
    	try {
    		net.getNeuralNet().save(randomNetworkName);
    	} catch (IOException e) {LOG.error(e.getMessage(), e);}
    	
        // Reset the game before evaluate the trained network
        game.initializeGame();
        evaluateNetwork(game, randomNetworkName);
        game.dispose();
	}
	
	/**
	 * Play the game and evaluate highscore and average of the network
	 * @param game The game to play
	 * @param networkName The name of the network, which will evaluate
	 */
	private void evaluateNetwork(API game, String networkName) {
	
		final MultiLayerNetwork multiLayerNetwork = NetworkUtil.loadNetwork(networkName, false);
		highscore = 0;
		int score = 0;
		average = 0;
		int j = 0;
		for(int i = 0; i < Vars.ITERATIONS; i++) {
			score = 0;
			game.initializeGame();
			while(game.getGameRun()) {
				final GameState gameState = game.buildStateObservation();
				final INDArray output = multiLayerNetwork.output(gameState.getMatrix(), false);
				double[] data = output.data().asDouble();
				if(j % 60 == 0) System.out.println(Arrays.toString(data));
				j++;
				int maxValueIndex = GameStateUtil.getMaxValueIndex(data);
				game.move(Direction.getDirection(maxValueIndex));
				score = game.getScore();
				Main.waitMs(3);
			}
			LOG.info("Iteration {}   Punktezahl {}", i, score);
			average += score;
			if(highscore < score) highscore = score;
		}
		average /= Vars.ITERATIONS;
        LOG.info("Evaluation fertig, erreichte Höchstpunktezahl {}", highscore);	
        LOG.info("Durchschnitt {}", (average));
	}
	
	/**
	 * If no save path is given, it is saved in the same folder where the jar file is located
	 * @param millis Current Time Millis
	 * @param nanos	Nano Time
	 * @param conf	Type of save file
	 * @return <b>Path</b> 	</br>If config is true then returning the path ends with .conf </br> Otherwise the path ends with .zip
	 */
	private Path getPathToSave(long millis, long nanos, boolean conf) {
		savePath = (savePath != null) ? savePath : userDir;
		if(!Files.exists(savePath)) {
			try {
				Files.createDirectory(savePath);
			} catch (IOException e) {
				e.printStackTrace();
				savePath = userDir;
				}
		}
		if(conf)	return savePath.resolve("SnakeNet___Durch_" + average + "__Höchst_" + highscore + "__Zeit_" + millis + "_" + nanos +  ".conf");		
		else		return savePath.resolve("SnakeNet___Durch_" + average + "__Höchst_" + highscore + "__Zeit_" + millis + "_" + nanos +  ".zip");		
	}
}
