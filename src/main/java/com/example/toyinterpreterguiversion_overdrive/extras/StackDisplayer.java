package com.example.toyinterpreterguiversion_overdrive.extras;

import com.example.toyinterpreterguiversion_overdrive.model.statements.CompoundStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;

import java.util.ArrayList;
import java.util.List;

public class StackDisplayer {
    CompoundStatement fullprogram;
    public StackDisplayer(CompoundStatement fp) {
        fullprogram = fp;
    }

    public StackADT<IStatement> decompose() {
        List<IStatement> stacksentences = new ArrayList<>();
        stacksentences.add(fullprogram);

        while (stacksentences.stream().anyMatch(statement -> statement instanceof CompoundStatement)) {
            List<IStatement> temporary = new ArrayList<>();
            stacksentences.forEach((statement -> {
                if (statement instanceof CompoundStatement cs) {
                    temporary.add(cs.getFirstStatement());
                    temporary.add(cs.getSecondStatement());
                }
                else {
                    temporary.add(statement);
                }
            }));
            stacksentences = temporary;
        }

        StackADT<IStatement> stack = new StackADT<>();
        for (int index = stacksentences.size() - 1; index >= 0; index--) {
            stack.push(stacksentences.get(index));
        }
        return stack;
    }
}
