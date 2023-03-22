// 
// Decompiled by Procyon v0.5.36
// 

package scs.util.tools;

import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

public class DataFormats
{
    private static DataFormats dataFormat;
    
    static {
        DataFormats.dataFormat = null;
    }
    
    private DataFormats() {
    }
    
    public static synchronized DataFormats getInstance() {
        if (DataFormats.dataFormat == null) {
            DataFormats.dataFormat = new DataFormats();
        }
        return DataFormats.dataFormat;
    }
    
    public float subFloat(final float value, final int length) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(length, 4);
        return bd.floatValue();
    }
    
    public String hexToBinary(final String hexStr, final int binLength) {
        final StringBuilder zeroStr = new StringBuilder();
        for (int i = 0; i < binLength; ++i) {
            zeroStr.append("0");
        }
        final String binStr = Integer.toBinaryString(Integer.parseInt(hexStr, 16));
        return String.valueOf(zeroStr.substring(0, binLength - binStr.length())) + binStr;
    }
    
    public String binaryToHex(final String binStr) {
        final String zeroStr = "00";
        final String hexStr = Integer.toHexString(Integer.parseInt(binStr, 2));
        return String.valueOf(zeroStr.substring(0, 2 - hexStr.length())) + hexStr;
    }
    
    public String decimalToBinary(final int decimal, final int length) {
        final StringBuffer zeroStr = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            zeroStr.append("0");
        }
        final String binStr = Integer.toBinaryString(decimal);
        return String.valueOf(zeroStr.substring(0, length - binStr.length())) + binStr;
    }
    
    public String decimalToHex(final int decimal) {
        String result = "";
        final String binStr = Integer.toHexString(decimal);
        final int length = binStr.length();
        if (length > 3 && length <= 8) {
            result = String.valueOf("00000000".substring(0, 8 - length)) + binStr;
        }
        else if (length >= 0 && length <= 3) {
            result = String.valueOf("000".substring(0, 3 - length)) + binStr;
        }
        return result;
    }
    
    public String decimalToHex(final int decimal, final int length) {
        final StringBuffer zeroStr = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            zeroStr.append("0");
        }
        final String binStr = Integer.toHexString(decimal).toUpperCase();
        return String.valueOf(zeroStr.substring(0, length - binStr.length())) + binStr;
    }
    
    public String reverseStr(final String oldStr) {
        final char[] tmp = oldStr.toCharArray();
        final int length = tmp.length;
        final int rows = length >> 3;
        final List<String> rowsList = new ArrayList<String>();
        for (int i = 0; i < rows; ++i) {
            rowsList.add(oldStr.substring(i << 3, (i << 3) + 8));
        }
        final StringBuffer newStr = new StringBuffer();
        for (int j = rows - 1; j >= 0; --j) {
            newStr.append(rowsList.get(j));
        }
        return newStr.toString();
    }
    
    public static void main(final String[] args) {
        System.out.println(getInstance().decimalToHex(65535, 4));
    }
}
