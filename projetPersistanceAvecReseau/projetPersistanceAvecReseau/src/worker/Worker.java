package worker;

import java.math.BigInteger;
import java.util.ArrayList;

/**  Contient 5 fonctions : 
 *  	getList()
 *  	getMIN()
 *  	getMAX()
 *  	persistence( final BigInteger MIN ,final BigInteger MAX )
 *  	run()
 */
public class Worker extends Thread {
	
		private final BigInteger        MIN;         // minimum de l'intervalle a calculer en const
		private final BigInteger        MAX;         // idem pour le max ( const car on ne les modifie pas )
		
		private       ArrayList<String> list;        // on stocke les resultats dans une arraylist
		
		// on initialise MIN et MAX avec les valeurs envoyees par le serveur
 		public Worker(final BigInteger pMIN,final BigInteger pMAX) {
			this.MIN  = pMIN;
			this.MAX  = pMAX;
			this.list = new ArrayList<String>();
		}	
			
		// retourne la liste des resultats pour le serveur
		public ArrayList<String> getList(){
			return this.list;
		}
				
		// retourne le minimum de l'intervalle specifie
		public BigInteger getMIN() {
			return MIN;
		}
		
		// retourne le minimum de l'intervalle specifie
		public BigInteger getMAX() {
			return MAX;
		}
  
		// fonction pour calculer la persistance
		private void persistence( final BigInteger MIN ,final BigInteger MAX ){
				
			int         persistance = 0;                    	     // persistance du nombre           
			BigInteger  iterations  = BigInteger.ONE.multiply(MAX).add(BigInteger.ONE); // nb d'iterations. On fait + 1 car dans les boucles on commence a 0
			final BigInteger offSet = MIN;                           // un offset pour ne pas commencer a 0; pas de modif = const
			BigInteger  nbInt	    = offSet;                        // un BigInteger initialise a la valeur de l'offset
			BigInteger  nbInter     = BigInteger.ONE;                // un BigInteger initialise a 1
			
			String      nb          = nbInt.toString();              // nbInt mais en type String ( pour les calculs )
			
			// tant que le nombre d'iterations restantes est > 0 
			while( iterations.subtract(offSet).compareTo(BigInteger.ZERO) == 1 ) {  
				// la taille du nombre intermediaire diminue : on boucle tant que taille > 1 
				while( nb.length() > 1 ){  
					// on parcours tout le nombre nb
					for( int j = 0 ; j < nb.length()  ; j++ ) {      
						//nbInter = nbInter * ( nb.charAt(i) ) ; 
						nbInter = nbInter.multiply( BigInteger.valueOf( Character.getNumericValue( nb.charAt( j ) ) ) );
					}
					// on met le nombre a calculer a la valeur intermediaire
					nb = nbInter.toString();     
					// reinitialisation du nombre intermediaire
					nbInter = BigInteger.ONE; 
					 // incrementation de la persistance de ce nombre
					persistance++;                                 
					
				}
										
				this.list.add( nbInt.toString() + ":" + persistance );
				// on incremente le nombre de base
				nbInt = nbInt.add( new BigInteger("1") );	
				// on met dans nb la conversion de nbInt en String
				nb = nbInt.toString(); 
				// on reset la persistance
				persistance = 0;                                     
				// on decremente le nombre d'iterations restantes
				iterations = iterations.subtract(BigInteger.valueOf(1));  
			}
						
		}		
		
		@Override
		public void run() {
						
			// calculs et sauvegarde les resultats dans la liste
			persistence(MIN,MAX);
			
		}
			
}

















