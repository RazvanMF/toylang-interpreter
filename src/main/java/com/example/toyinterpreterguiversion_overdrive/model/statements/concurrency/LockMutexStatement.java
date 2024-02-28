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

public class LockMutexStatement implements IStatement {
    String lockVarName;

    public LockMutexStatement(String lockVarName) {
        this.lockVarName = lockVarName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        StackADT<IStatement> exeStack = state.getExeStack();
        DictionaryADT<String, IValue> symTable = state.getSymTable();

        int progID = state.getPersonalID();

        if (symTable.isDefined(lockVarName)) {
            IValue lockAddrAsVal = symTable.lookup(lockVarName);
            int lockAddr = ((IntValue) lockAddrAsVal).getValue();

            ProgramState.writeLock.lock(); //lock before looking, unlock after operation
            if (ProgramState.getLockTable().isDefined(lockAddr)) {
                int lockerID = ProgramState.getLockTable().lookup(lockAddr);
                if (lockerID != -1) {
                    exeStack.push(new LockMutexStatement(lockVarName));
                    ProgramState.writeLock.unlock();  // push statement, unlock
                    return null;
                }
                else {
                    ProgramState.getLockTable().update(lockAddr, progID);
                    ProgramState.writeLock.unlock();  // update lock table, unlock
                    return null;
                }
            }
            else {
                ProgramState.writeLock.unlock();  // failure, unlock
                throw new StandardException(String.format("Lock address (%s) not existent.", String.valueOf(lockAddr)));
            }

        }
        else
            throw new StandardException(String.format("Lock name (%s) is not defined.", lockVarName));

    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        if (!typeEnv.lookup(lockVarName).equals(new IntType()))
            throw new StandardException("Mutex Address expected to be of type int.");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "Lock Mutex (" + lockVarName + ")";
    }
}
