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
import com.example.toyinterpreterguiversion_overdrive.utils.semaphoretable.SemaphoreTableADT;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateSemaphoreStatement implements IStatement {
    String variableName;
    IExpression maxUsersOfSemaphore;

    public CreateSemaphoreStatement(String variableName, IExpression maxUsersOfSemaphore) {
        this.variableName = variableName;
        this.maxUsersOfSemaphore = maxUsersOfSemaphore;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        IValue result = maxUsersOfSemaphore.evaluate(state.getSymTable(), state.getHeap());
        if (result.getType().equals(new IntType())) {
            int maxUsers = ((IntValue) result).getValue();
            SemaphoreTableADT<Pair<Integer, List<Integer>>> semTable = ProgramState.getSemaphoreTable();
            DictionaryADT<String, IValue> symTable = state.getSymTable();
            if (symTable.isDefined(variableName)) {
                ProgramState.writeLock.lock();
                int semAddr = semTable.put(new Pair<Integer, List<Integer>>(maxUsers, new ArrayList<Integer>()));
                symTable.update(variableName, new IntValue(semAddr));
                ProgramState.writeLock.unlock();
            }
            else
                throw new StandardException("Semaphore holder in symTable is not defined");
        }
        else
            throw new StandardException("Number of users parameter expected to be of type int.");
        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        if (maxUsersOfSemaphore.typecheck(typeEnv).equals(new IntType()) &&
                typeEnv.lookup(variableName).equals(new IntType()))
            return typeEnv;
        throw new StandardException("Number of users and semaphore holder parameter expected to be of type int.");
    }

    @Override
    public String toString() {
        return String.format("Create Semaphore in variable (%s), that can handle %s threads", variableName,
                maxUsersOfSemaphore);
    }
}
