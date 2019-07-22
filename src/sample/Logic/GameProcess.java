package sample.Logic;

import sample.DialogForm;
import sample.Enum.Country;
import sample.Model.Player;
import sample.Model.Properties.*;

import java.util.ArrayList;
import java.util.Collections;

public class GameProcess {

    private int numOfPlayers;
    private Player currentPlayer;
    private int currentDiceValue;
    private static ArrayList<Player> allPlayers;
    private ArrayList<Property> gameBoard;

    public GameProcess() {
        allPlayers = new ArrayList<>();
        gameBoard = new ArrayList<>();
        currentPlayer = null;
        currentDiceValue = 0;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        if (numOfPlayers > 0 && numOfPlayers < 8)
            this.numOfPlayers = numOfPlayers;
    }

    public GameProcess(int numOfPlayers) {
        if (numOfPlayers > 0)
            this.numOfPlayers = numOfPlayers;
        else
            this.numOfPlayers = 1;

        currentPlayer = null;
        currentDiceValue = 0;

        allPlayers = new ArrayList<>();
        gameBoard = new ArrayList<>();
    }

    public boolean isGameOver() {
        int bankruptCount = 0;
        Player winner = null;

        for (Player player : allPlayers) {
            if (player.isBankrupt())
                bankruptCount++;
            else
                winner = player;
        }
        if (bankruptCount >= (allPlayers.size() - 1)) {
            endGame();
            if (winner != null) {
                DialogForm.showOnlyText("Koniec gry!\nWygrywa gracz: " + winner.getName());
            } else {
                DialogForm.showOnlyText("Koniec gry!\nNiestety nikt nie wygrywa.\n");
            }
            return true;
        }
        return false;
    }


    public void initPlayers() {
        int value = DialogForm.getValue("Witaj! Podaj ilość graczy:");
        this.setNumOfPlayers(value);

        for (int i = 0; i < numOfPlayers; i++) {
            Player player = new Player();
            String name = DialogForm.showAndGetString("Podaj nazwę gracza nr " + (i + 1));
            player.setName(name);
            player.setNumLocation(1);
            player.setActualProperty(this.gameBoard.get(0));
            allPlayers.add(player);
        }
    }

    public void mixListOfPlayer() {
        Collections.shuffle(allPlayers);
    }

    public void nextTurn() {
        currentPlayer = allPlayers.remove(0); //pobranie gracza z listy
    }

    public void endTurn() {
        allPlayers.add(currentPlayer); //dodanie gracza do listy
    }

    public void rollDice() {
        int throwFirst = (int) (Math.random() * 6) + 1;
        int throwSecond = (int) (Math.random() * 6) + 1;
        this.currentDiceValue = throwFirst + throwSecond;
        if (throwFirst != throwSecond) {
            DialogForm.showOnlyText("Rzut kośćmi! Wypadło łącznie: " + currentDiceValue);
        } else {
            DialogForm.showOnlyText("Rzut kośćmi! Dublet! Wypadło łącznie: " + currentDiceValue + "\nRzucasz jeszcze raz...");
            throwFirst = (int) (Math.random() * 6) + 1;
            throwSecond = (int) (Math.random() * 6) + 1;
            this.currentDiceValue += throwFirst + throwSecond;
            if (throwFirst != throwSecond) {
                DialogForm.showOnlyText("Rzut kośćmi! Razem z dubletem wypadło łącznie: " + currentDiceValue);
            } else {
                DialogForm.showOnlyText("Rzut kośćmi! Znów dublet! Powinieneś iść do więzienia, ale tym razem Ci odpuszczamy.");
            }
        }
    }

    public boolean movePlayer() {
        boolean value;
        if ((currentPlayer.getNumLocation() + this.currentDiceValue) > 40) {
            value = true;
        } else value = false;
        currentPlayer.setNumLocation(currentPlayer.getNumLocation() + this.currentDiceValue);
        currentPlayer.setActualProperty(gameBoard.get(currentPlayer.getNumLocation() - 1));

//        if(currentPlayer.getActualProperty().getClass().getName()=="Utility" && !((PaidProperty)currentPlayer.getActualProperty()).isAvailable()){
//            DialogForm.showOnlyText("TUTAJ CHYBA SIE KONCZY PROGRAM... Naciśnij, aby rzucić kostkami do obliczenia opłaty.");
//        }

        return value;
    }

    public String getCurrentPlayerInfo() {
        return currentPlayer.ToString();
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setCurrentDiceValue(int currentDiceValue) {
        if (currentDiceValue > 1 && currentDiceValue < 13)
            this.currentDiceValue = currentDiceValue;
    }

    public static ArrayList<Player> getAllPlayers() {
        return allPlayers;
    }

    public ArrayList<Property> getGameBoard() {
        return this.gameBoard;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public int getCurrentDiceValue() {
        return this.currentDiceValue;
    }

    public int getNumOfPlayers() {
        return this.numOfPlayers;
    }

    public ArrayList<Property> initializeGameBoard() {
        gameBoard.add(new Property(1, "Start"));
        gameBoard.add(new CityProperty(2, "Saloniki", Country.Grecja, 120, 100, new int[]{10, 40, 120, 360, 640, 900}));
        gameBoard.add(new Property(3, "Puste pole"));
        gameBoard.add(new CityProperty(4, "Ateny", Country.Grecja, 120, 100, new int[]{10, 40, 120, 360, 640, 900}));
        gameBoard.add(new DutyProperty(5, "Parking strzeżony", 400));
        gameBoard.add(new Railroad(6, "Linie kolejowe poludniowe"));
        gameBoard.add(new CityProperty(7, "Neapol", Country.Włochy, 200, 100, new int[]{15, 60, 180, 540, 800, 1100}));
        gameBoard.add(new Property(8, "Puste pole"));
        gameBoard.add(new CityProperty(9, "Mediolan", Country.Włochy, 200, 100, new int[]{15, 60, 180, 540, 800, 1100}));
        gameBoard.add(new CityProperty(10, "Rzym", Country.Włochy, 240, 100, new int[]{20, 80, 200, 600, 900, 1200}));
        gameBoard.add(new Property(11, "Puste pole"));
        gameBoard.add(new CityProperty(12, "Barcelona", Country.Hiszpania, 280, 200, new int[]{20, 100, 300, 900, 1250, 1500}));
        gameBoard.add(new Utility(13, "Elektrownia", 300, 150));
        gameBoard.add(new CityProperty(14, "Sewilla", Country.Hiszpania, 280, 200, new int[]{20, 100, 300, 900, 1250, 1500}));
        gameBoard.add(new CityProperty(15, "Madryt", Country.Hiszpania, 320, 200, new int[]{25, 120, 360, 1000, 1400, 1800}));
        gameBoard.add(new Railroad(16, "Linie kolejowe zachodnie"));
        gameBoard.add(new CityProperty(17, "Liverpool", Country.Wielka_Brytania, 360, 200, new int[]{30, 140, 400, 1100, 1500, 1900}));
        gameBoard.add(new Property(18, "Puste pole"));
        gameBoard.add(new CityProperty(19, "Glasgow", Country.Wielka_Brytania, 360, 200, new int[]{30, 140, 400, 1100, 1500, 1900}));
        gameBoard.add(new CityProperty(20, "Londyn", Country.Wielka_Brytania, 400, 200, new int[]{35, 160, 440, 1200, 1600, 2000}));
        gameBoard.add(new Property(21, "Parking bezplatny"));
        gameBoard.add(new CityProperty(22, "Rotterdam", Country.Beneluks, 440, 300, new int[]{35, 180, 500, 1400, 1750, 2100}));
        gameBoard.add(new Property(23, "Puste pole"));
        gameBoard.add(new CityProperty(24, "Bruksela", Country.Beneluks, 440, 300, new int[]{35, 180, 500, 1400, 1750, 2100}));
        gameBoard.add(new CityProperty(25, "Amsterdam", Country.Beneluks, 480, 300, new int[]{40, 200, 600, 1500, 1850, 2200}));
        gameBoard.add(new Railroad(26, "Linie kolejowe polnocne"));
        gameBoard.add(new CityProperty(27, "Malmo", Country.Szwecja, 520, 300, new int[]{45, 220, 660, 1600, 1950, 1950}));
        gameBoard.add(new CityProperty(28, "Goteborg", Country.Szwecja, 520, 300, new int[]{45, 220, 660, 1600, 1950, 2300}));
        gameBoard.add(new Utility(29, "Siec wodociagowa", 300, 150));
        gameBoard.add(new CityProperty(30, "Sztokholm", Country.Szwecja, 560, 300, new int[]{50, 240, 720, 1700, 2050, 2400}));
        gameBoard.add(new Property(31, "Puste pole"));
        gameBoard.add(new CityProperty(32, "Frankfurt", Country.RFN, 600, 400, new int[]{55, 260, 780, 1900, 2200, 2550}));
        gameBoard.add(new CityProperty(33, "Kolonia", Country.RFN, 600, 400, new int[]{55, 260, 780, 1900, 2200, 2550}));
        gameBoard.add(new Property(34, "Puste pole"));
        gameBoard.add(new CityProperty(35, "Bonn", Country.RFN, 640, 400, new int[]{60, 300, 900, 2000, 2400, 2800}));
        gameBoard.add(new Railroad(36, "Linie kolejowe wschodnie"));
        gameBoard.add(new Property(37, "Puste pole"));
        gameBoard.add(new CityProperty(38, "Insbruck", Country.Austria, 700, 400, new int[]{70, 350, 1000, 2200, 2600, 3000}));
        gameBoard.add(new DutyProperty(39, "Podatek od wzbogacenia", 200));
        gameBoard.add(new CityProperty(40, "Wiedeń", Country.Austria, 800, 400, new int[]{100, 400, 1200, 2800, 3400, 4000}));

        Property.setMapCityCountry();
        return gameBoard;
    }

    public void endGame() {
        int highMoney = 0;
        ArrayList<Player> rankPlayer = new ArrayList<>();
        int counter = 0;

        for(Player player : getAllPlayers()){
            for (Property property : player.getPropertiesOwned()) {
                if (property.getClass().getSimpleName().equals("Utility") || property.getClass().getSimpleName().equals("Railroad"))
                    player.addMoney(((PaidProperty) property).getPropertyPrice());
                if (property.getClass().getSimpleName().equals("CityProperty")) {
                    if (((CityProperty) property).isHotelOwned()) {
                        player.addMoney(((CityProperty) property).getHousePrice() * 5);
                    } else
                        player.addMoney(((CityProperty) property).getHousePrice() * (((CityProperty) property).getNumOfHouses()));
                }
            }
            System.out.println(getAllPlayers().size());
            if (highMoney == player.getMoney()) {
                counter++;
                rankPlayer.add(0, player);

            } else if (highMoney < player.getMoney()) {
                counter = 1;
                rankPlayer.add(0, player);
                highMoney = player.getMoney();
            } else rankPlayer.add(rankPlayer.size(), player);

        }
        String textToShow;
        textToShow = counter == 1 ? "Zwycięża:\n" : "Zwyciężają:\n";
        for (int i = 1; i <= counter; i++) {
            textToShow += rankPlayer.get(i - 1).getName() + "\n";
        }
        DialogForm.showOnlyText(textToShow);
        System.exit(0);
    }
}

