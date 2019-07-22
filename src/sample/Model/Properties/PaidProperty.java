package sample.Model.Properties;

import sample.DialogForm;
import sample.Enum.Country;
import sample.Model.Bank;
import sample.Model.Player;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class PaidProperty extends Property {

    private int propertyPrice;
    private int mortgage;
    protected Player owner;
    protected boolean available;
    protected boolean isPawned;

    public abstract String[] getPossibleActions(Player player);

    public abstract void showPropertyCard();

    //    protected abstract void buyHouses(Player player);
    protected abstract String calculateCharge(Player player, Bank bank);

    public PaidProperty(int numberOfField, String name, int propertyPrice) {
        super(numberOfField, name);
        this.propertyPrice = propertyPrice;
        this.mortgage = propertyPrice / 2;
        this.available = true;
        owner = null;
        isPawned = false;
    }


    public PaidProperty(int numberOfField, String name, int propertyPrice, int mortgage) {
        this(numberOfField, name, propertyPrice);
        this.mortgage = mortgage;
    }


    protected ArrayList<String> addPropertyGenericAvailableActions(ArrayList<String> actions, Player player) {

        if (player == this.owner) actions.add("Zobacz karte");
        if (player != this.owner && this.owner == null) {
            actions.add("Zobacz karte");
            actions.add("Kup pole");
        }
//        actions.add("Przenies");

        return this.addGenericAvailableActions(actions, player);

    }

    public String[] getRequiredActions(Player player, Bank bank) {
        ArrayList<String> actions = new ArrayList<>();

        if (player != this.owner && !this.available && !this.isPawned())
            actions.add(this.calculateCharge(player, bank));

        return actions.toArray(new String[actions.size()]);
    }

    public boolean isPawned() {
        return isPawned;
    }

    public void setPawned(boolean pawned) {
        isPawned = pawned;
    }

/*
    public String respondToAction(Player player, String action) {
*/
/*        switch (action) {
            case "Zobacz karte":
                this.showPropertyCard();
                break;

            case "Kup pole":
//                this.buyProperty(player);
//                player.buyProperty(this, mapCityCountry);
                break;
        }
        return "Udało się!";*//*

    }
*/

    public boolean checkPropertiesInCountryAndSet(Player player) {
        int counter = 0;
        CityProperty cityProperty = (CityProperty) this;
        for (int i = 0; i < Country.values().length; i++) {
            counter = 0;
            for (Property propertyOwned : player.getPropertiesOwned()) {
                for (int j = 0; j < mapCityCountry.get(Country.getNameByValue(i)).length; j++)
                    if (propertyOwned.numberOfField == mapCityCountry.get(Country.getNameByValue(i))[j]) {
                        counter++;
                    }
            }
            if (mapCityCountry.get(Country.getNameByValue(i)).length == counter) {
                cityProperty.setOwnerHaveAllPropertyInCountry(true);
                return true;
            } else {
                cityProperty.setOwnerHaveAllPropertyInCountry(false);
                return false;
            }

        }
        return false;
    }

    public boolean checkIsDoublePaymentProperty(Player player) {
        boolean checker = true;
        CityProperty cityProperty = null;
        for (int i = 0; i < Country.values().length; i++) {
            for (Property propertyOwned : player.getPropertiesOwned()) {
                for (int j = 0; j < mapCityCountry.get(Country.getNameByValue(i)).length; j++)
                    if (propertyOwned.numberOfField == mapCityCountry.get(Country.getNameByValue(i))[j]) {
                        cityProperty = (CityProperty) propertyOwned;
                        cityProperty.setDoublePayment(false);
                        if (cityProperty.isHotelOwned() || cityProperty.getNumOfHouses() > 0) checker = false;
                    }
            }
        }
        if (!checker) return false;
        for (int i = 0; i < Country.values().length; i++) {
            for (Property propertyOwned : player.getPropertiesOwned()) {
                for (int j = 0; j < mapCityCountry.get(Country.getNameByValue(i)).length; j++)
                    if (propertyOwned.numberOfField == mapCityCountry.get(Country.getNameByValue(i))[j]) {
                        cityProperty = (CityProperty) propertyOwned;
                        cityProperty.setDoublePayment(true);
                    }
            }
        }
        return true;
    }


    public void releaseProperty() {
        this.owner = null;
        this.available = true;
    }

    public int getPropertyPrice() {
        return propertyPrice;
    }

    public void setPropertyPrice(int propertyPrice) {
        if (propertyPrice > 0)
            this.propertyPrice = propertyPrice;
    }

    public int getMortgage() {
        return mortgage;
    }

    public void setMortgage(int mortgage) {
        this.mortgage = mortgage;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
