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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class HelloController {
    @FXML
   private Button btnNewProject;

    @FXML
    private VBox projectList;

    @FXML
    public void initialize() throws IOException {

        segregateApps(captureRunningApps());
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
            resumeBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight:bold;");
            resumeBtn.setText("Resume");

            Button checkpointBtn = new Button();
            checkpointBtn.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: black; -fx-font-weight:bold;");
            checkpointBtn.setText("Add Checkpoint");
            checkpointBtn.setOnAction(e -> {
                HashSet<String> runningApps = null;
                try {
                    runningApps = captureRunningApps();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                segregateApps(runningApps);
            });

            Button deleteBtn = new Button();
            deleteBtn.setStyle("-fx-background-color: #d60630; -fx-text-fill: white; -fx-font-weight:bold;");
            deleteBtn.setText("Delete");

            projectRow.getChildren().addAll(projectLabel,resumeBtn,deleteBtn,checkpointBtn);
            projectList.getChildren().add(projectRow);

        });

    }
    //IDEA:Also add a CLI to start this thing

    public HashSet<String> captureRunningApps() throws IOException {
        //runs tasklist, returns HashSet of running processes
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "tasklist");
        pb.redirectErrorStream(true);
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );
        String line;
        HashSet<String> appset = new HashSet<>();
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\s+");
//            System.out.println(parts[0]);
            appset.add(parts[0]);
        }
        return appset;
    }
    public void  segregateApps(HashSet<String> appset){
        //takes appset and categorizes it into results hashmap with hashset of item
        HashMap<String, String> knownApps = new HashMap();
        HashMap<String, HashSet<String>> result = new HashMap<>();
        knownApps.put("brave.exe","Browser");

        result.put("IDE", new HashSet<>());
        result.put("Browser",new HashSet<>());

        for (String item: knownApps.keySet()){
            if (appset.contains(item)){
               result.get(knownApps.get(item)).add(item);
            }
        }
        for (String item : result.keySet()) {
            System.out.println(item + " -> " + result.get(item));
        }
    }
}
