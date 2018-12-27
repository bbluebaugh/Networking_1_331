/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udpserver;
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
public class UDPServer extends JFrame{

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    
    //constructor
    public UDPServer(){
        super("Brian's Instant Messenger");
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
        add(new JScrollPane(chatWindow));
        setSize(300,150);
        setVisible(true);
    }
    
    //set up and run the server
    public void startRunning(){
        try{
            server = new ServerSocket(6789, 100);
            while(true){
                try{
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                    
                }catch(EOFException eofException){
                    showMessage("\n Server ended connection ");
                }finally{
                    closeEverything();
                }
            }
            
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    //wait for connection, then show connection info
    private void waitForConnection() throws IOException{
        showMessage("Waiting for someone to connect... \n");
        connection = server.accept();
        showMessage("Now connected to " + connection.getInetAddress().getHostName());
    }
    //get stream to send/receive data
    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Strams are now setup \n");
    }
    //during the chat 
    private void whileChatting() throws IOException{
        String message = "You are now connected ";
        sendMessage(message);
        ableToType(true);
        do{
            try{
                message = (String) input.readObject();
                showMessage("\n" + message);
            }catch(ClassNotFoundException classNotFoundException){
                showMessage("\n Information Unknown");
            }
        }while(!message.equals("Client - END"));//have conversation until a user ends
    }
    //close streams and sockets when finished
    private void closeEverything(){
        showMessage("\n Closing connection... \n");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    //send a message to the client
    private void sendMessage(String message){
        try{
            output.writeObject("Server - " + message);
            output.flush();
            showMessage("\n Server - " + message);
        }catch(IOException ioException){
            chatWindow.append("\n Error: Cannot Send Message");
        }
    }
    //updates chatWindow
    private void showMessage(final String text){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    chatWindow.append(text);                        
                }
            }
        );
    }
    //alow user to type
    //tof = true or false
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
