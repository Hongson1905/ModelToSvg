package bean.function;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import ui.FMain;

import javax.swing.*;
import java.io.*;

/**
 * 远程执行linux的shell script
 * @author Ickes
 * @since  V0.1
 */
public class MakeConn {
    private static String  DEFAULTCHART="UTF-8";
    private Connection conn;
    public Session session;
    private String ip;
    private String userName;
    private String userPwd;
    public int exitCode;//命令执行退出标志结果，多个session共享
    private PropertyGet propertyGet = FMain.propertyGet;

    public MakeConn(String ip, String userName, String userPwd) {
        this.ip = ip;
        this.userName = userName;
        this.userPwd = userPwd;
    }


    public MakeConn() {
        this.ip  = propertyGet.prop.getProperty("hostName");
        this.userName = propertyGet.prop.getProperty("userName");
        this.userPwd = propertyGet.prop.getProperty("password");
    }

    /**
     * 远程登录linux的主机
     * @author Ickes
     * @since  V0.1
     * @return
     *      登录成功返回true，否则返回false
     */
    public Boolean login(){
        boolean flg=false;
        try {
            conn = new Connection(ip);
            conn.connect();//连接
            flg=conn.authenticateWithPassword(userName, userPwd);//认证
        } catch (IOException e) {
            JOptionPane.showMessageDialog(FMain.mainFrame,"远程连接错误!\n请检查网络！", "系统信息", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return flg;
    }
    public Session getSession(){
        try {
            if(login()){
                session= conn.openSession();//打开一个会话
                return session;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return session;
    }
    /**
     * @author Ickes
     * 远程执行shll脚本或者命令
     * @param cmd
     *      即将执行的命令
     * @return
     *      命令执行完后返回的结果值，成功返回结果值，失败返回错误流中内容
     * @since V0.1
     */
    public String execute(String cmd){
        String result="";
        try {
            if(login()){
                session= conn.openSession();//打开一个会话
                session.execCommand(cmd);//执行命令
                result=processStdout(session.getStdout(),DEFAULTCHART);
                //如果为得到标准输出为空，说明脚本执行出错了,读取错误流
                if("".equals(result)){
                    result=processStdout(session.getStderr(),DEFAULTCHART);
                }
                session.close();
                conn.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @author Ickes
     * 远程执行shll脚本或者命令
     * @param cmd
     *      即将执行的命令
     * @return
     *      命令执行成功后返回的结果值，如果命令执行失败，返回空字符串
     * @since V0.1
     */
    public String executeSuccess(String cmd){
        String result="";
        try {
            if(login()){
                session= conn.openSession();//打开一个会话
                session.execCommand(cmd);//执行命令
                result=processStdout(session.getStdout(),DEFAULTCHART);
                conn.close();
                session.close();
                exitCode = session.getExitStatus();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析脚本执行返回的结果集
     * @author Ickes
     * @param in 输入流对象
     * @param charset 编码
     * @since V0.1
     * @return
     *       以纯文本的格式返回
     */
    private String processStdout(InputStream in, String charset){
        InputStream    stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout,charset));
            String line=null;
            while((line=br.readLine()) != null){

                if(line.indexOf("/etc/iesprofile: line 4: ulimit: POSIX message queues: cannot modify limit: ")<0){
                    buffer.append(line+"\n");
                    System.out.println(line);
                    if(line.indexOf("have finished Graph Change")>=0) return buffer.toString();
                }

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static void setCharset(String charset) {
        DEFAULTCHART = charset;
    }
    public Connection getConn() {
        return conn;
    }
    public void setConn(Connection conn) {
        this.conn = conn;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserPwd() {
        return userPwd;
    }
    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }


    public static void main(String[] args) {
        MakeConn makeConn = new MakeConn("127.0.0.1","","");
        makeConn.execute("start explorer http://www.baidu.com");
    }
}