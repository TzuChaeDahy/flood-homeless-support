package com.tzuchaedahy.ui;

import java.util.Scanner;

public class UI {
    public static Scanner scanner = new Scanner(System.in);

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.out.print(e.getMessage());
            delay(500);
        }
    }

    public static void showCustomMessage(String message) {
        System.out.print(message);
        delay(2000);
    }

    public static void showInvalidOptionMessage() {
        clearScreen();
        System.out.println("Opçao invalida! Tente novamente!");
        delay(1000);
    }

    public static void showRedirectingMessage() {
        clearScreen();
        System.out.println("Redirecting...");
        delay(500);
    }

    public static void showMainMenu() {
        clearScreen();
        System.out.println("Flood Homeless Support - Menu Inicial");
        System.out.println();
        System.out.println("Selecione uma opçao: ");
        System.out.println("1. Entrar como doador");
        System.out.println("2. Entrar como centro de distribuiçao");
        System.out.println("3. Entrar como abrigo");
        System.out.println();
        System.out.println("0. Sair da aplicaçao");

    }

    public static boolean handleMainMenu() {
        showMainMenu();

        switch (scanner.nextInt()) {
            case 0:
                return false;
            case 1:
                UI.showRedirectingMessage();
                DonationUI.handleMenu();
                break;
            case 2:
                UI.showRedirectingMessage();
                DistributionCenterUI.handleMenu();
                break;
            case 3:
                UI.showRedirectingMessage();
                ShelterUI.handleMenu();
                break;
            default:
                showInvalidOptionMessage();
        }

        return true;
    }
}
