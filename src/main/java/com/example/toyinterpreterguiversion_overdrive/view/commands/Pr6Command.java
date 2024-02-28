package com.example.toyinterpreterguiversion_overdrive.view.commands;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.*;
import com.example.toyinterpreterguiversion_overdrive.model.statements.*;
import com.example.toyinterpreterguiversion_overdrive.model.statements.assignment.AssignStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.PrintStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.VariableDeclarationStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.loops.WhileStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;

import java.io.IOException;

public class Pr6Command extends Command {

    public Pr6Command(String k, String v) {
        super(k, v);
    }

    @Override
    public void create() throws StandardException, IOException, InterruptedException {
        //int v; v=4; (while (v>0) print(v);v=v-1);print(v);
        IStatement line1 = new VariableDeclarationStatement("v", new IntType());
        IStatement line2 = new AssignStatement("v", new ValueExpression(new IntValue(4)));
        IStatement whilebracket = new CompoundStatement(
                new PrintStatement(new VariableLookupExpression("v")),
                new AssignStatement("v", new ArithmeticExpression('-', new VariableLookupExpression("v"), new ValueExpression(new IntValue(1)))));
        IStatement line3 = new WhileStatement(new RelationalExpression(">", new VariableLookupExpression("v"), new ValueExpression(new IntValue(0))), whilebracket);
        IStatement line4 = new PrintStatement(new VariableLookupExpression("v"));


        IStatement ex6 = new CompoundStatement(line1,
                new CompoundStatement(line2,
                        new CompoundStatement(line3, line4)));

        program = ex6;
        //programExecutor(ex6);
    }
}
