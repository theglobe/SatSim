import java.awt.*;

/** 
 * Klass som h�ller reda p� satellitens variabler samt har metoder 
 * f�r att �ndra eller komma �t dessa.
 */
class Satellite {
    // Satellitens massa
    private double mass;
    
    // Satellitens position
    private Point.Double position;
	
    // Hastigheten sparas som en punkt, egentligen avses hastighetsvektorns 
    // komponenter i x- och y-riktning.
    private Point.Double velocity;

    // Styrkommando som ska genomf�ras
    // h�ller koll p� styrimpulser (i x- och y-led)
    private Point.Double impulse;

    // Sant om satelliten har kolliderat.
    boolean hasCollided = false;
	
    // Konstruktor
    Satellite (double m, Point.Double p, Point.Double v) {
	mass     = m;
	position = p;
	velocity = v;
	impulse  = new Point.Double();  // satelliten initieras utan styrimpuls
    }
    
    /** 
     * Returnerar avst�ndet till origo.
     */
    double getDistance() {
    	return Math.sqrt(position.x*position.x + position.y*position.y);
    }

    /**
     * Returnerar satellitens banhastighet.
     */
    double getSpeed() {
	    return Math.sqrt(velocity.x*velocity.x + velocity.y*velocity.y);
    }
    
    
    /**
     * Nedanst�ende metoder anv�nds f�r att h�mta/st�lla in klassvariabler.
     */

    Point.Double getPosition() { return position; }
	
    Point.Double getVelocity() { return velocity; }
	
    void setPosition(Point.Double p) {
	    position = p;
    }
	
    void setVelocity(Point.Double v) {
	velocity = v;
    }
	
    double getMass() {
	return mass;
    }
	
    public void setMass(double m) {
	mass = m;
    }

    void setImpulse(Point.Double i) {
	impulse = i;
    }

    Point.Double getImpulse() {
	return impulse;
    }
}
