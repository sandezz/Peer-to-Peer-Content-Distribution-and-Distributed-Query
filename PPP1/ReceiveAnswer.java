/*
Author: Sandesh Bhetwal
Student ID: 12096531
Student Email: sandesh.bhetwal@cqumail.com
Purpose: It is the service used by a peer to recieve the answer from other peers via datagram socket
		It checks if the answer is form the other peers and dispaly it in the Answer Board in Main UI.

 */

//import java required libraries
import java.net.*;
import java.io.*;
import java.util.*;
import static java.lang.Integer.parseInt;

//declare a class ReceiveAnswer that can be run as a thread
public class ReceiveAnswer extends Thread {

    //declaring variable for the class
    DatagramSocket ds = null; //for datagramsocket information
    String myid = null; //for storing the peer id

    //constructor for ReceiveAnswer
    public ReceiveAnswer(DatagramSocket ds, String myid) {

        //assign provided value to class variables
        this.ds = ds;
        this.myid = myid;

        //start the thread
        this.start();
    }

    //this function is executed when the thread of ReceiveAnswer service thread is started
    public void run() {

        //while loop implemented here to hear for answer until the whole program is terminated
        while (true) {

            //buffer reader is declared to store the answer from the datagram packet
            BufferedReader reader = null;

            //this try block is used to get the answer and display it to the answer board
            try {
                //declare buffer size to store the incoming answer
                byte[] buffer = new byte[50];

                //creating a datapacket answerIn to receive the answer from datagram socket
                DatagramPacket answerIn = new DatagramPacket(buffer, buffer.length);

                //receiving the answer from datagram socket
                ds.receive(answerIn);

                //Saving the datagrampacket message to a string
                String msg = new String(answerIn.getData(), 0, answerIn.getLength());

                //if the message is not from self and the message is answer to the question only then display it in the answer board
                if (!msg.contains(myid) && msg.contains("Answer")) {
                    MainUI.jTextArea1.append(String.valueOf(msg) + "\n");
                } else {
                }

            } //Exception Handling done here
            catch (SocketException e) {
                System.out.println("Socket: " + e.getMessage());
            } catch (UnknownHostException e) {
                System.out.println("Socket:" + e.getMessage());
            } catch (EOFException e) {
                System.out.println("EOF:" + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO: " + e.getMessage());
            }

        }
    }
}//end of class
