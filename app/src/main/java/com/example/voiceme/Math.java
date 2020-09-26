package com.example.voiceme;

import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author elsakrismonti@gmail.com
 */
public class Math {
    
    // Random any Integer    
    public static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
    
    // Decimal to Binary
    public static String decimalToBinnary(int e){
        String binary = "";
        while(e > 0){
            if(e % 2 == 0) binary = "0" + binary;
            else binary = "1" + binary;
            e = e / 2;
        }
        return binary;
    }  
    
    // Decimal to Binary
    public static String decimalToBinnaryFlag(int e){
        String binnary = "";
        while(e > 0){
            if(e % 2 == 0) binnary = "0" + binnary;
            else binnary = "1" + binnary;
            e = e / 2;
        }
        int legth = binnary.length();
        for(int i = 0; i < (8 - legth); i++) binnary = "0" + binnary;
        return binnary;
    }  
    
    //Binary to Decimal
    public static int binnaryToDecimal(String b){
        int dec_b = 0;
        int base = 1;
        for(int i = (b.length()-1); i >= 0; i--){
            if(b.charAt(i) == '1') dec_b += base;
            base = base * 2;
        }        
//        dec_b = Integer.parseInt(b, 2);
        return dec_b;
    }
    //Square and Multiple
    public static int modularExponentiation(int d, int e, int f){
        BigInteger m = new BigInteger(1 + "");
        BigInteger c = new BigInteger(d + "");
        BigInteger p = new BigInteger(f + "");
        String binary_e = decimalToBinnary(e);
        for(int i = 0; i < binary_e.length(); i++){
            if(binary_e.charAt(i) == '0' ) m = m.multiply(m).mod(p);
            else m = c.multiply(m).multiply(m).mod(p);
        }
        return Integer.valueOf(m.toString());
    } 

    //Agrawal-Biswas
    public static boolean isPrime(int p){
        int repeat = 3;
        while(repeat > 0){
            int z = getRandomNumber(1, (p-1));
            if(modularExponentiation((1+z), p, p) == 1 + (modularExponentiation(z, p, p)) && (p % 2 != 0 && p % 5 != 0))
                repeat--; 
            else 
                return false;      
        }
        return true;
    }
    
    // Find gcd
    public static int gcd(int a, int b) 
    { 
        if (b == 0) 
            return a; 
        return gcd(b, a % b);  
    }
    
    // Co prime with
    public static int isCoPrime(int p){
        int e;
        do{
            e = getRandomNumber(0, p);
        } while( e >= p || gcd(e, p-1) != 1);
        return e;
    }    
    
    // Inverse Modulo
    public static int modularInverse(int a, int b){
        BigInteger x1, y1, x2, y2, q, temp, modulo, p, e;        
        p  = new BigInteger(b +"");  
        p = p.subtract(BigInteger.ONE);
        modulo = p;        
        x1 = new BigInteger("1");
        y2 = new BigInteger("1");
        x2 = new BigInteger("0");
        y1 = new BigInteger("0");        
        e  = new BigInteger(a +"");        
        while (!p.equals(BigInteger.ZERO)){
            q = e.divide(p);
            temp = p;
            p = e.subtract(q.multiply(p));
            e = temp;
            temp = x2;
            x2 = x1.subtract(q.multiply(x2));
            x1 = temp;
            temp = y2;
            y2 = y1.subtract(q.multiply(y2));
            y1 = temp;
        }        
        int compareValue = x1.compareTo(BigInteger.ZERO);
        x1 = (compareValue == 1) ? x1 : modulo.add(x1);
        return Integer.valueOf(x1.toString());
    }    
    
    //Byte to int
    public static int[] byteToInt(byte[] b){
        int[] temp = new int[b.length];
        for(int i = 0; i< b.length; i++){
            temp[i] = (b[i] & 0xff);
        }
        return temp;
    }
    
    //Int to byte
    public static byte[] intToByte(int[] integer){
        byte[] temp = new byte[integer.length];
        for(int i = 0; i< integer.length; i++){
            temp[i] = (byte) integer[i];
        }
        return temp;
    }		
}
