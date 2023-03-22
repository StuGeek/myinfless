// 
// Decompiled by Procyon v0.5.36
// 

package scs.util.tools;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Generate
{
    private Random rand;
    public static final String SOURCES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    
    public Generate() {
        this.rand = new Random();
    }
    
    public static void main(final String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("[startId endId filePath]");
        }
        else {
            new Generate().generate(Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2]);
        }
    }
    
    public void generate(final int startId, final int endId, final String filePath) throws IOException {
        final FileWriter writer = new FileWriter(filePath);
        final StringBuilder sBuilder = new StringBuilder();
        for (int i = startId; i < endId; ++i) {
            sBuilder.append("{\"index\":{\"_id\":\"").append(i).append("\"}}\r\n");
            writer.write(sBuilder.toString());
            sBuilder.setLength(0);
            sBuilder.append("{\"account_number\":").append(this.rand.nextInt(9999)).append(",\"balance\":").append(this.rand.nextInt(9999)).append(",\"firstname\":\"").append(this.generateSpaceString(30)).append("\",\"lastname\":\"").append(this.generateSpaceString(30)).append("\",\"age\":").append(this.rand.nextInt(99)).append(",\"gender\":\"F\",\"address\":\"677 Hope Street\",\"employer\":\"Fortean\",\"email\":\"wheelerayers@fortean.com\",\"city\":\"Ironton\",\"state\":\"PA\"}\r\n");
            writer.write(sBuilder.toString());
            sBuilder.setLength(0);
        }
        writer.flush();
        writer.close();
    }
    
    public String generateString(final int length) {
        final char[] text = new char[length];
        for (int i = 0; i < length; ++i) {
            text[i] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".charAt(this.rand.nextInt(62));
        }
        return new String(text);
    }
    
    public String generateSpaceString(final int length) {
        final StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            sBuilder.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".charAt(this.rand.nextInt(62))).append(" ");
        }
        return sBuilder.toString();
    }
}
