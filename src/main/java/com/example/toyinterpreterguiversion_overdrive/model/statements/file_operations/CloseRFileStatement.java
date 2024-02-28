package com.example.toyinterpreterguiversion_overdrive.model.statements.file_operations;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.IExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.StringValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFileStatement implements IStatement {
    IExpression expr;
    public CloseRFileStatement(IExpression e) {
        expr = e;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        StackADT<IStatement> stack = state.getExeStack();
        DictionaryADT<StringValue, BufferedReader> fileTable = state.getFileTable();

        IValue exprResult = expr.evaluate(state.getSymTable(), state.getHeap());
        if (exprResult instanceof StringValue exprToStr) {
            BufferedReader fileDescriptor = fileTable.lookup(exprToStr);
            fileDescriptor.close();
            fileTable.remove(exprToStr);
            //return state;
            return null;
        }
        else
            throw new StandardException(String.format("Expected string type for file name. Received %s",
                    exprResult.getType().toString()));
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        expr.typecheck(typeEnv);
        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("Close File Descriptor from %s", expr.toString());
    }
}
