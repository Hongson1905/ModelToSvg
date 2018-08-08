package bean.function;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HandleBackStream {
    /**
     * @description 处理执行命令执行后的返回流
     */
    public HandleBackStream(){

    }

    public static boolean handleStream(final InputStream in) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        while (null != (line = br.readLine())){
            System.out.println(line);
        }
        return true;

    }
}
