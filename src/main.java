import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by KickVN on 24/6/2015.
 */
public class main {
    static String version="1.0.5";
    static String Url = "http://kickvn.tk/launcher/";
    static Launcher l;
    public static void main(String[] args) throws ZipException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException {
        Loading load = new Loading();
        JWindow window = load.getWindow();
        File up = new File("KUpdate.jar");
        if(validFile("KUpdate.jar")) up.delete();
        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch(Exception e){
        }

        String currver = null;
        try {
            currver = getVer();
            if(!currver.equals(version)) {
                UpdateDl upd = new UpdateDl(currver,version);
                upd.pack();
                window.setVisible(false);
                upd.setVisible(true);
                if(upd.b) {
                    File f = new File("Update.zip");
                    FileUtils.copyURLToFile(new URL(getDownload())
                            , f);
                    ZipFile zipFile = new ZipFile("Update.zip");
                    zipFile.extractAll(System.getProperty("user.dir"));
                    f.delete();
                    String path = "KUpdate.jar";
                    Process p = Runtime.getRuntime().exec("cmd /c " + path);
                    System.exit(1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        l = new Launcher();
        window.setVisible(false);
    }

    private static boolean validFile(String p){
        File f = new File(p);
        if(f.exists() && f.isFile()){
            return true;
        }
        return false;
    }
    public static String getVer() throws IOException {
        URL url = new URL(Url+"version.txt");

        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
    public static String getDownload() throws IOException {
        URL url = new URL(Url+"update.txt");

        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
