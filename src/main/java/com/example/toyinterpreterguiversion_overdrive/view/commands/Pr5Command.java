package com.example.toyinterpreterguiversion_overdrive.view.commands;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.HeapReadingExpression;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.ValueExpression;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.VariableLookupExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.*;
import com.example.toyinterpreterguiversion_overdrive.model.statements.heap.HeapAllocationStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.heap.HeapWritingStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.PrintStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.VariableDeclarationStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.types.ReferenceType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;

import java.io.IOException;

public class Pr5Command extends Command {

    public Pr5Command(String k, String v) {
        super(k, v);
    }

    @Override
    public void create() throws StandardException, IOException, InterruptedException {
        IStatement line1 = new VariableDeclarationStatement("v", new ReferenceType(new IntType()));
        IStatement line2 = new HeapAllocationStatement("v", new ValueExpression(new IntValue(20)));
        IStatement line3 = new PrintStatement(new HeapReadingExpression(new VariableLookupExpression("v")));

        IStatement line4 = new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType())));
        IStatement line5 = new HeapAllocationStatement("a", new VariableLookupExpression("v"));

        IStatement line6 = new VariableDeclarationStatement("r", new ReferenceType(new ReferenceType(new ReferenceType(new IntType()))));
        IStatement line7 = new HeapAllocationStatement("r", new VariableLookupExpression("a"));

        IStatement line8 = new HeapWritingStatement("v", new ValueExpression(new IntValue(40)));
        IStatement line9 = new PrintStatement(new HeapReadingExpression(new VariableLookupExpression("v")));
        IStatement line10 = new PrintStatement(new HeapReadingExpression(new VariableLookupExpression("a")));
        IStatement line11 = new PrintStatement(new HeapReadingExpression(new HeapReadingExpression(new VariableLookupExpression("a"))));

        IStatement ex5 = new CompoundStatement(line1,
                new CompoundStatement(line2,
                        new CompoundStatement(line3,
                                new CompoundStatement(line4,
                                        new CompoundStatement(line5,
                                                new CompoundStatement(line6,
                                                        new CompoundStatement(line7,
                                                                new CompoundStatement(line8,
                                                                        new CompoundStatement(line9,
                                                                                new CompoundStatement(line10, line11))))))))));
        program = ex5;
        //programExecutor(ex5);
    }
}
