package Game;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    public static int width = 800;
    
    /**
     * @var Height of the screen
     */
    public static int height = 600;
    
    static 
    {
        height -= 28;
    }
    
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
     * @param args command line arguments
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
