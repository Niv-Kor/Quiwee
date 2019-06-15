package com.GUI.states.dashboard;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JTextField;
import com.GUI.ColorConstants;

public class SettingsTab extends Tab
{
	private static final long serialVersionUID = -2232661139446087215L;
	private static final Dimension FIELD_DIM = new Dimension(180, 25);
	
	public SettingsTab() {
		//labels
		gbc.insets = new Insets(10, -150, 10, 10);
		
		JLabel currentPassLab = new JLabel("Current password");
		currentPassLab.setForeground(ColorConstants.TEXT_COLOR_DARK);
		currentPassLab.setFont(DESCRIPTION_FONT);
		addComponent(currentPassLab, 0, 0);
		
		JLabel newPassLab = new JLabel("New password");
		newPassLab.setForeground(ColorConstants.TEXT_COLOR_DARK);
		newPassLab.setFont(DESCRIPTION_FONT);
		addComponent(newPassLab, 0, 1);
		
		JLabel repeatPassLab = new JLabel("Repeat password");
		repeatPassLab.setForeground(ColorConstants.TEXT_COLOR_DARK);
		repeatPassLab.setFont(DESCRIPTION_FONT);
		addComponent(repeatPassLab, 0, 2);
		
		JLabel newUsernameLab = new JLabel("New username");
		newUsernameLab.setForeground(ColorConstants.TEXT_COLOR_DARK);
		newUsernameLab.setFont(DESCRIPTION_FONT);
		gbc.insets.bottom = 250;
		addComponent(newUsernameLab, 0, 3);
		
		//text fields
		gbc.insets = new Insets(10, 10, 10, 10);
		
		JTextField currentPassF = new JTextField();
		currentPassF.setPreferredSize(FIELD_DIM);
		addComponent(currentPassF, 1, 0);
		
		JTextField newPassF = new JTextField();
		newPassF.setPreferredSize(FIELD_DIM);
		addComponent(newPassF, 1, 1);
		
		JTextField repeatPassF = new JTextField();
		repeatPassF.setPreferredSize(FIELD_DIM);
		addComponent(repeatPassF, 1, 2);

		JTextField newUsernameF = new JTextField();
		newUsernameF.setPreferredSize(FIELD_DIM);
		gbc.insets.bottom = 250;
		addComponent(newUsernameF, 1, 3);
	}
	
	@Override
	protected String getTitle() { return "Settings"; }

	@Override
	protected String[] getDescription() {
		return new String[] {
			"Update your personal information and preferences."
		};
	}

	@Override
	protected Color getTabColor() { return new Color(167, 54, 54); }

	@Override
	protected boolean addButtonFunction(boolean activate) { return false; }

	@Override
	protected boolean deleteButtonFunction(boolean activate) { return false; }

	@Override
	protected boolean saveButtonFunction(boolean activate) {
		// TODO Auto-generated method stub
		return true;
	}
}