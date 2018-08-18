package ui;

import bean.function.PropertyGet;

import java.awt.*;

public class FMain {
    public static UnborderFrame mainFrame;
    public static PropertyGet propertyGet;
    private PrimaryUI primaryUI;

    public FMain(){
        propertyGet = new PropertyGet("src/data/ServerProperty.properties ");//加载配置文件
        mainFrame = new UnborderFrame();//加载窗口
        mainFrame.getPanel().setOpaque(true);
        PrimaryUI primaryUI = new PrimaryUI(mainFrame.getPanel());

    }


    public void start(){
        mainFrame.repaint();
        mainFrame.setVisible(true);

    }



    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FMain fMain = new FMain();
                    fMain.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
