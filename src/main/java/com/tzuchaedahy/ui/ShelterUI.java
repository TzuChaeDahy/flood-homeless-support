package com.tzuchaedahy.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.tzuchaedahy.controllers.ItemDonationController;
import com.tzuchaedahy.controllers.ItemTypeController;
import com.tzuchaedahy.controllers.OrderController;
import com.tzuchaedahy.controllers.OrderItemController;
import com.tzuchaedahy.controllers.ShelterController;
import com.tzuchaedahy.domain.ItemDonation;
import com.tzuchaedahy.domain.ItemType;
import com.tzuchaedahy.domain.Order;
import com.tzuchaedahy.domain.OrderItem;
import com.tzuchaedahy.domain.OrderStatus;
import com.tzuchaedahy.domain.Shelter;
import com.tzuchaedahy.util.StringFormatter;

public class ShelterUI {
    public static Scanner scanner = new Scanner(System.in);

    public static ShelterController shelterController = new ShelterController();
    public static ItemTypeController ItemTypeController = new ItemTypeController();
    public static ItemDonationController itemDonationController = new ItemDonationController();

    private static OrderItemController orderItemController = new OrderItemController();
    private static OrderController orderController = new OrderController();

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

        System.out.print("Nome do abrigo: ");
        shelter.setName(scanner.nextLine());

        UI.clearScreen();
        System.out.print(
                "Endereço do abrigo (ex.: r. dr. décio martins costa, 312 - vila eunice nova, cachoeirinha - rs, 94920-170): ");
        shelter.setAddress(scanner.nextLine());

        UI.clearScreen();
        System.out.print("Nome do responsavel: ");
        shelter.setResponsible(scanner.nextLine());

        UI.clearScreen();
        System.out.print("Telefone do abrigo - sem traço e nem espaços (ex.: 5199999000): ");
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

        final int[] i = { 1 };
        shelters.forEach(shelter -> {
            System.out.printf("%s. %s\n", i[0], StringFormatter.capitalize(shelter.getName()));
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
        System.out.println("2. Solicitar item");
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
                case 2:
                    UI.showRedirectingMessage();
                    handleItemsCheckout(shelter);
                    break;
                default:
                    UI.showInvalidOptionMessage();
            }
        } while (keepGoing);
    }

    private static void handleViewShelterInformation(Shelter shelter) {
        UI.clearScreen();

        var donatedItems = shelterController.findDonatedItemTypesQuantities(shelter);

        System.out.printf("Nome: %s\n", StringFormatter.capitalize(shelter.getName()));
        System.out.printf("Endereço: %s\n", shelter.getAddress());
        System.out.printf("Nome do Responsável: %s\n", StringFormatter.capitalize(shelter.getResponsible()));
        System.out.printf("Email: %s\n", shelter.getEmail());
        System.out.printf("Telefone: %s\n", shelter.getPhone());
        System.out.printf("Capacidade Total: %s pessoas\n", shelter.getCapacity());
        System.out.printf("Ocupação: %.2f%%\n",
                ((float) shelter.getOccupation() / (float) shelter.getCapacity()) * 100);
        System.out.println("Unidades de itens recebidos: ");
        if (donatedItems.size() <= 0) {
            System.out.println("Ainda não existem itens doados");
        } else {
            donatedItems.forEach((key, value) -> {
                System.out.printf("  %s: %s unidades\n", key, value);
            });
        }

        scanner.next();
    }

    private static void handleItemsCheckout(Shelter shelter) {
        List<OrderItem> orderItems = new ArrayList<>();

        Order order = new Order();
        order.setShelter(shelter);
        order.setStatus(OrderStatus.defaultStatus());

            var orderItem = chooseItem(shelter);
            if (orderItem == null) {
                return;
            }
            orderItems.add(orderItem);

        if (orderItems.size() <= 0) {
            UI.clearScreen();
            UI.showCustomMessage("Nenhum item foi selecionado.");
            return;
        }

        orderController.save(order);
        orderItems.forEach(item -> item.setOrder(order));
        orderItemController.saveAll(orderItems);

        UI.clearScreen();
        UI.showCustomMessage("Pedido solicitado com sucessso!");
    }

    public static OrderItem chooseItem(Shelter shelter) {
        UI.clearScreen();
        System.out.println("Que tipo de item voce deseja solicitar?");

        Map<String, ItemType> itemTypes = ItemTypeController.findAll();
        itemTypes.forEach((key, value) -> {
            System.out.printf("%s. %s\n", key, StringFormatter.capitalize(value.getName()));
        });

        int itemTypeIndex = scanner.nextInt();
        if (itemTypeIndex < 1 || itemTypeIndex > itemTypes.size()) {
            UI.showInvalidOptionMessage();
            return null;
        }

        UI.clearScreen();
        System.out.println("Qual item deseja solicitar?");
        final int[] i = { 1 };
        itemTypes.get(String.valueOf(itemTypeIndex)).getDefaultNames().forEach(name -> {
            System.out.printf("%s. %s\n", i[0], StringFormatter.capitalize(name));
            i[0]++;
        });

        int itemNameIndex = scanner.nextInt();

        if (itemNameIndex < 1 || itemNameIndex > itemTypes.size() + 1) {
            UI.showInvalidOptionMessage();
            return null;
        }

        String itemName = itemTypes.get(String.valueOf(itemTypeIndex)).getDefaultNames().get(itemNameIndex - 1);

        UI.clearScreen();
        System.out.println("Quantas unidades deseja solicitar? ");

        int quantity = scanner.nextInt();

        var canReceiveOrder = orderItemController.canReceiveOrder(
            shelter,
            itemTypes.get(String.valueOf(itemTypeIndex)),
            quantity
        );

        if (!canReceiveOrder) {
            UI.clearScreen();
            UI.showCustomMessage("Nao foi possivel finalizar a solicitacao, pois a sua quantidade excede o limite do estoque.");
            return null;
        }

        Map<String, List<ItemDonation>> availableItems = itemDonationController.findAvailableItemsByName(itemName);

        if (availableItems.size() == 0) {
            UI.clearScreen();
            UI.showCustomMessage("Nao foram encontrados itens disponiveis com esses atributos");
            return null;
        }

        UI.clearScreen();
        System.out.println("Essas sao os itens disponiveis para voce realizar o seu pedido:");

        final int[] j = { 1 };
        final List<ItemDonation> possibleItems = new ArrayList<>();
        availableItems.forEach((distributionCenterName, items) -> {
            System.out.println("Centro de Distribuicao: " + distributionCenterName);

            items.forEach(item -> {
                System.out.printf("  %s. ", j[0]);
                item.getItem().getAttributes().forEach((key, value) -> {
                    System.out.printf("%s ", value);
                });
                System.out.printf("(%s unidades)\n", item.getQuantity());

                possibleItems.add(item);

                j[0]++;
            });
        });

        var item = possibleItems.get(scanner.nextInt() - 1);

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item.getItem());
        orderItem.setQuantity(quantity < item.getQuantity() ? quantity : item.getQuantity());

        return orderItem;
    }
}
