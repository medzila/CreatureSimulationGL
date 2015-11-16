package creatures.comportement;

import creatures.ICreature;
import plug.IPlugin;

public interface IStrategieComportement extends IPlugin {
	
	void setNextDirectionAndSpeed(ICreature c);
}
