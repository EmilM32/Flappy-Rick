package flappyrick;

import java.awt.Graphics;
import javax.swing.JPanel;

public class Render extends JPanel 
{

    @Override
    protected void paintComponent(Graphics grphcs)
    {
        super.paintComponent(grphcs); 
        
        Main.main.repaint(grphcs);
    }
    
}
