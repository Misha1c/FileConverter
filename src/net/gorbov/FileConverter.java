package net.gorbov;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;

import static java.nio.file.StandardOpenOption.*;

/**
 * Created by Mihail on 12.01.2015.
 */
public class FileConverter {

    final private static long  SIZE_FOR_COMPRESS = 10*1024*1024;

    public static void main(String args[]){


        createFile("resources/TestFile.txt");

        Path fileTo = Paths.get("resources/NewFile.txt");
        copyFile("resources/TestFile.txt", "resources/NewFile.txt");
    }

    public static void createFile(String filePath){

        Path file = Paths.get(filePath);
        Charset charset = Charset.forName("Cp1251");
        String s = "This is my favorite file This is my favorite file This is my favorite file This is my favorite file";

        try (BufferedWriter writer = Files.newBufferedWriter(file, charset, CREATE )) {

            for (int i = 0; i < 200000; i++){
                writer.write(s, 0, s.length());
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public static void copyFile(String stringPathFrom, String stringPathTo){

        Charset charsetFrom = Charset.forName("UTF-8");
        Charset charsetTo = Charset.forName("Cp1251");

        Path pathFrom = Paths.get(stringPathFrom);
        Path pathTo = Paths.get(stringPathTo);

        try (BufferedReader reader = Files.newBufferedReader(pathFrom, charsetFrom);
             BufferedWriter writer = Files.newBufferedWriter(pathTo, charsetTo)) {

            String line = null;
            while ((line = reader.readLine()) != null) {
                writer.write(line, 0, line.length());
            }

            long fileSize = Files.size(pathTo);
            if (fileSize > SIZE_FOR_COMPRESS){
                compressFile(stringPathTo);
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    private static void compressFile(String pathFrom) throws IOException {

        FileInputStream fis = new FileInputStream(pathFrom);
        FileOutputStream fos = new FileOutputStream(pathFrom+".gz");
        GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
        byte[] buffer = new byte[1024];
        int len;
        while((len=fis.read(buffer)) != -1){
            gzipOS.write(buffer, 0, len);
        }

        gzipOS.close();
        fos.close();
        fis.close();

    }
}
