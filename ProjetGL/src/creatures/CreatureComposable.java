package creatures;

import java.awt.Color;
import java.awt.geom.Point2D;

public class CreatureComposable extends AbstractCreature{

	IStrategieComportement comportement;
	IStrategieDeplacement deplacement;
	
	public CreatureComposable(IEnvironment environment, Point2D position, double direction, double speed,
			Color color, IStrategieComportement comp, IStrategieDeplacement depl) {
		super(environment, position);
		this.direction=direction;
		this.speed=speed;
		this.color=color;
		this.comportement = comp;
		this.deplacement = depl;
	}

	@Override
	public void move() {
		this.deplacement.setNextPosition(this);
	}

	@Override
	public void act() {
		this.comportement.setNextDirectionAndSpeed(this);
		
		gainOrLoseHealth();	
	}

}
