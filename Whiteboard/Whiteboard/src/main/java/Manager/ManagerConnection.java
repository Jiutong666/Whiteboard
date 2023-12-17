package Manager;
/**
 * Name: YUNCONG JI
 * Student id: 1373110
 */

import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ManagerConnection extends Thread {

    public DataInputStream in;
    public DataOutputStream out;
    public Socket socket;
    String list;

    public ManagerConnection(Socket acceptUser) {
        this.socket = acceptUser;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized int login(String username){

        int request = 9;
        try {
            request = CreateWhiteBoard.whiteBoard.Request(username);
        } catch (NullPointerException e) {
        }
        return request;

    }

    public static void sendNewUser(ArrayList<String> shapeList) throws IOException {
        for (String picture : shapeList){
            for (int i = 0; i < CreateWhiteBoard.connectionList.size(); i++) {
                ManagerConnection managerConnection = CreateWhiteBoard.connectionList.get(i);
                managerConnection.out.writeUTF("shape"+","+picture);
                managerConnection.out.flush();
            }
        }
    }

    @Override
    public void run() {

       // System.out.println("Thread Name: " + Thread.currentThread().getName());

        try {
            String respond;

            while (true){
                respond=in.readUTF();
                String [] split = respond.split(",",2);
                switch (split[0]){

                    case "Request":
                        String username = split[1];
                        if (CreateWhiteBoard.activeUserList.contains(username)){
                            out.writeUTF("d"+","+"Duplicate");
                            out.flush();

                        }else {
                            int option = login(username);
                            if (option == JOptionPane.YES_OPTION){
                                if (CreateWhiteBoard.activeUserList.contains(username)){
                                    out.writeUTF("d"+","+"Duplicate");
                                    out.flush(); //Double-checked locking
                                }else {
                                    CreateWhiteBoard.activeUserList.add(username);
                                    DefaultListModel<String> listModel = CreateWhiteBoard.whiteBoard.listModel;
                                    listModel.addElement(username);

                                    out.writeUTF("s"+","+"Successful");
                                    out.flush();
                                }
                            }else if (option ==9){
                                out.writeUTF("f"+","+"Fail");
                                out.flush();
                                CreateWhiteBoard.connectionList.remove(this);
                            }else {
                                out.writeUTF("r"+","+"Reject");
                                out.flush();
                                CreateWhiteBoard.connectionList.remove(this);
                            }

                        }
                        break;
                    case "Start":
                        ArrayList<String> shape = WhiteBoard.toolListener.saveList();
                        sendNewUser(shape);

                        list = String.join(",", CreateWhiteBoard.activeUserList);
                        list = "list," + list;

                        for (ManagerConnection st : CreateWhiteBoard.connectionList) {
                            try {
                                st.out.writeUTF(list);
                                st.out.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        break ;
                    case "shape":
                        for (int i = 0; i < CreateWhiteBoard.connectionList.size(); i++) {
                            ManagerConnection st = CreateWhiteBoard.connectionList.get(i);
                            st.out.writeUTF(respond);
                            st.out.flush();
                        }
                        WhiteBoard.toolListener.updateShape(split[1]);
                        WhiteBoard.draw.repaint();

                        break ;
                    case "m":
                        String message = split[1];
                        for (int i = 0; i < CreateWhiteBoard.connectionList.size(); i++) {
                            ManagerConnection st = CreateWhiteBoard.connectionList.get(i);
                            st.out.writeUTF("m"+","+ message);
                            st.out.flush();
                        }
                        WhiteBoard.textSent.append(message+"\n");
                        break;

                    case "CloseUser":
                        String closeUsername = split[1];

                        CreateWhiteBoard.activeUserList.remove(closeUsername);
                        DefaultListModel<String> listModel = CreateWhiteBoard.whiteBoard.listModel;
                        listModel.removeElement(closeUsername);

                        list = String.join(",", CreateWhiteBoard.activeUserList);
                        list = "list," + list;
                        for (ManagerConnection st1 : CreateWhiteBoard.connectionList) {
                            st1.out.writeUTF(list);
                            st1.out.flush();
                        }

                        socket.close();
                        break;

                    case "Close":
                        socket.close();
                        break;

                }

            }

        } catch (SocketException e) {
            CreateWhiteBoard.connectionList.remove(this);



        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                in.close();
                out.close();;
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }



}