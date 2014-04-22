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
     */
    private ListMultimap<String, int[]> terrainMap = ArrayListMultimap.create();
    {
        terrainMap.put("Terrain.Ground", new int[] {0,100,300,0});
        terrainMap.put("Terrain.Ground", new int[] {300,200,400,0});
        terrainMap.put("Terrain.Ground", new int[] {400,250,800,0});
        terrainMap.put("Terrain.Ground", new int[] {800,500,810,0});
        terrainMap.put("Terrain.Ground", new int[] {100,200,150,150});
        terrainMap.put("Terrain.Ground", new int[] {500,400,600,350});
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
        setSize(800, 600);

        // Create terrain objects out of tile matrix
        for (Map.Entry<String, int[]> entry : terrainMap.entries())
        {
            try {
                //System.out.println(entry.getValue()[0]);
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
        mario = new Mario(terrain.get(0).getY1() + 49);
        
        gravity = new Physics();
        
        timer = new Timer(30, this);
        timer.start();
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
        for(int i = 0; i < terrain.size(); i++) {
            for(List<Integer> coords : terrain.get(i).divideIntoBlocks())
                g2d.drawImage(terrain.get(i).getTexture(), coords.get(0), coords.get(1), null);
        }

        // Draw mario
        g2d.drawImage(mario.getImage(), mario.getX(), mario.getY(), null);
    }
    private int flag = 0;
    /**
     * Perform action on an event (key hit)
     * 
     * @param e 
     */
    public void actionPerformed(ActionEvent e)
    {
        mario.updateRelativePosition(50 - terrain.get(0).getX1());

        if(!checkTerrainCollisions())
        {
            for(int i = 0; i < terrain.size(); i++)
                terrain.get(i).move();
        }
        
        if(!checkMarioCollisions())
        {
            if(!mario.isMarioJumping())
            {
                if(flag == 0) gravity.updateTime();
                flag = 1;
                mario.y -= gravity.gravitationalPull(0);
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
            collides = terrain.get(i).checkCollisions(
                       mario.getRelativePosition(), 600 - mario.getY());
            if(collides) break;
        }
        
        return collides;
    }
    private int flag1 = 0;
    /**
     * Mario can't drop through the ground
     */
    private boolean checkVerticalCollisions()
    {
        boolean collides = false;
        
        if(mario.isMarioJumping()) flag1 = 0;
        
        int i = 0;
        for (Map.Entry<String, int[]> entry : terrainMap.entries())
        {
            if(mario.getRelativePosition() > entry.getValue()[0] && mario.getRelativePosition() < entry.getValue()[2]) 
            {
                if(550 - mario.getY() > entry.getValue()[1] - 12 && 550 - mario.getY() < entry.getValue()[1] || flag1 == 1)
                {
                    mario.y = 551 - entry.getValue()[1];
                    collides = true;
                    mario.jump = false;
                    break;
                }
            }
            i++;
        }
        
        return collides;
        
        /*
        // Calculate the height of the block mario is above
        int height = 0, i = 0;
        for (Map.Entry<String, int[]> entry : terrainMap.entries()) {
            System.out.println(mario.getRelativePosition());
            if(mario.getRelativePosition() > entry.getValue()[0] && mario.getRelativePosition() < entry.getValue()[2]) {
                    int tmp = terrain.get(i).getY1();
                    if(tmp >= 600-mario.getY()-50 && tmp >= height)
                            height = tmp;
            }
            i++;
        }
        System.out.println(height);
        height = 550 - height;
        
        if( mario.getY() > height) {
            mario.jump = false;
            mario.y -= 8;
            flag = 0;
        } else {
            if(mario.isMarioJumping())
            {
                mario.jump();
            } else {
                flag = 1;
                mario.y -= gravity.gravitationalPull(0);
            }
        }*/
    }
    
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
