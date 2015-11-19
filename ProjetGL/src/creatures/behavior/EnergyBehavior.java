package creatures.behavior;

import static commons.Utils.filter;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.random;

import java.util.ArrayList;

import commons.Utils.Predicate;
import creatures.AbstractCreature;
import creatures.CreatureComposable;
import creatures.ICreature;
import creatures.EnergySource;

public class EnergyBehavior implements IStrategyBehavior {
	private static final double MIN_SPEED = 2;
	private static final double MAX_SPEED = 6;

	/**
	 * Number of cycles after which we apply some random noise.
	 */
	private static final int NUMBER_OF_CYCLES_PER_CHANGE = 50;
	
	static class EnergieAroundCreature implements Predicate<EnergySource> {
		private final AbstractCreature observer;

		public EnergieAroundCreature(AbstractCreature observer) {
			this.observer = observer;
		}

		@Override
		public boolean apply(EnergySource input) {
			if (input.position == observer.getPosition()) {
				return false;
			}
			double dirAngle = input.directionFormAPoint(observer.getPosition(),
					observer.getDirection());

			return abs(dirAngle) < (observer.getFieldOfView() / 2)
					&& observer.distanceFromAPoint(input.position) <= (observer
							.getLengthOfView()+30);} //observer.getLengthOfView + 30 augmentation de la distance de vision de la creature.

	}

	public Iterable<EnergySource> ptsAround(
			ICreature c) {
		return filter(c.getEnvironment().getPoints(), new EnergieAroundCreature((AbstractCreature)c));
	}

	@Override
	public String getName() {
		return EnergyBehavior.class.getName();
	}

	@Override
	public void setNextDirectionAndSpeed(ICreature c) {
		CreatureComposable c1 = (CreatureComposable)c;
		double angle = Double.MAX_VALUE;
		boolean energieDirection = true;

		EnergySource p = null;
		ArrayList<EnergySource> ptsEnergie = (ArrayList) ptsAround(c1);
		
		if(!ptsEnergie.isEmpty()){
			p = ptsEnergie.get(0);
			double dx = p.getPosition().getX() - c1.getPosition().getX();
	        double dy = p.getPosition().getY() - c1.getPosition().getY();
	        angle = Math.atan2(dy, dx);
			c1.setDirection(-angle);
			energieDirection=false;
		}
		if(energieDirection){
			applyNoise(c1);
		}
		c1.move();
	}
	
	/**
	 * Every number of cycles we apply some random noise over speed and
	 * direction
	 */
	/**
	 * Every number of cycles we apply some random noise over speed and
	 * direction
	 */
	public void applyNoise(ICreature c) {
		CreatureComposable c1 = (CreatureComposable)c;
		c1.setCurrCycle(c1.getCurrCycle()+1);
		c1.currCycle %= NUMBER_OF_CYCLES_PER_CHANGE;

		// every NUMBER_OF_CYCLES_PER_CHANGE we do the change
		if (c1.currCycle == 0) {
			c1.setSpeed(c1.getSpeed()+((random() * 2) - 1));

			// maintain the speed within some boundaries
			if (c1.getSpeed() < MIN_SPEED) {
				c1.setSpeed(MIN_SPEED);
			} else if (c1.getSpeed() > MAX_SPEED) {
				c1.setSpeed(MAX_SPEED);
			}

			c1.setDirection(c1.getDirection()
					+ ((random() * PI / 2) - (PI / 4)));
		}
	}

}
