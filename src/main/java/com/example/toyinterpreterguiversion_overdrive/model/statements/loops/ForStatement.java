package com.example.toyinterpreterguiversion_overdrive.model.statements.loops;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.IExpression;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.ValueExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.CompoundStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.assignment.AssignStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.VariableDeclarationStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.BoolType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;

import java.io.IOException;

public class ForStatement implements IStatement {
    String varID;
    IValue assignmentValue;
    IExpression comparison;
    IExpression changeRatio;
    IStatement forBody;
    public ForStatement(String varID, IValue assignmentValue, IExpression comparison, IExpression changeRatio, IStatement forBody) {
        this.varID = varID;
        this.assignmentValue = assignmentValue;
        this.comparison = comparison;
        this.changeRatio = changeRatio;
        this.forBody = forBody;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        IStatement whileBody = new CompoundStatement(forBody, new AssignStatement(varID, changeRatio));
        IStatement whileExec = new WhileStatement(comparison, whileBody);
        IStatement fullForBody = new CompoundStatement(new VariableDeclarationStatement(varID, new IntType()),
                new CompoundStatement(new AssignStatement(varID, new ValueExpression(assignmentValue)),
                        whileExec));

        StackADT<IStatement> exeStack = state.getExeStack();
        exeStack.push(fullForBody);
        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        typeEnv.put(varID, new IntType());
        if (comparison.typecheck(typeEnv).equals(new BoolType()) && assignmentValue.getType().equals(new IntType())
            && changeRatio.typecheck(typeEnv).equals(new IntType()))
            return typeEnv;
        else
            throw new StandardException("expected type for comparison is boolean and for assignment value is int");
    }

    @Override
    public String toString() {
        return String.format("for (int %s = %s; %s; %s = %s) do {%s}", varID, assignmentValue, comparison, varID, changeRatio, forBody);
    }
}
