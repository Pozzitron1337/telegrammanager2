package TelegramRobot;

import TelegramRobot.TelegramCLIPocessEmulator.TelegramCLIProcessEmulator;

// Builder pattern
/*
*
*
* */
public class TelegramDOSer extends TelegramRobot {

    String targetName;
    String message;
    long interval;
    long iterations;

    public TelegramDOSer(TelegramCLIProcessEmulator telegramCLIProcessEmulator){
        this.telegramCLIProcessEmulator = telegramCLIProcessEmulator;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setIterations(long iterations) {
        this.iterations = iterations;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void execute() {
        for (int i=0;i<iterations;i++){
            telegramCLIProcessEmulator.sendTextMessage(targetName,message);
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
