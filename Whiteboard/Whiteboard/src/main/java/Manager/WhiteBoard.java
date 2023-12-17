package Manager;
import Manager.ToolListener;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
/**
 * Name: YUNCONG JI
 * Student id: 1373110
 */
public class WhiteBoard {

    private JFrame frame;
    private JButton btnPen;
    private JTextField textField;
    private JTextField textField_1;
    static ToolListener toolListener;
    static TextArea textSent;
    JList list;
    static ManagerDrawing draw;
    static DefaultListModel<String> listModel;

    static WhiteBoard whiteBoard;

    /**
     * Launch the application.
     */


    /**
     * Create the application.
     */
    public WhiteBoard(String username) {
        initialize(username);
    }

    public int Request(String username){
        int request = JOptionPane.showConfirmDialog(null,username+ " want to join Whiteboard","Confirm",JOptionPane.INFORMATION_MESSAGE);
        return request;
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize(String username) {

        frame = new JFrame();
        frame.setBounds(100, 100, 1039, 604);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        toolListener = new ToolListener(frame);
        frame.getContentPane().setLayout(null);
        frame.setTitle(username);
        frame.setVisible(true);
        frame.setResizable(false);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        frame.setLocation(x, y);

        draw = new ManagerDrawing();
        draw.setBackground(Color.white);
        draw.setBorder(null);
        draw.setBounds(79, 0, 718, 544);

        frame.getContentPane().add(draw);
        draw.setLayout(null);


        draw.setList(toolListener.saveList());
        draw.addMouseListener(toolListener);
        draw.addMouseMotionListener(toolListener);
        toolListener.setGr(draw.getGraphics());


        JLabel lblNewLabel = new JLabel("Client List");
        lblNewLabel.setFont(new Font("Arial", Font.BOLD, 14));
        lblNewLabel.setBounds(800, 0, 77, 21);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblChat = new JLabel("Chat");
        lblChat.setFont(new Font("Arial", Font.BOLD, 14));
        lblChat.setBounds(800, 202, 77, 21);
        frame.getContentPane().add(lblChat);

        JLabel lblNewLabel_1 = new JLabel("Tool");
        lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 14));
        lblNewLabel_1.setBounds(23, 0, 46, 21);
        frame.getContentPane().add(lblNewLabel_1);

        JPanel panel_3 = new JPanel();
        panel_3.setBackground(new Color(255, 255, 255));
        panel_3.setBounds(0, 20, 77, 203);
        frame.getContentPane().add(panel_3);

        /**
         * Tool
         * **/

        //menu
        JComboBox menu= new JComboBox();
        menu.setModel(new DefaultComboBoxModel(new String[]{"New","Save","Save as","Exit"}));
        menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String m = menu.getSelectedItem().toString();
                switch (m){
                    case "New":
                        draw.removeAll();
                        draw.updateUI();
                        toolListener.newShape();
                        try {
                            for (int i = 0; i < CreateWhiteBoard.connectionList.size(); i++) {
                                ManagerConnection st = CreateWhiteBoard.connectionList.get(i);
                                st.out.writeUTF("new"+","+ "new");
                                st.out.flush();
                            }
                        } catch (IOException ex) {

                        }
                        break;

                    case "Save":

                        BufferedImage image = new BufferedImage(draw.getWidth(), draw.getHeight(), BufferedImage.TYPE_INT_RGB);
                        Graphics2D graphics2D = image.createGraphics();
                        draw.paint(graphics2D);
                        try {
                            ImageIO.write(image, "png", new File("C:\\Users\\27908\\Desktop\\demo\\draw.png"));

                        } catch (IOException ex) {
                            System.out.println("save fail");
                        }

                        break;

                    case "Save as":
                        BufferedImage image2 = new BufferedImage(draw.getWidth(), draw.getHeight(), BufferedImage.TYPE_INT_RGB);
                        Graphics2D graphics2D2 = image2.createGraphics();
                        draw.paint(graphics2D2);
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle("Specify a file to save");
                        int userSelection = fileChooser.showSaveDialog(null);
                        if (userSelection == JFileChooser.APPROVE_OPTION) {
                            File fileSaveAs = fileChooser.getSelectedFile();
                            try {
                                ImageIO.write(image2, "png", fileSaveAs);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        break;


                    case "Exit":
                        System.exit(1);
                        break;

                }


            }
        });
        panel_3.add(menu);

        //LINE
        JButton btnLine = new JButton("Line");
        btnLine.setActionCommand("Line");
        btnLine.addActionListener(toolListener);
        panel_3.add(btnLine);


        JButton btnCircle = new JButton("Circle");
        btnCircle.setActionCommand("Circle");
        btnCircle.addActionListener(toolListener);
        panel_3.add(btnCircle);


        JButton btnOval = new JButton("Oval");
        btnOval.setActionCommand("Oval");
        btnOval.addActionListener(toolListener);
        panel_3.add(btnOval);

        JButton btnRect = new JButton("Rect");
        btnRect.setActionCommand("Rect");
        btnRect.addActionListener(toolListener);
        panel_3.add(btnRect);

        JButton btnText = new JButton("Text");
        btnText.setActionCommand("Text");
        btnText.addActionListener(toolListener);
        panel_3.add(btnText);

        JLabel lblColor = new JLabel("Color");
        lblColor.setFont(new Font("Arial", Font.BOLD, 14));
        lblColor.setBounds(10, 254, 46, 21);
        frame.getContentPane().add(lblColor);

        //Color button
        JButton btnColor= new JButton("Color");
        btnColor.setHorizontalAlignment(SwingConstants.LEFT);
        btnColor.setBounds(0, 272, 77, 23);
        btnColor.setActionCommand("Color");
        btnColor.addActionListener(toolListener);
        frame.getContentPane().add(btnColor);


        //sent
        textField = new JTextField();
        textField.setBounds(865, 521, 158, 23);
        frame.getContentPane().add(textField);
        textField.setColumns(10);


        textSent = new TextArea();
        textSent.setColumns(10);
        textSent.setBounds(800, 220, 223, 298);
        frame.getContentPane().add(textSent);


        JButton btnSent = new JButton("Sent");
        btnSent.setFont(new Font("Arial", Font.BOLD, 11));
        btnSent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = username+": "+textField.getText();
                textSent.append(message+ "\n");

                for (int i = 0; i < CreateWhiteBoard.connectionList.size(); i++) {
                    ManagerConnection managerConnection = CreateWhiteBoard.connectionList.get(i);
                    try {
                        managerConnection.out.writeUTF("m"+","+message);
                        managerConnection.out.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                textField.setText(null);

            }
        });
        btnSent.setHorizontalAlignment(SwingConstants.LEFT);
        btnSent.setBounds(800, 521, 66, 23);
        frame.getContentPane().add(btnSent);

        // kick out
        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(887, 176, 136, 23);
        frame.getContentPane().add(textField_1);

        // user List
        list = new JList();
        list.setBounds(799, 20, 224, 155);
        frame.getContentPane().add(list);
        listModel = new DefaultListModel<>();
        listModel.addElement(username);
        list.setModel(listModel);

        JButton btnKickOut = new JButton("kick out");
        btnKickOut.setHorizontalAlignment(SwingConstants.LEFT);
        btnKickOut.setFont(new Font("Arial", Font.BOLD, 11));
        btnKickOut.setBounds(800, 176, 87, 23);
        btnKickOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String kickName = textField_1.getText();
                if(!kickName.equals(username)){
                    if (CreateWhiteBoard.activeUserList.contains(kickName)){
                        try {
                            for (int i = 0; i < CreateWhiteBoard.connectionList.size(); i++) {
                                ManagerConnection st = CreateWhiteBoard.connectionList.get(i);
                                st.out.writeUTF("kick"+","+ kickName);
                                st.out.flush();

                                CreateWhiteBoard.activeUserList.remove(kickName);
                                DefaultListModel<String> listModel = CreateWhiteBoard.whiteBoard.listModel;
                                listModel.removeElement(kickName);


                                String list = String.join(",", CreateWhiteBoard.activeUserList);
                                list = "list," + list;
                                for (ManagerConnection st1 : CreateWhiteBoard.connectionList) {
                                        st1.out.writeUTF(list);
                                        st1.out.flush();
                                }

                            }
                        } catch (IOException ex) {

                        }

                    }

                }


            }
        });
        frame.getContentPane().add(btnKickOut);






    }



    void setFrame(WhiteBoard whiteBoard){
        this.whiteBoard = whiteBoard;
    }

}
