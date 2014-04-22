package Terrain;

import Character.Mario;
import java.awt.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import Game.Physics;

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
     * @var Texture of the block
     */
    protected Image texture;
    
    /**
     * @var Dimension of the texture
     */
    protected int imageDim;
    
    /**
     * @var Physics used to accelerate movement - refreshing time
     * @see keyPressed, keyReleased methods
     */
    private Physics wp;
    
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
        
        for(int i = 0; i < getHeight()/imageDim; i++)
        {
            for(int j = 0; j < getWidth()/imageDim; j++)
            {
                coords.add(new ArrayList<Integer>());
                
                int x = getX1() + (imageDim*j);
                int y = 572 - (getY1() - (imageDim*i));
                
                coords.get((i*getWidth()/imageDim)+j).add(x);
                coords.get((i*getWidth()/imageDim)+j).add(y);
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
    public boolean checkCollisions(int marioX, int marioY)
    {
        // To get initial block x1 position
        int X1 = getX1() + (marioX - 50);

        boolean collisionWithLeftBorder = ( ( (marioX + 15) > (X1 - 6) && (marioX + 15) < (X1 + 6) )
                                            && marioY - 49 < getY1() && marioY - 49 > getY2())
                                            ? true : false;
        
        return collisionWithLeftBorder;
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
            wp = new Physics(); // refresh timer on -> release
            dx = 0;
        }
    }
    
} // End Terain
