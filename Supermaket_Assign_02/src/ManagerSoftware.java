import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ManagerSoftware {
    Warehouse warehouse;
    User user;

    public ManagerSoftware(Warehouse warehouse, User user){
        this.warehouse = warehouse;
        this.user = user;
    }
    public void run(){
        int option;
        Scanner scanner = new Scanner(System.in);
        String startTime = GetTime();
        String lastTimeForPay = startTime;
        int index = 0;
        String indexString = "";
        int totalAmountPaidToEmployees = 0;

        System.out.println("Welcome! Logged in at " + startTime + "\n");

        do{
            System.out.println("************ Manager Software ************\n");

            //log time
            System.out.println("\tIt's currently " + startTime);

            //bought
            System.out.print("\tBought items: " );
            user.DisplayToTalBoughtItem();

            // sold
            System.out.print("\tSold items: " );
            user.DisplayToTalSoldItem();

            // total cost
            System.out.println("\tCost of all purchased items: $" + String.format("%.2f", user.totalExpense));

            // total payments
            System.out.println("\tCost of employee payment: $" + totalAmountPaidToEmployees);

            //expenses
            System.out.println("\tTotal expenses: $" + String.format("%.2f", (user.totalExpense + totalAmountPaidToEmployees)));

            // earning
            System.out.println("\tTotal sales: $" + String.format("%.2f", user.totalEarning));

            //Net profit
            double netIncome = user.totalEarning - (user.totalExpense + totalAmountPaidToEmployees);
            if(netIncome >= 0) {
                System.out.println("\tNet profit: $" + String.format("%.2f", netIncome));
            } else {
                System.out.println("\tNet loss: $" + String.format("%.2f", Math.abs(netIncome)));
            }

            System.out.println();
            System.out.println("\t\t1 - Item specifics");
            System.out.println("\t\t2 - Pay employee");
            System.out.println("\t\t3 - Sell");
            System.out.println("\t\t4 - Buy");
            System.out.println("\t\t5 - Quit");
            System.out.print("\n\tPlease enter your choice: ");

            try{
                if (scanner.hasNextInt()) {
                    option = scanner.nextInt();
                    scanner.nextLine();
                } else {
                    scanner.nextLine();
                    option = 0;
                }
            }catch (Exception ex){
                option = 0;
            }

            String temp;

            switch (option){

                case 1:
                    System.out.println("\n************ Sold and Bought Items ************\n");
                    user.DisplayInventory();

                    do {
                        System.out.print("\n\tEnter index of item to be tracked: ");
                        index = scanner.nextInt();
                    } while(!CheckIndexOfUserInventory(user, index));

                    System.out.println("\n\tSold units: " + user.items.get(index).trackSold);
                    System.out.println("\tBought units: " + user.items.get(index).trackBought);
                    temp = scanner.nextLine();
                    System.out.println("\nPress enter to return");
                    temp = scanner.nextLine();
                    break;

                case 2:

                    int[] arrOfIntOfLastTimeForPay=ConvertTimeToInt(lastTimeForPay.split(":"));
                    String currentTime=GetTime();
                    int[] arrOfIntOfCurrentTime= ConvertTimeToInt(currentTime.split(":"));
                    int hoursElapsed=arrOfIntOfCurrentTime[0]-arrOfIntOfLastTimeForPay[0];
                    int minutesElapsed=arrOfIntOfCurrentTime[1]-arrOfIntOfLastTimeForPay[1];
                    int amountToPayEmployees=((hoursElapsed*60)+minutesElapsed)*2600;
                    totalAmountPaidToEmployees+=amountToPayEmployees;
                    System.out.println("\n\tYou were charged $"+ amountToPayEmployees + " for a duration of " + ((hoursElapsed*60)+minutesElapsed) + " minute(s)");
                    lastTimeForPay=currentTime;

                    System.out.println("\nPress enter to return");
                    temp = scanner.nextLine();
                    break;
                case 3:

                    System.out.println("\n\n************** Selling item ************** ");
                    System.out.println("\n----------- Your Inventory -----------\n");
                    user.DisplayInventory();
                    boolean validInput = false;
                    while (!validInput) {
                        System.out.print("\n\tPlease enter index/name you wish to sell: ");
                        indexString = scanner.next();
                        index = -1;

                        try {
                            index = Integer.parseInt(indexString);
                            if (CheckIndexOfUserInventory(user, index)) {
                                validInput = true;
                            } else {
                                System.err.print("\n\tInvalid index. Try again ");
                            }
                        } catch (Exception e) {
                            boolean itemFound = false;
                            for (int i = 0; i < user.items.size(); i++) {
                                if (user.items.get(i).name.equalsIgnoreCase(indexString)) {
                                    index = i;
                                    itemFound = true;
                                    break;
                                }
                            }
                            if (itemFound) {
                                validInput = true;
                            } else {
                                System.err.print("\n\tInvalid item name. Try again ");
                            }
                        }
                    }

                    int quantity = -1;
                    boolean isZero = false;
                    do {
                        System.out.print("\n\tPlease enter quantity of item you wish to sell: ");

                        try {
                            quantity = scanner.nextInt();

                        } catch (Exception ex) {
                            scanner.nextLine();
                            continue;
                        }
                        if (user.items.get(index).quantity == 0) {
                            System.out.println("\n\tSorry. Item is out of stock to sell!");
                            quantity = 0;
                            isZero = true;
                            break;
                        }
                    }  while (!CheckQuantityOfUserInventory(user, index, quantity));

                    double price;

                    if (!isZero) {

                        int warehouseIndex = getWarehouseIndexByName(warehouse, user.items.get(index).name);

                        if (warehouseIndex != -1) {
                            System.out.println("\n\tWarehouse sells 1 " + warehouse.items.get(warehouseIndex).name + " for $" + warehouse.items.get(warehouseIndex).price);
                        }

                        System.out.print("\n\tPlease enter the price of an item you wish to sell: ");
                        price = scanner.nextDouble();

                        // Handle user sell item <= 0
                        while (price <= 0) {
                            System.out.print("\tPlease enter the price > 0: ");
                            price = scanner.nextDouble();
                        }

                        boolean confirmed = false;
                        while (!confirmed) {
                            // Handle user entered price that's larger by more than 50% compared to the current price of that item selling by warehouse.
                            if (price > warehouse.items.get(warehouseIndex).price * 1.50) {
                                System.out.print("\tYour selling price is over 50% higher than the warehouse price, try entering a cheaper price: ");
                                price = scanner.nextDouble();
                            }

                            // Getting confirmation from user for selling item cheaper or equal to warehouse price
                            else if (price <= warehouse.items.get(warehouseIndex).price) {
                                System.out.print("\n\tYou sell equal or cheaper than warehouse price. Press 'y' to confirm or 'n' to enter a new price: ");
                                char confirmation = scanner.next().charAt(0);
                                if (Character.toLowerCase(confirmation) == 'y') {
                                    confirmed = true;
                                } else {
                                    System.out.print("\n\tPlease enter the price of an item you wish to sell: ");
                                    price = scanner.nextDouble();
                                }
                            }

                            // If the price is within the acceptable range
                            else {
                                confirmed = true;
                            }
                        }

                        user.SellItem(user.items.get(index).name, price, quantity);
                        warehouse.BuyItem(user.items.get(index).name, quantity);

                        System.out.println("\n****** Updated Warehouse Inventory ******");
                        warehouse.DisplayWarehouse();

                        System.out.println("\n***** Updated User Inventory ******");
                        user.DisplayInventory();

                        temp = scanner.nextLine();
                        System.out.println("\nPress enter to return");
                        temp = scanner.nextLine();
                        break;
                    }

                    temp = scanner.nextLine();
                    System.out.println("\nPress enter to return");
                    temp = scanner.nextLine();

                    break;

                //buy
                case 4:
                    System.out.println("\n************** Buying item **************");
                    System.out.println("\n----------- Warehouse Inventory -----------\n");
                    warehouse.DisplayWarehouse();

                    validInput = false;
                    while(!validInput) {
                        System.out.print("\n\tPlease enter index/name you wish to buy: ");
                        indexString = scanner.next();
                        index = -1;

                        try {
                            index = Integer.parseInt(indexString);
                            if(CheckIndexOfWarehouse(warehouse, index)) {
                                validInput = true;
                            } else
                            {
                                System.err.print("Invalid index. Try again ");
                            }
                        } catch(Exception e) {
                            boolean itemFound = false;
                            for(int i = 0; i < warehouse.items.size(); i++) {
                                if(warehouse.items.get(i).name.equalsIgnoreCase(indexString)) {
                                    index = i;
                                    itemFound = true;
                                    break;
                                }

                            }
                            if(itemFound) {
                                validInput = true;
                            } else {
                                System.err.print("Invalid item name. Try again ");
                            }
                        }
                    }

                    quantity = -1;
                    boolean isZero2 = false;
                    do {
                        System.out.print("\n\tPlease enter quantity of item you wish to buy: ");
                        //handling string
                        try {
                            quantity = scanner.nextInt();

                        } catch (Exception ex) {
                            scanner.nextLine();
                            continue;
                        }

                        if(warehouse.items.get(index).quantity == 0) {
                            System.out.println("\n\tSorry. Item is out of stock to buy!");
                            quantity = 0;
                            isZero2 = true;
                            break;
                        }
                    } while(!CheckQuantityOfWarehouse(warehouse,index, quantity));


                    if(!isZero2){

                        warehouse.SellItem(warehouse.items.get(index).name, quantity);
                        user.BuyItem(warehouse.items.get(index).name, warehouse.items.get(index).price, quantity);

                        //print
                        System.out.println("\n****** Updated Warehouse Inventory ******");
                        warehouse.DisplayWarehouse();

                        System.out.println("\n****** Updated User Inventory ******");
                        user.DisplayInventory();
                        System.out.println();
                    }
                    temp = scanner.nextLine();
                    System.out.println("\nPress enter to return");
                    temp = scanner.nextLine();
                    break;

                case 5:
                    System.out.println("\n\tThank you for using our program!");
                    System.exit(5);
                    break;

                default:
                    System.err.println("\tInvalid choice. Please try again\n");
                    System.out.println("\nPress enter to return");
                    temp = scanner.nextLine();
                    break;
            }

        } while(option != 5);
    }

    public static int[] ConvertTimeToInt(String[] arrOfStr){
        int[] arrOfInt=new int[2];
        int i=0;
        for(String part : arrOfStr){
            arrOfInt[i]=Integer.parseInt(part); i++;
        }

        return arrOfInt;
    }

    public static String GetTime(){
        LocalTime myObj = LocalTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm");

        String formattedDate = myObj.format(myFormatObj);
        return formattedDate;
    }

    public static boolean CheckIndexOfWarehouse(Warehouse warehouse,int index) {

        return !(index < 0 || index >= warehouse.items.size()); //false is invalid

    }
    public static boolean CheckQuantityOfWarehouse(Warehouse warehouse,int index, int quantity) {
        if (quantity < 0 || quantity > warehouse.items.get(index).quantity) {
            System.err.print("\tInvalid quantity. Try again ");
            return false;
        }
        return true;
    }
    public static boolean CheckIndexOfUserInventory(User user,int index) {

        return !(index < 0 || index >= user.items.size()); //false is invalid

    }
    public static boolean CheckQuantityOfUserInventory(User user, int index, int quantity) {
        if (quantity < 0 || quantity > user.items.get(index).quantity) {
            System.err.print("\tInvalid quantity. Try again ");
            return false;
        }
        return true;
    }

    public static int getWarehouseIndexByName(Warehouse warehouse, String itemName) {
        for (int i = 0; i < warehouse.items.size(); i++) {
            if (warehouse.items.get(i).name.equalsIgnoreCase(itemName)) {
                return i; // return the index if the item is found
            }
        }
        return -1; // return -1 if the item is not found
    }

}
