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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.deeplearning4j.ui.api.UIServer;
import org.nd4j.linalg.learning.config.IUpdater;
import org.nd4j.linalg.learning.config.Sgd;

class AutoML {

	public static UIServer uiServer;
	
	private HashMap<Long, Parameter> param = new HashMap<>();

	private final long[] seed = {123L};
	private final int[] maxEpochStep = {20000};
	private final int[] maxStep = {20000, 25000, 30000, 35000};
	private final int[] expRepMaxSize = {150000};
	private final int[] batchSize = {128};
	private final int[] targetDqnUpdateFreq = {200};
	private final int[] updateStart = {15};
	private final double[] rewardFactor = {0.1};
	private final double[] gamma = {0.60};
	private final double[] errorClamp  = {1.0, 0.5};
	private final double[] minEpsilon = {0.05f};
	private final int[] epsilonNbStep = {5000};
	private final boolean[] doubleDQN = {true};
	private final double[] l2 = {0.0000325};
	private final IUpdater[] updater = {new Sgd(0.0009)};
	private final int[] numHiddenNodes = {256, 500};
	private final int[] numLayers = {3};
	
	/**
	 * Run a fixed number of games parallel
	 */
	AutoML(){
		this(Paths.get("G:", "Snake"));
	}

	/**
	 * Run a fixed number of games parallel 
	 * @param savePath The location where the trained net should be save
	 */
	AutoML(final Path savePath) {
		System.out.println(createParamList());
		ExecutorService executor = Executors.newFixedThreadPool(2);
		int delay = 0;
		for(Map.Entry<Long, Parameter> entry : param.entrySet()) {
			if(delay > 0 && delay < 5)	Main.waitMs(60000);
			executor.execute(() ->  new GameKI(savePath, entry.getValue()).trainNewModel());
			delay++;
		}
		executor.shutdown();
		try {
			while(!executor.awaitTermination(10, TimeUnit.SECONDS)) {
				
			}
		} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	/**
	 * Create all able parameter lists and store them randomly into a map
	 * @return size 	The size of the map
	 */
	private int createParamList() {
		Long rand;
		for (int a = 0; a < seed.length; a++) {
			for (int b = 0; b < maxEpochStep.length; b++) {
				for (int c = 0; c < maxStep.length; c++) {
					for (int d = 0; d < expRepMaxSize.length; d++) {
						for (int e = 0; e < batchSize.length; e++) {
							for (int f = 0; f < targetDqnUpdateFreq.length; f++) {
								for (int g = 0; g < updateStart.length; g++) {
									for (int h = 0; h < rewardFactor.length; h++) {
										for (int i = 0; i < gamma.length; i++) {
											for (int j = 0; j < errorClamp.length; j++) {
												for (int k = 0; k < minEpsilon.length; k++) {
													for (int l = 0; l < epsilonNbStep.length; l++) {
														for (int m = 0; m < doubleDQN.length; m++) {
															for (int n = 0; n < l2.length; n++) {
																for (int o = 0; o < updater.length; o++) {
																	for (int p = 0; p < numHiddenNodes.length; p++) {
																		for (int q = 0; q < numLayers.length; q++) {
																			do { 
																				rand =  new Random().nextLong();
																			}
																			while(param.containsKey(rand));
																			param.put(rand, new Parameter(
														        					seed[a], 											//	SEED
														        					maxEpochStep[b], 									//	MAX EPOCH STEP
														        					maxStep[c], 										//	MAX STEPS
														        					expRepMaxSize[d],									//	EXP REP MAX SIZE
														        					batchSize[e],										//	BATCH SIZE
														        					targetDqnUpdateFreq[f], 							//	TARGET DQN UPDATE FREQ
														        					updateStart[g], 									//	UPDATE START
														        					rewardFactor[h], 									//	REWARD FACTOR
														        					gamma[i], 											//	GAMMA
														        					errorClamp[j], 										//	ERROR CLAMP
														        					minEpsilon[k], 										//	MIN EPSILON
														        					epsilonNbStep[l],									//	EPSILON NB STEP
														        					doubleDQN[m],										//	DOUBLE DQN
														        					l2[n], 												//	L2
														        					updater[o],											//	UPDATER
														        					numHiddenNodes[p], 									//	NUM HIDDEN NODES
														        					numLayers[q] 										//	NUM LAYERS
														        					));
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return param.size();
	}
}
