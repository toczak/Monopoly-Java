package sample.Model.Properties;

import sample.DialogForm;
import sample.Model.Bank;
import sample.Model.Player;

import java.util.ArrayList;

public class Utility extends PaidProperty {

    private static final int baseMultiplier = 10;


    public Utility(int numberOfField, String name, int propertyPrice, int mortgage) {
        super(numberOfField, name, propertyPrice, mortgage);
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
        textToShow += ("   10$ x Ilosc Oczek \n");
        textToShow += ("   jesli wlasciciel posiada\n");
        textToShow += ("   wodociagi i elektrownie to x2 \n");
        textToShow += (" Zastaw hipoteczny: " + getMortgage() + "\n");
        textToShow += ("******************************\n");

        DialogForm.showOnlyText(textToShow);
    }

    public String[] getRequiredActions(Player player, Bank bank) {
        ArrayList<String> actions = new ArrayList<>();

        if (player != this.owner && !this.available && !this.isPawned())
            actions.add(this.calculateCharge(player, bank));

        return actions.toArray(new String[actions.size()]);
    }

    protected String calculateCharge(Player player, Bank bank) {
        int diceValue = (int) (Math.random() * 6) + 1 + (int) (Math.random() * 6) + 1;
        int multiplier;

        DialogForm.showOnlyText("Rzucasz kostką aby obliczyć kwote do zapłaty!\nWyrzuciłeś kostkami łącznie: " + diceValue);

        if (owner.getUtilitiesOwned() == 2)
            multiplier = baseMultiplier * 2;
        else
            multiplier = baseMultiplier;

        int charge = (multiplier * diceValue);
        owner.demandMoneyFromPlayer(player, charge, bank);
        return "";
    }

}

