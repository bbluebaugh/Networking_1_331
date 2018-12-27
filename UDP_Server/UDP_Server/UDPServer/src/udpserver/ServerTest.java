/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udpserver;
import javax.swing.JFrame;
/**
 *
 * @author Brian Bluebaugh
 */
public class ServerTest {
    public static void main(String[] args){
        UDPServer alice = new UDPServer();
        alice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        alice.startRunning();
    }
}
