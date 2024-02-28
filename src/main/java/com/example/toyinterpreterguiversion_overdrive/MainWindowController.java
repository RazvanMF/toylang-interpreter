package com.example.toyinterpreterguiversion_overdrive;

import com.example.toyinterpreterguiversion_overdrive.exceptions.StandardException;
import com.example.toyinterpreterguiversion_overdrive.model.statements.IStatement;
import com.example.toyinterpreterguiversion_overdrive.view.commands.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MainWindowController {
    @FXML
    public ListView<IStatement> programList = new ListView<IStatement>();
    @FXML
    private Label welcomeText;

    @FXML
    private void initialize() throws StandardException {
        ArrayList<IStatement> programs = new ArrayList<>();
        programs.add(StaticCommandDispenser.basicProgramStatement());
        programs.add(StaticCommandDispenser.forkTestProgramStatement());
        programs.add(StaticCommandDispenser.procedureTestProgramStatement());

        programs.add(StaticCommandDispenser.concurrencyLockTestProgramStatement());
        programs.add(StaticCommandDispenser.concurrencySemaphoreTestProgramStatement());
        programs.add(StaticCommandDispenser.concurrencyLatchTestProgramStatement());
        programs.add(StaticCommandDispenser.concurrencyBarrierTestProgramStatement());
        ObservableList<IStatement> items = FXCollections.observableArrayList(programs);
        programList.setItems(items);
    }

    @FXML
    protected void onOpenButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("side-window.fxml"));
        Scene sidescene = new Scene(fxmlLoader.load());
        Stage sidestage = new Stage();
        sidestage.setScene(sidescene);
        sidestage.setTitle("Toy Interpreter - Program Window");

        SideWindowController controller = fxmlLoader.getController();
        if (programList.getSelectionModel().getSelectedItem() != null)
            controller.programToRun = programList.getSelectionModel().getSelectedItem();
        else
            controller.programToRun = programList.getItems().get(0);
        controller.manualInitialize();
        sidestage.show();
    }
}