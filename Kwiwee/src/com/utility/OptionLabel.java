package com.utility;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.Callable;
import javax.swing.JLabel;

public class OptionLabel extends JLabel implements MouseListener
{
	private static final long serialVersionUID = 3440174835250357589L;
	private final static Color DEF_HOVER_COLOR = Color.ORANGE;
	private final static Color DEF_SELECT_COLOR = new Color(88, 178, 255);
	
	private Color originColor, hoverColor, selectColor;
	private Callable<?> func;
	private boolean clicked;
	
	public OptionLabel() {
		super();
		init();
	}
	
	public OptionLabel(String name) {
		super(name);
		init();
	}
	
	public OptionLabel(String name, Callable<Void> func) {
		this(name);
		this.func = func;
	}
	
	private void init() {
		addMouseListener(this);
		hoverColor = DEF_HOVER_COLOR;
		selectColor = DEF_SELECT_COLOR;
	}
	
	public void mouseEntered(MouseEvent arg0) {
		super.setForeground(hoverColor);
	}
	
	public void mouseExited(MouseEvent arg0) {
		if (!clicked) super.setForeground(originColor);
		else super.setForeground(selectColor);
	}

	public void mousePressed(MouseEvent arg0) {
		clicked = !clicked;
		super.setForeground(selectColor);
  		if (func != null) {
			try { func.call(); }
			catch (Exception e) { e.printStackTrace(); }
		}
	}

	public void setForeground(Color color) {
		super.setForeground(color);
		originColor = color;
	}
	
	public void release() {
		clicked = false;
		setForeground(originColor);
	}
	
	public Color getHoverColor() { return hoverColor; }
	public Color getSelectColor() { return selectColor; }
	public Callable<?> getFunction() { return func; }
	public void setFunction(Callable<?> f) { func = f; }
	public void setHoverColor(Color c) { hoverColor = c; }
	public void setSelectColor(Color c) { selectColor = c; }
	public boolean isClicked() { return clicked; }
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
}