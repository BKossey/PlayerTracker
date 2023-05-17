package ui;

import model.*;
import persistence.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.GridLayout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

// Represents all ui associated application code required to run the program and its functionality
public class PlayerTrackerApp {

    private static final String PROFILE = "./data/profile.json";
    private Profile user;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 700;

    private JDesktopPane desktop;
    private JInternalFrame controlPanel;
    private JInternalFrame matchRecorder;
    private JInternalFrame keyPad;
    private JInternalFrame filter;
    private JList list;
    private DefaultListModel listModel;

    private JFrame frame;
    private KeyPad kp;

    private JLabel image;
    private ImageIcon lolIcon = new ImageIcon("./Images/LoL_icon.png");
    private ImageIcon jinxIcon = new ImageIcon("./Images/JinxSquare.png");
    private ImageIcon ezrealIcon = new ImageIcon("./Images/EzrealSquare.png");
    private ImageIcon twitchIcon = new ImageIcon("./Images/TwitchSquare.png");
    private ImageIcon zeriIcon = new ImageIcon("./images/ZeriSquare.png");
    private ImageIcon jhinIcon = new ImageIcon("./images/JhinSquare.png");


    private Champion selectedChampion;
    private int selectedKills;
    private int selectedDeaths;
    private int selectedAssists;
    private boolean selectedWinLose;

    private Boolean filterSelected;

    // EFFECTS: runs the match history app
    public PlayerTrackerApp() throws FileNotFoundException {
        launchPlayerTracker();
    }

    // MODIFIES: this
    // EFFECTS: initiate user profile from user input
    private void launchPlayerTracker() throws FileNotFoundException {

        input = new Scanner(System.in);

        user = new Profile("New User");
        selectedChampion = user.getChampions().get(0);
        selectedKills = 0;
        selectedDeaths = 0;
        selectedAssists = 0;
        selectedWinLose = true;
        filterSelected = false;


        jsonWriter = new JsonWriter(PROFILE);
        jsonReader = new JsonReader(PROFILE);

        graphicsInitializer();

        runPlayerTracker();
    }


    // MODIFIES: this
    // EFFECTS: Process user input
    private void runPlayerTracker() {
        boolean keepGoing = true;
        String command = null;


        while (keepGoing) {
            displayMenu();
            command = input.nextLine();
            command = command.toLowerCase(Locale.ROOT);

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("Exiting Program.");
        for (Event next: EventLog.getInstance()) {
            System.out.println(next.getDescription());
        }
    }

    // EFFECTS: sets user name from user input
    private void setUserName() {
        System.out.println("Please enter your summoner name.");
        user.setProfileName(input.nextLine());
    }


    // EFFECTS: Displays menu of options
    private void displayMenu() {
        System.out.println("Select from:");
        System.out.println("v -> View profile");
        System.out.println("n -> Set Username");
        System.out.println("p -> Promote rank");
        System.out.println("d -> Demote rank");
        System.out.println("r -> Record a match played");
        System.out.println("h -> View match history");
        System.out.println("s -> View champion specific stats");
        System.out.println("w -> Save user profile");
        System.out.println("l -> Load user profile");
        System.out.println("q -> Quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        switch (command) {
            case "v":
                viewProfile();
                break;
            case "p":
                promoteRank();
                break;
            case "d":
                demoteRank();
                break;
            case "r":
                recordMatchInputs();
                break;
            case "h":
                viewMatchHistory();
                break;
            case "s":
                viewStats();
                break;
            default:
                processCommand2(command);
                break;
        }

    }

    // MODIFIES: this
    // EFFECTS: processes more user commands
    private void processCommand2(String command) {
        switch (command) {
            case "n":
                setUserName();
                break;
            case "w":
                saveProfile();
                break;
            case "l":
                loadProfile();
                break;
            default:
                System.out.println("Selection not valid");
                break;

        }
    }

    // MODIFIES: this
    // EFFECTS: loads profile from profile.json
    private void loadProfile() {
        try {
            user = jsonReader.read();
            System.out.println("Loaded " + user.getProfileName() + " from " + PROFILE);

            selectedChampion = user.getChampions().get(0);
            image.setIcon(jinxIcon);
            user.loadLog();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + PROFILE);
        }
    }

    // MODIFIES: profile.json
    // EFFECTS: saves profile to profile.json
    private void saveProfile() {
        try {
            jsonWriter.open();
            jsonWriter.write(user);
            jsonWriter.close();
            System.out.println("Saved " + user.getProfileName() + " to " + PROFILE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + PROFILE);
        }
    }

    // EFFECTS: displays profile overview
    private void viewProfile() {
        double gp = 0;
        double wins = 0;
        for (Match m : user.getMatchHistory()) {
            gp += 1;
            if (m.getWonStatus()) {
                wins += 1;
            }
        }
        String winrate = Double.toString((wins / gp) * 100);
        String games = Integer.toString(Math.toIntExact(Math.round(gp)));
        System.out.println("Summoner Name: " + user.getProfileName());
        System.out.println("Rank: " + user.getRank());
        System.out.println(user.getProfileName() + " has " + games + " games played with a win rate of: "
                + winrate + "%");
        System.out.println("");

    }

    // MODIFIES: this
    // EFFECTS: promotes the user one rank
    private void promoteRank() {
        if (Objects.equals(user.getRank(), "Challenger")) {
            System.out.println(user.getProfileName() + " is already Challenger and can not promote any higher.");
            System.out.println("");
        } else {
            user.promote();
            System.out.println(user.getProfileName() + " has been promoted to " + user.getRank());
            System.out.println("");
        }

    }

    // MODIFIES: this
    // EFFECTS: demotes the user one rank
    private void demoteRank() {
        if (Objects.equals(user.getRank(), "Unranked")) {
            System.out.println(user.getProfileName() + " is not currently ranked and thus can not demote.");
            System.out.println("");
        } else if (Objects.equals(user.getRank(), "Iron")) {
            System.out.println(user.getProfileName() + " is already Iron and can not demote any lower.");
            System.out.println("");
        } else {
            user.demote();
            System.out.println(user.getProfileName() + " has been demoted to " + user.getRank());
            System.out.println("");
        }

    }

    // MODIFIES: this
    // EFFECTS: records user inputs about a new match to record
    private void recordMatchInputs() {
        boolean winOrLoseBoolean = false;

        askChampionPlayed();

        String command = input.nextLine();
        command = command.toLowerCase(Locale.ROOT);

        if (isAChampion(command)) {
            Champion champion = selectChampion(command);
            System.out.println("Input kills: ");
            String kills = input.nextLine();

            System.out.println("Input deaths: ");
            String deaths = input.nextLine();

            System.out.println("Input assists: ");
            String assists = input.nextLine();

            if (checkIsANumber(kills, deaths, assists)) {
                System.out.println("Was the game a win? Input Y or N");
                String winOrLose = input.nextLine();
                if (Objects.equals(winOrLose, "Y")) {
                    winOrLoseBoolean = true;
                }
                recordMatch(champion, kills, deaths, assists, winOrLoseBoolean);
            }

        } else {
            System.out.println("ERROR: not a champion");
        }
    }


    // EFFECTS: displays match history.
    private void viewMatchHistory() {
        System.out.println("END OF MATCH HISTORY");
        for (Match m : user.getMatchHistory()) {
            String match = matchToString(m);
            System.out.println(match);
        }
        System.out.println("BEGINNING OF MATCH HISTORY");
        System.out.println("");
    }

    // MODIFIES: this
    // EFFECTS: displays champion statistics of users selected champion
    private void viewStats() {
        String command = null;

        System.out.println("Select champion to view: ");
        for (Champion c : user.getChampions()) {
            System.out.println(c.getChampionName());
        }
        command = input.nextLine();
        command = command.toLowerCase(Locale.ROOT);

        if (isAChampion(command)) {
            Champion champion = selectChampion(command);
            double games = champion.getTotalGamesPlayed();
            double wins = champion.getTotalWins();
            double winrate = (wins * 100 / games);

            System.out.println(champion.getChampionName() + " has " + Integer.toString(champion.getTotalKills())
                    + " kills " + Integer.toString(champion.getTotalDeaths()) + " deaths and  "
                    + Integer.toString(champion.getTotalAssists()) + " assists");
            System.out.println(Integer.toString(champion.getTotalGamesPlayed()) + " games played and a win ratio of "
                    + Double.toString(winrate) + "%");
            System.out.println("TOTAL KDA: " + Double.toString(getChampionKDA(champion)));

        } else {
            System.out.println("ERROR: not a champion");
        }
    }


    // REQUIRES: champion is one of possible champions
    // EFFECTS: selects and returns a champion based off champion name string
    private Champion selectChampion(String champion) {
        for (Champion c : user.getChampions()) {
            if (Objects.equals(c.getChampionName().toLowerCase(Locale.ROOT), champion)) {
                return c;
            }
        }
        return null;
    }


    // MODIFIES: Champion
    // EFFECTS: updates a champions stats
    private void updateStats(Champion champion, int kills, int deaths, int assists, boolean winOrLose) {
        champion.addGamePlayed();
        champion.addKills(kills);
        champion.addDeaths(deaths);
        champion.addAssists(assists);
        if (winOrLose) {
            champion.addWin();
        }

    }

    // EFFECTS: returns (kill + assists) / deaths for a champion
    private double getChampionKDA(Champion champion) {
        double kills = champion.getTotalKills();
        double deaths = champion.getTotalDeaths();
        double assists = champion.getTotalAssists();

        return (kills + assists) / deaths;
    }

    // EFFECTS: lists champions and asks which was played for recordMatchInputs method
    private void askChampionPlayed() {
        System.out.println("Input champion played: ");
        for (Champion c : user.getChampions()) {
            System.out.println(c.getChampionName());
        }
    }

    // EFFECTS: checks if command is one of the champions
    private boolean isAChampion(String command) {
        return command.equals("jinx") || command.equals("twitch")
                || command.equals("ezreal") || command.equals("zeri") || command.equals("jhin");
    }

    // MODIFIES: matchHistory, Champion
    // EFFECTS: Creates a match with inputs from recordMatchInputs. Adds match to matchHistory. Updates champion stats
    private void recordMatch(Champion champion, String kills, String deaths, String assists,
                             boolean winOrLoseBoolean) {
        Match match;

        match = new Match(champion, parseInt(kills), parseInt(deaths), parseInt(assists), winOrLoseBoolean);
        user.record(match);
        updateStats(champion, parseInt(kills), parseInt(deaths), parseInt(assists), winOrLoseBoolean);
    }

    // EFFECTS: ensures inputs for kills(k), deaths(d) and assists(a) are integers to prevent crashing
    private boolean checkIsANumber(String k, String d, String a) {
        try {
            parseInt(k);
            parseInt(d);
            parseInt(a);
        } catch (Exception e) {
            System.out.println("ERROR: One or more inputs for K/D/A are not integers");
            return false;
        }
        return true;
    }

    // EFFECTS: constructs and displays user graphical interface
    private void graphicsInitializer() {
        frame = new JFrame();
        image = new JLabel();

        desktop = new JDesktopPane();
        desktop.addMouseListener(new DesktopFocusAction());

        setupJInternalFrames();

        frame.setContentPane(desktop);
        frame.setTitle("LoL Player Tracker");
        frame.setSize(WIDTH, HEIGHT);

        addButtonPanel();
        addMatchHistory();
        addMatchRecorderPanelUI();
        addKeyPad();
        addFilter();
        imagePanel();

        setupPanels();

        addWindowListener();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        centreOnScreen();
        frame.setVisible(true);

    }

    // EFFECTS: adds WindowListener to print EventLog when program exited
    public void addWindowListener() {
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                for (Event next: EventLog.getInstance()) {
                    System.out.println(next.getDescription());
                }
                System.exit(0);

            }
        };
        frame.addWindowListener(exitListener);
    }


    //EFFECTS: Constructs JInternalFrames
    private void setupJInternalFrames() {
        controlPanel = new JInternalFrame("Control Panel", false, false, false,
                false);
        controlPanel.setLayout(new BorderLayout());
        matchRecorder = new JInternalFrame("Record Match Inputs Selecter", false, false, false,
                false);
        matchRecorder.setLayout(new BorderLayout());
        keyPad = new JInternalFrame("KeyPad", false, false, false, false);
        keyPad.setLayout(new BorderLayout());
        filter = new JInternalFrame("Filter", false, false, false, false);
        filter.setLayout(new BorderLayout());


    }

    // EFFECTS: sets up internal interfaces
    private void setupPanels() {
        controlPanel.pack();
        controlPanel.setVisible(true);
        keyPad.pack();
        keyPad.setVisible(true);
        keyPad.setLocation(325, 0);
        matchRecorder.setLocation(0, 250);
        matchRecorder.pack();
        matchRecorder.setVisible(true);
        filter.pack();
        filter.setLocation(325, 150);
        filter.setVisible(true);
        desktop.add(controlPanel);
        desktop.add(matchRecorder);
        desktop.add(keyPad);
        desktop.add(filter);

    }

    // EFFECTS: converts a match into a string summary
    private String matchToString(Match match) {

        Champion champion = match.getChampionPlayed();
        String championName = champion.getChampionName();
        String kills = Integer.toString(match.getKills());
        String deaths = Integer.toString(match.getDeaths());
        String assists = Integer.toString(match.getAssists());
        String winOrLose;
        if (match.getWonStatus()) {
            winOrLose = "W";
        } else {
            winOrLose = "L";
        }
        return championName + " " + kills + "/" + deaths + "/" + assists + " " + winOrLose;

    }

    // EFFECTS: adds the match history panel to GUI
    private void addMatchHistory() {
        listModel = new DefaultListModel();

        list = new JList(listModel);
        list.setVisibleRowCount(10);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);

        JScrollPane matchHistoryList = new JScrollPane(list);


        controlPanel.add(matchHistoryList, BorderLayout.CENTER);
    }

    // EFFECTS: centers main application on desktop
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        frame.setLocation((width - frame.getWidth()) / 2, (height - frame.getHeight()) / 2);
    }

    // EFFECTS: adds keypad to main application window
    private void addKeyPad() {
        kp = new KeyPad();
        frame.addKeyListener(kp);
        keyPad.add(kp, BorderLayout.CENTER);

    }

    // EFFECTS: adds filter selecter panel to GUI
    private void addFilter() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new GridLayout(2, 1));
        filterPanel.add(new JButton(new FilterOnAction()));
        filterPanel.add(new JButton(new FilterOffAction()));

        filter.add(filterPanel);

    }

    // EFFECTS: adds image panel to match recorder GUI
    private void imagePanel() {
        JPanel imagePanel = new JPanel();

        image.setIcon(lolIcon);
        imagePanel.add(image);

        matchRecorder.add(imagePanel);

    }

    // MODIFIES: this
    // EFFECTS: updates filterSelected to true
    private class FilterOnAction extends AbstractAction {

        FilterOnAction() {
            super("ON");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            filterSelected = true;

        }
    }

    // MODIFIES: this
    // EFFECTS: updates filterSelected to true
    private class FilterOffAction extends AbstractAction {

        FilterOffAction() {
            super("OFF");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            filterSelected = false;
            user.filterOff();

        }
    }


    // EFFECTS: adds button panel to GUI
    private void addButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2));
//        buttonPanel.add(new JButton(new ViewProfileAction()));
//        buttonPanel.add(new JButton(new SetUserNameAction()));
//        buttonPanel.add(new JButton(new PromoteAction()));
//        buttonPanel.add(new JButton(new DemoteAction()));
        buttonPanel.add(new JButton(new RecordMatchAction()));
        buttonPanel.add(new JButton(new ViewMatchHistoryAction()));
//        buttonPanel.add(new JButton(new ViewStatsAction()));
        buttonPanel.add(new JButton(new SaveProfileAction()));
        buttonPanel.add(new JButton(new LoadProfileAction()));
//        buttonPanel.add(new JButton(new QuitAction()));

        controlPanel.add(buttonPanel, BorderLayout.PAGE_END);

    }


    // EFFECTS: constructs match recorder user interface
    private void addMatchRecorderPanelUI() {


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(((new GridLayout(2, 5))));
        buttonPanel.add(new JButton(new JinxAction()));
        buttonPanel.add(new JButton(new TwitchAction()));
        buttonPanel.add(new JButton(new EzrealAction()));
        buttonPanel.add(new JButton(new ZeriAction()));
        buttonPanel.add(new JButton(new JhinAction()));

        buttonPanel.add(new JButton(new KillsAction()));
        buttonPanel.add(new JButton(new DeathsAction()));
        buttonPanel.add(new JButton(new AssistsAction()));
        buttonPanel.add(new JButton(new WinAction()));
        buttonPanel.add(new JButton(new LoseAction()));

        matchRecorder.add(buttonPanel, BorderLayout.PAGE_END);

    }

    // MODIFIES: this
    // EFFECTS: updates selectedKills based off keypad input
    private class KillsAction extends AbstractAction {

        KillsAction() {
            super("Set Kills");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            selectedKills = Integer.parseInt(kp.getCode());

        }
    }

    // MODIFIES: this
    // EFFECTS: updates selectedDeaths based off keypad input
    private class DeathsAction extends AbstractAction {

        DeathsAction() {
            super("Set Deaths");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            selectedDeaths = Integer.parseInt(kp.getCode());

        }
    }

    // MODIFIES: this
    // EFFECTS: updates selectedAssists based off keypad input
    private class AssistsAction extends AbstractAction {

        AssistsAction() {
            super("Set Assists");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            selectedAssists = Integer.parseInt(kp.getCode());

        }
    }

    // MODIFIES: this
    // EFFECTS: updates selectedWinLose to true
    private class WinAction extends AbstractAction {

        WinAction() {
            super("Set Win");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            selectedWinLose = true;

        }
    }

    // MODIFIES: this
    // EFFECTS: updates selectedWinLose to false
    private class LoseAction extends AbstractAction {

        LoseAction() {
            super("Set Loss");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            selectedWinLose = false;

        }
    }

    // MODIFIES: this
    // EFFECTS: updates selectedChampion to Jinx
    private class JinxAction extends AbstractAction {

        JinxAction() {
            super("Jinx");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            selectedChampion = user.getChampions().get(0);
            image.setIcon(jinxIcon);


        }
    }

    // MODIFIES: this
    // EFFECTS: updates selectedChampion to Twitch
    private class TwitchAction extends AbstractAction {

        TwitchAction() {
            super("Twitch");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            selectedChampion = user.getChampions().get(1);
            image.setIcon(twitchIcon);

        }
    }

    // MODIFIES: this
    // EFFECTS: updates selectedChampion to Ezreal
    private class EzrealAction extends AbstractAction {

        EzrealAction() {
            super("Ezreal");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            selectedChampion = user.getChampions().get(2);
            image.setIcon(ezrealIcon);

        }
    }

    // MODIFIES: this
    // EFFECTS: updates selectedChampion to Zeri
    private class ZeriAction extends AbstractAction {

        ZeriAction() {
            super("Zeri");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            selectedChampion = user.getChampions().get(3);
            image.setIcon(zeriIcon);

        }
    }

    // MODIFIES: this
    // EFFECTS: updates selectedChampion to Jhin
    private class JhinAction extends AbstractAction {

        JhinAction() {
            super("Jhin");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            selectedChampion = user.getChampions().get(4);
            image.setIcon(jhinIcon);

        }
    }

    //EFFECTS: button to record a new match
    private class RecordMatchAction extends AbstractAction {

        RecordMatchAction() {
            super("Record Match");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Match match = new Match(selectedChampion, selectedKills, selectedDeaths, selectedAssists, selectedWinLose);
            user.record(match);

        }
    }

    // EFFECTS: button to refresh the displayed match history
    private class ViewMatchHistoryAction extends AbstractAction {

        ViewMatchHistoryAction() {
            super("Refresh Match History");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            listModel.clear();
            if (filterSelected) {
                ArrayList<Match> filteredMatchHistory = user.filter(selectedChampion);

                for (Match f : filteredMatchHistory) {
                    String filteredMatch = matchToString(f);

                    listModel.addElement(filteredMatch);

                }

            } else {
                for (Match m : user.getMatchHistory()) {
                    String match = matchToString(m);

                    listModel.addElement(match);


                }
            }


        }
    }

    // MODIFIES: profile.json
    // EFFECTS: button to save profile data
    private class SaveProfileAction extends AbstractAction {

        SaveProfileAction() {
            super("Save Profile");
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            saveProfile();
        }
    }

    // MODIFIES: this
    // EFFECTS: button to load user profile from profile.json
    private class LoadProfileAction extends AbstractAction {

        LoadProfileAction() {
            super("Load Profile");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            loadProfile();

        }
    }


    // EFFECTS: Switches desktop focus for key handling
    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            PlayerTrackerApp.this.frame.requestFocusInWindow();
        }
    }


}
