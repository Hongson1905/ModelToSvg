package bean.function;

import java.io.*;
import java.util.Properties;
import java.util.Set;

public class PropertyGet {

    public static String hostName;
    public static String userName;
    public static String password;
    public static int server_port;
    public static Properties prop;

    private InputStream inStream;
    public PropertyGet(String url) {
        System.out.println("=======服务配置=======");
        String relativelyPath=System.getProperty("user.dir"); //获取项目的绝对路径
        //System.out.println(relativelyPath);
        //InputStream inStream = getClass().getResourceAsStream("data/ServerProperty.properties ");

        try {
            inStream = new FileInputStream(new File(relativelyPath+File.separator+url));
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        prop = new Properties();
        try {
            prop.load(new InputStreamReader(inStream, "GBK"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Set<Object> keys = prop.keySet();//返回属性key的集合
        //历遍配置文件所有数据
        for(Object key:keys){
            System.out.println("key:"+key.toString()+",value:"+prop.get(key));
        }

        hostName = prop.getProperty("hostName");
        userName = prop.getProperty("userName");
        password = prop.getProperty("password");
    }
    public static void main(String[] args) {
        PropertyGet propertyGet = new PropertyGet("src/data/ServerProperty.properties");
        System.out.println(propertyGet.prop.getProperty("model_merge_proc"));
        System.exit(0);
    }
}
