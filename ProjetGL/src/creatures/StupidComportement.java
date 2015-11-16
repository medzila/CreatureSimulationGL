package creatures;

public class StupidComportement implements IStrategieComportement {

	@Override
	public void setNextDirectionAndSpeed(ICreature c) {
		c.move();

	}

}
