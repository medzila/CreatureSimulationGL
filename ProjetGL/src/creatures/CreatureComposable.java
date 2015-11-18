package creatures;

import java.awt.Color;
import java.awt.Image;
import java.awt.geom.Point2D;

import creatures.comportement.IStrategieComportement;
import creatures.deplacement.IStrategieDeplacement;

public class CreatureComposable extends AbstractCreature{

	IStrategieComportement comportement;
	IStrategieDeplacement deplacement;
	
	public int currCycle;
	
	
	public CreatureComposable(IEnvironment environment, Point2D position, double direction, double speed,
			Color color, IStrategieComportement comp, IStrategieDeplacement depl) {
		super(environment, position);
		this.direction=direction;
		this.speed=speed;
		this.color=color;
		this.comportement = comp;
		this.deplacement = depl;
		this.currCycle = 0;
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

	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int getCurrCycle(){
		return this.currCycle;
	}
	
	public void setCurrCycle(int i){
		this.currCycle = i;
	}

}
