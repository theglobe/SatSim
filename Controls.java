import java.awt.*;
import java.awt.event.*;
import java.util.*;

/** 
 * Controls - kontroller i realtid f�r simuleringen.
 * Definierar fyra knappar f�r styrning i fyra riktningar samt en Scrollbar 
 * f�r att st�lla in styrimpulsens storlek.
 */

class Controls extends Panel implements ActionListener, AdjustmentListener {
    // den modell som kontrollerna p�verkar
    private Model m;

    // View-objekt associerat med kontrollerna
    private View v;

    private Panel buttons;
    private Panel scrollbars;
    private Scrollbar impulseScroll;
    private Scrollbar timeScroll;
    
    // Etiketter att skriva ut rullningslisternas v�rde p�.
    private Label impulseValue;
    private Label timeValue;
	
    // Konstruktorn l�gger till knappar och rullningslister
    Controls (Model m, View v) {
	this.m = m;
	this.v = v;
		
       	setLayout(new BorderLayout());	
	
	// Skapa en panel med knappar f�r varje riktning

	buttons = new Panel();
       	buttons.setLayout(new GridLayout(4, 3));

	addNoButton();
	addButton("Upp");
    	addNoButton();
    	addButton("V�nster");
    	addNoButton();
    	addButton("H�ger");
    	addNoButton();
    	addButton("Ner");
    	addNoButton();
    	addButton("Rensa");
	addNoButton();
	addButton("�terst�ll");

    	add("Center", buttons);
    	
    	
    	// Skapa panel med rullningslister.

	scrollbars = new Panel();
	scrollbars.setLayout(new GridLayout(2,3));

    	// Rullningslist f�r justering av styrimpulsens storlek.
	impulseScroll = new Scrollbar(Scrollbar.HORIZONTAL, 0, 10, 0, 10000);
    	impulseScroll.addAdjustmentListener(this);

	// L�t etikettens text vara rullningslistens v�rde fr�n b�rjan.
	impulseValue  = new Label(Integer.toString(impulseScroll.getValue()));
	
	Label impulseLabel = new Label ("Impuls");
	
	// Rullningslist f�r justering av tidsskalan.
	timeScroll = new Scrollbar(Scrollbar.HORIZONTAL, 100, 10, 1, 10010);
	timeScroll.addAdjustmentListener(this);
    	
	// Etiketten ska visa inst�llt v�rde fr�n b�rjan
	timeValue  = new Label("x" + Integer.toString(timeScroll.getValue()));

	Label timeLabel = new Label("Tidsskala");

	// Uppdatera modellen s� att dess inst�llning f�r tidsskala st�mmer 
	// �verenns med kontrollens
	m.setTimeScale(timeScroll.getValue());
 
	// L�gg till rullningslisterna och textetiketter
	scrollbars.add(impulseLabel);
    	scrollbars.add(impulseScroll);
	scrollbars.add(impulseValue);
	scrollbars.add(timeLabel);
    	scrollbars.add(timeScroll);
	scrollbars.add(timeValue);

	add("South", scrollbars);
	
    }
	
    /**
     * Metod som anropas d� anv�ndaren tryckt p� en knapp
     * skickar ett v�rde (med angiven riktning) till modellen.
     */
    public void actionPerformed(ActionEvent e) {
	String cmd = e.getActionCommand();
	if (cmd.equals("Upp")) {
	    m.addImpulse(0, -(double)impulseScroll.getValue());
	}
	else if (cmd.equals("Ner")) {
	    m.addImpulse(0, (double)impulseScroll.getValue());
	}
	else if (cmd.equals("V�nster")) {
	    m.addImpulse(-(double)impulseScroll.getValue(), 0);
	}
	else if (cmd.equals("H�ger")) {
	    m.addImpulse((double)impulseScroll.getValue(), 0);
	}
	else if (cmd.equals("Rensa")) {
	    v.clear();
	}
	else if (cmd.equals("�terst�ll")) {
	    v.clear(); // Rensa sk�rmen

	    // St�ll om satellitens l�ge och hastighet s� att den hamnar i den'
	    // geostation�ra banan och att den inte r�knas som kraschad.
	    m.sat.setPosition(new Point.Double(42164e3, 0));
	    m.sat.setVelocity(new Point.Double(0 ,3.07e3));
	    m.sat.hasCollided = false;
	}
    }

    /**
     * Hanterar scrollbarh�ndelser.
     */
    public void adjustmentValueChanged(AdjustmentEvent e) {
	if (e.getSource() == impulseScroll) {
	    // Uppdatera etiketten
	    impulseValue.setText(Integer.toString(impulseScroll.getValue()));
	}
	else if (e.getSource() == timeScroll) {
	    // Uppdatera etiketten
	    timeValue.setText("x" + Integer.toString(timeScroll.getValue()));
	    // �nda tidsskalningen i modellen
	    m.setTimeScale(timeScroll.getValue());
	}
    }
	
    /**
     * L�gger till en knapp till panelen buttons.
     */
    private void addButton(String command) {
    	Button b = new Button(command);
    	buttons.add(b);
    	b.addActionListener(this);
    }

    /**
     * L�gger till en tom panel som utfyllnad d�r ingen knapp �nskas.
     */
    private void addNoButton() {
    	buttons.add(new Panel());
    }
}



