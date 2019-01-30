/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages;
	private boolean close;

	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		try{

			byte[ ] buffer = new byte[1000];
			pacSize = buffer.length;
			pacData = buffer;

			while(close){
				pac = new DatagramPacket(pacData, pacSize);
				recvSoc.receive(pac);

				pacData =  pac.getData();
				String tmp = pacData.toString();


				processMessage(tmp);
				totalMessages++;

				recvSoc.setSoTimeout(30000);
			}
			

		}catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		}

		
	}

	public void processMessage(String data) {

		MessageInfo msg = null;

		// TO-DO: Use the data to construct a new MessageInfo object
		try{
			msg = new MessageInfo(data);
		}
		catch (Exception e) {
			System.out.println("Message Info: " + e.getMessage());
		}
		// TO-DO: On receipt of first message, initialise the receive buffer
		if(totalMessages == 0){
			receivedMessages = new int[1000];
		}
		// TO-DO: Log receipt of the message
		receivedMessages[totalMessages] = Integer.parseInt(msg.toString());

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages

	}


	public UDPServer(int rp) {
		
		try{
		// TO-DO: Initialise UDP socket for receiving data
		recvSoc = new DatagramSocket(rp); // create socket at agreed port
		// Done Initialisation
		System.out.println("UDPServer ready");

		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}
	}


	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer Server = new UDPServer(recvPort);
		Server.run();
	}

}
