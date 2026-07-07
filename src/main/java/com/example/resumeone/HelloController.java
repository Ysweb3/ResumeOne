package com.example.resumeone;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HelloController {
    @FXML
   private Button btnNewProject;

    @FXML
    private VBox projectList;

    @FXML
    public void initialize() throws IOException {
        captureRunningApps();
    }

    @FXML
    private void onNewProjectClick(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New project");
        dialog.setHeaderText(null);
        dialog.setContentText("Project Name:");

        dialog.showAndWait().ifPresent(name -> {
            HBox projectRow = new HBox(10);
            Label projectLabel = new Label(name);
            projectLabel.setStyle("-fx-text-fill: white; -fx-padding: 8px;");


            Button resumeBtn = new Button();
            resumeBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-:bold;");
            resumeBtn.setText("Resume");

            Button deleteBtn = new Button();
            deleteBtn.setStyle("-fx-background-color: #d60630; -fx-text-fill: white; -fx-font-weight:bold;");
            deleteBtn.setText("Delete");

            projectRow.getChildren().addAll(projectLabel,resumeBtn,deleteBtn);
            projectList.getChildren().add(projectRow);

        });

    }
    public void captureRunningApps() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "tasklist");
        pb.redirectErrorStream(true);
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\s+");
            System.out.println(parts[0]);
        }
    }
}
