import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends JFrame implements ActionListener  {

	private PBoard pb;
	private Board eb;
	private JPanel cp, tp, sp;

	private JButton btnFire, btnRotate, btnReady;
	
	private JLabel title, servermsg;
	
	private JTextArea txaSList;
	private JScrollPane scrSList;
	
	private ShotList sl;
	
	private int lastRow, lastCol;

	//
	private InetAddress ip;
	private Socket s;
	private DataInputStream in;
	private DataOutputStream out;
	
	public Client() {
		super("Sea Battle");
		setSize(600, 750);
		setLayout(null);
		
		tp = new JPanel();
		tp.setBounds(0,0,300,50);
		
		title = new JLabel("Gicatore #");
		servermsg = new JLabel("In attesa che l'altro giocatore si connetta...");
		
		tp.add(title);
		tp.add(servermsg);
		
		add(tp);

		pb = new PBoard();
		eb = new Board();

		pb.drawBoard();
		eb.drawBoard();

		pb.setBounds(0,350,300,300);
		eb.setBounds(0,50,300,300);

		add(eb);
		add(pb);

		cp = new JPanel();
		cp.setBounds(0,650,300,100);

		btnFire = new JButton("Colpisci");
		btnRotate = new JButton("Ruota");
		btnReady = new JButton("Pronto");

		btnFire.addActionListener(this);
		btnRotate.addActionListener(this);
		btnReady.addActionListener(this);
		
		cp.add(btnFire);
		cp.add(btnRotate);
		cp.add(btnReady);
		
		btnFire.setVisible(false);
		btnFire.setEnabled(false);

		add(cp);
		
		
		sp = new JPanel();
		sp.setBounds(320, 50, 260, 300);
		sp.setLayout(null);
		
		txaSList = new JTextArea();
		
		scrSList = new JScrollPane(txaSList);
		scrSList.setBounds(0,0,260,300);
		
		sp.add(scrSList);
		
		sl = new ShotList();
		
		add(sp);
		//

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		lastRow = -1; lastCol = -1;

		try {
			ip = InetAddress.getByName("100.78.254.26");

			s = new Socket(ip, 4444);

			in = new DataInputStream(s.getInputStream());
			out = new DataOutputStream(s.getOutputStream());
			
			title.setText(in.readUTF());
			servermsg.setText("Posiziona le tue navi e parti. Pronto!");

			while (true) {
				
				String turn = in.readUTF();
				
				if (turn.equals("Wait")) {
					servermsg.setText("In attesa della mossa nemica...");
					
					btnFire.setEnabled(false);
					
					pb.setHP(1);
					
					int row = Integer.parseInt(in.readUTF());
					int col = Integer.parseInt(in.readUTF());
					
					out.writeUTF(Boolean.toString(pb.checkEnemyShot(row, col)));
					
					sl.insert(new ShotRecord(row, col, pb.checkEnemyShot(row, col), "Opponent"));
					txaSList.setText(sl.toString());
					
					out.writeUTF(Boolean.toString(pb.hasLost()));
					if(pb.hasLost()) {
						JOptionPane.showMessageDialog(null, "Hai Perso!");
						
						out.writeUTF("Esci");
						s.close();in.close();out.close();
						break;
					}
				}
				
				else if (turn.equals("Turn")) {
					servermsg.setText("La tua mossa!");
					
					btnFire.setEnabled(true);
					
					boolean hit = Boolean.parseBoolean(in.readUTF());
					
					if (hit) {
						eb.placeHitMarker(eb.getRclick(), eb.getCclick());
					}
					else {
						eb.placeMissMarker(eb.getRclick(), eb.getCclick());
					}
					
					sl.insert(new ShotRecord(eb.getRclick(), eb.getCclick(), hit, "Tu"));
					txaSList.setText(sl.toString());
					
					//
					boolean hasWon = Boolean.parseBoolean(in.readUTF());
					if (hasWon) {
						JOptionPane.showMessageDialog(null, "Hai vinto!");
						out.writeUTF("Esci");
						s.close();in.close();out.close();
						break;
					}
				}
				
			}

			//in.close();
			//out.close();
		} catch (Exception e) {
			e.printStackTrace();        //CHANGE
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (btnFire == e.getSource()) {
			int row = eb.getRclick();
			int col = eb.getCclick();
			
			if (row == lastRow && col == lastCol) {
				servermsg.setText("Seleziona una nuova cella");
			}
			else {
			try {
				out.writeUTF("clientoneshot");
				out.writeUTF(Integer.toString(row));
				out.writeUTF(Integer.toString(col));
				
				lastRow = row;
				lastCol = col;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			}
		}

		else if (btnRotate == e.getSource()) {
			pb.setIsVertical(!pb.getIsVertical());
		}
		
		else if (btnReady == e.getSource()) {
			if (pb.getAllShipsPlaced()) {
				btnFire.setVisible(true);
				btnRotate.setVisible(false);
				btnReady.setVisible(false);
				
				if (!servermsg.getText().equals("La tua mossa!")) {
					servermsg.setText("Aspettando che l'avversario sia pronto...");
				}
				
				
				if(title.getText().equals("Giocatore 2")) {
					try {
						out.writeUTF("Pronto");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				
			}
			else {
				
			}
			
		}

	}
	
	public static void main(String[] args) throws IOException{
		Client c = new Client();

	}
}