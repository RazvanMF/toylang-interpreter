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

public class HeapWritingStatement implements IStatement {
    String heapVarName;
    IExpression expression;

    public HeapWritingStatement(String v, IExpression expr) {
        heapVarName = v;
        expression = expr;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        DictionaryADT<String, IValue> table = state.getSymTable();
        HeapADT<IValue> heap = state.getHeap();
        boolean definedInTable = table.isDefined(heapVarName);

        IType returnedType = null;
        if (definedInTable)
            returnedType = table.lookup(heapVarName).getType();

        boolean definedInHeap = heap.isDefined(((ReferenceValue) table.lookup(heapVarName)).getAddress());

        IType returnedHeapType = null;
        int elementHeapAddress = 0;
        if (definedInHeap) {
            returnedHeapType = heap.lookup(((ReferenceValue) table.lookup(heapVarName)).getAddress()).getType();
            elementHeapAddress = ((ReferenceValue) table.lookup(heapVarName)).getAddress();
        }

        if (definedInTable && returnedType instanceof ReferenceType && definedInHeap) {
            IValue evaluation = expression.evaluate(table, heap);
            if (evaluation.getType().equals(returnedHeapType)) {
                heap.update(elementHeapAddress, evaluation);
                //return state;
                return null;
            }
        }
        else {
            if (!definedInTable)
                throw new StandardException("Variable " + heapVarName + " not found in table");
            else if (!(returnedType instanceof ReferenceType))
                throw new StandardException("Variable " + heapVarName + " does not have type ReferenceType");
            else
                throw new StandardException("Variable " + heapVarName + "'s address not found in heap");
        }
        throw new StandardException("You should not see this.");
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        IType typevar = typeEnv.lookup(heapVarName);
        IType typexp = expression.typecheck(typeEnv);
        if (typevar instanceof ReferenceType refvar) {
            if (refvar.getReferenced().equals(typexp)) {
                return typeEnv;
            }
            else throw new StandardException("Assignment: right hand side and left hand side have different types ");

        }
        else
            throw new StandardException("Fatal error: function called on non-heap element");
    }

    @Override
    public String toString() {
        return String.format("Write to heap at %s the value %s", heapVarName.toString(), expression.toString());
    }
}
