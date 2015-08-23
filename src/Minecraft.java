import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import sun.misc.IOUtils;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.core.ZipFile;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;

public class Minecraft {
    private JsonObj js;
    private String dlib,libs;
    private String ver;
    private final Launcher l;
    private long time=System.currentTimeMillis();
    public Minecraft(Launcher lc) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        this.l= lc;
        try {
            FileUtils.copyURLToFile(new URL("http://s3.amazonaws.com/Minecraft.Download/versions/versions.json")
                    ,new File(l.cobj.mcpath+"versions.json"),2000,2000);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        }
    }
    public void loadLib(String target) throws ZipException, IOException {
        libs="";
        for(Lib lib: js.libraries){
            String[] spl = lib.name.split(":");
                String dlibp = l.cobj.mcpath+"libraries\\"+spl[0].replace('.','\\')+"\\"+spl[1]+"\\"+spl[2];
//                System.out.println(dlibp);
                if(validDir(dlibp)) {
                    AddMsg("Đã có thư viện " + dlibp + "\\" + spl[1] + "-" + spl[2] + ".jar");
                    if(lib.natives!=null) {
                        String arch = System.getProperty("os.arch");
                        if (arch.equals("x86")) {
                            arch = "32";
                        } else {
                            arch = "64";
                        }
                        if (lib.natives.windows != null) {
                            boolean b = false;
                            if (lib.rules != null) {
                                for (Rule rule : lib.rules) {
                                    if ((rule.os == null) && (rule.action.equals("allow"))) {
                                        b = true;
                                    }
                                    if ((rule.os != null) && (rule.os.name.equals("windows")) && (rule.action.equals("allow"))) {
                                        b = true;
                                    }
                                }
                            }

                            if ((lib.rules == null) || (b)) {
                                String dlibt = dlibp + "\\" + spl[1] + "-" + spl[2]
                                        + "-" + lib.natives.windows.replace("${arch}", arch) + ".jar";
                                ZipFile zipFile = new ZipFile(dlibt);
                                zipFile.extractAll(l.cobj.mcpath + "versions\\" + target + "\\" + target + "-natives-"+time);
                            }

                        }
                    }
                }
                else{
                    boolean skip=false;
                    String p = "";
                    if(lib.natives!=null){
                        String dlibd = spl[0].replace('.','/')+"/"+spl[1]+"/"+spl[2];
                        p = dlibd + "/" + spl[1] + "-" + spl[2];
                        if(lib.natives.windows==null) {
                            skip=true;
                        }
                        else {
                            String arch = System.getProperty("os.arch");
                            if (arch.equals("x86")) {
                                arch = "32";
                            } else {
                                arch = "64";
                            }
                            p += "-" + lib.natives.windows.replace("${arch}", arch) + ".jar";
                        }
                    }
                    else {
                        p = spl[0].replace('.', '/') + "/" + spl[1] + "/" + spl[2] + "/" + spl[1] + "-" + spl[2] + ".jar";
                    }
                    if(!skip) {
                        AddMsg("Đang tải thư viện " + p);
//                        if (!validFile(l.cobj.mcpath + "libraries/" + p)) {
                            try {
                                FileUtils.copyURLToFile(new URL("https://libraries.minecraft.net/" + p)
                                        , new File(l.cobj.mcpath + "libraries\\" + p));
                                FileUtils.copyURLToFile(new URL("https://libraries.minecraft.net/" + p + ".sha1")
                                        , new File(l.cobj.mcpath + "libraries\\" + p + ".sha1"));
                                if(lib.natives!=null) {
                                    String arch = System.getProperty("os.arch");
                                    if (arch.equals("x86")) {
                                        arch = "32";
                                    } else {
                                        arch = "64";
                                    }
                                    if (lib.natives.windows != null) {
                                        boolean b = false;
                                        if (lib.rules != null) {
                                            for (Rule rule : lib.rules) {
                                                if ((rule.os == null) && (rule.action.equals("allow"))) {
                                                    b = true;
                                                }
                                                if ((rule.os != null) && (rule.os.name.equals("windows")) && (rule.action.equals("allow"))) {
                                                    b = true;
                                                }
                                            }
                                        }

                                        if ((lib.rules == null) || (b)) {
                                            String dlibt = l.cobj.mcpath + "libraries\\"+p.replace("/","\\");
                                            ZipFile zipFile = new ZipFile(dlibt);
                                            zipFile.extractAll(l.cobj.mcpath + "versions\\" + target + "\\" + target + "-natives-"+time);
                                        }

                                    }
                                }
                            } catch (IOException e) {
                                AddMsg(e.getMessage());
                                e.printStackTrace();
                            }
                    }
                }
            if(lib.natives==null) {
                String p = l.cobj.mcpath + "libraries\\" + spl[0].replace('.', '\\') + "\\" + spl[1] + "\\" + spl[2] + "\\" + spl[1] + "-" + spl[2] + ".jar";
                libs += p+";";
            }
            }
    }
    public boolean validDir(String p){
        File f = new File(p);
        if(f.exists() && f.isDirectory()){
            return true;
        }
        return false;
    }
    private boolean validFile(String p){
        File f = new File(p);
        if(f.exists() && f.isFile()){
            return true;
        }
        return false;
    }
    public void Start(String id,String vers) throws ZipException, IOException {
        l.progressBar.setValue(0);
        ver = vers;
        if(!validDir(l.cobj.mcpath+"versions\\"+ver)){
            AddMsg("Chưa có dữ liệu phiên bản "+vers+". Đang tải xuống...");
            l.progressBar.setString("Đang tải phiên bản...");
            Download(ver);
        }
        l.progressBar.setValue(0);
        l.StartBtn.setText("Đang chơi");
//        System.exit(1);
        l.progressBar.setString("Đọc dữ liệu phiên bản...");
        js = LoadJson(ver);
        l.progressBar.setValue(10);
        AddMsg("Quét dọn các lần chơi trước...");
        cleanNatives();
        l.progressBar.setValue(15);
        l.progressBar.setString("Đọc các thư viện...");
        loadLib(vers);
        l.progressBar.setValue(40);
        String cmd = "java ";
        l.progressBar.setString("Đọc các tài nguyên...");
        if(js.assets!=null) DownloadAssets(js.assets,true);
        else DownloadAssets("legacy",false);
        l.progressBar.setValue(70);
        l.progressBar.setString("Xây dựng...");
        String mpath = l.cobj.mcpath.replace("\\","/");
        cmd+= "-Djava.library.path=\""+mpath+"versions/"+ver+"/"+ver+"-natives-"+time+"\"";
//        ZipFile zipFile = new ZipFile(dlibp);
//        zipFile.extractAll(dlib);
        cmd+= " -cp \"";
        cmd+= libs.replace("\\","/");
        if(js.inheritsFrom!=null){
            JsonObj js2 = js;
            ver = js.inheritsFrom;
            js = LoadJson(ver);
            loadLib(vers);
            cmd+= libs.replace("\\","/");
            ver = vers;
            js = js2;
        }
        cmd+= mpath+"versions/"+ver+"/"+ver+".jar";
        cmd+= "\" "+js.mainClass+" ";
//        if(l.QuickStart.isSelected()){
//            cmd+="--server mine.com ";
//        }
        String sv = l.svlist.get(l.ServerList.getSelectedItem());
        if(sv.length()>2){
            cmd+="--server "+sv.replace(":"," --port ")+" ";
        }
        String tail = js.minecraftArguments.replace("${auth_player_name}", id).replace("${version_name}", ver)
                .replace("${game_directory}", "\""+mpath+"\"").replace("${game_assets}", "\""+mpath + "assets/virtual/legacy\"")
                .replace("${assets_root}",  "\""+mpath + "assets\"")
                .replace("${auth_uuid}", "0").replace("${auth_access_token}", "0")
                .replace("${user_properties}", "{}").replace("${user_type}","{}");
        if(js.assets!=null){
            tail = tail.replace("${assets_index_name}",js.assets);
        }
        cmd+=tail;
        l.progressBar.setString("Hoàn thành");
        l.progressBar.setValue(100);
        l.LogPane.append("Bắt đầu chơi...\n");
//        System.out.println(cmd);
//        AddMsg(cmd);
        executeCommand(cmd);
    }

    private void cleanNatives()  {
        File f = new File(l.cobj.mcpath+"versions\\"+ver);
        for(File file: f.listFiles()){
            if(file.isDirectory()){
                try {
                    FileUtils.deleteDirectory(file.getAbsoluteFile());
                } catch (IOException e) {
                    AddMsg("Vẫn đang chơi. Bỏ qua dọn!");
                    e.printStackTrace();
                }
            }
        }
    }

    public void Download(String v) throws ZipException, IOException {
        FileUtils.copyURLToFile(new URL("http://s3.amazonaws.com/Minecraft.Download/versions/" + v + "/" + v + ".jar")
                , new File(l.cobj.mcpath + "versions\\" + v + "\\" + v + ".jar"));
        FileUtils.copyURLToFile(new URL("http://s3.amazonaws.com/Minecraft.Download/versions/"+v+"/"+v+".json")
                ,new File(l.cobj.mcpath+"versions\\"+v+"\\"+v+".json"));
    }
    public void DownloadAssets(String v,boolean b) throws IOException {
        if(!validFile(l.cobj.mcpath+"assets\\indexes\\"+v+".json")){
            FileUtils.copyURLToFile(new URL("https://s3.amazonaws.com/Minecraft.Download/indexes/" + v + ".json")
                    , new File(l.cobj.mcpath+"assets\\indexes\\"+v+".json"));
        }
        Gson gson = new Gson();
        if(b) {
            Type mapType = new TypeToken<Map<String,Map<String,Assest>>>() {}.getType();
            Map<String,Map<String,Assest>> map = gson.fromJson(new FileReader(l.cobj.mcpath+"assets\\indexes\\"+v+".json"), mapType);
            Map<String,Assest> as = map.get("objects");
            for (Object key : as.keySet()) {
                String p = l.cobj.mcpath + "assets\\objects\\" + as.get(key).hash.substring(0, 2) + "\\" + as.get(key).hash;
                if (!validFile(p)) {
                    AddMsg("Chưa có tài nguyên " + p);
                    AddMsg("Đang tải xuống tài nguyên " + p);
                    System.out.println("http://resources.download.minecraft.net/" + as.get(key).hash.substring(0, 2) + "/" + as.get(key).hash);
                    try {
                        FileUtils.copyURLToFile(new URL("http://resources.download.minecraft.net/" + as.get(key).hash.substring(0, 2) + "/" + as.get(key).hash)
                                , new File(p));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else AddMsg("Đã có tài nguyên " + p);
            }
        }
        else{
            BufferedReader br = new BufferedReader(new FileReader(l.cobj.mcpath+"assets\\indexes\\"+v+".json"));
            String json="";
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
            Type mapType = new TypeToken<Map<String,Map<String,Assest>>>() {}.getType();
            Map<String,Map<String,Assest>> map = gson.fromJson(json.replace("\"virtual\": true,",""), mapType);
            Map<String,Assest> as = map.get("objects");
            for (Object key : as.keySet()) {
                String p = l.cobj.mcpath + "assets\\virtual\\legacy\\" + key.toString().replace("/","\\");
                if (!validFile(p)) {
                    AddMsg("Chưa có tài nguyên " + p);
                    AddMsg("Đang tải xuống tài nguyên " + p);
                    System.out.println("http://resources.download.minecraft.net/" + as.get(key).hash.substring(0, 2) + "/" + as.get(key).hash);
                    try {
                        FileUtils.copyURLToFile(new URL("http://resources.download.minecraft.net/" + as.get(key).hash.substring(0, 2) + "/" + as.get(key).hash)
                                , new File(p));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else AddMsg("Đã có tài nguyên " + p);
            }
        }
    }
    public void AddMsg(String s){
        l.LogPane.append(s+"\n");
        l.LogPane.setCaretPosition(l.LogPane.getDocument().getLength());
    }
    public String[] ListVer() throws IOException {
        VerObj ve = LoadVerJson();
        List<String> ver = new ArrayList<>();
        if(validDir(l.cobj.mcpath+"versions")) {
            File file = new File(l.cobj.mcpath + "versions");
            String[] directories = file.list(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) {
                    return new File(current, name).isDirectory();
                }
            });
            for (String s : directories) {
                ver.add(s);
            }
        }
        Collections.reverse(ver);
        for(Versions v:ve.versions){
            if (!ver.contains(v.id)) {
                if (l.cobj.isRelease && v.type.equals("release")) ver.add(v.id);
                else if (l.cobj.isSnapshot && v.type.equals("snapshot")) ver.add(v.id);
                else if (l.cobj.isBeta && v.type.equals("old_beta")) ver.add(v.id);
                else if (l.cobj.isAlpha && v.type.equals("old_alpha")) ver.add(v.id);
            }
        }
        String[] verlist =  ver.toArray(new String[ver.size()]);
        return verlist;
    }
    private void executeCommand(String command) throws FileNotFoundException {
        Process p;
        int stt = l.cobj.launcherStatus;
        try {
            p = Runtime.getRuntime().exec(command);
            if(stt<2) l.setVisible(false);
            if(stt==1) {
                l.saveConfig();
                System.exit(1);
            }
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                l.LogPane.append(line+"\n");
                l.LogPane.setCaretPosition(l.LogPane.getDocument().getLength());
                System.out.println(line+"\n");
            }
            l.StartBtn.setText("Chơi");
            l.StartBtn.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(stt==0) l.setVisible(true);
    }
    public JsonObj LoadJson(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(l.cobj.mcpath+"versions\\"+filename+"\\"+filename+".json"));
        String json;
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            json = sb.toString();
        } finally {
            br.close();
        }
        Gson gson = new Gson();
        JsonObj obj = gson.fromJson(json.toString(), JsonObj.class);
        return obj;
    }
    public VerObj LoadVerJson() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(l.cobj.mcpath+"versions.json"));
        String json;
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            json = sb.toString();
        } finally {
            br.close();
        }
        Gson gson = new Gson();
        VerObj obj = gson.fromJson(json.toString(), VerObj.class);
        return obj;
    }
    public class VerObj{
        private Versions[] versions;
    }
    class Versions{
        String id,type;
    }
    class JsonObj {
        private String minecraftArguments;
        private String mainClass;
        private String inheritsFrom,assets;
        private Lib[] libraries;
        JsonObj() {
        }
    }
    class Lib{
        private String name;
        private Natives natives;
        private Rule[] rules;
    }
    class Natives{
        private String windows;
    }
    class Rule{
        String action;
        Os os;
    }
    class Os{
        String name;
    }
    class Assest{
        String hash;
        int size;
    }
}
