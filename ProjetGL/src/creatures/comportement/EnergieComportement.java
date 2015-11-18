package creatures.comportement;

import static commons.Utils.filter;
import static java.lang.Math.abs;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;

import commons.Utils.Predicate;
import creatures.AbstractCreature;
import creatures.ICreature;
import creatures.PointEnergie;

public class EnergieComportement implements IStrategieComportement {
	
	static class EnergieAroundCreature implements Predicate<PointEnergie> {
		private final AbstractCreature observer;

		public EnergieAroundCreature(AbstractCreature observer) {
			this.observer = observer;
		}

		@Override
		public boolean apply(PointEnergie input) {
			if (input.position == observer.getPosition()) {
				return false;
			}
			double dirAngle = input.directionFormAPoint(observer.getPosition(),
					observer.getDirection());

			return abs(dirAngle) < (observer.getFieldOfView() / 2)
					&& observer.distanceFromAPoint(input.position) <= (observer
							.getLengthOfView()+30);} //observer.getLengthOfView + 30 augmentation de la distance de vision de la creature.

	}

	public Iterable<PointEnergie> ptsAround(
			ICreature c) {
		return filter(c.getEnvironment().getPoints(), new EnergieAroundCreature((AbstractCreature)c));
	}

	@Override
	public String getName() {
		return EnergieComportement.class.getName();
	}

	@Override
	public void setNextDirectionAndSpeed(ICreature c) {
		double angle = Double.MAX_VALUE;

		PointEnergie p = null;
		ArrayList<PointEnergie> ptsEnergie =(ArrayList) ptsAround(c);
		
		if(!ptsEnergie.isEmpty()){
			p = ptsEnergie.get(0);
			double dx = p.getPosition().getX() - c.getPosition().getX();
	        double dy = p.getPosition().getY() - c.getPosition().getY();
	        angle = Math.atan2(dy, dx);
			c.setDirection(-angle);
		}
		c.move();
	}

}
