package ui;

import bean.function.GridPref;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class OneStepMenu extends JWindow{

    public OneStepMenu(){
        super();
        getContentPane().setLayout(new GridBagLayout());
        contentPanel.setLayout(new GridBagLayout());
        getContentPane().add(contentPanel, GridPref.getGridPref(0,0,1,1,1.0,1.0,10,1));
        contentPanel.setOpaque(false);
        com.sun.awt.AWTUtilities.setWindowOpaque(this, false);
    }


    JPanel contentPanel = new JPanel(){
        @Override
        public void paint(Graphics g){
            Image im = null;
            try {
                im = ImageIO.read(new File(System.getProperty("user.dir")+ File.separator+"src/data/menu01.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            g.drawImage(im, 0, 0, contentPanel.getWidth(),contentPanel.getHeight(), null);
            super.paint(g);
        }
    };

    public JPanel getContentPanel(){
        return contentPanel;
    }

}
