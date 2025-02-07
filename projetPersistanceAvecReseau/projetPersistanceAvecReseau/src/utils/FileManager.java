package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**  Contient 7 fonctions : 
 *  	checkDirectory( String pPath )
 *      checkFile(final String pParent, final String pName)
 * 		serializeList(final String pPath,final ArrayList<String> toWrite)
 * 		readLog(final String pPath)
 * 		getSingleResFromFile(final BigInteger NUMBER, final BigInteger INTERVAL, final String parentPath)
 * 		getResFromSingleFile( final BigInteger pMIN, final BigInteger pMAX, final BigInteger INTERVAL, final String parentPath)
 * 		getResFromFiles(final BigInteger pMIN,final BigInteger pMAX, final BigInteger INTERVAL, final String parentPath )
 */
public class FileManager {

	public FileManager() {}	
	
	// fonction pour creer | remplacer un dossier 
	public String checkDirectory(String pPath){	
		
		Path toPath   = Paths.get(pPath);	// chemin du dossier a creer
		
		try{                                         
			
			// si le dossier n'existe pas, on le cree | et si il existe, on le remplace
			toPath = Files.createDirectories(toPath);	 
			
			System.out.println("Directory created at: " + toPath.toString());
				
		// ou cas ou il y a une erreur	
		}catch(IOException e) {                     
			e.printStackTrace();
		}
		
		// retourne le chemin du dossier sous forme de String
		return toPath.toString();                 
		
	}
	
	// fonction qui va creer le fichier log et retourner son chemin
	public String checkFile(final String pParent, final String pName) {
				
		// on cree un chemin de .txt : pParent/pName 
		Path pathToLog = Paths.get(pParent + "/" + pName + ".txt" ); 
		
		try{                                        
			
			// si il existe deja, on le supprime
			Files.deleteIfExists(pathToLog);
			
			// on cree le fichier a l'emplacement demande
			pathToLog = Files.createFile(pathToLog);
							
			System.out.println("File created at: " + pathToLog.toString());
			
		// ou cas ou il y a une erreur
		}catch(IOException e) {                     
			e.printStackTrace();
		}
		
		// retourne le chemin en String
		return pathToLog.toString();
	}
	
	// fonction pour serialiser les resultats
	public void serializeList(final String pPath,final ArrayList<String> toWrite){	
				
		try {
			
			FileOutputStream fos = new FileOutputStream( pPath );
			
			ObjectOutputStream serializer = new ObjectOutputStream( new BufferedOutputStream( fos ) );
			
			serializer.writeObject(toWrite);
			
			serializer.close();
			
			fos.close();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
			
	}
	
	// fonction pour deserialiser les resultats 
	@SuppressWarnings("unchecked")
	public ArrayList<String> readLog(final String pPath){
		
		ArrayList<String> list = null;
		
		try {
			
			FileInputStream fis = new FileInputStream(pPath);
			
			ObjectInputStream deserializer = new ObjectInputStream( new BufferedInputStream( fis ) );
			
			list = ( ArrayList<String> ) deserializer.readObject();
						
			deserializer.close();
			
			fis.close();
			
		}catch(IOException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}		
		
		return list;		
	}
	
	// OK
	// fonction pour retourner un seul resultat depuis une log
	public String getSingleResFromFile(final BigInteger NUMBER, final BigInteger INTERVAL, final String parentPath){
		
		// liste des resultats
		ArrayList<String> list = new ArrayList<>();
		
		// resultat
		String res = "";
		
		// si le resultat demande est dans la premiere log
		if( NUMBER.compareTo( INTERVAL ) == -1 || NUMBER.equals(INTERVAL) ) {
			
			// on change la chemin de la log
			list = readLog( parentPath + "0-" + INTERVAL + ".txt" );			
			
			// du fait que les resultats de la log commencent a 0, l'index du resultat est le nombre lui meme
			res = list.get( NUMBER.intValue() );		
			
			// on vide la liste
			list.clear();
			
			return res;
			
		}else {
			
			BigInteger quotientMIN = BigInteger.ZERO;
			
			// si le nombre est un multiple de INTERVAL
			if( NUMBER.mod( INTERVAL ).equals( BigInteger.ZERO ) ) {
				
				// on recupere le quotient de pMIN par INTERVAL, on enleve un, puis on multiplie par INTERVAL et + 1 pour obtenir le bon chemin de log
				quotientMIN = NUMBER.divide( INTERVAL ).subtract(BigInteger.ONE).multiply( INTERVAL ).add( BigInteger.ONE );
								
				// on recupere les resultats 
				list = readLog( parentPath + quotientMIN.toString() + "-" + quotientMIN.add( INTERVAL ).subtract( BigInteger.ONE ).toString()  + ".txt"  );
				
				// on prend le dernier element de la liste
				res = list.get( list.size() -1 );
				
			}else {
				
				// on recupere le quotient de pMIN par INTERVAL, puis on multiplie par INTERVAL et + 1 pour obtenir le bon chemin de log
				quotientMIN = NUMBER.divide( INTERVAL ).multiply( INTERVAL ).add( BigInteger.ONE );
				
				// on recupere les resultats 
				list = readLog( parentPath + quotientMIN.toString() + "-" + quotientMIN.add( INTERVAL ).subtract( BigInteger.ONE ).toString()  + ".txt"  );
				
				// on recupere le modulo de NUMBER par INTERVAL, pour obtenir l'index du resultat
				res = list.get( NUMBER.mod(INTERVAL).intValue() - 1 );
				
			}
			
			// on vide la liste
			list.clear();
			
			return res;		
	 		
		}
	}
	
	// OK 
	// fonction pour retourner une ArrayList de resultats contenus dans une log
	public ArrayList<String> getResFromSingleFile( final BigInteger pMIN, final BigInteger pMAX, final BigInteger INTERVAL, final String parentPath){
		
		// liste des resultats
		ArrayList<String> list = new ArrayList<>();
		
		BigInteger quotientMIN = BigInteger.ZERO;
		
		// si on est dans la 1ere log
		if( pMIN.divide(INTERVAL).equals( BigInteger.ZERO ) ) {
					
			// on recupere la liste des resultats;
			list = readLog( parentPath + "0-" + INTERVAL.toString() + ".txt" );
			
			// on supprime les resultats en dessous de pMIN | si pMIN = 0, cela ne fait rien 
			for( int i = 0 ; i < pMIN.intValue() ; i++ ) {
				list.remove( 0 ); 				
			}
						
		// si on est pas dans la premiere log
		}else {
			
			// on recupere le quotient de pMIN par INTERVAL, puis on multiplie par INTERVAL et + 1 pour obtenir le bon chemin de log
			quotientMIN = pMIN.divide( INTERVAL ).multiply( INTERVAL ).add( BigInteger.ONE );
			
			// on recupere la liste des resultats
			list = readLog( parentPath + quotientMIN.toString() + "-" + quotientMIN.add(INTERVAL).subtract( BigInteger.ONE ).toString() + ".txt"  );
			
			// on supprime les resultats en dessous de pMIN | si pMIN = 0, cela ne fait rien 
			for( int i = 0 ; i < pMIN.subtract(quotientMIN).intValue() ; i++ ) {
				list.remove( 0 ); 				
			}
											
		}
		
		// tant que pMAX est different du dernier element de la liste
		while( !( pMAX.equals( new BigInteger( list.get( list.size() - 1 ).split(":")[0] ) ) ) ){
			
			// on supprime le dernier element
			list.remove( list.size() - 1 );
			
		}
		
		return list;
	}
	
	// NON TESTE
	// fonction pour retourner une ArrayList de resultats contenus dans plusieurs logs ou qui appelle les autres fonctions du meme type
	public ArrayList<String> getResFromFiles(final BigInteger pMIN,final BigInteger pMAX, final BigInteger INTERVAL, final String parentPath ) {
		
		ArrayList< String > list = new ArrayList<>();
				
 		// if / else pour trouver des erreurs potentielles 
 		
			// si l'intervalle demande est plus grand que la taille max des ArrayList ==> erreur
	 		if( pMAX.subtract( pMIN ).compareTo( BigInteger.valueOf( 2147483647 ) ) == 1 ) {
	 			
	 			System.out.println("The requested interval is too big, it has to be smaller than 2 147 483 647 .");
	 			return null;
	 			
	 		// sinon si pMAX < pMIN ==> erreur
	 		}else if( pMAX.compareTo(pMIN) == -1 ) {
	 			
	 			System.out.println("The requested interval is not valid.");
	 			return null; 			
	 		
	 		}
 		
 		// obtention des resultats
	 		
	 		// si l'intervalle ne contient qu'une valeur ou que pMIN = pMAX
	 		if( pMAX.subtract( pMIN ).equals( BigInteger.ONE ) || pMAX.equals( pMIN ) ) {
	 			
	 			// on appelle la fonction qui recupere UN nombre depuis UNE log
	 			list.add( getSingleResFromFile( pMIN , INTERVAL , parentPath) );
	 			return list;
	 			
	 		// si l'intervalle est contenu dans un seul fichier
	 		}else if( ( pMIN.divide(INTERVAL).equals( pMAX.divide(INTERVAL) ) ) ) { 			
	 			
	 		// on appelle la fonction qui recupere un intervalle depuis UNE log
				list = getResFromSingleFile( pMIN , pMAX , INTERVAL , parentPath);
				return list;			
	 			
	 		// si l'intervalle est contenu dans plusieurs fichiers	 			
	 		}else {
	 			
	 			BigInteger quotientMIN = BigInteger.ZERO;
	 			
	 			// on recupere le premier intervalle dans le premier fichier
	 			list = getResFromSingleFile( pMIN , INTERVAL , INTERVAL , parentPath);
	 			
	 			// pMIN est congru a 0 modulo INTERVAL
	 			if( pMIN.divide( INTERVAL ).equals( BigInteger.ZERO )) {
	 				
		 			// on recupere le quotient de pMIN par INTERVAL, puis on multiplie par INTERVAL pour obtenir le bon chemin de log
		 			quotientMIN = pMIN.divide( INTERVAL ).multiply( INTERVAL );
	 				
	 			}else {
	 				
	 				// on recupere le quotient de pMIN par INTERVAL, puis on multiplie par INTERVAL et + 1 pour obtenir le bon chemin de log
		 			quotientMIN = pMIN.divide( INTERVAL ).multiply( INTERVAL ).add( BigInteger.ONE );
	 				
	 			} 
	 			
	 			// on incremente quotientMIN avec INTERVAL + 1 car les prochains fichiers ont un debut de nom finissant par 1 
	 			quotientMIN = quotientMIN.add(INTERVAL).add( BigInteger.ONE );
	 			
	 			// pour toutes les logs entre celle de debut et celle de fin
	 			for( int i = 0 ; i < pMAX.divide( INTERVAL ).subtract( pMIN.divide( INTERVAL ) ).subtract( BigInteger.ONE ).intValue() ; i++ ) {				
	 				
	 				// on ajoute tout le contenu de cette log dans la liste
	 				list.addAll( readLog( parentPath + quotientMIN.toString() + "-" + quotientMIN.add(INTERVAL).subtract( BigInteger.ONE ).toString() + ".txt" ) );
	 				
	 				// on incremente quotientMIN
	 				quotientMIN = quotientMIN.add(INTERVAL);
	 					 				
	 			} 					
	 			
	 			// on ajoute les derniers resultats demandes a la liste
	 			list.addAll( getResFromSingleFile( pMAX.divide( INTERVAL ).multiply( INTERVAL ) , pMAX , INTERVAL , parentPath) );
				return list;
	 		} 		
 		
	}
		
}
