package ru.spbau.pavlyutchenko.task1;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.commons.cli.ParseException;

public class GrepOptionsParserJCommander implements GrepOptionsParser {

    @Override
    public Grep.GrepParameters getParameters(String[] options) throws ParseException {
        JArgs jct = new JArgs();
        new JCommander(jct, options);

        boolean i = jct.isIgnoreCase;
        boolean w = jct.isWordRegexp;

        int cA = jct.countLinesAfterContext;
        boolean A = cA != -1;

        Grep.GrepParameters gps = new Grep.GrepParameters();
        gps.setParameters(i,w,A,cA);
        return gps;
    }

    private static class JArgs {
        @Parameter(names = "-A", description = "After context")
        public Integer countLinesAfterContext = -1;

        @Parameter(names = "-w", description = "Word regexp")
        public boolean isWordRegexp = false;

        @Parameter(names = "-i", description = "Ignore case flag")
        private boolean isIgnoreCase = false;
    }
}
