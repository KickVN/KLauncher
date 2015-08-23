import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class NotiFolder extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton editButton;
    private JButton exitButton;
    private JLabel lab1;
    private JLabel lab2;
    public boolean b=false;
    String download="";
    public NotiFolder(final Launcher l) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI(download));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }
                b=true;
                System.exit(1);
            }
        });
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int res = fc.showOpenDialog(l);
                if(res == JFileChooser.APPROVE_OPTION) {
                    String path = fc.getSelectedFile().getAbsolutePath();
                    if (path != null) {
                        path += "\\";
                        l.cobj.mcpath = path;
                        try {
                            l.saveConfig();
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                        lab1.setVisible(false);
                        buttonOK.setVisible(false);
                        editButton.setVisible(false);
                        lab2.setText("Hãy mở lại Launcher");
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                b=true;
                System.exit(1);
            }
        });
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                b=true;
                System.exit(1);
            }
        });
    }
}
