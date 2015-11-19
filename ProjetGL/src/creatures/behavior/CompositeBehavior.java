package creatures.behavior;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import plug.creatures.ComportementPluginFactory;
import creatures.AbstractCreature;
import creatures.ICreature;

public class CompositeBehavior implements IStrategyBehavior {
	
	private static float seuil;
	Constructor<? extends IStrategyBehavior> energieConst = null;
	Constructor<? extends IStrategyBehavior> smartConst = null;
	private EnergyBehavior energyBehavior = null;
	private EmergingBehavior emergingBehavior = null;
	
	
	
	public CompositeBehavior(){
		boolean energie = false;
		boolean smart = false ;
		this.seuil=(float) (AbstractCreature.DEFAULT_HEALTH/2);
		Map<String,Constructor<? extends IStrategyBehavior>> factory = ComportementPluginFactory.getInstance().getMap();
		
		// On regarde chaque comportement dans la factory et il faut trouver tous les comportments qu'on va utilisé
		for (String s : factory.keySet()){
			IStrategyBehavior i = null;
			if (!s.equals(this.getName())){
				try {
					i = factory.get(s).newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (i.getClass().isAssignableFrom(EnergyBehavior.class)){
					energie = true;
					this.energyBehavior = (EnergyBehavior) i;
				}else if(i.getClass().isAssignableFrom(EmergingBehavior.class)){
					smart = true ; 
					this.emergingBehavior = (EmergingBehavior) i;
				}
			}
		}
		if(!(energie && smart)){
			throw new IllegalArgumentException("Classes du comportement composite non trouvable");
		}
	}
	

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public void setNextDirectionAndSpeed(ICreature c) {
		if(c.getHealth() > seuil){
			emergingBehavior.setNextDirectionAndSpeed(c);
		} else {
			energyBehavior.setNextDirectionAndSpeed(c);
		}

	}

}
