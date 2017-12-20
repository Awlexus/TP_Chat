package gui;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

import static gui.MainWindow.UI_SCALING;

/**
 * @author Matteo Cosi
 * @since 01.12.2017
 */
class BubbleBorder extends AbstractBorder {


    private Color color;
    private int thickness = 4;
    private int radii = 8;
    private int pointerSize = 7;
    private Insets insets = null;
    private BasicStroke stroke = null;
    private int strokePad;
    private int pointerPad = 4;
    private boolean left = true;
    RenderingHints hints;
    Chat.chatMessageType type;

    BubbleBorder(Chat.chatMessageType type,
                 Color color, int thickness, int radii, int pointerSize) {
        this.thickness = thickness;
        this.radii = radii;
        this.pointerSize = pointerSize;
        this.color = color;
        this.type = type;

        stroke = new BasicStroke(thickness);
        strokePad = thickness / 2;

        hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = radii + strokePad;
        switch (type) {
            case INFO:
                insets = new Insets(pad, pad, pad, pad);
                break;
            case FROM:
                insets = new Insets(pad, pad + pointerSize + strokePad, pad, pad);
                break;
            case TO:
                insets = new Insets(pad, pad, pad, pad + pointerSize + strokePad);
                break;
        }

    }


    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        return getBorderInsets(c);
    }

    @Override
    public void paintBorder(
            Component c,
            Graphics g,
            int x, int y,
            int width, int height) {

        Graphics2D g2 = (Graphics2D) g;


        RoundRectangle2D.Double bubble;

        Polygon pointer = new Polygon();

        if (type == Chat.chatMessageType.FROM) {
             bubble = new RoundRectangle2D.Double(
                    strokePad +pointerSize,
                    strokePad,
                    width -(thickness + pointerSize)-2,
                    height,
                    radii,
                    radii);
            pointer.addPoint((int) (pointerSize+strokePad),
                    (int) (2+ UI_SCALING*3));
            // right point
            pointer.addPoint((int) (pointerSize+strokePad),
                    (int) (10+ UI_SCALING*4));
            // bottom point
            pointer.addPoint((int) (0+UI_SCALING),
                    (int) (2+ UI_SCALING*3));
        } else if (type == Chat.chatMessageType.TO) {
            bubble = new RoundRectangle2D.Double(
                    strokePad,
                    strokePad,
                    width -( thickness+pointerSize),
                    height,
                    radii,
                    radii);
            pointer.addPoint((int) bubble.getWidth(),
                    (int) (2+ UI_SCALING*3));
            // right point
            pointer.addPoint((int) bubble.getWidth(),
                    (int) (10+ UI_SCALING*4));
            // bottom point
            pointer.addPoint((int) bubble.getWidth()+pointerSize-2,
                    (int) (2+ UI_SCALING*3));
        }else{
             bubble = new RoundRectangle2D.Double(
                    strokePad ,
                    strokePad,
                    width -thickness-1,
                    height,
                    radii,
                    radii);
        }

        Area area = new Area(bubble);
        area.add(new Area(pointer));

        g2.setRenderingHints(hints);

        Component parent = c.getParent();
        if (parent != null) {
            Color bg = parent.getBackground();
            Rectangle rect = new Rectangle(0, 0, width, height);
            Area borderRegion = new Area(rect);
            borderRegion.subtract(area);
            g2.setClip(borderRegion);
            g2.setColor(bg);
            g2.fillRect(0, 0, width, height);
            g2.setClip(null);
        }

        g2.setColor(color);
        g2.setStroke(stroke);
        g2.draw(area);

    }

}