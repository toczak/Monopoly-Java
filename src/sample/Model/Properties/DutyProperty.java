package sample.Model.Properties;

import sample.DialogForm;
import sample.Model.Bank;
import sample.Model.Player;

import java.util.ArrayList;

public class DutyProperty extends Property {

    private int charge;

    public DutyProperty(int numberOfField, String name, int charge) {
        super(numberOfField, name);
        this.charge = charge;
    }

    public String[] getRequiredActions(Player player, Bank bank) {
        ArrayList<String> actions = new ArrayList<>();
            actions.add(this.calculateCharge(player, bank));

        return actions.toArray(new String[actions.size()]);
    }

    protected String calculateCharge(Player player, Bank bank) {
        if (charge > player.getMoney()) {
            DialogForm.showOnlyText("Nie masz na tyle pieniędzy: " + charge + ", aby opłacić " + this.getName() + ".\nMusisz sprzedać lub zastawić swoje pola.");
            do {
                player.showMenuToSell(bank);
            } while (charge > player.getMoney() || player.isBankrupt());
            if (player.isBankrupt()) {
                DialogForm.showOnlyText("Niestety, to koniec gry dla Ciebie!");
            } else DialogForm.showOnlyText("Udało ci się spłacić dług. Możesz grać dalej!");

        } else {
            player.takeMoney(charge);
            DialogForm.showOnlyText(player.getName() + "! Niestety, jesteś zmuszony \nzapłacić: " + charge + " dla gracza: " + this.getName());
        }
        return "";
    }
}
