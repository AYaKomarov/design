package ru.spbau.pavlyutchenko.task1;

import org.apache.commons.cli.*;

public class GrepOptionsParserCli implements GrepOptionsParser{

    @Override
    public Grep.GrepParameters getParameters(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("i", false, "Ignore case");
        options.addOption("w", false, "Word regexp");
        options.addOption("A", true, "After context");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        boolean i = false, w = false, A = false;
        int cA = 0;

        if(cmd.hasOption("i")) {
            i = true;
        }
        if(cmd.hasOption("w")) {
            w = true;
        }
        if(cmd.hasOption("A")) {
            A = true;
            cA = Integer.parseInt(cmd.getOptionValue("A"));
        }

        Grep.GrepParameters gps = new Grep.GrepParameters();
        gps.setParameters(i, w, A, cA);
        return gps;
    }
}
