package creatures.behavior;

import creatures.ICreature;

public class StupidBehavior implements IStrategyBehavior {

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
