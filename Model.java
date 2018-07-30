import java.awt.*;

/**
 * De matematiska beräkningarna för satellitens rörelse utförs av denna klass.
 * Den utgör även gränssnitt mot satelliten(-erna).
 */

class Model {
    // Konstanter i SI-enheter.
    
    // allmänna gravitationskonstanten
    private final static double grav_const = 6.6720e-11;
    // jordens massa i kg
    private final static double earth_mass = 5.977e24;
    // jordens radie i m
    private final static double earth_radius = 6350e3;


    // Antal iterationer som utförs vid varje steg.
    private final static int NUM_ITERATIONS = 1000;

    // tidsintervall som simuleras för varje steg (i sekunder)
    private double stepTime = 1;

    // tid som går mellan beräkningarna i realtid
    private double timeResolution = 0.005;

    // Skapa en satellit som ska simuleras.
    Satellite sat = new Satellite(100, new Point.Double(42164e3, 0),
					 new Point.Double(0 ,3.07e3)); 


    /**
     * Stegar fram simuleringen ett tidssteg. Denna metod sköter själva 
     * beräkningarna för modellen.
     * Nya värden för läge och hastighet beräknas med hjälp av Eulers stegmetod.
     * Ju större antal iterationer (NUM_ITERATIONS) desto noggrannare beräkningar.
     * till exempel: x(t + dt) = x(t) + dx/dt * t
     */
    synchronized void doStep() {
	// Beräkna hur stort tidssteg varje iteration omfattar.
	double t = stepTime / NUM_ITERATIONS;

	if (!sat.hasCollided) {	
	    for (int i = 0; i < NUM_ITERATIONS; i++) {
		// beräkna acceleration
		Point.Double accel = calcGravAccel(sat.getPosition());
		
		// uppdatera positioner (använder ett medelvärde av hastigheten under 
		// tidsintervallet)
		sat.getPosition().x += sat.getVelocity().x * t + accel.x * t*t / 2;
		sat.getPosition().y += sat.getVelocity().y * t + accel.y * t*t / 2;
				
		// uppdatera hastigheter
		sat.getVelocity().x += t * accel.x;
		sat.getVelocity().y += t * accel.y;
	    }
	
	    // uppdatera hastigheten med avseende på styrimpuls
	    sat.getVelocity().x += sat.getImpulse().x / sat.getMass();
	    sat.getVelocity().y += sat.getImpulse().y / sat.getMass();

	    // nollställ impulsen
	    sat.setImpulse(new Point.Double(0,0));

	    // Kontrollera om satelliten har kolliderat med jorden
	    if (sat.getDistance() <= earth_radius) 
		sat.hasCollided = true;
	}
    }

    /**
     * Lägger till en styrimpuls som kan ske mellan två steg i simuleringen
     * gränssnittet till sattaliterna sköts helt av Model-klassen.
     */
    void addImpulse(double x, double y) {
	sat.setImpulse(new Point.Double(x, y));
    }
    
    /**
     * Ställer in tidsskalningen.
     */
    void setTimeScale(double t) {
	stepTime = t * timeResolution;
    }
    
    /**
     * Ställer in tidsupplösningen.
     */
    void setTimeResolution(double t) {
	timeResolution = t;
    }
 
    /**
     * Beräknar tyngdkraftsaccelerationen i punkten p
     */
    private Point.Double calcGravAccel(Point.Double p) {
    	// Räknar ut del av uttrycket till en temp-variabel för att slippa göra 
	// det två gånger.
    	// Kvadrering genömförs på ett enkelt sätt m.h.a. multiplikation (p.x*p.x)
    	
	double temp = - grav_const * earth_mass / 
	    Math.pow(p.x*p.x + p.y*p.y, (double)3/2);
    	
	return new Point.Double(p.x * temp, p.y * temp);
    }
}


