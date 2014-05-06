package Terrain;

import Game.Main;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.HashMap;
import Bonus.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Brick-like surface
 * 
 * @author khernik
 */
public class BonusBlock extends Terrain
{
    
    /**
     * @var Path to the texture
     */
    private String pathToImage = new String("images/destroyableBlock" + Main.width + ".png");
    
    /**
     * @var Animation when the block is hit
     */
    private String discoverAnimationImagePath = new String("images/destroyableBlockHit" + Main.width + ".gif");
    
    /**
     * @var List of bonuses dropping out of this block
     */
    private java.util.Map<Integer, String> possibleBonuses;
    {
        possibleBonuses = new java.util.HashMap<Integer, String>();
        possibleBonuses.put(80, "money");
        possibleBonuses.put(15, "mushroom");
        possibleBonuses.put(5, "star");
    };   
    
    /**
     * @var Which bonus does this block have?
     */
    private Bonus currentBonus;
    
    /**
     * @var Was the block hit yet?
     */
    private boolean wasHit = false;
    
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
    public BonusBlock(int[] coords)
    {
        x1 = coords[0];
        y1 = coords[1];
        x2 = coords[2];
        y2 = coords[3];
        
        randomizeBonus();
    }
    
    /**
     * Randomize the bonus in the block based on chances
     * given in the map (see attributes)
     */
    private void randomizeBonus()
    {
        Random rand = new Random();        
        int rnum = rand.nextInt((10000 - 1) + 1) + 1;
        
        String bonusname = "";
        
        for (Map.Entry<Integer, String> entry : possibleBonuses.entrySet())
        {
            bonusname = entry.getValue();
            if(rnum < entry.getKey()) break;
        }
        
        try {
            Class myClass = Class.forName(bonusname);
            Class[] types = {int.class, int.class};
            Constructor constructor = myClass.getConstructor(types);
            currentBonus = (Bonus)constructor.newInstance(x1, y1);
        } catch(ClassNotFoundException e) { 
                
        } catch(NoSuchMethodException e) { 
                
        } catch(InstantiationException e) { 
                
        } catch(IllegalAccessException e) { 
              
        } catch(InvocationTargetException e) { 
                
        }
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
        counter += dx;
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
        ImageIcon ii = new ImageIcon(discoverAnimationImagePath);
        texture = ii.getImage();
        wasHit = true;
    }
    
} // End BonusBlock
