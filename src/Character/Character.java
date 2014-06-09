package Character;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import java.util.ArrayList;

/**
 * Characters base class
 * 
 * @author khernik
 */
abstract public class Character
{
    
    /**
     * @var Current position of the character (on the screen)
     */
    public int x, y;
    
    /**
     * @var Movement speed
     */
    protected int dx, dy;
    
    /**
     * @var The image of the character
     */
    protected ImageIcon image;
    
    /**
     * @var Is character curently visible on screen?
     */
    protected boolean visible;
    
    /**
     * Return x
     * 
     * @return 
     */
    public int getX()
    {
        return x;
    }
    
    /**
     * Return y
     * 
     * @return 
     */
    public int getY()
    {
        return y;
    }
    
    /**
     * Return character's texture
     * 
     * @return 
     */
    public Image getImage()
    {
        return image.getImage();
    }
    
    /**
     * Return character's width based on the image
     * 
     * @return 
     */
    public int getImageWidth()
    {
        return image.getIconWidth();
    }
    
    /**
     * Return character's height based on the image
     * 
     * @return 
     */
    public int getImageHeight()
    {
        return image.getIconHeight();
    }
    
    /**
     * Is character visible?
     * 
     * @return 
     */    
    public boolean isVisible()
    {
        return visible;
    }
    
    /**
     * Set character's visibility
     * 
     * @param visible 
     */
    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
    
    /**
     * What happenes when the key is being pressed?
     * 
     * @param e 
     */
    abstract public void keyPressed(KeyEvent e);
    
    /**
     * What happenes after the key was released?
     * 
     * @param e 
     */
    abstract public void keyReleased(KeyEvent e);
    
} // End Character
