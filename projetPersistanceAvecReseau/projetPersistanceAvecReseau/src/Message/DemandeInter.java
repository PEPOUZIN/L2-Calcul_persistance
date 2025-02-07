package Message;

import java.io.*;
import java.math.BigInteger;


public class DemandeInter implements Serializable {
	
	public  boolean ok;
    

	public DemandeInter() {
		
		ok=true;
        

	}
   public boolean getOk() {
	   return this.ok;
   }
   public void setOk(boolean kk) {
	   this.ok=kk;
   }
}