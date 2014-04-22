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
    private static final double gravity = 9.8;
    
    /**
     * Constructor
     */
    public Physics()
    {
        updateTime();
    }
    
    /**
     * Updates start time
     * 
     * @param t 
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
     * @param t
     * @return next step
     */
    public int accelerate(double a, int v0, int vk)
    {
        double t = (double)( System.currentTimeMillis() - startTime )/1000;
        
        int v = v0 + (int)Math.floor(a*t);
        System.out.println(-v);
        return (v < vk) ? (-v) : (-vk);
    }
    
    public int gravitationalPull(int v0)
    {
        double t = (double)( System.currentTimeMillis() - startTime )/1000;

        int v = (int)Math.floor(v0 - gravity*t);

        return v;
    }
    
    public int gravitationalPull()
    {
        double t = (double)( System.currentTimeMillis() - startTime )/1000;

        int v = (int)Math.floor(-gravity*t);

        return v;
    }
    
} // End WorldPhysics
