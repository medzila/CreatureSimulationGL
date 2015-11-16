package creatures;

import java.awt.Color;

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
