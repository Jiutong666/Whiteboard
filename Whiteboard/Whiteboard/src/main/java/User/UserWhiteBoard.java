package User;

/**
 * Name: YUNCONG JI
 * Student id: 1373110
 */

import Manager.ToolListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class UserWhiteBoard {

    private JFrame frame;
    private JButton btnPen;
    private JTextField textField;
    static UserListener userListener;
    static ManagerDrawing draw;
    static TextArea textSent;
    static UserWhiteBoard userwhiteBoard;
    static JList list;
    static DefaultListModel<String> listModel;

    /**
     * Launch the application.
     */


    /**
     * Create the application.
     */
    public UserWhiteBoard(String username) {
        initialize(username);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize(String username) {
        frame = new JFrame();
        frame.setBounds(100, 100, 1039, 604);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        userListener = new UserListener(frame);
        frame.setTitle(username);
        frame.setVisible(true);
        frame.setResizable(false);


        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        frame.setLocation(x, y);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {

                    JoinWhiteBoard.userConnection.out.writeUTF("CloseUser"+","+username);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });


        draw= new ManagerDrawing();
        draw.setBackground(Color.white);
        draw.setBorder(null);
        draw.setBounds(79, 0, 718, 544);

        draw.setList(userListener.saveList());
        frame.getContentPane().add(draw);
        draw.setLayout(null);


        draw.addMouseListener(userListener);
        draw.addMouseMotionListener(userListener);
        userListener.setGr(draw.getGraphics());

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


        JButton btnLine = new JButton("Line");
        btnLine.setActionCommand("Line");
        btnLine.addActionListener(userListener);
        panel_3.add(btnLine);

        JButton btnCircle = new JButton("Circle");
        btnCircle.setActionCommand("Circle");
        btnCircle.addActionListener(userListener);
        panel_3.add(btnCircle);

        JButton btnOval = new JButton("Oval");
        btnOval.setActionCommand("Oval");
        btnOval.addActionListener(userListener);
        panel_3.add(btnOval);

        JButton btnRect = new JButton("Rect");
        btnRect.setActionCommand("Rect");
        btnRect.addActionListener(userListener);
        panel_3.add(btnRect);

        JButton btnText = new JButton("Text");
        btnText.setActionCommand("Text");
        btnText.addActionListener(userListener);
        panel_3.add(btnText);

        JLabel lblColor = new JLabel("Color");
        lblColor.setFont(new Font("Arial", Font.BOLD, 14));
        lblColor.setBounds(10, 254, 46, 21);
        frame.getContentPane().add(lblColor);

        //Color button
        JButton btnColor = new JButton("Color");
        btnColor.setHorizontalAlignment(SwingConstants.LEFT);
        btnColor.setBounds(0, 272, 77, 23);
        btnColor.setActionCommand("Color");
        btnColor.addActionListener(userListener);
        frame.getContentPane().add(btnColor);



        //sent

        textSent = new TextArea();
        textSent.setColumns(10);
        textSent.setBounds(800, 220, 223, 298);
        frame.getContentPane().add(textSent);


        textField = new JTextField();
        textField.setBounds(865, 521, 158, 23);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        JButton btnSent = new JButton("Sent");
        btnSent.setFont(new Font("Arial", Font.BOLD, 11));
        btnSent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = username+": "+textField.getText();
                try {
                    JoinWhiteBoard.userConnection.out.writeUTF("m"+","+message);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                textField.setText(null);
            }
        });
        btnSent.setHorizontalAlignment(SwingConstants.LEFT);
        btnSent.setBounds(800, 521, 66, 23);
        frame.getContentPane().add(btnSent);


        list = new JList();
        list.setBounds(799, 20, 224, 182);
        frame.getContentPane().add(list);
        listModel = new DefaultListModel<>();
        listModel.addElement(username);
        list.setModel(listModel);


    }
    void setFrame(UserWhiteBoard userwhiteBoard){
        this.userwhiteBoard = userwhiteBoard;
    }

}
