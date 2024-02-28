package com.example.toyinterpreterguiversion_overdrive.model.statements.logic;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;

public class NoStatement implements IStatement {

    @Override
    public ProgramState execute(ProgramState state) {
        //return state;
        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "";
    }

}
