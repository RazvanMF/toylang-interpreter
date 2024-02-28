package com.example.toyinterpreterguiversion_overdrive.storage;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;

import java.io.IOException;
import java.util.List;

public interface IRepository {
    //ProgramState getCurrentProgram();
    void logPrgStateExec(ProgramState program) throws StandardException, IOException;
    void signalStartState() throws IOException;
    void signalStopState() throws IOException;
    List<ProgramState> getProgramList();
    void setProgramList(List<ProgramState> progList);
}
