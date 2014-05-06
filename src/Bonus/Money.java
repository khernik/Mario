package Bonus;
import javax.swing.ImageIcon;

/**
 *
 * @author Karol
 */
public class Money extends Bonus
{
    
    /**
     * @var Path to the image
     */
    private String pathToTheImage = "images/gold.png";
    
    /**
     * @var The texture of the bonus
     */
    private ImageIcon image;
    
    /**
     * @var Is money visible?
     */
    private boolean visible = true;
    
    /**
     * Set up the money
     * 
     * @param x
     * @param y 
     */
    public Money(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    /**
     * The money doesnt move
     */
    public void move()
    {
        // empty function here
    }
    
} // End Money
