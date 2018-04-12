import java.io.*;
import java.util.*;
public class TextSeparator {
	
	//separates the full text file into different movies
	public static void separate(){
		BufferedReader br=null;
		try{
			int count=0;
			String filename="file"+count;
			PrintWriter currentFile = new PrintWriter("textfiles/" + filename, "UTF-8");
			
			
			br= new BufferedReader(new FileReader("plot.list"));
		    String line;
		    while ((line = br.readLine()) != null) {
		    	if(line.length()>0 && line.charAt(0)=='-'){
		    		count++;
		    		currentFile.close();
		    		System.out.println("starting file " + count );
		    		filename="file"+count;
		    		currentFile = new PrintWriter("textfiles/" + filename, "UTF-8");
		    		
		    	}
		    	else{
		    		currentFile.write(line+"\n");
		    	}
		    	if(count>1000) break;
		       
		    }
		    currentFile.close();
		}
		
		
		catch(IOException ex){
			System.err.println("Caught IOException: ----" + ex.getMessage());
		}
		
	}
	public static void getFirstMthToNthMovieFull(int m, int n, String filename, String nameOfOutputFiles,String folder){
		BufferedReader br=null;
		try{
			int count=0;
//			nameOfOutputFiles=nameOfOutputFiles+count;

			// if the directory does not exist, create it
			File theDir = new File("textfiles/" + folder);
			if (!theDir.exists()) {
			    System.out.println("creating directory: " + theDir.getName());
			    boolean result = false;

			    try{
			        theDir.mkdir();
			        result = true;
			    } 
			    catch(SecurityException se){
			        //handle it
			    }        
			    if(result) {    
			        System.out.println("DIR created");  
			    }
			}
			PrintWriter currentFile = new PrintWriter("textfiles/" + folder + "/" + filename, "UTF-8");
			
			
			br= new BufferedReader(new FileReader(filename));
		    String line;
		    while ((line = br.readLine()) != null) {
		    	if(line.length()>0 && line.charAt(0)=='-'){
		    		count++;
		    		if(count<m)continue;
		    		currentFile.close();
		    		System.out.println("starting file " + count );
		    		filename=nameOfOutputFiles + count;
		    		currentFile = new PrintWriter("textfiles/" + folder + "/" + filename, "UTF-8");
		    		
		    	}
		    	else{
		    		if(count<m)continue;
		    		if(line.length()>5)line=line.substring(4);
		    		currentFile.write(line+"\n");
		    	}
		    	if(count>n) break;
		       
		    }
		    currentFile.close();
		}
		
		
		catch(IOException ex){
			System.err.println("Caught IOException:" + ex.getMessage());
		}
	}
	public static void getFirstMthToNthMovie(int n, String filename){
		getFirstMthToNthMovieFull(0, n, filename, "output","default folder");
		
	}
	
}
