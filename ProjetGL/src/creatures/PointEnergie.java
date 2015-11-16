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
	
	public PointEnergie(Point2D p) {
		this.position = p;
		
	}
	
	@Override
	public Color getColor() {
		return DEFAULT_COLOR;
	}

	@Override
	public int getSize() {
		return DEFAULT_SIZE;
	}

	@Override
	public void paint(Graphics2D g2) {
		// center the point
		g2.translate(position.getX(), position.getY());
		g2.fillOval(0, 0, 5, 5);
		// center the surrounding rectangle
		g2.translate(-DEFAULT_SIZE / 2, -DEFAULT_SIZE / 2);
		// center the arc
		// rotate towards the direction of our vector
		//g2.rotate(-direction, size / 2, size / 2);

		// useful for debugging
		// g2.drawRect(0, 0, size, size);

		// set the color
		g2.setColor(DEFAULT_COLOR);
		g2.fillOval(0, 0, DEFAULT_SIZE, DEFAULT_SIZE);
		// we need to do PI - FOV since we want to mirror the arc
		//g2.fillArc(0, 0, size, size, (int) toDegrees(-fieldOfView / 2),
		//		(int) toDegrees(fieldOfView));


	}

}
