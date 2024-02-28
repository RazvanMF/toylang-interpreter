package com.example.toyinterpreterguiversion_overdrive.view.commands;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.ArithmeticExpression;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.ValueExpression;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.VariableLookupExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.*;
import com.example.toyinterpreterguiversion_overdrive.model.statements.assignment.AssignStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.PrintStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.VariableDeclarationStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.wait_operations.WaitStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;

import java.io.IOException;

public class Pr9Command extends Command {
    public Pr9Command(String k, String v) {
        super(k, v);
    }

    @Override
    public void create() throws StandardException, IOException, InterruptedException {
        //v = 20
        //wait(10)
        //print(v*10)
        IStatement line1 = new VariableDeclarationStatement("v", new IntType());
        IStatement line2 = new AssignStatement("v", new ValueExpression(new IntValue(20)));
        IStatement line3 = new WaitStatement(new IntValue(10));
        IStatement line4 = new PrintStatement(new ArithmeticExpression('*',
                new VariableLookupExpression("v"), new ValueExpression(new IntValue(10))));

        IStatement ex9 = new CompoundStatement(line1,
                new CompoundStatement(line2,
                        new CompoundStatement(line3, line4)));
        program = ex9;
        //programExecutor(ex9);
    }
}
