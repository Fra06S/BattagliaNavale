import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ConnectException;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

public class ClientUI extends JFrame implements ActionListener{

	private PBoard pb;
	private Board eb;
	private JPanel cp, tp, sp, bp;

	private JButton btnFire, btnRotate, btnReady, btnSortUp, btnSortDown, btnSearch, btnMenu;

	private JLabel title, servermsg;

	private JTextArea txaSList;
	private JScrollPane scrSList;

	private ShotList sl;

	private int lastRow, lastCol;

	private ExperimentalClient c;

	private CPU cpu;
	private String cpuGameTurn;


	public ClientUI() throws IOException {
		super("BattagliaNavale");
		setSize(600, 750);
		setLayout(null);

		tp = new JPanel();
		tp.setBounds(0,0,300,50);

		title = new JLabel("Giocatore #");
		servermsg = new JLabel("In attesa che l'altro giocatore si connetta...");

		title.setForeground(Color.GREEN);
		servermsg.setForeground(Color.GREEN);
		title.setFont(new Font("Monospaced", Font.BOLD, 16));
		servermsg.setFont(new Font("Monospaced", Font.PLAIN, 12));

		tp.setBackground(Color.BLACK);

		tp.add(title);
		tp.add(servermsg);

		add(tp);
		
		pb = new PBoard();
		eb = new Board();

		pb.drawBoard();
		eb.drawBoard();

		pb.setBounds(0,350,300,300);
		eb.setBounds(0,50,300,300);

		pb.setBackground(Color.BLACK);
		eb.setBackground(Color.BLACK);

		add(eb);
		add(pb);
		
		cp = new JPanel();
		cp.setBounds(0,650,300,100);

		btnFire = new JButton("Colpisci");
		btnRotate = new JButton("Ruota");
		btnReady = new JButton("Pronto");
		btnMenu = new JButton("Menu");

		btnFire.addActionListener(this);
		btnRotate.addActionListener(this);
		btnReady.addActionListener(this);
		btnMenu.addActionListener(this);

		cp.add(btnFire);
		cp.add(btnRotate);
		cp.add(btnReady);
		cp.add(btnMenu);

		btnFire.setVisible(false);
		btnFire.setEnabled(false);
		btnMenu.setVisible(false);

		cp.setBackground(Color.BLACK);

		btnFire.setBackground(Color.BLACK);
		btnReady.setBackground(Color.BLACK);
		btnRotate.setBackground(Color.BLACK);
		btnMenu.setBackground(Color.BLACK);
		btnFire.setForeground(Color.GREEN);
		btnReady.setForeground(Color.GREEN);
		btnRotate.setForeground(Color.GREEN);
		btnMenu.setForeground(Color.GREEN);
		btnFire.setBorder(new LineBorder(Color.GREEN, 1));
		btnReady.setBorder(new LineBorder(Color.GREEN, 1));
		btnRotate.setBorder(new LineBorder(Color.GREEN, 1));
		btnMenu.setBorder(new LineBorder(Color.GREEN, 1));

		btnReady.setFont(new Font("Monospaced", Font.BOLD, 14));
		btnFire.setFont(new Font("Monospaced", Font.BOLD, 14));
		btnRotate.setFont(new Font("Monospaced", Font.BOLD, 14));
		btnMenu.setFont(new Font("Monospaced", Font.BOLD, 14));

		add(cp);
		
		sp = new JPanel();
		sp.setBounds(320, 50, 260, 400);
		sp.setLayout(null);

		txaSList = new JTextArea();
		txaSList.setEditable(false);

		scrSList = new JScrollPane(txaSList);
		scrSList.setBounds(0,0,260,300);
		scrSList.setAutoscrolls(true);

		sp.add(scrSList);

		sl = new ShotList();

		btnSortUp = new JButton("Su");
		btnSortDown = new JButton("Giù");
		btnSearch = new JButton("Cerca");

		btnSortUp.setBounds(0,320,45,25);
		btnSortDown.setBounds(70,320,45,25);
		btnSearch.setBounds(145,320,45,25);

		btnSortUp.addActionListener(this);
		btnSortDown.addActionListener(this);
		btnSearch.addActionListener(this);

		sp.add(btnSortDown);
		sp.add(btnSortUp);
		sp.add(btnSearch);

		btnSortUp.setBackground(Color.BLACK);
		btnSortDown.setBackground(Color.BLACK);
		btnSearch.setBackground(Color.BLACK);
		btnSortUp.setForeground(Color.GREEN);
		btnSortDown.setForeground(Color.GREEN);
		btnSearch.setForeground(Color.GREEN);
		btnSortUp.setBorder(new LineBorder(Color.GREEN, 1));
		btnSortDown.setBorder(new LineBorder(Color.GREEN, 1));
		btnSearch.setBorder(new LineBorder(Color.GREEN, 1));
		
		btnSortUp.setToolTipText("Sort by date from oldest to newest");
		btnSortDown.setToolTipText("Sort by date from newest to oldest");

		sp.setBackground(Color.BLACK);
		txaSList.setBackground(Color.BLACK);
		txaSList.setForeground(Color.GREEN);
		txaSList.setBorder(new LineBorder(Color.GREEN,1));
		txaSList.setFont(new Font("Monospaced", Font.PLAIN, 11));

		add(sp);
		
		bp = new JPanel();
		bp.setBackground(Color.BLACK);
		bp.setBounds(0,0,600,750);
		add(bp);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		this.setLocation(200,0);

		lastRow = -1; lastCol = -1;

	}

	public void runGame(int port) throws IOException {
		
		c = new ExperimentalClient(port);

		title.setText(c.clientIn());
		
		c.clientIn();
		
		servermsg.setText("Posiziona le tue navi e parti. Pronto!");
		
		while (true) {
			
			String turn = c.clientIn();

			if (turn.equals("Wait")) {
				servermsg.setText("In attesa della mossa nemica...");
				
				btnFire.setEnabled(false);

				
				int row = Integer.parseInt(c.clientIn());
				int col = Integer.parseInt(c.clientIn());
				
				boolean hit = pb.checkEnemyShot(row, col);
				c.clientOut(Boolean.toString(hit));
				
				sl.insert(new ShotRecord(row, col, hit, "Opponent"));
				txaSList.setText(sl.toString());

				
				c.clientOut(Boolean.toString(pb.hasLost()));
				if(pb.hasLost()) {	// if the player lost
					servermsg.setText("Hai perso!");

					c.clientOut("Esci");
					c.endClient();
					break;
				}
			}
			
			else if (turn.equals("Turn")) {
				servermsg.setText("Tua Mossa!");
				
				btnFire.setEnabled(true);

				boolean hit = Boolean.parseBoolean(c.clientIn());	
				if (hit) {
					eb.placeHitMarker(eb.getRclick(), eb.getCclick());
				}
				else {
					eb.placeMissMarker(eb.getRclick(), eb.getCclick());
				}

				
				sl.insert(new ShotRecord(eb.getRclick(), eb.getCclick(), hit, "Tu"));
				txaSList.setText(sl.toString());

				boolean hasWon = Boolean.parseBoolean(c.clientIn());
				if (hasWon) {
					servermsg.setText("Hai Vinto!");
					c.clientOut("Esci");
					// c.endClient();
					break;
				}
			}

		}
		btnMenu.setVisible(true);
	}

	public void runGameAgainstAI() throws IOException {

		cpu = new CPU();
		title.setText("Giocatore 1");
		cpuGameTurn = "Raccolta";
		btnFire.setEnabled(true);

		boolean hasWon = false;
		while (true) {
			if (cpuGameTurn.equals("Raccolta")) {
				servermsg.setText("Seleziona una cella e spara!");
			} 

			else if (cpuGameTurn.equals("Picked")) {
				boolean hit = cpu.checkHit(eb.getRclick(), eb.getCclick());
				if (hit) {
					eb.placeHitMarker(eb.getRclick(), eb.getCclick());
				} else {
					eb.placeMissMarker(eb.getRclick(), eb.getCclick());
				}
				sl.insert(new ShotRecord(eb.getRclick(), eb.getCclick(), hit, "Tu"));
				txaSList.setText(sl.toString());

				hasWon = cpu.checkLost();
				if (hasWon) {
					break;
				}
				cpuGameTurn = "cpu";
			}

			else if (cpuGameTurn.equals("cpu")) {
				int shot[] = cpu.shoot();
				boolean hit = pb.checkEnemyShot(shot[0], shot[1]);
				sl.insert(new ShotRecord(shot[0], shot[1], hit, "CPU"));
				txaSList.setText(sl.toString());

				if (pb.hasLost()) {
					break;
				}
				cpuGameTurn = "Raccolta";
			}


		}
		
		btnMenu.setVisible(true);
		
		if (hasWon) {
			servermsg.setText("Hai vinto!");
		} else {
			servermsg.setText("Hai Perso!");
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
					c.clientOut("clientoneshot");
					c.clientOut(Integer.toString(row));
					c.clientOut(Integer.toString(col));

					lastRow = row;
					lastCol = col;
				} 
				catch (IOException e1) {

				} 
				catch (NullPointerException e2) {
					int r = eb.getRclick();
					int c = eb.getCclick();
					if (r == lastRow && c == lastCol) {
						servermsg.setText("Seleziona una nuova cella");
					}
					else {
						cpuGameTurn = "Picked";
						lastRow = r;
						lastCol = c;
					}



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

				if (!servermsg.getText().equals("Your move!")) {
					servermsg.setText("Waiting for opponent to be ready...");
				}

				if(title.getText().equals("Player 2")) {
					try {
						c.clientOut("Ready");
					} catch (IOException e1) {

					}
				}

			}
			else {

			}

		}
		
		else if(btnSortUp == e.getSource()) {
			try {
				sl.insertionSortUp();
				txaSList.setText(sl.toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		else if (btnSortDown == e.getSource()) {
			try {
				sl.insertionSortDown();
				txaSList.setText(sl.toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		else if (btnSearch == e.getSource()) {
			String date = JOptionPane.showInputDialog(null, "Search for a record by date", "HH:MM:ss");
			String out = "Record not found";
			try {
				sl.insertionSortUp();
				int index = sl.binarySearch(date);
				if (index != -1) {
					out = sl.getRecord(index).toString();
				}
			}
			catch (ParseException e1) {
				out = "ParseException: Incorrect date format";
			}
			JOptionPane.showMessageDialog(null, out);
		}
		else if (btnMenu == e.getSource()) {
			new MainUI();
			dispose();
		}


	}


	/**
	 * @param args
	 * @throws IOException 
	 * main UI
	 * The menuUI did not work so we had to create an impromptu menu
	 */
	public static void main(String[] args) {
		ClientUI cui = null;

		String[] options = {"Single-Player", "Multi-Player"};
		int x = JOptionPane.showOptionDialog(null, "Select a game mode",
				"Battleship",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

		if (x == 1) {
			int port = 0;
			while (true) {
				try {
					port = Integer.parseInt(JOptionPane.showInputDialog("Enter a 4-digit port between 1024 and 9999"));
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Invalid port");
				}

				if (port >= 1024 && port <= 9999) {
					break;
				}

			}

			try {
				cui = new ClientUI();
				cui.runGame(port);
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Server does not exist");
				System.exit(0);
			}
		} else {
			try {
				cui = new ClientUI();
				cui.runGameAgainstAI();
			}
			catch (IOException e) {

			}
		}

	}


}
