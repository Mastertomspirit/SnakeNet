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

import java.awt.Color;
import javax.swing.JFrame;

final class View extends JFrame {

	private static final long serialVersionUID = 7845336403267449229L;
	private final Frame frame;
	
	View(final Controller controller){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Controller.GAME_WIDTH + 30, 
				Controller.GAME_HEIGHT + 60);
		setLocationRelativeTo(null);
		setBackground(Color.BLACK);
		setResizable(false);
		
		frame = new Frame(controller);
		setContentPane(frame);
				
		setVisible(true);	
	}
	
	void update() {
		frame.repaint();
		repaint();
	}
}
