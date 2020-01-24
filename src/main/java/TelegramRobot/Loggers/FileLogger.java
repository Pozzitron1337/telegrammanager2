package TelegramRobot.Loggers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FileLogger implements Logger {

    private FileWriter fr;
    private File file;

    private List<String> buffer;
    private int bufferSize;


    private final SimpleDateFormat filenameFormat=
            new SimpleDateFormat("'TelegramRobotLog 'dd.MM.yyyy-HH:mm:ss");

    private final SimpleDateFormat lineFormat=
            new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");

    private void initialize() {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileLogger(){
        file=new File("./"+filenameFormat.format(new Date())+".txt");
        buffer=new ArrayList<String>();
        bufferSize=200;
        initialize();
    }

    @Override
    public void log(String message) {
        if(buffer.size()<bufferSize) {
            buffer.add(message);
        }else {
            writeToFile();
        }
    }

    private synchronized void writeToFile(){
        try{
            FileWriter fr = new FileWriter(file, true);
            for (String msg : buffer) {
                fr.write(lineFormat.format(new Date()) + msg + "\n");
                fr.flush();
            }
            fr.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
    public void appendBuffer(){
        writeToFile();
    }

}
