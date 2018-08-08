package bean.using;

import java.io.File;

class Test {


    public void fd(){
        File tmpFile = new File(System.getProperty("user.dir")+"/src/data/tmp/000_3540200_澳溪中学-联络图20180615154326_eq_imp.log");
        System.out.println(tmpFile.delete());
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.fd();
    }

}