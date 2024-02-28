package com.example.toyinterpreterguiversion_overdrive.view.commands;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.*;
import com.example.toyinterpreterguiversion_overdrive.model.statements.*;
import com.example.toyinterpreterguiversion_overdrive.model.statements.assignment.AssignStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.concurrency.ForkStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.heap.HeapAllocationStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.heap.HeapWritingStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.PrintStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.VariableDeclarationStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.types.ReferenceType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;

import java.io.IOException;

public class Pr7Command extends Command {

    public Pr7Command(String k, String v) {
        super(k, v);
    }

    @Override
    public void create() throws StandardException, IOException, InterruptedException {
        // int v; Ref int a; v=10;new(a,22);
        // fork(wH(a,30);v=32;print(v);print(rH(a)));
        // print(v);print(rH(a))

        IStatement line1 = new VariableDeclarationStatement("v", new IntType());
        IStatement line2 = new VariableDeclarationStatement("a", new ReferenceType(new IntType()));
        IStatement line3 = new AssignStatement("v", new ValueExpression(new IntValue(10)));
        IStatement line4 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(22)));

        IStatement forkline1 = new HeapWritingStatement("a", new ValueExpression(new IntValue(30)));
        IStatement forkline2 = new AssignStatement("v", new ValueExpression(new IntValue(32)));
        IStatement forkline3 = new PrintStatement(new VariableLookupExpression("v"));
        IStatement forkline4 = new PrintStatement(new HeapReadingExpression(new VariableLookupExpression("a")));
        IStatement line5 = new ForkStatement(new CompoundStatement(forkline1,
                new CompoundStatement(forkline2,
                        new CompoundStatement(forkline3, forkline4))));

        IStatement line6 = new PrintStatement(new VariableLookupExpression("v"));
        IStatement line7 = new PrintStatement(new HeapReadingExpression(new VariableLookupExpression("a")));

        IStatement ex7 = new CompoundStatement(line1,
                new CompoundStatement(line2,
                        new CompoundStatement(line3,
                                new CompoundStatement(line4,
                                        new CompoundStatement(line5,
                                                new CompoundStatement(line6, line7))))));

        program = ex7;
        //programExecutor(ex7);
    }
}
