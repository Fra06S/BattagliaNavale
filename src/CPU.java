import javax.swing.JFrame;

public class CPU {


	private PBoard pb;
	private boolean hasShot[][];	
	

	public CPU() {
		pb = new PBoard();
		hasShot = new boolean[10][10];
		
		placeShips();
	}
	
	public void placeShips() {
		while (true) {
			int row = (int)(Math.random()*10);
			int col = (int)(Math.random()*10);
			boolean isVertical = false;
			
			if (Math.random()*10 > 4) {
				isVertical = true;
			}
			
			pb.setIsVertical(isVertical);
			pb.placeShip(row, col);
			
			if (pb.getAllShipsPlaced()) {
				break;
			}
		}
		
	}
	
	public int[] shoot() {
		int row = 0;
		int col = 0;
		while (true) {
			row = (int)(Math.random()*10);
			col = (int)(Math.random()*10);
			
			if (!hasShot[row][col]) {
				break;
			}
		}
		return new int[] {row, col};
	}
	
	public boolean checkHit(int row, int col) {
		return pb.checkEnemyShot(row, col);
	}
	
	public boolean checkLost() {
		return pb.hasLost();
	}
	

	/**
	 * @param args
	 * self testing main
	 */
	public static void main(String[] args) {
		CPU c = new CPU();
		
		int shot[] = c.shoot();
		System.out.println(shot[0] + " " + shot[1]);
		System.out.println(c.checkHit(7, 8));
		System.out.println(c.checkLost());
	
		
	}

}
