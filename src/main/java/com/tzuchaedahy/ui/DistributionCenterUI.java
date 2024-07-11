package com.tzuchaedahy.ui;

import com.tzuchaedahy.controllers.DistributionCenterController;
import com.tzuchaedahy.controllers.DonationController;
import com.tzuchaedahy.controllers.ItemController;
import com.tzuchaedahy.controllers.ItemDonationController;
import com.tzuchaedahy.controllers.ItemTypeController;
import com.tzuchaedahy.controllers.OrderController;
import com.tzuchaedahy.controllers.OrderItemController;
import com.tzuchaedahy.domain.DistributionCenter;
import com.tzuchaedahy.domain.Donation;
import com.tzuchaedahy.domain.Item;
import com.tzuchaedahy.domain.ItemDonation;
import com.tzuchaedahy.domain.ItemType;
import com.tzuchaedahy.domain.Order;
import com.tzuchaedahy.domain.OrderStatus;
import com.tzuchaedahy.util.StringFormatter;

import java.util.*;

public class DistributionCenterUI {
    public static Scanner scanner = new Scanner(System.in);

    public static DistributionCenterController distributionCenterController = new DistributionCenterController();
    public static ItemDonationController itemDonationController = new ItemDonationController();
    public static ItemTypeController itemTypeController = new ItemTypeController();

    public static OrderController orderController = new OrderController();
    public static OrderItemController orderItemController = new OrderItemController();

    public static DonationController donationController = new DonationController();
    public static ItemController itemController = new ItemController();

    public static void showMenu() {
        UI.clearScreen();
        System.out.println("Flood Homeless Support - Menu de Centro de Distribuiçao");
        System.out.println();
        System.out.println("1. Ver todos os itens doados");
        System.out.println("2. Visualizar pedidos pendentes");
        System.out.println("3. Transferir doação pra outro centro de distribuição");
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
                case 2:
                    handlePendingOrders(distributionCenter);
                    break;
                case 3:
                    handleTransferDonation(distributionCenter);
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

        var treeSet = new TreeSet<>(attributes.keySet());

        final StringBuilder textAttributtes = new StringBuilder();
        treeSet.forEach(key -> {
            textAttributtes.append(attributes.get(key)).append("/");
        });

        return String.format("(%s)", textAttributtes.substring(0, textAttributtes.length() - 1));
    }

    public static void handleViewAllDonation(DistributionCenter distributionCenter) {
        UI.clearScreen();

        List<ItemDonation> donation = itemDonationController.findAllSimplifiedByDistributionCenter(distributionCenter);
        Map<String, ItemType> itemTypes = itemTypeController.findAll();

        itemTypes.forEach((index, itemType) -> {
            System.out.println(StringFormatter.capitalize(itemType.getName()));

            var donationFiltered = donation.stream().filter(item -> Objects.equals(item.getItem().getItemType().getName(), itemType.getName())).toList();

            if (donationFiltered.isEmpty()) {
                System.out.println("nao ha itens desse tipo cadastrados.");
            }

            final int[] total = {0};
            final int[] i = {1};
            donationFiltered.forEach(itemDonation -> {
                    System.out.printf("%s. %s %s - %s unidades\n",
                            i[0],
                            StringFormatter.capitalize(itemDonation.getItem().getName()),
                            handleAttributesViewCreation(itemDonation.getItem().getAttributes()),
                            itemDonation.getQuantity()
                    );

                    total[0] += itemDonation.getQuantity();
                    i[0]++;
            });

            if (!donationFiltered.isEmpty()) {
                System.out.printf("Total de unidades: %d\n", total[0]);
            }
            System.out.println();
        });

        scanner.next();
    }

    public static void handlePendingOrders(DistributionCenter distributionCenter) {
        var pendingOrders = orderController.pendingOrders(distributionCenter);

        if(pendingOrders.size() <= 0) {
            UI.clearScreen();
            UI.showCustomMessage("Nao ha pedidos pendentes para esse centro de distribuicao.");
            return;
        }

        UI.clearScreen();
        System.out.println("Pedidos pendentes: ");

        final int[] i = {1};
        pendingOrders.forEach(pendingOrder -> {
            System.out.printf("%s. ID: %s (Para: %s)\n", i[0], pendingOrder.getId(), pendingOrder.getShelter().getName());
        });
        System.out.println("\n0. Voltar ao menu anterior");

        int option = scanner.nextInt();

        if (option < 0 || option > pendingOrders.size()) {
            UI.clearScreen();
            UI.showInvalidOptionMessage();
        } else if (option == 0) {
            UI.showRedirectingMessage();
            return;
        }

        showPendingOrderOption(pendingOrders.get(option - 1));
    }

    private static void showPendingOrderOption(Order order) {
        var orderItem = orderItemController.findByOrderID(order.getId());
        UI.clearScreen();

        if (orderItem == null) {
            UI.showCustomMessage("Nao existe item para esse pedido!");
        }
        System.out.println("Item: ");
        System.out.printf("%s %s - %s unidades\n", 
        StringFormatter.capitalize(orderItem.getItem().getName()), 
        handleAttributesViewCreation(orderItem.getItem().getAttributes()),
        orderItem.getQuantity()
        );

        System.out.println("O que deseja realizar?");
        System.out.println("1. Aceitar pedido");
        System.out.println("2. Recusar pedido\n");
        System.out.println("0. Voltar ao menu anterior");

        int option = scanner.nextInt();

        OrderStatus orderStatus = null;

        String description = null;
        if (option < 0 || option > 2) {
            UI.showInvalidOptionMessage();
        } else if (option == 1) {
            orderStatus = OrderStatus.acceptedStatus();
        } else if (option == 2) {
            orderStatus = OrderStatus.declinedStatus();
            UI.clearScreen();
            System.out.print("Qual o motivo da recusa? ");
            scanner.nextLine();
            description = scanner.nextLine();
        } else {
            return;
        }

        itemDonationController.subtractQuantity(orderItem.getItem().getId() ,orderItem.getQuantity());
        orderController.changeStatus(order.getId(), orderStatus.getId(), description);

        UI.clearScreen();
        UI.showCustomMessage("Dados salvos com sucesso!");
    }

    public static void handleTransferDonation(DistributionCenter distributionCenter) {
        UI.clearScreen();
        System.out.println("Escolha o centro de distribuição de destino: ");

        var distributionCenters = new ArrayList<>(distributionCenterController.findAll().values());
        final int[] i = {1};
        distributionCenters.forEach(center -> {
            if (!distributionCenter.equals(center)) {
                System.out.printf("%s. %s\n", i[0], StringFormatter.capitalize(center.getName()));
                i[0]++;
            }
        });

        int distributionCenterIndex = scanner.nextInt();

        if (distributionCenterIndex <= 0 || distributionCenterIndex > distributionCenters.size()) {
            UI.showInvalidOptionMessage();
            return;
        }

        var destinyDistributionCenter = distributionCenters.get(distributionCenterIndex);

        UI.clearScreen();
        System.out.println("Que tipo de item voce deseja transferir?");

        Map<String, ItemType> itemTypes = itemTypeController.findAll();
        itemTypes.forEach((key, value) -> {
            System.out.printf("%s. %s\n", key, StringFormatter.capitalize(value.getName()));
        });

        Integer itemTypeIndex = scanner.nextInt();
        if (itemTypeIndex < 1 || itemTypeIndex > itemTypes.size()) {
            UI.showInvalidOptionMessage();
            return;
        }

        UI.clearScreen();
        System.out.println("Qual item deseja transferir?");
        final int[] j = { 1 };
        itemTypes.get(String.valueOf(itemTypeIndex)).getDefaultNames().forEach(name -> {
            System.out.printf("%s. %s\n", j[0], StringFormatter.capitalize(name));
            j[0]++;
        });

        int itemNameIndex = scanner.nextInt();

        if (itemNameIndex < 1 || itemNameIndex > itemTypes.get(String.valueOf(itemTypeIndex)).getDefaultNames().size()) {
            UI.showInvalidOptionMessage();
            return;
        }

        String itemName = itemTypes.get(String.valueOf(itemTypeIndex)).getDefaultNames().get(itemNameIndex - 1);
        UUID[] donatedItemID = {null};

        final int[] totalQuantity = {0};
        var availableItemsMap = itemDonationController.findAvailableItemsByName(itemName);
        availableItemsMap.forEach((key, list) -> {
            if(distributionCenter.getName().equals(key)) {
                list.forEach(itemDonation -> {
                    donatedItemID[0] = itemDonation.getItem().getId();
                    totalQuantity[0] += itemDonation.getQuantity();
                });
            }
        });

        if (totalQuantity[0] <= 0) {
            UI.clearScreen();
            UI.showCustomMessage("Nao ha items disponiveis!");
            return;
        }
 
        UI.clearScreen();
        System.out.printf("Quantidade de itens disponiveis: %s\n\n", totalQuantity[0]);
        System.out.println("Quantas unidades deseja transferir? ");

        int quantity = scanner.nextInt();

        var canReceiveDonation = itemDonationController.canReceiveDonation(
            destinyDistributionCenter,
            itemTypes.get(String.valueOf(itemTypeIndex)),
            quantity
        );

        if (!canReceiveDonation) {
            UI.clearScreen();
            UI.showCustomMessage("Nao foi possivel finalizar a solicitacao, pois a sua quantidade excede o limite do estoque.");
            return;
        }

        if (quantity > totalQuantity[0]) {
            UI.clearScreen();
            UI.showCustomMessage("A quantidade a se doar nao pode ser maior que a disponivel.");
            return;
        }

        Map<String, String> itemAttributes = new HashMap<>();
        itemAttributes = DonationUI.mapItemAttributes(itemTypes.get(itemTypeIndex.toString()));

        var donation = new Donation();
        donation.setDistributionCenter(destinyDistributionCenter);
        donationController.save(donation);

        var item = new Item();
        item.setName(itemName);
        item.setItemType(itemTypes.get(itemTypeIndex.toString()));
        item.setAttributes(itemAttributes);
        itemController.save(item);

        var itemDonation = new ItemDonation();
        itemDonation.setDonation(donation);
        itemDonation.setItem(item);
        itemDonation.setQuantity(quantity);
        itemDonationController.save(itemDonation);

        itemDonationController.subtractQuantity(donatedItemID[0], quantity);

        UI.clearScreen();
        UI.showCustomMessage("Item transferido com sucesso!");
    }
}
