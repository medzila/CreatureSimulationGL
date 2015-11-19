package creatures.behavior;

import java.awt.image.ImageConsumer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
	
	
	
	public CompositeBehavior(){
		boolean isEnergyBehaviorHere = false;
		boolean isEmergingBehaviorHere = false ;
		this.TRESHOLD=(float) (AbstractCreature.DEFAULT_HEALTH/2);
		Map<String,Constructor<? extends IStrategyBehavior>> factory = BehaviorPluginFactory.getInstance().getMap();
		
		// We check every behavior in the factory. We have to find every behavior needed (emerging & energy)
		for (String s : factory.keySet()){
			IStrategyBehavior i = null;
			if (!ICompoundActingStrategy.class.isAssignableFrom(factory.get(s).getDeclaringClass())){
				try {
					i = factory.get(s).newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (i.getClass().isAssignableFrom(EnergyBehavior.class)){
					isEnergyBehaviorHere = true;
					this.energyBehavior = (EnergyBehavior) i;
				}else if(i.getClass().isAssignableFrom(EmergingBehavior.class)){
					isEmergingBehaviorHere = true ; 
					this.emergingBehavior = (EmergingBehavior) i;
				}
			}
		}
		if(!(isEnergyBehaviorHere && isEmergingBehaviorHere)){
			throw new IllegalArgumentException("Missing Emerging or Energy behavior.");
		}
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
