package bean.uiBeauty;

import javax.swing.border.Border;
import java.awt.*;

public class RoundRectBorder implements Border {
    Color color;
    int arc;
    public RoundRectBorder(){
        super();
        this.color = Color.RED;
    }
    public RoundRectBorder(Color color,int arc){
        this.color = color;
        this.arc = arc;
    }
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        ((Graphics2D) g).setStroke(new BasicStroke(3.0f));
        g.drawRoundRect(0,0,c.getWidth()-1,c.getHeight()-1,arc,arc);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(0,0,0,0);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
