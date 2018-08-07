package bean.function;

import ui.UnborderFrame;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

public class MoveWindow {
    public static UnborderFrame frame;
    public static Component component;
    // 全局的位置变量，用于表示鼠标在窗口上的位置
    static Point origin = new Point();

    /**
     * Create the application.
     */
    public MoveWindow(UnborderFrame frame) {
        this.frame = frame;
        initialize();
    }



    public static void setMove(UnborderFrame frame){
        MoveWindow.frame = frame;
        initialize();
    }

    public static void setMove(UnborderFrame frame,Component component){
        MoveWindow.frame = frame;
        MoveWindow.component = component;
        initializeForCom();
    }

    /**
     * Initialize the contents of the frame.
     */
    private static void initialize() {

        toolPanelMouseListener(frame);
        frame.addMouseMotionListener(new MouseMotionAdapter() {
            // 拖动（mouseDragged 指的不是鼠标在窗口中移动，而是用鼠标拖动）
            public void mouseDragged(MouseEvent e) {
                // 当鼠标拖动时获取窗口当前位置
                Point p = frame.getLocation();
                // 设置窗口的位置
                // 窗口当前的位置 + 鼠标当前在窗口的位置 - 鼠标按下的时候在窗口的位置
                frame.setLocation(p.x + e.getX() - origin.x, p.y + e.getY() - origin.y);
            }
        });
    }


    /**
     * Initialize the contents of the frame.
     */
    private static void initializeForCom() {

        toolPanelMouseListener(component);
        component.addMouseMotionListener(new MouseMotionAdapter() {
            // 拖动（mouseDragged 指的不是鼠标在窗口中移动，而是用鼠标拖动）
            public void mouseDragged(MouseEvent e) {
                // 当鼠标拖动时获取窗口当前位置
                Point p = frame.getLocation();
                // 设置窗口的位置
                // 窗口当前的位置 + 鼠标当前在窗口的位置 - 鼠标按下的时候在窗口的位置
                frame.setLocation(p.x + e.getX() - origin.x, p.y + e.getY() - origin.y);
            }
        });
    }

    private static void toolPanelMouseListener(Component component) {
        component.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getClickCount());
                System.out.println(e.getButton());
                if ((e.getClickCount() == 2&&(e.getButton()==MouseEvent.BUTTON1))) {
                    if (frame.getSizeState()== UnborderFrame.NORMAL_SIZE) {
                        frame.setSiseState(UnborderFrame.MAX_SIZE);
                    } else if(frame.getSizeState()== UnborderFrame.MAX_SIZE){
                        frame.setSiseState(UnborderFrame.NORMAL_SIZE);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // 当鼠标按下的时候获得窗口当前的位置
                origin.x = e.getX();
                origin.y = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
}
