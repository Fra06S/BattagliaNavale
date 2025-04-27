import javax.swing.JOptionPane;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShotList  {

	private ShotRecord[] list;
	private int maxSize;
	private int size;
	private SimpleDateFormat format;

	public ShotList() {
		this.maxSize = 200;
		list = new ShotRecord[maxSize];
		size = 0;
		format = new SimpleDateFormat("HH:mm:ss");
	}

	public boolean insert(ShotRecord record) {

		if (size < maxSize) {
			size++; 
			list[size-1] = record;  
			return true;
		}
		return false;
	}

	public void bubbleSort() throws ParseException {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size - 1; j++) {
				Date date1 = new SimpleDateFormat("HH:mm:ss").parse(list[j].getsDate()); 
				Date date2 = new SimpleDateFormat(" HH:mm:ss").parse(list[j+1].getsDate()); 
				if (date2.before(date1)) { 
					ShotRecord temp;
					temp = list[j]; 
					list[j] = list[j + 1];  
					list[j+1] = temp; 
				}
			}
		}
	}

	public void insertionSortUp() throws ParseException{
		for (int i = 1; i < size; i++) {
			ShotRecord temp = list[i]; 
			int k = i; 

			Date date1 = new SimpleDateFormat("HH:mm:ss").parse(temp.getsDate());
			Date date2 = new SimpleDateFormat("HH:mm:ss").parse(list[k-1].getsDate());

			while (k > 0 &&  format.parse(temp.getsDate()).before(format.parse(list[k-1].getsDate()))) { 
				list[k] = list[k-1]; 
				k--;
			}

			list[k] = temp; 

		}
	}

	public void insertionSortDown() throws ParseException{
		for (int i = 1; i < size; i++) {
			ShotRecord temp = list[i]; 
			int k = i;

			Date date1 = new SimpleDateFormat("HH:mm:ss").parse(temp.getsDate());
			Date date2 = new SimpleDateFormat("HH:mm:ss").parse(list[k-1].getsDate());

			while (k > 0 &&  format.parse(list[k-1].getsDate()).before(format.parse(temp.getsDate()))) { 
				list[k] = list[k-1];
				k--;
			}

			list[k] = temp; 

		}
	}


	public int binarySearch (String searchKey) throws ParseException {
		int low = 0; 
		int high = size -1; 
		int middle;



		while (low <= high) { 
			middle = (high + low) / 2; 
			Date date1 = new SimpleDateFormat("HH:mm:ss").parse(searchKey); 
			Date date2 = new SimpleDateFormat("HH:mm:ss").parse(list[middle].getsDate()); 

			if (date1.equals(date2)) { 
				return middle;
			}

			else if (date1.before(date2) == true) { 
				high = middle -1;
			}

			else {
				low = middle + 1;
			}

		}
		return -1;
	}

	public int getSize() {
		return this.size;
	}

	
	public ShotRecord getRecord(int i) {
		return list[i];
	}


	public String toString() {
		String out = "";
		for (int i = 0; i < size; i++) {
			out += list[i].toString() + "\n";
		}
		return out;
	}

	public static void main(String[] args) throws ParseException {
		ShotList list = new ShotList(); 

		while (true) {
			String input = JOptionPane.showInputDialog (null, "i - insert \n q - quit \n s - sort \n se - search \n bs - bubble sort");

			switch (input) {

			case "i": {
				String record = JOptionPane.showInputDialog(null, "Enter dd-MM-yyyy HH:mm/accountType/transType/transAmount/balance/endBalance", "20-10-2002 11:11/c/withdraw/500/5000/4500");
				ShotRecord tInfo = new ShotRecord();
				tInfo.processRecord(record);
				if (!list.insert(tInfo)) {
					JOptionPane.showMessageDialog(null, "adding failed");
				}
				break;
			}

			case "bs": {
				list.bubbleSort();
				break;
			}

			case "se": {
				list.bubbleSort();

				String name = JOptionPane.showInputDialog(null, "Enter HH:mm:ss");

				if (list.binarySearch(name) < 0) {
					JOptionPane.showMessageDialog(null, "Not Found");
				}

				else {
					JOptionPane.showMessageDialog(null, "Found");
				}
				break;
			}

			case "p": {
				System.out.println(list.toString());
			}


			}
		}
	}
} 