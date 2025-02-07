package utils;

import client.ClientInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;

public class StatsClient extends Thread {
    
    private ClientInterface client;
    
    private Hashtable< Byte , BigInteger > occurences;  // Byte au lieu de Integer car byte => 1 octet alors que int => 4
    
    private ArrayList< String > highestPers; 		// liste des nombres avec la plus grande persistance
    
    private javax.swing.JTextArea jTAOccurences; 
    
    private javax.swing.JLabel AverageLocation;
    
    private javax.swing.JLabel MedianLocation;
    
    private javax.swing.JLabel highestPerstLocation;
    
    private javax.swing.JTextArea jTAHighPerst;
    
    private boolean stop;
    

    public StatsClient( ClientInterface p_client , javax.swing.JTextArea p_jTAOccurences , javax.swing.JLabel p_AverageLocation ,
                javax.swing.JLabel p_MedianLocation , javax.swing.JLabel p_highestPerstLocation , javax.swing.JTextArea p_jTAHighPerst  ){
        
        this.occurences = new Hashtable<>();

        this.highestPers = new ArrayList();

        this.client = p_client;
        
        this.jTAOccurences = p_jTAOccurences;
        
        this.AverageLocation = p_AverageLocation;
        
        this.MedianLocation = p_MedianLocation;
        
        this.highestPerstLocation = p_highestPerstLocation;
        
        this.jTAHighPerst = p_jTAHighPerst;
        
        this.stop = false;
        
    }
    
    // fonction pour actualiser les resultats affiches dans le client 
    public void printStats(){

       AverageLocation.setText(  this.client.getServer().returnAverage().toString() ) ;

       MedianLocation.setText( this.client.getServer().returnMedian() + "" );

       highestPerstLocation.setText( this.client.getServer().returnPerstMax() + "" );

       printOccurences();
       
       printHighestPerst();

    }
    
    // OK
    // fonction pour imprimer la Hashtable des occurences
    public void printOccurences() {

        this.jTAOccurences.setText("");
        
        this.occurences = this.client.getServer().returnOccurences();
        
        for( int i = 0 ; i < this.occurences.size() ; i++ ) {
            this.jTAOccurences.append("Persistance : " + i + " pour " + this.occurences.get( (byte) i) + " nombres.\n");
        }      
    }
    
    // OK
    // fonction pour imprimer la Hashtable this.occurences
    public void printHighestPerst() {

        jTAHighPerst.setText("");
        
        this.highestPers = this.client.getServer().returnHighPerst();
        
            for( int i = 0 ; i < this.highestPers.size() ; i++ ) {

                this.jTAHighPerst.append( "Persistance : " + this.highestPers.get( i ).split(":")[1] + " pour le nombre : " + this.highestPers.get( i ).split(":")[0] + "\n" );		

            }

    }
    
    @Override
    public void run(){
        
        // tant que le serveur n'a pas fini de calculer
        while(!this.stop){
            
            // on regarde si le serveur a fini
            this.stop = this.client.getServer().returnFinished();

            // on ecrit les stats
            printStats();

            // on met un petit temps d'attente
            try{
                this.sleep(400);
            }catch(Exception e){
                e.printStackTrace();
            } 

                      
        }
        
    }
    
    
    
    
    
    
    
    
}
