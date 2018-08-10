package bean.using;

import bean.function.MakeConn;
import ch.ethz.ssh2.SCPClient;

import java.io.File;
import java.io.IOException;

class Test {


    public void fd(){
        File tmpFile = new File(System.getProperty("user.dir")+"/src/data/tmp/Exchange.txt");
        MakeConn makeConn = new MakeConn("10.35.10.105","ies","ies1234");
        makeConn.login();
        try {
            SCPClient scpClient = makeConn.getConn().createSCPClient();
            scpClient.put(System.getProperty("user.dir")+"/src/data/tmp/Exchange.txt","data/cimdata");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Test test = new Test();
        test.fd();

    }

}