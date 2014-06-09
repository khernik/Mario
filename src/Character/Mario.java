package Character;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import Game.*;
import Terrain.*;
import com.google.common.collect.*;

/**
 * Mario
 * 
 * @author khernik
 */
public class Mario extends Character
{
    
    /**
     * @var Maximum Mario's velovity (px/frame)
     */
    public static final int vk = 7;
    
    /**
     * @var Mario's initial velocity when running
     */
    public static final int v0 = 7;
    
    /**
     * @var Mario's acceleration
     */
    public static final double acceleration = 4;
    
    /**
     * @var Mario's position in the world (relative to the surface)
     */
    private int relativeTerrainPosition;
    
    /**
     * @var Distance from the left border of the frame (in pixels) - it's the
     *      initial position on which mario stands while the game is being started
     */
    private int fixedPositionFromLeftScreenBorder = 50;
    
    /**
     * @var Is mario moving through the screen?
     */
    private boolean isMarioMoving = true;
    
    /**
     * @var Map of different states of mario and images links to its animations
     */
    private Map<String, ImageIcon> statesMap;
    {
        statesMap = new HashMap<String, ImageIcon>();
        statesMap.put("standing", new ImageIcon("images/standing-mario800.gif"));
        statesMap.put("running", new ImageIcon("images/running-mario800.gif"));
    };
    
    /**
     * @var Is mario jumping?
     */
    private boolean jump = false;
    
    /**
     * @var Has mario bumped into something?
     */
    private boolean bump = false;
    
    /**
     * @var Initial jump time in milliseconds, difference used for wp
     */
    private long initialJumpTime;
    
    /**
     * @var The direction of the movement
     * r - right, l - left
     */
    private char direction = 'r';
    
    /**
     * @var Jumping physics timer
     */
    private Physics wp;
    
    /**
     * @var Running physics timer
     */
    private Physics wpRunning;
    
    /**
     * Initialize mario
     * 
     * @param y Used to place mario on the first block
     */
    public Mario(int y)
    {
        // Set mario on the horizontal axis
        this.x = fixedPositionFromLeftScreenBorder;
        
        // Set initial relative mario position
        this.updateRelativeTerrainPosition(this.x);
        
        // Set mario in the first state while he's standing
        image = statesMap.get("standing");

        // Set mario on the vertical axis
        this.y = Main.height - getImageHeight() - y + 1;
        
        visible = true;
    }
    
    /**
     * Returns relative to the surface horizontal coordinates of where mario is
     * 
     * @return position
     */
    public int getRelativeTerrainPosition()
    {
        return relativeTerrainPosition;
    }
    
    /**
     * Returns mario's position from the left screen border
     * 
     * @return position
     */
    public int getFixedPositionFromLeftScreenBorder()
    {
        return fixedPositionFromLeftScreenBorder;
    }
    
    /**
     * Can mario move through the screen?
     * 
     * @return boolean
     */
    public boolean isMarioMovingThroughTheScreen()
    {
        // Prevent bugs if space key is hit while moving
        if(getX() + 15 < getFixedPositionFromLeftScreenBorder())
        {
            x = getFixedPositionFromLeftScreenBorder();
            isMarioMoving = false;
        }
        else if(getX() - 15 > (Main.width/2 - 50))
        {
            x = Main.width/2 - 50;
            isMarioMoving = false;
        }
        
        return isMarioMoving;
    }
    
    /**
     * Can mario move to the left?
     * 
     * @return boolean
     */
    private boolean canMarioMoveLeft()
    {
        return ( getX() >= getFixedPositionFromLeftScreenBorder() );
    }
    
    /**
     * Can mario move to the right?
     * 
     * @return boolean
     */
    private boolean canMarioMoveRight()
    {
        return ( getX() <= (Main.width/2 - 50) );
    }
    
    /**
     * In which direction is mario currently moving?
     * 
     * @return direction (r - right, l - left)
     */
    public char getDirection()
    {
        return direction;
    }
    
    /**
     * Move mario
     * 
     * @param pos
     */
    public void updateRelativeTerrainPosition(int pos)
    {
        relativeTerrainPosition = pos;
    }
    
    /**
     * Has mario jumped?
     * 
     * @return boolean
     */
    public boolean isMarioJumping()
    {
        return jump;
    }
    
    /**
     * Has mario bumped into something?
     * 
     * @return boolean
     */
    public boolean hasMarioBumped()
    {
        return bump;
    }

    /**
     * Change mario's jumping boolean state
     * 
     * @param state 
     */
    public void setMarioJumpingState(boolean state)
    {
        this.jump = state;
    }
    
    /**
     * Change mario's bumping boolean state
     * 
     * @param state 
     */
    public void setMarioBumpingState(boolean state)
    {
        this.bump = state;
    }
    
    /**
     * Move mario horizontally
     */
    public void move()
    {
        x += dx;
    }
    
    /**
     * Perform jump
     */
    public void jump()
    {
        this.y += (-wp.gravitationalPull(12));
    }
    
    private int flag = 0;
    /**
     * Check collisions
     * 
     * @param terrainMap
     * @param terrain
     * @return boolean Collides or not? 
     */
    public boolean checkCollisions(ListMultimap<String, int[]> terrainMap, List<Terrain> terrain)
    {
        boolean collides = false;
        
        if(isMarioJumping()) flag = 0;
        
        int i = 0; 
        for (Map.Entry<String, int[]> entry : terrainMap.entries())
        {
            // Check x mario borders
            if(getRelativeTerrainPosition() + getImageWidth() > entry.getValue()[0] && 
               getRelativeTerrainPosition() < entry.getValue()[2])
            {
                // Check top borders
                if( Main.height - getImageHeight() - getY() > entry.getValue()[1] - 20 && 
                    Main.height - getImageHeight() - getY() < entry.getValue()[1] || flag == 1)
                {
                    y = Main.height - getImageHeight() - entry.getValue()[1];
                    collides = true;
                    setMarioJumpingState(false);
                    break;
                }
                // Check bottom borders
                else if( Main.height - getY() < entry.getValue()[3] + 12 && 
                         Main.height - getY() > entry.getValue()[3] && !hasMarioBumped())
                {
                    y = Main.height - entry.getValue()[3];
                    collides = true;
                    setMarioJumpingState(false);
                    if(terrain.get(i) instanceof DestroyableBlock) {
                        ((DestroyableBlock)terrain.get(i)).hit();
                        terrainMap.remove(entry.getKey(), entry.getValue());
                    }
                    break;
                }
                // Check standing on the surface
                else if (Main.height - getImageHeight() - getY() == entry.getValue()[1] && !isMarioJumping())
                {
                    collides = true;
                }
            }
            i++;
        }
        
        return collides;
    }
    
    /**
     * Key pressed bidings
     * 
     * @param e 
     */
    @Override
    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();
        
        if(wpRunning == null) wpRunning = new Physics(); // refresh timer
        
        //if(canMarioMoveLeft() && canMarioMoveRight()) isMarioMoving = true;
        //else isMarioMoving = false;
        
        if(key == KeyEvent.VK_RIGHT) 
        {
            image = statesMap.get("running");
            
            direction = 'r';
            
            if(canMarioMoveRight())
            {
                isMarioMoving = true;
                dx = -(wpRunning.accelerate(Mario.acceleration, Mario.v0, Mario.vk));
            }
            else
            {
                isMarioMoving = false;
            }
        }
        
        if(key == KeyEvent.VK_LEFT) 
        {
            image = statesMap.get("running");
            
            direction = 'l';
            
            if(canMarioMoveLeft())
            {
                isMarioMoving = true;
                dx = wpRunning.accelerate(Mario.acceleration, Mario.v0, Mario.vk);
            }
            else
            {
                isMarioMoving = false;
            }
        }
        
        if(key == KeyEvent.VK_SPACE) 
        {
             // flag to stop double jumping when hitting space key two times
            if(!this.isMarioJumping())
            {
                this.setMarioJumpingState(true);
                wp = new Physics();
                initialJumpTime = System.currentTimeMillis();
            }
        }
    }
    
    /**
     * Key release bindings
     * 
     * @param e 
     */
    @Override
    public void keyReleased(KeyEvent e)
    {        
        int key = e.getKeyCode();
        
        wpRunning = null;
        
        if(key == KeyEvent.VK_RIGHT) 
        {
            isMarioMoving = false;
            image = statesMap.get("standing");
            wpRunning = null; // refresh timer on -> release
            dx = 0;
        }
        
        if(key == KeyEvent.VK_LEFT) 
        {
            image = statesMap.get("standing");
            wpRunning = null; // refresh timer on -> release
            dx = 0;
        }
    }
    
} // End Mario
