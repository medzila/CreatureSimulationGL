package creatures.behavior;

import creatures.ICreature;
import plug.IPlugin;

public interface IStrategyBehavior extends IPlugin {
	
	void setNextDirectionAndSpeed(ICreature c);
}
