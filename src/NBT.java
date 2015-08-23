import com.evilco.mc.nbt.stream.NbtInputStream;
import com.evilco.mc.nbt.stream.NbtOutputStream;
import com.evilco.mc.nbt.tag.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

public class NBT {
    public int index = 0;
    /**
     * Tests read and write.
     * @throws IOException
     */
    public Map<String,String> check (Launcher l) throws IOException {
        InputStream inputStream = new FileInputStream(l.cobj.mcpath+"servers.dat");
        NbtInputStream nbtInputStream = new NbtInputStream (inputStream);
        TagCompound tag = (TagCompound)nbtInputStream.readTag ();
        List<TagCompound> server = tag.getList("servers",TagCompound.class);
        inputStream.close();
        TagCompound svt = new TagCompound("");
        TagList svlist = new TagList("servers");
        Map<String,String> map = new LinkedHashMap<>();
        map.put("Thường","");
        if(!validFile(l.cobj.mcpath+"servers.dat")) return map;
        int j=0;
        for(TagCompound t:server){
            String name = t.getString("name");
            String ip = t.getString("ip");
            String tt=name;
            int i = 0;
            while (map.containsKey(tt)) {
                tt = name;
                i++;
                tt = name+" "+i;
            }
//            map.put(tt,t.getString("ip"));
            if(l.cobj.server.equals(tt)) index = j;
            j++;
            if(l.cobj.isPing) {
                String ipr = ip;
                String[] spl = ip.split("\\:");
                if (spl.length == 2) ipr = spl[0];
                Ping p = new Ping();
                p.host = new InetSocketAddress(ipr, 25565);
                Ping.StatusResponse st = p.fetchData();
                if (st == null) map.put("Offline - " + tt, ip);
                else map.put(st.players.online + "/" + st.players.max + " - " + tt, ip);
            }
            else map.put(tt, ip);
        }
        return map;
    }
    private boolean validFile(String p){
        File f = new File(p);
        if(f.exists() && f.isFile()){
            return true;
        }
        return false;
    }
}