package Terrain;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.KeyEvent;

/**
 * Brick-like surface
 * 
 * @author khernik
 */
public class UndestroyableBlock extends Terrain
{
    
    /**
     * @var Path to the texture
     */
    private String pathToImage = "UndestroyableBlock.jpeg";
    
    /**
     * Set image object
     */
    {
        ImageIcon ii = new ImageIcon(pathToImage);
        texture = ii.getImage();
        
        imageDimX = 50;
        imageDimY = 50;
    }
    
    /**
     * Set all coordinates
     * 
     * @param coords 
     */
    public UndestroyableBlock(int[] coords)
    {
        x1 = coords[0];
        y1 = coords[1];
        x2 = coords[2];
        y2 = coords[3];
    }
    
    /**
     * Move that block
     */
    public void move()
    {
        x1 += dx;
        y1 += dy;
        x2 += dx;
        y2 += dy;
    }
    
} // End UndestroyableBlock
