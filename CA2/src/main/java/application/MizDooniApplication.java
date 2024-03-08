package application;

import controller.logic.CommandHndlr;
import service.MizDooni;

public class MizDooniApplication {
    public static void main(String[] args) throws Exception {
        MizDooni.getInstance();
        CommandHndlr cmd = new CommandHndlr();
        cmd.start();
    }
}
