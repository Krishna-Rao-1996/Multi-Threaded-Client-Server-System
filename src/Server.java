// Name: Krishna Rao
// Student ID: 1001738701


import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//References
//https://github.com/aoyshi/Multi-Threaded-Web-Server-Client/blob/master/myServer.java 
//https://github.com/jbmeta/Multiclient-Chat
//https://github.com/prajwal126/Multithreaded-Client-Server-Architechture 
//https://stackoverflow.com/questions/34613867/multiclient-java-client-server-chat
//https://gist.github.com/chatton/8955d2f96f58f6082bde14e7c33f69a6
//https://stackoverflow.com/questions/5125242/java-list-only-subdirectories-from-a-directory-not-files/5125258
//https://www.baeldung.com/java-delete-directory
//https://stackoverflow.com/questions/15247752/gui-client-server-in-java

public class Server {

	//Initializing required global instances
	
	public static ServerSocket ss;
	public static Vector<ClientHandler> ar = new Vector<>(); 
	public static Socket socket;
	public static ArrayList<String> userList = new ArrayList<>(); // used to add new users to userlist
	public static JFrame frmServer;
	public static JTextArea ServerTextArea;
	public static String test="test";
	private JButton ClearScreen;

	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server window = new Server(); //used to create new window instances every time its called
					window.frmServer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
        // don't need to specify a hostname, it will be the current machine
		
        ss = new ServerSocket(7777); // Creating socket connection
        System.out.println("ServerSocket awaiting connections...");
		
		while(true)
		{   
	        socket = ss.accept(); // blocking call, this will wait until a connection is attempted on this port.
	        System.out.println("Connection from " + socket + "!");
	        
        	ClientHandler mtch = new ClientHandler(socket,ServerTextArea,userList); //Client handler method which handles multiple clients
			Thread t = new Thread(mtch); // Initializing new threads			
			t.start();	//Starting new thread
		}
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public Server()
	{
		initialize(); //this method is called to initialize all GUI related contents
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmServer = new JFrame();
		frmServer.setTitle("Server");
		frmServer.setBounds(100, 100, 728, 516);
		frmServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmServer.getContentPane().setLayout(null);
		
		JButton StopButton = new JButton("Stop Server"); //this button is used to a stop server session
		StopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
		        System.exit(-1);
			}
		});
		StopButton.setBounds(104, 63, 113, 41);
		frmServer.getContentPane().add(StopButton);
		
	    ServerTextArea = new JTextArea();
		ServerTextArea.setBounds(104, 140, 433, 262);
		frmServer.getContentPane().add(ServerTextArea);
		ServerTextArea.setColumns(10);
		
		ClearScreen = new JButton("Clear Screen"); // this button is used to clear ServerTextArea
		ClearScreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				ServerTextArea.setText(" ");
			}
		});
		ClearScreen.setBounds(255, 63, 113, 41);
		frmServer.getContentPane().add(ClearScreen);
	}
	
	public void appends(JTextArea ServerTextArea, String newText){
		ServerTextArea.setText(ServerTextArea.getText() + "/r/n" + newText);
	}
}
