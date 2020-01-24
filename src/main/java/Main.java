import TelegramRobot.TelegramDOSer;
import TelegramRobot.TelegramCLIPocessEmulator.TelegramCLIProcessEmulator;

public class Main {




    public static void main(String[] args) {
        TelegramCLIProcessEmulator te=new TelegramCLIProcessEmulator();
        TelegramDOSer dos=new TelegramDOSer(te);
        dos.setTargetName("Катерилла");
        dos.setMessage("хуй");
        dos.setInterval(500);
        dos.setIterations(500);
        dos.execute();
        te.close();
    }

}
