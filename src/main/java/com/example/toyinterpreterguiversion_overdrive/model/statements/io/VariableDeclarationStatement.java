package com.example.toyinterpreterguiversion_overdrive.model.statements.io;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;

public class VariableDeclarationStatement implements IStatement {
    IType type;
    String name;

    public VariableDeclarationStatement(String id, IType ty) {
        name = id; type = ty;
    }

    @Override
    public String toString() {
        return String.format("%s %s", type.toString(), name);
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException {
        DictionaryADT<String, IValue> symTable = state.getSymTable();
        if (!symTable.isDefined(name))
            symTable.put(name, type.defaultValue());
        else throw new StandardException(String.format("Variable %s already declared.", name));
        //return state;
        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        typeEnv.put(name, type);
        return typeEnv;
    }
}
