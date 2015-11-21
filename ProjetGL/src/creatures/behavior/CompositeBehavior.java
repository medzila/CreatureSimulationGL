package creatures.behavior;

import java.lang.reflect.Constructor;
import java.util.Map;

import plug.creatures.BehaviorPluginFactory;
import creatures.ComposableCreature;
import main.Launcher;

public class CompositeBehavior implements IStrategyBehavior {
	
	private EnergyBehavior energyBehavior = null;
	private EmergingBehavior emergingBehavior = null;
	boolean isEnergyBehaviorHere = false;
	boolean isEmergingBehaviorHere = false ;
	
	public CompositeBehavior() throws Exception {
		Map<String,Constructor<? extends IStrategyBehavior>> factory = BehaviorPluginFactory.getInstance().getMap();
		
		// We check every behavior in the factory. We have to find every behavior needed (emerging & energy)
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
			}
		}
		if(!isEnergyBehaviorHere)
			throw new Exception("Energy behavior is missing. Add the \"EnergyBehavior\" plugin please.");
		if(!isEmergingBehaviorHere)
			throw new Exception("Emerging behavior is missing. Add the \"EmergingBehavior\" plugin please.");
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public void setNextDirectionAndSpeed(ComposableCreature c) {
		if(c.getHealth() >= Launcher.THRESHOLD){
			emergingBehavior.setNextDirectionAndSpeed(c);
		} else {
			energyBehavior.setNextDirectionAndSpeed(c);
		}

	}

}
