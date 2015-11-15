package creatures.visual;

import java.awt.Color;

import creatures.IColorStrategy;

public class ColorUnic implements IColorStrategy {

	private Color color;
	
	public ColorUnic(Color c) {
		this.color = c;
	}
	
	@Override
	public Color getColor() {
		return color;
	}

}
