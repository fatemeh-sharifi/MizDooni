package Application;

import Application.Controller.CommandHndlr;
import Service.MizDooni;

public class MizDooniApplication {
    public static void main(String[] args) throws Exception {
        MizDooni.getInstance();
        CommandHndlr cmd = new CommandHndlr();
        cmd.start();
    }
}
