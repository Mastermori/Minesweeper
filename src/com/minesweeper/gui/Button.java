package com.minesweeper.gui;

import processing.core.PApplet;

public class Button extends GUIElement{
	
	protected String text;
	
	public Button(String text, int x, int y, int width, int height) {
		super(x, y, width, height, true);
		this.text = text;
	}
	
	public Button(String text, int x, int y, int width, int height, GUIAction action) {
		super(x, y, width, height, true);
		this.text = text;
		setAction(action);
	}
	
	@Override
	public void draw(PApplet pa) {
		pa.fill(70);
		pa.rect(x, y, width, height);
		pa.fill(0);
		pa.text(text, x+width/2, y+height-32/2);
	}
	
}
