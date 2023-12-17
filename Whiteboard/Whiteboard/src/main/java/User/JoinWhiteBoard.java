package User;
/**
 * Name: YUNCONG JI
 * Student id: 1373110
 */
import org.json.JSONObject;

import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;


public class JoinWhiteBoard {

    private JFrame frame;
    private JTextField textField;
    static String ipAddress;
    static Integer userPort;
    static String username;
    UserWhiteBoard userwhiteBoard;



    /**
     * Launch the application.
     */
    static UserConnection userConnection = new UserConnection();

    public static void main(String[] args) {
        if (args.length>=3){
            ipAddress = args[0];
            userPort = Integer.valueOf(args[1]);
            username =args[2];
        }else {
            ipAddress = "localhost";
            userPort = 6666;
            username = "User";
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JoinWhiteBoard window = new JoinWhiteBoard();

                    window.frame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            try {
                                userConnection.out.writeUTF("Close"+","+username);
                                userConnection.out.flush();
                                userConnection.socket.close();

                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            System.exit(0);
                        }
                    });

                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        userConnection.run();

    }

    /**
     * Create the application.
     */
    public JoinWhiteBoard() {
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

        JLabel lblNewLabel = new JLabel("User");
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
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                username = textField.getText();

                if(!(username.isBlank())){
                    try {

                    userConnection.out.writeUTF("Request"+","+username);
                    userConnection.out.flush();

                    while (userConnection.loginState().equals("Default")) {
                            try {
                                Thread.sleep(100); // wait for 100ms
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }

                    String loginRespond = userConnection.loginState();

                    if(loginRespond.equals("Duplicate")){
                       // System.out.println("Username already exists");
                        JOptionPane.showMessageDialog(frame, "Username already exists");
                        userConnection.resetStatus();
                    }
                    else if (loginRespond.equals("Successful")){
                        frame.dispose();
                        userwhiteBoard =new UserWhiteBoard(username);
                        userwhiteBoard.setFrame(userwhiteBoard);

                        userConnection.out.writeUTF("Start"+","+username);
                        userConnection.out.flush();

                    }else if(loginRespond.equals("Reject")){
                        JOptionPane.showMessageDialog(frame, "Manager Reject your login");
                        frame.dispose();

                        userConnection.out.writeUTF("Close"+","+username);
                        userConnection.out.flush();

                        userConnection.socket.close();
                        System.exit(1);
                    }else if(loginRespond.equals("Fail")){
                        JOptionPane.showMessageDialog(null, "Connection failed, pleaser restart it");

                        System.exit(0);
                    }

                    } catch (IOException ex){
                        ex.printStackTrace();
                    }
                }

                else
                    JOptionPane.showMessageDialog(null, "Please enter username！");
            }

        });

        btnNewButton.setBounds(78, 173, 93, 23);
        frame.getContentPane().add(btnNewButton);


        JButton btnNewButton_1 = new JButton("close");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    userConnection.out.writeUTF("Close"+","+username);
                    userConnection.out.flush();
                    userConnection.socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }


                System.exit(0);

            }
        }
        );
        btnNewButton_1.setBounds(230, 173, 93, 23);
        frame.getContentPane().add(btnNewButton_1);


    }



}
