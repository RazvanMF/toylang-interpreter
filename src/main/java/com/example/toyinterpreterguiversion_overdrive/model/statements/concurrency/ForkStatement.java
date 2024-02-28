package com.example.toyinterpreterguiversion_overdrive.model.statements.concurrency;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;

import java.io.IOException;

public class ForkStatement implements IStatement {
    IStatement statementToFork;

    public ForkStatement(IStatement sTF) {
        statementToFork = sTF;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        StackADT<IStatement> forkedStack = new StackADT<>();
        StackADT<DictionaryADT<String, IValue>> symTableStack = new StackADT<>();
        for (DictionaryADT<String, IValue> element : state.getSymTableStack().getReversed()) {
            symTableStack.push(element.deepcopy());
        }

        ProgramState forked = new ProgramState(forkedStack, symTableStack, state.getOutput(),
                state.getFileTable(), statementToFork);
        forked.setHeap(state.getHeap());
        return forked;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        statementToFork.typecheck(typeEnv.deepcopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "Fork the following: " + statementToFork.toString();
    }
}
