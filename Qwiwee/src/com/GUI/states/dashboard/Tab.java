package com.GUI.states.dashboard;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import com.GUI.ColorConstants;
import com.GUI.MainWindow;

import javaNK.util.GUI.swing.components.InteractiveIcon;
import javaNK.util.files.FontHandler;
import javaNK.util.files.FontHandler.FontStyle;
import javaNK.util.files.ImageHandler;
import javaNK.util.math.DimensionalHandler;

public abstract class Tab extends JPanel
{
	protected static enum ActionButton
	{
		ADD, DELETE, SAVE;
		
		private static final String PATH = "tab_icons/";
		protected ImageIcon icon, hoverIcon;
		
		private ActionButton() {
			this.icon = ImageHandler.loadIcon(PATH + name().toLowerCase() + ".png");
			this.hoverIcon = ImageHandler.loadIcon(PATH + name().toLowerCase() + "_hover.png");
		}
	}
	
	private static final long serialVersionUID = 5140628779217675584L;
	public static final Dimension DIM = DimensionalHandler.adjust(MainWindow.DIM, 80, 100);
	protected static final Font DESCRIPTION_FONT = FontHandler.load("Poppins", FontStyle.PLAIN, 16);
	private static final Font TITLE_FONT = FontHandler.load("Ubuntu", FontStyle.PLAIN, 28);
	
	protected JPanel headerPanel, buttonPanel, panel;
	protected GridBagConstraints gbc;
	protected JTextPane textPane;
	protected List<InteractiveIcon> buttonList;
	
	public Tab() {
		super(new BorderLayout());
		setPreferredSize(DIM);
		
		this.gbc = new GridBagConstraints();
		this.buttonList = getButtonList();
		
		//north header panel
		this.headerPanel = new JPanel(new BorderLayout());
		headerPanel.setPreferredSize(DimensionalHandler.adjust(DIM, 100, 30));
		headerPanel.setOpaque(false);
		
		//title
		JPanel titlePanel = new JPanel(new GridBagLayout());
		titlePanel.setPreferredSize(DimensionalHandler.adjust(headerPanel.getPreferredSize(), 100, 50));
		titlePanel.setOpaque(false);
		headerPanel.add(titlePanel, BorderLayout.NORTH);
		
		JLabel title = new JLabel(getTitle());
		title.setForeground(ColorConstants.TEXT_COLOR_BRIGHT);
		title.setFont(TITLE_FONT);
		gbc.insets = new Insets(15, 10, 15, 10);
		addComponent(titlePanel, title, 0, 0);
		
		//description
		JPanel descriptionPanel = new JPanel(new GridBagLayout());
		descriptionPanel.setPreferredSize(DimensionalHandler.adjust(headerPanel.getPreferredSize(), 100, 50));
		descriptionPanel.setOpaque(false);
		headerPanel.add(descriptionPanel, BorderLayout.CENTER);
		
		gbc.insets = new Insets(-40, 10, 0, 10);
		String[] descStr = getDescription();
		JLabel[] description = new JLabel[descStr.length];
		for (int i = 0, y = 0; i < description.length; i++, y++) {
			description[i] = new JLabel(descStr[i]);
			description[i].setForeground(ColorConstants.COLOR_3);
			description[i].setFont(DESCRIPTION_FONT);
			addComponent(descriptionPanel, description[i], 0, y);
			
			if (i < description.length - 1)
				gbc.insets = new Insets(5, 10, 0, 10);
		}
		
		add(headerPanel, BorderLayout.NORTH);
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		//button panel
		this.buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.setPreferredSize(DimensionalHandler.adjust(DIM, 20, 70));
		buttonPanel.setOpaque(false);
		addButtons();
		add(buttonPanel, BorderLayout.EAST);
		
		//main panel
		this.panel = new JPanel(new GridBagLayout());
		panel.setPreferredSize(DimensionalHandler.adjust(DIM, 100, 70));
		panel.setOpaque(false);
		add(panel, BorderLayout.CENTER);
	}
	
	protected void addComponent(Component c, int x, int y) {
		addComponent(panel, c, x, y);
	}
	
	protected void addComponent(JPanel panel, Component c, int x, int y) {
		gbc.gridx = x;
		gbc.gridy = y;
		panel.add(c, gbc);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//headline
		int midWidth = DIM.width / 2;
		int space = 150, height = 55;;
		
		g.drawLine(0, height, midWidth - space, height);
		
		int n = 5;
		int[] xArr = new int[n], yArr = new int[n];
		
		xArr[0] = midWidth - space;
		yArr[0] = height;
		
		xArr[1] = midWidth - (int) (space / 2);
		yArr[1] = height - (int) (height / 2);
		
		xArr[2] = DIM.width;
		yArr[2] = height - (int) (height / 2);
		
		xArr[3] = DIM.width;
		yArr[3] = height + (int) (height / 2);
		
		xArr[4] = midWidth - (int) (space / 2);
		yArr[4] = height + (int) (height / 2);
		
		Polygon polygon = new Polygon(xArr, yArr, n);
		
		g.setColor(getTabColor().darker());
		g.drawPolygon(polygon);
		
		polygon.xpoints[0] = midWidth - space - 5;
		polygon.ypoints[1] = height - (int) (height / 2) - 1;
		polygon.ypoints[2] = height - (int) (height / 2) - 1;
		
		g.setColor(getTabColor());
		g.fillPolygon(polygon);
		
		//buttons
		int length = 70; 
		int x = DIM.width - 100;
		int heightJump = buttonPanel.getPreferredSize().height / 4;
		
		for (int i = 0; i < buttonList.size(); i++) {
			height = DIM.height - (i + 1) * heightJump;
			g.fillRect(x + 30, height + 1, 70, length - 1);
			g.fillOval(x, height, 70, length);
		}
	}
	
	private void addButtons() {
		//get the size of the list and return if it's 0
		int buttonsAmount = buttonList.size();
		if (buttonsAmount == 0) return;
		
		//locate the buttons in the tab
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		
		int buttonLength = 0;
		if (buttonsAmount > 0) buttonLength = buttonList.get(0).getIcon().getIconWidth();
		constraints.insets.left = buttonLength;
		constraints.insets.bottom = (int) (buttonLength * 0.91);
		
		int topPush = 0;
		if (buttonsAmount == 3) topPush = (int) (buttonLength * 2.07);
		else if (buttonsAmount >= 2) topPush = (int) (buttonLength * 3.95);
		else if (buttonsAmount >= 1) topPush = (int) (buttonLength * 5.88);
		
		//push from the top down
		constraints.insets.top = topPush;
		
		for (int i = 0; i < buttonsAmount; i++) {
			constraints.gridy = i;
			buttonPanel.add(buttonList.get(i), constraints);
			constraints.insets.top = 0;
		}
	}
	
	private List<InteractiveIcon> getButtonList() {
		List<InteractiveIcon> buttonList = new ArrayList<InteractiveIcon>();
		ActionButton actionButton;
		
		//add button should appear
		if (addButtonFunction(false)) {
			actionButton = ActionButton.ADD;
			InteractiveIcon addButton = new InteractiveIcon(actionButton.icon);
			addButton.setHoverIcon(actionButton.hoverIcon);
			addButton.setFunction(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					addButtonFunction(true);
					return null;
				}
			});
			
			buttonList.add(addButton);
		}
		
		//delete button should appear
		if (deleteButtonFunction(false)) {
			actionButton = ActionButton.DELETE;
			InteractiveIcon deleteButton = new InteractiveIcon(actionButton.icon);
			deleteButton.setHoverIcon(actionButton.hoverIcon);
			deleteButton.setFunction(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					deleteButtonFunction(true);
					return null;
				}
			});
			
			buttonList.add(deleteButton);
		}
		
		//save button should appear
		if (saveButtonFunction(false)) {
			actionButton = ActionButton.SAVE;
			InteractiveIcon saveButton = new InteractiveIcon(actionButton.icon);
			saveButton.setHoverIcon(actionButton.hoverIcon);
			saveButton.setFunction(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					saveButtonFunction(true);
					return null;
				}
			});
			
			buttonList.add(saveButton);
		}
		
		return buttonList;
	}
	
	/**
	 * @return the title of the tab.
	 */
	protected abstract String getTitle();
	
	/**
	 * @return the lines of the description of the tab.
	 */
	protected abstract String[] getDescription();
	
	/**
	 * @return the color of the title's tab.
	 */
	protected abstract Color getTabColor();
	
	/**
	 * The function to execute when clicking the add button.
	 * Run the function itself only if the argument 'activate' is true,
	 * otherwise, just return the boolean value that says if the button
	 * should appear or not.
	 * 
	 * @param activate - True to activate the function itself
	 * @return true if the button should appear on this tab, or false if it doesn't.
	 */
	protected abstract boolean addButtonFunction(boolean activate);
	
	/**
	 * The function to execute when clicking the delete button.
	 * Run the function itself only if the argument 'activate' is true,
	 * otherwise, just return the boolean value that says if the button
	 * should appear or not.
	 * 
	 * @param activate - True to activate the function itself
	 * @return true if the button should appear on the tab, or false if it doesn't.
	 */
	protected abstract boolean deleteButtonFunction(boolean activate);
	
	/**
	 * The function to execute when clicking the save button.
	 * Run the function itself only if the argument 'activate' is true,
	 * otherwise, just return the boolean value that says if the button
	 * should appear or not.
	 * 
	 * @param activate - True to activate the function itself
	 * @return true if the button should appear on the tab, or false if it doesn't.
	 */
	protected abstract boolean saveButtonFunction(boolean activate);
}