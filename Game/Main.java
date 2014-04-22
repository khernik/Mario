package Game;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Main class - displaying frame
 * 
 * @author khernik
 */
public class Main extends JFrame
{

    /**
     * @var Width of the screen
     */
    public static int width = 1240;
    
    /**
     * @var Height of the screen
     */
    public static int height = 800;
    
    static 
    {
        height -= 28; // no idea why but it works
    }
    
    /**
     * @var Possible screen resolutions
     */
    private static int[][] possibleResolutions = {
        {800,600},
        {1024,768}
    };
    
    /**
     * Create the frame
     */
    public Main()
    {
        add(new Board());
        setSize(width, height);
        setTitle("Mario by khernik");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() 
            {
                new Main();
            }
        });
    }

    
} // End Main
