package creatures.comportement;

import java.util.Map;

import plug.creatures.ComportementPluginFactory;
import creatures.AbstractCreature;
import creatures.ICreature;

public class CompositeComportement implements IStrategieComportement {
	
	private static float seuil;
	private EnergieComportement energieComportement;
	private SmartComportement smartComportement;
	
	
	
	public CompositeComportement(){
		boolean energie = false;
		boolean smart = false ;
		this.seuil=(float) (AbstractCreature.DEFAULT_HEALTH*(100/seuil));
		Map<String,IStrategieComportement> factory = ComportementPluginFactory.getInstance().getMap();
		for (String s : factory.keySet()){
			if (factory.get(s).getClass().isAssignableFrom(energieComportement.getClass())){
				energie = true;
				this.energieComportement = (EnergieComportement)factory.get(s);
			}else if(factory.get(s).getClass().isAssignableFrom(smartComportement.getClass())){
					smart = true ; 
					this.smartComportement = (SmartComportement)factory.get(s);
				}
			}
		if(!(energie && smart)){
			throw new IllegalArgumentException("Classes du comportement composite non trouvable");
		}
	}
	

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
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
