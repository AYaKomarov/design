package ru.spbau.pavlyutchenko.task1;

public interface ICommand {
    String run(String[] args, Boolean isFirstCommand);
    String man();
}