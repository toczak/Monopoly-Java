package sample;

import sample.Logic.GameProcess;
import sample.Model.Bank;
import sample.Model.Player;
import sample.Model.Properties.CityProperty;
import sample.Model.Properties.PaidProperty;
import sample.Model.Properties.Property;

import java.util.ArrayList;

public class Game {

    private GameProcess gameProcess;


    public Game() {
        gameProcess = new GameProcess();
    }

    public void setupGame() {
        gameProcess.initializeGameBoard();
        gameProcess.initPlayers();
        gameProcess.mixListOfPlayer();
    }


    public void playGame() {
        String[] options;
        int selection;
        boolean ifEndTurn = false;
        Bank bank = new Bank();

        while (!gameProcess.isGameOver()) {
            gameProcess.nextTurn();

            Player actualPlayer = gameProcess.getCurrentPlayer();
            DialogForm.showOnlyText("Ruch gracza " + actualPlayer.getName() + "!");
            DialogForm.showOnlyText(gameProcess.getCurrentPlayerInfo());

            if (actualPlayer.getPropertiesOwned().size() != 0) {
                ifEndTurn = false;
                int counter = 0;
                while (!ifEndTurn) {
                    options = getMenuBeforeRound(actualPlayer.getPropertiesOwned());
                    selection = this.showMenuBeforeRound(actualPlayer, options);

                    switch (options[selection]) {
                        case "Kup dom lub hotel na jednym z pól":
                            if (counter < 3) {
                                if (actualPlayer.buyHousesFromBank(bank) == 1)
                                    counter++;
                            } else DialogForm.showOnlyText("Kupiłeś już 3 budynki podczas rundy!");
                            break;
                        case "Sprzedaj pole":
                            actualPlayer.showMenuSellTheProperty(GameProcess.getAllPlayers(), bank);
                            break;
                        case "Sprzedaj budynki na polu":
                            actualPlayer.showMenuSellTheHouses();
                            break;
                        case "Zastaw pole":
                            actualPlayer.pawnTheProperty(bank);
                            break;
                        case "Spłać zastaw pola":
                            actualPlayer.redeemTheProperty(bank);
                            break;
                        case "Wyświetl karte pola":
                            actualPlayer.seeCardOfField();
                            break;
                        case "Rzucaj kośćmi":
                            ifEndTurn = true;
                            break;
                    }
                }
            }
            DialogForm.showOnlyText("Następuje rzut kośćmi!");
            gameProcess.rollDice();
            if (gameProcess.movePlayer()) { //jeśli przeszedł przez start - daj pieniadze
                bank.giveMoneyAfterStart(actualPlayer);
            }
            Property actualProperty = actualPlayer.getActualProperty();
            if (actualProperty.getClass().getSimpleName().equals("CityProperty") || actualProperty.getClass().getSimpleName().equals("Utility") ||
                    actualProperty.getClass().getSimpleName().equals("Railroad")) {
                if (((PaidProperty) actualProperty).getOwner() != actualPlayer && !((PaidProperty) actualProperty).isAvailable()) {
                    DialogForm.showOnlyText(actualPlayer.getName() + "! Znalazłeś się w: " + actualProperty.getName() +
                            "\nTo pole należy do: " + ((PaidProperty) actualProperty).getOwner().getName());
                    actualProperty.getRequiredActions(actualPlayer, bank);
                    if (((PaidProperty) actualProperty).isPawned()) {
                        DialogForm.showOnlyText("Pole jest zastawione - nic nie musisz płacić!");
                    }
                }
            }
            ifEndTurn = false;
            while (!ifEndTurn) {
                //skomentowane tylko po to, aby moc przenosic pionki

//                if (actualProperty.getClass().getSimpleName().equals("CityProperty") || actualProperty.getClass().getSimpleName().equals("Utility") ||
//                        actualProperty.getClass().getSimpleName().equals("Railroad")) {
//                    if (((PaidProperty) actualProperty).getOwner()!=actualPlayer && !((PaidProperty) actualProperty).isAvailable()) {
//                        DialogForm.showOnlyText(actualPlayer.getName() + "! Znalazłeś się w: "+ actualProperty.getName() +
//                                "\nTo pole należy do: " + ((PaidProperty) actualProperty).getOwner().getName());
//                        actualProperty.getRequiredActions(actualPlayer,bank);
//                        if(((PaidProperty) actualProperty).isPawned()){
//                            DialogForm.showOnlyText("Pole jest zastawione - nic nie musisz płacić!");
//                        }
//                    }
//                }
                options = printAvailable(actualProperty.getPossibleActions(actualPlayer));
                selection = showPropertyMenu(actualPlayer, options, actualProperty);

                switch (options[selection]) {
                    case "Zobacz karte":
                        bank.showCardFieldToPlayer((PaidProperty) actualProperty);
                        break;
                    case "Kup pole":
                        actualPlayer.buyPropertyFromBank(bank, (PaidProperty) actualProperty);
                        break;
                    case "Koniec rundy":
                        ifEndTurn = true;
                        gameProcess.endTurn();
                        break;
//                    case "Przenies":
//                        actualPlayer.setNumLocation(DialogForm.getValue("Gdzie przeniesc?"));
//                        actualPlayer.setActualProperty(gameProcess.getGameBoard().get(actualPlayer.getNumLocation()-1));
//                        actualProperty = actualPlayer.getActualProperty();
//                        break;
                    case "Koniec gry":
                        gameProcess.endTurn();
                        gameProcess.endGame();
                        break;
                }
            }
        }
    }

    private String[] getMenuBeforeRound(ArrayList<Property> properties) {
        boolean isValue = false;
        ArrayList<String> list = new ArrayList<>();

        for (Property property : properties) {
            if (property.getClass().getSimpleName().equals("CityProperty")) {
                CityProperty cityProperty = (CityProperty) property;
                if (cityProperty.isOwnerHaveAllPropertyInCountry()) {
                    list.add("Kup dom lub hotel na jednym z pól");
                    break;
                }
            }
        }
        if (properties.size() > 0) {
            list.add("Sprzedaj pole");
            list.add("Wyświetl karte pola");
        }
        for (Property property : properties) {
            if (property.getClass().getSimpleName().equals("CityProperty")) {
                CityProperty cityProperty = (CityProperty) property;
                if (cityProperty.isHotelOwned() || cityProperty.getNumOfHouses() > 0) {
                    list.add("Sprzedaj budynki na polu");
                    break;
                }
            }
        }
        for (Property property : properties) {
            PaidProperty paidProperty = (PaidProperty) property;
            if (!paidProperty.isPawned()) {
                list.add("Zastaw pole");
                break;
            }
        }
        for (Property property : properties) {
            PaidProperty paidProperty = (PaidProperty) property;
            if (paidProperty.isPawned()) {
                list.add("Spłać zastaw pola");
                break;
            }
        }
        list.add("Rzucaj kośćmi");

        return list.toArray(new String[list.size()]);
    }

    private int showMenuBeforeRound(Player player, String[] arrayOptions) {
        String textToShow = "Aktualny gracz: " + player.getName() + "\n\n";
        textToShow += "Wybierz co chcesz zrobić:\n";
        for (int i = 0; i < arrayOptions.length; i++) {
            textToShow += (i + ") " + arrayOptions[i] + "\n");
        }
        return DialogForm.getValueMinMax(textToShow, 0, arrayOptions.length + 1);
    }

    private int showPropertyMenu(Player player, String[] arrayOptions, Property property) {
        String textToShow = "Aktualny gracz: " + player.getName() + "\n\n";
        textToShow += "[Pole " + property.numberOfField + " - " + property.getName() + "]\n\nWybierz co chcesz zrobić: \n";
        for (int i = 0; i < arrayOptions.length; i++) {
            textToShow += (i + ") " + arrayOptions[i] + "\n");
        }
        return DialogForm.getValueMinMax(textToShow, 0, arrayOptions.length + 1);
    }


    private String[] printAvailable(String[] actions) {

        String textToShow = "Wybierz co chcesz zrobić:";

        for (int i = 0; i < actions.length; i++) {
            textToShow += (i + ") " + actions[i] + "\n");
        }

        return actions;

    }

}
