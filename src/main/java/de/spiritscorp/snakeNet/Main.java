/*
 		Snake Net
 		
 	 	@author Tom Spirit
 	 	@date 2022
 		@version	1.2.0.0

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

import org.nd4j.linalg.learning.config.Sgd;

public class Main {
	
	public static void main(String[] args) {
		Main.evaluateKI();
//		Main.continueKI();
//		Main.startKI();
//		Main.autoML();
	}
	
	public static void waitMs(long ms) {
		if(ms == 0)		return;
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {e.printStackTrace();}
	}

	private static void startKI() {
		new GameKI().trainNewModel();
	}
	
	private static void evaluateKI() {
		new GameKI().testNetwork("SnakeNet___Durch_93__Höchst_122__Zeit_1644561391214_120100000982300.zip");
	}
	
	/**
	 * TODO read the config and create the right parameter list
	 * 		You must change the learning rate manually in the config file, into the zip file. For each layer
	 */
	private static void continueKI() {
		String networkName = "SnakeNet___Durch_93__Höchst_122__Zeit_1644561391214_120100000982300.zip";
		new GameKI(new Parameter(123L, 7000, 17000, 150000, 128, 200, 20, 0.1, 0.75, 1.0, 0.05, 0, true, 0.00000325, new Sgd(0.000001), 256, 3)).preTrainedModel(networkName);
	}
	
	private static void autoML() {
		new AutoML();
	}
}
