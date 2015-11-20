package creatures;

import static java.lang.Math.toRadians;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import creatures.behavior.EnergyBehavior;
import creatures.movement.TorusMovement;
import creatures.visual.CreatureSimulator;

public class EnergyBehaviorTest {
	
	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 100;
	final double h = 100;
	EnergyBehavior e;
	TorusMovement t;
	
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		e = new EnergyBehavior();
		t = new TorusMovement();
	}
	
	/**
	 * Verifie si la creature voit le point devant elle.
	 * @throws Exception
	 */
	@Test
	public void testPointAround() throws Exception {
		
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),Math.PI/2,0,
				Color.BLACK,e,t);
		
		EnergySource pte = new EnergySource(new Point2D.Double(0,10),20);
				
		ArrayList<EnergySource> ptl = (ArrayList<EnergySource>)new ArrayList<EnergySource>();
		ptl.add(pte);
		
		when(creature.getEnvironment().getEnergySources()).thenReturn(ptl);
		
		ArrayList<EnergySource> ptsAround = (ArrayList<EnergySource>)e.ptsAround(creature);
		e.setNextDirectionAndSpeed(creature);
		
		assertEquals(1, ptsAround.size());
		assertEquals(creature.getDirection(),-Math.PI/2,0.01);
		
	}
	
	/**
	 * Verifie si la creature voit les points devant elle mais ignore ceux qui ne sont pas dans
	 * son champ de vision.
	 * @throws Exception
	 */
	@Test
	public void testPointsAround() throws Exception {
		
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),Math.PI/2,0,
				Color.BLACK,e,t);
		
		EnergySource pte = new EnergySource(new Point2D.Double(0,10),20);
		EnergySource pte1 = new EnergySource(new Point2D.Double(-10,10),20);
		EnergySource pte2 = new EnergySource(new Point2D.Double(10,10),20);

				
		ArrayList<EnergySource> ptl = (ArrayList<EnergySource>)new ArrayList<EnergySource>();
		ptl.add(pte);
		ptl.add(pte1);
		ptl.add(pte2);

		
		when(creature.getEnvironment().getEnergySources()).thenReturn(ptl);
		
		ArrayList<EnergySource> ptsAround = (ArrayList<EnergySource>)e.ptsAround(creature);
		e.setNextDirectionAndSpeed(creature);
		
		assertEquals(1, ptsAround.size());
		assertEquals(creature.getDirection(),-Math.PI/2,0.01);
		
	}
	
	/**
	 * Distance de perception de la creature.
	 * @throws Exception
	 */
	@Test
	public void testCreatureLengthOfView() throws Exception {
		
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(0,0),Math.PI/2,0,
				Color.BLACK,e,t);
		
		EnergySource pte = new EnergySource(new Point2D.Double(0,10),20);
		EnergySource pte1 = new EnergySource(new Point2D.Double(0,51),20);
		EnergySource pte2 = new EnergySource(new Point2D.Double(0,80),20); // Max = 80 car dans le apply il y a le +30
		EnergySource pte3 = new EnergySource(new Point2D.Double(0,81),20);// et par default LengthOfView = 50

				
		ArrayList<EnergySource> ptl = (ArrayList<EnergySource>)new ArrayList<EnergySource>();
		ptl.add(pte);
		ptl.add(pte1);
		ptl.add(pte2);
		ptl.add(pte3);

		
		when(creature.getEnvironment().getEnergySources()).thenReturn(ptl);
		
		ArrayList<EnergySource> ptsAround = (ArrayList<EnergySource>)e.ptsAround(creature);
		e.setNextDirectionAndSpeed(creature);
		
		//La creature doit voir seulement que 3 points.
		assertEquals(3, ptsAround.size());
		assertEquals(creature.getDirection(),-Math.PI/2,0.01);
		
	}
	
	/**
	 * Checks whether the creature moved closer to the energy point.
	 * @throws Exception
	 */
	@Test
	public void testCreatureGoToAnEnergyPoint() throws Exception {
		
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(2,5),Math.PI/2,6,
				Color.BLACK,e,t);
		
		EnergySource pte = new EnergySource(new Point2D.Double(0,10),20);
				
		ArrayList<EnergySource> ptl = (ArrayList<EnergySource>)new ArrayList<EnergySource>();
		ptl.add(pte);
		
		when(creature.getEnvironment().getEnergySources()).thenReturn(ptl);
		
		ArrayList<EnergySource> ptsAround = (ArrayList<EnergySource>)e.ptsAround(creature);
		
		double distanceBefore = creature.distanceFromAPoint(pte.getPosition());
		double vx = pte.getPosition().getX() - creature.getPosition().getX();
		double vy = pte.getPosition().getY() - creature.getPosition().getY();
		double angle = Math.atan2(vy, vx);
		
		e.setNextDirectionAndSpeed(creature);
		
		double distanceAfter = creature.distanceFromAPoint(pte.getPosition());
		double xAfter = creature.getPosition().getX();
		double yAfter = creature.getPosition().getY();
		
		assertEquals(1, ptsAround.size());
		assertEquals(creature.getDirection(),-angle,0.1);
		assertTrue(distanceAfter < distanceBefore);
		assertEquals(xAfter,pte.getPosition().getX(),1);
		assertEquals(yAfter,pte.getPosition().getY(),1);

		
	}
	
	/**
	 * Checks whether the creature don't moved closer to the energy point.
	 * @throws Exception
	 */
	@Test
	public void testCreatureDontGoToAnEnergyPoint() throws Exception {
		
		ComposableCreature creature = new ComposableCreature(environment,new Point2D.Double(2,5),Math.PI/2,6,
				Color.BLACK,e,t);
		
		EnergySource pte = new EnergySource(new Point2D.Double(-5,10),20);
				
		ArrayList<EnergySource> ptl = (ArrayList<EnergySource>)new ArrayList<EnergySource>();
		ptl.add(pte);
		
		when(creature.getEnvironment().getEnergySources()).thenReturn(ptl);
		
		ArrayList<EnergySource> ptsAround = (ArrayList<EnergySource>)e.ptsAround(creature);
		
		double distanceBefore = creature.distanceFromAPoint(pte.getPosition());
		double vx = pte.getPosition().getX() - creature.getPosition().getX();
		double vy = pte.getPosition().getY() - creature.getPosition().getY();
		double angle = Math.atan2(vy, vx);
		
		e.setNextDirectionAndSpeed(creature);
		
		double distanceAfter = creature.distanceFromAPoint(pte.getPosition());
		double xAfter = creature.getPosition().getX();
		double yAfter = creature.getPosition().getY();
		
		assertEquals(0, ptsAround.size());
		assertNotEquals(creature.getDirection(),-angle,0.1);
		assertTrue(distanceAfter >= distanceBefore);
		assertNotEquals(xAfter,pte.getPosition().getX(),1);
		assertNotEquals(yAfter,pte.getPosition().getY(),1);

		
	}

}
