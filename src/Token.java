import java.io.Serializable;
import java.util.*;
public class Token implements Serializable{


	
	public long ID;
	public ArrayList<Occurences> documents=new ArrayList<Occurences>(10);
	
	Token(int _ID){
		ID=_ID;
		
	}
	public void addOccurence(Occurences oc){
		documents.add(oc);
	}
	public void print(){
		System.out.println("Token ID:" + ID); 
		//System.out.println("occurences: " + documents);
	}
	
	//inserts another occurence
	public void updateOccurences(int _docID,int wordNum){
		for(Occurences oc:documents){
			if(oc.docID==_docID)oc.increaseOccurences();
//--			if(oc.docID==_docID)oc.addOccurence(wordNum);
		}
	}
	

}
