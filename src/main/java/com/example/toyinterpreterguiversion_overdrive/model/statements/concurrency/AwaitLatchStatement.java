package com.example.toyinterpreterguiversion_overdrive.model.statements.concurrency;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.latchtable.LatchTableADT;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;

import java.io.IOException;

public class AwaitLatchStatement implements IStatement {
    String latchKey;

    public AwaitLatchStatement(String latchKey) {
        this.latchKey = latchKey;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        StackADT<IStatement> exeStack = state.getExeStack();
        DictionaryADT<String, IValue> symTable = state.getSymTable();

        IValue latchKeyLookup = symTable.lookup(latchKey);
        if (latchKeyLookup != null) {
            int latchAddr = ((IntValue) latchKeyLookup).getValue();
            ProgramState.writeLock.lock();
            LatchTableADT<Integer> latchTable = ProgramState.getLatchTable();
            if (latchTable.isDefined(latchAddr)) {
                int remaining = latchTable.lookup(latchAddr);
                if (remaining > 0) {
                    exeStack.push(new AwaitLatchStatement(latchKey));
                }
                ProgramState.writeLock.unlock();
            }
            else {
                ProgramState.writeLock.unlock();
                throw new StandardException("Latch address not existent.");
            }
        }
        else
            throw new StandardException("Latch key not defined in SymTable.");

        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("Wait on latch (%s)", latchKey);
    }
}
