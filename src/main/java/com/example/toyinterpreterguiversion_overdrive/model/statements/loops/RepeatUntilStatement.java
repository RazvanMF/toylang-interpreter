package com.example.toyinterpreterguiversion_overdrive.model.statements.loops;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.IExpression;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.LogicNotExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.CompoundStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.BoolType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;

import java.io.IOException;

public class RepeatUntilStatement implements IStatement {
    IExpression expr;
    IStatement body;

    public RepeatUntilStatement(IExpression exp, IStatement bdy) {
        expr = exp;
        body = bdy;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        IStatement flippedWhile = new CompoundStatement(body, new WhileStatement(new LogicNotExpression(expr), body));

        StackADT<IStatement> exeStack = state.getExeStack();
        exeStack.push(flippedWhile);
        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        if (expr.typecheck(typeEnv).equals(new BoolType())) {
            return body.typecheck(typeEnv);
        }
        else
            throw new StandardException("Expression condition expected to be of type bool.");
    }

    @Override
    public String toString() {
        return String.format("repeat (%s), until (%s)", body.toString(), expr.toString());
    }
}
