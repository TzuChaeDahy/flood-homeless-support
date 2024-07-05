package com.tzuchaedahy.ui;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.tzuchaedahy.controllers.*;
import com.tzuchaedahy.domain.*;
import com.tzuchaedahy.util.StringFormatter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
        System.out.println("2. Realizar doaçoes atraves de arquivo csv");
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
                case 2:
                    UI.showRedirectingMessage();
                    handleCSVCreateDonation();
                    break;
                default:
                    UI.showInvalidOptionMessage();
            }
        } while (keepGoing);
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

        if (itemTypeIndex < 1 || itemTypeIndex > distributionCenters.size()) {
            UI.showInvalidOptionMessage();
            return;
        }

        // TODO: IMPLEMENTAR VALIDAÇAO CASO 1000 PRODUTOS

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

    public static void handleCSVCreateDonation() {
        UI.clearScreen();

        System.out.println("Digite o caminho do arquivo csv partindo da pasta resource (ex.: csv/data.csv): ");

        scanner.nextLine();

        List<String[]> lines = new ArrayList<>();
        String path = "/home/tzuchaedahy/IdeaProjects/flood-homeless-support/src/main/resources/" + scanner.nextLine();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine();

            while (reader.ready()) {
                lines.add(reader.readLine().split(","));
            }
        } catch (IOException e) {
            UI.clearScreen();
            UI.showCustomMessage("Nao foi possivel ler o arquivo csv.");
            return;
        }

        lines.forEach(line -> {
            var distributionCenter = distributionCenterController.findByName(line[0]);
            if (distributionCenter == null) {
                UI.clearScreen();
                UI.showCustomMessage(String.format("o centro de distribuicao de nome %s nao pode ser encontrado.", line[0]));
                return;
            }
            var itemType = itemTypeController.findByName(line[1]);
            if (itemType == null) {
                UI.clearScreen();
                UI.showCustomMessage(String.format("o tipo de item de nome %s nao pode ser encontrado.", line[1]));
                return;
            }

            var isItemNamePossible = itemTypeController.isItemNamePossible(itemType, line[2]);
            if (!isItemNamePossible) {
                UI.clearScreen();
                UI.showCustomMessage(String.format("o nome %s nao esta na lista de possiveis itens.", line[2]));
                return;
            }

            var name = line[2];

            var itemAttributes = mapItemAttributes(itemType, line[3]);
            if (itemAttributes == null) {
                return;
            }

            int quantity = Integer.parseInt(line[4]);

            var donation = new Donation();
            donation.setDistributionCenter(distributionCenter);
            donationController.save(donation);

            var item = new Item();
            item.setItemType(itemType);
            item.setName(name);
            item.setAttributes(itemAttributes);
            itemController.save(item);

            var itemDonation = new ItemDonation();
            itemDonation.setDonation(donation);
            itemDonation.setItem(item);
            itemDonation.setQuantity(quantity);
            itemDonationController.save(itemDonation);
        });
        
        UI.clearScreen();
        UI.showCustomMessage("Dados salvos com sucesso!");
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

    public static Map<String, String> mapItemAttributes(ItemType itemType, String attributes) {
        Map<String, String> itemAttributes = new TreeMap<>();
        if (attributes.isEmpty() | attributes.isBlank()) {
            return itemAttributes;
        }

        String[] attributesList = attributes.split("!");
        List<String> defaultAttributes = itemType.getDefaultAttributes();

        if (attributesList.length != defaultAttributes.size()) {
            UI.showCustomMessage("o numero de atributos e incoerente.");
            return null;
        }

        for (int i = 0; i < attributesList.length; i++) {
            itemAttributes.put(defaultAttributes.get(i), attributesList[i]);
        }

        return itemAttributes;
    }
}
