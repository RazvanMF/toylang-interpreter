package com.example.toyinterpreterguiversion_overdrive.model.statements;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;

import java.io.IOException;

public interface IStatement {
    ProgramState execute(ProgramState state) throws StandardException, IOException;
    // statements add expressions to the program, so we'll work with the typeEnv 
    DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException;
}
