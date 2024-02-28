package com.example.toyinterpreterguiversion_overdrive.model.statements.procedures;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.IExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.list.ListADT;
import com.example.toyinterpreterguiversion_overdrive.utils.proceduretable.ProcedureTableADT;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;
import javafx.util.Pair;

import java.io.IOException;

public class ProcedureExecutionStatement implements IStatement {
    String procedureName;
    ListADT<IExpression> givenParameters;
    ListADT<String> expectedParameters; //inside the ProcTableADT
    IStatement stToExecute; //inside the ProcTableADT

    public ProcedureExecutionStatement(String procName, ListADT<IExpression> params) {
        procedureName = procName;
        givenParameters = params;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        ProcedureTableADT<String, Pair<ListADT<String>, IStatement>> procTable = ProgramState.getProcedureTable();
        if (procTable.isDefined(procedureName)) {
            expectedParameters = procTable.lookup(procedureName).getKey();
            stToExecute = procTable.lookup(procedureName).getValue();

            ListADT<IValue> results = new ListADT<>();
            DictionaryADT<String, IValue> procSymTable = new DictionaryADT<>();

            for (IExpression expr : givenParameters.getList()) {
                results.add(expr.evaluate(state.getSymTable(), state.getHeap()));
            }

            for (int i = 0; i < expectedParameters.getList().size(); i++) {
                procSymTable.put(expectedParameters.getList().get(i),
                        results.getList().get(i));
            }

            StackADT<DictionaryADT<String, IValue>> symTableStack = state.getSymTableStack();
            symTableStack.push(procSymTable);

            StackADT<IStatement> exeStack = state.getExeStack();
            exeStack.push(new ReturnStatement());
            exeStack.push(stToExecute);
        }
        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        return typeEnv;
        // AT MOST, YOU CAN VERIFY IF THE VARIABLES HAVE THE DESIRED TYPE, BUT SINCE THIS IS A PYTHON/C MONSTROSITY,
        // WE DID NOT DO OUR PROCEDURES W/ VARIABLE TYPES IN MIND! MAYBE NEXT PATCH
    }

    private String listFormatter() {
        StringBuilder out = new StringBuilder();
        for (IExpression name : givenParameters.getList()) {
            out.append(name); out.append(", ");
        }
        out.setLength(out.length() - 2);
        return out.toString();
    }

    @Override
    public String toString() {
        return String.format("call %s(%s)", procedureName, listFormatter());
    }
}
