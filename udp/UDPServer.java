package udp;

import java.awt.Window;
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
	String TOTAL[];

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

				TOTAL = message[0].split(";");
				 
				totalMessages++;
				processMessage(message[0]);
				
				recvSoc.setSoTimeout(5000);
			}
			

		}catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
				for(int i=1 ; i <= Integer.parseInt(TOTAL[0]); i++){
					if(receivedMessages[i]==0){ //print missing messages
						System.out.println("Missing: " + TOTAL[0] 
						+ ";" + Integer.toString(i));
						lostMessages++; //increment lost counter
					}
					else{ //print received messages
						System.out.println("Received: " + TOTAL[0]  
						+ ";" + Integer.toString(i));
					}	
				}
				//print statistics:
				float num = totalMessages;
				float den = totalMessages+lostMessages;
				float p =  (num/den)*100;
				System.out.println("Total messages RECEIVED: " + Integer.toString(totalMessages) );
				System.out.println("Total messages LOST: " + Integer.toString(lostMessages) );
				System.out.println("Transmission RELIABILITY: " + Float.toString(p) + "%");
		
		}

		
	}

	public void processMessage(String data) {
		// TO-DO: Use the data to construct a new MessageInfo object
			
			MessageInfo msg = null;
			try{
				msg = new MessageInfo(data);
				// TO-DO: On receipt of first message, initialise the receive buffer
			}
			catch (Exception e) {
				System.out.println("Error message: " + e.getMessage());
			}
			
				if(totalMessages == 1){
					receivedMessages = new int[msg.totalMessages+1]; //creates buffer size to account for expected messages
				}	
			
			// TO-DO: Log receipt of the message
			receivedMessages[msg.messageNum] = totalMessages;

			// TO-DO: If this is the last expected message, then identify any missing messages
			if( msg.totalMessages == msg.messageNum ){

			
			System.out.println("Last message recieved!");

				for(int i=1 ; i <= msg.totalMessages; i++){

					if(receivedMessages[i]==0){ //print missing messages
						System.out.println("Missing: " + Integer.toString(msg.totalMessages) 
						+ ";" + Integer.toString(i));
						lostMessages++; //increment lost counter
					}
					else{ //print received messages
						System.out.println("Received: " + Integer.toString(msg.totalMessages) 
						+ ";" + Integer.toString(i));
					}
					
				}

				//print statistics:
				
				float num = totalMessages;
				float den = totalMessages+lostMessages;
				float p =  (num/den)*100;
				System.out.println("Total messages RECEIVED: " + Integer.toString(totalMessages) );
				System.out.println("Total messages LOST: " + Integer.toString(lostMessages) );
				System.out.println("Transmission RELIABILITY: " + Float.toString(p) + "%");
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
