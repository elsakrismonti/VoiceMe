package com.example.voiceme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author elsakrismonti@gmail.com
 */
public class LevensteinCode {
    public static String compression(int[] m){

        Map<Integer, String> levensteinCodeTable = levensteinCodeTable(sampleVariation(sortByFreq(m)));
        StringBuilder compressed_code = new StringBuilder();
        StringBuilder padding_bit = new StringBuilder();        
        int compressed_code_length;
        String flag_bit;        
        for(int value: m){
            if(levensteinCodeTable.containsKey(value)){
                compressed_code.append(levensteinCodeTable.get(value));
            }
        }        
        compressed_code_length = compressed_code.length();
        if(compressed_code_length % 8 != 0) {
            for(int i = 0; i < 8 - (compressed_code_length % 8); i++) 
                padding_bit.append("0");
            flag_bit = Math.decimalToBinary8Bits(8 - (compressed_code_length % 8));
            compressed_code.append(padding_bit).append(flag_bit);
        }else {
            flag_bit = Math.decimalToBinary8Bits(compressed_code_length % 8);
            compressed_code.append(flag_bit);
        }

        StringBuilder ret = new StringBuilder();

        for (int i = 0; i < compressed_code.length() / 8; i++) {
            String s = Character.toString((char) Math.binaryToDecimal(compressed_code.substring(i*8, i*8 + 8 )));
            ret.append(s);
        }

        return ret.toString();
    }

    //Decompression
    public static int[] decompression(String compressed, List<Integer> m) {
        Map<String, Integer> levensteinCodeTable = levensteinCodeTableDec(m);
        StringBuilder compressed_m = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        List<Integer> m_list = new ArrayList<>();
        int length, flag_bit, erase;

        for (int i = 0; i < compressed.length(); i++) {
            String s = Math.decimalToBinary8Bits(compressed.charAt(i));
            compressed_m.append(s);
        }

        length = compressed_m.length();
        flag_bit = Math.binaryToDecimal(compressed_m.substring(length - 8, length));
        erase = length - (flag_bit + 8);
        compressed_m.delete(erase, length);
        int i = 0;
        while(compressed_m.length() != 0){
            temp.append(compressed_m.charAt(i));
            if(levensteinCodeTable.containsKey(temp.toString())){
                m_list.add(levensteinCodeTable.get(temp.toString()));
                compressed_m.delete(0, temp.length());
                temp.delete(0, temp.length());                                       
                i = -1;
            }
            i++;
        }    
        int[] array = new int[m_list.size()];
        for (i = 0; i < array.length; i++) {
            array[i] = m_list.get(i);
        }
        return array;
    }                            
  
    //HashMap Levenstein Code Table For Compression
    public static Map<Integer, String> levensteinCodeTable(List<Integer> sampleVariation){
        Map<Integer, String> elemCountMap = new HashMap<>();
        for(int i = 0; i < sampleVariation.size(); i++){
            elemCountMap.put(sampleVariation.get(i), levensteinCode(i));
        }   
        return  elemCountMap;                
    }
    
    //HashMap Levenstein Code Table For Decompression
    public static Map<String, Integer> levensteinCodeTableDec(List<Integer> sampleVariation){
        Map<String, Integer> elemCountMap = new HashMap<>();
        for(int i = 0; i < sampleVariation.size(); i++){
            elemCountMap.put(levensteinCode(i), sampleVariation.get(i));
        }   
        return  elemCountMap;                
    }
    
    //Levenstein Code for n
    public static String levensteinCode(int n){
        StringBuilder code_so_far = new StringBuilder(); 
        StringBuilder left = new StringBuilder();
        String n_binnary,right;
        
        int C = 1, M;
        
        if(n == 0) return "0";
        n_binnary = Math.decimalToBinary(n);
        right = n_binnary.substring(1);
        code_so_far.insert(0, right);
            M = right.length();
            while(M != 0){
                C++;
                n_binnary = Math.decimalToBinary(M);
                right = n_binnary.substring(1);
                code_so_far.insert(0, right);
                M = right.length();
            }
            code_so_far.insert(0, "0");
            for(int j = 0; j < C; j++) left.insert(0, "1");
            code_so_far.insert(0, left);
        return code_so_far.toString();
    }

    //Sorting sample's frequency descencing
    public static Map<Integer, Integer> sortByFreq(int[] arr){
        Map<Integer, Integer> elemCountMap = new LinkedHashMap<>();
        Map<Integer, Integer> reverseSortedMap = new LinkedHashMap<>();
        for(int i = 0; i < arr.length; i++){
            if(elemCountMap.containsKey(arr[i])){
                int newVal = elemCountMap.get(arr[i])+1;
                elemCountMap.put(arr[i],newVal);
            }
            else
                elemCountMap.put(arr[i],1);
        }
        reverseSortedMap = sortByValue(elemCountMap);
        return reverseSortedMap;
    }

    //Sorting map by value
    public static Map sortByValue(Map<Integer, Integer> mapIntInt){
        List<Entry<Integer, Integer>> list = new LinkedList<>(mapIntInt.entrySet());
        Collections.sort(list, new Comparator<Entry<Integer, Integer>>(){
            @Override
            public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }

        });
        LinkedHashMap<Integer, Integer> sortedByValue = new LinkedHashMap <>();
        for(Entry<Integer, Integer> entry : list){
            sortedByValue.put(entry.getKey(), entry.getValue());
        }
        return sortedByValue;
    }
    
    //Total key sample
    public static int totalSampleVariaton(Map<Integer, Integer> sampleSortByFreq){
        int m = sampleSortByFreq.size();
        return m;        
    }
    
    //Sample variation in samples
    public static List<Integer> sampleVariation(Map<Integer, Integer> sampleSortByFreq){
        int m = totalSampleVariaton(sampleSortByFreq);
        List<Integer> variationList = new ArrayList<>(m);
        for (Map.Entry mapElement : sampleSortByFreq.entrySet()) { 
            Integer key = (Integer) mapElement.getKey();
            variationList.add(key);
        }
        return variationList;
    }

    // Ratio of Compression
    public static double ratioOfCompression(double beforeCompression, double  afterCompression){
        return beforeCompression/afterCompression;
    }

    // Compression Ratio
    public static double compressionRatio(double  beforeCompression, double  afterCompression){
        return (afterCompression/beforeCompression)*100;
    }

    // Space Savings
    public static double spaceSavings(double  beforeCompression, double  afterCompression){
        return (1-(afterCompression/beforeCompression))*100;
    }

    // Bit Rate
    public static double bitRate(double  compressedBits, double  variationLength){
        return (compressedBits/variationLength);
    }

}
