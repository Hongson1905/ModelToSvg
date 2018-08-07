package bean.using;

import bean.function.MakeConn;

class Test {


    public void fd(){
        MakeConn makeConn = new MakeConn("10.35.10.105","ies","ies1234");
        makeConn.execute("killall model_mul_handle");

    }

    public static void main(String[] args) {
        Test test = new Test();
        test.fd();
    }

}