package application;
import controller.commandHndlr;
import service.MizDooni;

public class MizDooniApplication {
    public static void main(String[] args) throws InterruptedException {
        MizDooni.getInstance();
        commandHndlr cmd = new commandHndlr();
        cmd.start();
    }
}
