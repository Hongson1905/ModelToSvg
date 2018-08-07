package bean.uiBeauty;

import javax.swing.*;
import java.awt.*;

public class RoundRectPanel extends JPanel {
    public int arc = 40;
    public int jfW;
    public int jfH = 100;
    public Color color = Color.white;
    public RoundRectPanel(){
        super();
    }
    public RoundRectPanel(int arc){
        this.arc = arc;
    }
    public RoundRectPanel(int arc,Color color){
        this.arc = arc;
        this.color = color;
    }
    @Override
    protected void paintComponent(Graphics g) {
        ((Graphics2D)g).setPaint(new GradientPaint(0,0,color,0,jfH,color));
        g.fillRoundRect(0,0,this.getWidth(),this.getHeight(),arc,arc);
        super.paintComponent(g);
    }
}
