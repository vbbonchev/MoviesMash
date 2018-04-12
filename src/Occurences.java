
import java.io.Serializable;
import java.util.ArrayList;
public class Occurences implements Serializable{

	int docID;
	int numOfOccurencesInDoc=0;
	//public ArrayList<Integer> occurences=new ArrayList<Integer>(5);
	
//	Occurences(int _docID,int wordNum){
//		docID=_docID;
//		occurences.add(wordNum);
//	}
	Occurences(int _docID,int _wordCount){
		docID=_docID;
		numOfOccurencesInDoc=_wordCount;
		
	}
	
//	public void addOccurence(int wordNum){
//		occurences.add(wordNum);
//	}
	
	public void increaseOccurences(){
		numOfOccurencesInDoc++;
	}

}
