package com.example.toyinterpreterguiversion_overdrive.model.statements.loops;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.IExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.BoolType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.values.BoolValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.HeapADT;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;

import java.io.IOException;

public class WhileStatement implements IStatement {
    IExpression condition;
    IStatement statementToExecute;

    public WhileStatement(IExpression cond, IStatement stExec) {
        condition = cond;
        statementToExecute = stExec;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        StackADT<IStatement> exeStack = state.getExeStack();
        DictionaryADT<String, IValue> symTable = state.getSymTable();
        HeapADT<IValue> heap = state.getHeap();
        IValue conditionResult = condition.evaluate(symTable, heap);

        if (conditionResult.getType() instanceof BoolType) {
            boolean executor = ((BoolValue) conditionResult).getValue();
            if (executor) {
                exeStack.push(new WhileStatement(condition, statementToExecute));
                exeStack.push(statementToExecute);
            }
            //return state;
            return null;
        }
        else throw new StandardException("Expression was not evaluated as a boolean");
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        IType typexp = condition.typecheck(typeEnv);
        if (typexp.equals(new BoolType())) {
            statementToExecute.typecheck(typeEnv.deepcopy());
            return typeEnv;
        }
        else
            throw new StandardException("The condition of WHILE does not have the type bool");
    }

    @Override
    public String toString() {
        return String.format("while (%s), do (%s)", condition.toString(), statementToExecute.toString());
    }
}
