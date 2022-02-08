/*
 		Snake Net
 		
 	 	@author Tom Spirit
 	 	@date 2022
 		@version	0.9.1.0

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

public class Main {
	
	public static void main(String[] args) {
	//	Main.continueKI();
		Main.startKI();
	//	Main.autoML();
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
	
	/**
	 * TODO read the config and create the right parameter list
	 */
	private static void continueKI() {
		String networkName = "SnakeNet___Durch_65__Höchst_93__Zeit_1644189031349_105083574123500.zip";
		new GameKI().preTrainedModel(networkName);
	}
	
	private static void autoML() {
		new AutoML();
	}
}
