import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class PBoard extends Board implements ActionListener {


	private Ship ship[];
	private int hp, index;
	private boolean isVertical, shipsPlaced;

	public PBoard() {
		super();

		this.ship = new Ship[5];

		ship[0] = new Ship("Carrier", 5);
		ship[1] = new Ship("Battleship", 4);
		ship[2] = new Ship("Cruiser", 3);
		ship[3] = new Ship("Submarine", 3);
		ship[4] = new Ship("Destroyer", 2);

		this.hp = 17;
		this.index = 0;
		this.isVertical = false;
		this.shipsPlaced = false;
	}

	public void placeShip(int row, int col) {
		boolean fits = true;
		try {
			for (int i = 0; i < ship[index].getLength(); i++) {
				if (isVertical) {	
					if (btnGrid[row + i][col].getText().equals(" ")) {
						fits = false;		
					}
				}
				else {				
					if (btnGrid[row][col + i].getText().equals(" ")) {
						fits = false;		
					}
				}
			}
		}
		
		catch (ArrayIndexOutOfBoundsException e) {	
		}
		
		
		if (fits) {
			
			ship[index].setPos(row, col, isVertical);

			if (isVertical) {		
				for (int i = 0; i < ship[index].getLength(); i++) {
					btnGrid[row + i][col].setBackground(Color.GREEN); btnGrid[row + i][col].setOpaque(true); btnGrid[row + i][col].setBorderPainted(false);
					btnGrid[row + i][col].setText(" ");	btnGrid[row + i][col].setForeground(Color.GREEN);
					
				}
			}
			else {					
				for (int i = 0; i < ship[index].getLength(); i++) {
					btnGrid[row][col + i].setBackground(Color.GREEN); btnGrid[row][col + i].setOpaque(true); btnGrid[row][col + i].setBorderPainted(false);
					btnGrid[row][col + i].setText(" ");	btnGrid[row][col + i].setForeground(Color.GREEN);
				}
			}
			
			index++;

			if (index == ship.length) {		
				this.shipsPlaced = true;
				for (int i = 0; i < btnGrid.length; i++) {
					for (int j = 0; j < btnGrid.length; j++) {
						btnGrid[i][j].setEnabled(false);
						btnGrid[i][j].setText("");
					}
				}
			}
		}

	}

	public boolean checkEnemyShot(int row, int col) {
		for (int i = 0; i < ship.length; i++) {
			if (ship[i].checkHit(row, col)) {
				hp--;
				placeHitMarker(row, col);
				return true;
			}
		}
		return false;
	}

	public boolean hasLost() {
		if (hp == 0) {
			return true;
		}
		else {
			return false;
		}
	}

	public void setIsVertical(boolean in) {
		this.isVertical = in;
	}

	public boolean getIsVertical() {
		return isVertical;
	}

	public boolean getAllShipsPlaced() {
		return shipsPlaced;
	}

	public void setHP(int in) {
		hp = in;
	}

	public void actionPerformed(ActionEvent e) {
		for (int row = 0; row < btnGrid.length; row++) {
			for (int col = 0; col < btnGrid.length; col++) {
				if (btnGrid[row][col] == e.getSource()) {
					placeShip(row, col);
				}
			}
		}
	}

	/**
	 * @param args
	 * Self testing main method
	 */
	public static void main(String[] args) {

		PBoard b = new PBoard();
		b.drawBoard();
		

		JFrame f1 = new JFrame();
		f1.setSize(500,500);
		f1.setLayout(null);
		f1.add(b);
		f1.setVisible(true);

	}

}
