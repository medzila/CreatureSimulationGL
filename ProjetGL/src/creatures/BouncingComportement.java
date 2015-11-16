package creatures;

import static java.lang.Math.PI;
import static java.lang.Math.random;

public class BouncingComportement implements IStrategieComportement {
	
	private static final double MIN_SPEED = 3;
	private static final double MAX_SPEED = 10;

	/**
	 * Number of cycles after which we apply some random noise.
	 */
	private static final int NUMBER_OF_CYCLES_PER_CHANGE = 30;

	protected int currCycle;
	
	@Override
	public void setNextDirectionAndSpeed(ICreature c) {

		 
		applyNoise(c);
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

	@Override
	public String getName() {
		return BouncingComportement.class.getName();
	}

}
