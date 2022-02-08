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

import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import de.spiritscorp.snakeNet.net.util.GameState;
import de.spiritscorp.snakeNet.net.util.NetworkUtil;

public class GameObservationSpace implements ObservationSpace<GameState>{

	private static final double[] LOWS = GameObservationSpace.createValueArray(NetworkUtil.LOW_VALUE);
	private static final double[] HIGHS = GameObservationSpace.createValueArray(NetworkUtil.HIGH_VALUE);
	
	
	@Override
	public String getName() {
		return "GameObservationSpace";
	}

	@Override
	public int[] getShape() {
		return new int[] {
				1, NetworkUtil.NUMBER_OF_INPUTS
		};
	}

	@Override
	public INDArray getLow() {
		return Nd4j.create(LOWS);
	}

	@Override
	public INDArray getHigh() {
		return Nd4j.create(HIGHS);
	}

	private static double[] createValueArray(double value) {
		double[] values = new double[NetworkUtil.NUMBER_OF_INPUTS];
		for(int i = 0; i< NetworkUtil.NUMBER_OF_INPUTS; i++) {
			values[i] = value;
		}
		return values;
	}
}
