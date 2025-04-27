import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Server extends JFrame implements ActionListener {
	//private instance variables
	private static ArrayList<ClientHandler> al = new ArrayList<ClientHandler>();  
	private static boolean gameStarted = false;

	private JButton btnStart, btnEnd;           
	private JTextField txtPortNum;
	private static JTextArea txaFeed;
	private JScrollPane jscp;
	private JLabel lbl;

	
	public Server() {
		super("Server");                 
		setSize(400, 300);
		setLayout(null);

		
		btnStart = new JButton("Start");
		btnEnd = new JButton("End");
		lbl = new JLabel("Enter 4-digit port #:");
		txtPortNum = new JTextField();

		txaFeed = new JTextArea();      
		txaFeed.setEditable(false);

		jscp = new JScrollPane(txaFeed);     

		btnStart.setBounds(20,20,100,30);  
		add(btnStart);
		btnStart.addActionListener(this);

		btnEnd.setBounds(20,60,100,30);   
		add(btnEnd);
		btnEnd.addActionListener(this);

		lbl.setBounds(140,30,200,30);     
		add(lbl);

		txtPortNum.setBounds(140,60,100,30);  
		add(txtPortNum);
		txtPortNum.setToolTipText("Inserisci una porta compresa tra 1024 e 9999");

		jscp.setBounds(20,100,360,150);   
		add(jscp);

		setDefaultCloseOperation(EXIT_ON_CLOSE);  
		setResizable(false);
		setVisible(true);


	}
	public Server(int port) {
		try {
			ServerSocket sSocket = new ServerSocket(port); 

			while (al.size() < 2) { 
				Socket s = null;

				try {
					s = sSocket.accept();  

					DataInputStream in = new DataInputStream(s.getInputStream());
					DataOutputStream out = new DataOutputStream(s.getOutputStream());

					
					ClientHandler t = new ClientHandler(s, in, out);
					t.start();
					al.add(t);

				} catch (Exception e) {
					s.close();
				}
			}
		}
		catch (IOException e) {

		}
		catch (Exception e) {

		}
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == btnStart) {
			
			int port = 0;
			try {
				port = Integer.parseInt(txtPortNum.getText());
			}
			catch (NumberFormatException e) {
				addFeed("Inserisci una porta compresa tra 1024 e 9999");  
			}
			if (port <= 9999 && port >= 1024) {  
				btnStart.setEnabled(false);         
				btnEnd.setEnabled(true);
				try {
					ServerSocket sSocket = new ServerSocket(Integer.parseInt(txtPortNum.getText())); //creates new socket with port

					while (al.size() < 2) { //checks if less than two clients have connected
						Socket s = null;

						try {
							s = sSocket.accept();  //connects client to server
							addFeed("Client " + (al.size() + 1) + "/2 joined");

							DataInputStream in = new DataInputStream(s.getInputStream());
							DataOutputStream out = new DataOutputStream(s.getOutputStream());

							ClientHandler t = new ClientHandler(s, in, out);
							t.start();
							al.add(t);

						} catch (Exception e) {
							s.close();
						}
					}
				}
				catch (IOException e) {

				}
			}

			else {
				addFeed("Porta no valida");
			}

		}
		else if (evt.getSource() == btnEnd) { 
			Server.closeServer();
		}

	}

	
	public static void main(String[] args) throws IOException{
		Server s = new Server();
	}

	
	public static void broadcastMsg(String msg) throws IOException {
		for (int i = 0; i < al.size(); i++) {  
			ClientHandler ch = al.get(i);
			ch.out(msg);
		}
	}

	
	public static void clientOneOut (String msg) throws IOException {
		ClientHandler ch = al.get(0);
		ch.out(msg);
	}


	public static void clientTwoOut (String msg) throws IOException {
		ClientHandler ch = al.get(1);
		ch.out(msg);
	}

	
	public static String clientOneIn () throws IOException {
		ClientHandler ch = al.get(0);
		return ch.in();
	}

	
	public static String clientTwoIn () throws IOException {
		ClientHandler ch = al.get(1);
		return ch.in();
	}

	
	public static void addFeed(String in) {
		txaFeed.setText(txaFeed.getText() + "\n" + in);
	}

	
	public static void closeServer() {
		addFeed("CLOSING SERVER");

		try {
			Thread.sleep(1000);
		}
		catch (Exception e){

		}

		System.exit(0);
	}

}

class ClientHandler extends Thread {
	
	private final DataInputStream in;
	private final DataOutputStream out;
	private final Socket s;

	
	public ClientHandler(Socket s, DataInputStream in, DataOutputStream out) {
		this.s = s;
		this.in = in;
		this.out = out;
	}

	
	public void out(String msg) throws IOException{
		out.writeUTF(msg);
	}

	
	public String in() throws IOException {
		return in.readUTF();
	}

	
	public void run() {
		try {
			
			Server.clientOneOut("Giocatore 1");
			
			Server.clientTwoOut("Giocatore 2");
			
			Server.broadcastMsg("Collegato");

			Server.clientTwoIn();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while (true) {
			try {
				
				Server.clientOneOut("Turn");
				Server.clientTwoOut("Wait");

				String received = Server.clientOneIn();
				if (received.equals("clientoneshot")) {
					String row = Server.clientOneIn();
					String col = Server.clientOneIn();

					Server.clientTwoOut(row);
					Server.clientTwoOut(col);

					String hit = Server.clientTwoIn();

					Server.clientOneOut(hit);

					String hasLost = Server.clientTwoIn();
					Server.clientOneOut(hasLost);
				}

				else if(received.equals("Esci")) {
					this.s.close();
					System.exit(0);
					break;
				}

				Server.clientOneOut("Wait");
				Server.clientTwoOut("Turn");

				received = Server.clientTwoIn();

				if (received.equals("clientoneshot")) {
					String row = Server.clientTwoIn();
					String col = Server.clientTwoIn();

					Server.clientOneOut(row);
					Server.clientOneOut(col);

					String hit = Server.clientOneIn();

					Server.clientTwoOut(hit);

					String hasLost = Server.clientOneIn();
					Server.clientTwoOut(hasLost);
				}

				else if(received.equals("Esci")) {
					this.s.close();
					System.exit(0);
					break;
				}

			} catch (SocketException e) {
				Server.closeServer();

			} catch (IOException e) {
		
			} catch (Exception e) {

			}
		}
		try {
			this.in.close();
			this.out.close();
		} catch (IOException e) {
		}
	}
}