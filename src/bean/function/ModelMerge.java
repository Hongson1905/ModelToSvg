package bean.function;

import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Vector;

/**
 * 模型转换业务逻辑
 */
public class ModelMerge {
    public static MakeConn makeConn = new MakeConn();//创建远程连接
    private int backCode = -999;
    private String backResult = "";
    private Thread tmpThread;
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
    public void startMerge(JTextField textField,JTextArea textArea){
        System.out.println("=== 压缩文件转图-start ===");
        startInput(textField,textArea);
        StartConvertMerge();
        System.out.println("=== 压缩文件转图-end ===");

    }
    //模型导入：解压，入库
    public void startInput(final JTextField textField, final JTextArea textArea){
        tmpThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("=== 模型导入-start ===");
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
                }
                //清空放置入库文件的文件夹
                textArea.append("清空data/cimdata/oldcime...");
                textArea.append("\n");
                if(cleanDir(PropertyGet.prop.getProperty("zipEnd"))){
                    System.out.println(PropertyGet.prop.getProperty("zipEnd")+":清空完成");
                    textArea.append("data/cimdata/oldcime清空完成.");
                    textArea.append("\n");
                }else {
                    System.out.println(PropertyGet.prop.getProperty("zipEnd")+":清空失败");
                    textArea.append("data/cimdata/oldcime清空失败.");
                    textArea.append("\n");
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
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                判断哪些模型正在入库
                System.out.println(getDirFile(PropertyGet.prop.getProperty("zipNow")).size());
                boolean depand = true;
                Vector<String> tmpInDB = new Vector<String>();
                while (depand||getDirFile(PropertyGet.prop.getProperty("zipNow")).size()!=0){
                    if(getDirFile(PropertyGet.prop.getProperty("zipNow")).size()!=0) depand = false;
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
                }
                for(int i = 0;i<tmpInDB.size();i++){
                    textArea.append(tmpInDB.get(i)+":导入完成.");
                    textArea.append("\n");
                    textArea.repaint();
                }
                makeConn.execute("killall "+PropertyGet.prop.getProperty("svgModelZip"));
                //session.execCommand("killall "+PropertyGet.prop.getProperty("svgModelZip"));
                System.out.println("=Info:"+"killall "+PropertyGet.prop.getProperty("svgModelZip"));
                //读取入库结果：带Error文件保存本地tmp文件夹
                Vector<String> errContent = getErrorFile(PropertyGet.prop.getProperty("zipEnd"));
                for (String a:errContent){
                    textArea.append(a);
                    textArea.append("\n");
                    textArea.repaint();·
                }+

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
    public void StartJustMerge(final JLabel titlabel,final JTextField subTitle,final JLabel processLabel, final JTextArea processArea){
        tmpThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("=== 模型拼接-start ===");
                setLabelIcon(titlabel,"");
                subTitle.setText("");
                setLabelIcon(processLabel,"");
                cmd = "./"+PropertyGet.prop.getProperty("modelMerge")+PropertyGet.prop.getProperty("model_merge_proc");
                System.out.println(cmd);
                session = makeConn.getSession();
                backResult = makeConn.executeSuccess(cmd);

                System.out.println("模型拼接返回值："+makeConn.exitCode);
                System.out.println("=== 模型拼接-end ===");
            }
        });
        tmpThread.start();
    }

    //图模转换
    public void StartConvertMerge(){
        tmpThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(getDirFile(PropertyGet.prop.getProperty("resultUrl")).size()==1){
                    System.out.println(PropertyGet.prop.getProperty("resultUrl")+":下没有模型文件！");
                    return;
                }
                System.out.println("=== 图模转换-start ===");
                cmd = "./"+PropertyGet.prop.getProperty("modelConvert")+PropertyGet.prop.getProperty("model_convert_proc");
                System.out.println(cmd);
                backResult = makeConn.execute(cmd);

                System.out.println(backResult);
                System.out.println(makeConn.exitCode);
                cmd = "ls "+PropertyGet.prop.getProperty("resultUrl");
                System.out.println(cmd);
                backResult = makeConn.execute(cmd);


                String[] tmpResult = backResult.split("\\s+");
                rigthGraph = new Vector<String>();
                errGraph = new Vector<String>();
                for(String a:tmpResult){
                    if(a.indexOf("sys.pic.g.svg")>=0){
                        rigthGraph.add(a);
                    }else if(a.indexOf("B.svg")>=0){
                        errGraph.add(a);
                    }
                }
                for (String a:rigthGraph){
                    System.out.println(a);
                }
                for (String a:errGraph){
                    System.out.println(a);
                }
                System.out.println("图模转换返回值："+makeConn.exitCode);
                System.out.println("=== 图模转换-end ===");
                //System.out.println("清空："+PropertyGet.prop.getProperty("resultUrl")+":"+cleanDir(PropertyGet.prop.getProperty("resultUrl")));
            }
        });
        tmpThread.start();

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
                    if(isDelete) tmpFile.delete();
                    if(isDelete) errContent.remove(errContent.size()-1);
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
