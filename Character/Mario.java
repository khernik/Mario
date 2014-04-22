package Character;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import Game.*;
import Terrain.*;

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
    private static final int vk = 7;
    
    /**
     * @var Mario's initial velocity when running
     */
    private static final int v0 = 3;
    
    /**
     * @var Mario's acceleration
     */
    private static final double acceleration = 5;
    
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
    private Map<String, Image> statesMap;
    {
        statesMap = new HashMap<String, Image>();
        statesMap.put("standing", new ImageIcon("standing-mario.gif").getImage());
        statesMap.put("running", new ImageIcon("running-mario.gif").getImage());
    };
    
    /**
     * @var Is mario jumping?
     */
    public boolean jump = false;
    
    /**
     * @var Height of the block where space was hit
     */
    public int initialYJumpPosition;
    
    /**
     * @var Initial jump time in milliseconds
     */
    private long initialJumpTime;
    
    private Physics wp;
    
    /**
     * Initialize mario
     * 
     * @param y Used to place mario on the first block
     */
    public Mario(int y)
    {
        // Set mario on the horizontal axis
        this.x = fixedPosition;
        
        // Set mario on the vertical axis
        this.y = 600 - y;
        
        // Set initial relative mario position
        relativePosition = fixedPosition;
        
        // Set mario states (animations)
        setStates();
        
        // Set mario in the first state
        image = images.get(0);
        
        visible = true;        
    }
    
    /**
     * Set mario states - add images to the array
     */
    private void setStates()
    {
        for(int i = 0; i < states.length; i++) 
        {
            ImageIcon icon = new ImageIcon(states[i] + ".gif");
            images.add(icon.getImage());
        }
    }
    
    /**
     * Returns relative to the surface horizontal coordinates of where mario is
     * 
     * @return 
     */
    public int getRelativePosition()
    {
        return relativePosition;
    }
    
    /**
     * Move mario
     */
    public void updateRelativePosition(int pos)
    {
        relativePosition = pos;
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
     * Perform jump
     */
    public void jump()
    {
        int vel;
        int y = 500;
        vel = wp.gravitationalPull(12);
        this.y += (-vel);
        /*
        double t = ( System.currentTimeMillis() - initialJumpTime )/100;
        
        int h, a;
        h = (int)Math.floor(WorldPhysics.gravitationalPull(initialYJumpPosition-200, t/10));
        
        if(h > 0) {
            y = initialYJumpPosition - (300 - h);
        }
        else
        {
            a = (int)Math.floor(WorldPhysics.gravitationalPull(initialYJumpPosition-200, t/10));
            System.out.println(a);
            y = 300 - h;
        }*/
        //System.out.println(300-h);
    }
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();
        
        if(key == KeyEvent.VK_RIGHT) {
            image = images.get(1);
        }
        
        if(key == KeyEvent.VK_SPACE && jump == false) {
            wp = new Physics();
            jump = true;
            initialYJumpPosition = getY();
            initialJumpTime = System.currentTimeMillis();
            y--;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e)
    {        
        int key = e.getKeyCode();
        
        if(key == KeyEvent.VK_RIGHT) {
            image = images.get(0);
        }
    }
    
} // End Mario
