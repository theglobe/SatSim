import java.lang.*;

/** 
 * Simulation: sk�ter stegningen i simuleringen genom att med vissa tidsintervall
 * anropa Model-klassens doStep() metod och d�refter se till att sk�rmen 
 * uppdateras genom att anropa repaint() f�r View.
 */

class Simulation extends Thread {
    // h�ller koll p� modell och View
    private Model m;
    private View  v;
	
    // tid mellan anrop av doStep() i millisekunder
    private final static double timeResolution = 5;

    Simulation (Model m, View v) {
	this.m = m;
	this.v = v;

	// Uppdatera modellen om den tidsuppl�sning som anv�nds.
	m.setTimeResolution(timeResolution / 1000);

	// Startar tr�den
	start();
    }

    /**
     * Stegar fram modellen och beg�r omritning med j�mna mellanrum.
     */
    public void run () {
	while (!interrupted()) {
	    try { sleep(5); }
	    catch (InterruptedException e) {
		break;
	    }
	    m.doStep();
	    v.repaint();
	}
    }
}
