package ru.spbau.pavlyutchenko.task1;

import org.apache.commons.cli.ParseException;

public interface GrepOptionsParser {
    Grep.GrepParameters getParameters(String[] args) throws ParseException;
}
