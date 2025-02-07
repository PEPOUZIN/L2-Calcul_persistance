package Message;

import java.io.*;
import java.util.ArrayList;

/**
 * Contributeurs : Eric Leclercq, Annabelle Gillet
 */
public class Message implements Serializable {
	
	private ArrayList< ArrayList < String > > liste;
	private ArrayList < String >  pMin;
	private ArrayList < String >  pMAx;	
	
	public ArrayList<ArrayList<String>> getList(){
		return this.liste;
	}
	
	public ArrayList<String> getListeMin(){
		return this.pMin;
	}
	public ArrayList<String> getListMax(){
		return this.pMAx;
	}

	public Message( ArrayList<ArrayList< String >> list, ArrayList< String > Min, ArrayList< String > MAx){
	
		this.liste=list;
		this.pMAx=MAx;
		this.pMin=Min;

	}
}