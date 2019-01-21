package com.minesweeper.main;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

public class Map {
	
	protected Field[][] map;
	
	protected List<Field> bombs;
	protected List<Field> flags;
	
	protected int fW, fH;
	
	public Map(int width, int height) {
		map = new Field[width][height];
		bombs = new ArrayList<Field>();
		flags = new ArrayList<Field>();
		fW = width;
		fH = height;
		for(int i = 0; i < fW; i++) {
			for(int j = 0; j < fH; j++) {
				map[i][j] = new Field(i, j);
			}
		}
	}
	
	public void revealBombs(boolean defuse) {
		for(Field[] fA : map) {
			for(Field f : fA) {
				if(f.isBomb()) {
					f.setDefused(defuse);
					f.setVisible(true);
				}
			}
		}
	}
	
	public void draw(PApplet pa) {
		for(Field[] fA : map) {
			for(Field f : fA) {
				f.draw(pa);
			}
		}
	}
	
	public void addFlag(Field f) {
		flags.add(f);
	}
	
	public void removeFlag(Field f) {
		flags.remove(f);
	}
	
	public Field getField(int x, int y) {
		if(x >= 0 && y >= 0 && x < fW && y < fH)
			return map[x][y];
		else
			return null;
	}
	
	public void generateBombs(int amount, int x, int y) {
		List<Field> spots = new ArrayList<Field>();
		for(Field[] fA : map) {
			for(Field f : fA) {
				if(PApplet.abs(f.getX()-x) <= 1 && PApplet.abs(f.getY()-y) <= 1)
					continue;
				spots.add(f);
			}
		}
		while(amount > 0 && spots.size() > 0) {
			int random = (int) Main.pa.random(0, spots.size());
			setBomb(spots.get(random));
			spots.remove(random);
			amount--;
		}
	}
	
	public int getFlagCount() {
		return flags.size();
	}
	
	public int getBombCount() {
		return bombs.size();
	}
	
	public void setBomb(Field f) {
		setBomb(f.getX(), f.getY());
	}
	
	public void setBomb(int x, int y) {
		if(x < 0 || y < 0 || x >= fW || y >= fH)
			System.err.println("Trying to set bomb out of map range");
		
		Field f = getField(x, y);
		
		if(f.isBomb())
			return;
		
		f.setBomb(true);
		bombs.add(f);
		
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				markBomb(x+i, y+j);
			}
		}
	}
	
	public void markBomb(int x, int y) {
		if(x < 0 || y < 0 || x >= fW || y >= fH)
			return; //Return if out of range
		getField(x, y).addNearBomb();
	}
	
	public void revealField(Field f) {
		revealField(f.getX(), f.getY());
	}
	
	public void revealField(int x, int y) {
		if(x < 0 || y < 0 || x >= fW || y >= fH)
			return; //Break recursion if out of range
		
		Field f = getField(x, y);
		
		if(f.isVisible())
			return; //Break recursion if already revealed
		if(f.isBomb())
			return; //Break recursion if bomb (shouldn't be revealed)
		if(f.isFlagged())
			return; //Break recursion if its flagged (shoudln't be revealed)
		
		f.setVisible(true);
		
		if(f.getBombCount() > 0)
			return; //Don't reveal fields around marked fields
		
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				revealField(x + i, y + j);
			}
		}
	}
	
	public boolean checkWin() {
		boolean bWin = true;
		boolean fWin = true;
		for(Field[] fA : map) {
			for(Field f : fA) {
				if(f.isBomb() && !f.isFlagged())
					bWin =  false;
				if(!f.isBomb() && f.isFlagged())
					bWin = false;
				if(!f.isBomb() && !f.isVisible())
					fWin = false;
			}
		}
		return bWin || fWin;
	}
	
	
	public int getWidth() {
		return fW;
	}
	
	public int getHeight() {
		return fH;
	}
	
}
