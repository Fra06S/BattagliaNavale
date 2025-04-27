import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;




public class ShotRecord {

    private int row;
    private int col;
    private boolean hitOrMiss;
    private String userID;

    private Date date;
    private String sDate;
    private SimpleDateFormat format;

    public ShotRecord() {

        row = 0;
        col = 0;
        hitOrMiss = false;
        userID = "Giocatore 1";


        format = new SimpleDateFormat("HH:mm:ss");
        date = new Date();
        sDate = format.format(date);
    }

    public ShotRecord(int r, int c, boolean hOrm, String id) {
        row = r;
        col = c;
        hitOrMiss = hOrm;
        userID = id;

        format = new SimpleDateFormat("HH:mm:ss");
        date = new Date();
        sDate = format.format(date);
    }

    public String getsDate() {
        return sDate;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean getHitOrMiss() {
        return hitOrMiss;
    }

    public void setRow(int r) {
        this.row = r;
    }

    public void setY(int c) {
        this.col = c;
    }

    public void setHitOrMiss(boolean hitOrMiss) {
        this.hitOrMiss = hitOrMiss;
    }

    public void processRecord (String record) {
        String[] words;
        words = record.split("/");

        this.sDate = words[0];
        this.row = Integer.parseInt(words[1]);
        this.col = Integer.parseInt(words[2]);
        this.hitOrMiss = Boolean.parseBoolean(words[3]);
    }


    public String toString() {
    	String loc = "";
    	loc = Character.toString((char)(row + 65));
    	loc += (col +1);
    	
    	String hOm = "";
    	if (hitOrMiss) {
    		hOm = "Colpito";
    	}
    	else {
    		hOm = "Mancato";
    	}
    	
    	
        return "[" + sDate + "] " + userID + " fired on " + loc + ", " + hOm;
    }


    public static void main(String[] args) {

        ShotRecord tran = new ShotRecord();

        String record = "10:20:45/10/5/Hit";

        tran.processRecord(record);


        System.out.println(tran.toString());

        System.out.println(tran.getsDate());
        System.out.println(tran.getRow());
        System.out.println(tran.getCol());
        System.out.println(tran.getHitOrMiss());

    }
} 
