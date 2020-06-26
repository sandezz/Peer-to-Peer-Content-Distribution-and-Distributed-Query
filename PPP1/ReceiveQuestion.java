/*
Author: Sandesh Bhetwal
Student ID: 12096531
Student Email: sandesh.bhetwal@cqumail.com
Purpose: It is the service used by a peer to recieve the questions from other peers in the multicast group
		It checks if the peer has the answer for the asked question or not and if the peer knows the answer
		it sends the answer to the requesting peer via a datagram packet.

 */

//import required libraries
import java.net.*;
import java.io.*;
import java.util.*;
import static java.lang.Integer.parseInt;

//declare a class ReceiveQuestion that can run as a thread
public class ReceiveQuestion extends Thread {

    //declaring required variables for the class
    MulticastSocket ms = null; //for multicastsocket
    DatagramSocket ds = null; //for datagramsocket
    String myid = null; //for the id of the peer

    //constructor for class ReceiveQuestion
    public ReceiveQuestion(MulticastSocket ms, String myid, DatagramSocket ds) {

        //assign provided value to class variables
        this.ms = ms;
        this.myid = myid;
        this.ds = ds;

        //start the thread
        this.start();
    }

    //this function is executed when the thread of ReceiveQuestion service is started
    public void run() {

        //while lopp is implemented here to hear for question until the whole program is terminated
        while (true) {

          //Buffer Reader is declared to store data from received datagram
            BufferedReader reader = null;

            //this try block is used to get the question and check if we can answer it, if we have the answer we send it to the required peer only
            try {

                //declare size of buffer reader in bytes
                byte[] buffer = new byte[50];

                // create a datagram packet questionIn to receive the question from the multicast
                DatagramPacket questionIn = new DatagramPacket(buffer, buffer.length);

                //receive tqueestion from multicast group and add to questionIn datagram packet
                ms.receive(questionIn);

                //arrray list of question and answer that the peer can answer
                //each peer have different question and answer array
                String[] queList = new String[]{"1", "6", "4", "2"};
                String[] ansList = new String[]{"1", "6-1", "4-1", "2"};

                //extract the question from the message received to string for better manipulation of question
                String msg = new String(questionIn.getData(), 0, questionIn.getLength());

                //Checking wheather the message is not from ourself, the questions are only processed if it from other peers
                if (!msg.contains(myid)) {
                    //if the question is from other peer

                    String[] strPart = msg.split(" "); //splitting the message by spaces in them for better processing of question

                    //Now to check if the quesion asked is with our question array
                    //So we check the question with our question list
                    for (int i = 0; i < queList.length; i++) {

                       // if the asked question is found in question list send the answer to the peer
                        if (queList[i].contains(strPart[2])) {

                           //Create an answer for sending to the peer by accessing the answer array
                            String answer = "Answer " + ansList[i] + " (For " + strPart[1] + " " + strPart[2] + ")\n";

                            //start the sending service threads
                            try {

                                //modify answer to show the peer id sending the answer
                                String message = new String(myid + ": " + answer);

                                //convert the message into bytes to send it through datagram packet
                                byte[] m = message.getBytes();

                                //create a datagram packet with answers in byte and with ip address and port for peer location of the one asking question
                                DatagramPacket answerOut = new DatagramPacket(m, m.length, questionIn.getAddress(), parseInt(strPart[3]));

                               //send the answer through datagram socket
                                ds.send(answerOut);

                            } // different Exception Handling done here
                            catch (SocketException e) {
                                System.out.println("Socket: " + e.getMessage());
                            } catch (IOException e) {
                                System.out.println("IO: " + e.getMessage());
                            }

                            //if the answer is found stop the search for question
                            break;

                        }

                    }
                }

            } //different Exception Handling done here
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
}
//end of class
