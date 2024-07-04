package com.tzuchaedahy.application;

import com.tzuchaedahy.ui.UI;

public class Main {
    public static void main(String[] args) {
        boolean keepGoing = true;

        do {
            keepGoing = UI.handleMainMenu();
        } while (keepGoing);
    }
}