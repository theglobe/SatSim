import java.awt.*;
import java.applet.*;

/** 
 * Huvudklassen för själva appleten, initierar simuleringen
 */

public class SatSim extends Applet {
    private Model      m = new Model();
    
    // View vill veta Model för att komma åt data såsom positioner för de olika 
    // objekten
    private View       v = new View(m);
    
    private Controls   c = new Controls(m, v);
    private Simulation s = new Simulation(m, v);
	
    public void init() {
	setLayout(new BorderLayout());
	setSize(640*4, 480*4);
		
	add("West", v);
	add("East", c);

	setVisible(true);
    }
}
