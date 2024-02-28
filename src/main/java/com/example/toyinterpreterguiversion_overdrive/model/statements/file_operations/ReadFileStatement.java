package com.example.toyinterpreterguiversion_overdrive.model.statements.file_operations;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.IExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.StringValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.list.ListADT;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStatement implements IStatement {
    IExpression exprFileName;
    String nameIntValue;

    public ReadFileStatement(IExpression eFN, String nIV) {
        exprFileName = eFN;
        nameIntValue = nIV;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        StackADT<IStatement> exeStack = state.getExeStack();
        DictionaryADT<String, IValue> symTable = state.getSymTable();
        ListADT<IValue> out = state.getOutput();
        DictionaryADT<StringValue, BufferedReader> fileTable = state.getFileTable();

        if (symTable.isDefined(nameIntValue)) {
            IValue exprResult = exprFileName.evaluate(symTable, state.getHeap());
            if (exprResult instanceof StringValue exprToStr) {
                BufferedReader fileDescriptor = fileTable.lookup(exprToStr);
                String line = fileDescriptor.readLine();
                int expectedValue = (line != null ? Integer.parseInt(line) : 0);
                IntValue convertedValue = new IntValue(expectedValue);
                symTable.update(nameIntValue, convertedValue);
                //return state;
                return null;
            }
            else
                throw new StandardException(String.format("Expected string type for file name. Got %s.",
                        exprResult.getType().toString()));
        }
        else
            throw new StandardException(String.format("Name %s not found in table.", nameIntValue));
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        exprFileName.typecheck(typeEnv);
        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("Read from File Descriptor %s into %s", exprFileName.toString(), nameIntValue);
    }
}
