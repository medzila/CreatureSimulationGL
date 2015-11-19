package creatures.behavior;

import java.lang.reflect.Constructor;
import java.util.Map;

import plug.creatures.BehaviorPluginFactory;
import creatures.AbstractCreature;
import creatures.ICreature;

public class CompositeBehavior implements ICompoundActingStrategy {
	
	private static float TRESHOLD;
	Constructor<? extends IStrategyBehavior> energieConst = null;
	Constructor<? extends IStrategyBehavior> smartConst = null;
	private EnergyBehavior energyBehavior = null;
	private EmergingBehavior emergingBehavior = null;
	boolean isEnergyBehaviorHere = false;
	boolean isEmergingBehaviorHere = false ;
	
	
	public CompositeBehavior() throws Exception {
		CompositeBehavior.TRESHOLD=(float) (AbstractCreature.DEFAULT_HEALTH/2);
		Map<String,Constructor<? extends IStrategyBehavior>> factory = BehaviorPluginFactory.getInstance().getMap();
		
		// We check every behavior in the factory. We have to find every behavior needed (emerging & energy)
		for (String s : factory.keySet()){
			IStrategyBehavior i = null;
			if (!ICompoundActingStrategy.class.isAssignableFrom(factory.get(s).getDeclaringClass())){
					Constructor<? extends IStrategyBehavior> c = factory.get(s);
					if(c == null)
						throw new Exception("Something went wrong with the factory. Report it to devs without reprisal please.");
					else
						i = c.newInstance();

				if (i.getClass().isAssignableFrom(EnergyBehavior.class)){
					isEnergyBehaviorHere = true;
					this.energyBehavior = (EnergyBehavior) i;
				}else if(i.getClass().isAssignableFrom(EmergingBehavior.class)){
					isEmergingBehaviorHere = true ; 
					this.emergingBehavior = (EmergingBehavior) i;
				}
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
	public void setNextDirectionAndSpeed(ICreature c) {
		if(c.getHealth() > TRESHOLD){
			emergingBehavior.setNextDirectionAndSpeed(c);
		} else {
			energyBehavior.setNextDirectionAndSpeed(c);
		}

	}

}
