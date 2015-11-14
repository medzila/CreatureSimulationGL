package creatures.visual;

import static commons.Utils.filter;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JLabel;

import simulator.Simulator;

import commons.Utils.Predicate;

import creatures.ICreature;
import creatures.IEnvironment;
import creatures.PointEnergie;


/**
 * Environment for the creatures together with the visualization facility.
 */
public class CreatureSimulator extends Simulator<ICreature> implements IEnvironment {

	static class CreaturesNearbyPoint implements Predicate<ICreature> {
		private final Point2D point;
		private final double margin;

		public CreaturesNearbyPoint(Point2D point, double margin) {
			this.point = point;
			this.margin = margin;
		}

		@Override
		public boolean apply(ICreature input) {
			return input.distanceFromAPoint(point) <= margin;
		}
	}

	private Dimension size;
	private Iterable<PointEnergie> points;
	private int creaturesDead;
	private JLabel creaturesTotal;
	private JLabel creaturesMortes;


	public Iterable<PointEnergie> getPoints() {
		return points;
	}

	public CreatureSimulator(Dimension initialSize, int nombresDePoints) {
		super(new CopyOnWriteArrayList<ICreature>(), 10);
		this.size = initialSize;
		Random randomInts = new Random();
		ArrayList<PointEnergie> al = new ArrayList<PointEnergie>();
		for (int i = 0; i < nombresDePoints; i++) {
			al.add(new PointEnergie(new Point2D.Double(randomInts.nextInt(initialSize.width) - initialSize.width / 2,
					randomInts.nextInt(initialSize.height) - initialSize.height / 2)));
		}
		points = al;
		this.creaturesDead = 0;
		this.creaturesTotal = new JLabel();
		this.creaturesMortes = new JLabel();
	}
	
	/**
	 * @return a copy of current size
	 */
	public synchronized Dimension getSize() {
		return new Dimension(size);
	}
	
	public synchronized void setSize(Dimension size) {
		this.size = size;
	}
	
	/**
	 * @return a copy of the current creature list.
	 */
	@Override
	public Iterable<ICreature> getCreatures() {
		ArrayList<ICreature> creatureList = new ArrayList<ICreature>(actionables);
		for(ICreature c : creatureList)
			if(c.isDead()){
				removeCreature(c);
				creaturesDead++;
			}
		int creaturesDepart = creatureSize() + getCreaturesDead();
		creaturesTotal.setText("Nombre de créatures : " + creaturesDepart  +"   //");
		creaturesMortes.setText("Nombre de créatures mortes : " + getCreaturesDead());
		return new ArrayList<ICreature>(actionables);
	}
	
	public JLabel getLabelCreaturesDead(){
		return creaturesMortes;
	}
	
	public JLabel getLabelCreaturesTotal(){
		return creaturesTotal;
	}
	
	/**
	 * @return number of dead creatures.
	 */
	public int getCreaturesDead(){
		return creaturesDead;
	}
	
	public int creatureSize() {
		return actionables.size();
	}
	
	public void addCreature(ICreature creature) {
		actionables.add(creature);
	}
	
	public void removeCreature(ICreature creature) {
		actionables.remove(creature);
	}
	
	public Iterable<ICreature> creaturesNearByAPoint(Point2D point,  double radius) {
		return filter(actionables, new CreaturesNearbyPoint(point, radius));
	}

	public void addAllCreatures(Collection<? extends ICreature> creatures) {
		actionables.addAll(creatures);
	}
	
	public void clearCreatures() {
		actionables.clear();
	}

}
