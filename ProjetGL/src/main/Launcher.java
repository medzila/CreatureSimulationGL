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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import plug.creatures.CreaturePluginFactory;
import plug.creatures.PluginMenuItemBuilder;
import creatures.ColorCube;
import creatures.ICreature;
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

	private final CreaturePluginFactory factory;
	
	private final CreatureInspector inspector;
	private final CreatureVisualizer visualizer;
	private final CreatureSimulator simulator;
	private JRadioButtonMenuItem rbMenuItem;
	
	private PluginMenuItemBuilder menuBuilder;
	private JMenuBar mb = new JMenuBar();	
	private Constructor<? extends ICreature> currentConstructor = null;
	private JMenu menu , submenu;
	
	int creatureNumber = 10;
	
	public Launcher() {
		factory = CreaturePluginFactory.getInstance();
		GridBagConstraints c = new GridBagConstraints();

		setName("Creature Simulator Plugin Version");
		setLayout(new BorderLayout());
		
		JPanel buttons = new JPanel(new GridBagLayout());
		JButton loader = new JButton("Load plugins");
		loader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				factory.load();
				buildPluginMenus();
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		buttons.add(loader, c);

		JButton reloader = new JButton("Reload plugins");
		reloader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				factory.reload();
				buildPluginMenus();
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		buttons.add(reloader, c);
		
		JButton restart = new JButton("(Re-)start simulation");
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentConstructor != null) {
					synchronized(simulator) {
						if (simulator.isRunning()) {
							simulator.stop();
						}
					}
					simulator.clearCreatures();
					simulator.clearStat();
					Collection<? extends ICreature> creatures = factory.createCreatures(simulator, creatureNumber, new ColorCube(50),currentConstructor);
					simulator.addAllCreatures(creatures);
					simulator.start();
				}
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 0;
		buttons.add(restart, c);
		
		JButton boutonNouveau = new JButton("Bim ! Le bouton !");
		boutonNouveau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.clearCreatures();
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 0;
		buttons.add(boutonNouveau, c);
		
		JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 50, 10);  
		slider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				creatureNumber = ((JSlider)e.getSource()).getValue();
			}
		});
		
		slider.setMinorTickSpacing(2);  
		slider.setMajorTickSpacing(10);  
		  
		slider.setPaintTicks(true);  
		slider.setPaintLabels(true);  
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		buttons.add(slider, c);  
		
		String[] colorStrings = { "ColorCube", "ColorUnic"};
		JComboBox<String> colorPicker = new JComboBox<>(colorStrings);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		buttons.add(colorPicker, c); 
		
		
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

		
	
	    buildPluginMenus();

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

	public void buildPluginMenus() {	
		mb.removeAll();
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// the name of the plugin is in the ActionCommand
				currentConstructor = factory.getConstructorMap().get(((JComboBox) e.getSource()).getSelectedItem());
			}
		};
		/*menuBuilder = new PluginMenuItemBuilder(factory.getConstructorMap(),listener);
		menuBuilder.setMenuTitle("Creatures");
		menuBuilder.buildMenu();*/
		JComboBox test = new JComboBox();
		if (! factory.getConstructorMap().keySet().isEmpty()) {
			for (String s: factory.getConstructorMap().keySet()) {
				test.addItem(s);
			}
		}
		test.addActionListener(listener);
		test.setSelectedIndex(0);
		mb.add(test);

		
		/*menu = new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_N);
		menu.getAccessibleContext().setAccessibleDescription(
		        "This menu does nothing");
		menu.addSeparator();
		submenu = new JMenu("Nombre de creatures");
		submenu.setMnemonic(KeyEvent.VK_S);
		menu.add(submenu);
		 
		mb.add(menu);*/
		
		rbMenuItem = new JRadioButtonMenuItem("RadioButton");
		rbMenuItem.setMnemonic(KeyEvent.VK_O);
		mb.add(rbMenuItem);

		setJMenuBar(mb);
	}


	
	
	public static void main(String args[]) {
	    Logger.getLogger("plug").setLevel(Level.INFO);
		double myMaxSpeed = 5;
		CreaturePluginFactory.init(myMaxSpeed);
		Launcher launcher = new Launcher();
		launcher.setVisible(true);
	}
	
}


