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

import org.nd4j.linalg.learning.config.IUpdater;

public class Parameter {

	private final long seed;
	private final int maxEpochStep;
	private final int maxStep;
	private final int expRepMaxSize;
	private final int batchSize;
	private final int targetDqnUpdateFreq;
	private final int updateStart;
	private final double rewardFactor;
	private final double gamma;
	private final double errorClamp;
	private final double minEpsilon;
	private final int epsilonNbStep;
	private final boolean doubleDQN;
	private final double l2;
	private final IUpdater updater;
	private final int numHiddenNodes;
	private final int numLayers;
	
	Parameter(long seed, int maxEpochStep, int maxStep, int expRepMaxSize, int batchSize, int targetDqnUpdateFreq, int updateStart, 
			double rewardFactor, double gamma, double errorClamp, double minEpsilon, int epsilonNbStep, boolean doubleDQN, double l2, 
			IUpdater updater, int numHiddenNodes, int numLayers) {
		this.seed = seed;
		this.maxEpochStep = maxEpochStep;
		this.maxStep = maxStep;
		this.expRepMaxSize = expRepMaxSize;
		this.batchSize = batchSize;
		this.targetDqnUpdateFreq = targetDqnUpdateFreq;
		this.updateStart = updateStart;
		this.rewardFactor = rewardFactor;
		this.gamma = gamma;
		this.errorClamp = errorClamp;
		this.minEpsilon = minEpsilon;
		this.epsilonNbStep = epsilonNbStep;
		this.doubleDQN = doubleDQN;
		this.l2 = l2;
		this.updater = updater;
		this.numHiddenNodes = numHiddenNodes;
		this.numLayers = numLayers;
	}

	public long getSeed() {
		return seed;
	}

	public int getMaxEpochStep() {
		return maxEpochStep;
	}

	public int getMaxStep() {
		return maxStep;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public int getTargetDqnUpdateFreq() {
		return targetDqnUpdateFreq;
	}

	public int getUpdateStart() {
		return updateStart;
	}

	public double getRewardFactor() {
		return rewardFactor;
	}
	
	public double getGamma() {
		return gamma;
	}

	public double getErrorClamp() {
		return errorClamp;
	}

	public double getMinEpsilon() {
		return minEpsilon;
	}

	public int getEpsilonNbStep() {
		return epsilonNbStep;
	}

	public boolean isDoubleDQN() {
		return doubleDQN;
	}

	public double getL2() {
		return l2;
	}

	public IUpdater getUpdater() {
		return updater;
	}

	public int getNumHiddenNodes() {
		return numHiddenNodes;
	}

	public int getNumLayers() {
		return numLayers;
	}

	public int getExpRepMaxSize() {
		return expRepMaxSize;
	}
}
