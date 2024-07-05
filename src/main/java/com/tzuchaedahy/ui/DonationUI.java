package com.tzuchaedahy.ui;

import com.tzuchaedahy.controllers.*;
import com.tzuchaedahy.domain.Donation;
import com.tzuchaedahy.domain.Item;
import com.tzuchaedahy.domain.ItemDonation;
import com.tzuchaedahy.domain.ItemType;
import com.tzuchaedahy.util.StringFormatter;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DonationUI {
    public static Scanner scanner = new Scanner(System.in);

    private static final DistributionCenterController distributionCenterController = new DistributionCenterController();
    private static final ItemTypeController itemTypeController = new ItemTypeController();
    private static final DonationController donationController = new DonationController();
    private static final ItemController itemController = new ItemController();
    private static final ItemDonationController itemDonationController = new ItemDonationController();

    public static void showMenu() {
        UI.clearScreen();
        System.out.println("Flood Homeless Support - Menu de Doador");
        System.out.println();
        System.out.println("1. Realizar doaçao");
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
                    handleCreateDonation();
                    break;
                default:
                    UI.showInvalidOptionMessage();
            }
        } while (keepGoing);
    }

    public static Map<String, String> mapItemAttributes(ItemType itemType) {
        Map<String, String> itemAttributes = new HashMap<>();

        for (String attribute : itemType.getDefaultAttributes()) {
            UI.clearScreen();
            System.out.printf("Qual o(a) %s: ", attribute);
            String result = scanner.next();

            itemAttributes.put(attribute, result);
        }

        return itemAttributes;
    }

    public static void handleCreateDonation() {
        UI.clearScreen();
        System.out.print("Escolha um dos centros de distribuicao: \n\n");

        var distributionCenters = distributionCenterController.findAll();

        if (distributionCenters.isEmpty()) {
            UI.showCustomMessage("Nao ha centros de distribuicao cadastrados.");

            return;
        }

        distributionCenters.forEach((index, distributionCenter) -> {
            System.out.printf("%s. %s%n", index, StringFormatter.capitalize(distributionCenter.getName()));
        });

        Integer distributionCenterIndex = scanner.nextInt();

        if (distributionCenterIndex < 1 || distributionCenterIndex > distributionCenters.size()) {
            UI.showInvalidOptionMessage();
            return;
        }

        UI.clearScreen();
        System.out.print("Escolha um tipo de item a doar: \n\n");

        var itemTypes = itemTypeController.findAll();

        if (itemTypes.isEmpty()) {
            UI.showCustomMessage("Nao ha tipos de itens cadastrados.");

            return;
        }

        itemTypes.forEach((index, itemType) -> {
            System.out.printf("%s. %s%n", index, StringFormatter.capitalize(itemType.getName()));
        });

        Integer itemTypeIndex = scanner.nextInt();

        // TODO: IMPLEMENTAR VALIDAÇAO CASO 1000 PRODUTOS

        if (itemTypeIndex < 1 || itemTypeIndex > distributionCenters.size()) {
            UI.showInvalidOptionMessage();
            return;
        }

        UI.clearScreen();
        System.out.print("Qual o produto?\n\n");

        if (itemTypes.get(itemTypeIndex.toString()).getDefaultNames().isEmpty()) {
            UI.showCustomMessage("Nao ha possiveis itens cadastrados.");

            return;
        }

        final int[] index = {1};
        itemTypes.get(itemTypeIndex.toString()).getDefaultNames().forEach((name) -> {
            System.out.printf("%s. %s%n", index[0], StringFormatter.capitalize(name));
            index[0]++;
        });

        var itemNameIndex = scanner.nextInt();

        if (itemNameIndex < 1 || itemNameIndex > itemTypes.get(itemTypeIndex.toString()).getDefaultNames().size()) {
            UI.showInvalidOptionMessage();
            return;
        }

        Map<String, String> itemAttributes = new HashMap<>();
        itemAttributes = mapItemAttributes(itemTypes.get(itemTypeIndex.toString()));

        UI.clearScreen();
        System.out.println("Qual a quantidade itens a doar? ");
        var itemsQuantity = scanner.nextInt();

        if (itemsQuantity < 1) {
            UI.showInvalidOptionMessage();
            return;
        }

        var donation = new Donation();
        donation.setDistributionCenter(distributionCenters.get(distributionCenterIndex.toString()));
        donationController.save(donation);

        var item = new Item();
        item.setName(itemTypes.get(itemTypeIndex.toString()).getDefaultNames().get(itemNameIndex - 1));
        item.setItemType(itemTypes.get(itemTypeIndex.toString()));
        item.setAttributes(itemAttributes);
        itemController.save(item);

        var itemDonation = new ItemDonation();
        itemDonation.setDonation(donation);
        itemDonation.setItem(item);
        itemDonation.setQuantity(itemsQuantity);
        itemDonationController.save(itemDonation);
    }
}
