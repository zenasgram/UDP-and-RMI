package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);
		try{
				// TO-DO: Initialise Security Manager
				if (System.getSecurityManager()==null){
					System.setSecurityManager(new SecurityManager());
				}
			// TO-DO: Bind to RMIServer
				try{
					
					
					Registry registry = LocateRegistry.getRegistry(args[0],Registry.REGISTRY_PORT);
					RMIServerI iRMIServer = (RMIServerI) registry.lookup(urlServer);
			
				// TO-DO: Attempt to send messages the specified number of times		
					int	tries = 0;

					MessageInfo message = null;

					// TO-DO: Send the messages to the server
					while(tries<numMessages){
					
						tries++; 

						message = new MessageInfo(numMessages,tries);
						
						iRMIServer.receiveMessage(message); //send to server
						System.out.println("Sent: " + message.toString() );
						
					}
				}
				catch(Exception e){
					System.out.println("Exception message: " + e);
				}
		
		
		}
		catch (Exception e){
			System.out.println("Security Manager Exception: " + e);
		}
	}
}
