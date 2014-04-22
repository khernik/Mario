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
    public static final int v0 = 3;
    
    /**
     * @var Mario's acceleration
     */
    public static final double acceleration = 4;
    
    /**
     * @var Mario's position in the world (relative to the surface)
     */
    private int relativeTerrainPosition;
    
    /**
     * @var Distance from the left border of the frame (in pixels)
     */
    private int fixedPositionFromLeftScreenBorder = 50;
    
    /**
     * @var Map of different states of mario and images links to its animations
     */
    private Map<String, ImageIcon> statesMap;
    {
        statesMap = new HashMap<String, ImageIcon>();
        statesMap.put("standing", new ImageIcon("standing-mario.gif"));
        statesMap.put("running", new ImageIcon("running-mario.gif"));
    };
    
    /**
     * @var Is mario jumping?
     */
    private boolean jump = false;
    
    /**
     * @var Has mario bumped from something?
     */
    private boolean bump = false;
    
    /**
     * @var Initial jump time in milliseconds, difference used for wp
     * @see Physics wp attribute
     */
    private long initialJumpTime;
    
    /**
     * @var Jumping physics
     */
    private Physics wp;
    
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
     * @return 
     */
    public int getRelativeTerrainPosition()
    {
        return relativeTerrainPosition;
    }
    
    /**
     * Returns mario's position from the left screen border
     * 
     * @return 
     */
    public int getFixedPositionFromLeftScreenBorder()
    {
        return fixedPositionFromLeftScreenBorder;
    }
    
    /**
     * Move mario
     * 
     * @param int Position x coordinate
     */
    public void updateRelativeTerrainPosition(int pos)
    {
        relativeTerrainPosition = pos;
    }
    
    /**
     * Has mario jumped?
     * 
     * @return 
     */
    public boolean isMarioJumping()
    {
        return jump;
    }
    
    /**
     * Has mario bumped?
     * 
     * @return 
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
     * @return 
     */
    public boolean checkCollisions(ListMultimap<String, int[]> terrainMap, List<Terrain> terrain)
    {
        boolean collides = false;
        
        if(isMarioJumping()) flag = 0;
        
        int i = 0; 
        for (Map.Entry<String, int[]> entry : terrainMap.entries())
        {
            // Check x mario borders
            if(getRelativeTerrainPosition() > entry.getValue()[0] && 
               getRelativeTerrainPosition() < entry.getValue()[2]) 
            {
                // Check top borders
                if( Main.height - getImageHeight() - getY() > entry.getValue()[1] - 12 && 
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
        
        if(key == KeyEvent.VK_RIGHT) {
            image = statesMap.get("running");
        }
        
        if(key == KeyEvent.VK_SPACE) {
            if(!this.isMarioJumping()) { // flag to stop double jumping when hitting
                                         // space two times
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
        
        if(key == KeyEvent.VK_RIGHT) {
            image = statesMap.get("standing");
        }
    }
    
} // End Mario
