// 
// Decompiled by Procyon v0.5.36
// 

package scs.util.tools;

import java.io.LineNumberReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import java.util.Locale;
import java.text.SimpleDateFormat;

public class FileOperation
{
    private SimpleDateFormat dateFormat;
    
    public FileOperation() {
        this.dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
    }
    
    public int headFile(final String filePath, final int headnum) throws IOException {
        final File file = new File(filePath);
        BufferedReader reader = null;
        try {
            final FileInputStream fileInputStream = new FileInputStream(file);
            final InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String line = "";
            int count = 0;
            while ((line = reader.readLine()) != null && ++count <= headnum) {
                System.out.println(line);
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }
        if (reader != null) {
            try {
                reader.close();
            }
            catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        return 0;
    }
    
    public List<Long> readLogFile(final String filePath) throws IOException {
        final List<Long> timeList = new ArrayList<Long>();
        final File file = new File(filePath);
        BufferedReader reader = null;
        try {
            final FileInputStream fileInputStream = new FileInputStream(file);
            final InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String line = "";
            int start = 0;
            while ((line = reader.readLine()) != null) {
                start = line.indexOf("[") + 1;
                try {
                    timeList.add(this.dateFormat.parse(line.substring(start, start + 20)).getTime());
                }
                catch (Exception ex) {}
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }
        if (reader != null) {
            try {
                reader.close();
            }
            catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        return timeList;
    }
    
    public List<Double> readDoubleFile(final String filePath) throws IOException {
        final List<Double> timeList = new ArrayList<Double>();
        final File file = new File(filePath);
        BufferedReader reader = null;
        try {
            final FileInputStream fileInputStream = new FileInputStream(file);
            final InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = reader.readLine()) != null) {
                timeList.add(Double.parseDouble(line));
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }
        if (reader != null) {
            try {
                reader.close();
            }
            catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        return timeList;
    }
    
    public List<String> readStringFile(final String filePath) throws IOException {
        final List<String> timeList = new ArrayList<String>();
        final File file = new File(filePath);
        BufferedReader reader = null;
        try {
            final FileInputStream fileInputStream = new FileInputStream(file);
            final InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = reader.readLine()) != null) {
                timeList.add(line);
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }
        if (reader != null) {
            try {
                reader.close();
            }
            catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        return timeList;
    }
    
    public List<Integer> readIntFile(final String filePath) throws IOException {
        final List<Integer> timeList = new ArrayList<Integer>();
        final File file = new File(filePath);
        BufferedReader reader = null;
        try {
            final FileInputStream fileInputStream = new FileInputStream(file);
            final InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = reader.readLine()) != null) {
                timeList.add(Integer.parseInt(line));
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }
        if (reader != null) {
            try {
                reader.close();
            }
            catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        return timeList;
    }
    
    public int splitFile(final String fileInputPath, final int lineNumPerFile) throws IOException {
        final int totalLineNum = this.countLineNum(fileInputPath);
        int count = 0;
        int fileIndex = 0;
        FileWriter outFile = null;
        final File file = new File(fileInputPath);
        BufferedReader reader = null;
        try {
            final FileInputStream fileInputStream = new FileInputStream(file);
            final InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (++count % lineNumPerFile == 1) {
                    if (outFile != null) {
                        outFile.flush();
                        outFile.close();
                    }
                    outFile = new FileWriter(fileInputPath.replace(".", "_" + fileIndex + "."));
                    ++fileIndex;
                    System.out.println("process:" + count * 100.0 / totalLineNum + "%");
                }
                outFile.write(String.valueOf(line) + "\r\n");
            }
            if (outFile != null) {
                outFile.flush();
                outFile.close();
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }
        if (reader != null) {
            try {
                reader.close();
            }
            catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        return count;
    }
    
    public int countLineNum(final String filePath) {
        int linenumber = 0;
        try {
            final File file = new File(filePath);
            if (file.exists()) {
                final FileReader fr = new FileReader(file);
                final LineNumberReader lnr = new LineNumberReader(fr);
                lnr.skip(file.length());
                linenumber = lnr.getLineNumber();
                lnr.close();
            }
            else {
                System.out.println("File does not exists!");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return linenumber;
    }
    
    public <T> void writeResFile(final List<T> resList, final String filePath, final String type) {
        try {
            final FileWriter writer = new FileWriter(filePath);
            final int size = resList.size();
            if (type != null && type.equals("row")) {
                for (int i = 0; i < size; ++i) {
                    writer.write(resList.get(i) + "\n");
                    if (i % 1000 == 0) {
                        writer.flush();
                    }
                }
            }
            else {
                for (int i = 0; i < size; ++i) {
                    writer.write(resList.get(i).toString());
                    if (i % 1000 == 0) {
                        writer.flush();
                    }
                }
            }
            writer.flush();
            writer.close();
            System.out.println("\u5199\u5165\u5b8c\u6bd5");
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
