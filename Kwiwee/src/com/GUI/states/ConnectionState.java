package com.GUI.states;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import com.GUI.ColorConstants;

import javaNK.util.GUI.swing.containers.Window;
import javaNK.util.GUI.swing.state_management.State;
import javaNK.util.files.FontHandler;
import javaNK.util.files.FontHandler.FontStyle;
import javaNK.util.math.Percentage;

public abstract class ConnectionState extends State
{
	protected final static Font TITLE_FONT = FontHandler.load("Ubuntu", FontStyle.PLAIN, 28);
	protected final static Font LABEL_FONT = FontHandler.load("Poppins", FontStyle.PLAIN, 16);
	
	protected JLabel emailLab, error, usernameLab, passwordLab, repeatLab;
	protected JTextField emailField, usernameField, passField, repeatField;
	protected JButton button;
	
	public ConnectionState(Window window) {
		super(window, 3);
		createPanel(new GridBagLayout(), Percentage.createDimension(window.getDimension(), 100, 20), null);
		createPanel(new GridBagLayout(), Percentage.createDimension(window.getDimension(), 100, 60), null);
		createPanel(new GridBagLayout(), Percentage.createDimension(window.getDimension(), 100, 20), null);
		
		//labels
		JLabel title = new JLabel(getTitle());
		title.setFont(TITLE_FONT);
		gridConstraints.insets = new Insets(10, 10, 60, -220);
		addComponent(0, title, 0, 0);
		
		this.error = new JLabel("-");
		error.setForeground(window.getColor());
		
		this.emailLab = new JLabel("Email:");
		emailLab.setForeground(ColorConstants.TEXT_COLOR_DARK);
		emailLab.setFont(LABEL_FONT);
		
		this.usernameLab = new JLabel("Username:");
		usernameLab.setForeground(ColorConstants.TEXT_COLOR_DARK);
		usernameLab.setFont(LABEL_FONT);
		
		this.passwordLab = new JLabel("Password:");
		passwordLab.setForeground(ColorConstants.TEXT_COLOR_DARK);
		passwordLab.setFont(LABEL_FONT);
		
		this.repeatLab = new JLabel("Repeat password:");
		repeatLab.setForeground(ColorConstants.TEXT_COLOR_DARK);
		repeatLab.setFont(LABEL_FONT);
		
		//text fields
		Dimension fieldDim = new Dimension(200, 30);
		Border border = new MatteBorder(1, 1, 1, 1, Color.BLACK);
		
		this.emailField = new JTextField();
		emailField.setPreferredSize(fieldDim);
		emailField.setBorder(border);
		
		this.usernameField = new JTextField();
		usernameField.setPreferredSize(fieldDim);
		usernameField.setBorder(border);
		
		this.passField = new JTextField();
		passField.setPreferredSize(fieldDim);
		passField.setBorder(border);
		
		this.repeatField = new JTextField();
		repeatField.setPreferredSize(fieldDim);
		repeatField.setBorder(border);
		
		//action button
		this.button = new JButton(getActionName());
		button.setPreferredSize(new Dimension(fieldDim.width / 2, fieldDim.height));
		button.addActionListener(getButtonAction());
	}
	
	@Override
	public void applyPanels() {
		insertPanel(0, BorderLayout.NORTH);
		insertPanel(1, BorderLayout.CENTER);
		insertPanel(2, BorderLayout.SOUTH);
	}
	
	protected void fail(String message) {
		error.setText(message);
		error.setForeground(Color.RED);
	}
	
	protected abstract String getTitle();
	protected abstract String getActionName();
	protected abstract ActionListener getButtonAction();
}