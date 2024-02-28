package com.example.toyinterpreterguiversion_overdrive.model;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.extras.StackDisplayer;
import com.example.toyinterpreterguiversion_overdrive.model.statements.CompoundStatement;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.StringValue;
import com.example.toyinterpreterguiversion_overdrive.utils.barriertable.BarrierTableADT;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.HeapADT;
import com.example.toyinterpreterguiversion_overdrive.utils.latchtable.LatchTableADT;
import com.example.toyinterpreterguiversion_overdrive.utils.list.ListADT;
import com.example.toyinterpreterguiversion_overdrive.utils.locktable.LockTableADT;
import com.example.toyinterpreterguiversion_overdrive.utils.proceduretable.ProcedureTableADT;
import com.example.toyinterpreterguiversion_overdrive.utils.semaphoretable.SemaphoreTableADT;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProgramState {
    //region STATICS: Application-wide required variables
    static int id = 0;
    public static Lock writeLock = new ReentrantLock();
    static ProcedureTableADT<String, Pair<ListADT<String>, IStatement>> procedureTable
            = new ProcedureTableADT<String, Pair<ListADT<String>, IStatement>>();
    static LockTableADT<Integer> lockTable = new LockTableADT<Integer>();  //synchronized union means we must lock before update?
    static SemaphoreTableADT<Pair<Integer, List<Integer>>> semaphoreTable
            = new SemaphoreTableADT<Pair<Integer, List<Integer>>>();
    static LatchTableADT<Integer> latchTable = new LatchTableADT<Integer>();
    static BarrierTableADT<Pair<Integer, List<Integer>>> barrierTable
            = new BarrierTableADT<Pair<Integer, List<Integer>>>();
    //endregion

    //LOCALS
    int personalID;

    private int stackUsage = 1; //0 - normal, 1 - decomposed

    StackADT<IStatement> exeStack;
    DictionaryADT<String, IValue> symTable;
    StackADT<DictionaryADT<String, IValue>> symTableStack;
    ListADT<IValue> output;
    DictionaryADT<StringValue, BufferedReader> fileTable;
    ArrayList<String> log;
    HeapADT<IValue> heap;

    //IStatement originalProgram;

    public ProgramState(StackADT<IStatement> stk, StackADT<DictionaryADT<String, IValue>> symtblStack, ListADT<IValue> out,
                        DictionaryADT<StringValue, BufferedReader> fltbl, IStatement prg) {
        personalID = manageID();

        exeStack = stk;

        symTableStack = symtblStack;
        if (symtblStack.isEmpty()) {
            DictionaryADT<String, IValue> baseSymTable = new DictionaryADT<>();
            symtblStack.push(baseSymTable);
        }

        output = out;

        fileTable = fltbl;

        //originalProgram = deepCopy(prg);
        log = new ArrayList<String>();
        heap = new HeapADT<>();

        if (stackUsage == 0)
            exeStack.push(prg);
        else {
            if (prg instanceof CompoundStatement compound) {
                StackDisplayer stackDecomposer = new StackDisplayer(compound);
                exeStack = stackDecomposer.decompose();
            }
            else
                exeStack.push(prg); //can't be decomposed
        }
    }

    public void addToLog(String message) {
        log.add(message);
    }

    public ArrayList<String> showLog() {
        return log;
    }

    public StackADT<IStatement> getExeStack() {
        return exeStack;
    }

    public void setExeStack(StackADT<IStatement> exeStack) {
        this.exeStack = exeStack;
    }

//    public DictionaryADT<String, IValue> getSymTable() {
//        return symTable;
//    }

    public DictionaryADT<String, IValue> getSymTable() {return symTableStack.top(); }
    public StackADT<DictionaryADT<String, IValue>> getSymTableStack() {
        return symTableStack;
    }

    public void setSymTable(DictionaryADT<String, IValue> symTable) {
        this.symTable = symTable;
    }

    public ListADT<IValue> getOutput() {
        return output;
    }

    public void setOutput(ListADT<IValue> output) {
        this.output = output;
    }

    public DictionaryADT<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public void setFileTable(DictionaryADT<StringValue, BufferedReader> fileTable) {
        this.fileTable = fileTable;
    }

    public HeapADT<IValue> getHeap() {return heap;}

    public void setHeap(HeapADT<IValue> heap) {this.heap = heap;}

    @Override
    public String toString() {
        return String.format("Program State %d", personalID);
    }
    public boolean isNotCompleted() {
        return !(exeStack.isEmpty());
    }

    public ProgramState oneStep() throws StandardException, IOException {
        if (exeStack.isEmpty())
            throw new StandardException("Program State Stack is empty");

        IStatement statement = exeStack.pop();
        return statement.execute(this);
    }

    synchronized static int manageID() {
        id++;
        return id - 1;
    }

    public int getPersonalID() {
        return personalID;
    }
    public static void reseedIDs() {
        id = 0;
    }
    public static ProcedureTableADT<String, Pair<ListADT<String>, IStatement>> getProcedureTable() {
        return procedureTable;
    }
    public static LockTableADT<Integer> getLockTable() {
        return lockTable;
    }
    public static SemaphoreTableADT<Pair<Integer, List<Integer>>> getSemaphoreTable() {
        return semaphoreTable;
    }
    public static LatchTableADT<Integer> getLatchTable() {
        return latchTable;
    }
    public static BarrierTableADT<Pair<Integer, List<Integer>>> getBarrierTable() {
        return barrierTable;
    }

    public static void cleanAllStatics() {
        procedureTable = new ProcedureTableADT<>();
        lockTable = new LockTableADT<>();
        semaphoreTable = new SemaphoreTableADT<>();
        latchTable = new LatchTableADT<>();
        barrierTable = new BarrierTableADT<>();
    }


}
