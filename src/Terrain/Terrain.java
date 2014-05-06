package Terrain;

import Character.Mario;
import java.awt.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import Game.*;

/**
 * Class responsible for displaying terrain elements
 * 
 * @author khernik
 */
public abstract class Terrain
{
    
    /**
     * @var Coordinates of the block
     */
    protected int x1, y1, x2, y2;
    
    /**
     * @var Movement of the block (always along with the character)
     */
    protected int dx, dy;
    
    /**
     * @var Variable used for calculating initial block's position based on the
     *      current position (how many times has dx changed?)
     */
    protected int counter = 0;
    
    /**
     * @var Texture of the block
     */
    protected Image texture;
    
    /**
     * @var Height imension of the texture
     */
    protected int imageDimX;
    
    /**
     * @var Width dimension of the texture
     */
    protected int imageDimY;
    
    /**
     * @var Physics used to accelerate movement - refreshing time
     * @see keyPressed, keyReleased methods
     */
    private Physics wp;
    
    {
        imageDimX *= Main.height/800;
        imageDimY *= Main.width/600;
    }
    
    /**
     * Method returning texture of the block
     * 
     * @return 
     */
    public Image getTexture()
    {
        return texture;
    }
    
    /**
     * Move that block
     */
    abstract public void move();
    
    /**
     * Get x1 coordinate
     * 
     * @return 
     */
    public int getX1()
    {
        return x1;
    }
    
    /**
     * Get y1 coordinate
     * 
     * @return 
     */
    public int getY1()
    {
        return y1;
    }
    
    /**
     * Get x2 coordinate
     * 
     * @return 
     */
    public int getX2()
    {
        return x2;
    }
    
    /**
     * Get y2 coordinate
     * 
     * @return 
     */
    public int getY2()
    {
        return y2;
    }
    
    /**
     * Get horizontal position change
     * 
     * @return 
     */
    public int getdX()
    {
        return dx;
    }
    
    /**
     * Get vertical position change
     * 
     * @return 
     */    
    public int getdY()
    {
        return dy;
    }
    
    /**
     * Returns width of the block
     * 
     * @return 
     */
    public int getHeight()
    {
        return getY1() - getY2();
    }
    
    /**
     * Returns height of the block
     * 
     * @return 
     */
    public int getWidth()
    {
        return getX2() - getX1();
    }
    
    /**
     * Divide the block into imageDim x imageDim texture matrix
     * 
     * @return
     */
    public List< List<Integer> > divideIntoBlocks()
    {
        List<List<Integer> > coords = new ArrayList< List<Integer> >();
        
        for(int i = 0; i < getHeight()/imageDimY; i++)
        {
            for(int j = 0; j < getWidth()/imageDimX; j++)
            {
                coords.add(new ArrayList<Integer>());

                int x = getX1() + (imageDimX*j);
                int y = Main.height - (getY1() - (imageDimY*i));

                coords.get((i*getWidth()/imageDimY)+j).add(x);
                coords.get((i*getWidth()/imageDimY)+j).add(y);
            }
        }

        return coords;
    }
    
    /**
     * Check collisions with the left border, and if true, stops the
     * movement
     * 
     * @param marioX
     * @param marioY 
     */
    public boolean checkCollisions(Mario mario)
    {
        // Find initial block x1 position
        int initX = mario.getRelativeTerrainPosition() + ((mario.getDirection() == 'r') ? getX1() : getX2()) - mario.getX();

        // Collisions of x coordinates with left or right border (based on direction)
        boolean xCoords = ( mario.getRelativeTerrainPosition() + ((mario.getDirection() == 'r') ? mario.getImageWidth() : 0) > (initX - 6) &&
                            mario.getRelativeTerrainPosition() + ((mario.getDirection() == 'r') ? mario.getImageWidth() : 0) < (initX + 6));

        // Collisions of y coordinates with left and right border (based on direction)
        boolean yCoords = ( Main.height - mario.getY() - mario.getImageHeight() < getY1() &&
                            Main.height - mario.getY() - mario.getImageHeight() >= getY2());

        return ( xCoords && yCoords ) ? true : false;
    }
    
    /**
     * Perform action on key pressed
     * 
     * @param e 
     */
    public final void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_RIGHT) {
            if(wp == null) 
                wp = new Physics(); // refresh timer after -> press only once
            dx = wp.accelerate(Mario.acceleration, Mario.v0, Mario.vk);
        }
    }
    
    /**
     * Perform action on key released
     * 
     * @param e 
     */
    public final void keyReleased(KeyEvent e)
    {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_RIGHT) {
            wp = null; // refresh timer on -> release
            dx = 0;
        }
    }
    
} // End Terain
