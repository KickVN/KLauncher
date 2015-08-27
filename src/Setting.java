import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Admin on 29/6/2015.
 */
public class Setting extends  JFrame{
    private JComboBox StatusBox;
    private JPanel mainPanel;
    private JButton saveButton;
    private JTextField fileField;
    private JButton fileChoose;
    private JCheckBox boxPing;
    private JCheckBox releaseCheckBox;
    private JCheckBox snapshotCheckBox;
    private JCheckBox betaCheckBox;
    private JCheckBox alphaCheckBox;
    private JPanel colorPanel;
    private JButton bgButton;
    private JSlider opacityBar;
    final Launcher l;
    public final JFileChooser fc;
    ColorPicker color;
    public Setting(Launcher launcher) {
        super("KLauncher - Cài đặt");
        this.l=launcher;
        color  = new ColorPicker(this.l,this);
        fc = new JFileChooser(l.cobj.mcpath);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(false);
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/diamond.png"));
        setIconImage(icon);
        StatusBox.addItem("Ẩn Launcher khi mở Minecraft");
        StatusBox.addItem("Thoát Launcher khi mở Minecraft");
        StatusBox.addItem("Luôn hiện Launcher mọi nơi");
//        StatusBox.setSelectedIndex(l.cobj.launcherStatus);
//        StatusBox.setVisible(false);
        releaseCheckBox.setSelected(l.cobj.isRelease);
        snapshotCheckBox.setSelected(l.cobj.isSnapshot);
        betaCheckBox.setSelected(l.cobj.isBeta);
        alphaCheckBox.setSelected(l.cobj.isAlpha);
//        setResizable(false);
        if(l.cobj.isPing) boxPing.setSelected(true);
        else boxPing.setSelected(false);
        fileField.setText(l.cobj.mcpath);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                try {
                    l.cobj.launcherStatus = StatusBox.getSelectedIndex();
                    l.cobj.a = opacityBar.getValue();
                    l.saveConfig();
                    l.loadVerList();
                    l.loadServerList();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        fileChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int res = fc.showOpenDialog(l);
                if(res == JFileChooser.APPROVE_OPTION) {
                    String path = fc.getSelectedFile().getAbsolutePath();
                    if (path != null) {
                        path += "\\";
                        l.s.fileField.setText(path);
                        l.cobj.mcpath = path;
                        try {
                            l.saveConfig();
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        boxPing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                l.cobj.isPing = boxPing.isSelected();
                try {
                    l.saveConfig();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        releaseCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                l.cobj.isRelease = releaseCheckBox.isSelected();
                try {
                    l.saveConfig();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        snapshotCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                l.cobj.isSnapshot = snapshotCheckBox.isSelected();
                try {
                    l.saveConfig();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        betaCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                l.cobj.isBeta = betaCheckBox.isSelected();
                try {
                    l.saveConfig();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        alphaCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                l.cobj.isAlpha = alphaCheckBox.isSelected();
                try {
                    l.saveConfig();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        bgButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color.setVisible(true);
            }
        });
//        colorPanel.add(new JButton());
        opacityBar.setValue(l.cobj.a);
        Hashtable labelTable = new Hashtable();
        labelTable.put(new Integer(0), new JLabel("Ẩn"));
        labelTable.put(new Integer(255), new JLabel("Hiện"));
        opacityBar.setLabelTable(labelTable);
        opacityBar.setPaintLabels(true);
        opacityBar.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                l.setBg(opacityBar.getValue());
            }
        });
        mainPanel.setBackground(new Color(l.cobj.r, l.cobj.g, l.cobj.b));
        pack();
    }
}
