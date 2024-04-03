public class Main {
    public static void main(String[] args) {
    Warehouse warehouse = new Warehouse();
        warehouse.items.add(new Item("Weed" , 200, 50));
        warehouse.items.add(new Item("Alcohol" , 300, 50));
        warehouse.items.add(new Item("Cig" , 400, 50));
        warehouse.items.add(new Item("Coca" , 500, 50));
        warehouse.items.add(new Item("Milk" , 100, 50));
        warehouse.items.add(new Item("Soda" , 200, 50));

    User user = new User();
        user.items.add(new Item("Corn", 100, 10));
        user.items.add(new Item("Cig", 200, 10));
        user.items.add(new Item("Alcohol", 250, 10));
        user.items.add(new Item("Milk", 50, 10));

    ManagerSoftware manager = new ManagerSoftware(warehouse, user);
        manager.run();
}
}