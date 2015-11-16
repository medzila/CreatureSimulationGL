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
			return true;
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

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}
	
	

}
