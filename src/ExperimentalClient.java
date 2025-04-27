import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class ExperimentalClient  {
	
	private InetAddress ip;
	private Socket s;
	private DataInputStream in;
	private DataOutputStream out;
	
	public ExperimentalClient(int port) {
		
		try {
			ip = InetAddress.getByName("100.78.254.26");//; //sets ip
			//System.out.println(ip);

			s = new Socket(ip, port); //creates a new socket on the given port

			in = new DataInputStream(s.getInputStream());        //creates new input and output streams
			out = new DataOutputStream(s.getOutputStream());
			
		} catch (Exception e) {
			
		}
	}

	
	public String clientIn() throws IOException {
		String i = in.readUTF();
		
		return i;
	}

	//method to send a message to server
	public void clientOut(String msg) throws IOException {
		out.writeUTF(msg);
	}

	//terminates the client
	public void endClient() throws IOException {
		s.close();
		in.close();
		out.close();
	}

	//calls the class constructor to create a client connection (tests the class)
	public static void main(String[] args) throws IOException{
		ExperimentalClient c = new ExperimentalClient(4444); //uses 4444 as a default port

	}
}