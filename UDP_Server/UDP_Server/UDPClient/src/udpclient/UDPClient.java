/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udpclient;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Brian Bluebaugh
 * @version 4/7/16
 */
public class UDPClient extends JFrame{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;
    
    //constructor
    public UDPClient(String host){
        super("Client ");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent event){
                    sendMessage(event.getActionCommand());
                    userText.setText("");
                }
            }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(300, 150);
        setVisible(true);
    }
    //connect to server
    public void startRunning(){
        try{
            connectToServer();
            setupStreams();
            whileChatting();
        }catch(EOFException eofException){
            showMessage("\n Client ended connection");
        }catch(IOException ioException){
            ioException.printStackTrace();
        }finally{
            closeEverything();
        }
    }
    //connect to server
    private void connectToServer() throws IOException{
        showMessage("Attempting connection.... \n");
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("Connected to: " + connection.getInetAddress().getHostName());
    }
    //set up streams for sending/receiving messages
    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Strams are setup \n");
    }
    //while chatting
    private void whileChatting() throws IOException{
        ableToType(true);
        do{
            try{
                message = (String) input.readObject();
                showMessage("\n" + message);
            }catch(ClassNotFoundException classNotFoundException){
                showMessage("\n Unknown Information");
            }
        }while(!message.equals("SERVER - END"));
    }
    //close everything; streams/sockets
    private void closeEverything(){
        showMessage("\n Closing everything");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    //send message
    private void sendMessage(String message){
        try{
            output.writeObject("CLIENT - " + message);
            output.flush();
            showMessage("\nClient - " + message);
        }catch(IOException ioException){
            chatWindow.append("\n Something went wrong");
        }
    }
    //change/update chat window
    private void showMessage(final String m){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    chatWindow.append(m);
                }
            }
        );
    }
    //gives user permission to type tof = true or false
    private void ableToType(final boolean tof){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    userText.setEditable(tof);
                }
            }
        );
    }
    
}
