import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HelpUI extends JFrame implements ActionListener{
    
    private JButton btnClose;           
    private JTextArea txaHelpInfo;
    private JLabel lblStudio;

    private String helpText = "";       

   
    public HelpUI() throws IOException {
        super ("Battagli Navale Aiuto");   

        getContentPane().setBackground(Color.BLACK);    

        
        btnClose = new JButton("Chiudi");
        btnClose.setBounds(250, 650, 75, 25);
        btnClose.setFont(new Font(Font.MONOSPACED, Font.BOLD, 10));     
        btnClose.setForeground(Color.BLACK);
        btnClose.setBackground(Color.GREEN);
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.addActionListener(this);
        add(btnClose);

        
        FileReader fr = new FileReader("src/help.txt");
        BufferedReader input = new BufferedReader(fr);

        
        for (int i = 0; i < sizeOfFile("src/help.txt"); i++) {
            helpText = helpText + input.readLine() + "\n";
        }

        input.close();

        txaHelpInfo = new JTextArea(helpText);
        txaHelpInfo.setBounds(25, 100, 550, 600);
        txaHelpInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        txaHelpInfo.setLineWrap(true);
        txaHelpInfo.setWrapStyleWord(true);
        txaHelpInfo.setForeground(Color.GREEN);
        txaHelpInfo.setBackground(Color.BLACK);
        txaHelpInfo.setEditable(false);
        this.add(txaHelpInfo);

        
        lblStudio = new JLabel("Aiuto");
        lblStudio.setBounds(250, 20, 200, 50);
        lblStudio.setForeground(Color.GREEN);
        lblStudio.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 30));
        this.add(lblStudio);

        
        this.setLayout(null);
		this.setLocation(200,0);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600,750);

        
        this.setVisible(true);
        this.setResizable(false);
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == btnClose) {  
        }
    }

    /**
     * Method to determine the length of the file
     * @param fileName is the name of the file to be read
     * @return an the size of the file as an integer
     */
    public static int sizeOfFile(String fileName) throws IOException {
        FileReader fr = new FileReader(fileName);         
        BufferedReader input = new BufferedReader(fr);

        int fileSize = 0;    

        while (!input.readLine().equalsIgnoreCase("EOF")) {  
            fileSize++;
        }

        input.close();   
        return fileSize;  
    }

    public static void main(String[] args) throws IOException {
        HelpUI help = new HelpUI();  
    }

}