package creatures.behavior;

import static commons.Utils.filter;
import static java.lang.Math.abs;

import commons.Utils.Predicate;
import creatures.ComposableCreature;
import creatures.ICreature;

public class EmergingBehavior implements IStrategyBehavior {
	
	static class CreaturesAroundCreature implements Predicate<ICreature> {
		private final ComposableCreature observer;

		public CreaturesAroundCreature(ComposableCreature observer) {
			this.observer = observer;
		}
		@Override
		public boolean apply(ICreature input) {
			if (input == observer) {
				return false;
			}
			double dirAngle = input.directionFormAPoint(observer.getPosition(),
					observer.getDirection());

			return abs(dirAngle) < (observer.getFieldOfView() / 2)
					&& observer.distanceFromAPoint(input.getPosition()) <= observer
							.getLengthOfView();

		}
	}


	/** Minimal distance between this creature and the ones around. */
	private final static double MIN_DIST = 10d;

	/** Minimal speed in pixels per loop. */
	private final static double MIN_SPEED = 3d;
	
	
	public Iterable<ICreature> creaturesAround(
			ICreature creature) {
		return filter(creature.getEnvironment().getCreatures(), new CreaturesAroundCreature((ComposableCreature)creature));
	}
	
	
	public void setNextDirectionAndSpeed(ComposableCreature c) {
		// speed - will be used to compute the average speed of the nearby
				// creatures including this instance
				double avgSpeed = c.getSpeed();
				// direction - will be used to compute the average direction of the
				// nearby creatures including this instance
				double avgDir = c.getDirection();
				// distance - used to find the closest nearby creature
				double minDist = Double.MAX_VALUE;

				// iterate over all nearby creatures
				Iterable<ICreature> creatures = creaturesAround(c);
				int count = 0;
				for (ICreature c2 : creatures) {
					avgSpeed += c2.getSpeed();
					avgDir += c2.getDirection();
					minDist = Math.min(minDist, c2.distanceFromAPoint(c2.getPosition()));
					count++;
				}
				// average
				avgSpeed = avgSpeed / (count + 1);
				// min speed check
				if (avgSpeed < MIN_SPEED) {
					avgSpeed = MIN_SPEED;
				}
				// average
				avgDir = avgDir / (count + 1);

				// apply - change this creature state
				c.setDirection(avgDir);
				c.setSpeed(avgSpeed);
				
				// if we are not too close move closer
				if (minDist > MIN_DIST) {
					c.move();
				}
				if(count==0){
					c.setLossHealth(c.DEFAULT_LOSS_HEALTH);
				}else {
					c.setLossHealth(c.DEFAULT_LOSS_HEALTH/count);
				}
				
				c.gainOrLoseHealth();	
			}

	


	@Override
	public String getName() {
		return EmergingBehavior.class.getName();
	}

}
