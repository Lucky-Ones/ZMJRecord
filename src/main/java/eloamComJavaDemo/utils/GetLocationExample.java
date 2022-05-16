package eloamComJavaDemo.utils;


import me.hupeng.ipLocationService.IpLocationResult;
import me.hupeng.ipLocationService.IpLocationService;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;

import javax.lang.model.util.Elements;
import javax.swing.text.Document;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//IP��λ
public class GetLocationExample {
    public static String getBlock() throws IOException, DbMakerConfigException {
        DbConfig config = new DbConfig();
        //��ȡip���λ�ã�����src�£���ֱ��ͨ���������ȡ�ļ�Ip2RegionTestΪ�����ࣩ
//        String dbfile = GetLocationExample.class.getResource("/ip2region.db").getPath();
//        DbSearcher searcher = new DbSearcher(config, dbfile);
//        DbSearcher searcher = new DbSearcher(config, System.getProperty("exe.path")+"/ip2region.db");
        DbSearcher searcher = new DbSearcher(config, "D:\\project\\javaProject\\ZMJ\\ZMJRecord.exe\\ip2region.db");
        //����Btree����
        DataBlock block = searcher.btreeSearch(getOutIPV4());
        //��ӡλ����Ϣ����ʽ������|����|ʡ��|����|��Ӫ�̣�
        String[] blockinfo = block.getRegion().split("\\|");
        String blockPlace = blockinfo[0]+blockinfo[2]+blockinfo[3];
        System.out.println(blockPlace);
        return blockPlace;

    }
    public static void main(String[] args) throws IOException, DbMakerConfigException {
        getBlock();
    }


    public static String getOutIPV4() {
        String ip = "";
        String chinaz = "http://ip.chinaz.com";

        StringBuilder inputLine = new StringBuilder();
        String read = "";
        URL url = null;
        HttpURLConnection urlConnection = null;
        BufferedReader in = null;
        try {
            url = new URL(chinaz);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            while ((read = in.readLine()) != null) {
                inputLine.append(read + "\r\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
        Matcher m = p.matcher(inputLine.toString());
        if (m.find()) {
            String ipstr = m.group(1);
            ip = ipstr;
        }
        return ip;
    }





}
