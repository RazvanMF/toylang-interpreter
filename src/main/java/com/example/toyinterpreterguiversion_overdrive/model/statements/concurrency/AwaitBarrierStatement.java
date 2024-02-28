package com.example.toyinterpreterguiversion_overdrive.model.statements.concurrency;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;
import com.example.toyinterpreterguiversion_overdrive.utils.barriertable.BarrierTableADT;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;

public class AwaitBarrierStatement implements IStatement {
    String barrierKey;

    public AwaitBarrierStatement(String barrierKey) {
        this.barrierKey = barrierKey;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        StackADT<IStatement> exeStack = state.getExeStack();
        DictionaryADT<String, IValue> symTable = state.getSymTable();
        IValue barrierKeyLookup = symTable.lookup(barrierKey);
        if (barrierKeyLookup != null && barrierKeyLookup.getType().equals(new IntType())) {
            int barrierAddr = ((IntValue) barrierKeyLookup).getValue();

            ProgramState.writeLock.lock();

            BarrierTableADT<Pair<Integer, List<Integer>>> barrierTable = ProgramState.getBarrierTable();
            Pair<Integer, List<Integer>> barrierTuple = barrierTable.lookup(barrierAddr);
            if (barrierTuple != null) {
                int capacity = barrierTuple.getKey();
                List<Integer> members = barrierTuple.getValue();
                int memlen = members.size();
                int prgID = state.getPersonalID();

                if (capacity > memlen) {
                    if (members.contains(prgID)) {
                        exeStack.push(new AwaitBarrierStatement(barrierKey));
                    }
                    else {
                        members.add(prgID);
                        exeStack.push(new AwaitBarrierStatement(barrierKey));
                    }
                }

                ProgramState.writeLock.unlock();
            }
            else {
                ProgramState.writeLock.unlock();
                throw new StandardException("Barrier address does not exist in barrierTable.");
            }
        }
        else
            throw new StandardException("Barrier holder is not defined in symTable.");

        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "Wait at barrier (" + barrierKey + ") until the required number of threads arrive";
    }
}
