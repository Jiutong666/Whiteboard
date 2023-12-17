package Manager;
/**
 * Name: YUNCONG JI
 * Student id: 1373110
 */



import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class CreateWhiteBoard {

    private JFrame frame;
    private JTextField textField;
    static String ipAddress;
    static Integer serverPort;
    static String username;


    public static List<String> activeUserList = Collections.synchronizedList(new ArrayList<>());
    public static List<ManagerConnection> connectionList = Collections.synchronizedList(new ArrayList<>());

    ServerSocket serverSocket = null;
    public static WhiteBoard whiteBoard;



    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (args.length>=3){
            ipAddress = args[0];
            serverPort = Integer.valueOf(args[1]);
            username =args[2];
        }else {
            ipAddress = "localhost";
            serverPort = 6666;
            username = "Manager";

        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CreateWhiteBoard window = new CreateWhiteBoard();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Create the application.
     */
    public CreateWhiteBoard() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        frame = new JFrame();
        frame.setBounds(100, 100, 420, 290);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        //Centering
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        frame.setLocation(x, y);

        JLabel lblNewLabel = new JLabel("Manager");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Arial", Font.BOLD, 25));
        lblNewLabel.setBounds(80, 30, 253, 73);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblUsername = new JLabel("Username: ");
        lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
        lblUsername.setFont(new Font("Arial", Font.BOLD, 15));
        lblUsername.setBounds(10, 110, 119, 26);
        frame.getContentPane().add(lblUsername);

        textField = new JTextField();
        textField.setBounds(121, 113, 227, 21);
        frame.getContentPane().add(textField);
        textField.setColumns(10);
        textField.setText(username);

        JButton btnNewButton = new JButton("Log in");
        ToolListener toolListener = new ToolListener();
        btnNewButton.addActionListener(toolListener);
        btnNewButton.addActionListener(e ->{
            username = textField.getText();
            if(!(username.isBlank())){
                frame.dispose();
                whiteBoard =new WhiteBoard(username);
                whiteBoard.setFrame(whiteBoard);

            }
            else{
                JOptionPane.showMessageDialog(null, "Please enter username！");
            }
        }

        );
        btnNewButton.setBounds(78, 173, 93, 23);
        frame.getContentPane().add(btnNewButton);


        JButton btnNewButton_1 = new JButton("close");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);

                }
            }
        );
        btnNewButton_1.setBounds(230, 173, 93, 23);
        frame.getContentPane().add(btnNewButton_1);

        //Manager Thread
        Thread serverThread = new Thread(() -> {
            activeUserList.add(username);
            try {
                serverSocket =new ServerSocket(serverPort);
                while (true){
                    Socket acceptUser = serverSocket.accept();
                    ManagerConnection managerConnection= new ManagerConnection(acceptUser);
                    connectionList.add(managerConnection);
                    managerConnection.start();
                }

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Connection failed1");
            }
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        serverThread.start();

    }
}
