package User;
/**
 * Name: YUNCONG JI
 * Student id: 1373110
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class UserListener implements ActionListener, MouseListener, MouseMotionListener {

    Graphics2D g2d;
    JFrame frame;
    Integer sX,sY,eX,eY;
    static Color color = Color.black;
    int thickness =3;
    String command = "Line";

    String rgb;
    String shape;
    ArrayList<String> shapeList = new ArrayList<>();
    public UserListener()
    {

    }
    public UserListener(JFrame frame){
        this.frame = frame;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        this.command=e.getActionCommand();
        if (command.equals("Color")){
            Color curColor = JColorChooser.showDialog(frame,"Cloose color",null);
            if (curColor != null){
                color =curColor;
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        sX = e.getX();
        sY = e.getY();
        g2d.setStroke(new BasicStroke(thickness));
        if(!g2d.getColor().equals(color)){
            g2d.setColor(color);
        }

        if (command.equals("Text")) {
            String text=" ";
            text = JOptionPane.showInputDialog(null, "Please enter Text");
            rgb = getColor(color);
            g2d.drawString(text, sX, sY);
            shape = "Text"+"," + this.thickness + "," + rgb + ","+ sX + "," + sY + "," + text;
            shapeList.add(shape);
            try {
                JoinWhiteBoard.userConnection.out.writeUTF("shape"+","+shape);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        eX=e.getX();
        eY=e.getY();
        int length =Math.abs(sX-eX);
        int weight = Math.abs(sY-eY);

        int minX =Math.min(sX,eX);
        int minY= Math.min(sY,eY);

        int centerX = (sX + eX) / 2;
        int centerY = (sY + eY) / 2;
        switch (command) {
            case "Line":
                g2d.drawLine(sX, sY, eX, eY);
                rgb=getColor(color);
                shape = "Line"+","+ this.thickness +","+rgb+","+sX+","+sY+","+eX+","+eY;
                shapeList.add(shape);

                break;
            case "Circle":
                int diameter = (int) Math.sqrt(length * length + weight * weight);
                int radius = diameter / 2;
                int x1= centerX - radius;
                int y1=centerY - radius;
                g2d.drawOval(x1, y1, diameter, diameter);
                rgb=getColor(color);
                shape = "Circle"+","+this.thickness +","+rgb+","+x1+","+y1+","+diameter+","+diameter;
                shapeList.add(shape);

                break;
            case "Oval":
                g2d.drawOval(minX,minY,length,weight);
                rgb=getColor(color);
                shape = "Oval"+","+this.thickness +","+rgb+","+minX+","+minY+","+length+","+weight;
                shapeList.add(shape);

                break;
            case "Rect":
                g2d.drawRect(minX,minY,length,weight);
                rgb=getColor(color);
                shape = "Rect"+","+this.thickness +","+rgb+","+minX+","+minY+","+length+","+weight;
                shapeList.add(shape);
                break;
        }

        try {
             JoinWhiteBoard.userConnection.out.writeUTF("shape"+","+shape);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    @Override
    public void mouseDragged(MouseEvent e) {
        eX=e.getX();
        eY=e.getY();

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    public void setGr(Graphics g){
        this.g2d = (Graphics2D) g;

    }


    @Override
    public void mouseMoved(MouseEvent e) {

    }
    public ArrayList<String> saveList(){
        return shapeList;
    }

    public void updateShape(String str){
        shapeList.add(str);
    }
    public void newShape(){
        shapeList.clear();
    }


    public String getColor(Color color){
        return color.getRed()+","+color.getGreen()+","+color.getBlue();
    }
}