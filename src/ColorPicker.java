import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class ColorPicker extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JPanel colorPanel;
    private final Launcher launcher;
    private final Setting setting;
    Color color;
    public ColorPicker(Launcher launcher, Setting setting) {
        this.launcher = launcher;
        this.setting = setting;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });


// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    private void onOK() {
// add your code here
        launcher.cobj.r = color.getRed();
        launcher.cobj.g = color.getGreen();
        launcher.cobj.b = color.getBlue();
        launcher.setBg(color.getRed(),color.getGreen(),color.getBlue());
        setting.getContentPane().setBackground(color);
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    private void createUIComponents() {
        final JColorChooser cl = new JColorChooser();
        cl.setPreviewPanel(new JPanel());
        AbstractColorChooserPanel panels[] = cl.getChooserPanels();
        for(AbstractColorChooserPanel panel:panels){
            if(!panel.getDisplayName().equals("HSV")) cl.removeChooserPanel(panel);
        }
        cl.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                color = cl.getColor();
            }
        });
        colorPanel = new JPanel();
        colorPanel.add(cl);
    }

}
