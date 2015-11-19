package creatures.comportement;

import static commons.Utils.filter;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.random;

import commons.Utils.Predicate;
import creatures.AbstractCreature;
import creatures.CreatureComposable;
import creatures.ICreature;

public class proieComportement implements IStrategieComportement {
	private static final double MAX_SPEED = 6;
	/**
	 * Number of cycles after which we apply some random noise.
	 */
	private static final int NUMBER_OF_CYCLES_PER_CHANGE = 50;
	
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
			CreatureComposable creature) {
		return filter(creature.getEnvironment().getCreatures(), new CreaturesAroundCreature((AbstractCreature)creature));
	}


	@Override
	public void setNextDirectionAndSpeed(ICreature c) {
		CreatureComposable c1 = (CreatureComposable)c;
		CreatureComposable toFollow = null;
		boolean noise = true;

		Iterable<ICreature> creatures = creaturesAround(c1);
		
		for (ICreature c2 : creatures) {
			if(c1.getHealth()>c2.getHealth()){
				toFollow = (CreatureComposable)c2;
				c1.setDirection(c2.getDirection());
				noise = false;
				break;
			}
		}

		if(toFollow != null && c1.distanceFromAPoint(toFollow.getPosition())<40){
			toFollow.setLossHealth(c1.getHealth());
			c1.setSpeed(6);
			toFollow.setSpeed(3);
			noise = false;
		}
		
		if(noise){
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
	public void applyNoise(CreatureComposable c1) {
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


	@Override
	public String getName() {
		return proieComportement.class.getName();
		}
}