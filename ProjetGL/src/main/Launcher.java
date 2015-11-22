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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import plug.creatures.ColorPluginFactory;
import plug.creatures.BehaviorPluginFactory;
import plug.creatures.MovementPluginFactory;
import creatures.ComposableCreature;
import creatures.ICreature;
import creatures.behavior.IStrategyBehavior;
import creatures.EnergySource;
import creatures.color.IColorStrategy;
import creatures.movement.IStrategieMovement;
import creatures.visual.CreatureInspector;
import creatures.visual.CreatureSimulator;
import creatures.visual.CreatureVisualizer;



/**
 * Just a simple test of the simulator.
 *
 */
@SuppressWarnings("serial")
public class Launcher extends JFrame {
    
    private final MovementPluginFactory movementFactory;
    private final BehaviorPluginFactory behaviorFactory;
    private final ColorPluginFactory colorFactory;
    
    public Constructor<? extends IStrategyBehavior> behavior = null;
    public IStrategieMovement movement = null;
    public Constructor<? extends IColorStrategy> colorConstructor = null;
    
    private final CreatureInspector inspector;
    private final CreatureVisualizer visualizer;
    private final CreatureSimulator simulator;
    
    private JPanel buttons = new JPanel(new GridBagLayout());
    private JPanel buttonsAndTreshold = new JPanel(new GridBagLayout());
    private JPanel threshold = new JPanel(new GridBagLayout());
    
    public int creatureNumber = 10;
    public int spotsNumber = 10;
    public int spotsSize = 50;
    
    public static float THRESHOLD = (float) (ComposableCreature.DEFAULT_HEALTH / 2);
    
    public Launcher() {
        
        movementFactory = MovementPluginFactory.getInstance();
        behaviorFactory = BehaviorPluginFactory.getInstance();
        colorFactory = ColorPluginFactory.getInstance();
        
        setName("Creature Simulator Plugin Version");
        setLayout(new BorderLayout());
        add(buttonsAndTreshold, BorderLayout.AFTER_LAST_LINE);
        
        simulator = new CreatureSimulator(new Dimension(640, 480));    
        inspector = new CreatureInspector();
        inspector.setFocusableWindowState(false);
        visualizer = new CreatureVisualizer(simulator);

        visualizer.setDebug(false);
        visualizer.setPreferredSize(simulator.getSize());
        visualizer.add(simulator.getLabelCreaturesTotal());
        visualizer.add(simulator.getLabelCreaturesDead());
        visualizer.add(simulator.getLabelCreaturesLife());
                
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

    @SuppressWarnings("rawtypes")
    public void buildInterface() {    
        
        buttons.removeAll();
        threshold.removeAll();
        
        GridBagConstraints c = new GridBagConstraints();

            JSlider tresholdSlider = new JSlider(JSlider.VERTICAL, 0, (int)ComposableCreature.DEFAULT_HEALTH, (int)ComposableCreature.DEFAULT_HEALTH/2);          
            tresholdSlider.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    Launcher.THRESHOLD = (float) ((JSlider)e.getSource()).getValue();
                }
            });
            
            tresholdSlider.setMinorTickSpacing(2);  
            tresholdSlider.setMajorTickSpacing(25);  
            
            tresholdSlider.setPaintLabels(true);
            tresholdSlider.setPreferredSize(new Dimension(50, 135));
            JPanel sliderTresholdPanel = new JPanel(new BorderLayout());
            JLabel labelTreshold = new JLabel("Threshold", JLabel.CENTER);
            sliderTresholdPanel.add(labelTreshold, BorderLayout.NORTH);
            sliderTresholdPanel.add(tresholdSlider, BorderLayout.SOUTH);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            threshold.add(sliderTresholdPanel, c);  
        
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
            colorComboBox.addItem("No plugin found");
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
            movementComboBox.addItem("No plugin found");
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
            actionComboBox.addItem("No plugin found");
        }
        actionComboBox.addActionListener(actionListener);
        actionComboBox.setSelectedIndex(0);
        
        JPanel choiceActionPanel = new JPanel();
        choiceActionPanel.setLayout(new BorderLayout());
        JLabel labelActionCrea = new JLabel("Behavior", JLabel.CENTER);
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
        
        c.fill =  GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        buttonsAndTreshold.add(buttons, c);

        c.gridx = 1;
        c.gridy = 0;
        buttonsAndTreshold.add(threshold, c);

        revalidate();
        repaint();
        
    }

    public static void main(String args[]) {
        Logger.getLogger("plug").setLevel(Level.INFO);
        MovementPluginFactory.init();
        BehaviorPluginFactory.init();
        ColorPluginFactory.init();
        Launcher launcher = new Launcher();
        launcher.setVisible(true);
        if (args[0].equals("1")){
        	launcher.behavior = BehaviorPluginFactory.getInstance().getMap().get("creatures.behavior.EmergingBehavior");
        	launcher.movement = MovementPluginFactory.getInstance().getMap().get("creatures.movement.TorusMovement");
        	launcher.colorConstructor = ColorPluginFactory.getInstance().getConstructorMap().get("color.ColorCube");
        	launcher.creatureNumber = 20;
            launcher.spotsNumber = 5;
            launcher.spotsSize = 100;
        }
    }
    
}