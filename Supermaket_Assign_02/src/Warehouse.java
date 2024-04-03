import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    public List<Item> items;


    public int totalOfItems = 0;

    public Warehouse() {

        this.items = new ArrayList<>();

    }
    public void BuyItem(String name, int quantity) {
        boolean itemExists = false;
        for(Item item : this.items) {
            if (item.name.equalsIgnoreCase(name)) {
                item.quantity += quantity;
                itemExists = true;
                break;
            }

        }
        if(!itemExists) {
            items.add(new Item(name, 200, quantity));
        }
    }

    public void SellItem(String name, int quantity) {
        for(Item item : this.items) {
            if(item.name.equalsIgnoreCase(name)) {
                item.quantity -= quantity;
                break;
            }
        }
    }
    public void DisplayWarehouse() {
        for(int i = 0; i < items.size(); i++) {
            System.out.println("\t" + i + " - " + items.get(i).name + " available: " + items.get(i).quantity + " units");
            System.out.println("\t\tPrice: $" + items.get(i).price);
        }
    }
}
