package com.example.toyinterpreterguiversion_overdrive;

import com.example.toyinterpreterguiversion_overdrive.controller.Controller;
import com.example.toyinterpreterguiversion_overdrive.model.ProgramState;
import com.example.toyinterpreterguiversion_overdrive.utils.barriertable.BarrierTableValueClass;
import com.example.toyinterpreterguiversion_overdrive.utils.latchtable.LatchTableValueClass;
import com.example.toyinterpreterguiversion_overdrive.utils.locktable.LockTableValueClass;
import com.example.toyinterpreterguiversion_overdrive.utils.semaphoretable.SemaphoreTableValueClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class ConcurrencyWindowController {
    private Controller refApplication;

    @FXML private TableView lockTableView = new TableView<>();
          private ObservableList<LockTableValueClass> lockTableElements;
          private TableColumn<LockTableValueClass, String> addrLockTableColumn;
          private TableColumn<LockTableValueClass, String> idLockTableColumn;


    @FXML private TableView semaphoreTableView = new TableView<>();
          private  ObservableList<SemaphoreTableValueClass> semaphoreTableElements;
          private TableColumn<SemaphoreTableValueClass, String> addrSemTableColumn;
          private TableColumn<SemaphoreTableValueClass, String> capSemTableColumn;
          private TableColumn<SemaphoreTableValueClass, String> usersSemTableColumn;


    @FXML private TableView latchTableView = new TableView<>();
          private ObservableList<LatchTableValueClass> latchTableElements;
          private TableColumn<LatchTableValueClass, String> addrLatchTableColumn;
          private TableColumn<LatchTableValueClass, String> valueLatchTableColumn;


    @FXML private TableView barrierTableView = new TableView<>();
          private ObservableList<BarrierTableValueClass> barrierTableElements;
          private TableColumn<BarrierTableValueClass, String> addrBarrTableColumn;
          private TableColumn<BarrierTableValueClass, String> capBarrTableColumn;
          private TableColumn<BarrierTableValueClass, String> subsBarrTableColumn;



    @FXML public void initialize() {
        addrLockTableColumn = new TableColumn<>("Address");
        idLockTableColumn = new TableColumn<>("PrgID");
        lockTableView.getColumns().addAll(addrLockTableColumn, idLockTableColumn);

        addrLockTableColumn.prefWidthProperty().bind(lockTableView.widthProperty().multiply(0.5));
        idLockTableColumn.prefWidthProperty().bind(lockTableView.widthProperty().multiply(0.5));

        addrLockTableColumn.setResizable(false);
        idLockTableColumn.setResizable(false);


        addrSemTableColumn = new TableColumn<>("Address");
        capSemTableColumn = new TableColumn<>("Capacity");
        usersSemTableColumn = new TableColumn<>("PrgIDs");
        semaphoreTableView.getColumns().addAll(addrSemTableColumn, capSemTableColumn, usersSemTableColumn);

        addrSemTableColumn.prefWidthProperty().bind(semaphoreTableView.widthProperty().multiply(0.33));
        capSemTableColumn.prefWidthProperty().bind(semaphoreTableView.widthProperty().multiply(0.33));
        usersSemTableColumn.prefWidthProperty().bind(semaphoreTableView.widthProperty().multiply(0.34));

        addrSemTableColumn.setResizable(false);
        capSemTableColumn.setResizable(false);
        usersSemTableColumn.setResizable(false);


        addrLatchTableColumn = new TableColumn<>("Address");
        valueLatchTableColumn = new TableColumn<>("Value");
        latchTableView.getColumns().addAll(addrLatchTableColumn, valueLatchTableColumn);

        addrLatchTableColumn.prefWidthProperty().bind(latchTableView.widthProperty().multiply(0.5));
        valueLatchTableColumn.prefWidthProperty().bind(latchTableView.widthProperty().multiply(0.5));

        addrLatchTableColumn.setResizable(false);
        valueLatchTableColumn.setResizable(false);


        addrBarrTableColumn = new TableColumn<>("Address");
        capBarrTableColumn = new TableColumn<>("Capacity");
        subsBarrTableColumn = new TableColumn<>("PrgIDs");
        barrierTableView.getColumns().addAll(addrBarrTableColumn, capBarrTableColumn, subsBarrTableColumn);

        addrBarrTableColumn.prefWidthProperty().bind(barrierTableView.widthProperty().multiply(0.33));
        capBarrTableColumn.prefWidthProperty().bind(barrierTableView.widthProperty().multiply(0.33));
        subsBarrTableColumn.prefWidthProperty().bind(barrierTableView.widthProperty().multiply(0.34));

        addrBarrTableColumn.setResizable(false);
        capBarrTableColumn.setResizable(false);
        subsBarrTableColumn.setResizable(false);

    }

    public void manualInitialize(Controller pTR) {
        refApplication = pTR;
    }

    private void setLockTableView() {
        lockTableElements = FXCollections.observableArrayList();
        addrLockTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAddressProperty());
        idLockTableColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
        Map<Integer, Integer> lockTable = ProgramState.getLockTable().getDictionary();
        for (Map.Entry<Integer, Integer> entry : lockTable.entrySet()) {
            lockTableElements.add(new LockTableValueClass(entry.getKey(), entry.getValue()));
        }
        lockTableView.setItems(lockTableElements);
        lockTableView.refresh();
    }

    private void setSemaphoreTableView() {
        semaphoreTableElements = FXCollections.observableArrayList();
        addrSemTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAddressProperty());
        capSemTableColumn.setCellValueFactory(cellData -> cellData.getValue().getCapacityProperty());
        usersSemTableColumn.setCellValueFactory(cellData -> cellData.getValue().getUsersProperty());

        Map<Integer, Pair<Integer, List<Integer>>> semTable = ProgramState.getSemaphoreTable().returnSemTable();
        for (Map.Entry<Integer, Pair<Integer, List<Integer>>> entry : semTable.entrySet()) {
            semaphoreTableElements.add(new SemaphoreTableValueClass(entry.getKey(), entry.getValue().getKey(),
                    entry.getValue().getValue()));
        }
        semaphoreTableView.setItems(semaphoreTableElements);
        semaphoreTableView.refresh();
    }

    private void setLatchTableView() {
        latchTableElements = FXCollections.observableArrayList();
        addrLatchTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAddressProperty());
        valueLatchTableColumn.setCellValueFactory(cellData -> cellData.getValue().getValueProperty());
        Map<Integer, Integer> latchTable = ProgramState.getLatchTable().getDictionary();
        for (Map.Entry<Integer, Integer> entry : latchTable.entrySet()) {
            latchTableElements.add(new LatchTableValueClass(entry.getKey(), entry.getValue()));
        }
        latchTableView.setItems(latchTableElements);
        latchTableView.refresh();
    }

    private void setBarrierTableView() {
        barrierTableElements = FXCollections.observableArrayList();
        addrBarrTableColumn.setCellValueFactory(cellData -> cellData.getValue().getAddressProperty());
        capBarrTableColumn.setCellValueFactory(cellData -> cellData.getValue().getCapacityProperty());
        subsBarrTableColumn.setCellValueFactory(cellData -> cellData.getValue().getSubscribersProperty());

        Map<Integer, Pair<Integer, List<Integer>>> barrTable = ProgramState.getBarrierTable().getDictionary();
        for (Map.Entry<Integer, Pair<Integer, List<Integer>>> entry : barrTable.entrySet()) {
            barrierTableElements.add(new BarrierTableValueClass(entry.getKey(), entry.getValue().getKey(),
                    entry.getValue().getValue()));
        }
        barrierTableView.setItems(barrierTableElements);
        barrierTableView.refresh();
    }

    public void refresh() {
        setLockTableView();
        setSemaphoreTableView();
        setLatchTableView();
        setBarrierTableView();
    }

    public void forceClose() {
        Stage stage = (Stage) lockTableView.getScene().getWindow();
        stage.close();
    }

}
