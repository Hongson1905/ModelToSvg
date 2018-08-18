package bean.function;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class CopyFunction {
    /**
     * 将文件复制到指定文件夹
     */
    public void fileCopy_channel(String srcFileUrl,String targetFileUrl) {
        FileChannel input = null;
        FileChannel output = null;
        File srcFile = new File(srcFileUrl);
        File targetFile = new File(targetFileUrl);
        try {
            if (!srcFile.exists()) System.out.println("Error:复制源文件不存在。"+srcFileUrl);
            if (!targetFile.getParentFile().exists()) {
                boolean result = targetFile.getParentFile().mkdirs();
                if (!result) {
                    System.out.println("Error:复制目标文件不存在。"+targetFileUrl);
                }
            }
            input = new FileInputStream(srcFile).getChannel();
            output = new FileOutputStream(targetFile).getChannel();
            output.transferFrom(input, 0, input.size());
            input.close();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CopyFunction copyFunction = new CopyFunction();
        copyFunction.fileCopy_channel("F:/r/2.xml","F:/r/zip/djfakjf/2.xml");
    }



}
