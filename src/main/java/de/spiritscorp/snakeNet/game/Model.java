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

import java.awt.Point;
import java.util.LinkedList;

import de.spiritscorp.snakeNet.game.util.Direction;

final class Model {

	private Direction direction = Direction.UP;
	private LinkedList<Snake> snake;
	private Food food;
	private final Controller controller;

	Model(final Controller controller) {
		this.controller = controller;
	}
		
	final void initGame() {
		initSnake();
		food = createFood();
		direction = Direction.UP;
	}
	
	final boolean runStep() {
		int x = snake.getFirst().getPosition().x;
		int y = snake.getFirst().getPosition().y;
		int xL = snake.getLast().getPosition().x;
		int yL = snake.getLast().getPosition().y;
		
		if(direction == Direction.DOWN)		{
			snake.addFirst(snake.getLast());
			snake.getFirst().updatePosition(x, y + 10); 
		}
		else if(direction == Direction.UP)		{
			snake.addFirst(snake.getLast());
			snake.getFirst().updatePosition(x, y - 10); 
		}
		else if(direction == Direction.LEFT)		{
			snake.addFirst(snake.getLast());
			snake.getFirst().updatePosition(x - 10, y);
		}
		else if(direction == Direction.RIGHT)	{
			snake.addFirst(snake.getLast());
			snake.getFirst().updatePosition(x + 10, y); 
		}
		snake.removeLast();
		
		if(snake.getFirst().getBounds().intersects(food.getBounds()))		{
			food = createFood();
			snake.addLast(new Snake(xL, yL));
		}

		for(int i = 1; i < snake.size(); i++) {
			if(snake.getFirst().getBounds().intersects(snake.get(i).getBounds()))		return false;
		}
		
		if(snake.getFirst().getPosition().x - 10 < 0 || snake.getFirst().getPosition().x + 30 > Controller.GAME_WIDTH
		   || snake.getFirst().getPosition().y - 10 < 0 || snake.getFirst().getPosition().y + 60 > Controller.GAME_HEIGHT)		return false;

		controller.updateFrame();
		return true;
	}
	
	final void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	final LinkedList<Snake> getSnake(){
		return snake;
	}
	final Food getFood() {
		return food;
	}
	
	final Direction getDirection() {
		return direction;
	}
	
	private Food createFood() {
		Point p = null;
		boolean go = true;
		do{
			Point p1 = new Point(normalize((int)((Math.random()* (Controller.GAME_WIDTH - 40)) + 10 )), normalize((int)((Math.random()* (Controller.GAME_HEIGHT - 70)) + 10)));
			
			if(!snake.stream().anyMatch((s) -> s.getPosition().equals(p1)))		{
				go = false;
				p = p1;
			}
		}
		while(go);
		return new Food(p.x, p.y);
	}
	
	private void initSnake() {
		snake = new LinkedList<>();
		snake.add(new Snake((Controller.GAME_WIDTH/2) - 5, (Controller.GAME_HEIGHT / 2) - 5));
		snake.add(new Snake((Controller.GAME_WIDTH/2) - 5, (Controller.GAME_HEIGHT / 2) - 5 + 10));
		snake.add(new Snake((Controller.GAME_WIDTH/2) - 5, (Controller.GAME_HEIGHT / 2) - 5 + 20));
	}
	
	private int normalize(int coordinate) {
		int x = coordinate % 5;
		int y = coordinate / 5;
		
		int res;
		
		if((y % 2) == 0) res = coordinate + 5 - x;
		else 	res = coordinate - x;
		return res;
	}
}
