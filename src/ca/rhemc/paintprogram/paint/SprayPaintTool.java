package ca.rhemc.paintprogram.paint;

import ca.rhemc.paintprogram.pointer.ModifierEvent;
import ca.rhemc.paintprogram.pointer.PointerEvent;

import java.awt.event.MouseEvent;

public class SprayPaintTool implements ShapeManipulatorStrategy {

    private StylePanel style;
    private PaintShape[] shapes;
    private PaintPanel paintPanel;

    public SprayPaintTool(StylePanel style, PaintPanel paintPanel, PaintShape[] shapes) {
        this.style = style;
        this.shapes = shapes;
        this.paintPanel = paintPanel;
    }

    @Override
    public Drawable deselect() {

        return null;
    }

    @Override
    public void selected() {

    }

    @Override
    public Drawable handlePointerUpdate(PointerEvent e) {
        Drawable rtn = null;
        switch(e.getID()) {
            case MouseEvent.MOUSE_PRESSED:
                shapes[e.getPointerId()] = new SprayPaint(e.getX(), e.getY(), style.getColour(),
                        1f, style.isFill(), style.getStrokeStyle(), style.getLineThickness(),paintPanel);
                break;
            case MouseEvent.MOUSE_MOVED:
                if(shapes[e.getPointerId()] != null) {
                    shapes[e.getPointerId()].mouseMoved(e.getX(), e.getY());
                    shapes[e.getPointerId()].setLineThickness(style.getLineThickness(e.getPressure()));
                }
                break;
            case MouseEvent.MOUSE_RELEASED:
                if(shapes[e.getPointerId()] == null)
                    break;
                shapes[e.getPointerId()].mouseMoved(e.getX(), e.getY());
                shapes[e.getPointerId()].setLineThickness(style.getLineThickness(e.getPressure()));
                ((SprayPaint)shapes[e.getPointerId()]).end();
                rtn = shapes[e.getPointerId()];
                shapes[e.getPointerId()] = null;
        }
        return rtn;
    }

    @Override
    public boolean handleModifierUpdated(ModifierEvent e) {

        return false;
    }


}
