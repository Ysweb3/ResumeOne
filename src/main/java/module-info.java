module com.example.resumeone {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.example.resumeone to javafx.fxml;
    exports com.example.resumeone;
}