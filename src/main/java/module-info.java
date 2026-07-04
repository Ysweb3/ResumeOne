module com.example.resumeone {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.resumeone to javafx.fxml;
    exports com.example.resumeone;
}