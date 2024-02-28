package com.example.toyinterpreterguiversion_overdrive.view.commands;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.ValueExpression;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.VariableLookupExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.*;
import com.example.toyinterpreterguiversion_overdrive.model.statements.assignment.AssignStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.PrintStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.VariableDeclarationStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.logic.IfStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.BoolType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.values.BoolValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;

import java.io.IOException;

public class Pr3Command extends Command {
    public Pr3Command(String k, String v) {
        super(k, v);
    }

    @Override
    public void create() throws StandardException, IOException, InterruptedException {
        IStatement ex3 = new CompoundStatement(new VariableDeclarationStatement("a",new BoolType()),
                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(new AssignStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompoundStatement(new IfStatement(new VariableLookupExpression("a"),
                                        new AssignStatement("v",new ValueExpression(new IntValue(2))),
                                        new AssignStatement("v", new ValueExpression(new IntValue(3)))),
                                        new PrintStatement(new VariableLookupExpression("v"))))));

        program = ex3;
        //programExecutor(ex3);
    }
}
