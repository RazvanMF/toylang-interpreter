package com.example.toyinterpreterguiversion_overdrive.view.commands;

import com.example.toyinterpreterguiversion_overdrive.controller.Controller;
import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.StringValue;
import com.example.toyinterpreterguiversion_overdrive.storage.MemRepository;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.list.ListADT;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;

import java.io.BufferedReader;
import java.io.IOException;

public abstract class Command {
    private String key, description;
    protected IStatement program;
    public Command(String k, String v) {
        key = k; description = v;
    }

    public abstract void create() throws StandardException, IOException, InterruptedException;
    public String getKey() {
        return key;
    }
    public String getDescription() {
        return description;
    }

    protected void programExecutor(IStatement ex) throws StandardException, IOException, InterruptedException {
        StackADT<IStatement> exeStack = new StackADT<IStatement>();
        StackADT<DictionaryADT<String, IValue>> symTableStack = new StackADT<>();
        ListADT<IValue> output = new ListADT<IValue>();
        DictionaryADT<StringValue, BufferedReader> fileTable = new DictionaryADT<StringValue, BufferedReader>();
        ProgramState exercise = new ProgramState(exeStack, symTableStack, output, fileTable, ex);

        MemRepository memrepo = new MemRepository("log.txt");
        memrepo.add(exercise);
        Controller app = new Controller(memrepo);

        app.allSteps();
        for (String logelem : exercise.showLog()) {
            System.out.println(logelem);
        }
    }
}
