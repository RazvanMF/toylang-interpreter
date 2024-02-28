package com.example.toyinterpreterguiversion_overdrive.view.commands;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.ValueExpression;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.VariableLookupExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.*;
import com.example.toyinterpreterguiversion_overdrive.model.statements.assignment.AssignStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.PrintStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.VariableDeclarationStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;

import java.io.IOException;

public class Pr1Command extends Command{
    public Pr1Command(String k, String v) {
        super(k, v);
    }

    @Override
    public void create() throws StandardException, IOException, InterruptedException {
        IStatement ex1= new CompoundStatement(new VariableDeclarationStatement("v",new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableLookupExpression("v"))));

        program = ex1;
        //programExecutor(ex1);
    }
}
