import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class Board extends JPanel implements ActionListener {

	
	protected JButton btnGrid[][];
	private int rClick, cClick;
	private Border dborder, cborder;
	private JLabel markers[];
	
	public Board() {
		
		super();
		
	
		this.btnGrid = new JButton[10][10];
		rClick = 0;
		cClick = 0;
		
		dborder = new LineBorder(Color.GREEN, 1);
		cborder = new LineBorder(Color.GREEN, 4);
		markers = new JLabel[21];
		
		for (int i = 0; i < btnGrid.length; i++) {
			for (int j = 0; j < btnGrid.length; j++) {
				btnGrid[i][j] = new JButton();
				btnGrid[i][j].addActionListener(this);
				btnGrid[i][j].setText("");
				
				btnGrid[i][j].setBackground(Color.BLACK);
				btnGrid[i][j].setBorder(dborder);
			}
		}
		
		for (int i = 0; i < markers.length; i++) {
			markers[i] = new JLabel("");
			markers[i].setForeground(Color.GREEN);
			markers[i].setFont(new Font("Monospaced", Font.BOLD, 11));
		}
	}
	

	public void drawBoard() {
		
		setSize(400, 400);
		setLayout(new GridLayout(11, 11));
		setBounds(0, 0, 200, 200);
		
		int index = 1;
		
		add(new JLabel(""));
		for (int i = 1; i < 11; i++) {
			markers[index].setText(Integer.toString(i));
			add(markers[index]);
			index++;
		}
		
		for (int i = 0; i < btnGrid.length; i++) {
			for (int j = 0; j < btnGrid.length; j++) {
				if (j == 0) {
					markers[index].setText(Character.toString((char)(65 + i)));
					add(markers[index]);
					index++;
				}
				add(btnGrid[i][j]);
			}
		}
		setVisible(true);
	}
	
	public void placeHitMarker(int row, int col) {
		btnGrid[row][col].setBackground(Color.YELLOW); btnGrid[row][col].setOpaque(true); btnGrid[row][col].setBorderPainted(false);
		btnGrid[row][col].setEnabled(false);
	}
	
	public void placeMissMarker(int row, int col) {
		btnGrid[row][col].setBackground(Color.GRAY); btnGrid[row][col].setOpaque(true); btnGrid[row][col].setBorderPainted(false);
		btnGrid[row][col].setEnabled(false);
	}
	
	public int getRclick() {
		return rClick;
	}
	
	public int getCclick() {
		return cClick;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		for (int row = 0; row < btnGrid.length; row++) {
			for (int col = 0; col < btnGrid.length; col++) {
				btnGrid[row][col].setBorder(dborder);
			}
		}
		
		for (int row = 0; row < btnGrid.length; row++) {
			for (int col = 0; col < btnGrid.length; col++) {
				if (btnGrid[row][col] == e.getSource()) {
					rClick = row; cClick = col;
					btnGrid[row][col].setBorder(cborder);
				}
			}
		}
	}

	public static void main(String[] args) {
		Board b = new Board();
		b.drawBoard();
		
		b.placeHitMarker(0, 0);
		b.placeMissMarker(0, 1);
		
		JFrame f1 = new JFrame();
		f1.setSize(500,500);
		f1.setLayout(null);
		f1.add(b);
		f1.setVisible(true);
		
		
	}

	

}
