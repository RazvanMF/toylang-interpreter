package com.example.toyinterpreterguiversion_overdrive.storage;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.StringValue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MemRepository implements IRepository {
    private List<ProgramState> programs;
    private String logFilePath;

    public MemRepository(String file) {
        programs = new ArrayList<ProgramState>();
        logFilePath = file;
    }

    public void add(ProgramState program) {
        programs.add(program); //this should "emulate?" threads, since we'll just use one program state and change its
                                    //attributes
    }

    @Override
    public void logPrgStateExec(ProgramState program) throws StandardException, IOException {
        BufferedWriter logFile = new BufferedWriter(new FileWriter(logFilePath, true));

        logFile.write(String.format("\t//////////[START EXECUTION ON THREAD %d]//////////\n", program.getPersonalID()));

        logFile.write("\t\t----------[EXECUTION STACK]----------\n");
        for (IStatement elem : program.getExeStack().getReversed()) {
            logFile.write("\t\t" + elem.toString());
            logFile.write("\n");
        }
        logFile.write("\n");

        logFile.write("\t\t----------[SYMBOL TABLE]----------\n");
        for (String key : program.getSymTable().getDictionary().keySet()) {
            logFile.write(String.format("\t\t%s -> %s\n", key, program.getSymTable().lookup(key).toString()));
        }
        logFile.write("\n");

        logFile.write("\t\t----------[OUTPUT LOG]----------\n");
        for (IValue val : program.getOutput().getList()) {
            logFile.write("\t\t" + val.toString());
            logFile.write("\n");
        }
        logFile.write("\n");

        logFile.write("\t\t----------[FILE TABLE]----------\n");
        for (IValue val : program.getFileTable().getDictionary().keySet()) {
            logFile.write("\t\t" + val.toString() + " -> " +
                    String.valueOf(program.getFileTable().lookup((StringValue) val)));
            logFile.write("\n");
        }
        logFile.write("\n");

        logFile.write("\t\t----------[HEAP]----------\n");
        for (Integer addr : program.getHeap().returnHeap().keySet()) {
            logFile.write("\t\t" + addr.toString() + " -> " +
                    String.valueOf(program.getHeap().lookup(addr)));
            logFile.write("\n");
        }

        logFile.write(String.format("\t//////////[END EXECUTION ON THREAD %d]//////////\n\n", program.getPersonalID()));
        logFile.close();
    }

    @Override
    public void signalStartState() throws IOException {
        FileWriter logFile = new FileWriter(logFilePath, true);
        logFile.write("~~~~~~~~~~[START PROGRAM]~~~~~~~~~~\n");
        logFile.close();
    }

    @Override
    public void signalStopState() throws IOException {
        FileWriter logFile = new FileWriter(logFilePath, true);
        logFile.write("~~~~~~~~~~[END PROGRAM]~~~~~~~~~~\n\n\n");
        logFile.close();
    }

    @Override
    public List<ProgramState> getProgramList() {
        return programs;
    }

    @Override
    public void setProgramList(List<ProgramState> progList) {
        programs = progList;
    }
}
