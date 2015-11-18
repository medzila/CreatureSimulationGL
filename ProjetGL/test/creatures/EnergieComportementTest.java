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
	
	/**
	 * Verifie si la creature voit le point devant elle.
	 * @throws Exception
	 */
	@Test
	public void testPointAround() throws Exception {
		
		CreatureComposable creature = new CreatureComposable(environment,new Point2D.Double(0,0),Math.PI/2,0,
				Color.BLACK,e,t);
		
		PointEnergie pte = new PointEnergie(new Point2D.Double(0,10),20);
				
		ArrayList<PointEnergie> ptl = (ArrayList<PointEnergie>)new ArrayList<PointEnergie>();
		ptl.add(pte);
		
		when(creature.getEnvironment().getPoints()).thenReturn(ptl);
		
		ArrayList<PointEnergie> ptsAround = (ArrayList<PointEnergie>)e.ptsAround(creature);
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
		
		CreatureComposable creature = new CreatureComposable(environment,new Point2D.Double(0,0),Math.PI/2,0,
				Color.BLACK,e,t);
		
		PointEnergie pte = new PointEnergie(new Point2D.Double(0,10),20);
		PointEnergie pte1 = new PointEnergie(new Point2D.Double(-10,10),20);
		PointEnergie pte2 = new PointEnergie(new Point2D.Double(10,10),20);

				
		ArrayList<PointEnergie> ptl = (ArrayList<PointEnergie>)new ArrayList<PointEnergie>();
		ptl.add(pte);
		ptl.add(pte1);
		ptl.add(pte2);

		
		when(creature.getEnvironment().getPoints()).thenReturn(ptl);
		
		ArrayList<PointEnergie> ptsAround = (ArrayList<PointEnergie>)e.ptsAround(creature);
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
		
		CreatureComposable creature = new CreatureComposable(environment,new Point2D.Double(0,0),Math.PI/2,0,
				Color.BLACK,e,t);
		
		PointEnergie pte = new PointEnergie(new Point2D.Double(0,10),20);
		PointEnergie pte1 = new PointEnergie(new Point2D.Double(0,51),20);
		PointEnergie pte2 = new PointEnergie(new Point2D.Double(0,80),20); // Max = 80 car dans le apply il y a le +30
		PointEnergie pte3 = new PointEnergie(new Point2D.Double(0,81),20);// et par default LengthOfView = 50

				
		ArrayList<PointEnergie> ptl = (ArrayList<PointEnergie>)new ArrayList<PointEnergie>();
		ptl.add(pte);
		ptl.add(pte1);
		ptl.add(pte2);
		ptl.add(pte3);

		
		when(creature.getEnvironment().getPoints()).thenReturn(ptl);
		
		ArrayList<PointEnergie> ptsAround = (ArrayList<PointEnergie>)e.ptsAround(creature);
		e.setNextDirectionAndSpeed(creature);
		
		//La creature doit voir seulement que 3 points.
		assertEquals(3, ptsAround.size());
		assertEquals(creature.getDirection(),-Math.PI/2,0.01);
		
	}

}
