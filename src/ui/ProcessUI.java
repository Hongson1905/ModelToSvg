package ui;

import bean.function.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

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
    private MakeConn makeConn = new MakeConn();

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
        titleField.setEditable(false);
        titleField.setOpaque(false);
        titleField.setBorder(null);
        titleField.setForeground(new Color(45,79,58));
        titleField.setFont(setOwnFont.getOwnFont(1,28));
        subTitleField = new JTextField("1");subTitleField.setBorder(null);
        subTitleField.setFont(new Font("楷体",0,18));
        subTitleField.setEditable(false);
        subTitleField.setOpaque(false);

        sLabel = new JLabel();
        setLabelIcon(sLabel,"src/data/stop.png",40,40,Image.SCALE_SMOOTH);
        sLabel.setToolTipText("stop");
        sLabel.setBackground(new Color(139,204,245));
        sLabel.addMouseListener(processUImouseListener);

        backLabel = new JLabel();
        setLabelIcon(backLabel,"src/data/back.png",30,30,Image.SCALE_SMOOTH);
        backLabel.setToolTipText("back");
        backLabel.setBackground(new Color(139,204,245));
        backLabel.addMouseListener(processUImouseListener);

        topPanel.add(titleLabel, GridPref.getGridPref(0,0,1,2,0.0,0.0,10,1,0,30,0,15,0,0));
        topPanel.add(titleField,GridPref.getGridPref(1,0,1,1,1.0,0.0,10,1));
        topPanel.add(subTitleField,GridPref.getGridPref(1,1,1,1,1.0,0.0,17,1));
        topPanel.add(sLabel,GridPref.getGridPref(2,0,1,1,0.0,0.0,10,1,0,0,5,12,0,0));
        topPanel.add(backLabel,GridPref.getGridPref(2,1,1,1,0.0,0.0,10,0,5,5,0,17,0,0));

        contentPanel = new JPanel();
        processLabel = new JLabel();
        processArea = new JTextArea();
        processArea.setFont(new Font("楷体",0,15));
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.add(processArea,GridPref.getGridPref(0,0,1,1,1.0,1.0,10,1));
        contentScrollPanel = new JScrollPane(contentPanel);
        contentScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentScrollPanel.getVerticalScrollBar().setValue(contentScrollPanel.getVerticalScrollBar().getMaximum());
        contentScrollPanel.getVerticalScrollBar().setValue(contentScrollPanel.getVerticalScrollBar().getMaximum());
        panel.add(topPanel, GridPref.getGridPref(0,0,1,1,1.0,0.1,10,1));
        panel.add(contentScrollPanel,GridPref.getGridPref(0,1,1,1,1.0,1.0,10,1,0,0,20,0,0,0));

        panel.validate();
    }

    //图模转换（全步骤）
    public void allMerge(){
        ModelMerge.depand = true;
        if(modelMerge.getDirFile(PropertyGet.prop.getProperty("zipSvg")).size()<1){
            JOptionPane.showMessageDialog(FMain.mainFrame,
                    "图形压缩文件不存在!\n请检查："+PropertyGet.prop.getProperty("zipSvg")+"下的文件！", "系统信息", JOptionPane.ERROR_MESSAGE);
            System.out.println("---未检测到图形压缩文件---");
            ModelMerge.depand = false;
            return;
        }else if(modelMerge.getDirFile(PropertyGet.prop.getProperty("zipModel")).size()<1){
            JOptionPane.showMessageDialog(FMain.mainFrame,
                    "模型压缩文件不存在!\n请检查："+PropertyGet.prop.getProperty("zipModel")+"下的文件！", "系统信息", JOptionPane.ERROR_MESSAGE);
            System.out.println("---未检测到模型压缩文件---");
            ModelMerge.depand = false;
            return;
        } else {
            initUI("压缩文件图模转换","src/data/001.png");
            modelMerge.startMerge(titleLabel,subTitleField,processArea);
        }
    }
    //模型导入
    public void inputMerge(){
        ModelMerge.depand = true;
        if(modelMerge.getDirFile(PropertyGet.prop.getProperty("zipSvg")).size()<1){
            JOptionPane.showMessageDialog(FMain.mainFrame,
                    "图形压缩文件不存在!\n请检查："+PropertyGet.prop.getProperty("zipSvg")+"下的文件！", "系统信息", JOptionPane.ERROR_MESSAGE);
            System.out.println("---未检测到图形压缩文件---");
            ModelMerge.depand = false;
            return;
        }else if(modelMerge.getDirFile(PropertyGet.prop.getProperty("zipModel")).size()<1){
            JOptionPane.showMessageDialog(FMain.mainFrame,
                    "模型压缩文件不存在!\n请检查："+PropertyGet.prop.getProperty("zipModel")+"下的文件！", "系统信息", JOptionPane.ERROR_MESSAGE);
            System.out.println("---未检测到模型压缩文件---");
            ModelMerge.depand = false;
            return;
        } else {
            panel.removeAll();
            panel.repaint();
            initUI("模型导入","src/data/input.png");
            modelMerge.startInput(titleLabel,subTitleField,processArea);
        }

    }
    //模型拼接
    public void justMerge(){
        panel.removeAll();
        panel.repaint();
        ModelMerge.depand = true;
        initUI("模型拼接","src/data/merge.png");
        subTitleField.setText("模型拼接");
        modelMerge.StartJustMerge(titleLabel,subTitleField,processArea);


    }
    //模型转换成图形
    public void convertMerge(){
        panel.removeAll();
        panel.repaint();
        initUI("图模转换","src/data/convert.png");
        modelMerge.StartConvertMerge(titleLabel,subTitleField,processArea);

    }


    //为label添加图标
    public void setLabelIcon(JLabel label,String url){
        if(url.indexOf("001")>=0){
            setLabelIcon(label,url,120,70,Image.SCALE_SMOOTH);
        }else {
            setLabelIcon(label,url,90,90,Image.SCALE_DEFAULT);
        }

    }
    public void setLabelIcon(JLabel label,String url,int w,int h,int convertType){
        tmpIcon = new ImageIcon(System.getProperty("user.dir")+ File.separator+url);
        tmpIcon.setImage(tmpIcon.getImage().getScaledInstance(w, h, convertType));
        label.setIcon(tmpIcon);
    }


    MouseListener processUImouseListener = new MouseListener() {
        JLabel tmpLabel;
        @Override
        public void mouseClicked(MouseEvent e) {
            tmpLabel = (JLabel) e.getSource();
            if("stop".equals(tmpLabel.getToolTipText())){
                stopNowProc();
                setLabelIcon(tmpLabel,"src/data/start.png",40,40,Image.SCALE_SMOOTH);
                tmpLabel.setToolTipText("start");
                tmpLabel.repaint();
                tmpLabel.setOpaque(false);
                processArea.append("=== stopped ===");
                processArea.append("\n");
                processArea.repaint();

            }else if("back".equals(tmpLabel.getToolTipText())){
                stopNowProc();
                primaryUI = new PrimaryUI(panel);
            }else if("start".equals(tmpLabel.getToolTipText())){
                setLabelIcon(tmpLabel,"src/data/stop.png",40,40,Image.SCALE_SMOOTH);
                tmpLabel.setToolTipText("stop");
                tmpLabel.repaint();
                tmpLabel.setOpaque(false);
                if("模型导入".equals(subTitleField.getText())){
                    modelMerge.startInput(titleLabel,subTitleField,processArea);
                }else if("模型拼接".equals(subTitleField.getText())){
                    modelMerge.StartJustMerge(titleLabel,subTitleField,processArea);
                }else if("图模转换".equals(subTitleField.getText())){
                    modelMerge.StartConvertMerge(titleLabel,subTitleField,processArea);
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
            if("stop".equals(tmpLabel.getToolTipText())||"back".equals(tmpLabel.getToolTipText())){
                tmpLabel.setOpaque(true);
                tmpLabel.repaint();
                tmpLabel.validate();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            tmpLabel = (JLabel) e.getSource();
            if("stop".equals(tmpLabel.getToolTipText())||"back".equals(tmpLabel.getToolTipText())){
                tmpLabel.setOpaque(false);
                tmpLabel.repaint();
                tmpLabel.validate();
            }
        }
    };

    private void stopNowProc(){
        if("图模转换".equals(titleField.getText())){

        }else if("模型导入".equals(subTitleField.getText())){
            try {
                makeConn.getSession().execCommand("killall "+PropertyGet.prop.getProperty("svgModelZip"));
                makeConn.getSession().close();
                ModelMerge.depand = false;
                setLabelIcon(titleLabel,"src/data/input.png");

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }else if("模型拼接".equals(subTitleField.getText())){
            try {
                makeConn.getSession().execCommand("killall "+PropertyGet.prop.getProperty("model_merge_proc"));
                makeConn.getSession().close();
                ModelMerge.depand = false;
                setLabelIcon(titleLabel,"src/data/merge.png");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }else if("图模转换".equals(subTitleField.getText())){
            try {
                makeConn.getSession().execCommand("killall "+PropertyGet.prop.getProperty("model_convert_proc"));
                makeConn.getSession().close();
                ModelMerge.depand = false;
                setLabelIcon(titleLabel,"src/data/convert.png");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


}
