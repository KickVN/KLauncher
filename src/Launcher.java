import com.google.gson.Gson;
import net.lingala.zip4j.exception.ZipException;
import sun.rmi.runtime.Log;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.ResourceBundle;
/**
 * Created by Admin on 27/6/2015.
 */
public class Launcher extends JFrame{
    public JButton StartBtn;
    private JTextField IdField;
    private JPanel mainPanel;
    private JComboBox VerList;
    private JTabbedPane TabPane;
    public JTextArea LogPane;
    private JPanel LogTab;
    private JScrollPane LogScroll;
    private JPanel VerPanel;
    public JProgressBar progressBar;
    private JPanel BottomPanel;
    public JCheckBox QuickStart;
    private JPanel HomePanel;
    private JScrollPane WebScroll;
    private JButton settingBtn;
    public JComboBox ServerList;
    private JPanel topPanel;
    private JButton exitButton;
    private JLabel topLabel;
    public Minecraft m;
    private JEditorPane jep;
    public ConfigObj cobj;
    public Map<String,String> svlist;
    Setting s;
    String Url = main.Url;
    private Point initialClick;
    public Launcher() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException {
        super("KLauncher v" + main.version + " - VN Minecraft Launcher");
        topLabel.setText("KLauncher v" + main.version + " - VN Minecraft Launcher");
        cobj=loadConfig();
        m = new Minecraft(this);
        s = new Setting(this);
        JEditorPane web = new JEditorPane();
        web.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
        web.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (URISyntaxException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        web.setEditable(false);
        try {
            web.setPage(new URL(Url+"launcher.htm"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        WebScroll.setViewportView(web);
        StartBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                StartBtn.setText("Đang Chơi");
                StartBtn.setEnabled(false);
                TabPane.setSelectedIndex(1);
                Component component = (Component) e.getSource();
                Launcher frame = (Launcher) SwingUtilities.getRoot(component);
                StartThread tr = new StartThread(frame);
                tr.start();


            }
        });
        settingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                s.setVisible(true);
            }
        });
        setSize(800, 450);
        TabPane.setFocusable(false);
        VerList.setFocusable(false);
        progressBar.setStringPainted(true);
        progressBar.setString("");
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 11.25));
        setContentPane(mainPanel);
        setResizable(false);
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/diamond.png"));
        setIconImage(icon);
        IdField.setText(cobj.id);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveConfig();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                System.exit(1);
            }
        });
        mainPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        mainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                // get location of Window
                int thisX = getLocation().x;
                int thisY = getLocation().y;

                // Determine how much the mouse moved since the initial click
                int xMoved = (thisX + e.getX()) - (thisX + initialClick.x);
                int yMoved = (thisY + e.getY()) - (thisY + initialClick.y);

                // Move window to this position
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                setLocation(X, Y);
            }
        });
        addWindowListener(new WindowListener() {
            //I skipped unused callbacks for readability

            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    saveConfig();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                System.exit(1);
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        loadServerList();
        loadVerList();
        setBg(cobj.r, cobj.g, cobj.b,cobj.a);
        setLocationRelativeTo(null);
        setVisible(true);

    }
    public float getPercent(int i){
        return i/255;
    }
    public void setBg(int x,int y,int z){
        getContentPane().setBackground(new Color(x,y,z));
        setBackground(new Color(x, y, z));
    }
    public void setBg(int x,int y,int z, int a){
        if(a==255){
            getContentPane().setBackground(new Color(x,y,z));
            mainPanel.setOpaque(true);
        }
        else{
            mainPanel.setOpaque(false);
            setBackground(new Color(x, y, z, a));
        }
    }
    public void setBg(int a){
        if(a==255){
            getContentPane().setBackground(new Color(cobj.r,cobj.g,cobj.b));
            mainPanel.setOpaque(true);
        }
        else {
            mainPanel.setOpaque(false);
            setBackground(new Color(cobj.r, cobj.g, cobj.b, a));
        }
    }
    public void loadServerList() throws IOException {
        ServerList.removeAllItems();
        NBT n = new NBT();
        svlist = n.check(this);
        if(svlist!=null){
            for(String k:svlist.keySet()){
                ServerList.addItem(k);
            }
        }
        ServerList.setSelectedIndex(n.index);
    }

    public void loadVerList() throws IOException {
        VerList.removeAllItems();
        if(validDir(cobj.mcpath)) {
            String[] list = m.ListVer();
            for (String li : list) {
                VerList.addItem(li);
            }
        }
        VerList.setSelectedItem(cobj.version);
    }

    public void saveConfig() throws FileNotFoundException {
        ConfigObj obj = cobj;
        obj.id = IdField.getText();
        obj.server = ServerList.getSelectedItem().toString();
        obj.version = VerList.getSelectedItem().toString();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        File file = new File(System.getenv("APPDATA")+"\\.minecraft\\"+"Klauncher.json");

        try (FileOutputStream fop = new FileOutputStream(file)) {

            if (!file.exists()) {
                file.createNewFile();
            }

            byte[] contentInBytes = json.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ConfigObj loadConfig() throws IOException {
        String path = System.getenv("APPDATA")+"\\.minecraft\\";
        if(validFile(path+"Klauncher.json")){
            BufferedReader br = new BufferedReader(new FileReader(path+"Klauncher.json"));
            String json = null;
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                json = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                br.close();
            }
            Gson gson = new Gson();
            ConfigObj obj = gson.fromJson(json.toString(), ConfigObj.class);
            if(obj.r == null){
                obj.r = 51;
                obj.g = 153;
                obj.b = 255;
                obj.a = 255;
            }
            return obj;
        }
        else{
            ConfigObj obj = new ConfigObj();
            obj.id = "KickVN";
            obj.version = "1.8.7";
            obj.server = "Thường";
            obj.launcherStatus = 1;
            obj.mcpath = System.getenv("APPDATA")+"\\.minecraft\\";
            obj.isPing = obj.isRelease = true;
            obj.isAlpha = obj.isBeta = obj.isSnapshot = false;
            return obj;
        }
    }
    private boolean validFile(String p){
        File f = new File(p);
        if(f.exists() && f.isFile()){
            return true;
        }
        return false;
    }
    private void createUIComponents() {
        LogPane = new JTextArea();
        LogPane.setEditable(false);
        LogPane.setText("");
        LogPane.setLineWrap(true);
        LogScroll = new JScrollPane(LogPane);
    }

    public class StartThread extends Thread
    {
        private final Launcher l;

        public StartThread(Launcher lc)
        {
            l=lc;
        }

        @Override
        public void run()
        {
            try {
            l.m.Start(IdField.getText(), VerList.getSelectedItem().toString());
            } catch (ZipException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    class ConfigObj{
        String id,version,mcpath,server;
        int launcherStatus;
        Integer r=51,g=153,b=255,a=255;
        boolean isPing,isRelease,isSnapshot,isBeta,isAlpha;
    }
    public boolean validDir(String p){
        File f = new File(p);
        if(f.exists() && f.isDirectory()){
            return true;
        }
        return false;
    }
    class ItemChangeListener implements ItemListener{
        @Override
        public void itemStateChanged(ItemEvent event) {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                cobj.version = VerList.getSelectedItem().toString();
            }
        }
    }
}
