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
	private int totalMessages = 0;
	private int[] receivedMessages;
	private boolean close;

	int lostMessages = 0;

	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		try{

			close = false;

			while(!close){

				pacData = new byte[1024];
				pacSize = pacData.length;
				
				pac = new DatagramPacket(pacData, pacSize);
				recvSoc.receive(pac); //receive packet
				
				String tmp = new String( pac.getData());
				String message[] = tmp.split("\\n");  //get rid of new line in string

				totalMessages++;
				processMessage(message[0]);
				

				recvSoc.setSoTimeout(30000);
			}
			

		}catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		}

		
	}

	public void processMessage(String data) {
		// TO-DO: Use the data to construct a new MessageInfo object
		MessageInfo msg = null;
		try{
			msg = new MessageInfo(data);
			// TO-DO: On receipt of first message, initialise the receive buffer
			if(totalMessages == 1){
				receivedMessages = new int[msg.totalMessages+1]; //creates buffer size to account for expected messages
			}
			
			// TO-DO: Log receipt of the message
			receivedMessages[msg.messageNum] = totalMessages;

			// System.out.println("Received: " + msg.toString() );

			// TO-DO: If this is the last expected message, then identify any missing messages
			if( (msg.totalMessages) == msg.messageNum){

				for(int i=1 ; i <= msg.totalMessages ; i++){
					if(receivedMessages[i]==0){ //print missing messages
						System.out.println("Missing: " + Integer.toString(msg.totalMessages) 
						+ ";" + Integer.toString(i));
						lostMessages++; //increment lost counter
					}
					else{ //print received messages
						System.out.println("Received: " + msg.toString() );
					}
					
				}

				//print statistics:
				System.out.println("Total messages RECEIVED: " + Integer.toString(totalMessages) );
				System.out.println("Total messages LOST: " + Integer.toString(lostMessages) );
			}
		}
		catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

		
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
