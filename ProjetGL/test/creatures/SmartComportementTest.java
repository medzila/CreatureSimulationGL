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

import creatures.comportement.SmartComportement;
import creatures.deplacement.TorusDeplacement;
import creatures.visual.CreatureSimulator;

public class SmartComportementTest {

	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 100;
	final double h = 100;
	SmartComportement s;
	TorusDeplacement t;
	
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		s = new SmartComportement();
		t = new TorusDeplacement();
	}
	
	/**
	 * Verifie si la creature voit le point devant elle.
	 * @throws Exception
	 */
	@Test
	public void testEmerginComportement() throws Exception {
		
		CreatureComposable creature = new CreatureComposable(environment,new Point2D.Double(0,0),toRadians(10),5,
				Color.BLACK,s,t);
		
		ICreature other = mock(ICreature.class);
		when(other.getDirection()).thenReturn(toRadians(270));
		when(other.getSpeed()).thenReturn(10.0);
		when(other.getPosition()).thenReturn(new Point2D.Double(1,0));
		when(other.distanceFromAPoint(eq(creature.getPosition()))).thenReturn(1.0);
		when(other.directionFormAPoint(eq(creature.getPosition()), eq(creature.getDirection()))).thenReturn(0.0);

		ArrayList<ICreature> creaturesAround = new ArrayList<ICreature>();
		creaturesAround.add(other);
		Point2D positionBefore = creature.getPosition();
		when(environment.getCreatures()).thenReturn(creaturesAround);
		when(environment.getPoints()).thenReturn(new ArrayList<PointEnergie>());
		
		s.setNextDirectionAndSpeed(creature);
		
		assertEquals(toRadians((270+10)/2), creature.getDirection(), .01);
		assertEquals((10.0+5.0)/2, creature.getSpeed(), .01);
				
	}

}