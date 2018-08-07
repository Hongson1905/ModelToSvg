package ui;

import bean.function.GridPref;
import bean.function.ModelMerge;
import bean.function.PropertyGet;
import bean.function.SetOwnFont;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

/**
 * 模型转换进程UI处理
 */
public class ProcessUI {
    private JPanel panel;
    private JPanel topPanel;
    private JPanel contentPanel;
    private JScrollPane contentScrollPanel;
    public static JLabel titleLabel;//执行功能的图标
    public static JTextField titleField;//执行的功能
    public static JTextField subTitleField;//当前的进度
    private JLabel sLabel;//启停图标
    private JLabel backLabel;//返回图标
    public static JLabel processLabel;//内容区进程图标
    public static JTextArea processArea;//内容区文本域
    private SetOwnFont setOwnFont = new SetOwnFont();
    private PrimaryUI primaryUI;//用于返回初始化界面
    private ModelMerge modelMerge  = new ModelMerge();

    private ImageIcon tmpIcon;

    public ProcessUI(JPanel panel){
        this.panel = panel;
    }

    public void initUI(String title,String labelUrl){
        panel.removeAll();
        panel.repaint();
        backLabel = new JLabel("返回");
        topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setBackground(Color.white);
        topPanel.setSize(new Dimension(1,400));
        titleLabel = new JLabel();
        setLabelIcon(titleLabel,labelUrl);

        titleField = new JTextField(title);
        titleField.setBorder(null);
        titleField.setForeground(new Color(45,79,58));
        titleField.setFont(setOwnFont.getOwnFont(1,28));
        subTitleField = new JTextField("1");subTitleField.setBorder(null);
        subTitleField.setFont(new Font("楷体",0,18));

        sLabel = new JLabel();
        setLabelIcon(sLabel,"src/data/stop03.png",40,40);
        sLabel.setToolTipText("stop");
        sLabel.addMouseListener(processUImouseListener);

        backLabel = new JLabel();
        setLabelIcon(backLabel,"src/data/back.png",30,30);
        backLabel.setToolTipText("back");
        backLabel.addMouseListener(processUImouseListener);

        topPanel.add(titleLabel, GridPref.getGridPref(0,0,1,2,0.0,0.0,10,1,0,30,0,15,0,0));
        topPanel.add(titleField,GridPref.getGridPref(1,0,1,1,1.0,0.0,10,1));
        topPanel.add(subTitleField,GridPref.getGridPref(1,1,1,1,1.0,0.0,17,1));
        topPanel.add(sLabel,GridPref.getGridPref(2,0,1,1,0.0,0.0,10,1,0,0,5,12,0,0));
        topPanel.add(backLabel,GridPref.getGridPref(2,1,1,1,0.0,0.0,10,1,5,5,0,7,0,0));

        contentPanel = new JPanel();
        processLabel = new JLabel();
        processArea = new JTextArea();
        processArea.setFont(new Font("楷体",0,15));
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.add(processArea,GridPref.getGridPref(0,0,1,1,1.0,1.0,10,1));
        contentScrollPanel = new JScrollPane(contentPanel);
        contentScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentScrollPanel.getVerticalScrollBar().setValue(contentScrollPanel.getVerticalScrollBar().getMaximum());
        panel.add(topPanel, GridPref.getGridPref(0,0,1,1,1.0,0.1,10,1));
        panel.add(contentScrollPanel,GridPref.getGridPref(0,1,1,1,1.0,1.0,10,1,0,0,20,0,0,0));

        panel.validate();
    }

    //图模转换（全步骤）
    public void allMerge(){
        initUI("压缩文件图模转换","src/data/001.png");
    }
    //模型导入
    public void inputMerge(){

        if(modelMerge.getDirFile(PropertyGet.prop.getProperty("zipSvg")).size()<1){
            JOptionPane.showMessageDialog(FMain.mainFrame,
                    "图形压缩文件不存在!\n请检查："+PropertyGet.prop.getProperty("zipSvg")+"下的文件！", "系统信息", JOptionPane.ERROR_MESSAGE);
            System.out.println("---未检测到图形压缩文件---");
            return;
        }else if(modelMerge.getDirFile(PropertyGet.prop.getProperty("zipModel")).size()<1){
            JOptionPane.showMessageDialog(FMain.mainFrame,
                    "模型压缩文件不存在!\n请检查："+PropertyGet.prop.getProperty("zipModel")+"下的文件！", "系统信息", JOptionPane.ERROR_MESSAGE);
            System.out.println("---未检测到模型压缩文件---");
            return;
        } else {
            panel.removeAll();
            panel.repaint();
            initUI("图模导入","src/data/001.png");
            modelMerge.startInput(subTitleField,processArea);
        }

    }
    //模型拼接
    public void justMerge(){
        panel.removeAll();
        panel.repaint();
        initUI("模型拼接","src/data/001.png");
        subTitleField.setText("模型拼接");
        modelMerge.StartJustMerge(titleLabel,subTitleField,processLabel,processArea);


    }
    //模型转换成图形
    public void convertMerge(){
        panel.removeAll();
        panel.repaint();
        initUI("图模转换","src/data/001.png");
        modelMerge.StartConvertMerge();

    }


    //为label添加图标
    public void setLabelIcon(JLabel label,String url){
        setLabelIcon(label,url,120,70);
    }
    public void setLabelIcon(JLabel label,String url,int w,int h){
        tmpIcon = new ImageIcon(System.getProperty("user.dir")+ File.separator+url);
        tmpIcon.setImage(tmpIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        label.setIcon(tmpIcon);
    }


    MouseListener processUImouseListener = new MouseListener() {
        JLabel tmpLabel;
        @Override
        public void mouseClicked(MouseEvent e) {
            tmpLabel = (JLabel) e.getSource();
            if("stop".equals(tmpLabel.getToolTipText())){
                if("图模转换".equals(titleField.getText())){

                }else if("".equals(titleField.getText())){

                }else if("".equals(titleField.getText())){

                }else if("".equals(titleField.getText())){

                }
            }else if("back".equals(tmpLabel.getToolTipText())){
                primaryUI = new PrimaryUI(panel);
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
            if("stop".equals(tmpLabel.getToolTipText())){

            }else if("back".equals(tmpLabel.getToolTipText())){

            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            tmpLabel = (JLabel) e.getSource();
            if("stop".equals(tmpLabel.getToolTipText())){

            }else if("back".equals(tmpLabel.getToolTipText())){

            }
        }
    };
}
