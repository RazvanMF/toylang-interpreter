package com.example.toyinterpreterguiversion_overdrive.view.commands;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.LogicExpression;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.ValueExpression;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.VariableLookupExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.CompoundStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.assignment.AssignStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.assignment.ConditionalAssignment;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.PrintStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.VariableDeclarationStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.BoolType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.values.BoolValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;

import java.io.IOException;

public class Pr11Command extends Command {
    public Pr11Command(String k, String v) {
        super(k, v);
    }

    @Override
    public void create() throws StandardException, IOException, InterruptedException {
        //bool b;
        //int c;
        //b=true;
        //c=b?100:200;
        //print(c);
        //c= (false)?100:200;
        //print(c);
        IStatement line1 = new VariableDeclarationStatement("b", new BoolType());
        IStatement line2 = new VariableDeclarationStatement("c", new IntType());
        IStatement line3 = new AssignStatement("b", new ValueExpression(new BoolValue(true)));
        IStatement line4 = new ConditionalAssignment("c", new VariableLookupExpression("b"),
                new ValueExpression(new IntValue(100)), new ValueExpression(new IntValue(200)));
        IStatement line5 = new PrintStatement(new VariableLookupExpression("c"));
        IStatement line6 = new ConditionalAssignment("c", new ValueExpression(new BoolValue(false)),
                new ValueExpression(new IntValue(10)), new ValueExpression(new IntValue(20)));
        IStatement line7 = new PrintStatement(new VariableLookupExpression("c"));

        IStatement ex11 = new CompoundStatement(line1,
                new CompoundStatement(line2,
                        new CompoundStatement(line3,
                                new CompoundStatement(line4,
                                        new CompoundStatement(line5,
                                                new CompoundStatement(line6, line7))))));

        program = ex11;
        //programExecutor(ex11);

    }
}
