package application;
import controller.commandHndlr;

public class MizDooniApplication {
    public static void main(String[] args) throws InterruptedException {
        commandHndlr cmd = new commandHndlr();
        cmd.start();
    }
}
