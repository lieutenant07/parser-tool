package com.assignment.parser.command;

import com.assignment.parser.exception.ParserException;

import java.io.IOException;

public interface Command {
    void execute() throws IOException, ParserException;
}
