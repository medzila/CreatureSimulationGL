package creatures.deplacement;

import creatures.ICreature;
import plug.IPlugin;

public interface IStrategieDeplacement extends IPlugin{
	
	void setNextPosition(ICreature c);
}