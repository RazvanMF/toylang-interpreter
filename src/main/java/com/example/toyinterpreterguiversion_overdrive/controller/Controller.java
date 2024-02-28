package com.example.toyinterpreterguiversion_overdrive.controller;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.ReferenceValue;
import com.example.toyinterpreterguiversion_overdrive.storage.IRepository;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.HeapADT;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Controller {
    IRepository repo;

    public ExecutorService executor;
    public List<IValue> output;

    public Controller(IRepository r) {
        repo = r;
    }

    public IRepository getRepo() {
        return repo;
    }

    public void setRepo(IRepository repo) {
        this.repo = repo;
    }

    public void allSteps() throws StandardException, IOException, InterruptedException {
        repo.signalStartState();
        //executor = Executors.newFixedThreadPool(8);
        List<ProgramState> prgList=removeCompletedPrg(repo.getProgramList());
        while(!prgList.isEmpty()){
            HeapADT<IValue> workaround = prgList.get(0).getHeap();
            workaround.setHeap(
                    safeGC(allAddresses(prgList), workaround.returnHeap())
            );
            oneStepForAllPrgStates(prgList);
            prgList=removeCompletedPrg(repo.getProgramList());
        }
        executor.shutdownNow();
        repo.setProgramList(prgList);
        repo.signalStopState();
    }

    public List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) {
        return inPrgList.stream()
                .filter(p -> p.isNotCompleted())
                .collect(Collectors.toList());
    }

    List<Integer> getAddr_AllScopes(Collection<IValue> symTableValues, HeapADT<IValue> heapTable){
        ConcurrentLinkedDeque<Integer> symTableAdr = symTableValues.stream()
                .filter(v-> v instanceof ReferenceValue)
                .map(v-> { ReferenceValue v1 = (ReferenceValue)v; return v1.getAddress(); })
                .collect(Collectors.toCollection(ConcurrentLinkedDeque::new));


        symTableAdr.stream()
                .forEach(a -> {
                    IValue v= heapTable.returnHeap().get(a);
                    if (v instanceof ReferenceValue)
                        if (!symTableAdr.contains(((ReferenceValue)v).getAddress()))
                            symTableAdr.add(((ReferenceValue)v).getAddress());
                    }
                );

        return symTableAdr.stream().toList();
    }

    public Map<Integer, IValue> safeGC(List<Integer> symTableAddr, Map<Integer,IValue> heap){
        return heap.entrySet().stream()
                .filter(e->symTableAddr.contains(e.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public List<Integer> allAddresses(List<ProgramState> programStates) {
        HashSet<Integer> alladdr = new HashSet<>();
        for (ProgramState prg : programStates) {
            ConcurrentLinkedDeque<Integer> symTableAdr = prg.getSymTable().getDictionary().values()
                    .stream()
                    .filter(v-> v instanceof ReferenceValue)
                    .map(v-> { ReferenceValue v1 = (ReferenceValue)v; return v1.getAddress(); })
                    .collect(Collectors.toCollection(ConcurrentLinkedDeque::new));


            symTableAdr.stream()
                    .forEach(a -> {
                                IValue v= prg.getHeap().returnHeap().get(a);
                                if (v instanceof ReferenceValue)
                                    if (!symTableAdr.contains(((ReferenceValue)v).getAddress()))
                                        symTableAdr.add(((ReferenceValue)v).getAddress());
                            }
                    );
            symTableAdr.forEach((Int) -> alladdr.add(Int));
        }
        return alladdr.stream().toList();
    }

    public void oneStepForAllPrgStates(List<ProgramState> programStates) throws InterruptedException {
        programStates.forEach((prog) -> {
            try {
                repo.logPrgStateExec(prog);
            } catch (StandardException | IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        List<Callable<ProgramState>> callList = programStates.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) (() -> {return p.oneStep();}))
                .collect(Collectors.toList());

        List<ProgramState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());

        programStates.addAll(newPrgList);

        programStates.forEach((prog) -> {
            if (prog.getExeStack().isEmpty())
                try {
                    repo.logPrgStateExec(prog);
                } catch (StandardException | IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
        });

        repo.setProgramList(programStates);
    }

    public ProgramState getProgramStateByID(int id) {
        for (ProgramState prog : repo.getProgramList()) {
            if (prog.getPersonalID() == id)
                return prog;
        }
        return null;
    }
}
