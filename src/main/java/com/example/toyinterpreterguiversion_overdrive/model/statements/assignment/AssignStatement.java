package com.example.toyinterpreterguiversion_overdrive.model.statements.assignment;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.IExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;

public class AssignStatement implements IStatement {
    String id;
    IExpression exp;
    public AssignStatement(String str, IExpression e) {
        id = str;
        exp = e;
    }

    @Override
    public String toString() {
        return String.format("%s = %s", id, exp.toString());
    }
    @Override
    public ProgramState execute(ProgramState state) throws StandardException {
        DictionaryADT<String, IValue> symTable = state.getSymTable();

        if (symTable.isDefined(id)) {
            IValue value = exp.evaluate(symTable, state.getHeap());
            IType type = symTable.lookup(id).getType();
            if (value.getType().equals(type))
                symTable.update(id, value);
            else throw new StandardException(String.format(
                    "declared type of variable \"%s\" and type of the assigned expression do not match", id));
        }
        else throw new StandardException(String.format("variable %s was not declared", id));
        //return state;
        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        IType typevar = typeEnv.lookup(id);
        IType typexp = exp.typecheck(typeEnv);
        if (typevar.equals(typexp))
            return typeEnv;
        else
            throw new StandardException("Assignment: right hand side and left hand side have different types ");
    }

//    public IStatement deepCopy() {
//        return new AssignStatement(new String(id), exp.deepCopy());
//    }
}
