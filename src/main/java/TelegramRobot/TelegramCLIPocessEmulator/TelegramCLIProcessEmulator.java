package TelegramRobot.TelegramCLIPocessEmulator;

import TelegramRobot.Loggers.FileLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelegramCLIProcessEmulator {

    //the handle of process
    private Process telegramProcess;

    private OutputStream output;

    private InputStream input;

    private Thread fileLoggerThread;

    private List<String> contactList;

    private List<String> channelList;

    public TelegramCLIProcessEmulator(){
        try {
            contactList=new ArrayList<>();
            channelList =new ArrayList<>();
            telegramProcess = Runtime.getRuntime().exec("telegram-cli");
            Thread.sleep(2000);
            output=telegramProcess.getOutputStream();
            input=telegramProcess.getInputStream();
            fileLoggerThread=new Thread(new Runnable() {
                @Override
                public void run(){
                    FileLogger fileLogger=new FileLogger();
                    String line;
                    try {
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(input));
                        while ((line = br.readLine()) != null){
                            fileLogger.log(line);
                        }
                    }catch (IOException ioe){
                        fileLogger.appendBuffer();
                    }
                }
            });
            initialize();
        }catch (IOException ioexception){
            ioexception.printStackTrace();
            System.exit(1);
        }catch (InterruptedException ie){
            ie.printStackTrace();
            System.exit(2);
        }
    }

    private void initialize()throws IOException{
        BufferedReader br=new BufferedReader(
                new InputStreamReader(input));
        String contactList="contact_list"+"\n";
        output.write(contactList.getBytes());
        output.flush();
        findAndSetContactList(br);

        String channelList="channel_list"+"\n";
        output.write(channelList.getBytes());
        output.flush();
        findAndSetChannelList(br);
        br.close();

        fileLoggerThread.start();
    }

    private void findAndSetContactList(BufferedReader br){
        try{
            String line;
            while(!(line=br.readLine()).equals("> contact_list")){
                //System.out.println(line);
            }
            while (line.matches(">?\\s*(contact_list)?")){
                line=br.readLine();
                //System.out.println(line);
            }
            do{
                //System.out.println(line);
                contactList.add(line);
                line=br.readLine();
            }while (!line.matches(">?\\s*"));
            System.out.println(contactList);
            //System.out.println(line);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    private void findAndSetChannelList(BufferedReader br){
        try{
            String line=br.readLine();
            while (line.matches(">?\\s*[chanel_ist]*")){
                //System.out.println("1:"+line);
                line=br.readLine();
            }
            ArrayList<String> channelListRaw=new ArrayList<>();
            do{
                channelListRaw.add(line);
                line=br.readLine();
                //System.out.println(line);
            }while (line.matches("Channel.*"));

            System.out.println(channelListRaw);

            Pattern pattern=Pattern.compile("Channel\\s(.*):");
            Matcher matcher;
            for (String s:channelListRaw){
                matcher=pattern.matcher(s);
                matcher.find();
                channelList.add(matcher.group(1));
            }
            System.out.println(channelList);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public List<String> getChannelList() { return channelList; }

    public List<String> getContactList(){
        return contactList;
    }

    public synchronized void sendTextMessage(String whom,String text){
        try {
            String message="msg"+" "+whom+" "+text+"\n";
            output.write(message.getBytes());
            output.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void close(){
        try{
            fileLoggerThread.interrupt();
            output.close();
            input.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        telegramProcess.destroy();
    }

}
