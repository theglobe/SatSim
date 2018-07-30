import java.awt.*;
import java.awt.event.*;
import java.util.*;

/** 
 * Controls - kontroller i realtid för simuleringen.
 * Definierar fyra knappar för styrning i fyra riktningar samt en Scrollbar 
 * för att ställa in styrimpulsens storlek.
 */

class Controls extends Panel implements ActionListener, AdjustmentListener {
    // den modell som kontrollerna påverkar
    private Model m;

    // View-objekt associerat med kontrollerna
    private View v;

    private Panel buttons;
    private Panel scrollbars;
    private Scrollbar impulseScroll;
    private Scrollbar timeScroll;
    
    // Etiketter att skriva ut rullningslisternas värde på.
    private Label impulseValue;
    private Label timeValue;
	
    // Konstruktorn lägger till knappar och rullningslister
    Controls (Model m, View v) {
	this.m = m;
	this.v = v;
		
       	setLayout(new BorderLayout());	
	
	// Skapa en panel med knappar för varje riktning

	buttons = new Panel();
       	buttons.setLayout(new GridLayout(4, 3));

	addNoButton();
	addButton("Upp");
    	addNoButton();
    	addButton("Vänster");
    	addNoButton();
    	addButton("Höger");
    	addNoButton();
    	addButton("Ner");
    	addNoButton();
    	addButton("Rensa");
	addNoButton();
	addButton("Återställ");

    	add("Center", buttons);
    	
    	
    	// Skapa panel med rullningslister.

	scrollbars = new Panel();
	scrollbars.setLayout(new GridLayout(2,3));

    	// Rullningslist för justering av styrimpulsens storlek.
	impulseScroll = new Scrollbar(Scrollbar.HORIZONTAL, 0, 10, 0, 10000);
    	impulseScroll.addAdjustmentListener(this);

	// Låt etikettens text vara rullningslistens värde från början.
	impulseValue  = new Label(Integer.toString(impulseScroll.getValue()));
	
	Label impulseLabel = new Label ("Impuls");
	
	// Rullningslist för justering av tidsskalan.
	timeScroll = new Scrollbar(Scrollbar.HORIZONTAL, 100, 10, 1, 10010);
	timeScroll.addAdjustmentListener(this);
    	
	// Etiketten ska visa inställt värde från början
	timeValue  = new Label("x" + Integer.toString(timeScroll.getValue()));

	Label timeLabel = new Label("Tidsskala");

	// Uppdatera modellen så att dess inställning för tidsskala stämmer 
	// överenns med kontrollens
	m.setTimeScale(timeScroll.getValue());
 
	// Lägg till rullningslisterna och textetiketter
	scrollbars.add(impulseLabel);
    	scrollbars.add(impulseScroll);
	scrollbars.add(impulseValue);
	scrollbars.add(timeLabel);
    	scrollbars.add(timeScroll);
	scrollbars.add(timeValue);

	add("South", scrollbars);
	
    }
	
    /**
     * Metod som anropas då användaren tryckt på en knapp
     * skickar ett värde (med angiven riktning) till modellen.
     */
    public void actionPerformed(ActionEvent e) {
	String cmd = e.getActionCommand();
	if (cmd.equals("Upp")) {
	    m.addImpulse(0, -(double)impulseScroll.getValue());
	}
	else if (cmd.equals("Ner")) {
	    m.addImpulse(0, (double)impulseScroll.getValue());
	}
	else if (cmd.equals("Vänster")) {
	    m.addImpulse(-(double)impulseScroll.getValue(), 0);
	}
	else if (cmd.equals("Höger")) {
	    m.addImpulse((double)impulseScroll.getValue(), 0);
	}
	else if (cmd.equals("Rensa")) {
	    v.clear();
	}
	else if (cmd.equals("Återställ")) {
	    v.clear(); // Rensa skärmen

	    // Ställ om satellitens läge och hastighet så att den hamnar i den'
	    // geostationära banan och att den inte räknas som kraschad.
	    m.sat.setPosition(new Point.Double(42164e3, 0));
	    m.sat.setVelocity(new Point.Double(0 ,3.07e3));
	    m.sat.hasCollided = false;
	}
    }

    /**
     * Hanterar scrollbarhändelser.
     */
    public void adjustmentValueChanged(AdjustmentEvent e) {
	if (e.getSource() == impulseScroll) {
	    // Uppdatera etiketten
	    impulseValue.setText(Integer.toString(impulseScroll.getValue()));
	}
	else if (e.getSource() == timeScroll) {
	    // Uppdatera etiketten
	    timeValue.setText("x" + Integer.toString(timeScroll.getValue()));
	    // Ända tidsskalningen i modellen
	    m.setTimeScale(timeScroll.getValue());
	}
    }
	
    /**
     * Lägger till en knapp till panelen buttons.
     */
    private void addButton(String command) {
    	Button b = new Button(command);
    	buttons.add(b);
    	b.addActionListener(this);
    }

    /**
     * Lägger till en tom panel som utfyllnad där ingen knapp önskas.
     */
    private void addNoButton() {
    	buttons.add(new Panel());
    }
}



