package sample.Model.Properties;

import sample.DialogForm;
import sample.Enum.Country;
import sample.Model.Bank;
import sample.Model.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Property implements Comparable<Property>{

    public int numberOfField;
    public String name;
    static HashMap<String,int[]> mapCityCountry = new HashMap<>();



    public Property(int numberOfField, String name) {
        this.numberOfField = numberOfField;
        this.name = name;
    }

    public static void setMapCityCountry() {
        mapCityCountry.put(Country.Grecja.toString(), new int[]{2, 4});
        mapCityCountry.put(Country.Włochy.toString(), new int[]{7, 9, 10});
        mapCityCountry.put(Country.Hiszpania.toString(), new int[]{12, 14, 15});
        mapCityCountry.put(Country.Wielka_Brytania.toString(), new int[]{17, 19, 20});
        mapCityCountry.put(Country.Beneluks.toString(), new int[]{22, 24,25});
        mapCityCountry.put(Country.Szwecja.toString(), new int[]{27, 28, 30});
        mapCityCountry.put(Country.RFN.toString(), new int[]{32, 33, 35});
        mapCityCountry.put(Country.Austria.toString(), new int[]{38, 40});
    }

    public int getNumberOfField() {
        return numberOfField;
    }

    public void setNumberOfField(int numberOfField) {
        this.numberOfField = numberOfField;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected ArrayList<String> addGenericAvailableActions(ArrayList<String> actions, Player player) {
        actions.add("Koniec rundy");
        actions.add("Koniec gry");

        return actions;
    }

    public String respondToAction(Player player, String action) {
//        switch (action) {
//
//            case "Kup domy lub hotel":
//                this.buyHouses(player);
//                break;
//        }
        return "";
    }

    public String[] getRequiredActions(Player player, Bank bank) {
        ArrayList<String> actions = new ArrayList<>();
        return actions.toArray(new String[actions.size()]);
    }

    public String[] getPossibleActions(Player player) {
        ArrayList<String> actions = new ArrayList<>();
        return this.addPropertyGenericAvailableActions(actions, player).toArray(new String[actions.size()]);
    }

    protected ArrayList<String> addPropertyGenericAvailableActions(ArrayList<String> actions, Player player) {
        return this.addGenericAvailableActions(actions, player);
    }

    @Override
    public int compareTo(Property property) {
        return this.getNumberOfField()-property.getNumberOfField();
    }

}
