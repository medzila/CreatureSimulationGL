package creatures.comportement;

import static commons.Utils.filter;
import static java.lang.Math.abs;

import commons.Utils.Predicate;
import creatures.AbstractCreature;
import creatures.CreatureComposable;
import creatures.ICreature;

public class proieComportement implements IStrategieComportement {

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

		Iterable<ICreature> creatures = creaturesAround(c1);
		
		for (ICreature c2 : creatures) {
			if(c1.getHealth()>c2.getHealth()){
				toFollow = (CreatureComposable)c2;
				c1.setDirection(c2.getDirection());
				break;
			}
		}

		if(toFollow != null && c1.distanceFromAPoint(toFollow.getPosition())<40){
			c1.setSpeed(0);
			toFollow.setSpeed(0);
			toFollow.setLossHealth(c1.getHealth());
			c1.setSpeed(6);
			toFollow.setSpeed(3);
		}
		c.move();
	}


	@Override
	public String getName() {
		return proieComportement.class.getName();
		}
}