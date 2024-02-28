package com.example.toyinterpreterguiversion_overdrive.model.statements.logic;
import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.IExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.BoolType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.types.IntType;
import com.example.toyinterpreterguiversion_overdrive.model.values.BoolValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.IntValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;

public class IfStatement implements IStatement {
    IStatement ifClause, elseClause;
    IExpression expr;

    public IfStatement(IExpression ex, IStatement ifc, IStatement elc) {
        expr = ex; ifClause = ifc; elseClause = elc;
    }

    @Override
    public String toString() {
        return String.format("IF %s THEN %s ELSE %s", expr.toString(), ifClause.toString(),
                elseClause.toString());
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException {
        StackADT<IStatement> exeStack = state.getExeStack();
        DictionaryADT<String, IValue> symTable = state.getSymTable();

        IValue resultEval = expr.evaluate(symTable, state.getHeap());
        if (!(resultEval instanceof BoolValue) && !(resultEval instanceof IntValue))
            throw new StandardException(String.format("Given expression %s not a conditional expression",
                    expr.toString()));
        else {
            if (resultEval instanceof BoolValue boolres) {
                if (boolres.getValue())
                    exeStack.push(ifClause);
                else
                    exeStack.push(elseClause);
            }
            if (resultEval instanceof IntValue intres) {
                if (intres.getValue() != 0)
                    exeStack.push(ifClause);
                else
                    exeStack.push(elseClause);
            }

        }
        //return state;
        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        IType typexp = expr.typecheck(typeEnv);
        if (typexp.equals(new BoolType()) || typexp.equals(new IntType())) {
            ifClause.typecheck(typeEnv.deepcopy());
            elseClause.typecheck(typeEnv.deepcopy());
            return typeEnv;
        }
        else
            throw new StandardException("The condition of IF has not the type bool or int");
    }
}
