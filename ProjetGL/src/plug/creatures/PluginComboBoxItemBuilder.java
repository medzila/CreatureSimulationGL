package plug.creatures;

import java.lang.reflect.*;
import java.util.Map;
import java.util.logging.Logger;
import java.awt.event.*;

import javax.swing.*;

import creatures.ICreature;
import plug.IPlugin;


public class PluginComboBoxItemBuilder {

  private JComboBox<String> comboBox;

  private Map<String,? extends IPlugin> map;
  
  private static Logger logger = Logger.getLogger("plug.Menu");

  public PluginComboBoxItemBuilder(Map<String,? extends IPlugin> mc) {
    comboBox = new JComboBox<String>();
    this.map = mc;
  }

  public void buildMenu() {
	    logger.info("Building plugin menu");
	    comboBox.removeAll();
		if (! map.keySet().isEmpty()) {
			for (String s: map.keySet()) {
				comboBox.addItem(s);
			}
		}
		else {
			comboBox.addItem("Aucun plugin trouvé");
		}
  }

  public JComboBox<String> getComboBox() {
    return comboBox;
  }

}
