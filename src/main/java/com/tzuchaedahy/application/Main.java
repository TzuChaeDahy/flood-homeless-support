package com.tzuchaedahy.application;

import com.tzuchaedahy.ui.UI;

public class Main {
    public static void main(String[] args) {
        boolean keepGoing = true;
            do {
                try {
                    keepGoing = UI.handleMainMenu();
                } catch (RuntimeException e) {
                    UI.clearScreen();
                    UI.showCustomMessage(e.getMessage());
                }
            } while (keepGoing);

    }
}