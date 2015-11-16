package creatures;

import static commons.Utils.filter;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Collection;

import commons.Utils.Predicate;

public class CreatureEnergie extends AbstractCreature {
	
	static class EnergieAroundCreature implements Predicate<PointEnergie> {
		private final CreatureEnergie observer;

		public EnergieAroundCreature(CreatureEnergie observer) {
			this.observer = observer;
		}

		@Override
		public boolean apply(PointEnergie input) {
			if (input.position == observer.getPosition()) {
				return false;
			}
			return PointInTriangle(input.position,observer);
		}

		public Point2D getP1(Point2D p,double angle,double fov){
			return new Point2D.Double(p.getX()+DEFAULT_VISION_DISTANCE*Math.cos(fov+angle),p.getX()+DEFAULT_VISION_DISTANCE*Math.sin(fov+angle));
		}

		public Point2D getP2(Point2D p,double angle,double fov){
			return new Point2D.Double(p.getX()+DEFAULT_VISION_DISTANCE*Math.cos(-(fov+angle)),p.getX()+DEFAULT_VISION_DISTANCE*Math.sin(-(fov+angle)));
		}

		public double sign(Point2D p1,Point2D p2,Point2D p3)
		{
			return (p1.getX() - p3.getX()) * (p2.getY() - p3.getY()) - (p2.getX() - p3.getX()) * (p1.getY() - p3.getY());
		}

		public boolean PointInTriangle (Point2D pt,CreatureEnergie v1)
		{
			boolean b1, b2, b3;
			Point2D v2 = getP1(v1.getPosition(),v1.getDirection(),v1.fieldOfView);
			Point2D v3 = getP2(v1.getPosition(),v1.getDirection(),v1.fieldOfView);

			b1 = sign(pt, v1.getPosition(), v2) < 0.0d;
			b2 = sign(pt, v2, v3) < 0.0d;
			b3 = sign(pt, v3, v1.getPosition()) < 0.0d;

			return ((b1 == b2) && (b2 == b3));
		}
	}

	/** Minimal distance between this creature and the ones around. */
	private final static double MIN_DIST = 10d;

	/** Minimal speed in pixels per loop. */
	private final static double MIN_SPEED = 3d;

	public CreatureEnergie(IEnvironment environment, Point2D position, double direction, double speed,
			Color color) {
		super(environment, position);
		this.direction = direction;
		this.speed = speed;
		this.color = color;
	}

	@Override
	public void act() {
		double minDist = Double.MAX_VALUE;
		
		Collection<PointEnergie> ptsEnergie = (Collection) ptsAround(this);
		for (PointEnergie c : ptsEnergie) {
			minDist = Math.min(minDist, this.distanceFromAPoint(c.position));
		}
		
		double direction = getDirection();
		
		if(!ptsEnergie.isEmpty()){
		}
		
		setDirection(direction);

		double incX = speed * Math.cos(direction);
		double incY = - speed * Math.sin(direction);

		move(incX, incY);
		
		gainOrLoseHealth();

	}
	
	public Iterable<PointEnergie> ptsAround(
			CreatureEnergie c) {
		return filter(environment.getPoints(), new EnergieAroundCreature(this));
	}
	
	

}
