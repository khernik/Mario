package Game;

/**
 * Physics
 *  
 * @author khernik
 */
public class Physics
{
    
    /**
     * @var Start counting
     */
    public long startTime;
    
    /**
     * @var Gravity force
     */
    private static final double gravity = 15.0;
    
    /**
     * Constructor
     */
    public Physics()
    {
        updateTime();
    }
    
    /**
     * Updates start time
     */
    public void updateTime()
    {
        startTime = System.currentTimeMillis();
    }
    
    /**
     * Acceleration force
     * 
     *      a = v/t
     *      v = v0 + at
     * 
     * @param a
     * @param v0
     * @param vk
     * @return next step
     */
    public int accelerate(double a, int v0, int vk)
    {
        double t = (double)( System.currentTimeMillis() - startTime )/1000;
        
        int v = v0 + (int)Math.floor(a*t);

        return (v < vk) ? (-v) : (-vk);
    }
    
    /**
     * Jumping with v0 != 0
     * 
     * @param v0
     * @return velocity
     */
    public int gravitationalPull(int v0)
    {
        double t = (double)( System.currentTimeMillis() - startTime )/1000;

        int v = (int)Math.floor(v0 - gravity*t);

        return v;
    }
    
    /**
     * Jumping with v0 = 0
     * 
     * @return velocity
     */
    public int gravitationalPull()
    {
        double t = (double)( System.currentTimeMillis() - startTime )/1000;

        int v = (int)Math.floor(-gravity*t);

        return v;
    }
    
} // End Physics
