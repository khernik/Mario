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
public class DestroyableBlock extends Terrain
{
    
    /**
     * @var Path to the texture
     */
    private String pathToImage = "destroyableBlock.jpeg";
    
    /**
     * Image displayed after mario hits the block
     */
    private String destroyAnimationImagePath = "destroyableBlockDestroyed.gif";
    
    /**
     * @var Was the block hit? (if it's destroyable block)
     */
    private boolean wasHit = false;
    
    /**
     * @var Time when the block was hit in miliseconds
     */
    public long destroyTimer = 0;
    
    /**
     * @var Is destroyable block visible?
     */
    private boolean visible = true;
    
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
    public DestroyableBlock(int[] coords)
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
    
     /**
     * Was the block hit?
     * 
     * @return
     */
    public boolean wasHit()
    {
        return wasHit;
    }
    
    /**
     * Displaying animation after mario hits the block
     */
    public void hit()
    {
        ImageIcon ii = new ImageIcon(destroyAnimationImagePath);
        texture = ii.getImage();
        wasHit = true;
    }
    
} // End DestroyableBlock
