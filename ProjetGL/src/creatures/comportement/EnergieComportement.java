package creatures.comportement;

import static commons.Utils.filter;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.random;

import java.util.ArrayList;

import commons.Utils.Predicate;
import creatures.AbstractCreature;
import creatures.ICreature;
import creatures.PointEnergie;

public class EnergieComportement implements IStrategieComportement {
	private static final double MIN_SPEED = 2;
	private static final double MAX_SPEED = 6;

	/**
	 * Number of cycles after which we apply some random noise.
	 */
	private static final int NUMBER_OF_CYCLES_PER_CHANGE = 50;

	protected int currCycle;
	
	static class EnergieAroundCreature implements Predicate<PointEnergie> {
		private final AbstractCreature observer;

		public EnergieAroundCreature(AbstractCreature observer) {
			this.observer = observer;
		}

		@Override
		public boolean apply(PointEnergie input) {
			if (input.position == observer.getPosition()) {
				return false;
			}
			double dirAngle = input.directionFormAPoint(observer.getPosition(),
					observer.getDirection());

			return abs(dirAngle) < (observer.getFieldOfView() / 2)
					&& observer.distanceFromAPoint(input.position) <= (observer
							.getLengthOfView()+30);} //observer.getLengthOfView + 30 augmentation de la distance de vision de la creature.

	}

	public Iterable<PointEnergie> ptsAround(
			ICreature c) {
		return filter(c.getEnvironment().getPoints(), new EnergieAroundCreature((AbstractCreature)c));
	}

	@Override
	public String getName() {
		return EnergieComportement.class.getName();
	}

	@Override
	public void setNextDirectionAndSpeed(ICreature c) {
		double angle = Double.MAX_VALUE;
		boolean energieDirection = true;

		PointEnergie p = null;
		ArrayList<PointEnergie> ptsEnergie = (ArrayList) ptsAround(c);
		
		if(!ptsEnergie.isEmpty()){
			p = ptsEnergie.get(0);
			double dx = p.getPosition().getX() - c.getPosition().getX();
	        double dy = p.getPosition().getY() - c.getPosition().getY();
	        angle = Math.atan2(dy, dx);
			c.setDirection(-angle);
			energieDirection=false;
		}
		if(energieDirection){
			applyNoise(c);
		}
		c.move();
	}
	
	/**
	 * Every number of cycles we apply some random noise over speed and
	 * direction
	 */
	public void applyNoise(ICreature c) {
		currCycle++;
		currCycle %= NUMBER_OF_CYCLES_PER_CHANGE;

		// every NUMBER_OF_CYCLES_PER_CHANGE we do the change
		if (currCycle == 0) {
			c.setSpeed(c.getSpeed()+((random() * 2) - 1));

			// maintain the speed within some boundaries
			if (c.getSpeed() < MIN_SPEED) {
				c.setSpeed(MIN_SPEED);
			} else if (c.getSpeed() > MAX_SPEED) {
				c.setSpeed(MAX_SPEED);
			}

			c.setDirection(c.getDirection()
					+ ((random() * PI / 2) - (PI / 4)));
		}
	}

}
