package com.minesweeper.main;

import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Highscores {

    private File highscoreFile;
    private HashMap<String, List<Score>> highscores;


    public Highscores(String filePath) {
        highscoreFile = new File(filePath);
        highscores = new HashMap<>();
        readHighscores();
        System.out.println("Raw:");
        printAllScores();
        sortHighscores();
    }

    public void readHighscores() {
        try {
            if (!highscoreFile.exists())
                return;
            highscores.clear();
            BufferedReader in = new BufferedReader(new FileReader(highscoreFile));
            String line;
            String listName = "";
            while (in.ready() && (line = in.readLine()) != null) {
                if (line.startsWith("§/§ ")) {
                    listName = line.substring(4);
                } else if (line.startsWith("- ") && !listName.equals("")) {
                    line = line.substring(2);
                    String[] split = line.split(" ");
                    add(listName, split[0], split[1], split[2], split[3]);
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeHighscores() {
        System.out.println("Writing...");
        printAllScores();
        try {
            if (!highscoreFile.exists())
                highscoreFile.createNewFile();
            PrintWriter out = new PrintWriter(highscoreFile);
            for (String lKey : highscores.keySet()) {
                out.write("§/§ " + lKey + "\n");
                for (Score s : highscores.get(lKey)) {
                    out.write("- " + s.getName() + " " + s.getTime() + " " + s.getDifficulty() + " " + s.getBombCount() + "\n");
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sortHighscores() {
        if (highscores.isEmpty())
            return;
        highscores.forEach((s, l) -> l.sort(Comparator.comparing(Score::getTime)));
        /*highscores.sort((o1, o2) -> {
            if(o1.getValue() < o2.getValue())
                return -1;
            else if(o1.getValue() == o2.getValue())
                return 0;
            else
                return 1;
        });*/

        System.out.println("Sorted: ");
        printAllScores();
    }

    private void add(String listName, String name, String time, String difficulty, String bombCount) {
        add(listName, name, Integer.parseInt(time), Float.parseFloat(difficulty), Integer.parseInt(bombCount));
    }

    public void add(String listName, String name, int time, float difficulty, int bombCount) {
        System.out.println("Adding " + name + " " + time + " to " + listName);
        if (!highscores.containsKey(listName))
            highscores.put(listName, new LinkedList<>());
        highscores.get(listName).add(new Score(name, time, difficulty, bombCount));
    }

    public void printAllScores() {
        for (String lName : highscores.keySet()) {
            System.out.println(lName + ":");
            printScores(lName);
        }
    }

    public void printScores(String listName) {
        highscores.get(listName).forEach((s) -> System.out.println(s.getName() + " " + s.getTime() + " "
                + s.getDifficulty() + " " + s.getBombCount()));
    }


    private class Score {

        final String name;
        final int time;
        final float difficulty;
        final int bombCount;

        Score(String name, int time, float difficulty, int bombCount) {
            this.name = name;
            this.time = time;
            this.difficulty = difficulty;
            this.bombCount = bombCount;
        }

        String getName() {
            return name;
        }

        int getTime() {
            return time;
        }

        float getDifficulty() {
            return difficulty;
        }

        int getBombCount() {
            return bombCount;
        }

        public float getBombTime() {
            return time / bombCount;
        }
    }

}
