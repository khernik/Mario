package Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

/**
 * Generate mountains background image
 * 
 * @author khernik
 */
public class Landscape 
{
    
    /**
     * @var rectangles array
     */
    private List<Rectangle> rectangles;

    /**
     * Prepare the image
     */
    public Landscape()
    {
        rectangles = new ArrayList<Rectangle>();
        
        Random rand = new Random();
        int first = rand.nextInt((Main.width - 1) + 1) + 1;
        rectangles.add(new Rectangle(0, Main.width - first, 1, first));
        
        int d = first % 2;
        if(d == 0) d = -1;

        for(int i = 1; i <= Main.width; i++) {
            int choose = rand.nextInt((1000 - 1) + 1) + 1;

            if(choose > 950) {
                d = -(d);
            }
            
            if(d == -1) {
                first += 1;                
            } else {
                first -= 1;
            }
           
            rectangles.add(new Rectangle(i, Main.width - first, 1, first));
        }
    }
    
    /**
     * Draw the image
     * 
     * @param g
     */
    public void draw(Graphics g) 
    {
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(new Color(92, 163, 99));
        
        for (Rectangle r : rectangles) {
            g2d.fillRect(r.x, r.y, r.width, r.height);
        }
    }
    
} // End Landscape
