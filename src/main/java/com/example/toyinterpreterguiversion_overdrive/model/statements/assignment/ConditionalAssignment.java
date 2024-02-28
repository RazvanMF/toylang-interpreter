package com.example.toyinterpreterguiversion_overdrive.model.statements.assignment;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.IExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.logic.IfStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;

import java.io.IOException;

public class ConditionalAssignment implements IStatement {

    String id;
    IExpression condition, value_if, value_else;

    public ConditionalAssignment(String i, IExpression cond, IExpression v1, IExpression v2) {
        id = i; condition = cond; value_if = v1; value_else = v2;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        IStatement ifAssigner = new IfStatement(condition,
                new AssignStatement(id, value_if), new AssignStatement(id, value_else));
        StackADT<IStatement> exeStack = state.getExeStack();
        exeStack.push(ifAssigner);
        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return String.format("%s = (%s) ? %s : %s", id, condition.toString(), value_if.toString(), value_else.toString());
    }
}
