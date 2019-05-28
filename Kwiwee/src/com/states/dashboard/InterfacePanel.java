package com.states.dashboard;
import java.awt.LayoutManager;
import javax.swing.JPanel;

public abstract class InterfacePanel extends JPanel
{
	public InterfacePanel(LayoutManager layout) {
		super(layout);
	}
	
	public abstract void apply(JPanel panel);
}