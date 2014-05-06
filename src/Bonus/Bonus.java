package Bonus;

/**
 * Handles bonuses
 * 
 * @author khernik
 */
public abstract class Bonus
{
    
    /**
     * @var Current position of the bonus
     */
    int x, y;
    
    /**
     * @var Changes in movement
     */
    int dx, dy;
    
    /**
     * @var Move the bonus through the map
     */
    public abstract void move();
    
} // End Bonus
