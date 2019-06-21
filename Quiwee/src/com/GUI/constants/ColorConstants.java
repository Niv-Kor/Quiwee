package com.GUI.constants;
import java.awt.Color;
import javaNK.util.math.RNG;

final public class ColorConstants
{
	public final static Color BASE_COLOR = new Color(225, 230, 233);
	public final static Color COLOR_2 = new Color(48, 179, 209);
	public final static Color COLOR_3 = new Color(55, 73, 88);
	public final static Color TEXT_COLOR_DARK = Color.BLACK;
	public final static Color TEXT_COLOR_BRIGHT = Color.WHITE;
	public final static Color TEXT_COLOR_SELECTED = new Color(255, 200, 0);
	
	public final static Color generate(Integer red, Integer green, Integer blue, Integer alpha) {
		int genRed = (red != null) ? red : RNG.generate(0x0, 0xFF);
		int genGrn = (green != null) ? green : RNG.generate(0x0, 0xFF);
		int genBlu = (blue != null) ? blue : RNG.generate(0x0, 0xFF);
		int genAlp = (alpha != null) ? alpha : RNG.generate(0x0, 0xFF);
		return new Color(genRed, genGrn, genBlu, genAlp);
	}
	
	public final static Color generate(Integer red, Integer green, Integer blue) {
		return generate(red, green, blue, 0xFF);
	}
}