package ui;

import bean.function.GridPref;
import bean.function.MoveWindow;
import bean.function.OutPoint;
import bean.uiBeauty.RoundRectPanel;
import bean.using.PanelForContent;
import com.sun.awt.AWTUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

/**
 * ui界面主函数
 */
public class UnborderFrame extends JFrame{
    public   RoundRectPanel panel;//基础容器
    private RoundRectPanel toolPanel;//工具栏：缩放，关闭，移动
    private JLabel closeLabel;
    private JLabel minLabel ;
    private JLabel maxLabel ;
    private JLabel titleLabel ;
    private int sizeState = 1;//正常大小
    public static int MIN_SIZE = 0;//最小
    public static int NORMAL_SIZE = 1;//正常大小
    public static int MAX_SIZE = 2;//最大
    public static int MAX_SIZE_COVER = 3;//最大，覆盖工具栏
    private int width = 900;
    private int height = 500;

    public JPanel contentPanel = new PanelForContent();//内容面板

    public UnborderFrame(){
        super();
        initWindow();
    }
    private void initWindow(){
        this.setBounds((OutPoint.screenWidth-width)/2, (OutPoint.screenHeight-height)/2, width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(null);
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        AWTUtilities.setWindowOpaque(this, false);//JFrame设置为透明

        initPanel();
        this.setContentPane(panel);

        initToolPanel();
        panel.add(toolPanel,GridPref.getGridPref(0,0,1,1,1.0,0.0,10,2));
        panel.add(contentPanel,GridPref.getGridPref(0,1,1,1,1.0,1.0,10,1));


        MoveWindow.setMove(this,toolPanel);
        setIconImage(new ImageIcon(System.getProperty("user.dir")+"/src/data/modelToSvg.png").getImage());
    }

    private void  initPanel(){
        panel = new RoundRectPanel(7);
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);//Panel设置为透明
    }

    //工具栏初始化
    private void initToolPanel(){
        toolPanel = new RoundRectPanel(7,new Color(224,242,164));
        toolPanel.setOpaque(false);
        toolPanel.setLayout(new GridBagLayout());

        titleLabel = new JLabel();titleLabel.setText("Model_Server");setLabelIcon(titleLabel,"src/data/modelToSvg.png",new Color(224,242,164),25);
        closeLabel = new JLabel();closeLabel.setToolTipText("close");setLabelIcon(closeLabel,"src/data/close.png",new Color(224,242,164),20);
        minLabel = new JLabel();minLabel.setToolTipText("min");setLabelIcon(minLabel,"src/data/min.png",new Color(224,242,164),15);
        maxLabel = new JLabel();maxLabel.setToolTipText("max");setLabelIcon(maxLabel,"src/data/max.png",new Color(224,242,164),15);
        closeLabel.setBorder(null);
        minLabel.setBorder(null);
        maxLabel.setBorder(null);
        closeLabel.addMouseListener(toolMouseListener);
        minLabel.addMouseListener(toolMouseListener);
        maxLabel.addMouseListener(toolMouseListener);

        toolPanel.add(titleLabel,GridPref.getGridPref(0,0,1,1,1.0,0.0,17,1));
        toolPanel.add(minLabel, GridPref.getGridPref(1,0,1,1,0.01,1.0,10,3));
        toolPanel.add(maxLabel, GridPref.getGridPref(2,0,1,1,0.01,1.0,10,3));
        toolPanel.add(closeLabel, GridPref.getGridPref(3,0,1,1,0.01,1.0,10,3));

    }

    public JPanel getPanel(){
        return contentPanel;
    }

    private void setLabelIcon(JLabel tmpLabel,String url,Color bgc,int w){
        ImageIcon tmpIcon = new ImageIcon(System.getProperty("user.dir")+File.separator+url);
        tmpIcon.setImage(tmpIcon.getImage().getScaledInstance(w, w, Image.SCALE_SMOOTH));
        tmpLabel.setIcon(tmpIcon);
        tmpLabel.setOpaque(true);
        tmpLabel.setBackground(bgc);
    }

    private MouseListener toolMouseListener = new MouseListener() {
        JLabel tmpLabel = new JLabel();
        String commend = "";
        @Override
        public void mouseClicked(MouseEvent e) {
            tmpLabel = (JLabel) e.getSource();
            commend = tmpLabel.getToolTipText();
            if(commend.equals("close")){
                System.exit(0);
            }else if (commend.equals("min")){
                setSiseState(UnborderFrame.MIN_SIZE);

            }else if (commend.equals("max")){
                if(getSizeState() != UnborderFrame.MAX_SIZE) {
                    setSiseState(UnborderFrame.MAX_SIZE);//最大化窗体
                }else {
                    setSiseState(UnborderFrame.NORMAL_SIZE);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            tmpLabel = (JLabel) e.getSource();
            commend = tmpLabel.getToolTipText();
            if(commend.equals("close")){
                closeLabel.setBorder(BorderFactory.createEtchedBorder());
                closeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }else if (commend.equals("min")){
                minLabel.setBorder(BorderFactory.createEtchedBorder());
                minLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }else if (commend.equals("max")){
                maxLabel.setBorder(BorderFactory.createEtchedBorder());
                maxLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

        }

        @Override
        public void mouseExited(MouseEvent e) {
            tmpLabel = (JLabel) e.getSource();
            commend = tmpLabel.getToolTipText();
            if(commend.equals("close")){
                closeLabel.setBorder(null);
                closeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }else if (commend.equals("min")){
                minLabel.setBorder(null);
                minLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }else if (commend.equals("max")){
                maxLabel.setBorder(null);
                maxLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    };

    //窗口的放大缩小
    public void setSiseState(int w){
        if(w == 0){
            setExtendedState(JFrame.ICONIFIED);
        }else if(w == 1){
            setBounds((OutPoint.screenWidth-width)/2, (OutPoint.screenHeight-height)/2, width, height);
        }else if(w == 2){
            Rectangle   bounds   =   new   Rectangle(OutPoint.screenWidth,OutPoint.screenHeight);
            Insets   insets   =   Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
            bounds.x   +=   insets.left;
            bounds.y   +=   insets.top;
            bounds.width   -=   insets.left   +   insets.right;
            bounds.height   -=   insets.top   +   insets.bottom;
            setBounds(bounds);
        }else if(w == 3){
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }else{
            setBounds(new Rectangle(width,height));
        }
        sizeState = w;
    }

    public int getSizeState(){
        return sizeState;
    }


}
