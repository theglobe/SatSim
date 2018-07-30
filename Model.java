import java.awt.*;

/**
 * De matematiska ber�kningarna f�r satellitens r�relse utf�rs av denna klass.
 * Den utg�r �ven gr�nssnitt mot satelliten(-erna).
 */

class Model {
    // Konstanter i SI-enheter.
    
    // allm�nna gravitationskonstanten
    private final static double grav_const = 6.6720e-11;
    // jordens massa i kg
    private final static double earth_mass = 5.977e24;
    // jordens radie i m
    private final static double earth_radius = 6350e3;


    // Antal iterationer som utf�rs vid varje steg.
    private final static int NUM_ITERATIONS = 1000;

    // tidsintervall som simuleras f�r varje steg (i sekunder)
    private double stepTime = 1;

    // tid som g�r mellan ber�kningarna i realtid
    private double timeResolution = 0.005;

    // Skapa en satellit som ska simuleras.
    Satellite sat = new Satellite(100, new Point.Double(42164e3, 0),
					 new Point.Double(0 ,3.07e3)); 


    /**
     * Stegar fram simuleringen ett tidssteg. Denna metod sk�ter sj�lva 
     * ber�kningarna f�r modellen.
     * Nya v�rden f�r l�ge och hastighet ber�knas med hj�lp av Eulers stegmetod.
     * Ju st�rre antal iterationer (NUM_ITERATIONS) desto noggrannare ber�kningar.
     * till exempel: x(t + dt) = x(t) + dx/dt * t
     */
    synchronized void doStep() {
	// Ber�kna hur stort tidssteg varje iteration omfattar.
	double t = stepTime / NUM_ITERATIONS;

	if (!sat.hasCollided) {	
	    for (int i = 0; i < NUM_ITERATIONS; i++) {
		// ber�kna acceleration
		Point.Double accel = calcGravAccel(sat.getPosition());
		
		// uppdatera positioner (anv�nder ett medelv�rde av hastigheten under 
		// tidsintervallet)
		sat.getPosition().x += sat.getVelocity().x * t + accel.x * t*t / 2;
		sat.getPosition().y += sat.getVelocity().y * t + accel.y * t*t / 2;
				
		// uppdatera hastigheter
		sat.getVelocity().x += t * accel.x;
		sat.getVelocity().y += t * accel.y;
	    }
	
	    // uppdatera hastigheten med avseende p� styrimpuls
	    sat.getVelocity().x += sat.getImpulse().x / sat.getMass();
	    sat.getVelocity().y += sat.getImpulse().y / sat.getMass();

	    // nollst�ll impulsen
	    sat.setImpulse(new Point.Double(0,0));

	    // Kontrollera om satelliten har kolliderat med jorden
	    if (sat.getDistance() <= earth_radius) 
		sat.hasCollided = true;
	}
    }

    /**
     * L�gger till en styrimpuls som kan ske mellan tv� steg i simuleringen
     * gr�nssnittet till sattaliterna sk�ts helt av Model-klassen.
     */
    void addImpulse(double x, double y) {
	sat.setImpulse(new Point.Double(x, y));
    }
    
    /**
     * St�ller in tidsskalningen.
     */
    void setTimeScale(double t) {
	stepTime = t * timeResolution;
    }
    
    /**
     * St�ller in tidsuppl�sningen.
     */
    void setTimeResolution(double t) {
	timeResolution = t;
    }
 
    /**
     * Ber�knar tyngdkraftsaccelerationen i punkten p
     */
    private Point.Double calcGravAccel(Point.Double p) {
    	// R�knar ut del av uttrycket till en temp-variabel f�r att slippa g�ra 
	// det tv� g�nger.
    	// Kvadrering gen�mf�rs p� ett enkelt s�tt m.h.a. multiplikation (p.x*p.x)
    	
	double temp = - grav_const * earth_mass / 
	    Math.pow(p.x*p.x + p.y*p.y, (double)3/2);
    	
	return new Point.Double(p.x * temp, p.y * temp);
    }
}


