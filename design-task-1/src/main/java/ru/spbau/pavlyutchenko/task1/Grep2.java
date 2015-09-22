package ru.spbau.pavlyutchenko.task1;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ayakomarov
 */
@Command(name = "grep2")
public class Grep2 implements ICommand {

    @Override
    public String run(String[] args, Boolean isFirstCommand) {
        try {
            return GrepUtils.run(args, isFirstCommand, GrepUtils.CommandParserVersion.JCOMMANDER_VERSION);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String man() {
        return "grep2 - print lines matching a pattern (JCommander version)";
    }
}
