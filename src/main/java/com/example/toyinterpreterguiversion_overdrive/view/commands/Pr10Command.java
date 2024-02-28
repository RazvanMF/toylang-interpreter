package com.example.toyinterpreterguiversion_overdrive.view.commands;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.ArithmeticExpression;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.IExpression;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.ValueExpression;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.VariableLookupExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.*;
import com.example.toyinterpreterguiversion_overdrive.model.statements.assignment.AssignStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.concurrency.ForkStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.PrintStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.VariableDeclarationStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.procedures.ProcedureDeclarationStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.procedures.ProcedureExecutionStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;
import com.example.toyinterpreterguiversion_overdrive.utils.list.ListADT;

import java.io.IOException;

public class Pr10Command extends Command {
    public Pr10Command(String k, String v) {
        super(k, v);
    }

    @Override
    public void create() throws StandardException, IOException, InterruptedException {
        //procedure sum(a,b) v=a+b;print(v)
        //procedure product(a,b) v=a*b;print(v)
        //and the main program is
        //v=2;w=5;call sum(v*10,w);print(v);
        //fork(call product(v,w);
        //fork(call sum(v,w)))

        IStatement sumLine1 = new VariableDeclarationStatement("v", new IntType());
        IStatement sumLine2 = new AssignStatement("v", new ArithmeticExpression('+',
                new VariableLookupExpression("a"), new VariableLookupExpression("b")));
        IStatement sumLine3 = new PrintStatement(new VariableLookupExpression("v"));
        IStatement sumBody = new CompoundStatement(sumLine1, new CompoundStatement(sumLine2, sumLine3));

        IStatement prodLine1 = new VariableDeclarationStatement("v", new IntType());
        IStatement prodLine2 = new AssignStatement("v", new ArithmeticExpression('*',
                new VariableLookupExpression("a"), new VariableLookupExpression("b")));
        IStatement prodLine3 = new PrintStatement(new VariableLookupExpression("v"));
        IStatement prodBody = new CompoundStatement(prodLine1, new CompoundStatement(prodLine2, prodLine3));

        ListADT<String> variables = new ListADT<>();
        variables.add("a"); variables.add("b");
        IStatement sumDeclare = new ProcedureDeclarationStatement("sum", variables, sumBody);
        IStatement prodDeclare = new ProcedureDeclarationStatement("prod", variables, prodBody);

        IStatement line1 = new VariableDeclarationStatement("v", new IntType());
        IStatement line2 = new AssignStatement("v", new ValueExpression(new IntValue(2)));
        IStatement line3 = new VariableDeclarationStatement("w", new IntType());
        IStatement line4 = new AssignStatement("w", new ValueExpression(new IntValue(5)));

        ListADT<IExpression> sumparams = new ListADT<>();
        sumparams.add(new ArithmeticExpression('*',
                new VariableLookupExpression("v"), new ValueExpression(new IntValue(10))));
        sumparams.add(new VariableLookupExpression("w"));

        IStatement line5 = new ProcedureExecutionStatement("sum", sumparams);
        IStatement line6 = new PrintStatement(new VariableLookupExpression("v"));

        ListADT<IExpression> sumforkparams = new ListADT<>();
        sumforkparams.add(new VariableLookupExpression("v"));
        sumforkparams.add(new VariableLookupExpression("w"));
        IStatement forkbody1 = new ProcedureExecutionStatement("sum", sumforkparams);
        IStatement line7 = new ForkStatement(forkbody1);

        ListADT<IExpression> prodforkparams = new ListADT<>();
        prodforkparams.add(new VariableLookupExpression("v"));
        prodforkparams.add(new VariableLookupExpression("w"));
        IStatement forkbody2 = new ProcedureExecutionStatement("prof", sumforkparams);
        IStatement line8 = new ForkStatement(forkbody2);

        IStatement ex10 = new CompoundStatement(sumDeclare,
                new CompoundStatement(prodDeclare,
                        new CompoundStatement(line1,
                                new CompoundStatement(line2,
                                        new CompoundStatement(line3,
                                                new CompoundStatement(line4,
                                                        new CompoundStatement(line5,
                                                                new CompoundStatement(line6,
                                                                        new CompoundStatement(line7, line8)))))))));

        program = ex10;
        //programExecutor(ex10);
    }
}
