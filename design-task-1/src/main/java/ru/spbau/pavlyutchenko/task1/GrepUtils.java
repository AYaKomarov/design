package ru.spbau.pavlyutchenko.task1;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author ayakomarov
 */
public class GrepUtils {

    public static enum CommandParserVersion {
        CLI_VERSION,
        JCOMMANDER_VERSION;
    }

    public static String run(String[] args, Boolean isFirstCommand, CommandParserVersion commandParserVersion) throws IOException {

        GrepParameters grepParameters = getParameters(args, commandParserVersion);
        if(grepParameters == null)
            return "";

        List<String> lines = getLines(args, isFirstCommand);
        ArrayList<String> resultLines = new ArrayList<>();
        Set<Integer> addedLinesIndex = new HashSet<>();
        String query = args[args.length - 2];

        Pattern pattern;
        if (grepParameters.isIgnoreCase()) {
            pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile(query);
        }

        for (int j=0; j < lines.size(); j++) {

            String line = lines.get(j);
            boolean find = false;
            if (grepParameters.isWordRegexp()) {

                String[] split = line.split("\\s+");
                for (int i = 0; i < split.length; i++) {
                    if (grepParameters.isIgnoreCase()) {
                        split[i] = split[i].toLowerCase();
                        query = query.toLowerCase();
                    }
                    if (query.equals(split[i])) {
                        find = true;
                        break;
                    }
                }
            } else {
                find = pattern.matcher(line).find();
            }
            if (find) {
                if (grepParameters.isAfterContext()) {
                    int addedLeft = grepParameters.getCountLinesAfterContext();

                    for (int d = j; d < lines.size() && addedLeft >= 0; d++) {
                        if (!addedLinesIndex.contains(d)) {
                            resultLines.add(lines.get(d));
                            addedLinesIndex.add(d);
                        }
                        addedLeft--;
                    }
                } else {
                    if (!addedLinesIndex.contains(j)) {
                        resultLines.add(line);
                        addedLinesIndex.add(j);
                    }
                }
            }
        }

        return String.join(System.lineSeparator(), resultLines);
    }

    private static List<String> getLines(String[] args, Boolean isFirstCommand) throws IOException {
        List<String> lines;
        if (isFirstCommand) {
            File file = new File(args[args.length - 1]);
            lines = Files.readAllLines(file.toPath());
        } else {
            lines = new ArrayList<>(Arrays.asList(args[args.length - 1].split(System.lineSeparator())));
        }
        return lines;
    }

    private static GrepParameters getParameters(String[] args, CommandParserVersion commandParserVersion) {
        try {
            switch (commandParserVersion) {
                case CLI_VERSION:
                {
                    return getParametersCli(args);
                }
                case JCOMMANDER_VERSION:
                {
                    return getParametersJCommander(args);
                }
            }
            return null;
        } catch (ParseException e) {
            System.out.print(e.getMessage());
            return null;
        }
    }
    private static GrepParameters getParametersCli(String[] args) throws ParseException {
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

        GrepParameters gps = new GrepParameters();
        gps.setParameters(i, w, A, cA);
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

    private static GrepParameters getParametersJCommander(String[] args) throws ParseException {
        JArgs jct = new JArgs();
        new JCommander(jct, Arrays.copyOfRange(args, 1, args.length - 2));

        boolean i = jct.isIgnoreCase;
        boolean w = jct.isWordRegexp;

        int cA = jct.countLinesAfterContext;
        boolean A = cA != -1;

        GrepParameters gps = new GrepParameters();
        gps.setParameters(i,w,A,cA);
        return gps;
    }

    public static class GrepParameters {
        private boolean ignoreCase = false;
        private boolean wordRegexp = false;
        private boolean afterContext = false;
        private int countLinesAfterContext = 0;

        public void setParameters(boolean ignoreCase,
                                  boolean wordRegexp,
                                  boolean afterContext,
                                  int countLinesAfterContext) {
            this.ignoreCase = ignoreCase;
            this.wordRegexp = wordRegexp;
            this.afterContext = afterContext;
            this.countLinesAfterContext = countLinesAfterContext;
        }

        public boolean isIgnoreCase() {
            return ignoreCase;
        }

        public boolean isWordRegexp() {
            return wordRegexp;
        }

        public boolean isAfterContext() {
            return afterContext;
        }

        public int getCountLinesAfterContext() {
            return countLinesAfterContext;
        }
    }
}
