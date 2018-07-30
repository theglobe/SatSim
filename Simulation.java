import java.lang.*;

/** 
 * Simulation: sköter stegningen i simuleringen genom att med vissa tidsintervall
 * anropa Model-klassens doStep() metod och därefter se till att skärmen 
 * uppdateras genom att anropa repaint() för View.
 */

class Simulation extends Thread {
    // håller koll på modell och View
    private Model m;
    private View  v;
	
    // tid mellan anrop av doStep() i millisekunder
    private final static double timeResolution = 5;

    Simulation (Model m, View v) {
	this.m = m;
	this.v = v;

	// Uppdatera modellen om den tidsupplösning som används.
	m.setTimeResolution(timeResolution / 1000);

	// Startar tråden
	start();
    }

    /**
     * Stegar fram modellen och begär omritning med jämna mellanrum.
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
