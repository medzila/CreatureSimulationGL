package creatures.behavior;

import static commons.Utils.filter;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.random;

import java.util.ArrayList;

import commons.Utils.Predicate;
import creatures.ComposableCreature;
import creatures.ICreature;
import creatures.EnergySource;

public class EnergyBehavior implements IStrategyBehavior {
	private static final double MIN_SPEED = 2;
	private static final double MAX_SPEED = 6;

	/**
	 * Number of cycles after which we apply some random noise.
	 */
	private static final int NUMBER_OF_CYCLES_PER_CHANGE = 50;
	
	static class EnergySourceAroundCreature implements Predicate<EnergySource> {
		private final ComposableCreature observer;

		public EnergySourceAroundCreature(ComposableCreature observer) {
			this.observer = observer;
		}

		@Override
		public boolean apply(EnergySource input) {
			if (input.getPosition()== observer.getPosition() || observer.distanceFromAPoint(input.getPosition())<=input.getSize()/2) {
				return false;
			}
			double dirAngle = input.directionFormAPoint(observer.getPosition(),
					observer.getDirection());

			return abs(dirAngle) < (observer.getFieldOfView() / 2)
					&& observer.distanceFromAPoint(input.getPosition()) <= (observer
							.getLengthOfView()+input.getSize()/2);} //observer.getLengthOfView + 30 augmentation de la distance de vision de la creature.

	}

	public Iterable<EnergySource> ptsAround(
			ICreature c) {
		return filter(c.getEnvironment().getEnergySources(), new EnergySourceAroundCreature((ComposableCreature)c));
	}

	@Override
	public String getName() {
		return EnergyBehavior.class.getName();
	}

	@Override
	public void setNextDirectionAndSpeed(ComposableCreature c) {
		ComposableCreature c1 = (ComposableCreature)c;
		if(!c1.hasTarget()){
			double angle = Double.MAX_VALUE;
			//boolean energieDirection = true;

			EnergySource p = null;
			ArrayList<EnergySource> ptsEnergie = (ArrayList<EnergySource>) ptsAround(c1);

			if(!ptsEnergie.isEmpty()){
				p = ptsEnergie.get(0);
				double dx = p.getPosition().getX() - c1.getPosition().getX();
				double dy = p.getPosition().getY() - c1.getPosition().getY();
				angle = Math.atan2(dy, dx);
				c1.setDirection(-angle);
				c1.setHasTarget(true);
				//energieDirection=false;
			}
		}else{
			if(!c1.isOnAnEnergySource()){
				c1.setHasTarget(false);
			}

		}
		if(!c1.hasTarget()){
			applyNoise(c1);
		}
		c1.move();
	}
	

	/**
	 * Every number of cycles we apply some random noise over speed and
	 * direction
	 */
	public void applyNoise(ICreature c) {
		ComposableCreature c1 = (ComposableCreature)c;
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
