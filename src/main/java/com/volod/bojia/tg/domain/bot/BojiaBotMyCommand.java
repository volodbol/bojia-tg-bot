package com.volod.bojia.tg.domain.bot;

import com.pengrad.telegrambot.model.BotCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum BojiaBotMyCommand {
    HELP("help", "see help page"),
    ADD_PROMPT("prompt", "add prompt to use for cover letter generation"),
    SEARCHES("searches", "list of all searches"),
    REMOVE_SEARCH("remove", "remove search from use");

    private final String command;
    private final String description;

    public String getCommand() {
        return "/" + this.command;
    }

    public static BotCommand[] getBotCommands() {
        return Arrays.stream(BojiaBotMyCommand.values())
                .map(command -> new BotCommand(command.getCommand(), command.getDescription()))
                .toArray(BotCommand[]::new);
    }

    public static String getJoinedCommands() {
        return Arrays.stream(BojiaBotMyCommand.values())
                .map(BojiaBotMyCommand::getCommand)
                .collect(Collectors.joining("\n"));
    }

}
