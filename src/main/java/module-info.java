module com.example.toyinterpreterguiversion {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.toyinterpreterguiversion_overdrive to javafx.fxml;
    exports com.example.toyinterpreterguiversion_overdrive;
}