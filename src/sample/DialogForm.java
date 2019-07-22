package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;

import java.io.IOException;
import java.util.Optional;

public class DialogForm {

    public static int getValue(String text) {
        Dialog dialog = new TextInputDialog();
        dialog.setTitle("Monopoly");
        dialog.setHeaderText(text);

        Optional<String> result;
        String valueString = "";
        int valueInt = 0;
        boolean isNumber = true;

        do {
            result = dialog.showAndWait();
            isNumber = true;
            if (result.isPresent()) {
                valueString = result.get();

                try {
                    valueInt = Integer.parseInt(valueString);
                } catch (NumberFormatException e) {
                    showOnlyText("Błędna liczba!");
                    isNumber = false;
                }

//                if(valueString.length()>0){
//                }else isNumber = false;
            } else isNumber = false;
        } while (!isNumber);
        return valueInt;
    }

    public static int getValueMinMax(String text, int min, int max) {
        Dialog dialog = new TextInputDialog();
        dialog.setTitle("Monopoly");
        dialog.setHeaderText(text);

        Optional<String> result;
        String valueString = "";
        int valueInt = 0;
        boolean isNumber = true;

        do {
            isNumber = true;
            result = dialog.showAndWait();
            if (result.isPresent()) {
                valueString = result.get();
                try {
                    if (valueString.length() > 0) {
                        valueInt = Integer.parseInt(valueString);
                    } else isNumber = false;
                } catch (NumberFormatException e) {

                    showOnlyText("Błąd! Liczba ma zły format.");
                    isNumber = false;
                }
                if (valueInt < min || valueInt >= max) {
                    showOnlyText("Błąd! Liczba jest niepoprawna.");
                    isNumber = false;
                }
            } else isNumber = false;
        } while (!isNumber);
        return valueInt;
    }

    public static String showAndGetString(String text) {
        Dialog dialog = new TextInputDialog();
        dialog.setTitle("Monopoly");
        dialog.setHeaderText(text);
        Optional<String> result;
        String value = "";
        boolean isString;

        do {
            isString = true;
            result = dialog.showAndWait();

            if (result.isPresent() && result.get().length() > 0) {
                value = result.get();
            } else {
                isString = false;
            }
        } while (!isString);
        return value;

    }

    public static void showOnlyText(String text) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Monopoly");
        alert.setHeaderText("");
        alert.setContentText(text);

        alert.showAndWait();
//        Dialog dialog = new Dialog();
//        dialog.setTitle("Monopoly");
//        dialog.setHeaderText(text);
//
//        Optional<String> result = dialog.showAndWait();
//        String entered = "none.";
//
//        if (result.isPresent()) {
//            entered = result.get();
//        }
    }
}
