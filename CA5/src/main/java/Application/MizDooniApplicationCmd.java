package Application;

import Controller.CommandHndlr;
import Service.Mizdooni.MizDooni;

public class MizDooniApplicationCmd {
    public static void main(String[] args) throws Exception {
        MizDooni.getInstance();
        CommandHndlr cmd = new CommandHndlr();
        cmd.start();
    }
}
