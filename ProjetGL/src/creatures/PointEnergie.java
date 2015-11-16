package creatures;

import static java.lang.Math.toDegrees;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import visual.IDrawable;

public class PointEnergie implements IDrawable {

	public static final int DEFAULT_SIZE = 100;
	public static final Color DEFAULT_COLOR = new Color(255, 0, 0, 128);
	
	public Point2D position;
	public int size;
	
	public PointEnergie(Point2D p, int size) {
		this.position = p;
		this.size = size;
	}
	
	@Override
	public Color getColor() {
		return DEFAULT_COLOR;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public void paint(Graphics2D g2) {
		// center the point
		g2.translate(position.getX(), position.getY());
		g2.fillOval(0, 0, 5, 5);
		// center the surrounding rectangle
		g2.translate(-size / 2, -size / 2);
		// center the arc
		// rotate towards the direction of our vector
		//g2.rotate(-direction, size / 2, size / 2);

		// useful for debugging
		// g2.drawRect(0, 0, size, size);

		// set the color
		g2.setColor(DEFAULT_COLOR);
		g2.fillOval(0, 0, size, size);
		// we need to do PI - FOV since we want to mirror the arc
		//g2.fillArc(0, 0, size, size, (int) toDegrees(-fieldOfView / 2),
		//		(int) toDegrees(fieldOfView));


	}

}
