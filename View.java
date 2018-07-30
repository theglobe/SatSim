import java.awt.*;
import java.awt.image.*;

/** Klassen View �r det omr�de d�r simuleringen ritas upp
 *  och ansvarar �ven f�r uppritningen av de olika objekten
 */

class View extends Canvas {
    // konstanter f�r ritytans bredd och h�jd
    private final static int width = 400*4;
    private final static int height = 400*4;
	
    // inst�llningar f�r f�rger
    private final static Color back_color = Color.black;
    private final static Color earth_color = Color.blue;
    private final static Color satellite_color = Color.yellow;
	
    /** Skalfaktor, anv�nds i skalningsmetoderna nedan.
      * Best�ms som ett f�rh�llande mellan ett antal pixlar och det avst�nd (i meter)
      * som de representerar.
      */
    private final static double scale = 100/36e6;
    
    /** Jordradien omr�knad till pixlar enligt skalfaktorn {@link View#scale}. */
    private final static int earth_radius = (int)(6350e3 * scale);

    // den modell vars objekt ritas upp
    private Model m;

    // variabler f�r dubbelbuffring
    private Image buffer;
    private Graphics g2;
	
    View(Model m) {
	this.m = m;
	setSize(width, height);
	setBackground(back_color);
		
	// Skapa buffert f�r dubbelbuffring och koppla ett Graphics-objekt till 
	// denna.
	buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	g2 = buffer.getGraphics();
	
	// Flytta origo till mitten. Ber�kningarna utg�r fr�n att jorden �r i 
	// origo, och det �r �nskv�rt att detta hamnar i mitten p� ritytan.
	g2.translate(width / 2, height / 2);
    }	
	
    /**
     * Egen version f�r dubbelbuffring.
     * Denna update() ser till att sk�rmen inte
     * rensas mellan uppritningarna.
     */
    public void update(Graphics g) {
		paint(g);
    }
		
    /**
     * Ritar upp alla synliga objekt.
     */
    public void paint(Graphics g) {
	// Rita en jord
	g2.setColor(earth_color);
	g2.fillOval(-earth_radius, -earth_radius, 
		    earth_radius * 2, earth_radius * 2);
			
	// Rita ut satelliten, om den har kraschat visa text.
	if (m.sat.hasCollided) {
	    g.setColor(Color.red);
	    g.drawString("Satelliten har kolliderat med jorden", 0, 380);
	}
	else {
	    int x=xToScreen(m.sat.getPosition().x);
	    int y=yToScreen(m.sat.getPosition().y);
	    g2.setColor(satellite_color);
	    g2.drawLine(x,y,x,y);
		
	    // kopiera buffer till sk�rmen
	    g.drawImage(buffer, 0, 0, this);
		
	    g.setColor(Color.white);
	    g.drawString("hastighet: " + (int)m.sat.getSpeed(), 0, 390);
	    g.drawString("avst�nd:   " + (int)m.sat.getDistance(), 0, 400);
	}
    }
    
    /**
     * Rensar ritytan genom att rita �ver den med bakgrundsf�rgen.
     */
    void clear() {
	g2.setColor(back_color);
	g2.fillRect(-width, -height, width*2, height*2);
    }
    
    /** 
     * Metoder f�r att transformera modellens koordinater till och fr�n 
     * sk�rmkoordinater, skalfaktor (definierad ovan) anv�nds.
     */
    
    static private int xToScreen (double x) {
	return (int)(scale * x);
    }
	
    static private int yToScreen (double y) {
	// Eftersom samma skala i x- och y-led.
	return xToScreen(y);
    }
}

