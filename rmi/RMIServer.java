package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = 0;
	private int[] receivedMessages;
	int lostMessages = 0;

	public RMIServer() throws RemoteException {
		super(Registry.REGISTRY_PORT);
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {
		totalMessages++;
		
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


	public static void main(String[] args) {

		RMIServer rmis = null;

			// TO-DO: Initialise Security Manager
			if (System.getSecurityManager() == null){

				System.setSecurityManager(new SecurityManager());
			}
			try{
				System.setProperty("java.rmi.server.hostname", "192.168.1.139");
				
				
				String urlServer = "rmi://" + "192.168.1.139" + "/RMIServer";
				// TO-DO: Instantiate the server class
				rmis = new RMIServer();

				// TO-DO: Bind to RMI registry
				rebindServer(urlServer, rmis);
			}
			catch(Exception e){
				System.out.println("Trouble: " + e);
			}
		

	}

	protected static void rebindServer(String serverURL, RMIServer server) {
		
		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
		try{
			// RMIServerI stub = (RMIServerI)UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

			// TO-DO:
			// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
			// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
			// expects different things from the URL field.
			registry.rebind(serverURL, server);
			System.out.println("RMIServer bound");
		}
		catch (Exception e){
			System.out.println("Exception: " + e);
		}
	}
}
