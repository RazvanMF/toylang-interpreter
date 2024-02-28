package com.example.toyinterpreterguiversion_overdrive.model.statements.concurrency;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;

public class ReleaseSemaphoreStatement implements IStatement {
    String semaphoreKey;
    public ReleaseSemaphoreStatement(String semKey) {
        semaphoreKey = semKey;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        IValue semAddrInSym = state.getSymTable().lookup(semaphoreKey);
        if (semAddrInSym != null && semAddrInSym.getType().equals(new IntType())) {
            int semAddr = ((IntValue) semAddrInSym).getValue();
            ProgramState.writeLock.lock();
            Pair<Integer, List<Integer>> semTuple = ProgramState.getSemaphoreTable().lookup(semAddr);
            if (semTuple != null) {
                int maxUsers = semTuple.getKey();
                List<Integer> members = semTuple.getValue();
                int memLen = members.size();
                int stateID = state.getPersonalID();

                if (members.contains(stateID)) {
                    members.remove(Integer.valueOf(stateID));
                }

                ProgramState.writeLock.unlock();
            }
            else {
                ProgramState.writeLock.unlock();
                throw new StandardException("Semaphore address does not exist.");
            }
        }
        else
            throw new StandardException("Semaphore holder undefined or not of type int in symTable.");

        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        if (typeEnv.lookup(semaphoreKey).equals(new IntType()))
            return typeEnv;
        else
            throw new StandardException("Semaphore holder expected to be of type int.");
    }

    @Override
    public String toString() {
        return "Release Semaphore defined in (" + semaphoreKey + ")";
    }
}
