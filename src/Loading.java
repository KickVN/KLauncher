import javax.swing.*;
import java.awt.*;

/**
 * Created by Admin on 31/7/2015.
 */
public class Loading {
    private JWindow window = new JWindow();
    public Loading(){
        window.setBackground(new Color(0, 162, 255));
        JLabel lb = new JLabel("", new ImageIcon(getClass().getResource("images/banner.jpg")), SwingConstants.CENTER);
        lb.setBackground(new Color(0, 162, 255));
        window.getContentPane().add(lb);
        window.setBounds(500, 150, 600, 240);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
    public JWindow getWindow(){
        return window;
    }
}
