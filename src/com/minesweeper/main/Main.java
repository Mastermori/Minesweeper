package com.minesweeper.main;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.minesweeper.gui.Button;
import com.minesweeper.gui.GUIAction;
import com.minesweeper.gui.GUIElement;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Main extends PApplet{
	
	public static Main pa;
	public static int xOff = 0, yOff = 0;
	public static final int WINDOWWIDTH = 928 + 4, WINDOWHEIGHT = 608 + 3 + 18;
	
	public Map map;
	
	protected PFont font;
	protected PImage gradient;
	
	public boolean started;
	public boolean active;
	public boolean bombsPlaced;
	
	public float difficulty;
	public static float difMin, difMax;
	
	public static List<GUIElement> gui;
	public File highscoreFile;
	public List<Integer> highscores;
	public int timer;
	protected int deltaTimer;
	protected int lastDelta;
	
	public static void main(String[] args) {
		PApplet.main("com.minesweeper.main.Main");
	}
	
	public Main() {
		pa = this;
		highscores = new ArrayList<Integer>();
		readHighscores();
		//restart();
	}
	
	public void readHighscores() {
		try {
			highscoreFile = new File("highscores.txt");
			if(!highscoreFile.exists())
				return;
			BufferedReader in = new BufferedReader(new FileReader(highscoreFile));
			String line;
			while(in.ready() && (line = in.readLine()) != null) {
				highscores.add(Integer.parseInt(line));
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeHighscores() {
		try {
			highscoreFile = new File("highscores.txt");
			if(!highscoreFile.exists())
				highscoreFile.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(highscoreFile));
			for(int h : highscores) {
				out.write(h + "\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void restart() {
		map = new Map(WINDOWWIDTH/Field.fieldSize, WINDOWHEIGHT/Field.fieldSize);
		xOff = (WINDOWWIDTH - (map.getWidth() * Field.fieldSize))/2-1;
		yOff = (WINDOWHEIGHT - (map.getHeight() * Field.fieldSize))/2+9;
		bombsPlaced = false;
		active = true;
		started = true;
		difficulty = pa.random(difMin / 100f, difMax / 100f);
		System.out.println("Difficulty: " + difficulty);
		textSize(16);
		timer = 0;
		deltaTimer = 0;
		//difficulty = random(0.15f, 0.5f);
		//difficulty = 0.01f;
	}
	
	public void setDifficulty(int min, int max) {
		difMin = min;
		difMax = max;
	}
	
	public void settings() {
		size(WINDOWWIDTH, WINDOWHEIGHT);
	}
	
	public void setup() {
		font = createFont("Arial", 16, true);
		textFont(font, 16);
		gui = new ArrayList<GUIElement>();
		gui.add(new Button("BABY", width/8*3, height/3, 200, 50, new GUIAction(this) {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.pa.setDifficulty(2, 7);
				Main.pa.restart();
			}
		}));
		gui.add(new Button("Easy", width/8, height/2, 200, 50, new GUIAction(this) {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.pa.setDifficulty(7, 12);
				Main.pa.restart();
			}
		}));
		gui.add(new Button("Medium", width/8*3, height/2, 200, 50, new GUIAction(this) {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.pa.setDifficulty(12, 17);
				Main.pa.restart();
			}
		}));
		gui.add(new Button("Hard", width/8*5, height/2, 200, 50, new GUIAction(this) {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.pa.setDifficulty(17, 27);
				Main.pa.restart();
			}
		}));
		gui.add(new Button("X-TREME", width/8*3, height/3*2, 200, 50, new GUIAction(this) {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.pa.setDifficulty(27, 35);
				Main.pa.restart();
			}
		}));
		gradient = loadImage("gradient_b-w.png");
		stopMap();
	}
	
	public void stopMap() {
		textSize(32);
		textAlign(CENTER);
		started = false;
	}
	
	public void draw() {
		if(started) {
			background(0);
			translate(xOff, yOff);
			map.draw(this);
			translate(-xOff, -yOff);
			if(bombsPlaced) {
				if(active) {
					deltaTimer += millis() - lastDelta;
					lastDelta = millis();
					timer = deltaTimer / 1000;
				}
				fill(255);
				textAlign(CENTER);
				int s = timer % 60;
				int m = timer / 60;
				text((m < 10 ? "0" : "") + m + ":" + (s < 10 ? "0" : "") + s, width/3, 17);
				fill(10, 255, 10);
				text(map.getFlagCount() + "/" + map.getBombCount(), width/2, 17);
				textAlign(LEFT);
			}
		}else {
			int[] top = new int[5];
			for(int i = 0; i < top.length; i++) { top[i] = -1; }
			for(int h : highscores) {
				for(int i = 0; i < top.length; i++) {
					if(h < top[i] || top[i] == -1) {
						moveTop(top, h, i);
						top[i] = h;
						break;
					}
				}
			}
			image(gradient, 0, 0);
			for(int i = 0; i < top.length; i++) {
				if(top[i] == -1)
					continue;
				int m = top[i] / 60;
				int s = top[i] % 60;
				text((i+1) + ". " + (m < 10 ? "0" : "") + m + ":" + (s < 10 ? "0" : "") + s, width/8, (height/3)/top.length*i+height/10);
			}
			for(GUIElement e : gui) {
				e.draw(this);
			}
		}
	}
	
	public void moveTop(int[] top, int next, int startI) {
		for(int i = startI; i < top.length; i++) {
			int temp = top[i];
			top[i] = next;
			next = temp;
		}
	}
	
	public void endGame(boolean won) {
		map.revealBombs(won);
		if(won) {
			highscores.add(timer);
			writeHighscores();
		}
		active = false;
	}
	
	public void showField(Field f) {
		if(!bombsPlaced) {
			map.generateBombs((int) ((map.getHeight() * map.getWidth()-9) * difficulty), f.getX(), f.getY());
			bombsPlaced = true;
			lastDelta = millis();
		}
		if(!f.isFlagged() && !f.isVisible()) {
			if(f.isBomb()){
				endGame(false);
				//exit();
			}
			map.revealField(f.getX(), f.getY());
		}
	}
	
	public void checkWin() {
		if(active && map.checkWin()) {
			endGame(true);
			System.out.println("WON!");
		}
	}
	
	public void mouseClicked() {
		if(started) {
			Field f = map.getField((mouseX-xOff)/Field.fieldSize, (mouseY-yOff)/Field.fieldSize);
			if(mouseButton == LEFT && active){
				showField(f);
			}else if(mouseButton == RIGHT && active && !f.isVisible()){
				f.setFlagged(!f.isFlagged());
			}
			if(active && bombsPlaced) {
				checkWin();
			}
		}else {
			for(GUIElement e : GUIElement.clickables) {
				if(e.isVisible() && e.overLapping(mouseX, mouseY)) {
					e.click(this);
				}
			}
		}
	}
	
	public void keyPressed() {
		if(started) {
			Field f = map.getField((mouseX-xOff)/Field.fieldSize, (mouseY-yOff)/Field.fieldSize);
			if(key == 'r') {
				restart();
				return;
			}else if(key == 'f' && active && !f.isVisible()) {
				f.setFlagged(!f.isFlagged());
			}else if(key == ' ' && active) {
				showField(f);
			}else if(key == 'l' && bombsPlaced && active) {
				endGame(false);
			}else if(key == 'x') {
				stopMap();
			}
			if(active && bombsPlaced) {
				checkWin();
			}
		}
	}
	
}
