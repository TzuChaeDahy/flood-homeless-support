package com.tzuchaedahy.ui;

import com.tzuchaedahy.controllers.ShelterController;
import com.tzuchaedahy.domain.Shelter;

import java.util.List;
import java.util.Scanner;

public class ShelterUI {
    public static Scanner scanner = new Scanner(System.in);

    public static ShelterController shelterController = new ShelterController();

    public static void showMenu() {
        UI.clearScreen();
        System.out.println("Flood Homeless Support - Menu de Abrigo");
        System.out.println();
        System.out.println("1. Cadastrar abrigo");
        System.out.println("2. Entrar como um abrigo");
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
                case 2:
                    UI.showRedirectingMessage();
                    handleLogAsShelter();
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

    private static void handleLogAsShelter() {
        UI.clearScreen();

        List<Shelter> shelters = shelterController.findAll();
        if (shelters.size() < 1) {
            UI.showCustomMessage("Não foi possivel encontrado nenhum abrigo.");
            return;
        }

        System.out.println("Escolha um abrigo: ");

        final int[] i = {1};
        shelters.forEach(shelter -> {
            System.out.printf("%s. %s\n", i[0], shelter.getName());
            i[0]++;
        });

        int option = scanner.nextInt();

        if (option < 1 || option > shelters.size()) {
            UI.showInvalidOptionMessage();
            return;
        }

        var shelter = shelters.get(option - 1);

        handleChosenShelterMenu(shelter);
    }

    public static void showChosenShelterMenu(String name) {
        UI.clearScreen();
        System.out.printf("Flood Homeless Support - Menu de Abrigo - %s\n", name);
        System.out.println();
        System.out.println("1. Visualizar dados do abrigo");
        System.out.println();
        System.out.println("0. Voltar ao menu anterior");
    }

    public static void handleChosenShelterMenu(Shelter shelter) {
        boolean keepGoing = true;

        do {
            showChosenShelterMenu(shelter.getName());

            switch (scanner.nextInt()) {
                case 0:
                    UI.showRedirectingMessage();
                    keepGoing = false;
                    break;
                case 1:
                    UI.showRedirectingMessage();
                    handleViewShelterInformation(shelter);
                    break;
                default:
                    UI.showInvalidOptionMessage();
            }
        } while (keepGoing);
    }

    private static void handleViewShelterInformation(Shelter shelter) {
        UI.clearScreen();

        var donatedItems = shelterController.findDonatedItemTypesQuantities(shelter);

        System.out.printf("Nome: %s\n", shelter.getName());
        System.out.printf("Endereço: %s\n", shelter.getAddress());
        System.out.printf("Nome do Responsável: %s\n", shelter.getResponsible());
        System.out.printf("Email: %s\n", shelter.getEmail());
        System.out.printf("Telefone: %s\n", shelter.getPhone());
        System.out.printf("Capacidade Total: %s\n", shelter.getCapacity());
        System.out.printf("Ocupação: %s%%\n", (shelter.getOccupation() / shelter.getCapacity()));
        System.out.println("Unidade de itens recebidos: ");
        if (donatedItems.size() <= 0) {
            System.out.println("Ainda não existem itens doados");
        } else {
            donatedItems.forEach((key, value) -> {
                System.out.printf("  %s: %s unidades\n", key, value);
            });
        }

        scanner.next();
    }
}
