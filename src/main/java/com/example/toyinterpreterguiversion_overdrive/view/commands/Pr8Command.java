package com.example.toyinterpreterguiversion_overdrive.view.commands;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.ArithmeticExpression;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.ValueExpression;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.VariableLookupExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.*;
import com.example.toyinterpreterguiversion_overdrive.model.statements.assignment.AssignStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.concurrency.ForkStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.PrintStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.VariableDeclarationStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.wait_operations.SleepStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;

import java.io.IOException;

public class Pr8Command extends Command {
    public Pr8Command(String k, String v) {
        super(k, v);
    }

    @Override
    public void create() throws StandardException, IOException, InterruptedException {
        //v = 10
        //fork (v = v - 1, v = v - 1, print(v))
        //sleep(10)
        //print(v * 10)
        IStatement line1 = new VariableDeclarationStatement("v", new IntType());
        IStatement line2 = new AssignStatement("v", new ValueExpression(new IntValue(10)));

        IStatement forkline1 = new AssignStatement("v", new ArithmeticExpression('-',
                new VariableLookupExpression("v"), new ValueExpression(new IntValue(1))));
        IStatement forkline2 = new AssignStatement("v", new ArithmeticExpression('-',
                new VariableLookupExpression("v"), new ValueExpression(new IntValue(1))));
        IStatement forkline3 = new PrintStatement(new VariableLookupExpression("v"));
        IStatement forkProgram = new CompoundStatement(forkline1,
                new CompoundStatement(forkline2, forkline3));

        IStatement line3 = new ForkStatement(forkProgram);
        IStatement line4 = new SleepStatement(new IntValue(10));
        IStatement line5 = new PrintStatement(new ArithmeticExpression('*',
                new VariableLookupExpression("v"), new ValueExpression(new IntValue(10))));

        IStatement ex8 = new CompoundStatement(line1,
                new CompoundStatement(line2,
                        new CompoundStatement(line3,
                                new CompoundStatement(line4, line5))));
        program = ex8;
        //programExecutor(ex8);
    }
}
