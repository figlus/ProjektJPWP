package sample;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigFile {
    private final String fileName = getClass().getResource("/config.test").getFile();
    private HashMap <String, Integer> players = new HashMap<String, Integer>();


    public HashMap<String, Integer> load() throws IOException {
        List<String> usefulLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            // Create list of useful lines (no nulls, empty or comment lines
            String line = br.readLine();
            while (line != null) {
                if (line.length() == 0) {
                    line = br.readLine();
                    continue;
                }
                if (line.charAt(0) == '#') {
                    line = br.readLine();
                    continue;
                }
                usefulLines.add(line);
                line = br.readLine();
            }
        }

        return userFind(usefulLines);

    }

    private HashMap <String, Integer> userFind(List<String> usefulLines) {
        int genStartIndex = usefulLines.indexOf("[Users]") + 1;
        int genEndIndex = usefulLines.indexOf("[endUsers]");
        for (int i = genStartIndex; i < genEndIndex; i++) {
            String line = usefulLines.get(i);
            if (line.charAt(0) == '<') {
                if (line.charAt(1) != '/') {
                    String userNick = line.substring(1, line.length() - 1);
                    String coins = usefulLines.get(i + 1);
                    int coinsNick = Integer.parseInt(coins);
                    players.put(userNick, coinsNick);
                    int endSingleUserNikc = usefulLines.indexOf("</" + userNick + ">");

                }
            }
        }
        return players;
    }

}

