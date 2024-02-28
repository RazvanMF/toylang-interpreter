package com.example.toyinterpreterguiversion_overdrive.model.statements.heap;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.expressions.IExpression;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.types.ReferenceType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.ReferenceValue;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.HeapADT;

import java.io.IOException;

public class HeapAllocationStatement implements IStatement {
    //new(variable name - string, expression - IExpression)
    String name;
    IExpression expression;

    public HeapAllocationStatement(String nm, IExpression expr) {
        name = nm;
        expression = expr;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        DictionaryADT<String, IValue> symTable = state.getSymTable();
        HeapADT<IValue> heap = state.getHeap();
        if (symTable.isDefined(name) && symTable.lookup(name).getType() instanceof ReferenceType) {
            IValue evaluation = expression.evaluate(symTable, heap);
            IType expectedtype = ((ReferenceValue) symTable.lookup(name)).getLocationType();
            IType returnedtype = evaluation.getType();
            if (expectedtype.equals(returnedtype)) {
                int address = heap.put(evaluation);
                symTable.update(name, new ReferenceValue(address, expectedtype));
                //return state;
                return null;
            }
            else throw new StandardException("types not equal");
        }
        else throw new StandardException("reference type for variable expected");
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        IType typevar = typeEnv.lookup(name);
        IType typexp = expression.typecheck(typeEnv);
        if (typevar.equals(new ReferenceType(typexp)))
            return typeEnv;
        else
            throw new StandardException("NEW stmt: right hand side and left hand side have different types ");
    }

    @Override
    public String toString() {
        return String.format("Allocate to heap variable %s, with value %s", name, expression.toString());
    }
}
