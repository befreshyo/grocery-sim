import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

/* 
bill processor does all the calculations for the program
it will go through each item that was purchased and apply the discount if applicable
it prints each item that was purchased with the quanity, original price, discount, and discounted price
it then prints the total and total discount amount
 */

class BillProcessor {
	public static void prepareBill(LinkedHashMap<String, Integer> purchases, LinkedHashMap<String,Double>
		items, LinkedHashMap<String,String> sales, ArrayList<String> itemNames) {
        String deal;                                                                        // bogo or amt of discount
        double quantity;                                                                    // quantity
        double totalBill = 0;                                                               // total bill amt
		double totalDiscount = 0;                                                           // total discount amt
        double discount = 0;                                                                // discount amt with quantity
        double ogItemCost;                                                                  // original item cost
        double itemPrice = 0;                                                               // item price with quantity
        
        for (String item: purchases.keySet()) {                                             //going through items in purchases
            discount = 0;
            ogItemCost = items.get(item);
            quantity = purchases.get(item);
            deal = sales.get(item);

            itemPrice = ogItemCost * quantity;                                              //determines item price per quanity 
            
			if(sales.containsKey(item)){                                                    //if item is a sales item 
                if (deal.equals("bogo")){                                                   //if sale is a bogo
                    discount = ((int) quantity / 2) * ogItemCost;
                    totalDiscount = totalDiscount + discount;
                    itemPrice = itemPrice - discount;
                    totalBill = totalBill + itemPrice;                                    
                } else {                                                                    //if sale is a discounted price 
                    discount = Double.parseDouble(deal) * quantity;
                    totalDiscount = totalDiscount + discount;
                    itemPrice = itemPrice - discount;
                    totalBill = totalBill + itemPrice;}
            }else {
                totalBill = totalBill + itemPrice;}
            
            if (discount > 0){                                                              //if discount amount is greater than $0 then print the discount statement  
                System.out.printf("%.0f %s, regularly $%.2f each, total $%.2f ($%.2f discount)\n", quantity, item, ogItemCost, itemPrice ,discount);
            } else {                                                                        //else print regular statement without discount   
            System.out.printf("%.0f %s, regularly $%.2f each, total $%.2f\n", quantity, item, ogItemCost, itemPrice );}
        }
        System.out.printf("\nYour total bill is $%.2f.\n",totalBill);
        System.out.printf("You saved $%.2f by shopping with us today.\n",totalDiscount);
	}
}

/* 
main class - starts with declaring the hashmaps for storing items, sales, and purchases 
 */

public class GrataGroceryStore {
    private static LinkedHashMap<String, Double> items;                                     //storage for items + costs
    private static LinkedHashMap<String, String> sales;                                     //storage for items + sale description
    private static LinkedHashMap<String, Integer> purchases;                                //storage for items + their quantities
    private static ArrayList<String> itemNames;
    
/* 
this will allow the items.txt file to be oppened and each line is read, then split by tab, and the items and their prices are stored
 */

    public static LinkedHashMap<String, Double> readItemsFromFile(String fname) {
        LinkedHashMap<String,Double> result = new LinkedHashMap<String,Double>();
        String line;
        String[] parts;
        try {
                itemNames = new ArrayList<String>();
                Scanner fsc = new Scanner(new File(fname));
                while (fsc.hasNextLine()){
                    line = fsc.nextLine();                                                  //reads file by line 
                    parts = line.split("\t");                                               //splits each words by tabs
                    result.put(parts[0].toLowerCase(),Double.parseDouble(parts[1]));        //adds items/prices into hashmap and converts to lowercase
                    itemNames.add(parts[0].toLowerCase());                                  //add items to arraylist and converts to lowercase
                } fsc.close();
                Collections.sort(itemNames);
                return result;
        }catch (Exception ex) {
            System.out.println("File did not open.");
            
        }
            return result;
    }

/* 
this will allow the sales.txt file to be oppened and each line is read, then split by tab, and the items and their sales are stored
 */

    public static LinkedHashMap<String, String> readSalesFromFile(String fname) {
        LinkedHashMap<String,String> result = new LinkedHashMap<String,String>();
        String line;
        String[] parts;
        try {
                Scanner fsc = new Scanner(new File(fname));
                while (fsc.hasNextLine()){
                    line = fsc.nextLine();                                                  //reads file by line 
                    parts = line.split("\t");                                               //splits each words by tabs
                    result.put(parts[0].toLowerCase(),parts[1]);                            //adds items/sales into hashmap and converts to lowercase
                } fsc.close();
                return result;
        }catch (Exception ex) {
            System.out.println("File did not open.");
            
        }
            return result;
    }

/* 
this will show the menu of items
displays by # value, item name, regular price, and sales price 
 */

    public static void presentMenuOfItems() {
        int lineNum = 0;
        System.out.printf("##   %-20s %8s  Sale\n","Item Name", "Reg.");
        System.out.println("----------------------------------------------------");
        for (String itemName: itemNames){
            ++lineNum;                                                                      //added the line numbers with this
            if (sales.get(itemName) == null){
                System.out.printf("%2s   %-20s %8.2f\n",lineNum,itemName,items.get(itemName),sales.get(itemName));
            } else if (sales.get(itemName).equals("bogo")){
                System.out.printf("%2s   %-20s %8.2f  Buy One, Get One\n",lineNum,itemName,items.get(itemName),sales.get(itemName));
            } else {
                System.out.printf("%2s   %-20s %8.2f  $%-4s discount\n",lineNum,itemName,items.get(itemName),sales.get(itemName));
            }
        }   System.out.println("----------------------------------------------------\n");
    }

/* 
this will print the welcome and intro
 */

	public static void printWelcome() {
		System.out.println("""
        ***************************************************************
        *                       CHARLIE'S PANTRY                      *
        ***************************************************************
        
        Welcome to your friendly neighborhood Charlie's Pantry. We sell
        only the highest quality groceries and freshest produce around.
        We have many great specials this week. The more you buy, the
        more you save!

        What would you like to buy?\n""");     
	}	

/* 
main function of the main class
sends items.txt and sales.txt to the function tha reads the files
prints welcome
presents menu of items
asks the user for their choice and continues asking until user enters 'q' to quit
for each purchase, it will add that item to a hash map
sends the purchases, items, sales, and item names to the prepare bill function
prints what was purchased and the total
prints the goodbye statement 
 */

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String choice;

        items = readItemsFromFile("items.txt");                                                 //declares items
        sales = readSalesFromFile("sales.txt");                                                 //declares sales
        purchases = new LinkedHashMap<String,Integer>();                                        //declares purchases

        printWelcome();                                                                         //prints welcome
        presentMenuOfItems();                                                                   //prints item menu
        do {
            System.out.print("Enter the number of your choice, or q to check out: ");
	    choice = sc.next();                                                                 //finds choice from user
            if (!choice.equals("q")) { 
                String itemName = (itemNames.get(Integer.parseInt(choice)-1));
                if (purchases.containsKey(itemName)) {                                          //adds each purchase and the quantity of item to the hashmap
                    purchases.put(itemName, purchases.get(itemName)+1);
                } else {
                    purchases.put(itemName, 1);
                }
                purchases.put(itemName, purchases.get(itemName));
            }
        } while (!choice.equals("q"));
        System.out.println("\nHere is what you purchased:");                                        
        BillProcessor.prepareBill(purchases, items, sales, itemNames);                          //prints what was purchased and the total
        System.out.println("\nThank you for your business. Come back soon!");   
        sc.close();
    }
}
