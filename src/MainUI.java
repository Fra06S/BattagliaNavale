import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;


public class MainUI extends JFrame implements ActionListener {
	//private instance data
	private JButton btnStart, btnConnect, btnHelp, btnSinglePlayer;        
	private JTextField txtPortNum;
	private JLabel lblPort, lblTitle, lblShip;

	private final int HEIGHT = 750;                                        
	private final int WIDTH = 600;


	
	public MainUI() {
		super("BattagliaNavale");         

		this.setLayout(null);
		this.getContentPane().setBackground(Color.BLACK);

		lblTitle = new JLabel("Battaglia Navale");
		lblTitle.setBounds(90,20,600,70);
		lblTitle.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 40));
		lblTitle.setForeground(Color.GREEN);
		this.add(lblTitle);

		try {
			lblShip = new JLabel(new ImageIcon(ImageIO.read(new FileInputStream("src/ship.png"))));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Image could not load");
		}
		lblShip.setBounds(10, 100, 550,400);
		this.add(lblShip);

		btnStart = new JButton("Crea un Server");
		btnStart.setBounds(30,600,250,30);
		btnStart.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		btnStart.setBackground(Color.GREEN);
		btnStart.setFocusable(false);
		btnStart.setToolTipText("Fare clic per avviare un server: inserire prima un numero di porta valido");
		btnStart.addActionListener(this);
		this.add(btnStart);

		btnConnect = new JButton("Connetti al Server");
		btnConnect.setBounds(310,600,250,30);
		btnConnect.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		btnConnect.setBackground(Color.GREEN);
		btnConnect.setFocusable(false);
		btnConnect.setToolTipText("Click to connect to an existing server - enter a valid port number first");
		btnConnect.addActionListener(this);
		this.add(btnConnect);

		btnSinglePlayer = new JButton("Giocatore Singolo");
		btnSinglePlayer.setBounds(30,650,250,30);
		btnSinglePlayer.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		btnSinglePlayer.setBackground(Color.GREEN);
		btnSinglePlayer.setFocusable(false);
		btnSinglePlayer.setToolTipText("Fai clic per eseguire una partita per giocatore singolo contro l'IA");
		btnSinglePlayer.addActionListener(this);
		this.add(btnSinglePlayer);

		btnHelp = new JButton("Aiuto");
		btnHelp.setBounds(310,650,250,30);
		btnHelp.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		btnHelp.setBackground(Color.GREEN);
		btnHelp.setFocusable(false);
		btnHelp.setToolTipText("Fare clic per ricevere aiuto");
		btnHelp.addActionListener(this);
		this.add(btnHelp);

		lblPort = new JLabel("Numero porta:");
		lblPort.setBounds(400,550,120,30);
		lblPort.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
		lblPort.setForeground(Color.GREEN);
		this.add(lblPort);

		txtPortNum = new JTextField();
		txtPortNum.setBounds(500,550,50,30);
		txtPortNum.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		txtPortNum.setBackground(Color.GREEN);
		txtPortNum.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		txtPortNum.setToolTipText("Seleziona un numero tra 1024 e 9999");
		this.add(txtPortNum);

		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.setLocation(200,0);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}



	public static void main(String[] args) {
		MainUI ui = new MainUI();
	}

	/**
	 * method to check if port number falls within the given range
	 * @param port takes integer port and an input
	 * @return true or false depending on whether the port is between the given range
	 */
	public boolean validPort(int port) {
		if(port >= 1024 && port <= 9999)    
			return true;
		else
			return false;
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == btnStart) {
			boolean validP = false;
			try {
			validP = validPort(Integer.parseInt(txtPortNum.getText()));
			}
			catch (NumberFormatException e) {
			}
			
			
			if (validP) {
				Thread t = new Thread() {
					public void run() {
						Server s = new Server(Integer.parseInt(txtPortNum.getText()));

					}
				};

				Thread t1 = new Thread() {
					public void run() {
						try {
							ClientUI c = new ClientUI();
							c.runGame(Integer.parseInt(txtPortNum.getText()));
							setVisible(false);
						} catch (IOException e) {
						}

					}
				};

				t.start();
				t1.start();
				this.dispose();
			}
			else {
				JOptionPane.showMessageDialog(null, "Porta non valida");
			}
			
			
		}
		else if (evt.getSource() == btnConnect) {
			
			boolean validP = false;
			try {
			validP = validPort(Integer.parseInt(txtPortNum.getText()));
			}
			catch (NumberFormatException e) {
			}
			
			
			if (validP) {
			
			Thread t = new Thread() {
				public void run() {
					ClientUI c;
					try {
						c = new ClientUI();
						c.runGame(Integer.parseInt(txtPortNum.getText()));

					} catch (IOException e) {
					}

				}
			};
			t.start();
			this.dispose();
			
			}
			
			else {
				JOptionPane.showMessageDialog(null, "Porta non valida");
			}
		}
		else if(evt.getSource() == btnHelp) {
			try {
				HelpUI help = new HelpUI();   
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Unable to Locate Help File");
			}
		}
		else if (evt.getSource() == btnSinglePlayer) {
			Thread t = new Thread() {
				public void run() {
					try {
						ClientUI c = new ClientUI();
						c.runGameAgainstAI();


					} catch (IOException e) {
						e.printStackTrace();
					}
				}};
				t.start();
				this.dispose();
		}
	}
}