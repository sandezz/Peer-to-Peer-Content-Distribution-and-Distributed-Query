/*
Author: Sandesh Bhetwal
Student ID: 12096531
Student Email: sandesh.bhetwal@cqumail.com
Purpose: It is the service used by a peer to multicast the question to the group of peers using multicastsocket.

 */

//import required libraries
import java.net.*;
import java.io.*;

//delcare class SendQuestion that can run as a thread
public class SendQuestion extends Thread {

    //declare class variables
    MulticastSocket ms = null; //for multicast socket
    InetAddress group = null; //for Ip InetAddress
    int port = 8888; //initialing port number to 8888
    String myid; //for storing the peerid
    String message = null; //for storing the question being sent

    public SendQuestion(MulticastSocket ms, InetAddress group, int port, String myid, String msg) {

        //assign provided values to class variable
        this.ms = ms;
        this.group = group;
        this.port = port;
        this.myid = myid;
        this.message = msg;

        //start the thread
        this.start();
    }

    //this function is executed when a thread of SendQuestion service is run
    public void run() {

        //this try block attaches the peer id of the peer and multicast it to the group of peers
        try {

            //format the question by adding peer id to it
            message = new String(myid + ": " + message);

            //convert the question to bytes before sending it through datagram packet
            byte[] m = message.getBytes();

            //attach the question to the datagram packet and group ip and port to multicast the question
            DatagramPacket questionOut = new DatagramPacket(m, m.length, group, port);

            //multicast the datagram packet with question to the groups of peer.
            ms.send(questionOut);

        } //different Exception Handling done here
        catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }

    }
}//end of class
