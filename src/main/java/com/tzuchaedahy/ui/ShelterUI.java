package com.tzuchaedahy.ui;

import com.tzuchaedahy.controllers.ShelterController;
import com.tzuchaedahy.domain.Shelter;

import java.util.Scanner;

public class ShelterUI {
    public static Scanner scanner = new Scanner(System.in);

    public static ShelterController shelterController = new ShelterController();

    public static void showMenu() {
        UI.clearScreen();
        System.out.println("Flood Homeless Support - Menu de Abrigo");
        System.out.println();
        System.out.println("1. Cadastrar abrigo");
        System.out.println();
        System.out.println("0. Voltar ao menu inicial");
    }

    public static void handleMenu() {
        boolean keepGoing = true;

        do {
            showMenu();

            switch (scanner.nextInt()) {
                case 0:
                    UI.showRedirectingMessage();
                    keepGoing = false;
                    break;
                case 1:
                    UI.showRedirectingMessage();
                    handleCreateShelter();
                    break;
                default:
                    UI.showInvalidOptionMessage();
            }
        } while (keepGoing);
    }

    public static void handleCreateShelter() {
        UI.clearScreen();
        scanner.nextLine();

        var shelter = new Shelter();

        System.out.print("Nome do abrigo :");
        shelter.setName(scanner.nextLine());

        UI.clearScreen();
        System.out.print("Endereço do abrigo (ex.: r. dr. décio martins costa, 312 - vila eunice nova, cachoeirinha - rs, 94920-170) :");
        shelter.setAddress(scanner.nextLine());

        UI.clearScreen();
        System.out.print("Nome do responsavel: ");
        shelter.setResponsible(scanner.nextLine());

        UI.clearScreen();
        System.out.print("Telefone do abrigo - sem traço e nem espaços (ex.: 5199999000) : ");
        shelter.setPhone(scanner.nextLine());

        UI.clearScreen();
        System.out.print("Email do abrigo: ");
        shelter.setEmail(scanner.nextLine());

        UI.clearScreen();
        System.out.print("Capacidade maxima do abrigo (em pessoas): ");
        shelter.setCapacity(scanner.nextInt());

        UI.clearScreen();
        System.out.print("Ocupaçao atual (em pessoas): ");
        shelter.setOccupation(scanner.nextInt());

        shelterController.save(shelter);

        UI.clearScreen();
        UI.showCustomMessage("Abrigo cadastrado com sucesso!");
    }
}
