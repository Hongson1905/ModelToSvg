package bean.function;

import ch.ethz.ssh2.SCPClient;

import java.io.*;

public class RemoteChange {
    /**
     * @description 修改远程文件：将文件读取到本地，在本地修改，再将本地文件读取成数据流导入远程文件
     * ip,用户名，密码，文件路径，文件名
     */
    private MakeConn makeConn;
    private SCPClient scpClient = null;
    private File localFile = null;
    //本地文件输出流
    private FileOutputStream fileOut = null;
    //本地文件输入流
    private FileWriter writer = null;
    private BufferedReader reader = null;
    public RemoteChange(String host,String user,String psw){
        makeConn = new MakeConn(host,user,psw);
    }


    //文件读取导本地
    public boolean getFile(String fileUrl,String fileName,String localFileUrl){
        localFile = new File(localFileUrl+"/"+fileName);
        if(localFile.exists()) {
            if(localFile.delete()){
                System.out.println("本地存在同名文件，清除成功！");
            }else {
                System.out.println("本地存在同名文件，清除失败！");
            }
        }
        try {
            localFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        //本地1文件输出流
        try {
            fileOut = new FileOutputStream(localFile);
        } catch (FileNotFoundException e) {
            System.out.println("error:本地文件输出创建失败");
            e.printStackTrace();
            return false;
        }

        makeConn.login();
        try {
            scpClient = makeConn.getConn().createSCPClient();
            scpClient.get(fileUrl+"/"+fileName, fileOut);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try {
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    //读取本地文件,判断是否包含指定字符串
    public boolean readLocalFile(String target){
        String line;
        try {
            reader = new BufferedReader(new FileReader(localFile));
            while ((line = reader.readLine())!= null){
                if(line.indexOf(target)>=0) {
                    System.out.println(line);
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //在文件尾部增加内容
    public boolean writeFile(String content){
        try {
            writer=new FileWriter(localFile,true);
            writer.write("\n"+content);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    //本地文件替换远程文件
    public boolean replaceFile(String localFileUrl,String fileName,String remoteFileDir){

        try {
            scpClient.put(localFileUrl+"/"+fileName,remoteFileDir);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error：本地exchange文件上传失败");
        }

        return false;
    }
    //删除本地文件
    public boolean deleteFile(){
        if(localFile.delete()){
            System.out.println("Info:本地文件清除完成！");
            return true;
        }else {
            System.out.println("Info:本地文件清除失败！");
            return false;
        }

    }
}
