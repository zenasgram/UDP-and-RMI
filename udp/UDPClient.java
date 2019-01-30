package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import common.MessageInfo;

public class UDPClient {

	private DatagramSocket sendSoc;

	public static void main(String[] args) { // creates socket on any available port no.
		InetAddress	serverAddr = null;
		int			recvPort;
		int 		countTo;
		String 		message;

		// Get the parameters
		if (args.length < 3) {
			System.err.println("Arguments required: server name/IP, recv port, message count");
			System.exit(-1);
		}

		try {
			serverAddr = InetAddress.getByName(args[0]); //convert name to IP addr.
		} catch (UnknownHostException e) {
			System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[1]);
		countTo = Integer.parseInt(args[2]);


		UDPClient Client = new UDPClient(); //initialise Socket

		byte[] tmp = args[0].getBytes();
		message = tmp.toString();
		System.out.println("Message: " + message);
		
		Client.testLoop(message, serverAddr, recvPort, countTo); //test sending

	}

	public UDPClient() {
		// TO-DO: Initialise the UDP socket for sending data

			sendSoc = null;
			try { 
				
				sendSoc = new DatagramSocket();  	

			}
			catch (SocketException e){
				System.out.println("Socket: " + e.getMessage());
			}
	}

	private void testLoop(String message, InetAddress serverAddr, int recvPort, int countTo) {
		int				tries = 0;

		// TO-DO: Send the messages to the server
		for(int i=0; i<countTo; i++){

			send(message, serverAddr , recvPort); //pass to send function

			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			
			try { 
				sendSoc.receive(reply);
				}
			catch(IOException e){
					System.out.println("IO: " + e.getMessage());
				}

			String check = new String(reply.getData());
			if (check == message){
				break;
			}
			tries++;
		}
		System.out.println("Tries: " + tries);
	}

	private void send(String payload, InetAddress destAddr, int destPort) {
		int				payloadSize;
		byte[]				pktData;
		DatagramPacket		pkt;

		// TO-DO: build the datagram packet and send it to the server
		pktData = payload.getBytes();
		payloadSize = payload.length();
		
		pkt = new DatagramPacket(pktData,  payloadSize, destAddr, destPort);

		try { 
		sendSoc.send(pkt);
		}
		catch(IOException e){
			System.out.println("IO: " + e.getMessage());
		}
	}
}
