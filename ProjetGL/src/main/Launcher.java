package main;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import plug.creatures.ComportementPluginFactory;
import plug.creatures.CreaturePluginFactory;
import plug.creatures.DeplacementPluginFactory;
import plug.creatures.PluginMenuItemBuilder;
import creatures.BouncingDeplacement;
import creatures.ColorCube;
import creatures.DeplacementTorus;
import creatures.ICreature;
import creatures.IStrategieComportement;
import creatures.IStrategieDeplacement;
import creatures.SmartComportement;
import creatures.StupidComportement;
import creatures.visual.CreatureInspector;
import creatures.visual.CreatureSimulator;
import creatures.visual.CreatureVisualizer;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JMenu;

/**
 * Just a simple test of the simulator.
 * 
 */
@SuppressWarnings("serial")
public class Launcher extends JFrame {

	private final CreaturePluginFactory factory;
	private final DeplacementPluginFactory movementFactory;
	private final ComportementPluginFactory actingFactory;
	
	IStrategieComportement compor = null;
	IStrategieDeplacement deplac = null;
	
	private final CreatureInspector inspector;
	private final CreatureVisualizer visualizer;
	private final CreatureSimulator simulator;
	private JRadioButtonMenuItem rbMenuItem;
	
	private PluginMenuItemBuilder menuBuilder;
	private JMenuBar mb = new JMenuBar();	
	private Constructor<? extends ICreature> currentConstructor = null;
	private JMenu menu , submenu;
	private JPanel buttons = new JPanel(new GridBagLayout());
	
	int creatureNumber = 10;
	
	public Launcher() {
		
		factory = CreaturePluginFactory.getInstance();
		movementFactory = DeplacementPluginFactory.getInstance();
		actingFactory = ComportementPluginFactory.getInstance();
		
		setName("Creature Simulator Plugin Version");
		setLayout(new BorderLayout());
		
		add(buttons, BorderLayout.AFTER_LAST_LINE);
				
		simulator = new CreatureSimulator(new Dimension(640, 480), 4);	
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
		
		GridBagConstraints c = new GridBagConstraints();

		// La partie de l'interface pour definir la strategie de coloriage
		
		JTextField textFieldColor = new JTextField("Color:");
		textFieldColor.setEditable(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		buttons.add(textFieldColor, c);
		
		String[] colorStrings = { "ColorCube", "ColorUnic"};
		JComboBox<String> colorPicker = new JComboBox<>(colorStrings);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		buttons.add(colorPicker, c); 
		
		
		// La partie de l'interface pour definir la strategie de mouvement
		
		JTextField textFieldMovement = new JTextField("Movement:");
		textFieldMovement.setEditable(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 0;
		buttons.add(textFieldMovement, c);
		
		ActionListener movementListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// the name of the plugin is in the ActionCommand
				deplac = movementFactory.getConstructorMap().get(((JComboBox) e.getSource()).getSelectedItem());
			}
		};
		
		JComboBox<String> movementComboBox = new JComboBox<String>();
		if (! movementFactory.getConstructorMap().keySet().isEmpty()) {
			for (String s: movementFactory.getConstructorMap().keySet()) {
				movementComboBox.addItem(s);
			}
		}
		else {
			movementComboBox.addItem("Aucun plugin trouvé");
		}
		movementComboBox.addActionListener(movementListener);
		movementComboBox.setSelectedIndex(0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 0;
		buttons.add(movementComboBox,c);
		
		
		// La partie de l'interface pour definir la strategie de comportement
		
		JTextField textFieldAction = new JTextField("Acting:");
		textFieldAction.setEditable(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridy = 0;
		buttons.add(textFieldAction, c);
		
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// the name of the plugin is in the ActionCommand
				compor = actingFactory.getConstructorMap().get(((JComboBox) e.getSource()).getSelectedItem());
			}
		};
		
		JComboBox<String> actionComboBox = new JComboBox<String>();
		if (! actingFactory.getConstructorMap().keySet().isEmpty()) {
			for (String s: actingFactory.getConstructorMap().keySet()) {
				actionComboBox.addItem(s);
			}
		}
		else {
			actionComboBox.addItem("Aucun plugin trouvÃ©");
		}
		actionComboBox.addActionListener(actionListener);
		actionComboBox.setSelectedIndex(0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridy = 0;
		buttons.add(actionComboBox,c);
		
		
		// Le bouton pour recharger les plugins qui gere le coloriage
		
		JButton colorLoader = new JButton("(Re-)load color plugin");
		colorLoader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*factory.load();
				movementFactory.load();
				actingFactory.load();*/
				// Mettre le reload de la colorFactory
				buildInterface();
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
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
		c.gridx = 3;
		c.gridy = 1;
		buttons.add(movementLoader, c);
		
		
		// Le bouton pour recharger les plugins qui gere le comportement

		JButton actionLoader = new JButton("(Re-)load acting plugin");
		actionLoader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actingFactory.reload();
				buildInterface();
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridy = 1;
		buttons.add(actionLoader, c);
		
		
		// Le slider pour qui gere le nombre de creatures
		
		JTextField textFieldNumberCreatures = new JTextField("Number of creatures:");
		textFieldNumberCreatures.setEditable(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		buttons.add(textFieldNumberCreatures, c);
		
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
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 3;
		buttons.add(numberOfCreaturesSlider, c);  
		
		
		// Le slider pour qui gere le nombre de points d'energie
		
		JTextField textFieldNumberEnergy = new JTextField("Number of energy points:");
		textFieldNumberEnergy.setEditable(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 3;
		buttons.add(textFieldNumberEnergy, c);

		JSlider numberOfEnergySlider = new JSlider(JSlider.HORIZONTAL, 0, 50, 10);  
		numberOfEnergySlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				creatureNumber = ((JSlider)e.getSource()).getValue();
			}
		});

		numberOfEnergySlider.setMinorTickSpacing(2);  
		numberOfEnergySlider.setMajorTickSpacing(10);  

		numberOfEnergySlider.setPaintTicks(true);  
		numberOfEnergySlider.setPaintLabels(true);  

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 3;
		buttons.add(numberOfEnergySlider, c);  

		
		// Le slider pour qui gere le nombre de points d'energie
		
		JTextField textFieldSizeEnergy = new JTextField("Number of energy points:");
		textFieldSizeEnergy.setEditable(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridy = 3;
		buttons.add(textFieldSizeEnergy, c);

		JSlider sizeOfEnergySlider = new JSlider(JSlider.HORIZONTAL, 0, 50, 10);  
		sizeOfEnergySlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				creatureNumber = ((JSlider)e.getSource()).getValue();
			}
		});

		sizeOfEnergySlider.setMinorTickSpacing(2);  
		sizeOfEnergySlider.setMajorTickSpacing(10);  

		sizeOfEnergySlider.setPaintTicks(true);  
		sizeOfEnergySlider.setPaintLabels(true);  

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridy = 3;
		buttons.add(sizeOfEnergySlider, c);  
		
		
		// Le button qui gere le (re-)demarrage de la simulation
		
		JButton restart = new JButton("(Re-)start simulation");
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((compor != null) && (deplac != null)) {
					synchronized(simulator) {
						if (simulator.isRunning()) {
							simulator.stop();
						}
					}
					simulator.clearCreatures();
					simulator.clearStat();
					Collection<? extends ICreature> creatures = factory.createCreatures(simulator, creatureNumber, new ColorCube(creatureNumber),compor, deplac);
					simulator.addAllCreatures(creatures);
					simulator.start();
				}
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 4;
		buttons.add(restart, c);
		
		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.clearCreatures();
				simulator.stop();
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridy = 4;
		buttons.add(stopButton, c);
		
		
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
		double myMaxSpeed = 5;
		CreaturePluginFactory.init(myMaxSpeed);
		DeplacementPluginFactory.init();
		ComportementPluginFactory.init();
		Launcher launcher = new Launcher();
		launcher.setVisible(true);
	}
	
}


