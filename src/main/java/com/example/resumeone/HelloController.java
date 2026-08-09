package com.example.resumeone;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class HelloController {
    @FXML
    public Button btnNewProject;

    @FXML
    public VBox projectList;

    public ArrayList<Project> projects = new ArrayList<>();

    @FXML
    public void initialize() throws IOException {

        segregateApps(captureRunningApps());
    }

    @FXML
    public void onNewProjectClick(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New project");
        dialog.setHeaderText(null);
        dialog.setContentText("Project Name:");

        dialog.showAndWait().ifPresent(name -> {
            HBox projectRow = new HBox(10);
            Label projectLabel = new Label(name);
            projectLabel.setStyle("-fx-text-fill: white; -fx-padding: 8px;");
            System.out.println(name);



            Button resumeBtn = new Button();
            resumeBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight:bold;");
            resumeBtn.setText("Resume");

            Project projectObj = new Project(name, new ArrayList<>(), new HashMap<>(), new ArrayList<>());
            projects.add(projectObj);

            resumeBtn.setUserData(projectObj);

            resumeBtn.setOnAction(e ->{

            });

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
                try {
                    projectObj.apps = segregateApps(runningApps);
                    saveProject(projectObj);

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

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
    public HashMap<String, HashSet<String>> segregateApps(HashSet<String> appset){
        //takes appset and categorizes it into results hashmap with hashset of item
        HashMap<String, String> knownApps = new HashMap();
        HashMap<String, HashSet<String>> result = new HashMap<>();
        knownApps.put("brave.exe", "Browser");
        knownApps.put("chrome.exe", "Browser");
        knownApps.put("firefox.exe", "Browser");
        knownApps.put("msedge.exe", "Browser");

        knownApps.put("code.exe", "IDE");
        knownApps.put("idea64.exe", "IDE");
        knownApps.put("devenv.exe", "IDE");
        knownApps.put("eclipse.exe", "IDE");

        knownApps.put("spotify.exe", "Music");
        knownApps.put("amazonmusic.exe", "Music");

        knownApps.put("discord.exe", "Communication");
        knownApps.put("slack.exe", "Communication");
        knownApps.put("teams.exe", "Communication");

        knownApps.put("notion.exe", "Notes");
        knownApps.put("obsidian.exe", "Notes");
        knownApps.put("onenote.exe", "Notes");

        knownApps.put("windowsterminal.exe", "Terminal");
        knownApps.put("cmd.exe", "Terminal");
        knownApps.put("powershell.exe", "Terminal");

        result.put("IDE", new HashSet<>());
        result.put("Browser",new HashSet<>());
        result.put("Music",new HashSet<>());
        result.put("Communication",new HashSet<>());
        result.put("Notes",new HashSet<>());
        result.put("Terminal",new HashSet<>());

        for (String item: knownApps.keySet()){
            if (appset.contains(item)){
               result.get(knownApps.get(item)).add(item);
            }
        }
        for (String item : result.keySet()) {
            System.out.println(item + " -> " + result.get(item));
        }
        return result;
    }
    public void saveProject(Project projectObj) throws IOException {

        ArrayList<String> objFolderPath = null;
        ArrayList<String> objUrls = new ArrayList<>();

        projects.add(projectObj);
        System.out.println(projectObj.urls);

        String dirPath = System.getenv("APPDATA") + "\\ResumeWork";
        new File(dirPath).mkdirs();
        String filePath = dirPath + "\\projects.json";

        Gson gson = new Gson();
        String jsonObj = gson.toJson(projectObj);

        //TODO:adding multiple projects and fix the json formatting
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(jsonObj);
        writer.newLine();
        writer.close();
    }

}
