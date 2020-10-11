
package com.example.voiceme;

import android.util.Log;

/**
 *
 * @author elsakrismonti@gmail.com
 */
public class MasseyOmura {
    
    public int p;
    private int e, d;

    //Constructor
    public MasseyOmura() {
        p = generatePrime();
        this.e = Math.isCoPrime(p);
        this.d = Math.modularInverse(e, p);
        Log.d("TAG", "p : " + p +" e :" + e +" d:" + d);
//        System.out.println("p : " + p +" e :" + e +" d:" + d);
    }
    		
    //Constructor with argument
    public MasseyOmura(int p){
        if(p == 5) p = generatePrime();
        else this.p = p;
        this.e = Math.isCoPrime(p);
        this.d = Math.modularInverse(e, p);
        Log.d("TAG", "p : " + p +" e :" + e +" d:" + d);
//        System.out.println("p : " + p +" e :" + e +" d:" + d);
    }

    //Generate prime
    public int generatePrime(){
        do{
            p = Math.getRandomNumber(1000, 999999);
        } while(!Math.isPrime(p));        
        return p;
    }
    
    //Encryption
    public int[] encryption(int[] c){
        int[] temp = new int[c.length];
        for(int i = 0; i < c.length; i++){
            temp[i] = Math.modularExponentiation(c[i], e, p);
        }
        return temp;        
    }
    
    //Decryption
    public int[] decryption(int[] c){
        int temp[] = new int[c.length];
        for(int i = 0; i < c.length; i++){
            temp[i] = Math.modularExponentiation(c[i], d, p);
        }
        return temp;
    }
    
}
