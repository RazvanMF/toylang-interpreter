package com.example.toyinterpreterguiversion_overdrive.view.commands;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.ValueExpression;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.VariableLookupExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.*;
import com.example.toyinterpreterguiversion_overdrive.model.statements.assignment.AssignStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.file_operations.CloseRFileStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.file_operations.OpenRFileStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.file_operations.ReadFileStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.PrintStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.VariableDeclarationStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.logic.NoStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.types.StringType;
import com.example.toyinterpreterguiversion_overdrive.model.values.StringValue;

import java.io.IOException;

public class Pr4Command extends Command {

    public Pr4Command(String k, String v) {
        super(k, v);
    }

    @Override
    public void create() throws StandardException, IOException, InterruptedException {
        IStatement line1 = new VariableDeclarationStatement("varf", new StringType());
        IStatement line2 = new AssignStatement("varf", new ValueExpression(new StringValue("test.in")));
        IStatement line3 = new OpenRFileStatement(new VariableLookupExpression("varf"));
        IStatement line4 = new VariableDeclarationStatement("varc", new IntType());
        IStatement line5 = new CompoundStatement(new ReadFileStatement(new VariableLookupExpression("varf"), "varc"),
                new PrintStatement(new VariableLookupExpression("varc")));
        IStatement line6 = new CompoundStatement(new ReadFileStatement(new VariableLookupExpression("varf"), "varc"),
                new PrintStatement(new VariableLookupExpression("varc")));
        IStatement line7 = new CloseRFileStatement(new VariableLookupExpression("varf"));

        IStatement ex4 = new CompoundStatement(line1,
                new CompoundStatement(line2,
                        new CompoundStatement(line3,
                                new CompoundStatement(line4,
                                        new CompoundStatement(line5,
                                                new CompoundStatement(line6, line7))))));

        program = ex4;
        //programExecutor(ex4);
    }
}
