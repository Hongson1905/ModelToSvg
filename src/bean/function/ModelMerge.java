package bean.function;

import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ui.FMain;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Vector;

/**
 * 模型转换业务逻辑
 */
public class ModelMerge {
    public static boolean depand = true;
    public static MakeConn makeConn = new MakeConn();//创建远程连接
    private int backCode = -999;
    private String backResult = "";
    private Thread tmpThread = new Thread();
    private String cmd;
    public Vector<String> rigthGraph;
    public Vector<String> errGraph;
    private Vector<String> tmpVector;
    private Session session;
    private BufferedReader br;
    private BufferedReader brErr;
    private String line;


    public ModelMerge(){
    }
    //压缩文件转图
    public void startMerge(final JTextField textField, final JTextArea textArea){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("=== 压缩文件转图-start ===");
                if(depand&&!tmpThread.isAlive()) startInput(textField,textArea);
                while (tmpThread.isAlive()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(depand&&!tmpThread.isAlive()) StartJustMerge(textField,textArea);
                while (tmpThread.isAlive()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(depand&&!tmpThread.isAlive()) StartConvertMerge(textField,textArea);
                System.out.println("=== 压缩文件转图-end ===");
            }
        }).start();


    }
    //模型导入：解压，入库
    public void startInput(final JTextField textField, final JTextArea textArea){
        tmpThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("=== 模型导入-start ===");
                depand = true;
                textField.setText("压缩文件解压入库");
                //清空放置解压文件的文件夹
                textArea.append("清空data/cimdata/cime...");
                textArea.append("\n");
                if(cleanDir(PropertyGet.prop.getProperty("zipNow"))){
                    System.out.println(PropertyGet.prop.getProperty("zipNow")+":清空完成");
                    textArea.append("data/cimdata/cime清空完成.");
                    textArea.append("\n");
                }else {
                    System.out.println(PropertyGet.prop.getProperty("zipNow")+":清空失败");
                    textArea.append("data/cimdata/cime清空失败.");
                    textArea.append("\n");
                    ModelMerge.depand = false;
                }
                //清空放置入库文件的文件夹
                textArea.append("清空data/cimdata/oldcime...");
                textArea.append("\n");
                if(cleanDir(PropertyGet.prop.getProperty("zipEnd"))){
                    System.out.println(PropertyGet.prop.getProperty("zipEnd")+":清空完成");
                    textArea.append("data/cimdata/oldcime清空完成.");
                    textArea.append("\n");
                    textArea.append("解压模型文件...");
                    textArea.append("\n");
                }else {
                    System.out.println(PropertyGet.prop.getProperty("zipEnd")+":清空失败");
                    textArea.append("data/cimdata/oldcime清空失败.");
                    textArea.append("\n");
                    ModelMerge.depand = false;
                }
//                启动模型解压和导入程序
                try {
                    session = makeConn.getSession();
                    session.execCommand("./"+PropertyGet.prop.getProperty("svgModelZipUrl")+ "/"+PropertyGet.prop.getProperty("svgModelZip"));
                    br = new BufferedReader(new InputStreamReader(session.getStdout()));
                    brErr = new BufferedReader(new InputStreamReader(session.getStderr()));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                showBackInfo(br);
                                showBackInfo(brErr);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();

                } catch (IOException e) {
                    System.out.println("=== model_mul_handle启动错误！===");
                    e.printStackTrace();
                    ModelMerge.depand = false;
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                判断哪些模型正在入库
                System.out.println(getDirFile(PropertyGet.prop.getProperty("zipNow")).size());
                boolean tmpDepand = true;
                Vector<String> tmpInDB = new Vector<String>();
                int emptyNum = 0;
                while (tmpDepand||getDirFile(PropertyGet.prop.getProperty("zipNow")).size()!=0||(emptyNum<3)){
                    if(!tmpDepand&&getDirFile(PropertyGet.prop.getProperty("zipNow")).size()==0) emptyNum++;
                    if(getDirFile(PropertyGet.prop.getProperty("zipNow")).size()!=0) tmpDepand = false;
                    tmpVector = getDirFile(PropertyGet.prop.getProperty("zipNow"));

                    for(int i = 0;i<tmpVector.size();i++){
                        if(tmpVector.get(i).indexOf("log.log")>0){
                            if(!includeStr(tmpInDB,tmpVector.get(i))){
                                tmpInDB.add(tmpVector.get(i));
                                textArea.append(tmpVector.get(i)+":导入中...");
                                textArea.append("\n");
                                textArea.repaint();
                            }
                        }
                        System.out.println(tmpVector.get(i));
                    }
                    for(int i = 0;i<tmpInDB.size();i++){
                        if(!includeStr(tmpVector,tmpInDB.get(i))){
                            textArea.append(tmpInDB.get(i)+":导入完成.");
                            tmpInDB.remove(i);
                            textArea.append("\n");
                            textArea.repaint();
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!depand) {
                        return;
                    }
                }
                for(int i = 0;i<tmpInDB.size();i++){
                    textArea.append(tmpInDB.get(i)+":导入完成.");
                    textArea.append("\n");
                    textArea.repaint();
                }
                //强制关闭解压程序
                makeConn.execute("killall "+PropertyGet.prop.getProperty("svgModelZip"));
                //session.execCommand("killall "+PropertyGet.prop.getProperty("svgModelZip"));
                System.out.println("=Info:"+"killall "+PropertyGet.prop.getProperty("svgModelZip"));
                //读取入库结果：带Error文件保存本地tmp文件夹
                Vector<String> errContent = getErrorFile(PropertyGet.prop.getProperty("zipEnd"));
                for (String a:errContent){
                    textArea.append(a);
                    textArea.append("\n");
                    textArea.repaint();
                }

                System.out.println("=== 模型导入-end ===");
            }
        });
        tmpThread.start();
    }

    private void showBackInfo(BufferedReader br) throws IOException {
        while((line= br.readLine())!= null){

        }
    }

    //模型拼接
    public void StartJustMerge(final JTextField subTitle ,final JTextArea processArea){
        tmpThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("=== 模型拼接-start ===");
                subTitle.setText("模型拼接");
                cmd = "./"+PropertyGet.prop.getProperty("modelMerge")+PropertyGet.prop.getProperty("model_merge_proc");
                System.out.println(cmd);
                processArea.append("=== 模型拼接 ===");
                processArea.append("\n");
                processArea.append(cmd);
                processArea.append("\n");
                processArea.append("模型拼接中...");
                processArea.append("\n");
                session = makeConn.getSession();
                backResult = makeConn.executeSuccess(cmd);
                System.out.println("模型拼接返回值："+makeConn.exitCode);
                if(makeConn.exitCode == 0){
                    processArea.append("=== 模型拼接完成 ===");
                    processArea.append("\n");
                    processArea.repaint();
                }
                System.out.println("=== 模型拼接-end ===");
            }
        });
        tmpThread.start();
    }

    //图模转换
    public void StartConvertMerge(final JTextField subTitle,final JTextArea processArea){

        tmpThread = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("=== 图模转换-start ===");
                subTitle.setText("图模转换");
                processArea.append("=== 图模转换 ===");
                processArea.append("\n");
                changTxt(processArea);
                if(getDirFile(PropertyGet.prop.getProperty("beforeUrl")).size()<=0){
                    JOptionPane.showMessageDialog(FMain.mainFrame,
                            "图形文件不存在!\n请检查："+PropertyGet.prop.getProperty("beforeUrl")+"下的文件！", "系统信息", JOptionPane.ERROR_MESSAGE);
                    System.out.println("--- 未检测到图形文件 ---");
                    System.out.println(PropertyGet.prop.getProperty("beforeUrl")+":下没有图形文件！");
                    return;
                }
                processArea.append("清空data/cimdata/graph/svg/3540200...");
                processArea.append("\n");
                if(cleanDir(PropertyGet.prop.getProperty("resultUrl"))){
                    System.out.println(PropertyGet.prop.getProperty("resultUrl")+":清空完成");
                    processArea.append("data/cimdata/graph/svg/3540200清空完成.");
                    processArea.append("\n");
                }else {
                    System.out.println(PropertyGet.prop.getProperty("resultUrl")+":清空失败");
                    processArea.append("data/cimdata/graph/svg/3540200清空失败.");
                    processArea.append("\n");
                    ModelMerge.depand = false;
                }
                cmd = "./"+PropertyGet.prop.getProperty("modelConvert")+PropertyGet.prop.getProperty("model_convert_proc");
                processArea.append(cmd+"\n");
                System.out.println(cmd);
                backResult = makeConn.execute(cmd);
                System.out.println(makeConn.exitCode);
                System.out.println(getDirFile(PropertyGet.prop.getProperty("resultUrl")).size());
                //查看转换结果
                try {
                    processArea.append("读取转图结果中..."+"\n");
                    Thread.sleep(7000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tmpVector = new Vector<String>();
                tmpVector = getDirFile(PropertyGet.prop.getProperty("resultUrl"));
                rigthGraph = new Vector<String>();
                errGraph = new Vector<String>();
                for(String a:tmpVector){
                    if(a.indexOf("sys.pic.g.svg")>=0){
                        rigthGraph.add(a);
                    }else if(a.indexOf("B.svg")>=0){
                        errGraph.add(a);
                    }
                }
                processArea.append("转图成功："+rigthGraph.size()+"\n");
                for (String a:rigthGraph){
                    processArea.append(a+"\n");
                    System.out.println(a);
                }
                processArea.append("转图失败："+errGraph.size()+"\n");
                for (String a:errGraph){
                    processArea.append(a+"\n");
                    System.out.println(a);
                }
                //清空转前图
                if(cleanDir(PropertyGet.prop.getProperty("beforeUrl"))){
                    System.out.println(PropertyGet.prop.getProperty("beforeUrl")+":清空完成");
                    processArea.append(PropertyGet.prop.getProperty("beforeUrl")+"清空完成.");
                    processArea.append("\n");
                }else {
                    System.out.println(PropertyGet.prop.getProperty("beforeUrl")+":清空失败");
                    processArea.append(PropertyGet.prop.getProperty("beforeUrl")+"清空失败.");
                    processArea.append("\n");
                    ModelMerge.depand = false;
                }
                processArea.append("=== 图模转换完成 ==="+"\n");
                System.out.println("图模转换返回值："+makeConn.exitCode);
                System.out.println("=== 图模转换-end ===");
                //System.out.println("清空："+PropertyGet.prop.getProperty("resultUrl")+":"+cleanDir(PropertyGet.prop.getProperty("resultUrl")));
            }
        });
        tmpThread.start();

    }
    //Exchange文件读写：判断转之前的图名是否在该文件中，不在，按照指定的格式添加
    public void changTxt(JTextArea processArea){
        boolean hasChanged = false;
        RemoteChange remoteChange = new RemoteChange(PropertyGet.prop.getProperty("hostName"),PropertyGet.prop.getProperty("userName"),PropertyGet.prop.getProperty("password"));
        remoteChange.getFile(PropertyGet.prop.getProperty("changeUrl"),PropertyGet.prop.getProperty("changeName"),System.getProperty("user.dir")+"/src/data/tmp");
        //转前图名
        tmpVector = getDirFile(PropertyGet.prop.getProperty("beforeUrl"));
        //写入没有的名字
        for(String a:tmpVector){
            a= a.substring(a.indexOf("[")+1,a.indexOf("-"));
            //找不到指定名称：
            if(!remoteChange.readLocalFile(a)){
                remoteChange.writeFile(a+"\t["+a+"-联络图]\t"+a+"_供电路径图");
                System.out.println("change文件新增："+a);
                processArea.append("Exchange.txt文件新增图："+a+"\n");
                hasChanged = true;
            }
        }
        //上传exchange文件,替换原有
        if(hasChanged){
            remoteChange.replaceFile(System.getProperty("user.dir")+"/src/data/tmp","Exchange.txt",PropertyGet.prop.getProperty("changeUrl"));
        }

        //删除本地exchange文件
    }
    //清空指定路径下的文件夹
    public boolean cleanDir(String dir){
        cmd = "rm "+dir+"/*";
        System.out.println(cmd);
        makeConn.execute(cmd);

        if(getDirFile(dir).size()==0){
            System.out.println(dir+":下文件清空成功！");
            return true;
        }else {
            return false;
        }
    }
    //查看指定路径下文件夹文件,返回文件名
    public Vector<String> getDirFile(String dir){
        MakeConn makeConn = new MakeConn();
        Vector<String> tmpVector = new Vector<String>();
        cmd = "ls "+dir;
        System.out.println(cmd);
        String tmpResult01 =  makeConn.execute(cmd);

        String[] tmpResult02 = tmpResult01.split("\n");
        for (String a:tmpResult02){
           if(!"".equals(a)){
               tmpVector.add(a);
           }

        }
        return tmpVector;
    }

    //为label添加图标
    ImageIcon tmpIcon;
    public void setLabelIcon(JLabel label,String url){
        setLabelIcon(label,url,120,70);
    }
    public void setLabelIcon(JLabel label,String url,int w,int h){
        tmpIcon = new ImageIcon(System.getProperty("user.dir")+ File.separator+url);
        tmpIcon.setImage(tmpIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        label.setIcon(tmpIcon);
    }

    //判断Vecter中是否包含指定String
    public boolean includeStr(Vector<String> vector , String str){
        for (String a:vector){
            if(str.equals(a)) return true;
        }
        return false;
    }


    //到指定路径下查找eq_imp.log文件，将带有Error的文件保存到本地
    public Vector<String> getErrorFile(String url){
        Vector<String> fileNameAll = getDirFile(url);
        Vector<String> errContent = new Vector<String>();

        for (String a:fileNameAll){
            if(a.endsWith("eq_imp.log")) {
                File tmpFile = new File(System.getProperty("user.dir")+"/src/data/tmp/"+a);
                try {
                    tmpFile.createNewFile();
                    FileOutputStream fileOut = new FileOutputStream(tmpFile);
                    makeConn.login();
                    SCPClient scpClient = makeConn.getConn().createSCPClient();
                    scpClient.get("data/cimdata/oldcime/111_3540200_城东中学-联络图20180615154409_eq_imp.log", fileOut);
                    fileOut.close();
                } catch (IOException e) {
                    System.out.println("=Error:"+a+"文件创建失败！");
                    e.printStackTrace();
                }

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(tmpFile));
                    String line;
                    boolean isDelete = true;
                    errContent.add(a+":");
                    while ((line = reader.readLine())!= null){
                        if(line.indexOf("Error")>=0) {
                            System.out.println(line);
                            errContent.add(line);
                            isDelete = false;
                        }
                    }
                    if(isDelete) {
                        System.out.println("======= 删除"+tmpFile.toString()+tmpFile.delete()+"=======");
                        errContent.remove(errContent.size()-1);
                    }
                    reader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return errContent;
    }

    public static void main(String[] args) {

        ModelMerge modelMerge = new ModelMerge();
//        modelMerge.StartJustMerge();
        System.out.println(modelMerge.getDirFile("data/graph/FJ/display/fac500+220"));
//        Vector<String > b = modelMerge.getDirFile(PropertyGet.prop.getProperty("modelMerge"));
//        for (String a:b){
//            System.out.println(a);
//        }

//        MakeConn makeConn = new MakeConn();
//
//        System.out.println(makeConn.executeSuccess("./src/bin/modelmerge/connect_border.sh"));
//        System.out.println(makeConn.execute("cd src \n"));
//        System.out.println(makeConn.execute("pwd \n"));
        //System.out.println(makeConn.executeSuccess("ls -lrt"));
        //System.out.println(makeConn.execute("./connect_border.sh \n"));
        //System.out.println(makeConn.session.getExitStatus());
    }
}
