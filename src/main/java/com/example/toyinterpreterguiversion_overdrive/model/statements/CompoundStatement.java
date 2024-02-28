package com.example.toyinterpreterguiversion_overdrive.model.statements;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.statements.logic.NoStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;

public class CompoundStatement implements IStatement {
    IStatement first;
    IStatement second;
    public CompoundStatement(IStatement st, IStatement nd) {
        first = st; second = nd;
    }

    @Override
    public String toString() {
        if (second instanceof NoStatement)
            return String.format("(%s%s)", first.toString(), second.toString());
        else
            return String.format("(%s; %s)", first.toString(), second.toString());
    }

    @Override
    public ProgramState execute(ProgramState state) {
        //changes only the execution stack
        StackADT<IStatement> stack = state.getExeStack();
        stack.push(second);
        stack.push(first);
        //return state;
        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        return second.typecheck(first.typecheck(typeEnv));
    }

    public IStatement getFirstStatement() {return first; }
    public IStatement getSecondStatement() {return second; }
}
