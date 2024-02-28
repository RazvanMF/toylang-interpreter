package com.example.toyinterpreterguiversion_overdrive.model.statements.wait_operations;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;

import java.io.IOException;

public class SleepStatement implements IStatement {
    IntValue stepsToSleep;
    //int stepsToSleep;
    public SleepStatement (IntValue sTS) {
        stepsToSleep = sTS;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        StackADT<IStatement> exeStack = state.getExeStack();
        if (stepsToSleep.getValue() > 0) {
            exeStack.push(new SleepStatement(new IntValue(stepsToSleep.getValue() - 1)));
        }
        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("sleep(%d)", stepsToSleep.getValue());
    }
}
