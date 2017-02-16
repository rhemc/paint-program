package ca.rhemc.paintprogram.paint;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class SprayPaint extends Polyline {

    private List<Point> pointArray;
    private float radius;
    private Thread print;
    private boolean draw = true,newPoints = true;

    public SprayPaint(int x, int y, Color colour, float lineThickness, boolean fill, int strokeStyle, float radius, PaintPanel paintPanel) {
        super(x, y, colour, lineThickness, fill, strokeStyle);
        this.radius = radius;
        this.pointArray = new ArrayList<Point>();
        randomPoints();
        print = new Thread(() -> {
            while(draw) {
                if(newPoints){
                    randomPoints();
                    paintPanel.repaint();
                }else{
                    newPoints=true;
                }
                System.out.println("run");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
                System.out.println("run");
            }
        });
        print.start();
    }

    @Override
    public void mouseMoved(int x, int y) {
        last = new Point(x, y);
        path.lineTo(x,y);
        newPoints=false;
        randomPoints();
    }

    @Override
    public void print(Graphics2D g2) {
        prepare(g2);
        synchronized (pointArray) {
            for (Point point : pointArray) {
                g2.fillRect(point.x, point.y, 1, 1);
            }
        }
    }

    public void randomPoints() {
        double centerX = last.x;
        double centerY = last.y;
        double area = Math.PI*radius*radius;
        int amount = (int)Math.ceil(area/3);
        ArrayList<Point> pointArray = new ArrayList<Point>();
        for (int i = 0; i < amount; i++) {
            double angle = 2*Math.PI*Math.random();
            double absoluteDistance = Math.random()+Math.random();
            double distance;
            if (absoluteDistance > 1) {
                distance = 2-absoluteDistance;
            }
            else {
                distance = absoluteDistance;
            }
            pointArray.add(new Point((int) (centerX + distance * Math.cos(angle) * radius*2),
                        (int) (centerY + distance * Math.sin(angle) * radius*2)));
        }
        synchronized (this.pointArray) {
            this.pointArray.addAll(pointArray);
        }
    }

    @Override
    public void end() {
        super.end();
        print.interrupt();
        draw=false;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

}
