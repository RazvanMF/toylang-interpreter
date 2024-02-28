package com.example.toyinterpreterguiversion_overdrive.view.commands;

import com.example.toyinterpreterguiversion_overdrive.controller.Controller;
import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.extras.PrototypeParser;
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

public class TestPrototypeParserCommand extends Command {
    String fileName;
    public TestPrototypeParserCommand(String k, String v, String fN) {
        super(k, v);
        fileName = fN;
    }

    @Override
    public void create() throws StandardException, IOException, InterruptedException {
        StackADT<IStatement> exeStack = new StackADT<>();
        StackADT<DictionaryADT<String, IValue>> symTableStack = new StackADT<>();
        ListADT<IValue> output = new ListADT<>();
        DictionaryADT<StringValue, BufferedReader> fileTable = new DictionaryADT<>();

        PrototypeParser tester = new PrototypeParser();
        IStatement text = tester.fileToStatement(fileName);

        ProgramState program = new ProgramState(exeStack, symTableStack, output, fileTable, text);

        MemRepository memrepo = new MemRepository("log.txt");
        memrepo.add(program);
        Controller app = new Controller(memrepo);

        app.allSteps();


    }
}
