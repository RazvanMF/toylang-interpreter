package com.example.toyinterpreterguiversion_overdrive.model.statements.concurrency;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.IExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.latchtable.LatchTableADT;

import java.io.IOException;

public class CreateLatchStatement implements IStatement {
    String latchKey;
    IExpression timeout;

    public CreateLatchStatement(String latchKey, IExpression timeout) {
        this.latchKey = latchKey;
        this.timeout = timeout;
    }
    //boolean modifiedSymTable = false;

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        DictionaryADT<String, IValue> symTable = state.getSymTable();
        IValue timeoutRes = timeout.evaluate(symTable, state.getHeap());
        if (timeoutRes != null && timeoutRes.getType().equals(new IntType())) {
            int timeoutVal = ((IntValue) timeoutRes).getValue();
            ProgramState.writeLock.lock();
            LatchTableADT<Integer> latchTable = ProgramState.getLatchTable();
            int addr = latchTable.put(timeoutVal);
            if (symTable.isDefined(latchKey)) {
                symTable.update(latchKey, new IntValue(addr));
            }
            else {
                //modifiedSymTable = true;
                symTable.put(latchKey, new IntValue(addr));
            }
            ProgramState.writeLock.unlock();
        }
        else
            throw new StandardException("timeout variable expected to be of type int");

        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("Create Latch in (%s), which expects %s threads", latchKey, timeout);
    }
}
