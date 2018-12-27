/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udpclient;
import javax.swing.JFrame;
/**
 *
 * @author bbluebaugh
 */
public class ClientTest {
    public static void main(String[] args){
        UDPClient bob;
        bob = new UDPClient("127.0.0.1");
        bob.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bob.startRunning();
    }
}
