import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import edu.mit.jwi.*;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
public class MoviesMash {

	public static HashMap<Integer,Token> tokensHashIndex=new HashMap<Integer,Token>(30000);
	public static HashMap<String,Integer> moviesTable=new HashMap<String,Integer>(1000);
	public static HashMap<String,Integer> tokensTable=new HashMap<String,Integer>(1000);
	public static HashMap<Integer,Integer> moviesDescrLength=new HashMap<Integer,Integer>(1000);
	public static HashMap<Integer,Double> scores= new HashMap<Integer,Double>(200);
	public static HashSet<String> stopWords=new HashSet<String>(25);

	public static String[] INPUT_TEXTS = {
		    "The growing popularity of Linux in Asia, Europe, and the US is " +
		    "a major concern for Microsoft."
	};
	
	
	public static void main(String[] args) throws IOException {
//		TextSeparator.getFirstMthToNthMovieFull(0, 1000, "plot.list", "movie", "movies");
//		goThroughFiles("textfiles/movies/", "movie",0,1000);
//		serializeTables();
//		deserialize("1000tokens.serialized");
//		startConsole();


	
}	
public static void testTagSentence() throws Exception {
    for (String sentence : INPUT_TEXTS) {
      RuleBasedTagger tagger = new RuleBasedTagger();
      tagger.setWordnetDictLocation("/home/aser93/workspace/WordNet-3.0/dict");
      tagger.setSuffixMappingLocation("src/main/resources/pos_suffixes.txt");
      tagger.setTransitionProbabilityDatafile(
        "src/main/resources/pos_trans_prob.txt");
      String taggedSentence = tagger.tagSentence(sentence);
      System.out.println("Original: " + sentence);
      System.out.println("Tagged:   " + taggedSentence);
    }
}
	public static int POStagging(String searchWord) throws IOException {
        String s = null;
		String wnhome = "/home/aser93/workspace/WordNet-3.0/";
		String path = wnhome + File . separator + "dict/";
		URL url = new URL ("file", null , path ) ;
		
		// construct the dictionary object and open it
		IDictionary dict = new Dictionary( url ) ;
		dict.open () ;
        try {

            IIndexWord idxWord = dict.getIndexWord(searchWord, POS.NOUN);
            for (int i = 0; i < idxWord.getWordIDs().size(); i++) {
            IWordID wordID = idxWord.getWordIDs().get(i); // ist meaning
            edu.mit.jwi.item.IWord word = dict.getWord(wordID);
            edu.mit.jwi.item.ISynset synset = word.getSynset();
            List<edu.mit.jwi.item.IWord> words;
            System.out.println(synset.getGloss());

            }
            return -1;
        } catch (Exception e) {
            return -2;
        }
    }
	
	
	
	public static void goThroughFiles(String path,String filename,int indexfrom,int indexto){
		BufferedReader br= null;
		createStopTable("stopWords");
		int counter=indexfrom;
		
		while(counter<indexto){
			try {
				generateTokens(path+filename+counter);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			counter++;
		}

		//printIndex();
		serializeTables();
		
		
	}
	
	
	public static void startConsole(){
		Scanner scanner=new Scanner(System.in);
		System.out.println("Enter a query:");
		String input=scanner.nextLine();
		executeQuery(input);
		String keyPressed="Y";
		while(keyPressed.equals("Y")||keyPressed.equals("y")){
		//
		System.out.print("Do you want another query? (Y/N):");
		keyPressed=scanner.nextLine();
		keyPressed=keyPressed.trim();
		
		if(keyPressed.equals("Y")||keyPressed.equals("y")){
			System.out.println("Enter a query:");
			String queryInput=scanner.nextLine();
			executeQuery(queryInput);
		}
		else System.out.println("Exiting...");
		
		}
		
	}
	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
	  List<T> list = new ArrayList<T>(c);
	  java.util.Collections.sort(list);
	  return list;
	}
	
	
	public static void executeQuery(String input){
		System.out.println("executing query: " + input);
		String[] words = input.split(" ");
		int N=moviesTable.size();
		
		//calculates all tf-idf values and puts them in the scores hashMap
		for(String token:words){
			token=cleanString(token);
			//System.out.println(tokensTable);
			if(!tokensTable.containsKey(token))continue;
			int tokenID=tokensTable.get(token);
			//System.out.println("tokenID: " + tokenID);
			Token thisToken = null;
			if(tokenID!=0)thisToken=tokensHashIndex.get(tokenID);
			
			//System.out.println("thisToken: " + thisToken);
			ArrayList<Occurences> postings=null;
			if(thisToken!=null){
				postings=thisToken.documents;
				//System.out.println("got postings for: " + token);
			}
			else continue;
			//calculate idf
			int numOfOccOfTerm=postings.size();
			double idf=Math.log10(1+N/numOfOccOfTerm);
			//System.out.println("postings for " + token +" are: " + postings);
			for(Occurences entry:postings){
//--				int numOfAppearancesInDoc=entry.occurences.size();
				int numOfAppearancesInDoc=entry.numOfOccurencesInDoc;
				int movieID=entry.docID;
				int docLength=moviesDescrLength.get(movieID);
				double tf=Math.log10(1+numOfAppearancesInDoc);
				if(scores.containsKey(entry.docID)){
					scores.put(movieID, scores.get(movieID)+tf*idf);
				}
				else scores.put(movieID, tf*idf);
			}
		}
		
		System.out.println(scores);
		printNthBestMovies(3, words);
//		Collection<Integer> movies = scores.keySet();
//		List<Integer> moviesSorted = asSortedList(movies);
//		for(int i=0;i<10;i++){
//		for(String moviename:moviesTable.keySet()){
//			if(scores.keySet().contains(moviesTable.get(moviename))){
//				printMovie(moviesTable.get(moviename)+1,words);
//				
//			}
//		}
//		}
		scores.clear();
		
		
		
		
	}
	public static void printNthBestMovies( int N, String[] words){
		System.out.println(words);
		for(String word:words)System.out.println(word);
		ArrayList<Integer> bestN=new ArrayList<Integer>(Collections.nCopies(N, 0));
		Set<Integer> moviesIDSet=scores.keySet();
		List<Integer> sortedIDSet = asSortedList(moviesIDSet);
		
		//for(int i=0;i<N;i++)bestN.add(sortedIDSet.get(i));
		
		Collections.sort(sortedIDSet,new Comparator<Integer>(){
			   @Override
			   public int compare(final Integer lhs,Integer rhs) {
			     //TODO return 1 if rhs should be before lhs 
			     //     return -1 if lhs should be before rhs
			     //     return 0 otherwise
				   double leftscore=scores.get(lhs);
				   double rightscore=scores.get(rhs);
				   if(rightscore<leftscore) return 1;
				   if(rightscore>leftscore) return -1;
				   return 0;
			     }
			 });
		for(int i=0;i<N;i++){
				printMovie(sortedIDSet.get(i)+1,words);
		}



	}
	
	public static void printMovie(int movieID,String[] words){
		System.out.println("---------------------------------");
		try{
		BufferedReader br= new BufferedReader(new FileReader("textfiles/movies/movie" + movieID));
		String line=null;
		while ((line = br.readLine()) != null){
			System.out.println(line);
			
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void printIndex(){
		Set<Integer> keys=tokensHashIndex.keySet();
		for(Integer key :keys){
			Token currentToken=tokensHashIndex.get(key);
			currentToken.print();
			System.out.println();
		}
	}
	//serializes all tables
	public static void serializeTables(){
		try{
	         FileOutputStream fos1= new FileOutputStream("1000tokens.serialized");
	         ObjectOutputStream oos1= new ObjectOutputStream(fos1);
	         oos1.writeObject(tokensHashIndex);
	         oos1.close();
	         fos1.close();
	         FileOutputStream fos2= new FileOutputStream("movieStable.serialized");
	         ObjectOutputStream oos2= new ObjectOutputStream(fos2);
	         oos2.writeObject(moviesTable);
	         oos2.close();
	         fos2.close();
	         FileOutputStream fos3= new FileOutputStream("tokensTable.serialized");
	         ObjectOutputStream oos3= new ObjectOutputStream(fos3);
	         oos3.writeObject(tokensTable);
	         oos3.close();
	         fos3.close();
	         FileOutputStream fos4= new FileOutputStream("moviesLenght.serialized");
	         ObjectOutputStream oos4= new ObjectOutputStream(fos4);
	         oos4.writeObject(moviesDescrLength);
	         oos4.close();
	         fos4.close();
	       
	        
	       }catch(IOException ioe){
	            ioe.printStackTrace();
	        }

		
	}
	//generate all tokens for a single file, then insert them into the hash table
	public static void generateTokens(String filename) throws MalformedURLException{
		String wnhome = "/home/aser93/workspace/WordNet-3.0/";
		String path = wnhome + File . separator + "dict/";
		URL url = new URL ("file", null , path );
		IDictionary dict = new Dictionary ( url ) ;
		try {
			dict.open() ;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// look up first sense of the word "dog "
		IIndexWord idxWord = dict.getIndexWord ("sword", POS . NOUN ) ;
		IWordID wordID = idxWord . getWordIDs () . get (0) ; // 1st meaning
		IWord word = dict . getWord ( wordID ) ;
		ISynset synset = word . getSynset () ;
		
		// iterate over words associated with the synset
		for( IWord w : synset . getWords () )
		System .out.println ( w . getLemma () ) ;

		try {
		BufferedReader br= new BufferedReader(new FileReader(filename));
		
		String line;
	    String title = br.readLine();
	    int tokensSize=tokensTable.size();
	    int wordCount=0;
	    //insert movie into hash table since it's the first occurence
	    int moviesSize=moviesTable.size();
	    int movieID=moviesSize;
	    moviesTable.put(title,moviesSize);
	    
	    HashMap<Integer,Occurences> occurencesInDescription=new HashMap<Integer,Occurences>(50);
	    //for each line
		while ((line = br.readLine()) != null) {
			String[] tokens=line.split(" ");
			//for every token - put in the table, clear up the token and insert it 
			for(String token:tokens){
				
				
				int tokenID=0;
				
				//sanitize token
				token=cleanString(token);
				
				//check if empty or a stop word
				if(token==""||token==" ")continue;
				if(stopWords.contains(token)){wordCount++;continue;}
				//check if token is new
				if(!tokensTable.containsKey(token)){
					//if token is new, create it's ID and add to table
					tokenID=tokensSize;
					tokensTable.put(token, tokensSize);
					//System.out.println("putting token:" + token + " with ID:" + tokensSize);
					Token newToken=new Token(tokenID);
					tokensHashIndex.put(tokenID, newToken);
// --				Occurences oc=new Occurences(movieID,wordCount);
					Occurences oc=new Occurences(movieID,1);
					occurencesInDescription.put(tokenID, oc);
					
					tokensSize++;
				}
				//if token already exists
				else{
					tokenID=tokensTable.get(token);
					//if there are no occurences of the token - create one for it and add to table
					if(!occurencesInDescription.containsKey(tokenID)){
//--				Occurences oc=new Occurences(movieID,wordCount);
						
						Occurences oc=new Occurences(movieID,1);
						occurencesInDescription.put(tokenID, oc);
//						System.out.println("occurences for " + token + "became " + 
//						occurencesInDescription.get(tokenID).numOfOccurencesInDoc);

					}
					///if there are - update the occurences list with the new position
					else{
//--					occurencesInDescription.get(tokenID).addOccurence(wordCount);
						occurencesInDescription.get(tokenID).increaseOccurences();
//						System.out.println("occurences for " + token + "became " + 
//						occurencesInDescription.get(tokenID).numOfOccurencesInDoc);
					}
					
				}
				wordCount++;
				//add all of the generated occurences to the Tokens in the global index
				Set<Integer> keys=occurencesInDescription.keySet();
				for(Integer key :keys){
					Token currentToken=tokensHashIndex.get(key);
					Occurences oc=occurencesInDescription.get(key);
					currentToken.addOccurence(oc);
				}
				

				
				
			}
			
		}
		br.close();
		moviesDescrLength.put(movieID, wordCount);
		

		}  catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("done with: " + filename);
		
		
		
		
	}
	
	public static void deserialize(String filename){
		try
        {   
            // Reading the objects from a file
            FileInputStream file1 = new FileInputStream(filename);
            ObjectInputStream in1 = new ObjectInputStream(file1);
            tokensHashIndex = (HashMap<Integer,Token>)in1.readObject();
            System.out.println("Tokens index deserialized.");
            in1.close();
            file1.close();
            FileInputStream file2 = new FileInputStream("movieStable.serialized");
            ObjectInputStream in2 = new ObjectInputStream(file2);
            moviesTable = (HashMap<String,Integer>)in2.readObject();
            System.out.println("Movies table deserialized.");
            in2.close();
            file2.close();
            FileInputStream file3 = new FileInputStream("tokensTable.serialized");
            ObjectInputStream in3 = new ObjectInputStream(file3);
            tokensTable = (HashMap<String,Integer>)in3.readObject();
            System.out.println("Tokens table deserialized.");
            in3.close();
            file3.close();
            FileInputStream file4 = new FileInputStream("moviesLenght.serialized");
            ObjectInputStream in4 = new ObjectInputStream(file4);
            moviesDescrLength = (HashMap<Integer,Integer>)in4.readObject();
            System.out.println("Movies length table deserialized.");
            in4.close();
            file4.close();
             
            
            
            
            
            
            
            //printIndex();
        }
         
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
         
        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }
	}

	
	public static String cleanString(String inputString){
		String outputString=inputString.trim();
		outputString=outputString.replaceAll("[-+.\"^:,']","");
		outputString=outputString.toUpperCase();
		return outputString;
	}
	
	public static void createStopTable(String filename){
		try{
			BufferedReader br= new BufferedReader(new FileReader(filename));
			String line=null;
			while ((line = br.readLine()) != null) {
				line=cleanString(line);
				stopWords.add(line);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}


