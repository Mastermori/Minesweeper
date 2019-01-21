package com.minesweeper.gui;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

public abstract class GUIElement {
	
	int x, y;
	int width, height;
	boolean clickable;
	boolean visible;
	protected GUIAction action;
	public static List<GUIElement> clickables = new ArrayList<GUIElement>();
	
	public GUIElement(int x, int y, int width, int height, boolean clickable) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.clickable = clickable;
		if(clickable)
			clickables.add(this);
		visible = true;
	}
	
	public abstract void draw(PApplet pa);
	
	public void click(PApplet pa) {
		action.actionPerformed(null);
	}
	
	public void setAction(GUIAction action) {
		this.action = action;
	}
	
	public boolean overLapping(int x, int y) {
		if(x > this.x && y > this.y && x < this.x + width && y < this.y + height) {
			return true;
		}
		return false;
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

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isClickable() {
		return clickable;
	}
	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}

	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	
	
}
