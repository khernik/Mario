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
     * Create the frame
     */
    public Main()
    {
        add(new Board());
        setTitle("Mario by khernik");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
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
