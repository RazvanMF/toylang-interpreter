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
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenRFileStatement implements IStatement {
    IExpression expr;

    public OpenRFileStatement(IExpression e) {
        expr = e;
    }
    @Override
    public ProgramState execute(ProgramState state) throws StandardException, FileNotFoundException {
        StackADT<IStatement> exeStack = state.getExeStack();
        DictionaryADT<StringValue, BufferedReader> fileTable = state.getFileTable();

        IValue exprResult = expr.evaluate(state.getSymTable(), state.getHeap());
        if (exprResult instanceof StringValue exprToStr) {
            String exprValue = exprToStr.getValue();
            if (fileTable.isDefined(exprToStr))
                throw new StandardException(String.format("File name %s already exists.", exprValue));
            try {
                BufferedReader fileDescriptor = new BufferedReader(new FileReader(exprValue));
                fileTable.put(exprToStr, fileDescriptor);
            }
            catch (FileNotFoundException e) {
                throw new StandardException("File not found.");
            }



        }
        else
            throw new StandardException(String.format("Expected string type for file name. Received %s",
                    exprResult.getType().toString()));

        //return state;
        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        expr.typecheck(typeEnv);
        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("Open File Descriptor in %s", expr.toString());
    }
}
