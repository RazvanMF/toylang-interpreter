package com.example.toyinterpreterguiversion_overdrive.model.statements.concurrency;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;

import java.io.IOException;

public class UnlockMutexStatement implements IStatement {

    String lockVarName;
    public UnlockMutexStatement(String lVN) {
        lockVarName = lVN;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        StackADT<IStatement> exeStack = state.getExeStack();
        DictionaryADT<String, IValue> symTable = state.getSymTable();

        int progID = state.getPersonalID();

        if (symTable.isDefined(lockVarName)) {
            IValue lockAddrAsVal = symTable.lookup(lockVarName);
            int lockAddr = ((IntValue) lockAddrAsVal).getValue();

            ProgramState.writeLock.lock();
            if (ProgramState.getLockTable().isDefined(lockAddr)) {
                if (ProgramState.getLockTable().lookup(lockAddr) == progID) {
                    ProgramState.getLockTable().update(lockAddr, -1);
                    ProgramState.writeLock.unlock();
                    return null;
                }
            }
            else {
                ProgramState.writeLock.unlock();
                throw new StandardException(String.format("Lock address (%s) not existent.", String.valueOf(lockAddr)));
            }
        }
        else
            throw new StandardException(String.format("Lock variable (%s) not defined.", lockVarName));

        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        if (!typeEnv.lookup(lockVarName).equals(new IntType()))
            throw new StandardException("Mutex Address expected to be of type int.");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "Unlock Mutex (" + lockVarName + ")";
    }
}
