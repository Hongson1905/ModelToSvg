package bean.function;

import java.awt.*;
import java.io.*;

public class SetOwnFont {
    private Font ownFont = null;
      
    public Font getOwnFont(int style,float size) {  
        String fontUrl="src/data/HWKT.ttf";
        if (ownFont == null) {    
            InputStream is = null;
            BufferedInputStream bis = null;
            try {    
				is =new FileInputStream(new File(System.getProperty("user.dir")+File.separator+fontUrl));//数据读到缓冲区
				bis = new BufferedInputStream(is);    
				ownFont = Font.createFont(Font.TRUETYPE_FONT, is);  
				ownFont = ownFont.deriveFont(size);//设置字体大小，float型
				ownFont = ownFont.deriveFont(style);
            } catch (FontFormatException e) {    
                e.printStackTrace();    
            } catch (IOException e) {
                e.printStackTrace();    
            } finally {
               try {    
                    if (null != bis) {    
                        bis.close();    
                    }    
                    if (null != is) {    
                        is.close();    
                    }    
                } catch (IOException e) {    
                    e.printStackTrace();    
                }  
            }    
        }    
        return ownFont;    
    }
}

