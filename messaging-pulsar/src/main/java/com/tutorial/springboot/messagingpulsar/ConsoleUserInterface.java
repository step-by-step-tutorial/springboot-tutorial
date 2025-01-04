package com.tutorial.springboot.messagingpulsar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class ConsoleUserInterface {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MessageSenderService messageSenderService;

    public ConsoleUserInterface(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    @ShellMethod(key = "send")
    public void sendCommand(@ShellOption(defaultValue = "") String message) {
        if (message.isBlank()) {
            logger.error("Message should not be empty.");
            return;
        }
        messageSenderService.send(message);
    }
}
