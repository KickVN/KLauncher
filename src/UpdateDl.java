import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UpdateDl extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel lab1;
    private JLabel lab2;
    private JLabel lab3;
    public boolean b=false;

    public UpdateDl(String cur, String pc) {
        setContentPane(contentPane);
        setModal(true);
        setSize(400, 200);
        getRootPane().setDefaultButton(buttonOK);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/diamond.png")));
        setLocationRelativeTo(null);
        setTitle("KLauncher - Cập nhật");
        lab1.setText("Đã có phiên bản mới\n");
        lab2.setText("Phiên bản đang dùng: " + pc + "\n");
        lab3.setText("Phiên bản mới nhất: "+cur);
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
// add your code here
        b=true;
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
