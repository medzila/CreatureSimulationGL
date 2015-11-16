package plug.creatures;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import plug.IPlugin;
import plug.PluginLoader;
import creatures.CreatureComposable;
import creatures.IColorStrategy;
import creatures.ICreature;
import creatures.IEnvironment;
import creatures.IStrategieComportement;
import creatures.IStrategieDeplacement;
import creatures.PointEnergie;

public class Builder {

	private final static Random rand = new Random();

	static public  Collection<CreatureComposable> createCreatures(IEnvironment env, int count, 
								IColorStrategy colorStrategy, IStrategieComportement comp, IStrategieDeplacement depl, double maxSpeed) {
		Collection<CreatureComposable> creatures = new ArrayList<CreatureComposable>();		
		Dimension s = env.getSize();		
		for (int i=0; i<count; i++) {	
			// X coordinate
			double x = (rand.nextDouble() * s.getWidth()) - s.getWidth() / 2;
			// Y coordinate
			double y = (rand.nextDouble() * s.getHeight()) - s.getHeight() / 2;
			// direction
			double direction = (rand.nextDouble() * 2 * Math.PI);
			// speed
			int speed = (int) (rand.nextDouble() * maxSpeed);			
			CreatureComposable creature = null;
			creature = new CreatureComposable(env, new Point2D.Double(x,y), direction, speed, colorStrategy.getColor(), comp, depl);
			creatures.add(creature);
		}		
		return creatures;
	}

	static public Collection<PointEnergie> createPoints(IEnvironment env, int number, int size) {
		Random randomInts = new Random();
		Collection<PointEnergie> energySpots = new ArrayList<PointEnergie>();
		for (int i = 0; i < number; i++) {
			energySpots.add(new PointEnergie(new Point2D.Double(randomInts.nextInt(env.getSize().width) - env.getSize().width / 2,
					randomInts.nextInt(env.getSize().height) - env.getSize().height / 2), size));
		}
		return energySpots;
	}
}
