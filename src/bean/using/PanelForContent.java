package bean.using;

import javax.swing.*;
import java.awt.*;

public class PanelForContent extends JPanel{


    public PanelForContent(){
        super();
        setBackground(Color.white);
    }

    public void paint(Graphics g){
        super.paint(g);
        g.setColor(new Color(0,0,0,100));
        g.drawString("北京科东电力控制系统有限责任公司", getWidth()/2-16*7,getHeight()-10);//版权标识绘制
        g.setColor(new Color(0,0,0,255));
    }






}
