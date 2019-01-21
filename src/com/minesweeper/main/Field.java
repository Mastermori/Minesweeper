package com.minesweeper.main;

import processing.core.PApplet;

public class Field {
	
	public static int fieldSize = 32;
	
	protected int x, y;
	protected int bombCount;
	protected boolean bomb;
	protected boolean visible;
	protected boolean defused;
	protected boolean flagged;
	
	public Field(int x, int y) {
		this.x = x;
		this.y = y;
		bombCount = 0;
		bomb = false;
		visible = false;
		flagged = false;
	}
	
	public void addNearBomb() {
		bombCount++;
	}
	
	public void draw(PApplet pa) {
		pa.fill(pa.color(visible?255:125));
		if(flagged)
			pa.fill(50);
		if(bomb && visible)
			pa.fill(220, 0, 0);
		if(defused)
			pa.fill(0, 220, 0);
		pa.rect(x*fieldSize+1, y*fieldSize+1, fieldSize-1, fieldSize-1);
		if(flagged) {
			pa.fill(255, 0, 10);
			pa.rect(x*fieldSize+fieldSize/8*2f, y*fieldSize+fieldSize/8*6, fieldSize-fieldSize/8*4, 3);
			pa.rect(x*fieldSize+fieldSize/2-2, y*fieldSize+fieldSize/8, 4, fieldSize-fieldSize/8*2-4);
			pa.triangle(x*fieldSize+fieldSize/2-2, y*fieldSize+fieldSize/8, x*fieldSize+fieldSize/2-2, y*fieldSize+fieldSize/8*4, x*fieldSize+fieldSize/8, y*fieldSize+fieldSize/8*2.5f);
		}
		if(visible && bombCount > 0 && !bomb){
			switch(bombCount) {
			case 1:
				pa.fill(0, 50, 255);
				break;
			case 2:
				pa.fill(0, 128, 0);
				break;
			case 3:
				pa.fill(255, 0, 0);
				break;
			case 4:
				pa.fill(25, 25, 200);
				break;
			default:
				pa.fill(205, 47, 47);
			}
			pa.text(bombCount, x*fieldSize+fieldSize/2-5, y*fieldSize+fieldSize/2+5);
		}
	}
	

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public int getBombCount() {
		return bombCount;
	}
	public void setBombCount(int bombCount) {
		this.bombCount = bombCount;
	}

	public boolean isBomb() {
		return bomb;
	}
	public void setBomb(boolean bomb) {
		this.bomb = bomb;
	}

	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isFlagged() {
		return flagged;
	}
	public void setFlagged(boolean flagged) {
		this.flagged = flagged;
		if(flagged) {
			Main.pa.map.addFlag(this);
		}else {
			Main.pa.map.removeFlag(this);
		}
	}

	public boolean isDefused() {
		return defused;
	}
	public void setDefused(boolean defused) {
		this.defused = defused;
	}

}
