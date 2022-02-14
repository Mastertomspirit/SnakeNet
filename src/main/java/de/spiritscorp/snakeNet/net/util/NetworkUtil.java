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

package de.spiritscorp.snakeNet.net.util;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.api.TrainingListener;
import org.deeplearning4j.rl4j.learning.configuration.QLearningConfiguration;
import org.deeplearning4j.rl4j.network.configuration.DQNDenseNetworkConfiguration;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import de.spiritscorp.snakeNet.Parameter;

public final class NetworkUtil {
    /**
     * Number of neural network inputs.
     */
    public static final int NUMBER_OF_INPUTS = 3;
    /**
     * Lowest value of the observation (e.g. player will die -1, nothing will happen 0, will move closer to the food 1)
     */
    public static final double LOW_VALUE = -15;
    /**
     * Highest value of the observation (e.g. player will die -1, nothing will happen 0, will move closer to the food 1)
     */
    public static final double HIGH_VALUE = 24.53362301587302;
    
    private static Parameter param = null;

    private NetworkUtil() {}

    public static QLearningConfiguration buildConfig() {
        return (param == null)	?	QLearningConfiguration.builder().build()	:
        		QLearningConfiguration.builder()
                .seed(param.getSeed())
                .maxEpochStep(param.getMaxEpochStep())
                .maxStep(param.getMaxStep())
                .expRepMaxSize(param.getExpRepMaxSize())
                .batchSize(param.getBatchSize())
                .targetDqnUpdateFreq(param.getTargetDqnUpdateFreq())
                .updateStart(param.getUpdateStart())
                .rewardFactor(param.getRewardFactor())
                .gamma(param.getGamma())
                .errorClamp(param.getErrorClamp())
                .minEpsilon(param.getMinEpsilon())
                .epsilonNbStep(param.getEpsilonNbStep())
                .doubleDQN(param.isDoubleDQN())
                .build();
    }

    public static DQNFactoryStdDense buildDQNFactory(Collection<TrainingListener> listener) {
        return (param == null)	?	new DQNFactoryStdDense(DQNDenseNetworkConfiguration.builder().build())	:
        							new DQNFactoryStdDense(DQNDenseNetworkConfiguration.builder()
        					                .l2(param.getL2())
        					                .updater(param.getUpdater())
        					                .numHiddenNodes(param.getNumHiddenNodes())
        					                .numLayers(param.getNumLayers())
        					                .listeners(listener)
        					                .build());
    }
    
    public static DQNFactoryStdDense buildDQNFactory() {
        return (param == null)	?	new DQNFactoryStdDense(DQNDenseNetworkConfiguration.builder().build())	:
			new DQNFactoryStdDense(DQNDenseNetworkConfiguration.builder()
	                .l2(param.getL2())
	                .updater(param.getUpdater())
	                .numHiddenNodes(param.getNumHiddenNodes())
	                .numLayers(param.getNumLayers())
	                .build());
    }
    
    public static MultiLayerNetwork loadNetwork(final String networkName, boolean train) {
        try {
            return MultiLayerNetwork.load(new File(networkName), train);
        } catch (IOException e) {e.printStackTrace();}
        return null;
    }

    public static void setParam(Parameter param) {
    	NetworkUtil.param = param;
    }
}
