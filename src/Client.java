// Name: Krishna Rao
// Student ID: 1001738701


import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.Font;

//References
//https://github.com/aoyshi/Multi-Threaded-Web-Server-Client/blob/master/myServer.java 
//https://github.com/jbmeta/Multiclient-Chat
//https://github.com/prajwal126/Multithreaded-Client-Server-Architechture 
//https://stackoverflow.com/questions/34613867/multiclient-java-client-server-chat
//https://gist.github.com/chatton/8955d2f96f58f6082bde14e7c33f69a6
//https://stackoverflow.com/questions/5125242/java-list-only-subdirectories-from-a-directory-not-files/5125258
//https://www.baeldung.com/java-delete-directory
//https://stackoverflow.com/questions/15247752/gui-client-server-in-java


public class Client {

	//Initializing required global instances
	
	public static JFrame frmClient;
	public static JTextArea ClientTextArea;
	public static JTextField UserNameArea;
	public static JButton CreateDirect;
	public static JButton DeleteDirect;
	public static BufferedReader br;
	public static JButton SendtoServer;
	public static JButton RenameDirect;
	public static JButton MoveDirect;
	public static ServerSocket ss;
	public static Socket socket;
	private JButton ListDirect;
	private JButton Goingin;
	private JButton Comingout;
	private JButton SelecttoMove;
	private JButton CloseConnect;
	
	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client(); //this initializes new instances of client
					window.frmClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Client() {
		initialize(); //this method is called to initialize all GUI related contents
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() { 
		frmClient = new JFrame();
		frmClient.setTitle("Client");
		frmClient.setBounds(100, 100, 738, 463);
		frmClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmClient.getContentPane().setLayout(null);
		
		CreateDirect = new JButton("Create Directory");
		CreateDirect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				ConnectServer("create");
			}
		});
		CreateDirect.setBounds(518, 155, 165, 33);
		CreateDirect.setEnabled(false);
		frmClient.getContentPane().add(CreateDirect);
		
		SendtoServer = new JButton("Create Home Directory");
		SendtoServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				ConnectServer("default");
			}
		});
		SendtoServer.setBounds(518, 69, 165, 33);
		frmClient.getContentPane().add(SendtoServer);
		
		DeleteDirect = new JButton("Delete Directory");
		DeleteDirect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				ConnectServer("delete");
			}
		});
		DeleteDirect.setBounds(518, 198, 165, 33);
		DeleteDirect.setEnabled(false);
		frmClient.getContentPane().add(DeleteDirect);
		
		RenameDirect = new JButton("Rename Directory");
		RenameDirect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				ConnectServer("rename");
			}
		});
		RenameDirect.setBounds(518, 241, 165, 33);
		RenameDirect.setEnabled(false);
		frmClient.getContentPane().add(RenameDirect);
		
		ClientTextArea = new JTextArea();
		ClientTextArea.setBounds(27, 132, 448, 231);
		frmClient.getContentPane().add(ClientTextArea);
		ClientTextArea.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("User Name :");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(27, 79, 96, 43);
		frmClient.getContentPane().add(lblNewLabel);
		
		UserNameArea = new JTextField();
		UserNameArea.setBounds(133, 88, 121, 29);
		frmClient.getContentPane().add(UserNameArea);
		UserNameArea.setColumns(10);
		
		ListDirect = new JButton("List Directories");
		ListDirect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				ConnectServer("list");
			}
		});
		ListDirect.setEnabled(false);
		ListDirect.setBounds(518, 112, 165, 33);
		frmClient.getContentPane().add(ListDirect);
		
		Goingin = new JButton("Go in");
		Goingin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				ConnectServer("go");
			}
		});
		Goingin.setBounds(288, 24, 165, 33);
		Goingin.setEnabled(false);
		frmClient.getContentPane().add(Goingin);
		
		Comingout = new JButton("Come out");
		Comingout.setEnabled(false);
		Comingout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				ConnectServer("come");
			}
		});
		Comingout.setBounds(288, 89, 165, 33);
		frmClient.getContentPane().add(Comingout);
		
		MoveDirect = new JButton("Move Directory");
		MoveDirect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				ConnectServer("move");
			}
		});
		MoveDirect.setEnabled(false);
		MoveDirect.setBounds(518, 330, 165, 33);
		frmClient.getContentPane().add(MoveDirect);
		
		SelecttoMove = new JButton("Select Directory to Move");
		SelecttoMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				ConnectServer("select");
			}
		});
		SelecttoMove.setEnabled(false);
		SelecttoMove.setBounds(487, 287, 196, 33);
		frmClient.getContentPane().add(SelecttoMove);
		
		CloseConnect = new JButton("Close");
		CloseConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				System.exit(-1);
			}
		});
		CloseConnect.setBounds(27, 30, 165, 33);
		frmClient.getContentPane().add(CloseConnect);
	}
	
	public void ConnectServer(String action) // this method is used to connect with server
	{   
		try
		{
	        // need host and port, we want to connect to the ServerSocket at port 7777
	        socket = new Socket("localhost", 7777); //starting socket
	        ClientTextArea.setText("Connected! \r\n");

	        // get the output stream from the socket.
	        OutputStream outputStream = socket.getOutputStream(); 
	        // create a data output stream from the output stream so we can send data through it
	        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
	        
	        // write the message we want to send
	        if(action.matches("default")) //this is for creating Home directories
	        {		            
	        	if(isAlphaNumeric(UserNameArea.getText().toString()))
	        	{
		        	dataOutputStream.writeUTF(UserNameArea.getText());
		        	
			        CreateDirect.setEnabled(true);
			        DeleteDirect.setEnabled(true);
			        RenameDirect.setEnabled(true);
			        ListDirect.setEnabled(true);
			        Goingin.setEnabled(true);
			        Comingout.setEnabled(true);
			        SelecttoMove.setEnabled(true);
			        MoveDirect.setEnabled(true);
	        	}
	        	else
	        	{
	        		JOptionPane.showMessageDialog(null, "Illegal username entered");
	        	}
	        }
	        if(action.matches("create")) // used for creating file directories
	        {
	        	dataOutputStream.writeUTF(UserNameArea.getText()+" "+"create");
	        }
	        if(action.matches("list")) // used to list directories
	        {
	        	dataOutputStream.writeUTF(UserNameArea.getText()+" "+"list");
	        }
	        if(action.matches("go")) // used to go inside a file directory
	        {
	        	dataOutputStream.writeUTF(UserNameArea.getText()+" "+"go");
	        }
	        if(action.matches("come")) // used to come back outside from a directory
	        {
	        	dataOutputStream.writeUTF(UserNameArea.getText()+" "+"come");
	        }	        
	        if(action.matches("delete")) // used to delete a directory
	        {
	        	dataOutputStream.writeUTF(UserNameArea.getText()+" "+"delete");
	        }
	        if(action.matches("rename")) // used to rename a directory
	        {
	        	dataOutputStream.writeUTF(UserNameArea.getText()+" "+"rename");
	        }
	        if(action.matches("select")) //used to select a directory to move
	        {
	        	dataOutputStream.writeUTF(UserNameArea.getText()+" "+"select");
	        }
	        if(action.matches("move")) //used to move directory
	        {
	        	dataOutputStream.writeUTF(UserNameArea.getText()+" "+"move");
	        }
	        dataOutputStream.flush(); // send the message
	        dataOutputStream.close(); // close the output stream when we're done.
	        
		}
		catch(Exception ex) //Failure case
		{
			ClientTextArea.append("Exception is"+ex.toString());
		}
	}
	public static boolean isAlphaNumeric(String s) { // used to check for illegal characters
		return s != null && s.matches("^[a-zA-Z0-9]*$");
	}
}
