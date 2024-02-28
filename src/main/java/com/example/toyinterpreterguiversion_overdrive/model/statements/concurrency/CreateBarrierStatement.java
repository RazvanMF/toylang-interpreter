package com.example.toyinterpreterguiversion_overdrive.model.statements.concurrency;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.IExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;
import com.example.toyinterpreterguiversion_overdrive.utils.barriertable.BarrierTableADT;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateBarrierStatement implements IStatement {
    String barrierKey;
    IExpression capacity;

    public CreateBarrierStatement(String barrierKey, IExpression capacity) {
        this.barrierKey = barrierKey;
        this.capacity = capacity;
    }


    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        DictionaryADT<String, IValue> symTable = state.getSymTable();
        IValue capacityEvaluation = capacity.evaluate(symTable, state.getHeap());
        if (capacityEvaluation != null && capacityEvaluation.getType().equals(new IntType())) {
            int capacityVal = ((IntValue) capacityEvaluation).getValue();

            ProgramState.writeLock.lock();

            BarrierTableADT<Pair<Integer, List<Integer>>> barrierTable = ProgramState.getBarrierTable();
            int addr = barrierTable.put(new Pair<Integer, List<Integer>>(capacityVal, new ArrayList<>()));
            if (symTable.isDefined(barrierKey)) {
                symTable.update(barrierKey, new IntValue(addr));
            }
            else {
                symTable.put(barrierKey, new IntValue(addr));
            }

            ProgramState.writeLock.unlock();
        }
        else
            throw new StandardException("Capacity value expected to be of type int.");

        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        if (typeEnv.lookup(barrierKey).equals(new IntType()) && capacity.typecheck(typeEnv).equals(new IntType())) {
            return typeEnv;
        }
        throw new StandardException("Barrier holder and capacity variables expected to be int.");


    }

    @Override
    public String toString() {
        return String.format("Create barrier in (%s), with capacity = %s", barrierKey, capacity);
    }
}
