package sample.Model.Properties;

import sample.DialogForm;
import sample.Model.Bank;
import sample.Model.Player;

import java.util.ArrayList;

public class Railroad extends PaidProperty {

    private int[] chargesArray = {50, 100, 200, 400};

    public Railroad(int numberOfField, String name) {
        super(numberOfField, name, 400, 200);
    }

    @Override
    public String[] getPossibleActions(Player player) {
        ArrayList<String> actions = new ArrayList<String>();

        return this.addPropertyGenericAvailableActions(actions, player).toArray(new String[actions.size()]);

    }

    @Override
    public void showPropertyCard() {
        String textToShow = "";
        textToShow += ("*******************************\n");
        textToShow += (" Pole: [" + getName() + "]\n");
        textToShow += (" Cena zakupu: " + getPropertyPrice() + "\n");
        textToShow += (" Oplata za postoj:\n");
        textToShow += ("  -jezeli wlasciciel posiada 1 linie - " + chargesArray[0] + "\n");
        textToShow += ("  -jezeli wlasciciel posiada 2 linie - " + chargesArray[1] + "\n");
        textToShow += ("  -jezeli wlasciciel posiada 3 linie - " + chargesArray[2] + "\n");
        textToShow += ("  -jezeli wlasciciel posiada 4 linie - " + chargesArray[3] + "\n");
        textToShow += (" Zastaw hipoteczny: " + getMortgage() + "\n");
        textToShow += ("******************************\n");

        DialogForm.showOnlyText(textToShow);
    }


    @Override
    protected String calculateCharge(Player player, Bank bank) {
        int charge = (chargesArray[this.owner.getRailRoadsOwned()-1]);
        owner.demandMoneyFromPlayer(player, charge, bank);
        return "";
    }

    public String[] getRequiredActions(Player player, Bank bank) {
        ArrayList<String> actions = new ArrayList<>();

        if (player != this.owner && !this.available && !this.isPawned())
            actions.add(this.calculateCharge(player, bank));

        return actions.toArray(new String[actions.size()]);
    }
}
