package creatures.behavior;

import creatures.ComposableCreature;
import creatures.ICreature;
import plug.IPlugin;

public interface IStrategyBehavior extends IPlugin {
	
	void setNextDirectionAndSpeed(ComposableCreature c);
}
