package creatures.comportement;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import plug.creatures.ComportementPluginFactory;
import creatures.AbstractCreature;
import creatures.ICreature;

public class CompositeComportement implements IStrategieComportement {
	
	private static float seuil;
	Constructor<? extends IStrategieComportement> energieConst = null;
	Constructor<? extends IStrategieComportement> smartConst = null;
	private EnergieComportement energieComportement = null;
	private SmartComportement smartComportement = null;
	
	
	
	public CompositeComportement(){
		boolean energie = false;
		boolean smart = false ;
		this.seuil=(float) (AbstractCreature.DEFAULT_HEALTH/2);
		Map<String,Constructor<? extends IStrategieComportement>> factory = ComportementPluginFactory.getInstance().getMap();
		
		// On regarde chaque comportement dans la factory et il faut trouver tous les comportments qu'on va utilisé
		for (String s : factory.keySet()){
			IStrategieComportement i = null;
			if (!s.equals(this.getName())){
				try {
					i = factory.get(s).newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (i.getClass().isAssignableFrom(EnergieComportement.class)){
					energie = true;
					this.energieComportement = (EnergieComportement) i;
				}else if(i.getClass().isAssignableFrom(SmartComportement.class)){
					smart = true ; 
					this.smartComportement = (SmartComportement) i;
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
			smartComportement.setNextDirectionAndSpeed(c);
		} else {
			energieComportement.setNextDirectionAndSpeed(c);
		}

	}

}
