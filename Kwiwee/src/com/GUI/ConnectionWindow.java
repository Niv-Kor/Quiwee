package com.GUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javaNK.util.GUI.swing.containers.Window;

public class ConnectionWindow extends Window
{
	private static final long serialVersionUID = 8161943524725746352L;
	private static final Dimension DIM = new Dimension(450, 400);
	
	public ConnectionWindow() {
		super(MainWindow.TITLE);
		setVisible(false);
	}

	@Override
	public Color getColor() { return ColorConstants.BASE_COLOR; }

	@Override
	public Dimension getDimension() { return DIM; }

	@Override
	protected boolean DisplayRightAway() { return false; }

	@Override
	protected void paintMainPanel(Graphics g) {
		g.setColor(new Color(151, 180, 200));
		g.fillRect(0, 0, 10, DIM.height);
		
		g.setColor(new Color(175, 199, 215));
		g.fillRect(0, 30, DIM.width, 50);
	}
}
