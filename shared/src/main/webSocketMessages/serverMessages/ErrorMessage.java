package webSocketMessages.serverMessages;

import webSocketMessages.userCommands.UserGameCommand;

public class ErrorMessage extends ServerMessage {
    private String errorMessage;

    public ErrorMessage(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
        //this.commandType = UserGameCommand.CommandType.ERROR;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}