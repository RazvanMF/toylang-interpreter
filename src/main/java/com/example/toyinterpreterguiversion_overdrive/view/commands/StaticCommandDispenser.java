package com.example.toyinterpreterguiversion_overdrive.view.commands;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.*;
import com.example.toyinterpreterguiversion_overdrive.model.statements.CompoundStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.assignment.AssignStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.assignment.ConditionalAssignment;
import com.example.toyinterpreterguiversion_overdrive.model.statements.concurrency.*;
import com.example.toyinterpreterguiversion_overdrive.model.statements.file_operations.CloseRFileStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.file_operations.OpenRFileStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.file_operations.ReadFileStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.heap.HeapAllocationStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.heap.HeapWritingStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.PrintStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.io.VariableDeclarationStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.logic.IfStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.logic.NoStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.logic.SwitchStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.loops.ForStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.loops.RepeatUntilStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.loops.WhileStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.procedures.ProcedureDeclarationStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.procedures.ProcedureExecutionStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.wait_operations.SleepStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.wait_operations.WaitStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.BoolType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.types.ReferenceType;
import com.example.toyinterpreterguiversion_overdrive.model.types.StringType;
import com.example.toyinterpreterguiversion_overdrive.model.values.BoolValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.StringValue;
import com.example.toyinterpreterguiversion_overdrive.utils.list.ListADT;

import java.util.LinkedHashMap;
import java.util.Map;

public class StaticCommandDispenser {

    public static IStatement basicProgramStatement() {
        IStatement ex1= new CompoundStatement(new VariableDeclarationStatement("v",new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableLookupExpression("v"))));
        return ex1;
    }

    public static IStatement intermediateProgramStatement() {
        IStatement ex2 = new CompoundStatement(new VariableDeclarationStatement("a",new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("b",new IntType()),
                        new CompoundStatement(new AssignStatement("a", new ArithmeticExpression('+',
                                new ValueExpression(new IntValue(2)),
                                new ArithmeticExpression('*',new ValueExpression(new IntValue(3)),
                                        new ValueExpression(new IntValue(5))))),
                                new CompoundStatement(new AssignStatement("b",new ArithmeticExpression('+',
                                        new VariableLookupExpression("a"),
                                        new ValueExpression(new IntValue(1)))),
                                        new PrintStatement(new VariableLookupExpression("b"))))));
        return ex2;
    }

    public static IStatement ifTestProgramStatement() {
        IStatement ex3 = new CompoundStatement(new VariableDeclarationStatement("a",new BoolType()),
                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(new AssignStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompoundStatement(new IfStatement(new VariableLookupExpression("a"),
                                        new AssignStatement("v",new ValueExpression(new IntValue(2))),
                                        new AssignStatement("v", new ValueExpression(new IntValue(3)))),
                                        new PrintStatement(new VariableLookupExpression("v"))))));

        return ex3;
    }

    public static IStatement fileOperationsProgramStatement() {
        IStatement line1 = new VariableDeclarationStatement("varf", new StringType());
        IStatement line2 = new AssignStatement("varf", new ValueExpression(new StringValue("test.in")));
        IStatement line3 = new OpenRFileStatement(new VariableLookupExpression("varf"));
        IStatement line4 = new VariableDeclarationStatement("varc", new IntType());
        IStatement line5 = new CompoundStatement(new ReadFileStatement(new VariableLookupExpression("varf"), "varc"),
                new PrintStatement(new VariableLookupExpression("varc")));
        IStatement line6 = new CompoundStatement(new ReadFileStatement(new VariableLookupExpression("varf"), "varc"),
                new PrintStatement(new VariableLookupExpression("varc")));
        IStatement line7 = new CloseRFileStatement(new VariableLookupExpression("varf"));
        IStatement line8 = new NoStatement();

        IStatement ex4 = new CompoundStatement(line1,
                new CompoundStatement(line2,
                        new CompoundStatement(line3,
                                new CompoundStatement(line4,
                                        new CompoundStatement(line5,
                                                new CompoundStatement(line6, line7))))));

        return ex4;
    }

    public static IStatement heapOperationsProgramStatement() {
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
        return ex5;
    }

    public static IStatement whileTestProgramStatement() {
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

        return ex6;
    }

    public static IStatement forkTestProgramStatement() throws StandardException {
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
        return ex7;
    }

    public static IStatement sleepTestProgramStatement() throws StandardException {
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
        return ex8;
    }
    public static IStatement waitTestProgramStatement() throws StandardException {
        IStatement line1 = new VariableDeclarationStatement("v", new IntType());
        IStatement line2 = new AssignStatement("v", new ValueExpression(new IntValue(20)));
        IStatement line3 = new WaitStatement(new IntValue(10));
        IStatement line4 = new PrintStatement(new ArithmeticExpression('*',
                new VariableLookupExpression("v"), new ValueExpression(new IntValue(10))));

        IStatement ex9 = new CompoundStatement(line1,
                new CompoundStatement(line2,
                        new CompoundStatement(line3, line4)));

        return ex9;
    }

    public static IStatement procedureTestProgramStatement() throws StandardException {
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
        IStatement forkbody2 = new ProcedureExecutionStatement("prod", prodforkparams);
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

        return ex10;
    }

    public static IStatement ternaryStmtProgramStatement() {
        IStatement line1 = new VariableDeclarationStatement("b", new BoolType());
        IStatement line2 = new VariableDeclarationStatement("c", new IntType());
        IStatement line3 = new AssignStatement("b", new ValueExpression(new BoolValue(true)));
        IStatement line4 = new ConditionalAssignment("c", new VariableLookupExpression("b"),
                new ValueExpression(new IntValue(100)), new ValueExpression(new IntValue(200)));
        IStatement line5 = new PrintStatement(new VariableLookupExpression("c"));
        IStatement line6 = new ConditionalAssignment("c", new ValueExpression(new BoolValue(false)),
                new ValueExpression(new IntValue(100)), new ValueExpression(new IntValue(200)));
        IStatement line7 = new PrintStatement(new VariableLookupExpression("c"));

        IStatement ex11 = new CompoundStatement(line1,
                new CompoundStatement(line2,
                        new CompoundStatement(line3,
                                new CompoundStatement(line4,
                                        new CompoundStatement(line5,
                                                new CompoundStatement(line6, line7))))));

        return ex11;
    }

    public static IStatement switchCaseProgramStatement() {
        //int a; int b; int c;
        //a=1;b=2;c=5;
        //(switch(a*10)
        //(case (b*c) : print(a);print(b))
        //(case (10) : print(100);print(200))
        //(default : print(300)));
        //print(300)

        IStatement line1 = new VariableDeclarationStatement("a", new IntType());
        IStatement line2 = new AssignStatement("a", new ValueExpression(new IntValue(1)));
        IStatement line3 = new VariableDeclarationStatement("b", new IntType());
        IStatement line4 = new AssignStatement("b", new ValueExpression(new IntValue(2)));
        IStatement line5 = new VariableDeclarationStatement("c", new IntType());
        IStatement line6 = new AssignStatement("c", new ValueExpression(new IntValue(5)));

        IExpression switchParam = new ArithmeticExpression('*', new VariableLookupExpression("a"),
                new ValueExpression(new IntValue(10)));
        Map<IExpression, IStatement> switchBody = new LinkedHashMap<>();
        IExpression switchCase1 = new ArithmeticExpression('*', new VariableLookupExpression("b"),
                new VariableLookupExpression("c"));
        IStatement switchCaseResult1 = new CompoundStatement(new PrintStatement(new VariableLookupExpression("a")),
                new PrintStatement(new VariableLookupExpression("b")));
        IExpression switchCase2 = new ValueExpression(new IntValue(10));
        IStatement switchCaseResult2 = new CompoundStatement(new PrintStatement(new ValueExpression(new IntValue(100))),
                new PrintStatement(new ValueExpression(new IntValue(200))));
        IStatement switchDefaultResult = new PrintStatement(new ValueExpression(new IntValue(300)));
        switchBody.put(switchCase1, switchCaseResult1); switchBody.put(switchCase2, switchCaseResult2);

        IStatement line7 = new SwitchStatement(switchParam, switchBody, switchDefaultResult);
        IStatement line8 = new PrintStatement(new ValueExpression(new IntValue(300)));

        IStatement ex12 = new CompoundStatement(line1,
                new CompoundStatement(line2,
                        new CompoundStatement(line3,
                                new CompoundStatement(line4,
                                        new CompoundStatement(line5,
                                                new CompoundStatement(line6,
                                                        new CompoundStatement(line7, line8)))))));

        return ex12;
    }

    public static IStatement repeatUntilTestProgramStatement() {
        //v=0;
        //(repeat (fork(print(v);v=v-1);v=v+1) until v==3);
        //x=1;y=2;z=3;w=4;
        //print(v*10)

        IStatement line1 = new VariableDeclarationStatement("v", new IntType());
        IStatement line2 = new AssignStatement("v", new ValueExpression(new IntValue(0)));

        IStatement forkBody = new CompoundStatement(new PrintStatement(new VariableLookupExpression("v")),
                new AssignStatement("v", new ArithmeticExpression('-',
                        new VariableLookupExpression("v"), new ValueExpression(new IntValue(1)))));
        IStatement forkLine = new ForkStatement(forkBody);
        IStatement incrementer = new AssignStatement("v",
                new ArithmeticExpression('+', new VariableLookupExpression("v"),
                        new ValueExpression(new IntValue(1))));

        IStatement repeatBody = new CompoundStatement(forkLine, incrementer);
        IStatement line3 = new RepeatUntilStatement(new RelationalExpression("==",
                new VariableLookupExpression("v"), new ValueExpression(new IntValue(3))), repeatBody);

        IStatement line4 = new PrintStatement(new ArithmeticExpression('*', new VariableLookupExpression("v"),
                new ValueExpression(new IntValue(10))));

        IStatement ex13 = new CompoundStatement(line1,
                new CompoundStatement(line2,
                        new CompoundStatement(line3, line4)));

        return ex13;
    }

    public static IStatement forTestProgramStatement() {
        //Ref int a; new(a,20);
        //(for(v=0;v<3;v=v+1) fork(print(v);v=v*rh(a)));
        //print(rh(a))

        IStatement line1 = new VariableDeclarationStatement("a", new ReferenceType(new IntType()));
        IStatement line2 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(20)));

        IStatement forkBody = new CompoundStatement(
                new PrintStatement(new VariableLookupExpression("v")),
                new AssignStatement("v", new ArithmeticExpression('*', new VariableLookupExpression("v"), new HeapReadingExpression(new VariableLookupExpression("a"))))
        );
        IStatement forkLine = new ForkStatement(forkBody);

        IStatement line3 = new ForStatement(
                "v",
                new IntValue(0),
                new RelationalExpression("<", new VariableLookupExpression("v"), new ValueExpression(new IntValue(3))),
                new ArithmeticExpression('+', new VariableLookupExpression("v"), new ValueExpression(new IntValue(1))),
                forkLine
        );
        IStatement line4 = new PrintStatement(new HeapReadingExpression(new VariableLookupExpression("a")));

        IStatement ex14 = new CompoundStatement(line1,
                new CompoundStatement(line2,
                        new CompoundStatement(line3, line4)));

        return ex14;
    }

    public static IStatement MULExpressionTestProgramStatement() {
        //v1=2;v2=3; (if (v1) then print(MUL(v1,v2)) else print (v1))
        IStatement line1 = new VariableDeclarationStatement("v1", new IntType());
        IStatement line2 = new AssignStatement("v1", new ValueExpression(new IntValue(2)));
        IStatement line3 = new VariableDeclarationStatement("v2", new IntType());
        IStatement line4 = new AssignStatement("v2", new ValueExpression(new IntValue(3)));

        IStatement line5 = new IfStatement(
                new VariableLookupExpression("v1"),
                new PrintStatement(new MULExpression(new VariableLookupExpression("v1"), new VariableLookupExpression("v2"))),
                new PrintStatement(new VariableLookupExpression("v1"))
        );

        IStatement ex15 = new CompoundStatement(line1,
                new CompoundStatement(line2,
                        new CompoundStatement(line3,
                                new CompoundStatement(line4, line5))));

        return ex15;
    }

    public static IStatement concurrencyLockTestProgramStatement() {
        //Ref int v1; Ref int v2; int x; int q;
        //new(v1,20);new(v2,30);newLock(x);
        //fork(
        //  fork(
        //      lock(x);wh(v1,rh(v1)-1);unlock(x)
        //  );
        //  lock(x);wh(v1,rh(v1)*10);unlock(x)
        //);
        //newLock(q);
        //fork(
        //  fork(
        //      lock(q);wh(v2,rh(v2)+5);unlock(q)
        //   );
        //  lock(q);wh(v2,rh(v2)*10);unlock(q)
        //);
        //nop;nop;nop;nop;
        //lock(x); print(rh(v1)); unlock(x);
        //lock(q); print(rh(v2)); unlock(q);
        IStatement line1 = new VariableDeclarationStatement("v1", new ReferenceType(new IntType()));
        IStatement line2 = new VariableDeclarationStatement("v2", new ReferenceType(new IntType()));
        IStatement line3 = new VariableDeclarationStatement("x", new IntType());
        IStatement line4 = new VariableDeclarationStatement("q", new IntType());
        IStatement line5 = new HeapAllocationStatement("v1", new ValueExpression(new IntValue(20)));
        IStatement line6 = new HeapAllocationStatement("v2", new ValueExpression(new IntValue(30)));
        IStatement line7 = new CreateMutexLockStatement("x");

        IStatement fork1line1 = new LockMutexStatement("x");
        IStatement fork1line2 = new HeapWritingStatement("v1", new ArithmeticExpression('-',
                new HeapReadingExpression(new VariableLookupExpression("v1")), new ValueExpression(new IntValue(1))));
        IStatement fork1line3 = new UnlockMutexStatement("x");
        IStatement fork2line1 = new ForkStatement(
                new CompoundStatement(fork1line1,
                        new CompoundStatement(fork1line2, fork1line3)));
        IStatement fork2line2 = new LockMutexStatement("x");
        IStatement fork2line3 = new HeapWritingStatement("v1", new ArithmeticExpression('*',
                new HeapReadingExpression(new VariableLookupExpression("v1")), new ValueExpression(new IntValue(10))));
        IStatement fork2line4 = new UnlockMutexStatement("x");
        IStatement fork2 = new CompoundStatement(fork2line1,
                new CompoundStatement(fork2line2,
                        new CompoundStatement(fork2line3, fork2line4)));

        IStatement line8 = new ForkStatement(fork2);
        IStatement line9 = new CreateMutexLockStatement("q");

        IStatement fork3line1 = new LockMutexStatement("q");
        IStatement fork3line2 = new HeapWritingStatement("v2", new ArithmeticExpression('+',
                new HeapReadingExpression(new VariableLookupExpression("v2")), new ValueExpression(new IntValue(5))));
        IStatement fork3line3 = new UnlockMutexStatement("q");
        IStatement fork4line1 = new ForkStatement(
                new CompoundStatement(fork3line1,
                        new CompoundStatement(fork3line2, fork3line3)));
        IStatement fork4line2 = new LockMutexStatement("q");
        IStatement fork4line3 = new HeapWritingStatement("v2", new ArithmeticExpression('*',
                new HeapReadingExpression(new VariableLookupExpression("v2")), new ValueExpression(new IntValue(10))));
        IStatement fork4line4 = new UnlockMutexStatement("q");
        IStatement fork4 = new CompoundStatement(fork4line1,
                new CompoundStatement(fork4line2,
                        new CompoundStatement(fork4line3, fork4line4)));

        IStatement line10 = new ForkStatement(fork4);
        IStatement line11 = new SleepStatement(new IntValue(4));
        IStatement line12 = new LockMutexStatement("x");
        IStatement line13 = new PrintStatement(new HeapReadingExpression(new VariableLookupExpression("v1")));
        IStatement line14 = new UnlockMutexStatement("x");
        IStatement line15 = new LockMutexStatement("q");
        IStatement line16 = new PrintStatement(new HeapReadingExpression(new VariableLookupExpression("v2")));
        IStatement line17 = new UnlockMutexStatement("q");

        IStatement ex16 = new CompoundStatement(line1,
                new CompoundStatement(line2,
                        new CompoundStatement(line3,
                                new CompoundStatement(line4,
                                        new CompoundStatement(line5,
                                                new CompoundStatement(line6,
                                                        new CompoundStatement(line7,
                                                                new CompoundStatement(line8,
                                                                        new CompoundStatement(line9,
                                                                                new CompoundStatement(line10,
                                                                                        new CompoundStatement(line11,
                                                                                                new CompoundStatement(line12,
                                                                                                        new CompoundStatement(line13,
                                                                                                                new CompoundStatement(line14,
                                                                                                                        new CompoundStatement(line15,
                                                                                                                                new CompoundStatement(line16, line17))))))))))))))));
        return ex16;
    }

    public static IStatement concurrencySemaphoreTestProgramStatement() {
        //Ref int v1; int cnt;
        //new(v1,1);createSemaphore(cnt,rH(v1));
        //fork(
        //  acquire(cnt);
        //  wh(v1,rh(v1)*10));
        //  print(rh(v1));
        //  release(cnt)
        //);
        //fork(
        // acquire(cnt);
        // wh(v1,rh(v1)*10));
        // wh(v1,rh(v1)*2));
        // print(rh(v1));
        // release(cnt)
        //);
        //acquire(cnt);
        //print(rh(v1)-1);
        //release(cnt)

        IStatement line1 = new VariableDeclarationStatement("v1", new ReferenceType(new IntType()));
        IStatement line2 = new VariableDeclarationStatement("cnt", new IntType());
        IStatement line3 = new HeapAllocationStatement("v1", new ValueExpression(new IntValue(1)));
        IStatement line4 = new CreateSemaphoreStatement("cnt", new HeapReadingExpression(new VariableLookupExpression("v1")));

        IStatement fork1line1 = new AcquireSemaphoreStatement("cnt");
        IStatement fork1line2 = new HeapWritingStatement("v1", new ArithmeticExpression('*',
                new HeapReadingExpression(new VariableLookupExpression("v1")), new ValueExpression(new IntValue(10))));
        IStatement fork1line3 = new PrintStatement(new HeapReadingExpression(new VariableLookupExpression("v1")));
        IStatement fork1line4 = new ReleaseSemaphoreStatement("cnt");
        IStatement fork1 = new CompoundStatement(fork1line1,
                new CompoundStatement(fork1line2,
                        new CompoundStatement(fork1line3, fork1line4)));

        IStatement line5 = new ForkStatement(fork1);

        IStatement fork2line1 = new AcquireSemaphoreStatement("cnt");
        IStatement fork2line2 = new HeapWritingStatement("v1", new ArithmeticExpression('*',
                new HeapReadingExpression(new VariableLookupExpression("v1")), new ValueExpression(new IntValue(10))));
        IStatement fork2line3 = new HeapWritingStatement("v1", new ArithmeticExpression('*',
                new HeapReadingExpression(new VariableLookupExpression("v1")), new ValueExpression(new IntValue(2))));
        IStatement fork2line4 = new PrintStatement(new HeapReadingExpression(new VariableLookupExpression("v1")));
        IStatement fork2line5 = new ReleaseSemaphoreStatement("cnt");
        IStatement fork2 = new CompoundStatement(fork2line1,
                new CompoundStatement(fork2line2,
                        new CompoundStatement(fork2line3,
                                new CompoundStatement(fork2line4, fork2line5))));

        IStatement line6 = new ForkStatement(fork2);
        IStatement line7 = new AcquireSemaphoreStatement("cnt");
        IStatement line8 = new PrintStatement(new ArithmeticExpression('-',
                new HeapReadingExpression(new VariableLookupExpression("v1")), new ValueExpression(new IntValue(1))));
        IStatement line9 = new ReleaseSemaphoreStatement("cnt");

        IStatement ex17 = new CompoundStatement(line1,
                new CompoundStatement(line2,
                        new CompoundStatement(line3,
                                new CompoundStatement(line4,
                                        new CompoundStatement(line5,
                                                new CompoundStatement(line6,
                                                        new CompoundStatement(line7,
                                                                new CompoundStatement(line8, line9))))))));

        return ex17;
    }

    public static IStatement concurrencyLatchTestProgramStatement() {
        //new(v1,2);new(v2,3);new(v3,4);newLatch(cnt,rH(v2));
        //fork(wh(v1,rh(v1)*10));print(rh(v1));countDown(cnt);
        //  fork(wh(v2,rh(v2)*10));print(rh(v2));countDown(cnt);
        //      fork(wh(v3,rh(v3)*10));print(rh(v3));countDown(cnt))))
        //await(cnt);
        //print(100);
        //countDown(cnt);
        //print(100)

        IStatement line1 = new VariableDeclarationStatement("v1", new ReferenceType(new IntType()));
        IStatement line2 = new VariableDeclarationStatement("v2", new ReferenceType(new IntType()));
        IStatement line3 = new VariableDeclarationStatement("v3", new ReferenceType(new IntType()));
        IStatement line4 = new VariableDeclarationStatement("cnt", new IntType());
        IStatement line5 = new HeapAllocationStatement("v1", new ValueExpression(new IntValue(2)));
        IStatement line6 = new HeapAllocationStatement("v2", new ValueExpression(new IntValue(3)));
        IStatement line7 = new HeapAllocationStatement("v3", new ValueExpression(new IntValue(4)));
        IStatement line8 = new CreateLatchStatement("cnt", new HeapReadingExpression(new VariableLookupExpression("v2")));

        IStatement fork1line1 = new HeapWritingStatement("v1",
                new ArithmeticExpression('*', new HeapReadingExpression(new VariableLookupExpression("v1")), new ValueExpression(new IntValue(10))));
        IStatement fork1line2 = new PrintStatement(new HeapReadingExpression(new VariableLookupExpression("v1")));
        IStatement fork1line3 = new CountdownLatchStatement("cnt");

        IStatement fork2line1 = new HeapWritingStatement("v2",
                new ArithmeticExpression('*', new HeapReadingExpression(new VariableLookupExpression("v2")), new ValueExpression(new IntValue(10))));
        IStatement fork2line2 = new PrintStatement(new HeapReadingExpression(new VariableLookupExpression("v2")));
        IStatement fork2line3 = new CountdownLatchStatement("cnt");

        IStatement fork3line1 = new HeapWritingStatement("v3",
                new ArithmeticExpression('*', new HeapReadingExpression(new VariableLookupExpression("v3")), new ValueExpression(new IntValue(10))));
        IStatement fork3line2 = new PrintStatement(new HeapReadingExpression(new VariableLookupExpression("v3")));
        IStatement fork3line3 = new CountdownLatchStatement("cnt");
        IStatement fork3 = new CompoundStatement(fork3line1,
                new CompoundStatement(fork3line2, fork2line3));

        IStatement fork2line4 = new ForkStatement(fork3);
        IStatement fork2 = new CompoundStatement(fork2line1,
                new CompoundStatement(fork2line2,
                        new CompoundStatement(fork2line3, fork2line4)));

        IStatement fork1line4 = new ForkStatement(fork2);
        IStatement fork1 = new CompoundStatement(fork1line1,
                new CompoundStatement(fork1line2,
                        new CompoundStatement(fork1line3, fork1line4)));

        IStatement line9 = new ForkStatement(fork1);
        IStatement line10 = new AwaitLatchStatement("cnt");
        IStatement line11 = new PrintStatement(new ValueExpression(new IntValue(100)));
        IStatement line12 = new CountdownLatchStatement("cnt");
        IStatement line13 = new PrintStatement(new ValueExpression(new IntValue(100)));

        IStatement ex18 = new CompoundStatement(line1,
                new CompoundStatement(line2,
                        new CompoundStatement(line3,
                                new CompoundStatement(line4,
                                        new CompoundStatement(line5,
                                                new CompoundStatement(line6,
                                                        new CompoundStatement(line7,
                                                                new CompoundStatement(line8,
                                                                        new CompoundStatement(line9,
                                                                                new CompoundStatement(line10,
                                                                                        new CompoundStatement(line11,
                                                                                                new CompoundStatement(line12, line13))))))))))));
        return ex18;
    }

    public static IStatement concurrencyBarrierTestProgramStatement() {
        IStatement line1 = new VariableDeclarationStatement("v1", new ReferenceType(new IntType()));
        IStatement line2 = new VariableDeclarationStatement("v2", new ReferenceType(new IntType()));
        IStatement line3 = new VariableDeclarationStatement("v3", new ReferenceType(new IntType()));
        IStatement line4 = new VariableDeclarationStatement("cnt", new IntType());
        IStatement line5 = new HeapAllocationStatement("v1", new ValueExpression(new IntValue(2)));
        IStatement line6 = new HeapAllocationStatement("v2", new ValueExpression(new IntValue(3)));
        IStatement line7 = new HeapAllocationStatement("v3", new ValueExpression(new IntValue(4)));
        IStatement line8 = new CreateBarrierStatement("cnt", new HeapReadingExpression(new VariableLookupExpression("v2")));

        IStatement fork1line1 = new AwaitBarrierStatement("cnt");
        IStatement fork1line2 = new HeapWritingStatement("v1",
                new ArithmeticExpression('*', new HeapReadingExpression(new VariableLookupExpression("v1")), new ValueExpression(new IntValue(10))));
        IStatement fork1line3 = new PrintStatement(new HeapReadingExpression(new VariableLookupExpression("v1")));
        IStatement fork1 = new CompoundStatement(fork1line1,
                new CompoundStatement(fork1line2, fork1line3));

        IStatement line9 = new ForkStatement(fork1);

        IStatement fork2line1 = new AwaitBarrierStatement("cnt");
        IStatement fork2line2 = new HeapWritingStatement("v2",
                new ArithmeticExpression('*', new HeapReadingExpression(new VariableLookupExpression("v2")), new ValueExpression(new IntValue(10))));
        IStatement fork2line3 = new HeapWritingStatement("v2",
                new ArithmeticExpression('*', new HeapReadingExpression(new VariableLookupExpression("v2")), new ValueExpression(new IntValue(10))));
        IStatement fork2line4 = new PrintStatement(new HeapReadingExpression(new VariableLookupExpression("v2")));
        IStatement fork2 = new CompoundStatement(fork2line1,
                new CompoundStatement(fork2line2,
                        new CompoundStatement(fork2line3, fork2line4)));

        IStatement line10 = new ForkStatement(fork2);
        IStatement line11 = new AwaitBarrierStatement("cnt");
        IStatement line12 = new PrintStatement(new HeapReadingExpression(new VariableLookupExpression("v3")));

        IStatement ex19 = new CompoundStatement(line1,
                new CompoundStatement(line2,
                        new CompoundStatement(line3,
                                new CompoundStatement(line4,
                                        new CompoundStatement(line5,
                                                new CompoundStatement(line6,
                                                        new CompoundStatement(line7,
                                                                new CompoundStatement(line8,
                                                                        new CompoundStatement(line9,
                                                                                new CompoundStatement(line10,
                                                                                        new CompoundStatement(line11, line12)))))))))));
        return ex19;

    }

}
