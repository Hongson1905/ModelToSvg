package ui;

import bean.function.GridPref;
import bean.function.SetOwnFont;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

/**
 * @description 主窗口初始化
 */
public class PrimaryUI {

    SetOwnFont setOwnFont = new SetOwnFont();
    private JPanel panel;
    private JLabel logo001;
    private CircleButton mergeButton;
    private JLabel textLabel01;//描述：大标题
    private JLabel textLabel02;//描述：详细步骤
    private JLabel oneStep;//单步执行
    private ImageIcon tmpIcon;
    private OneStepMenu oneStepMenu;//单步执行菜单
    private JLabel inputMerge;//模型导入
    private JLabel justMerge;//模型拼接
    private JLabel convertMerge;//图模转换
    Thread tmpThead = null;
    private boolean oneStepLabelEntered = false;
    private boolean oneStepMenuEntered = false;

    private ProcessUI modelToSvg ;//功能执行内容展示


    public PrimaryUI(JPanel panel){
        this.panel = panel;
        this.panel.setLayout(new GridBagLayout());
        modelToSvg = new ProcessUI(this.panel);
        init();

    }

    public void init(){
        panel.removeAll();
        logo001 = new JLabel();
        tmpIcon = new ImageIcon(System.getProperty("user.dir")+ File.separator+"src/data/001.png");
        tmpIcon.setImage(tmpIcon.getImage());
        tmpIcon.setImage(tmpIcon.getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH));
        logo001.setIcon(tmpIcon);
        panel.add(logo001, GridPref.getGridPref(0,0,1,3,0.0,0.0,10,1,-100,-70,0,50,0,0));

        textLabel01 = new JLabel("保电系统图模服务");
        textLabel01.setFont(setOwnFont.getOwnFont(1,40));
        textLabel01.setForeground(new Color(105,135,101));
        panel.add(textLabel01,GridPref.getGridPref(1,0,2,1,0.0,0.0,10,0,-110,0,0,0,0,0));

        textLabel02 = new JLabel("模型导入、模型拼接、图形转换");
        textLabel02.setFont(new Font("system",1,15));
        textLabel02.setForeground(new Color(171,181,170));
        panel.add(textLabel02,GridPref.getGridPref(1,1,2,1,0.0,0.0,10,0,-30,0,0,0,0,0));



        mergeButton = new CircleButton();
        mergeButton = new CircleButton(" 图模转换 ");
        mergeButton.setBUTTON_COLOR1(new Color(125, 161, 237));
        mergeButton.setBUTTON_COLOR2(new Color(91, 118, 173));
        mergeButton.setBUTTON_FOREGROUND_COLOR1(new Color(106,240,79));
        mergeButton.setBUTTON_FOREGROUND_COLOR2(new Color(106,240,79));
        mergeButton.setClickTran(0.8F);
        mergeButton.setExitTran(0.3F);
        Dimension preferredSize = new Dimension(230,40);//设置尺寸
        mergeButton.setPreferredSize(preferredSize);
        mergeButton.setActionCommand("allMerge");
        mergeButton.addActionListener(primaryUIActionListener);
        panel.add(mergeButton, GridPref.getGridPref(1,2,1,1,0.0,0.0,13,0,0,40,0,0,0,0));

        oneStep = new JLabel("单步执行");
        tmpIcon = new ImageIcon(System.getProperty("user.dir")+ File.separator+"src/data/oneStep.png");
        tmpIcon.setImage(tmpIcon.getImage());
        tmpIcon.setImage(tmpIcon.getImage().getScaledInstance(40, 50, Image.SCALE_SMOOTH));
        oneStep.setIcon(tmpIcon);
        oneStep.setHorizontalTextPosition(JLabel.CENTER);
        oneStep.setVerticalTextPosition(JLabel.BOTTOM);
        oneStep.setForeground(new Color(171,181,170));
        oneStep.addMouseListener(primaryUIMouselistener01);
        oneStep.setBackground(new Color(232,250,239));
        panel.add(oneStep, GridPref.getGridPref(2,2,1,1,0.0,0.0,13,0,20,0,0,0,0,0));

        //单步菜单
        oneStepMenu = new OneStepMenu();oneStepMenu.setSize(150,150);
        inputMerge = new JLabel("模型导入");initSetpMenuItem(inputMerge,"");
        justMerge = new JLabel("模型拼接");initSetpMenuItem(justMerge,"");
        convertMerge = new JLabel("图模转换");initSetpMenuItem(convertMerge,"");
        oneStepMenu.getContentPanel().addMouseListener(primaryUIMouselistener02);
        inputMerge.addMouseListener(primaryUIMouselistener03);inputMerge.addMouseListener(primaryUIMouselistener02);
        justMerge.addMouseListener(primaryUIMouselistener03);justMerge.addMouseListener(primaryUIMouselistener02);
        convertMerge.addMouseListener(primaryUIMouselistener03);convertMerge.addMouseListener(primaryUIMouselistener02);
        oneStepMenu.getContentPanel().setLayout(null);
        inputMerge.setBounds(23,0,150,60);
        justMerge.setBounds(23,47,150,60);
        convertMerge.setBounds(23,90,150,60);
        oneStepMenu.getContentPanel().add(inputMerge);
        oneStepMenu.getContentPanel().add(justMerge);
        oneStepMenu.getContentPanel().add(convertMerge);
        oneStepMenu.getContentPanel().repaint();


        panel.repaint();
        panel.validate();

    }


    private void initSetpMenuItem(JLabel label,String url){
        label.setFont(new Font("楷体",1,23));
        label.setForeground(new Color(5,135,55));
        if(!url.equals("")){
            tmpIcon = new ImageIcon(System.getProperty("user.dir")+ File.separator+url);
            tmpIcon.setImage(tmpIcon.getImage().getScaledInstance(120, 40, Image.SCALE_SMOOTH));
            label.setIcon(tmpIcon);
        }
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.CENTER);
    }

    final MouseListener primaryUIMouselistener01 = new MouseListener() {
        JLabel tmpLabel = new JLabel();
        Point labelPoint;

        @Override
        public void mouseClicked(MouseEvent e) {

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
            labelPoint = tmpLabel.getLocation();
            if (tmpLabel.getText().equals("单步执行")) {
                tmpLabel.setOpaque(true);
                tmpLabel.repaint();
                oneStepLabelEntered = true;
                if(oneStepLabelEntered&&!oneStepMenu.isVisible()){
                    oneStepMenu.setLocation(FMain.mainFrame.getX()+tmpLabel.getLocation().x+tmpLabel.getWidth(),
                            FMain.mainFrame.getY()+tmpLabel.getLocation().y+50);
                    oneStepMenu.setVisible(true);
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            tmpLabel = (JLabel) e.getSource();
            if (tmpLabel.getText().equals("单步执行")) {
                tmpLabel.setOpaque(false);
                tmpLabel.repaint();
                oneStepLabelEntered = false;
                tmpThead = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tmpThead.sleep(200);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        if(!oneStepLabelEntered&&!oneStepMenuEntered) {
                            oneStepMenu.setVisible(false);
                        }
                    }
                });
                tmpThead.start();

            }
        }
    };
    //单步菜单窗口监听
    MouseListener primaryUIMouselistener02 = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            oneStepMenuEntered = true;

        }

        @Override
        public void mouseExited(MouseEvent e) {
            oneStepMenuEntered = false;
            tmpThead = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        tmpThead.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    if(!oneStepMenuEntered&&!oneStepLabelEntered){
                        oneStepMenu.setVisible(false);
                    }
                }
            });
            tmpThead.start();
        }
    };

    //单步菜单项监听
    MouseListener primaryUIMouselistener03 = new MouseListener() {
        JLabel tmpLabel ;
        @Override
        public void mouseClicked(MouseEvent e) {
            tmpLabel = (JLabel) e.getSource();
            if(tmpLabel.getText().equals("模型导入")){
                oneStepMenuEntered = false;
                oneStepLabelEntered = false;
                oneStepMenu.setVisible(false);
                System.out.println("inputMerge");
                modelToSvg.inputMerge();
            }else if(tmpLabel.getText().equals("模型拼接")){
                oneStepMenuEntered = false;
                oneStepLabelEntered = false;
                oneStepMenu.setVisible(false);
                System.out.println("justMerge");
                modelToSvg.justMerge();
            }else if(tmpLabel.getText().equals("图模转换")){
                oneStepMenuEntered = false;
                oneStepLabelEntered = false;
                oneStepMenu.setVisible(false);
                System.out.println("convertMerge");
                modelToSvg.convertMerge();
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
            try {
                tmpLabel = (JLabel) e.getSource();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            if(tmpLabel.getText().equals("模型导入")||tmpLabel.getText().equals("模型拼接")){
                tmpIcon = new ImageIcon(System.getProperty("user.dir")+ File.separator+"src/data/menu02.png");
                tmpIcon.setImage(tmpIcon.getImage().getScaledInstance(120, 40, Image.SCALE_SMOOTH));
                tmpLabel.setIcon(tmpIcon);
                tmpLabel.repaint();
            }else if(tmpLabel.getText().equals("图模转换")){
                tmpIcon = new ImageIcon(System.getProperty("user.dir")+ File.separator+"src/data/menu03.png");
                tmpIcon.setImage(tmpIcon.getImage().getScaledInstance(120, 40, Image.SCALE_SMOOTH));
                tmpLabel.setIcon(tmpIcon);
                tmpLabel.repaint();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            try {
                tmpLabel = (JLabel) e.getSource();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            if(tmpLabel.getText().equals("模型导入")||tmpLabel.getText().equals("模型拼接")||tmpLabel.getText().equals("图模转换")){
                tmpLabel.setIcon(null);
                tmpLabel.repaint();
            }

        }
    };

    ActionListener primaryUIActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if("allMerge".equals(e.getActionCommand())){
                System.out.println("allMerge");
                modelToSvg.allMerge();
            }

        }
    };
}
