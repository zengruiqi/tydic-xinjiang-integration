package com.tydic.xinjiang.command;

import java.util.Map;

public class Command {
    private String commandType;
    private Map data;

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }
}
