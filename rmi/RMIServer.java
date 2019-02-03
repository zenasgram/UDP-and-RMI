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

	private int totalMessages = -1;
	private int[] receivedMessages;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {
		totalMessages++;
		// TO-DO: On receipt of first message, initialise the receive buffer
		if(totalMessages == 0){
			receivedMessages = new int[2000];
		}
		// TO-DO: Log receipt of the message
		receivedMessages[totalMessages] = msg.messageNum;
		System.out.println("Received: " + msg.toString() );

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if(totalMessages==msg.totalMessages-1){
			for(int i=0 ; i < totalMessages ; i++){
				if(receivedMessages[i]!=i){ //message missing
					System.out.println("Missing: " + Integer.toString(msg.totalMessages) 
					+ ";" + Integer.toString(i));
				}
			}
		}

	}


	public static void main(String[] args) {

		RMIServer rmis = null;

			// TO-DO: Initialise Security Manager
			if (System.getSecurityManager() == null){

				System.setSecurityManager(new SecurityManager());
			}
			try{
				String serverURL = "rmi://localhost/RMIServer";
				// TO-DO: Instantiate the server class
				rmis = new RMIServer();
				
				// TO-DO: Bind to RMI registry
				rebindServer(serverURL, rmis);
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
			//RMIServerI stub = (RMIServerI)UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.createRegistry(33333);

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
