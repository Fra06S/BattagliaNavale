
public class Ship {
	private int health, length;
	private String name;
	private boolean pos[][];
	private int r, c;
	

	public Ship(String n, int l) {
		this.name = n;
		this.length = l;
		this.health = l;
		this.pos = new boolean[10][10];
		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				this.pos[i][j] = false;
			}
		}
	}
	
	public void setPos(int row, int col, boolean isVertical) {
		this.r = row;
		this.c = col;
		
		if (isVertical) {	
			for (int i = 0; i < length; i++) {
				pos[row + i][col] = true;
			}
		}
		else {				
			for (int i = 0; i < length; i++) {
				pos[row][col + i] = true;
			}
		}
	}
	
	public boolean checkHit(int row, int col) {
		if (pos[row][col]) {
			health--;
			return true;
		}
		else {
			return false;
		}
	}

	public int getHealth() {
		return health;
	}
	
	public int getLength() {
		return length;
	}

	public String getName() {
		return name;
	}
	
	public int getRow() {
		return r;
	}
	
	public int getCol() {
		return c;
	}

	/**
	 * @param args
	 * self testing main method
	 */
	public static void main(String[] args) {
		
		Ship s = new Ship("Battleship", 4);
		s.setPos(3, 5, true);
		System.out.println(s.getHealth());
		
		System.out.println(s.checkHit(4, 5));
		
		System.out.println(s.getHealth());
		System.out.println(s.getName());
		System.out.println(s.getLength());
		System.out.println(s.getRow());
		System.out.println(s.getCol());
		

	}

}
