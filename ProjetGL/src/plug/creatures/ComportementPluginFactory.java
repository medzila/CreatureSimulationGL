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
import creatures.IColorStrategy;
import creatures.ICreature;
import creatures.IEnvironment;
import creatures.IStrategieComportement;
import creatures.IStrategieDeplacement;

public class ComportementPluginFactory {
	
	/**
	 * singleton for the abstract factory
	 */
	protected static ComportementPluginFactory _singleton;
		
	protected PluginLoader pluginLoader;
	
	private final String pluginDir = "myplugins/repository";
	
	protected Map<String,IStrategieComportement> constructorMap; 

	/**
	   * logger facilities to trace plugin loading...
	   */
	private static Logger logger = Logger.getLogger("plug.ComportementPluginFactory");
	
	
    public static void init() {
        if (_singleton != null) {
            throw new RuntimeException("CreatureFactory already created by " 
				  + _singleton.getClass().getName());
        } else {
             _singleton = new ComportementPluginFactory();
        }
     }

    public static ComportementPluginFactory getInstance() {
    	return _singleton;
    }

    private ComportementPluginFactory() {
    	try {
    		pluginLoader = new PluginLoader(pluginDir,IStrategieComportement.class);
    	}
    	catch (MalformedURLException ex) {
    	}
		constructorMap = new HashMap<String,IStrategieComportement>();
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
			Constructor<? extends IStrategieComportement> c = null;

			try {				
				c = (Constructor<? extends IStrategieComportement>)p.getDeclaredConstructor();
				c.setAccessible(true);
			} catch (SecurityException e) {
				logger.info("Cannot access (security) constructor for plugin" + p.getName());
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				logger.info("No constructor in plugin " + p.getName() + " with the correct signature");
				e.printStackTrace();
			}
			if (c != null)
				try {
					constructorMap.put(p.getName(),c.newInstance());
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public Map<String,IStrategieComportement> getConstructorMap() {
		return constructorMap;
	}


}