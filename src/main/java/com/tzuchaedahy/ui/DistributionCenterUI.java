package com.tzuchaedahy.ui;

import com.tzuchaedahy.controllers.DistributionCenterController;
import com.tzuchaedahy.controllers.ItemDonationController;
import com.tzuchaedahy.controllers.ItemTypeController;
import com.tzuchaedahy.domain.DistributionCenter;
import com.tzuchaedahy.domain.ItemDonation;
import com.tzuchaedahy.domain.ItemType;
import com.tzuchaedahy.util.StringFormatter;

import java.util.*;

public class DistributionCenterUI {
    public static Scanner scanner = new Scanner(System.in);

    public static DistributionCenterController distributionCenterController = new DistributionCenterController();
    public static ItemDonationController itemDonationController = new ItemDonationController();
    public static ItemTypeController itemTypeController = new ItemTypeController();

    public static void showMenu() {
        UI.clearScreen();
        System.out.println("Flood Homeless Support - Menu de Centro de Distribuiçao");
        System.out.println();
        System.out.println("1. Ver todos os itens doados");
        System.out.println();
        System.out.println("0. Voltar ao menu inicial");
    }

    public static void handleMenu() {
        boolean keepGoing = true;
        DistributionCenter distributionCenter = null;

        do {
            while (distributionCenter == null) {
                distributionCenter = showDistributionCenterOptions();
            }

            showMenu();

            switch (scanner.nextInt()) {
                case 0:
                    UI.showRedirectingMessage();
                    keepGoing = false;
                    break;
                case 1:
                    handleViewAllDonation(distributionCenter);
                    break;
                default:
                    UI.showInvalidOptionMessage();
            }
        } while (keepGoing);
    }

    public static DistributionCenter showDistributionCenterOptions() {
        var distributionCenters = distributionCenterController.findAll();

        if (distributionCenters.isEmpty()) {
            UI.showCustomMessage("Nao ha centros de distribuicao cadastrados.");

            return null;
        }

        UI.clearScreen();
        System.out.println("Escolha um centro de distribuicao: ");
        distributionCenters.forEach((index, distributionCenter) -> {
            System.out.printf("%s. %s%n", index, StringFormatter.capitalize(distributionCenter.getName()));
        });

        int option = scanner.nextInt();

        if (option < 1 || option > distributionCenters.size()) {
            UI.showInvalidOptionMessage();
            return null;
        }

        UI.showRedirectingMessage();
        return distributionCenters.get(Integer.toString(option));
    }

    public static String handleAttributesViewCreation(Map<String, String> attributes) {
        if (attributes.isEmpty()) {
            return "";
        }

        final StringBuilder textAttributtes = new StringBuilder();
        attributes.forEach((key, value) -> {
            textAttributtes.append(value).append("/");
        });

        return String.format("(%s)", textAttributtes.substring(0, textAttributtes.length() - 1));
    }

    public static void handleViewAllDonation(DistributionCenter distributionCenter) {
        UI.clearScreen();

        List<ItemDonation> donation = itemDonationController.findAllSimplifiedByDistributionCenter(distributionCenter);
        Map<String, ItemType> itemTypes = itemTypeController.findAll();

        final int[] i = {1};
        itemTypes.forEach((index, itemType) -> {
            System.out.println(StringFormatter.capitalize(itemType.getName()));

            var donationFiltered = donation.stream().filter(item -> Objects.equals(item.getItem().getItemType().getName(), itemType.getName())).toList();

            if (donationFiltered.isEmpty()) {
                System.out.println("nao ha itens desse tipo cadastrados.");
            }

            final int[] total = {0};
            donationFiltered.forEach(itemDonation -> {
                    System.out.printf("%s. %s %s - %s unidades\n",
                            i[0],
                            itemDonation.getItem().getName(),
                            handleAttributesViewCreation(itemDonation.getItem().getAttributes()),
                            itemDonation.getQuantity()
                    );

                    total[0] += itemDonation.getQuantity();
            });

            if (!donationFiltered.isEmpty()) {
                System.out.printf("Total de unidades: %d\n", total[0]);
            }
            System.out.println();
        });

        scanner.next();
    }
}
