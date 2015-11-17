package creatures;

import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;
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

import creatures.comportement.EnergieComportement;
import creatures.deplacement.TorusDeplacement;
import creatures.visual.CreatureSimulator;

public class EnergieComportementTest {
	
	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 100;
	final double h = 100;
	EnergieComportement e;
	TorusDeplacement t;
	
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		e = new EnergieComportement();
		t = new TorusDeplacement();
	}
	
	@Test
	public void testEmerginBehavior() throws Exception {
		
		CreatureComposable creature = new CreatureComposable(environment,new Point2D.Double(0, 1),Math.PI/2,0,
				Color.BLACK,e,t);
		
		PointEnergie pte = mock(PointEnergie.class);
		
		when(pte.getPosition()).thenReturn(new Point2D.Double(0,3));
		when(pte.getSize()).thenReturn(20);
		
		ArrayList<PointEnergie> ptl = (ArrayList<PointEnergie>)new ArrayList<PointEnergie>();
		ptl.add(pte);
		environment.addAllSpots(ptl);
		
		when(environment.getPoints()).thenReturn(ptl);
		
		System.out.println(creature.getPosition()+" "+pte.getPosition()+" "+creature.getDirection()+" "+creature.getEnvironment().getPoints());

		e.setNextDirectionAndSpeed(creature);
		
		System.out.println(creature.getPosition()+" "+pte.getPosition()+" "+creature.getDirection());
		assertEquals(1, environment.getSizePoints());
		
	}

}
