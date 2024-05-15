package com.volod.bojia.tg.domain.bot;

import com.pengrad.telegrambot.model.BotCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum BojiaBotCommand {
    HELP("help", "see help page"),
    ADD_PROMPT("prompt", "add prompt to use for cover letter generation"),
    SEARCHES("searches", "list of all searches"),
    REMOVE_SEARCH("remove", "remove search from use"),
    DJINNI("djinni", "add djinni search"),;

    private final String value;
    private final String description;

    public String getValue() {
        return "/" + this.value;
    }

    public static BotCommand[] getBotCommands() {
        return Arrays.stream(BojiaBotCommand.values())
                .map(command -> new BotCommand(command.getValue(), command.getDescription()))
                .toArray(BotCommand[]::new);
    }

    public static String getJoinedCommands() {
        return Arrays.stream(BojiaBotCommand.values())
                .map(BojiaBotCommand::getValue)
                .collect(Collectors.joining("\n"));
    }

}
