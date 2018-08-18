package bean.using;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

class Test {


    public void fd() throws BadLocationException {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(300, 300, 400, 300);
        JTextPane jp = new JTextPane();
        jp.setBackground(Color.gray);
        jp.setText("可以预定义内容");
        jp.setEditable(false);
        SimpleAttributeSet attrset = new SimpleAttributeSet();
        StyleConstants.setFontSize(attrset,24);
        StyleConstants.setForeground(attrset,Color.blue);
        Document doc = jp.getDocument();
        doc.insertString(doc.getLength(),"\ndfasdfas",attrset);
        try {
            jp.getDocument().insertString(54,"",attrset);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        jp.setBounds(100, 100, 40, 80);
        frame.setVisible(true);
        frame.add(jp);


    }

    public static void main(String[] args) {
        Test test = new Test();
        try {
            test.fd();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }

}