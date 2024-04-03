import java.util.ArrayList;
import java.util.List;

public class User {
    List<Item> items;
    double totalEarning = 0;
    double totalExpense = 0;
    public User() {
        this.items = new ArrayList<>();
    }

    public void SellItem(String itemName, double price, int quantity) {
        for(Item item : this.items) {
            if (item.name.equalsIgnoreCase(itemName)) {
                item.quantity -= quantity;
                this.totalEarning += (price * quantity);
                item.trackSold += quantity;
                break;
            }
        }
    }

    public void BuyItem(String itemName, double price, int quantity) {
        boolean itemExists = false;
        for(Item item : this.items) {
            if (item.name.equalsIgnoreCase(itemName)) {
                item.quantity += quantity;
                this.totalExpense += (price * quantity);
                item.trackBought += quantity;
                itemExists = true;
                break;
            }
        }
        if(!itemExists) {
            this.items.add(new Item(itemName, price, quantity, false));
            this.totalExpense += (price * quantity);
        }
    }

    //Displaying total quantity of items bought
    public int DisplayToTalBoughtItem() {
        int totalQuantityBought = 0;
        for (Item item : items) {
            totalQuantityBought += item.trackBought;
        }
        System.out.println(totalQuantityBought);
        return totalQuantityBought;
    }
    //Displaying total quantity of items sold
    public int DisplayToTalSoldItem() {
        int totalQuantitySold = 0;
        for (Item item : items) {
            totalQuantitySold += item.trackSold;
        }
        System.out.println(totalQuantitySold);
        return totalQuantitySold;

    }


    public void DisplayInventory() {
        for(int i = 0; i < items.size(); i++) {
            System.out.println("\t" + i + " - " + items.get(i).name + " available: " + items.get(i).quantity + " units");
        }
    }
}
