package bean.function;

import java.awt.*;

public class GridPref {
    private static GridBagConstraints gbc;

    public GridPref() {
        gbc = new GridBagConstraints();
    }

    public static GridBagConstraints getGridPref(int gridx, int gridy, int gridwidth, int gridheight, Double weightx, Double weighty, int anchor, int fill) {
        gbc = new GridBagConstraints();
        return getGridPref(gridx,gridy,gridwidth,gridheight,weightx,weighty,anchor,fill,1,1,1,1,0,0);

    }
    public static GridBagConstraints getGridPref(int gridx,int gridy,int gridwidth,int gridheight,Double weightx,Double weighty,int anchor,int fill,int a,int b,int c,int d,int ipadx,int ipady) {
        gbc = new GridBagConstraints();
        gbc.gridx=gridx;    //设置组件所处行的起始坐标
        gbc.gridy=gridy;    //设置组件所处列的起始坐标
        gbc.gridwidth=gridwidth;    //设置组件横向的单元格跨越个数
        gbc.gridheight=gridheight;   //设置组件纵向的单元格跨越个数
        gbc.weightx=weightx;    //设置窗口变大时的缩放比例。
        gbc.weighty=weighty;
        gbc.anchor=anchor;    //设置组件在单元格中的对齐方式center:10
        gbc.fill=fill;    //组件未能填满单元格时，可由此属性设置横向、纵向或双向填满both:1
        gbc.insets=new Insets(a,b,c,d);    //设置组件与单元格边界的间距：上，左，下，右
        gbc.ipadx=ipadx;    //修改组件的首选大小，如果为正数：在首选大小的基础上增加指定的宽度和高度，如果为负数：在指定首选大小基础上减少指定宽度和高度
        gbc.ipady=ipady;
        return gbc;
    }

}
