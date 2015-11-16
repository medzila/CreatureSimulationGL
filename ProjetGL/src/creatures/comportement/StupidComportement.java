package creatures.comportement;

import creatures.ICreature;

public class StupidComportement implements IStrategieComportement {

	@Override
	public void setNextDirectionAndSpeed(ICreature c) {
		c.move();

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.getClass().getName();
	}

}
