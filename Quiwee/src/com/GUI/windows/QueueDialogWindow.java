package com.GUI.windows;
import java.awt.Color;
import java.awt.Dimension;
import com.GUI.constants.ColorConstants;
import javaNK.util.GUI.swing.containers.Window;

public class QueueDialogWindow extends Window
{
	private static final long serialVersionUID = 5453756687755761238L;
	private static final Dimension DIM = new Dimension(500, 300);
	
	public QueueDialogWindow() {
		super("new queue");
	}

	@Override
	public Dimension getDimension() { return DIM; }

	@Override
	public Color getColor() { return ColorConstants.BASE_COLOR; }

	@Override
	protected boolean DisplayRightAway() { return false; }
}