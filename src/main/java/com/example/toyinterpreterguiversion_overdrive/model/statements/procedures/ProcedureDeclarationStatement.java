package com.example.toyinterpreterguiversion_overdrive.model.statements.procedures;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.list.ListADT;
import com.example.toyinterpreterguiversion_overdrive.utils.proceduretable.ProcedureTableADT;
import javafx.util.Pair;

import java.io.IOException;

public class ProcedureDeclarationStatement implements IStatement {
    //TODO: type check in signature (i.e. pass Dictionary<name, type> instead of list<name>), and a return type

    String nameOfFunction;
    ListADT<String> listOfParams;
    IStatement bodyFunction;

    public ProcedureDeclarationStatement(String name, ListADT<String> params, IStatement body) {
        nameOfFunction = name;
        listOfParams = params;
        bodyFunction = body;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StandardException, IOException {
        ProcedureTableADT<String, Pair<ListADT<String>, IStatement>> procTable = ProgramState.getProcedureTable();
        procTable.put(nameOfFunction, new Pair<>(listOfParams, bodyFunction));
        return null;
    }

    @Override
    public DictionaryADT<String, IType> typecheck(DictionaryADT<String, IType> typeEnv) throws StandardException {
        return typeEnv; //irrational to do anything else, since the variables don't exist
    }

    private String listFormatter() {
        StringBuilder out = new StringBuilder();
        for (String name : listOfParams.getList()) {
            out.append(name); out.append(", ");
        }
        out.setLength(out.length() - 2);
        return out.toString();
    }

    @Override
    public String toString() {
        return String.format("Declare: %s(%s) {%s}", nameOfFunction, listFormatter(), bodyFunction.toString());
    }
}
