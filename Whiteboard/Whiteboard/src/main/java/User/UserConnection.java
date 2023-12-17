package User;
/**
 * Name: YUNCONG JI
 * Student id: 1373110
 */

import Manager.CreateWhiteBoard;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class UserConnection {


    Socket socket =null;
    public DataInputStream in = null;
    public DataOutputStream out = null;
    String loginState;

    ArrayList<String> usernames = new ArrayList<>();

    public void run() {
        try {
            resetStatus();
            socket = new Socket(JoinWhiteBoard.ipAddress, JoinWhiteBoard.userPort);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            while (true) {
                String request = in.readUTF();

                String[] split = request.split(",", 2);
                switch (split[0]) {
                    case "d":
                        loginState = "Duplicate";
                        break;
                    case "s":
                        loginState = "Successful";
                        break;
                    case "f":
                        loginState = "Fail";
                        break;
                    case "r":
                        loginState = "Reject";
                        break;
                    case "shape":
                        UserWhiteBoard.userListener.updateShape(split[1]);
                        UserWhiteBoard.draw.repaint();
                        break;
                    case "m":
                        String message = split[1];
                        UserWhiteBoard.textSent.append(message+"\n");
                        break;
                    case "new":

                        UserWhiteBoard.draw.removeAll();
                        UserWhiteBoard.draw.updateUI();
                        UserWhiteBoard.userListener.newShape();
                        break;
                    case "list":
                        UserWhiteBoard.list.setListData(split[1].split(","));
                        break;
                    case "kick":

                        String kickName =split[1];
                        if (kickName.equals(JoinWhiteBoard.username)){
                            System.exit(0);

                        }
                        JOptionPane.showMessageDialog(null, kickName+" has been kicked out");
                        break;

                }

            }


        }  catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Connection failed");
            System.exit(0);

        }

    }



    String loginState() {
        return loginState;
    }

    public void resetStatus(){
        loginState="Default";
        return;
    }


}
