package com.example.toyinterpreterguiversion_overdrive.view.commands;

public class ExitCommand extends Command {
    public ExitCommand(String key, String desc) {
        super(key, desc);
    }
    @Override
    public void create() {
        System.exit(0);
    }
}
