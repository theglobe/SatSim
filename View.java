import java.awt.*;
import java.awt.image.*;

/** Klassen View är det område där simuleringen ritas upp
 *  och ansvarar även för uppritningen av de olika objekten
 */

class View extends Canvas {
    // konstanter för ritytans bredd och höjd
    private final static int width = 400*4;
    private final static int height = 400*4;
	
    // inställningar för färger
    private final static Color back_color = Color.black;
    private final static Color earth_color = Color.blue;
    private final static Color satellite_color = Color.yellow;
	
    /** Skalfaktor, används i skalningsmetoderna nedan.
      * Bestäms som ett förhållande mellan ett antal pixlar och det avstånd (i meter)
      * som de representerar.
      */
    private final static double scale = 100/36e6;
    
    /** Jordradien omräknad till pixlar enligt skalfaktorn {@link View#scale}. */
    private final static int earth_radius = (int)(6350e3 * scale);

    // den modell vars objekt ritas upp
    private Model m;

    // variabler för dubbelbuffring
    private Image buffer;
    private Graphics g2;
	
    View(Model m) {
	this.m = m;
	setSize(width, height);
	setBackground(back_color);
		
	// Skapa buffert för dubbelbuffring och koppla ett Graphics-objekt till 
	// denna.
	buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	g2 = buffer.getGraphics();
	
	// Flytta origo till mitten. Beräkningarna utgår från att jorden är i 
	// origo, och det är önskvärt att detta hamnar i mitten på ritytan.
	g2.translate(width / 2, height / 2);
    }	
	
    /**
     * Egen version för dubbelbuffring.
     * Denna update() ser till att skärmen inte
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
		
	    // kopiera buffer till skärmen
	    g.drawImage(buffer, 0, 0, this);
		
	    g.setColor(Color.white);
	    g.drawString("hastighet: " + (int)m.sat.getSpeed(), 0, 390);
	    g.drawString("avstånd:   " + (int)m.sat.getDistance(), 0, 400);
	}
    }
    
    /**
     * Rensar ritytan genom att rita över den med bakgrundsfärgen.
     */
    void clear() {
	g2.setColor(back_color);
	g2.fillRect(-width, -height, width*2, height*2);
    }
    
    /** 
     * Metoder för att transformera modellens koordinater till och från 
     * skärmkoordinater, skalfaktor (definierad ovan) används.
     */
    
    static private int xToScreen (double x) {
	return (int)(scale * x);
    }
	
    static private int yToScreen (double y) {
	// Eftersom samma skala i x- och y-led.
	return xToScreen(y);
    }
}

