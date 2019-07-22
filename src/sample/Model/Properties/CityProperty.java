package sample.Model.Properties;

import sample.DialogForm;
import sample.Enum.Country;
import sample.Model.Bank;
import sample.Model.Player;

import java.util.ArrayList;

public class CityProperty extends PaidProperty {

    private Country country;
    private int housePrice;
    private int[] chargesArray = new int[6];
    private boolean hotelOwned;
    private int numOfHouses;
    protected boolean ownerHaveAllPropertyInCountry;
    private boolean isDoublePayment;


    public CityProperty(int numberOfField, String name, Country country, int propertyPrice, int housePrice, int[] chargesArray) {
        super(numberOfField, name, propertyPrice);
        this.country = country;
        this.housePrice = housePrice;
        this.chargesArray = chargesArray;
        ownerHaveAllPropertyInCountry = false;
        isDoublePayment = false;
    }

    @Override
    public String getName() {
        return (super.getName() + " (" + getCountry() + ")");
    }

    public boolean isOwnerHaveAllPropertyInCountry() {
        return ownerHaveAllPropertyInCountry;
    }

    public void setOwnerHaveAllPropertyInCountry(boolean ownerHaveAllPropertyInCountry) {
        this.ownerHaveAllPropertyInCountry = ownerHaveAllPropertyInCountry;
    }

    public int getHousePrice() {
        return this.housePrice;
    }

    public void setHotelOwned(boolean hotelOwned) {
        this.hotelOwned = hotelOwned;
    }

    public boolean isHotelOwned() {
        return hotelOwned;
    }

    public int getNumOfHouses() {
        return numOfHouses;
    }

    public boolean isDoublePayment() {
        return isDoublePayment;
    }

    public void setDoublePayment(boolean doublePayment) {
        isDoublePayment = doublePayment;
    }

    public void setNumOfHouses(int numOfHouses) {
        if (numOfHouses >= 0 && numOfHouses < 5)
            this.numOfHouses = numOfHouses;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public String[] getPossibleActions(Player player) {
        ArrayList<String> actions = new ArrayList<>();

        return this.addPropertyGenericAvailableActions(actions, player).toArray(new String[actions.size()]);
    }

    @Override
    public void showPropertyCard() {
        String textToShow = "";
        textToShow += ("*******************************\n");
        textToShow += (" Pole: [" + getName() + "]\n");
        textToShow += (" Cena zakupu: " + getPropertyPrice() + "\n");
        textToShow += (" Oplata za postoj:\n");
        textToShow += ("  -teren niezabudowany - " + chargesArray[0] + "\n");
        textToShow += ("  -teren z 1 domem - " + chargesArray[1] + "\n");
        textToShow += ("  -teren z 2 domami - " + chargesArray[2] + "\n");
        textToShow += ("  -teren z 3 domami - " + chargesArray[3] + "\n");
        textToShow += ("  -teren z 4 domami - " + chargesArray[4] + "\n");
        textToShow += ("  -teren z 1 hotelem - " + chargesArray[5] + "\n");
        textToShow += (" 1 dom kosztuje: " + getPropertyPrice() + "\n");
        textToShow += (" 1 hotel kosztuje: 4 domy + " + getPropertyPrice() + "\n");
        textToShow += (" Zastaw hipoteczny: " + getMortgage() + "\n");
        textToShow += ("******************************\n");

        DialogForm.showOnlyText(textToShow);
    }

    @Override
    public String[] getRequiredActions(Player player, Bank bank) {
        ArrayList<String> actions = new ArrayList<>();

        if (player != this.owner && !this.available && !this.isPawned())
            actions.add(this.calculateCharge(player, bank));

        return actions.toArray(new String[actions.size()]);
    }


    protected String calculateCharge(Player player, Bank bank) {

        int charge;
        if (this.hotelOwned) {
            charge = chargesArray[5];
        } else {
            charge = chargesArray[this.numOfHouses];
        }
        if (isDoublePayment()) {
            charge *= 2;
        }

        owner.demandMoneyFromPlayer(player, charge, bank);
        return "";
    }

    public void sellTheHotel(Player player) {
        this.setHotelOwned(false);
        this.setNumOfHouses(4);
        player.addMoney(this.getPropertyPrice());
    }

    public void sellTheHouse(Player player) {
        this.setNumOfHouses(getNumOfHouses() - 1);
        player.addMoney(this.getPropertyPrice());

        if (this.checkPropertiesInCountryAndSet(player)) {
            this.checkIsDoublePaymentProperty(player);
        }
    }

    public void sellAllBuildings(Player player) {
        if (isHotelOwned())
            sellTheHotel(player);
        player.addMoney(getNumOfHouses() * getHousePrice());
        this.setNumOfHouses(0);

        if (this.checkPropertiesInCountryAndSet(player)) {
            this.checkIsDoublePaymentProperty(player);
        }
    }
}