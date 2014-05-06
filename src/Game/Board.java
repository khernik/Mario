package Game;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import com.google.common.collect.*;
import Character.*;
import Terrain.*;
import java.lang.reflect.*;

/**
 * Handles all logic
 * 
 * @author khernik
 */
public class Board extends JPanel implements ActionListener
{
    
    /**
     * @var Determines if user is still able to play
     */
    private boolean ingame = true;
    
    /**
     * @var Timer handles animations
     */
    private Timer timer;
    
    /**
     * @var Animation speed
     */
    private int animationSpeed = 30;
    
    /**
     * @var Gravity constantly pushing mario down
     */
    private Physics gravity;
    
    /**
     * @var Mario himself
     */
    private Mario mario;
    
    /**
     * @var Mountain background
     */
    private Landscape landscape;

    /**
     * @var Where are terrain blocks?
     * 
     *      {x1,y1,x2,y2}
     * 
     * Pixels are for 800x600 natively
     */
    private ListMultimap<String, int[]> terrainMap = LinkedListMultimap.create();
    {
        terrainMap.put("Terrain.Ground", new int[] {0,100,300,0});
        terrainMap.put("Terrain.Ground", new int[] {150,150,200,100});
        terrainMap.put("Terrain.Ground", new int[] {300,200,400,0});
        terrainMap.put("Terrain.Ground", new int[] {400,250,800,0});
        terrainMap.put("Terrain.Ground", new int[] {800,500,810,0});
        terrainMap.put("Terrain.BonusBlock", new int[] {100,200,150,150});
        terrainMap.put("Terrain.DestroyableBlock", new int[] {500,400,600,350});
        terrainMap.put("Terrain.Chimney", new int[] {650,400,800,220});
    };
    
    /**
     * @var Array of surfaces block objects
     */
    private List<Terrain> terrain = new ArrayList<Terrain>();
    
    /**
     * Prepares the board
     */
    public Board()
    {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setDoubleBuffered(true);
        setSize(Main.width, Main.height);
        
        scaleWorld();
        
        // Create terrain objects out of tile matrix
        for (Map.Entry<String, int[]> entry : terrainMap.entries())
        {
            try {
                Class myClass = Class.forName(entry.getKey());
                Class[] types = {int[].class};
                Constructor constructor = myClass.getConstructor(types);
                terrain.add( (Terrain)constructor.newInstance(entry.getValue()) );
            } catch(ClassNotFoundException e) { 
                
            } catch(NoSuchMethodException e) { 
                
            } catch(InstantiationException e) { 
                
            } catch(IllegalAccessException e) { 
                
            } catch(InvocationTargetException e) { 
                
            }
        }
        
        // prepare background image
        landscape = new Landscape();
        
        // initialize main character on the given height
        mario = new Mario(terrain.get(0).getY1());
        
        gravity = new Physics();
        
        timer = new Timer(animationSpeed, this);
        timer.start();
    }
    
    /**
     * Scales the world matrix to adjust to the current resolution
     */
    private void scaleWorld()
    {        
        int hScale = (Main.height + 28)/600;
        int wScale = Main.width/800;

        for (Map.Entry<String, int[]> entry : terrainMap.entries())
        {
            entry.getValue()[0] /= wScale;
            entry.getValue()[2] /= wScale;
            entry.getValue()[1] /= hScale;
            entry.getValue()[3] /= hScale;
        }
    }
    
    /**
     * Paints all elements on the board
     * 
     * @param g
     * @TODO fixed height while drawing blocks
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D)g;

        landscape.draw(g);
        
        // Draw terrain elements
        for(int i = 0; i < terrain.size(); i++) 
        {
            // handle destroyed blocks
            if(terrain.get(i) instanceof DestroyableBlock) 
            {
                DestroyableBlock el = (DestroyableBlock)terrain.get(i);
                if(el.wasHit())
                {
                    el.destroyTimer = (el.destroyTimer == 0) ? System.currentTimeMillis() : el.destroyTimer;
                    if(System.currentTimeMillis() - el.destroyTimer > 200) terrain.remove(i);
                }
            }
            for(List<Integer> coords : terrain.get(i).divideIntoBlocks())
                g2d.drawImage(terrain.get(i).getTexture(), coords.get(0), coords.get(1), null);
        }
        
        // Draw mario
        g2d.drawImage(mario.getImage(), mario.getX(), mario.getY(), null);
    }
    
    int flag = 0; // prevents timer update on every pixel change while jumping
    /**
     * Perform action on an event (key hit)
     * 
     * @param e 
     */
    public void actionPerformed(ActionEvent e)
    {
        mario.updateRelativeTerrainPosition(mario.getX() - terrain.get(0).getX1());

        if(!checkTerrainCollisions())
        {
            if(mario.isMarioMovingThroughTheScreen())
            {
                mario.move();
            }
            else
            {
                for(int i = 0; i < terrain.size(); i++)
                terrain.get(i).move();
            }
        }
        
        if(!checkMarioCollisions())
        {
            if(!mario.isMarioJumping())
            {
                if(flag == 0) gravity.updateTime();
                flag = 1;
                mario.y -= gravity.gravitationalPull();
            }
            else
            {
                mario.jump();
            }
        }
        else
        {
            flag = 0;
        }
        
        repaint();
    }
    
    /**
     * Mario can't walk through the walls - checks collisions of mario
     * with the next block
     * 
     * @return
     */
    private boolean checkTerrainCollisions()
    {
        boolean collides = false;
        
        for(int i = 0; i < terrain.size(); i++) {
            collides = terrain.get(i).checkCollisions(mario);
            if(collides) break;
        }
        
        return collides;
    }
    
    /**
     * Mario can't drop through the ground
     */
    private boolean checkMarioCollisions()
    {        
        return mario.checkCollisions(terrainMap, terrain);
    }
    
    /**
     * Class handling key events
     */
    private class TAdapter extends KeyAdapter
    {
        
        /**
         * Add key released events to all objects on the frame
         * 
         * @param e 
         */
        public void keyReleased(KeyEvent e) 
        {            
            for(int i = 0; i < terrain.size(); i++) {
                terrain.get(i).keyReleased(e);
            }
            
            mario.keyReleased(e);
        }

        /**
         * Add key pressed events to all objects on the frame
         * 
         * @param e 
         */
        public void keyPressed(KeyEvent e) 
        {            
            for(int i = 0; i < terrain.size(); i++) {
                terrain.get(i).keyPressed(e);
            }
            
            mario.keyPressed(e);
            
            if(e.getKeyCode() == KeyEvent.VK_O) {
                landscape = new Landscape();
            }
        }
        
    }

} // End Board
