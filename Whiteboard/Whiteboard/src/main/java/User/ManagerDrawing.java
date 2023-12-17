package User;
/**
 * Name: YUNCONG JI
 * Student id: 1373110
 */
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ManagerDrawing extends JPanel {
    Integer thickness;
    Integer red ;
    Integer green ;
    Integer blue ;
    Integer sX ;
    Integer sY ;
    Integer eX ;
    Integer eY ;
    Color color;



    private ArrayList<String> shapeList =new ArrayList<>();

    public void setList(ArrayList<String> shapeList){
        this.shapeList = shapeList;
    }

    public void paint(Graphics g){
        super.paint(g);
        draw((Graphics2D) g,this.shapeList);

    }

    public void draw(Graphics2D g2d, ArrayList<String> shapeList) {

        for (String line: shapeList) {
            String[] record = line.split(",");

            switch (record[0]) {
                case "Line":
                    thickness = Integer.parseInt(record[1]);
                    g2d.setStroke(new BasicStroke(thickness));
                    red = Integer.parseInt(record[2]);
                    green = Integer.parseInt(record[3]);
                    blue = Integer.parseInt(record[4]);
                    color = new Color(red, green, blue);
                    g2d.setColor(color);
                    sX = Integer.parseInt(record[5]);
                    sY = Integer.parseInt(record[6]);
                    eX = Integer.parseInt(record[7]);
                    eY = Integer.parseInt(record[8]);
                    g2d.drawLine(sX, sY, eX, eY);
                    break;
                case "Rect":
                    thickness = Integer.parseInt(record[1]);
                    g2d.setStroke(new BasicStroke(thickness));
                    red = Integer.parseInt(record[2]);
                    green = Integer.parseInt(record[3]);
                    blue = Integer.parseInt(record[4]);
                    color = new Color(red, green, blue);
                    g2d.setColor(color);
                    sX = Integer.parseInt(record[5]);
                    sY = Integer.parseInt(record[6]);
                    eX = Integer.parseInt(record[7]);
                    eY = Integer.parseInt(record[8]);
                    g2d.drawRect(sX,sY,eX,eY);
                    break;
                case "Circle":
                case "Oval":
                    thickness = Integer.parseInt(record[1]);
                    g2d.setStroke(new BasicStroke(thickness));
                    red = Integer.parseInt(record[2]);
                    green = Integer.parseInt(record[3]);
                    blue = Integer.parseInt(record[4]);
                    color = new Color(red, green, blue);
                    g2d.setColor(color);
                    sX = Integer.parseInt(record[5]);
                    sY = Integer.parseInt(record[6]);
                    eX = Integer.parseInt(record[7]);
                    eY = Integer.parseInt(record[8]);
                    g2d.drawOval(sX,sY,eX,eY);
                    break;
                case "Text":
                    thickness = Integer.parseInt(record[1]);
                    g2d.setStroke(new BasicStroke(thickness));
                    red = Integer.parseInt(record[2]);
                    green = Integer.parseInt(record[3]);
                    blue = Integer.parseInt(record[4]);
                    color = new Color(red, green, blue);
                    g2d.setColor(color);
                    sX = Integer.parseInt(record[5]);
                    sY = Integer.parseInt(record[6]);
                    String text;
                    if (record.length > 7) {
                        text = record[7];
                        g2d.drawString(text, sX,sY);
                    }
                    break;



            }
        }
    }
}
