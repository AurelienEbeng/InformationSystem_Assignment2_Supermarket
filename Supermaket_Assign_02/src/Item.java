import java.util.Random;

public class Item {
    String name;
    double price;
    int quantity;
    int trackBought = 0;
    int trackSold = 0;

    public Item(String name, double highestPrice, int highestQuantity) {
        this(name, highestPrice, highestQuantity, true);
    }

    public Item(String name, double highestPrice, int highestQuantity, boolean randomize) {
        Random random = new Random();
        double price = random.nextDouble() * (highestPrice - (highestPrice * 0.9)) + (highestPrice * 0.9);
        int quantity = random.nextInt(highestQuantity + 1 - (highestQuantity / 2));

        String formattedPrice = String.format("%.2f", price);

        this.name = name;
        this.price = Double.parseDouble(formattedPrice);

        if (randomize) {
            this.quantity = quantity;
        } else {
            this.quantity = highestQuantity;
            this.trackBought = highestQuantity;
        }
    }
}
