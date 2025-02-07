package Message;

import java.io.*;
import java.math.BigInteger;

/**
 * Contributeurs : Eric Leclercq, Annabelle Gillet
 */
public class Distribution implements Serializable {
	
	public BigInteger min;
    public BigInteger max;
    public BigInteger inter;
    public boolean ok;

	public Distribution(BigInteger p_MIN, BigInteger p_max,BigInteger p_interval) {
		
		this.min= p_MIN;
        this.max = p_max;
        this.inter = p_interval;
        ok=true;
    
    }
	public BigInteger getMin() {
    	return this.min;
    }
	public BigInteger getMax() {
    	return this.max;
    }
	public void setok(boolean k) {
		this.ok=k;
	}
    public boolean getok() {
    	return this.ok;
    }
    public BigInteger getInter() {
    	return this.inter;
    }
}