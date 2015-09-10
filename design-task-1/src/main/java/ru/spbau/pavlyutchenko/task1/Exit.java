package ru.spbau.pavlyutchenko.task1;

@Command(name = "exit")
public class Exit implements ICommand {
    @Override
    public String run(String[] args, Boolean isFirstCommand) {
        System.exit(0);
        return "";
    }

    @Override
    public void man() {
        System.out.println("ru.spbau.pavlyutchenko.task1.Command exit close the program. This command have no args.");
    }
}
