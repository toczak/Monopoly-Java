package sample.Model;

import sample.DialogForm;
import sample.Logic.GameProcess;
import sample.Model.Properties.CityProperty;
import sample.Model.Properties.PaidProperty;
import sample.Model.Properties.Property;

import java.util.ArrayList;
import java.util.Collections;

public class Player {

    private String name;
    private String counter; //pionek w grze
    private int money;
    private boolean bankrupt;
    private int numLocation;
    private Property actualProperty;
    private ArrayList<Property> propertiesOwned;
    private int railroadsOwned;
    private int utilitiesOwned;

    public Player(String name, String counter) {
        this.name = name;
        this.counter = counter;
        this.money = 3000;
        this.bankrupt = false;
        this.numLocation = 1;
        this.propertiesOwned = new ArrayList<Property>();
        this.railroadsOwned = 0;
        this.utilitiesOwned = 0;
    }

    public Player() {
        this("", "");
    }

    public void boughtProperty(Property property) {
        this.propertiesOwned.add(property);
        Collections.sort(propertiesOwned);
    }

    public void soldProperty(Property property) {
        this.propertiesOwned.remove(property);
        Collections.sort(propertiesOwned);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getNumLocation() {
        return numLocation;
    }

    public void setNumLocation(int numLocation) {
        this.numLocation = numLocation;
        if (this.numLocation > 40) {
            this.numLocation -= 40;
        }
    }

    public int getRailRoadsOwned() {
        return railroadsOwned;
    }

    public void setRailRoadsOwned(int railRoads) {
        if (railRoads >= 0 && railRoads < 5) {
            this.railroadsOwned = railRoads;
        }
    }

    public int getUtilitiesOwned() {
        return utilitiesOwned;
    }

    public void setUtilitiesOwned(int utilities) {
        if (utilities >= 0 && utilities < 3)
            this.utilitiesOwned = utilities;
    }

    public void incrementUtilities() {
        if (this.utilitiesOwned <= 1)
            this.utilitiesOwned++;
    }

    public void incrementRailRoads() {
        if (this.railroadsOwned <= 3)
            this.railroadsOwned++;
    }

    public void decrementUtilities() {
        if (this.utilitiesOwned > 0) {
            this.utilitiesOwned--;
        }
    }

    public void decrementRailRoads() {
        if (this.railroadsOwned > 0) {
            this.railroadsOwned--;
        }
    }

    public void takeMoney(int money) {
        if (money >= 0) {
            if (money > this.money) {
                this.money = 0;
                this.changeToBankrupt();
            } else {
                this.money -= money;
            }
        }
    }

    public void addMoney(int money) {
        if (money >= 0)
            this.money += money;
    }

    public int buyHousesFromBank(Bank bank) {
        int selection;
        boolean validInput = false;
        CityProperty chosenProperty = null;
        String textToDisplay = "Aktualny gracz: " + this.getName() + "\n\n";

        Collections.sort(propertiesOwned);
        do {
            textToDisplay += "Wybierz jedno z posiadanych pól, aby kupić na nim domy lub hotel:\n";
            for (int i = 0; i < propertiesOwned.size(); i++) {
                if (propertiesOwned.get(i).getClass().getSimpleName().equals("CityProperty"))
                    textToDisplay += ("\t" + i + ") " + propertiesOwned.get(i).getName() + "\n");
            }
            textToDisplay += ("\t" + propertiesOwned.size() + ") Wróć...\n");

            selection = DialogForm.getValue(textToDisplay);
            if (selection == propertiesOwned.size())
                return 0;
            else if (propertiesOwned.get(selection).getClass().getSimpleName().equals("CityProperty")) {
                chosenProperty = (CityProperty) propertiesOwned.get(selection);
                validInput = true;
            }
        } while (!validInput);

        validInput = false;

        textToDisplay = "Wybrałeś " + chosenProperty.getName() + ". Zawartość pola: \n Liczba domów: " + chosenProperty.getNumOfHouses() + " \n Liczba hoteli: " + ((chosenProperty.isHotelOwned()) ? 1 : 0);
        if (checkOtherCitiesInCountry(chosenProperty)) {
            if (!chosenProperty.isHotelOwned()) {
                textToDisplay += "\nMożesz kupić " + (chosenProperty.getNumOfHouses() == 4 ? "hotel" : "kolejny dom") + " za cenę " + chosenProperty.getHousePrice();
                while (!validInput) {
                    textToDisplay += ("\n Co zrobić? \n 0) Kup kolejny budynek\n 1) Wyjdź");
                    do {
                        selection = DialogForm.getValue(textToDisplay);
                        if (selection == 0) {
                            bank.sellTheHouse(this, chosenProperty);
                            return 1;
                        } else {
                            return 0;
                        }
                    } while (selection != 0 || selection != 1);
                }
            } else {
                DialogForm.showOnlyText("Kupiłeś już maksymalną ilość budynków na tym polu!");
            }
        } else {
            DialogForm.showOnlyText("To pole nie może mieć aktualnie więcej budynków!\nWybierz inne pole.");
        }
        return 0;
    }

    private boolean checkOtherCitiesInCountry(CityProperty cityProperty) {
        CityProperty propertyTemp = null;
        int lastValue = 0;
        int newValue = 0;
        boolean isFirst = true;
        for (Property property : propertiesOwned) {
            if (property.getClass().getSimpleName().equals("CityProperty")) {
                propertyTemp = (CityProperty) property;

                if (propertyTemp.getCountry() == cityProperty.getCountry()) {
                    if (propertyTemp != cityProperty) {
//                        if (isFirst) {
//                            lastValue = propertyTemp.getNumOfHouses();
//                        newValue = lastValue;
//                            isFirst = false;
//                        }
                        if (propertyTemp.isHotelOwned())
                            lastValue = 5;
                        else
                            lastValue = propertyTemp.getNumOfHouses();
                        if (cityProperty.getNumOfHouses() - lastValue > 0) return false;
//                        lastValue = newValue;
                    }
                }
            }
        }
        return true;
    }

    private void changeToBankrupt() {
        this.bankrupt = true;
    }

    public void setActualProperty(Property actualProperty) {
        this.actualProperty = actualProperty;
    }

    public Property getActualProperty() {
        return actualProperty;
    }

    public ArrayList<Property> getPropertiesOwned() {
        return propertiesOwned;
    }

    public boolean isBankrupt() {
        return bankrupt;
    }

    private String propertiesOwnedToString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (getPropertiesOwned().size() == 0) {
            stringBuilder.append("brak");
        } else {
            Collections.sort(propertiesOwned);
            for (Property property : propertiesOwned) {
                stringBuilder.append("  - " + "Pole " + property.numberOfField + " - " + property.getName() + "\n");
            }
        }
        return stringBuilder.toString();
    }

    public String ToString() {
        return String.format("> Gracz: %s < \n[Pole]: %d - %s\n[Pieniądze]: %d\n[Zakupione pola]: \n%s", getName(), numLocation, actualProperty.getName(), money, propertiesOwnedToString());
    }

    public void showMenuSellTheProperty(ArrayList<Player> allPlayers, Bank bank) {

        int selection;
        boolean validInput = false;
        PaidProperty chosenProperty = null;
        Player chosenPlayer = null;
        String textToDisplay = "Aktualny gracz: " + this.getName() + "\n\n";

        do {
            textToDisplay += "Wybierz jedno z posiadanych pól, aby je sprzedać innemu graczowi:\n";
            for (int i = 0; i < propertiesOwned.size(); i++) {
                textToDisplay += ("\t" + i + ") " + propertiesOwned.get(i).getName() + "\n");
            }
            textToDisplay += ("\t" + propertiesOwned.size() + ") Wróć...\n");

            selection = DialogForm.getValueMinMax(textToDisplay, 0, propertiesOwned.size() + 1);

            if (selection == propertiesOwned.size())
                return;
            else {
                chosenProperty = (PaidProperty) propertiesOwned.get(selection);
                validInput = true;
            }
        } while (!validInput);

        if (chosenProperty.getClass().getSimpleName().equals("CityProperty")) {
            CityProperty cityProperty = (CityProperty) chosenProperty;
            if (cityProperty.isHotelOwned() || cityProperty.getNumOfHouses() > 0) {
                DialogForm.showOnlyText("Nie możesz sprzedać pola na którym masz kupione budynki!\nSprzedaj je, aby sprzedać to pole.");
                return;
            }
        }

        validInput = false;
        int priceSellProperty = 0;
        do {
            priceSellProperty = DialogForm.getValue("Podaj za jaką cenę chcesz sprzedać pole:");
        } while (priceSellProperty <= 0);
        do {
            textToDisplay = "Wybierz gracza, któremu chcesz sprzedać pole:\n";
            for (int i = 0; i < allPlayers.size(); i++) {
                if (allPlayers.get(i).getName() != this.getName())
                    textToDisplay += ("\t" + i + ") " + allPlayers.get(i).getName() + "\n");
            }

            textToDisplay += ("\t" + allPlayers.size() + ") Wróć...\n");
            selection = DialogForm.getValueMinMax(textToDisplay, 0, allPlayers.size() + 1);
            if (selection == allPlayers.size())
                return;
            else {
                chosenPlayer = allPlayers.get(selection);
                if (chosenPlayer.getMoney() < priceSellProperty) {
                    DialogForm.showOnlyText("Wybrany gracz ma za mało pieniędzy!\nWybierz innego gracza.");
                } else {
                    validInput = true;
                }
            }
        } while (!validInput);

        textToDisplay = chosenPlayer.getName() + "! Czy zgadzasz się na kupno pola: " + chosenProperty.getName() + " za cenę: " + priceSellProperty + "?\n";
        textToDisplay += "\t0) Tak\n \t1) Nie";
        int getAnswerFromPlayer = DialogForm.getValueMinMax(textToDisplay, 0, 2);
        if (getAnswerFromPlayer == 1) {
            return;
        } else {
            soldProperty(chosenProperty);
            bank.changeOwnerTheProperty(this, chosenPlayer, chosenProperty, priceSellProperty);
            if (chosenProperty.getClass().getSimpleName().equals("CityProperty")) {
                if (chosenProperty.checkPropertiesInCountryAndSet(chosenPlayer)) {
                    chosenProperty.checkIsDoublePaymentProperty(chosenPlayer);
                }
            }
        }
    }

    public void pawnTheProperty(Bank bank) {

        int selection;
        boolean validInput = false;
        PaidProperty chosenProperty = null;
        String textToDisplay = "Aktualny gracz: " + this.getName() + "\n\n";

        do {
            textToDisplay += "Wybierz jedno z posiadanych pól, aby je zastawić:\n";
            for (int i = 0; i < propertiesOwned.size(); i++) {
                textToDisplay += ("\t" + i + ") " + propertiesOwned.get(i).getName() + "\n");
            }
            textToDisplay += ("\t" + propertiesOwned.size() + ") Wróć...\n");

            selection = DialogForm.getValueMinMax(textToDisplay, 0, propertiesOwned.size() + 1);
            if (selection == propertiesOwned.size())
                return;
            else {
                chosenProperty = (PaidProperty) propertiesOwned.get(selection);
                validInput = bank.checkPropertyBeforePawn(chosenProperty);
            }
        } while (!validInput);
        bank.pawnTheProperty(this, chosenProperty);
    }

    public void showMenuSellTheHouses() {
        int selection;
        boolean validInput = false;
        CityProperty chosenProperty = null;
        String textToDisplay = "Aktualny gracz: " + this.getName() + "\n\n";

        do {
            textToDisplay += "Wybierz jedno z posiadanych pól, aby sprzedać na nim budynki:\n";
            for (int i = 0; i < propertiesOwned.size(); i++) {
                if (((CityProperty) propertiesOwned.get(i)).getNumOfHouses() > 0 || ((CityProperty) propertiesOwned.get(i)).isHotelOwned())
                    textToDisplay += ("\t" + i + ") " + propertiesOwned.get(i).getName() + "\n");
            }
            textToDisplay += ("\t" + propertiesOwned.size() + ") Wróć...\n");

            selection = DialogForm.getValueMinMax(textToDisplay, 0, propertiesOwned.size() + 1);
            if (selection == propertiesOwned.size())
                return;
            else {
                chosenProperty = (CityProperty) propertiesOwned.get(selection);
                ArrayList<String> list = new ArrayList<>();
                if (chosenProperty.isHotelOwned()) list.add("Sprzedaj hotel");
                if (chosenProperty.getNumOfHouses() > 0) list.add("Sprzedaj dom");
                list.add("Sprzedaj wszystkie budynki");
                list.add("Opuść menu");
                String[] options = list.toArray(new String[list.size()]);
                boolean ifEndSell = false;
                while (!ifEndSell) {
                    selection = menuSellHouse(options, chosenProperty);

                    switch (options[selection]) {
                        case "Sprzedaj hotel":
                            chosenProperty.sellTheHotel(this);
                            break;
                        case "Sprzedaj dom":
                            chosenProperty.sellTheHouse(this);
                            break;
                        case "Sprzedaj wszystkie budynki":
                            chosenProperty.sellAllBuildings(this);
                            break;
                        case "Opuść menu":
                            ifEndSell = true;
                            break;
                    }
                    return;
                }
            }
        }
        while (!validInput);
    }

    private int menuSellHouse(String[] arrayOptions, CityProperty chosenProperty) {
        String textToDisplay = "Aktualny gracz: " + this.getName() + "\n\n";

         textToDisplay += "Wybrałeś " + chosenProperty.getName() + ". Zawartość pola: \n Liczba domów: " + chosenProperty.getNumOfHouses() + " \n Liczba hoteli: " + ((chosenProperty.isHotelOwned()) ? 1 : 0) + "\n";

        for (int i = 0; i < arrayOptions.length; i++) {
            textToDisplay += (i + ") " + arrayOptions[i] + "\n");
        }

        return DialogForm.getValueMinMax(textToDisplay, 0, arrayOptions.length);
    }

    public void redeemTheProperty(Bank bank) {

        int selection;
        boolean validInput = false;
        PaidProperty chosenProperty = null;
        String textToDisplay = "Aktualny gracz: " + this.getName() + "\n\n";

        do {
            textToDisplay += "Wybierz jedno z zastawionych pól, aby je spłacić:\n";
            for (int i = 0; i < propertiesOwned.size(); i++) {
                if (((PaidProperty) propertiesOwned.get(i)).isPawned())
                    textToDisplay += ("\t" + i + ") " + propertiesOwned.get(i).getName() + "\n");
            }
            textToDisplay += ("\t" + propertiesOwned.size() + ") Wróć...\n");

            selection = DialogForm.getValueMinMax(textToDisplay, 0, propertiesOwned.size() + 1);

            if (selection == propertiesOwned.size())
                return;
            else {
                chosenProperty = (PaidProperty) propertiesOwned.get(selection);
                validInput = bank.returnPawnedProperty(this, chosenProperty);
            }
        } while (!validInput);
    }

    public void seeCardOfField() {
        int selection;
        boolean validInput = false;
        PaidProperty chosenProperty = null;
        String textToDisplay = "Aktualny gracz: " + this.getName() + "\n\n";

        do {
            textToDisplay += "Wybierz jedno z pól, aby zobaczyć kartę:\n";
            for (int i = 0; i < propertiesOwned.size(); i++) {
                textToDisplay += ("\t" + i + ") " + propertiesOwned.get(i).getName() + "\n");
            }
            textToDisplay += ("\t" + propertiesOwned.size() + ") Wróć...\n");

            selection = DialogForm.getValueMinMax(textToDisplay, 0, propertiesOwned.size() + 1);

            if (selection == propertiesOwned.size())
                return;
            else {
                chosenProperty = (PaidProperty) propertiesOwned.get(selection);
                chosenProperty.showPropertyCard();
                validInput = true;
            }
        } while (!validInput);
    }

    private int showMenuToSell(String[] arrayOptions) {
        String textToShow = "Aktualny gracz: " + this.getName() + "\n\n";

        textToShow += "Wybierz co chcesz zrobić:\n";
        for (int i = 0; i < arrayOptions.length; i++) {
            textToShow += (i + ") " + arrayOptions[i] + "\n");
        }
        return DialogForm.getValueMinMax(textToShow, 0, arrayOptions.length + 1);
    }

    private String[] getMenuToSell(ArrayList<Property> properties) {
        boolean isValue = false;
        ArrayList<String> list = new ArrayList<>();

        if (properties.size() > 0) {
            list.add("Sprzedaj pole");
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
        list.add("Oglos sie bankrutem");
        return list.toArray(new String[list.size()]);
    }


    public void showMenuToSell(Player player, Bank bank) {
        String[] options = getMenuToSell(propertiesOwned);
        int selection = this.showMenuToSell(options);

        switch (options[selection]) {
            case "Sprzedaj pole":
//                this.showMenuSellTheProperty(player, );
                this.showMenuSellTheProperty(GameProcess.getAllPlayers(), bank);
                break;
            case "Sprzedaj budynki na polu":
                this.showMenuSellTheHouses();
                break;
            case "Zastaw pole":
                this.pawnTheProperty(bank);
                break;
            case "Oglos sie bankrutem":
                this.changeToBankrupt();
                this.transferPropertiesToPlayer(this, player);
                break;
        }
    }

    public void showMenuToSell(Bank bank) {
        String[] options = getMenuToSell(propertiesOwned);
        int selection = this.showMenuToSell(options);

        switch (options[selection]) {
            case "Sprzedaj pole":
//                this.showMenuSellTheProperty(player, );
                this.showMenuSellTheProperty(GameProcess.getAllPlayers(), bank);
                break;
            case "Sprzedaj budynki na polu":
                this.showMenuSellTheHouses();
                break;
            case "Zastaw pole":
                this.pawnTheProperty(bank);
                break;
            case "Oglos sie bankrutem":
                this.changeToBankrupt();
                break;
        }
    }

    private void transferPropertiesToPlayer(Player owner, Player player) {
        for (Property property : owner.propertiesOwned) {
            PaidProperty paidProperty = (PaidProperty) property;
            paidProperty.setOwner(player);
            owner.soldProperty(property);
            player.boughtProperty(property);
        }
    }

    public void buyPropertyFromBank(Bank bank, PaidProperty actualProperty) {
        bank.sellTheProperty(this, actualProperty);

    }

    public void demandMoneyFromPlayer(Player player, int charge, Bank bank) {
        if (charge > player.getMoney()) {
            DialogForm.showOnlyText("Nie masz na tyle pieniędzy: " + charge + ", aby opłacić " + this.getName() + ".\nMusisz sprzedać lub zastawić swoje pola.");
            do {
                player.showMenuToSell(this, bank);
            } while (charge > player.getMoney() || player.isBankrupt());
            if (player.isBankrupt()) {
                DialogForm.showOnlyText("Niestety, to koniec gry dla Ciebie!");
            } else DialogForm.showOnlyText("Udało ci się spłacić dług. Możesz grać dalej!");

        } else {
            player.takeMoney(charge);
            this.addMoney(charge);

            DialogForm.showOnlyText(player.getName() + "! Niestety, jesteś zmuszony \nzapłacić: " + charge + " dla gracza: " + this.getName());
        }
    }
}
