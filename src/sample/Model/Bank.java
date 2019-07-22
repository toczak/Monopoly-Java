package sample.Model;

import sample.DialogForm;
import sample.Model.Properties.CityProperty;
import sample.Model.Properties.PaidProperty;

public class Bank {

    public void giveMoneyAfterStart(Player player){
        player.addMoney(400);
        DialogForm.showOnlyText(player.getName() + " - przechodzisz przez START. Otrzymujesz 400!\nTwój aktualny stan konta: "+player.getMoney());
    }

    public void sellTheHouse(Player player, CityProperty chosenProperty){
        int housesLeft = 4 - chosenProperty.getNumOfHouses();
        int purchaseCost = chosenProperty.getHousePrice();
        String textToShow = "";
        if (player.getMoney() >= purchaseCost) {
            if (housesLeft > 0) {
                player.takeMoney(purchaseCost);
                chosenProperty.setNumOfHouses(chosenProperty.getNumOfHouses() + 1);
                chosenProperty.setOwner(player);

                textToShow = "Gratulacje! Kupiłeś dom na polu: " + chosenProperty.getName();

            } else {
                player.takeMoney(purchaseCost);
                chosenProperty.setNumOfHouses(0);
                chosenProperty.setHotelOwned(true);

                textToShow = "Gratulacje! Kupiłeś hotel na polu: " + chosenProperty.getName();
            }
            DialogForm.showOnlyText(textToShow);
        } else {
            DialogForm.showOnlyText("Niestety, masz za mało pieniędzy!");
        }
    }

    public void sellTheProperty(Player player, PaidProperty property){

        if (property.getOwner() == null && property.isAvailable() && player.getMoney() > property.getPropertyPrice()) {

            demandMoneyFromPlayer(player, property.getPropertyPrice());
            property.setOwner(player);
            property.setAvailable(false);
            player.boughtProperty(property);
            if (property.getClass().getSimpleName().equals("CityProperty")) {
                if (property.checkPropertiesInCountryAndSet(player)) {
                    property.checkIsDoublePaymentProperty(player);
                }
            }
            if (property.getClass().getSimpleName().equals("Utility")) player.incrementUtilities();
            if (property.getClass().getSimpleName().equals("Railroad")) player.incrementRailRoads();
            DialogForm.showOnlyText("Gratulacje! Kupiłeś: " + property.getName() + "\nTwoje saldo po transakcji: " + player.getMoney());
        } else
            DialogForm.showOnlyText("Niestety, nie możesz kupić tego pola!");

    }

    public void changeOwnerTheProperty(Player owner, Player buyer, PaidProperty chosenProperty, int priceSellProperty){

        transferMoneyToPlayer(owner, priceSellProperty);
        demandMoneyFromPlayer(buyer, priceSellProperty);
        buyer.boughtProperty(chosenProperty);
        if (chosenProperty.getClass().getSimpleName().equals("Utility")) {
            owner.decrementUtilities();
            buyer.incrementUtilities();
        }
        if (chosenProperty.getClass().getSimpleName().equals("Railroad")) {
            owner.decrementRailRoads();
            buyer.incrementRailRoads();
        }

        if (chosenProperty.getClass().getSimpleName().equals("CityProperty")) {
            if (chosenProperty.checkPropertiesInCountryAndSet(buyer)) {
                chosenProperty.checkIsDoublePaymentProperty(buyer);
            }
        }
    }

    private void transferMoneyToPlayer(Player player, int money){
        player.addMoney(money);
    }

    private void demandMoneyFromPlayer(Player player, int money){
        player.takeMoney(money);
    }

    public boolean checkPropertyBeforePawn(PaidProperty property){

        if (property.isPawned()) {
            DialogForm.showOnlyText("Wybrane pole już jest zastawione! Wybierz inne.");
            return false;
        } else {
            if (property.getClass().getSimpleName().equals("CityProperty")) {
                CityProperty cityProperty = (CityProperty) property;
                if (cityProperty.isHotelOwned() || cityProperty.getNumOfHouses() > 0) {
                    DialogForm.showOnlyText("Nie możesz zastawić pola na którym masz kupione budynki!\nSprzedaj je, aby zastawić to pole.");
                    return false;
                } else return true;
            } else return true;
        }
    }

    public void pawnTheProperty(Player player, PaidProperty chosenProperty){
        chosenProperty.setPawned(true);
        transferMoneyToPlayer(player, chosenProperty.getMortgage());
        DialogForm.showOnlyText("Zastawiłeś pole: " + chosenProperty.getName());
    }

    public boolean returnPawnedProperty(Player player, PaidProperty chosenProperty){

        if (!chosenProperty.isPawned()) {
            DialogForm.showOnlyText("Wybrane pole nie jest zastawione! Wybierz inne.");
        } else {
            int cost = chosenProperty.getMortgage() + (chosenProperty.getMortgage() * 10 / 100);
            if (cost > getPlayerMoney(player)) {
                DialogForm.showOnlyText("Nie masz na tyle pieniędzy, aby spłacić pole!");
                return false;
            } else {
                chosenProperty.setPawned(false);
                transferMoneyToPlayer(player,cost);
                DialogForm.showOnlyText("Spłaciłeś zastawione pole: " + chosenProperty.getName());
                return true;
            }
        }
        return true;
    }

    private int getPlayerMoney(Player player){
        return player.getMoney();
    }

    public void showCardFieldToPlayer(PaidProperty property){
        property.showPropertyCard();
    }
}
