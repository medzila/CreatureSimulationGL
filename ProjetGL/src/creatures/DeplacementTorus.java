package creatures;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.awt.Dimension;

public class DeplacementTorus implements IStrategieDeplacement {

	@Override
	public void setNextPosition(ICreature c) {
		Dimension s = c.getEnvironment().getSize();
		
		double newX = c.getPosition().getX() + c.getSpeed() * cos(c.getDirection());
		// the reason there is a minus instead of a plus is that in our plane
		// Y coordinates rises downwards
		double newY = c.getPosition().getY() - c.getSpeed() * sin(c.getDirection());
		
		if (newX > s.getWidth() / 2) {
			newX = -s.getWidth() / 2;
		} else if (newX < -s.getWidth() / 2) {
			newX = s.getWidth() / 2;
		}

		if (newY > s.getHeight() / 2) {
			newY = -s.getHeight() / 2;
		} else if (newY < -s.getHeight() / 2) {
			newY = s.getHeight() / 2;
		}
		
		c.setPosition(newX, newY);

	}

}
