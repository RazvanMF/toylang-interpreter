package com.example.toyinterpreterguiversion_overdrive;

import com.example.toyinterpreterguiversion_overdrive.controller.Controller;
import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.model.types.IType;
import com.example.toyinterpreterguiversion_overdrive.model.values.IValue;
import com.example.toyinterpreterguiversion_overdrive.model.values.StringValue;
import com.example.toyinterpreterguiversion_overdrive.storage.MemRepository;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.DictionaryADT;
import com.example.toyinterpreterguiversion_overdrive.utils.dictionary.SymTableValueClass;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.HeapADT;
import com.example.toyinterpreterguiversion_overdrive.utils.heap.HeapValueClass;
import com.example.toyinterpreterguiversion_overdrive.utils.list.ListADT;
import com.example.toyinterpreterguiversion_overdrive.utils.proceduretable.ProcTableValueClass;
import com.example.toyinterpreterguiversion_overdrive.utils.stack.StackADT;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class SideWindowController {
    public IStatement programToRun;
    private Controller application;
    private ConcurrencyWindowController concurrencyWindow;  // should be initialized in manualInitialize
                                            // (watches for all concurrency-related tables in PrgState)

    @FXML private TextField programStateNumber;
    @FXML private TextField idDisplayer;

    @FXML private ListView<ProgramState> programStateView = new ListView<>();
          private ObservableList<ProgramState> programStateElements;
          private int usedProgramStateID = 0;


    @FXML private ListView<IStatement> executionStackView = new ListView<IStatement>();
          private ObservableList<IStatement> executionStackElements;


    @FXML private ListView<IValue> fileTableView = new ListView<>();
          private ObservableList<IValue> fileTableElements;


    @FXML private ListView<IValue> outputView = new ListView<>();
          private ObservableList<IValue> outputElements;


    @FXML private TableView symbolTableView = new TableView<>();
          private ObservableList<SymTableValueClass> symbolTableElements;
          private TableColumn<SymTableValueClass, String> idSymTableColumn;
          private TableColumn<SymTableValueClass, String> valueSymTableColumn;


    @FXML private TableView heapTableView = new TableView<>();
          private TableColumn<HeapValueClass, String> addressHeapColumn;
          private TableColumn<HeapValueClass, String> valueHeapColumn;
          private ObservableList<HeapValueClass> heapTableElements;


    @FXML private TableView procTableView = new TableView<>();
          private TableColumn<ProcTableValueClass, String> signProcTableColumn;
          private TableColumn<ProcTableValueClass, String> bodyProcTableColumn;
          private ObservableList<ProcTableValueClass> procTableElements;


    @FXML private Button runButton;



    @FXML private void initialize() {
        addressHeapColumn = new TableColumn<>("Address");
        valueHeapColumn = new TableColumn<>("Value");
        heapTableView.getColumns().addAll(addressHeapColumn, valueHeapColumn);

        addressHeapColumn.prefWidthProperty().bind(heapTableView.widthProperty().multiply(0.5));
        valueHeapColumn.prefWidthProperty().bind(heapTableView.widthProperty().multiply(0.5));

        addressHeapColumn.setResizable(false);
        valueHeapColumn.setResizable(false);

        idSymTableColumn = new TableColumn<>("Identifier");
        valueSymTableColumn = new TableColumn<>("Value");
        symbolTableView.getColumns().addAll(idSymTableColumn, valueSymTableColumn);

        idSymTableColumn.prefWidthProperty().bind(symbolTableView.widthProperty().multiply(0.5));
        valueSymTableColumn.prefWidthProperty().bind(symbolTableView.widthProperty().multiply(0.5));

        idSymTableColumn.setResizable(false);
        valueSymTableColumn.setResizable(false);

        signProcTableColumn = new TableColumn<>("Signature");
        bodyProcTableColumn = new TableColumn<>("Body");
        procTableView.getColumns().addAll(signProcTableColumn, bodyProcTableColumn);

        signProcTableColumn.prefWidthProperty().bind(procTableView.widthProperty().multiply(0.5));
        bodyProcTableColumn.prefWidthProperty().bind(procTableView.widthProperty().multiply(0.5));

        signProcTableColumn.setResizable(false);
        bodyProcTableColumn.setResizable(false);
    }

    private void handleExceptionViaMessageBox(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("An exception has occured!");
        alert.setHeaderText(String.format("An exception of type %s has occurred during the program execution.",
                e.getClass().getName()));
        alert.setContentText(String.format("Message: %s",
                e.getMessage()));

        alert.showAndWait();
        Stage stage = (Stage) runButton.getScene().getWindow();
        stage.close();
    }

    private Controller startup() {
        ProgramState.reseedIDs();

        //type checker
        DictionaryADT<String, IType> typeEnvironment = new DictionaryADT<>();
        try {
            programToRun.typecheck(typeEnvironment);
        } catch (StandardException e) {
            handleExceptionViaMessageBox(e);
        }

        StackADT<IStatement> exeStack = new StackADT<IStatement>();
        StackADT<DictionaryADT<String, IValue>> symTableStack = new StackADT<DictionaryADT<String, IValue>>();
        ListADT<IValue> output = new ListADT<IValue>();
        DictionaryADT<StringValue, BufferedReader> fileTable = new DictionaryADT<StringValue, BufferedReader>();
        ProgramState exercise = new ProgramState(exeStack, symTableStack, output, fileTable, programToRun);

        MemRepository memrepo = new MemRepository("log.txt");
        memrepo.add(exercise);
        Controller app = new Controller(memrepo);
        app.executor = Executors.newFixedThreadPool(8);
        return app;
    }

    //region ---VIEW SETTERS---
    private void setExecutionStackView() {
        executionStackElements = FXCollections.observableArrayList(application.getProgramStateByID(usedProgramStateID)
                .getExeStack().getReversed());
        executionStackView.setItems(executionStackElements);
    }

    private void setOutputView() {
            outputElements =
                FXCollections.observableArrayList(application.getProgramStateByID(usedProgramStateID).getOutput().getList());
            outputView.setItems(outputElements);
    }

    private void setFileTableView() {
        if (!application.getRepo().getProgramList().isEmpty()) {
            fileTableElements =
                    FXCollections.observableArrayList(application.getProgramStateByID(usedProgramStateID).getFileTable().getDictionary().keySet());
            fileTableView.setItems(fileTableElements);
        }
    }

    private void setProgramStateView() {
        programStateElements = FXCollections.observableArrayList(application.getRepo().getProgramList());
        programStateView.setItems(programStateElements);
    }

    private void setHeapTableView() {
        heapTableElements = FXCollections.observableArrayList();
        addressHeapColumn.setCellValueFactory(cellData -> cellData.getValue().getAddressProperty());
        valueHeapColumn.setCellValueFactory(cellData -> cellData.getValue().getValueProperty());
        Map<Integer, IValue> heap = application.getProgramStateByID(usedProgramStateID).getHeap().returnHeap();
        for (Map.Entry<Integer, IValue> entry : heap.entrySet()) {
            heapTableElements.add(new HeapValueClass(entry.getKey(), entry.getValue()));
        }
        heapTableView.setItems(heapTableElements);
    }

    private void setSymbolTableView() {
        symbolTableElements = FXCollections.observableArrayList();
        idSymTableColumn.setCellValueFactory(cellData -> cellData.getValue().getIdentifierProperty());
        valueSymTableColumn.setCellValueFactory(cellData -> cellData.getValue().getValueProperty());
        Map<String, IValue> symTable = application.getProgramStateByID(usedProgramStateID).getSymTable().getDictionary();
        for (Map.Entry<String, IValue> entry : symTable.entrySet()) {
            symbolTableElements.add(new SymTableValueClass(entry.getKey(), entry.getValue()));
        }
        symbolTableView.setItems(symbolTableElements);
    }

    private void setProcTableView() {
        procTableElements = FXCollections.observableArrayList();
        signProcTableColumn.setCellValueFactory(cellData -> cellData.getValue().getSignatureProperty());
        bodyProcTableColumn.setCellValueFactory(cellData -> cellData.getValue().getBodyProperty());
        Map<String, Pair<ListADT<String>, IStatement>> procTable = ProgramState.getProcedureTable().getProcedureTable();
        for (Map.Entry<String, Pair<ListADT<String>, IStatement>> entry : procTable.entrySet()) {
            ProcTableValueClass element = new ProcTableValueClass(entry.getKey(), entry.getValue().getKey(),
                    entry.getValue().getValue());
            procTableElements.add(element);
        }
        procTableView.setItems(procTableElements);
    }
    //endregion

    private void refresh() {
        if (application.getProgramStateByID(usedProgramStateID) == null && !programStateView.getItems().isEmpty()) {
            usedProgramStateID = programStateView.getItems().get(0).getPersonalID();
        }

        programStateNumber.setText(String.valueOf(application.getRepo().getProgramList().size()));
        idDisplayer.setText(String.valueOf(usedProgramStateID));
        setProgramStateView();
    }

    private void prerefresh() {
        if (application.getProgramStateByID(usedProgramStateID) == null && !programStateView.getItems().isEmpty()) {
            usedProgramStateID = programStateView.getItems().get(0).getPersonalID();
        }

        setOutputView();
        setFileTableView();
        setExecutionStackView();
        setHeapTableView();
        setSymbolTableView();
        setProcTableView();

        concurrencyWindow.refresh();
    }

    public void manualInitialize() throws IOException {
        //CREATE PROGRAM TO RUN
        application = startup();

        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("concurrency-window.fxml"));
        Scene concurrencyScene = new Scene(fxmlLoader.load());
        Stage concurrencyStage = new Stage();
        concurrencyStage.setScene(concurrencyScene);
        concurrencyStage.setTitle("Toy Interpreter - Concurrency Watches");

        concurrencyWindow = fxmlLoader.getController();
        concurrencyWindow.manualInitialize(application);
        concurrencyStage.show();

        prerefresh();
        refresh();
    }

    @FXML
    protected void onRunButtonClick() throws IOException, InterruptedException {
        List<ProgramState> programStateList = application.getRepo().getProgramList();
        if (!programStateList.isEmpty()) {
            HeapADT<IValue> workaround = programStateList.get(0).getHeap();
            workaround.setHeap(
                    application.safeGC(application.allAddresses(programStateList), workaround.returnHeap())
            );

            try {
                application.oneStepForAllPrgStates(programStateList);
            }
            catch (Exception e) {
                handleExceptionViaMessageBox(e);
            }


            programStateList = application.removeCompletedPrg(application.getRepo().getProgramList());

            prerefresh();
            application.getRepo().setProgramList(programStateList);
            refresh();
        }
        else {
            application.executor.shutdownNow();
            Stage stage = (Stage) runButton.getScene().getWindow();

            concurrencyWindow.forceClose();
            stage.close();

            ProgramState.cleanAllStatics();
        }
    }

    @FXML
    protected void onPrgStateListMouseClick() {
        ProgramState selected = programStateView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            usedProgramStateID = selected.getPersonalID();
            prerefresh();
            refresh();
        }

    }
}
