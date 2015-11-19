package plug.creatures;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import plug.IPlugin;
import plug.PluginLoader;
import creatures.ICreature;
import creatures.IEnvironment;
import creatures.behavior.IStrategyBehavior;
import creatures.color.IColorStrategy;
import creatures.movement.IStrategieMovement;

public class BehaviorPluginFactory {
	
	/**
	 * singleton for the abstract factory
	 */
	protected static BehaviorPluginFactory _singleton;
		
	protected PluginLoader pluginLoader;
	
	private final String pluginDir = "myplugins/repository";
	
	protected Map<String,Constructor<? extends IStrategyBehavior>> constructorMap; 

	/**
	   * logger facilities to trace plugin loading...
	   */
	private static Logger logger = Logger.getLogger("plug.ComportementPluginFactory");
	
	
    public static void init() {
        if (_singleton != null) {
            throw new RuntimeException("CreatureFactory already created by " 
				  + _singleton.getClass().getName());
        } else {
             _singleton = new BehaviorPluginFactory();
        }
     }

    public static BehaviorPluginFactory getInstance() {
    	return _singleton;
    }

    private BehaviorPluginFactory() {
    	try {
    		pluginLoader = new PluginLoader(pluginDir,IStrategyBehavior.class);
    	}
    	catch (MalformedURLException ex) {
    	}
		constructorMap = new HashMap<String,Constructor<? extends IStrategyBehavior>>();
    	load();
    }
	
    public void load() {
    	pluginLoader.loadPlugins();
    	buildConstructorMap();
    }
    
    public void reload() {
    	pluginLoader.reloadPlugins();
    	constructorMap.clear();
    	buildConstructorMap();
    }
    
	@SuppressWarnings("unchecked")
	private void buildConstructorMap() {
		for (Class<? extends IPlugin> p : pluginLoader.getPluginClasses()) {
			Constructor<? extends IStrategyBehavior> c = null;

			try {				
				c = (Constructor<? extends IStrategyBehavior>)p.getDeclaredConstructor();
				c.setAccessible(true);
			} catch (SecurityException e) {
				logger.info("Cannot access (security) constructor for plugin" + p.getName());
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				logger.info("No constructor in plugin " + p.getName() + " with the correct signature");
				e.printStackTrace();
			}
			if (c != null)
				constructorMap.put(p.getName(),c);
		}
	}
	
	public Map<String,Constructor<? extends IStrategyBehavior>> getMap() {
		return constructorMap;
	}


}