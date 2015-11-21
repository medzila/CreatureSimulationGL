package creatures.behavior;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Random;

import plug.creatures.BehaviorPluginFactory;
import creatures.ComposableCreature;
import main.Launcher;

public class CompositeBehavior2 implements IStrategyBehavior {
		
	private EnergyBehavior energyBehavior = null;
	private EmergingBehavior emergingBehavior = null;
	private PredatorBehavior predatorBehavior = null;

	private boolean isEnergyBehaviorHere;
	private boolean isEmergingBehaviorHere;
	private boolean isPredatorBehaviorHere;
	
	public CompositeBehavior2() throws Exception{
		isEnergyBehaviorHere = false;
		isEmergingBehaviorHere = false ;
		isPredatorBehaviorHere = true;
		Map<String,Constructor<? extends IStrategyBehavior>> factory = BehaviorPluginFactory.getInstance().getMap();
		
		// On regarde chaque comportement dans la factory et il faut trouver tous les comportments qu'on va utilise
		for (String s : factory.keySet()){
			Constructor<? extends IStrategyBehavior> c = factory.get(s);
			if(c == null)
				throw new Exception("Something went wrong with the factory. Report it to devs without reprisal please.");

			if (EnergyBehavior.class.isAssignableFrom(c.getDeclaringClass())){
				isEnergyBehaviorHere = true;
				this.energyBehavior = (EnergyBehavior) c.newInstance();
			}else if(EmergingBehavior.class.isAssignableFrom(c.getDeclaringClass())){
				isEmergingBehaviorHere = true ; 
				this.emergingBehavior = (EmergingBehavior) c.newInstance();
			}else if(PredatorBehavior.class.isAssignableFrom(c.getDeclaringClass())){
				isPredatorBehaviorHere = true ; 
			this.predatorBehavior = (PredatorBehavior) c.newInstance();
			}
		}
		if(!isEnergyBehaviorHere)
			throw new Exception("Energy behavior is missing. Add the \"EnergyBehavior\" plugin please.");
		if(!isEmergingBehaviorHere)
			throw new Exception("Emerging behavior is missing. Add the \"EmergingBehavior\" plugin please.");
		if(!isPredatorBehaviorHere)
			throw new Exception("Predator behavior is missing. Add the \"PredatorBehavior\" plugin please.");
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public void setNextDirectionAndSpeed(ComposableCreature c) {
		Random rand = new Random();
		int nb = rand.nextInt(2);
		if(c.getHealth() >= Launcher.THRESHOLD && nb == 0){
			emergingBehavior.setNextDirectionAndSpeed(c);
		}else if(c.getHealth() >= Launcher.THRESHOLD && nb == 1){
			predatorBehavior.setNextDirectionAndSpeed(c);
		}else {
			energyBehavior.setNextDirectionAndSpeed(c);
		}

	}

}
