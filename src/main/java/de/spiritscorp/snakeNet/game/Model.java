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
import java.util.Random;

import de.spiritscorp.snakeNet.game.util.Direction;

final class Model {

	private Direction direction = Direction.UP;
	private LinkedList<Snake> snake;
	private Food food;
	private final Controller controller;
	private boolean trainStatus = false; 
	private int playgroundSize;

	Model(final Controller controller) {
		this.controller = controller;
	}
		
	final void initGame(boolean trainStatus, int playgroundSize) {
		initSnake();
		food = createFood(trainStatus);
		this.playgroundSize = playgroundSize;
		this.trainStatus = trainStatus;
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
			food = createFood(trainStatus);
			snake.addLast(new Snake(xL, yL));
		}

		for(int i = 1; i < snake.size(); i++) {
			if(snake.getFirst().getBounds().intersects(snake.get(i).getBounds()))		return false;
		}
		
		if(snake.getFirst().getPosition().x < 0 || snake.getFirst().getPosition().x > playgroundSize
		   || snake.getFirst().getPosition().y < 0 || snake.getFirst().getPosition().y > playgroundSize)		return false;

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
	
	private Food createFood(boolean trainStatus) {
		Point p = null;
		boolean go = true;
		if(trainStatus) {
			int rand = new Random().nextInt();
			if((rand % 50) == 0) {	System.out.println("Treffer"); return new Food(20,20);		}
			if((rand % 36) == 0) {	System.out.println("Treffer"); return new Food(20, playgroundSize - 20);	}
			if((rand % 16) == 0) {	System.out.println("Treffer"); return new Food(playgroundSize - 20, playgroundSize -20);	}
			if((rand % 15) == 0) {	System.out.println("Treffer"); return new Food(playgroundSize - 20, 20);	}
		}
		do{
			Point p1 = new Point(normalize((int)((Math.random()* (playgroundSize - 40)) + 20 )), normalize((int)((Math.random()* (playgroundSize - 40)) + 20)));
			
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
		snake.add(new Snake((playgroundSize /2) - 5, (playgroundSize / 2) - 5));
		snake.add(new Snake((playgroundSize /2) - 5, (playgroundSize / 2) - 5 + 10));
		snake.add(new Snake((playgroundSize /2) - 5, (playgroundSize / 2) - 5 + 20));
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
