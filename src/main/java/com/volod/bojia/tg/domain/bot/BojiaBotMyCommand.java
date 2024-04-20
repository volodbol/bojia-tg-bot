package com.volod.bojia.tg.domain.bot;

import com.pengrad.telegrambot.model.BotCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum BojiaBotMyCommand {
    HELP("help", "see help page"),
    ADD_PROMPT("prompt", "add prompt to use for generation cover letter");

    private final String command;
    private final String description;

    public static BotCommand[] getBotCommands() {
        return Arrays.stream(BojiaBotMyCommand.values())
                .map(command -> new BotCommand(command.getCommand(), command.getDescription()))
                .toArray(BotCommand[]::new);
    }

}
