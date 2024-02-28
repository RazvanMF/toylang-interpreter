package com.example.toyinterpreterguiversion_overdrive.model.statements.concurrency;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;

import java.io.IOException;

public class CreateMutexLockStatement implements IStatement {
    String varName;

    public CreateMutexLockStatement(String varName) {
        this.varName = varName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        ProgramState.writeLock.lock();
        int lockAddr = ProgramState.getLockTable().put(-1); //initially unlocked, we will insert a lock w/ value -1,
                                                    // and the function will return its address
        DictionaryADT<String, IValue> symTable = state.getSymTable();
        if (symTable.isDefined(varName)) {
            symTable.update(varName, new IntValue(lockAddr));
        }
        else {
            symTable.put(varName, new IntValue(lockAddr));
        }


        ProgramState.writeLock.unlock();
        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        if (!typeEnv.lookup(varName).equals(new IntType()))
            throw new StandardException("Mutex Address expected to be of type int.");

        return typeEnv;
    }

    @Override
    public String toString() {
        return "Create Mutex (" + varName + ")";
    }
}
