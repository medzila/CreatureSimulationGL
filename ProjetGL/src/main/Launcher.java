package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import plug.creatures.ColorPluginFactory;
import plug.creatures.BehaviorPluginFactory;
import plug.creatures.MovementPluginFactory;
import plug.creatures.PluginComboBoxItemBuilder;
import creatures.ICreature;
import creatures.behavior.IStrategyBehavior;
import creatures.EnergySource;
import creatures.color.IColorStrategy;
import creatures.movement.IStrategieMovement;
import creatures.visual.CreatureInspector;
import creatures.visual.CreatureSimulator;
import creatures.visual.CreatureVisualizer;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JMenu;

/**
 * Just a simple test of the simulator.
 * 
 */
@SuppressWarnings("serial")
public class Launcher extends JFrame {

	private final MovementPluginFactory movementFactory;
	private final BehaviorPluginFactory behaviorFactory;
	private final ColorPluginFactory colorFactory;
	
	Constructor<? extends IStrategyBehavior> behavior = null;
	IStrategieMovement movement = null;
	Constructor<? extends IColorStrategy> colorConstructor = null;
	
	private final CreatureInspector inspector;
	private final CreatureVisualizer visualizer;
	private final CreatureSimulator simulator;
	private JRadioButtonMenuItem rbMenuItem;
	
	private PluginComboBoxItemBuilder menuBuilder;
	private JMenuBar mb = new JMenuBar();	
	private Constructor<? extends ICreature> currentConstructor = null;
	private JMenu menu , submenu;
	private JPanel buttons = new JPanel(new GridBagLayout());
	
	int creatureNumber = 10;
	int spotsNumber = 10;
	int spotsSize = 50;
	
	public Launcher() {
		
		movementFactory = MovementPluginFactory.getInstance();
		behaviorFactory = BehaviorPluginFactory.getInstance();
		colorFactory = ColorPluginFactory.getInstance();
		
		setName("Creature Simulator Plugin Version");
		setLayout(new BorderLayout());
		
		add(buttons, BorderLayout.AFTER_LAST_LINE);
				
		simulator = new CreatureSimulator(new Dimension(640, 480));	
		inspector = new CreatureInspector();
		inspector.setFocusableWindowState(false);
		visualizer = new CreatureVisualizer(simulator);
		visualizer.setDebug(false);
		visualizer.setPreferredSize(simulator.getSize());
		visualizer.add(simulator.getLabelCreaturesTotal());
		visualizer.add(simulator.getLabelCreaturesDead());
		
		add(visualizer, BorderLayout.CENTER);

	    buildInterface();

	    pack();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				exit(evt);
			}
		});
		
	}
	
	private void exit(WindowEvent evt) {
		System.exit(0);
	}

	public void buildInterface() {	
		
buttons.removeAll();
		
		GridBagConstraints c = new GridBagConstraints();
		GridBagConstraints c1 = new GridBagConstraints();

		// La partie de l'interface pour definir la strategie de coloriage
		
		ActionListener colorListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// the name of the plugin is in the ActionCommand
				colorConstructor = colorFactory.getConstructorMap().get(((JComboBox) e.getSource()).getSelectedItem());
			}
		};
		
		JComboBox<String> colorComboBox = new JComboBox<String>();
		if (! colorFactory.getConstructorMap().keySet().isEmpty()) {
			for (String s: colorFactory.getConstructorMap().keySet()) {
				colorComboBox.addItem(s);
			}
		}
		else {
			colorComboBox.addItem("Aucun plugin trouver");
		}
		colorComboBox.addActionListener(colorListener);
		colorComboBox.setSelectedIndex(0);
		
		JPanel choiceColorPanel = new JPanel();
		choiceColorPanel.setLayout(new BorderLayout());
		JLabel labelColorCrea = new JLabel("Color", JLabel.CENTER);
		choiceColorPanel.add(labelColorCrea, BorderLayout.NORTH);
		choiceColorPanel.add(colorComboBox, BorderLayout.SOUTH);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		buttons.add(choiceColorPanel, c); 
		
		
		// La partie de l'interface pour definir la strategie de mouvement
		
		ActionListener movementListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// the name of the plugin is in the ActionCommand
				movement = movementFactory.getMap().get(((JComboBox) e.getSource()).getSelectedItem());
			}
		};
		
		JComboBox<String> movementComboBox = new JComboBox<String>();
		if (! movementFactory.getMap().keySet().isEmpty()) {
			for (String s: movementFactory.getMap().keySet()) {
				movementComboBox.addItem(s);
			}
		}
		else {
			movementComboBox.addItem("Aucun plugin trouver");
		}
		movementComboBox.addActionListener(movementListener);
		movementComboBox.setSelectedIndex(0);
		
		JPanel choiceMovementPanel = new JPanel();
		choiceMovementPanel.setLayout(new BorderLayout());
		JLabel labelMovementCrea = new JLabel("Movement", JLabel.CENTER);
		choiceMovementPanel.add(labelMovementCrea, BorderLayout.NORTH);
		choiceMovementPanel.add(movementComboBox, BorderLayout.SOUTH);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		buttons.add(choiceMovementPanel,c);
		
		
		// La partie de l'interface pour definir la strategie de comportement
		
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// the name of the plugin is in the ActionCommand
				behavior = behaviorFactory.getMap().get(((JComboBox) e.getSource()).getSelectedItem());
			}
		};
		
		JComboBox<String> actionComboBox = new JComboBox<String>();
		if (! behaviorFactory.getMap().keySet().isEmpty()) {
			for (String s: behaviorFactory.getMap().keySet()) {
				actionComboBox.addItem(s);
			}
		}
		else {
			actionComboBox.addItem("Aucun plugin trouver");
		}
		actionComboBox.addActionListener(actionListener);
		actionComboBox.setSelectedIndex(0);
		
		JPanel choiceActionPanel = new JPanel();
		choiceActionPanel.setLayout(new BorderLayout());
		JLabel labelActionCrea = new JLabel("Action", JLabel.CENTER);
		choiceActionPanel.add(labelActionCrea, BorderLayout.NORTH);
		choiceActionPanel.add(actionComboBox, BorderLayout.SOUTH);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 0;
		buttons.add(choiceActionPanel,c);
		
		
		// Le bouton pour recharger les plugins qui gere le coloriage
		
		JButton colorLoader = new JButton("(Re-)load color plugin");
		colorLoader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorFactory.reload();
				buildInterface();
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		buttons.add(colorLoader, c);
		
		
		// Le bouton pour recharger les plugins qui gere le deplacement

		JButton movementLoader = new JButton("(Re-)load movement plugin");
		movementLoader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				movementFactory.reload();
				buildInterface();
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		buttons.add(movementLoader, c);
		
		
		// Le bouton pour recharger les plugins qui gere le comportement

		JButton actionLoader = new JButton("(Re-)load acting plugin");
		actionLoader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				behaviorFactory.reload();
				buildInterface();
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 1;
		buttons.add(actionLoader, c);
		
		
		// Le slider pour qui gere le nombre de creatures
		
		JSlider numberOfCreaturesSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, 10);  
		numberOfCreaturesSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				creatureNumber = ((JSlider)e.getSource()).getValue();
			}
		});
		
		numberOfCreaturesSlider.setMinorTickSpacing(2);  
		numberOfCreaturesSlider.setMajorTickSpacing(10);  
		  
		numberOfCreaturesSlider.setPaintTicks(true);  
		numberOfCreaturesSlider.setPaintLabels(true); 
		
		JPanel sliderNumberCreaPanel = new JPanel();
		sliderNumberCreaPanel.setLayout(new BorderLayout());
		JLabel labelNumberCrea = new JLabel("Number of Creatures", JLabel.CENTER);
		sliderNumberCreaPanel.add(labelNumberCrea, BorderLayout.NORTH);
		sliderNumberCreaPanel.add(numberOfCreaturesSlider, BorderLayout.SOUTH);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		buttons.add(sliderNumberCreaPanel, c);  
		
		
		// Le slider pour qui gere le nombre de points d'energie

		JSlider numberOfEnergySlider = new JSlider(JSlider.HORIZONTAL, 0, 50, 10);  
		numberOfEnergySlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				spotsNumber = ((JSlider)e.getSource()).getValue();
			}
		});

		numberOfEnergySlider.setMinorTickSpacing(2);  
		numberOfEnergySlider.setMajorTickSpacing(10);  

		numberOfEnergySlider.setPaintTicks(true);  
		numberOfEnergySlider.setPaintLabels(true); 
		
		JPanel sliderNumberPanel = new JPanel();
		sliderNumberPanel.setLayout(new BorderLayout());
		JLabel labelNumber = new JLabel("Number of Energy", JLabel.CENTER);
		sliderNumberPanel.add(labelNumber, BorderLayout.NORTH);
		sliderNumberPanel.add(numberOfEnergySlider, BorderLayout.SOUTH);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 3;
		buttons.add(sliderNumberPanel, c);  

		
		// Le slider pour qui gere la taille de points d'energie

		JSlider sizeOfEnergySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);  
		sizeOfEnergySlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				spotsSize = ((JSlider)e.getSource()).getValue();
			}
		});

		sizeOfEnergySlider.setMinorTickSpacing(2);  
		sizeOfEnergySlider.setMajorTickSpacing(10);  

		sizeOfEnergySlider.setPaintTicks(true);  
		sizeOfEnergySlider.setPaintLabels(true);  
		
		JPanel sliderSizePanel = new JPanel();
		sliderSizePanel.setLayout(new BorderLayout());
		JLabel label = new JLabel("Size of Energy", JLabel.CENTER);
		sliderSizePanel.add(label, BorderLayout.NORTH);
		sliderSizePanel.add(sizeOfEnergySlider, BorderLayout.SOUTH);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 3;
		buttons.add(sliderSizePanel, c);  
		
		
		// Le button qui gere le (re-)demarrage de la simulation
		
		JButton restart = new JButton("(Re-)start simulation");
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((behavior != null) && (movement != null)) {
					synchronized(simulator) {
						if (simulator.isRunning()) {
							simulator.stop();
						}
					}
					Collection<? extends ICreature> creatures = null;
					double myMaxSpeed = 5;
					simulator.clearCreatures();
					simulator.clearSpots();
					simulator.clearStat();
					try {
						creatures = Builder.createCreatures(simulator, creatureNumber, colorConstructor.newInstance(Color.BLUE, creatureNumber),behavior.newInstance(), movement, myMaxSpeed);
						Collection<EnergySource> spots = Builder.createPoints(simulator, spotsNumber, spotsSize);
						simulator.addAllCreatures(creatures);
						simulator.addAllSpots(spots);
						simulator.start();
					} 
					catch (Exception exception) {
						JOptionPane.showMessageDialog(buttons, exception.getCause().getMessage(), "Missing mandatory behavior", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		buttons.add(restart, c);
		
		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.clearSpots();
				simulator.clearCreatures();
				simulator.clearStat();
				simulator.stop();
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 4;
		buttons.add(stopButton, c);
		
		revalidate();
		repaint();
		
		/*JButton reloader = new JButton("Reload plugins");
		reloader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				factory.reload();
				movementFactory.reload();
				actingFactory.reload();
				buildInterface();
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		buttons.add(reloader, c);*/
		
		/*mb.removeAll();
		
		
		menuBuilder = new PluginMenuItemBuilder(factory.getConstructorMap(),listener);
		menuBuilder.setMenuTitle("Creatures");
		menuBuilder.buildMenu();*/
		

		
		
		/*menu = new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_N);
		menu.getAccessibleContext().setAccessibleDescription(
		        "This menu does nothing");
		menu.addSeparator();
		submenu = new JMenu("Nombre de creatures");
		submenu.setMnemonic(KeyEvent.VK_S);
		menu.add(submenu);
		 
		mb.add(menu);
		
		rbMenuItem = new JRadioButtonMenuItem("RadioButton");
		rbMenuItem.setMnemonic(KeyEvent.VK_O);
		mb.add(rbMenuItem);

		setJMenuBar(mb);*/
	}


	
	
	public static void main(String args[]) {
	    Logger.getLogger("plug").setLevel(Level.INFO);
		MovementPluginFactory.init();
		BehaviorPluginFactory.init();
		ColorPluginFactory.init();
		Launcher launcher = new Launcher();
		launcher.setVisible(true);
	}
	
}


