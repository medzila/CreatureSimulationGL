package creatures;

import static commons.Utils.filter;
import static java.lang.Math.abs;

import commons.Utils.Predicate;

import creatures.SmartCreature.CreaturesAroundCreature;

public class SmartComportement implements IStrategieComportement {
	
	static class CreaturesAroundCreature implements Predicate<ICreature> {
		private final AbstractCreature observer;

		public CreaturesAroundCreature(AbstractCreature observer) {
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
		return filter(creature.getEnvironment().getCreatures(), new CreaturesAroundCreature((AbstractCreature)creature));
	}
	
	
	@Override
	public void setNextDirectionAndSpeed(ICreature c) {
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
				/*
				if(count==0){
					setLossHealth(DEFAULT_LOSS_HEALTH);
				}else if(count < 6){
					setLossHealth(0.04);
				}else if(count < 11){
					setLossHealth(0.03);
				}else if(count < 16){
					setLossHealth(0.02);
				}else{
					setLossHealth(0.01);
				}*/

	}

}
