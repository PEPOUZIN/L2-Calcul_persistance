package server;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Hashtable;

import utils.FileManager;
import worker.Worker;



public class Server extends Thread{

    private Hashtable< Byte , BigInteger > occurences;    // Byte au lieu de Integer car byte => 1 octet alors que int => 4
    
    private ArrayList< String > highestPers; 		// liste des nombres avec la plus grande persistance   
    
    private ArrayList< Worker > workers;      		// liste des workers      
    
    private final byte          cores;              // nombre de coeurs disponibles | byte => 1 octet alors que int => 4
    private byte                perstMax;           // persistance max trouvee      | byte car en theorie perstMax < 20
    
    private float               median;    			// mediane des persistances     
    
    private final BigInteger    INTERVAL;           // taille des intervalles envoyes aux workers   
    private final BigInteger    MIN;                // minimum de l'intervalle total
    private final BigInteger    MAX;				// maximum de l'intervalle total
    private       BigInteger    nbElem;             // nombre de nombres calcules         | utilise pour les stats
      
    private       BigDecimal    sum;				// somme de toute les persistances    | utilise pour les stats

    private       BigDecimal    average;			// moyenne de toutes les persistances | utilise pour les stats
    
    private FileManager         fileHandler;        // pour gerer les fichiers et dossiers des logs
    
    private final String              logsPath;           // chemin de la log principiale
    
    private boolean finished;                // pour mettre en pause l'actualisation des stats dans le client   
    
	public Server() {
		
		this.occurences  = new Hashtable<>();
		
		this.workers     = new ArrayList<Worker>();		
		this.highestPers = new ArrayList<String>();
		
		this.cores       = ( byte )Runtime.getRuntime().availableProcessors(); 
		this.perstMax    = 0;
		this.median      = 0;
		
		this.INTERVAL    = new BigInteger("4000000");
		this.MIN  		 = new BigInteger("0"); 
		this.MAX 		 = new BigInteger( this.INTERVAL.multiply( BigInteger.valueOf( this.cores*7 ) ).toString() );
		this.nbElem      = BigInteger.ZERO;
		
		this.sum         = BigDecimal.ZERO;
		this.average     = BigDecimal.ZERO;
		
		this.fileHandler = new FileManager();	
		
		this.logsPath    = "Logs/Logs_" + this.MIN.toString() + "-" + this.MAX.toString() + "/";
                
		this.finished = false;          
		
	}
	
        
	public float returnMedian() {
		return this.median;
	}
	
	public BigDecimal returnAverage() {
		return this.average;
	}

	public byte returnPerstMax() {
		return this.perstMax;
	}
        
        public Hashtable< Byte , BigInteger > returnOccurences(){
            return this.occurences;
        }
	
        public ArrayList< String > returnHighPerst(){
            return this.highestPers;
        }
        
        public boolean returnFinished(){
            return this.finished;
        }
           
        public String returnLogsPath(){
            return this.logsPath;
        }
       
	// OK
 	// fonction qui actualise le nombre d'occurrences par valeur de la persistance
 	public void gatherOccurrences( ArrayList<String> list) {
 		
 		// variable pour stocker le resultat
 		byte resInter;
 		
 		// pour tout les String de la list
 		for( String s : list ) {
 			
 			// on recupere la persistance
 			resInter = (byte)Integer.parseInt( s.split(":")[1] );
 			
 			// si on a deja rencontre cette persistance
 			if( this.occurences.containsKey( resInter ) ) {
 				
 				// on incremente la valeur associe
 				this.occurences.replace( resInter , this.occurences.get( resInter ).add( BigInteger.ONE ) );
 				 				
 			}else {
 				
 				// on ajoute une "ligne" ayant pour key la persistance et value 1
 				this.occurences.put( resInter , BigInteger.ONE );
 				
 			}
 					
 		}
		
 	}
 	 	
 	// OK
 	// fonction qui retourne une arraylist d'un intervalle de resultats
 	public ArrayList<String> getResultsInterv( final BigInteger pMIN, final BigInteger pMAX ){
 		
 		// on retourne une ArrayList<String> de resultats renvoyee par la fonction getResFromFiles
 		return this.fileHandler.getResFromFiles(pMIN, pMAX, this.INTERVAL , this.logsPath);
 		
 	}
		
 	// OK
	// fonction qui met dans highestPers les nombres ayant la plus grande persistance
	public void gatherPerstMax( ArrayList<String> pList) {
		
		// pour tout les string de pList
		for( String s : pList ) {
					
			// si la persistance du nombre est = a this.perstMax
			if( Integer.parseInt( s.split(":")[1] ) == this.perstMax ) {
				
				// on ajoute le nombre dans la liste des plus grandes persistances
				this.highestPers.add( s );				
			
			// si la persistance du nombre est > a this.perstMax	
			}else if( Integer.parseInt( s.split(":")[1] ) > this.perstMax ) {
				
				// on vide la liste des plus grandes persistances
				this.highestPers.clear();
				
				// on change la valeur de la plus grande persistance
				this.perstMax = ( byte ) Integer.parseInt( s.split(":")[1] );
				
				// on ajoute le nombre dans la liste des plus grandes persistances
				this.highestPers.add( s );
				
			}		
		}
	}	
        
	// OK
	// fonction qui retourne la moyenne des persistances
	public void gatherAverage( ArrayList<String> list ){
		
		// pour tout les string de list
		for( int i = 0 ; i < ( list.size() - 1 ) ; i++ ) {
			
			// on augmente le nombre d'element constituant la moyenne
			this.nbElem = this.nbElem.add( BigInteger.ONE );					
			
			// on ajoute la persistance a la somme en separant la persistance du nombre
			this.sum = this.sum.add( BigDecimal.valueOf( Integer.parseInt( list.get(i).split(":")[1] ) ) );
			
			// on divise la somme par le nombre d'elements ( le diviseur , la precision de resultat , le mode d'arrondissement )
			this.average = this.sum.divide( new BigDecimal( this.nbElem ), 2 , RoundingMode.CEILING ); 
						
		}
	
	}
	
	// OK
	// fonction qui retourne la mediane des persistances
	public void gatherMedian() {
		
		// si le nombre de persistances diferentes est pair
		if( this.occurences.size() % 2 == 0) {
			
			// la mediane vaut la moyenne des valeurs de rang this.occurences.size() / 2.0 et (  this.occurences.size() / 2.0 + 1 )
			this.median = (float) ( (  ( this.occurences.size() / 2.0 )  +  ( this.occurences.size() / 2.0 + 1 )  ) / 2.0 ) ;
			
		}else {
			
			// sinon la mediane vaut la valeur se trouvant au centre
			this.median = this.occurences.size() / 2;			
					
		}	
				
	}
	
	// OK
	// fonction qui retourne la persistance d'un nombre	
	public int getPerstNb(final BigInteger pNb) {
		
		// on return seulement la persistance en separant le string donne par la fonction
		return Integer.parseInt( this.fileHandler.getSingleResFromFile(pNb, this.INTERVAL , this.logsPath).split(":")[1] );
		
	}
	
	// OK
	// fonction pour savoir si tout les threads sont terminated
 	public boolean areTerminated() {
		
 		for(Worker w : this.workers) {
 			
 			// si le thread est en cours d'execution
			if( w.getState() == Thread.State.RUNNABLE )
				return false;
			
			// sinon si il est interrompu
			else if( w.isInterrupted() )
				System.out.println( "Thread " + w.getId() + " has been interrupted" ); 			
 		}
 		
		return true;		
	}
 	
 	// OK
	// fonction pour lancer le serveur
	@Override
        public void run(){
            
            	BigInteger pMIN = BigInteger.ZERO;       // borne minimale de l'intervalle envoye
		BigInteger pMAX = this.INTERVAL;		 // borne maximale de l'intervalle envoye
		
		boolean  one    = true;					 // boolean pour ajouter un a pMIN
		boolean  ready  = true;                  // booleen pour savoir si les workers ont fini
                
		this.fileHandler.checkDirectory("Logs"); // on verifie le dossier Logs
		
		// verification du dossier qui va contenir les multiples logs
		this.fileHandler.checkDirectory( this.logsPath );

		// fichier dans lequel on serialise
		String file = "";
		
	    try {
	    	
	    	// tant que this.MAX > pMAX 
	    	while( this.MAX.compareTo(pMAX) == 1 ){                  
                    
	    		// si on est pret a lancer les workers
	    		if(ready) { 
	    			
	    			// on vide la liste des workers
	    			this.workers.clear();
	    			
			    	// on cree autant de worker que de cores
				    for(int i = 0 ; i < this.cores ; i++ ) {			    	
				    	
				    	// worker avec comme intervalle [ pMIN ; pMAX ]
				    	workers.add( new Worker( pMIN,pMAX ) );
				    	
				    	System.out.println("Worker " + workers.get(i).getId() + " has started");
				    	
				    	workers.get(i).start();
				    	
				    	// si on doit ajouter un. On le fait comme ca aucune valeur n'est calculee 2x
				    	if(one) {
				    		pMIN = pMIN.add(this.INTERVAL).add( BigInteger.ONE );
				    		one = false;
				    	// sinon on ajoute juste l'intervalle	
				    	}else {
				    		pMIN = pMIN.add(this.INTERVAL);	
				    	}
			    		
				    	pMAX = pMAX.add(this.INTERVAL); 
				    	
				    }				    
				    ready = false;				    
	    		}
	    		
	    		// si les calculs sont finis 
    			if( areTerminated()  ) {
			 	
                            
                            
			    	// pour tous les workers
			    	for(Worker w : this.workers) { 
			    		
			    		gatherAverage(w.getList());
			    		
			    		gatherOccurrences(w.getList());
			    		
			    		gatherPerstMax(w.getList());
			    		
			    		gatherMedian();			    		
			    	
			    		// on cree un string avec un path de la forme Logs/Logs_this.MIN-this.MAX/w.getMIN()-w.getMAX().txt
			    		file = this.fileHandler.checkFile( this.logsPath  , w.getMIN().toString() + "-" + w.getMAX().toString() );
			    		
			    		// on serialise la liste des resultats dans file
			    		this.fileHandler.serializeList( file , w.getList() );
			    		
			    	}
                                
                                ready = true;
			    				    	
	    		}  
	    		
	    	}
	    	
	    	/*	on boucle tant que on n'a pas serialise les derniers resultats.
	    	 * 	on doit refaire un if ( areTerminated() ) car vu qu'a la fin du while precedent,
	    	 *  this.MAX.compareTo(pMAX) == 1 devient false, ducoup cela n'imprime pas les derniers resultats
	    	 */ 
	    	while(!ready) {
	    
                    // si les calculs sont finis
                    if( areTerminated()  ) {

                        // pour tous les workers
                        for(Worker w : this.workers) { 

                            gatherAverage(w.getList());

                            gatherOccurrences(w.getList());

                            gatherPerstMax(w.getList());

                            gatherMedian();			    				

                            // on cree un string avec un path de la forme Logs/Logs_this.MIN-this.MAX/w.getMIN()-w.getMAX().txt
                            file = this.fileHandler.checkFile( this.logsPath  , w.getMIN().toString() + "-" + w.getMAX().toString() );

                            // on serialise la liste des resultats dans file
                            this.fileHandler.serializeList( file , w.getList() );

                        }	

                        // on vide la liste des workers
                        this.workers.clear();

                        ready = true;

                    }	    	
	    	}
	    
	    	this.workers.clear();    	
	    		    	
	    	System.out.println("\nEnd of execution");
	    			
                this.finished = true;
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
            
            
        }
	
	
}
