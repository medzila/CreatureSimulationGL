package creatures;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import creatures.comportement.BouncingComportement;
import creatures.deplacement.TorusDeplacement;
import creatures.visual.CreatureSimulator;

public class BouncingComportementTest {

	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 100;
	final double h = 100;
	BouncingComportement b;
	TorusDeplacement t;
	
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		b = new BouncingComportement();
		t = new TorusDeplacement();
	}
	
	/**
	 * Verifie si la creature voit le point devant elle.
	 * @throws Exception
	 */
	@Test
	public void testBouncingNoise() throws Exception {
		
		CreatureComposable creature = new CreatureComposable(environment,new Point2D.Double(0,0),Math.PI/2,0,
				Color.BLACK,b,t);
		
		Point2D positionBefore = creature.getPosition();
		double directionBefore = creature.getDirection();
		double speedBefore = creature.getSpeed();
		for(int i=0;i<50;i++){
			b.applyNoise(creature);
		}
		b.setNextDirectionAndSpeed(creature);
		
		assertNotEquals(creature.getPosition(),positionBefore);
		assertNotEquals(creature.getDirection(),directionBefore,0.001);
		assertNotEquals(creature.getSpeed(),speedBefore,0.001);

		
				
	}

}
