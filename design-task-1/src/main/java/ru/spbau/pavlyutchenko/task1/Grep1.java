package ru.spbau.pavlyutchenko.task1;

import java.io.IOException;

/**
 * @author ayakomarov
 */
@Command(name = "grep1")
public class Grep1 implements ICommand {

    @Override
    public String run(String[] args, Boolean isFirstCommand) {
        try {
            return GrepUtils.run(args, isFirstCommand, GrepUtils.CommandParserVersion.CLI_VERSION);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String man() {
        return "grep1 - print lines matching a pattern (Cli version)";
    }
}
