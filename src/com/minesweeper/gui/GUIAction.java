package com.minesweeper.gui;

import javax.swing.AbstractAction;

import processing.core.PApplet;

@SuppressWarnings("serial")
public abstract class GUIAction extends AbstractAction {

	PApplet pa;
	
	public GUIAction(PApplet pa) {
		this.pa = pa;
	}
	
}
