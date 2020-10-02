// Name: Krishna Rao
// Student ID: 1001738701


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JTextArea;

//References
//https://github.com/aoyshi/Multi-Threaded-Web-Server-Client/blob/master/myServer.java 
//https://github.com/jbmeta/Multiclient-Chat
//https://github.com/prajwal126/Multithreaded-Client-Server-Architechture 
//https://stackoverflow.com/questions/34613867/multiclient-java-client-server-chat
//https://gist.github.com/chatton/8955d2f96f58f6082bde14e7c33f69a6
//https://stackoverflow.com/questions/5125242/java-list-only-subdirectories-from-a-directory-not-files/5125258
//https://www.baeldung.com/java-delete-directory
//https://stackoverflow.com/questions/15247752/gui-client-server-in-java


public class ClientHandler extends Thread {

	//Initializing required global instances
	
	protected static final FileVisitResult CONTINUE = null;
	protected static final FileVisitResult TERMINATE = null;
	public static String text;
	public static JTextArea servertext;
	public static String HomeDirect = "D:\\Eclipse\\DS\\";
	public static String Default = "D:\\Eclipse\\DS\\";
	public static String tomove = null;
	public ArrayList<String> usernamelist;
	public boolean isLoggedin;
	public boolean res = false;
	public Socket sock;
	public DataInputStream IN;
	public InputStream inputStream = null;
	
	public ClientHandler(Socket socket,JTextArea serverTextArea, ArrayList<String> userList) // this constructor brings in values and saves them in global variables called above
	{
		super("ClientHandler");
		this.sock = socket;
		this.servertext = serverTextArea;
		this.usernamelist = userList;
		this.isLoggedin=true;
	}

	public void run() 
	{	
		while(this.isLoggedin)
		{
			try
			{
		        // get the input stream from the connected socket
		        inputStream = sock.getInputStream();
		        // create a DataInputStream so we can read data from it.
		        IN = new DataInputStream(inputStream);

		        // read the message from the socket
		        text = IN.readUTF();
			}
			catch(Exception ex)
			{
				System.out.println("Exception in"+ex.toString());
			}
			
	        if(text!= null && text.contains(" ") == false)
	        {
	        	if(!usernamelist.contains(text)) // Checks whether username already exists
	        	{
	        		usernamelist.add(text);
	        		servertext.append("Client successfully added!! username is: " + text + "\r\n");
	        		
	        		File file = new File(""+HomeDirect+""+text+"");
	                if (!file.exists()) {
	                    if (file.mkdir()) { //Creates new Home Directory
	                    	servertext.append("Home Directory named "+text+" is created! \r\n");
	                    } else {
	                    	servertext.append("Failed to create directory! \r\n");
	                    }
	                }
	                else //Failure case of Creating new Home Directory
	                {
	                	servertext.append("Home Directory named "+text+" already exists! \r\n");
	                }
	        	}
	        	else
	        	{
	        		servertext.append("The username already exists \r\n");
	        	}
	        	
	        	servertext.append(HomeDirect);
	        }
	        else
	        {
	        	String[] splitstr = text.split(" ");
	        	
	        	String mes = splitstr[0];
	        	String action = splitstr[1];
	        	
	        	if(action.matches("list")) // this is used to list directories
	        	{
	        		try {
		        		Path dir = FileSystems.getDefault().getPath(HomeDirect);
		        		DirectoryStream<Path> stream = Files.newDirectoryStream( dir );
		        		for (Path path : stream) {
		        			servertext.append( path.getFileName().toString() +" \r\n");
		        		}
		        		stream.close();
	                }
	        		catch(Exception e)  //Failure case
	        		{
	                    System.err.println("Error!");
	                }  	
	        	}
	        	if(action.matches("create")) // this is used to create new file directories
	        	{
	        		servertext.append("Current Home Directory before create directory is "+HomeDirect+" \r\n");
	        		File file = new File(HomeDirect+"\\"+ mes);
	        		if (!file.exists()) {
	                    if (file.mkdir()) {
	                    	servertext.append("Directory named "+mes+" is created! \r\n");
	                    } else {
	                    	servertext.append("Failed to create directory! \r\n");
	                    }
	                }
	                else //Failure case
	                {
	                	servertext.append("Directory named "+mes+" already exists! \r\n");
	                }
	        	}
	        	if(action.matches("go")) // this is used to go inside a file directory
	        	{
	        		File f = new File(HomeDirect);
	        		String dummy = HomeDirect;
	        		File d = new File(dummy +"\\"+ mes);
	        		if(!d.exists()) //Failure case
	        		{
	        			servertext.append("File Directory is not valid \r\n");
	        		}
	        		else 
	        		{
	        			HomeDirect = f.getPath() +"\\"+ mes;
	        			servertext.append("Current File Directory is: "+HomeDirect+" \r\n");
	        		}
	        	}
	        	if(action.matches("come")) // this is used to go back from a file directory
	        	{
	        		File f = new File(HomeDirect);
	        		File d = new File(Default);
	        		if (f.compareTo(d) == 0) //Failure case
	        		{
		        		servertext.append(" Illegal Command : Cannot move out of Home Directory \r\n");
	        		}
	        		else
	        		{
		        		HomeDirect = f.getParentFile().getPath().toString()+ File.separator;
		        		servertext.append("Current File Directory is: "+HomeDirect+" \r\n");
	        		}
	        	}
	        	if(action.matches("delete")) // this is used to delete a directory
	        	{
	        		File index = new File(HomeDirect);
	        		boolean res = deleteDirectory(index);
	        		System.out.println(res);
	        		HomeDirect = index.getParentFile().getPath().toString()+"\\";
	        		servertext.append("Current File Directory is: "+HomeDirect+" \r\n");
	        	}
	        	if(action.matches("rename")) //this is used to rename a directory
	        	{
	        		File original = new File(HomeDirect);
	        		servertext.append("Current File Directory : "+HomeDirect+" \r\n");
	        		String toberenamed = original.getParentFile().toString()+"\\"+ mes;
	        		File rename = new File(toberenamed);
	        		servertext.append("Current File Directory rename to : "+toberenamed+" \r\n");
	        		
	        		if (original.renameTo(rename)) {
	        			servertext.append("Directory renamed successfully");
	        		} else {   //Failure case
	        			servertext.append("Failed to rename directory");
	        		}
	        	}
	        	if(action.matches("select")) // this is used to select a file directory to move
	        	{
	        		tomove = HomeDirect;
	        		servertext.append("Source File Directory : "+tomove+" \r\n");
	        	}
	        	if(action.matches("move")) // this is used to move a file directory to a new location
	        	{
	        		File from = new File(tomove);
	        		File to = new File(HomeDirect);
	        		
	        	    boolean success = from.renameTo(new File(to, from.getName()));
	        	    
	        	    if(success)
	        	    {
	        	    	servertext.append("Moving of Directories successfull \r\n");
	        	    }
	        	    else //Failure case
	        	    {
	        	    	servertext.append("Moving Error \r\n");
	        	    }
	        	}
	        }
	        
	        isLoggedin = false;
		}
	}
	boolean deleteDirectory(File directoryToBeDeleted) { //Function helping in deleting directory
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
	public static void move(File sourceFile, File destFile) { //Function helping in moving directory
        if (sourceFile.isDirectory()) {
            File[] files = sourceFile.listFiles();
            assert files != null;
            for (File file : files) move(file, new File(destFile, file.getName()));
            if (!sourceFile.delete()) throw new RuntimeException();
        } else {
            if (!destFile.getParentFile().exists())
                if (!destFile.getParentFile().mkdirs()) throw new RuntimeException();
            if (!sourceFile.renameTo(destFile)) throw new RuntimeException();
        }
    }
}
