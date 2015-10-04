package ru.spbau.pavlyutchenko.task1;

import org.apache.commons.cli.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Command(name = "grep")
public class Grep implements ICommand {

    GrepOptionsParser grepOptionsParser = new GrepOptionsParserCli();

    @Override
    public String run(String[] args, Boolean isFirstCommand) {

        if(args.length < 3 ) {
            System.out.println("Too few arguments for grep");
            return "";
        }

        String query = args[args.length-2];

        GrepParameters grepParameters = null;
        try {
            String[] options = Arrays.copyOfRange(args, 1, args.length - 2);
            grepParameters = grepOptionsParser.getParameters(options);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        List<String> resultsLines = new ArrayList<>();
        int numLines = 0;

        List<String> lines = null;
        try {
            lines = getLines(args, isFirstCommand);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        for (String line : lines) {
            if(numLines > 0) {
                resultsLines.add(line);
                numLines--;
            }

            Pattern pattern;
            if (grepParameters.isIgnoreCase()) {
                pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
            } else {
                pattern = Pattern.compile(query);
            }

            if( (!grepParameters.isWordRegexp() && pattern.matcher(line).find()) ||
                    (grepParameters.isWordRegexp() && findWord(pattern, line, grepParameters))) {
                if(grepParameters.isAfterContext()) {
                    numLines = grepParameters.getCountLinesAfterContext();
                } else {
                    resultsLines.add(line);
                }
            }

        }
        return String.join(System.lineSeparator(), resultsLines);
    }


    private List<String> getLines(String[] args, Boolean isFirstCommand) throws IOException {
        List<String> lines;
        if (isFirstCommand) {
            File file = new File(args[args.length - 1]);
            lines = Files.readAllLines(file.toPath());
        } else {
            lines = new ArrayList<>(Arrays.asList(args[args.length - 1].split(System.lineSeparator())));
        }
        return lines;
    }

    private boolean findWord(Pattern pattern, String line, GrepParameters grepParameters) {
        String patternString = pattern.pattern();
        boolean startLine = patternString.charAt(0) == '^';
        patternString = startLine ? patternString.substring(1) : patternString;
        boolean endLine = patternString.charAt(patternString.length()-1) == '$';
        patternString = endLine ? patternString.substring(0,patternString.length()-2) : patternString;

        String[] words = line.split("\\s+");
        for(int i=0; i < words.length; i++) {
            boolean find = grepParameters.ignoreCase ? patternString.equalsIgnoreCase(words[i]) :
                    patternString.equals(words[i]);
            if (find) {
                if (    (!startLine && !endLine) ||
                        (startLine && !endLine && line.startsWith(words[i])) ||
                        (!startLine && endLine && line.endsWith(words[i])) ||
                        (startLine && endLine && line.startsWith(words[i]) && line.endsWith(words[i]))) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public String man() {
        return "grep - print lines matching a pattern";
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
