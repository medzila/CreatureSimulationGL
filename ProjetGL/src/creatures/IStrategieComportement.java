package creatures;

import plug.IPlugin;

public interface IStrategieComportement extends IPlugin {
	
	void setNextDirectionAndSpeed(ICreature c);
}
