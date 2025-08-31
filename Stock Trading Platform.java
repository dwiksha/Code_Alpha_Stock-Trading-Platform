import java.util.*;


// --- Stock class ---
class Stock {
    String symbol;
    double price;

    Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    // update price randomly
    void updatePrice() {
        double change = (Math.random() - 0.5) * 10; // random -5 to +5
        price = Math.max(1, price + change); // price never < 1
    }
}

// --- Portfolio class ---
class Portfolio {
    double cash = 10000; // starting cash
    Map<String, Integer> holdings = new HashMap<>();

    void buy(Stock s, int qty) {
        double cost = s.price * qty;
        if (cash >= cost) {
            cash -= cost;
            holdings.put(s.symbol, holdings.getOrDefault(s.symbol, 0) + qty);
            System.out.println("Bought " + qty + " " + s.symbol);
        } else {
            System.out.println("Not enough cash!");
        }
    }

    void sell(Stock s, int qty) {
        int owned = holdings.getOrDefault(s.symbol, 0);
        if (owned >= qty) {
            holdings.put(s.symbol, owned - qty);
            cash += s.price * qty;
            System.out.println("Sold " + qty + " " + s.symbol);
        } else {
            System.out.println("Not enough shares!");
        }
    }

    void show(Map<String, Stock> market) {
        System.out.println("---- Portfolio ----");
        System.out.println("Cash: $" + cash);
        double value = cash;
        for (var e : holdings.entrySet()) {
            Stock s = market.get(e.getKey());
            int qty = e.getValue();
            double holdingValue = qty * s.price;
            System.out.println(s.symbol + ": " + qty + " shares @ $" + s.price + " = $" + holdingValue);
            value += holdingValue;
        }
        System.out.println("Total Value: $" + value);
    }
}

// --- Main Trading Platform ---
public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Market data
        Map<String, Stock> market = new HashMap<>();
        market.put("AAPL", new Stock("AAPL", 150));
        market.put("GOOG", new Stock("GOOG", 2800));
        market.put("TSLA", new Stock("TSLA", 700));

        Portfolio pf = new Portfolio();

        while (true) {
            // update prices
            for (Stock s : market.values()) s.updatePrice();

            // show menu
            System.out.println("\n=== Stock Trading Platform ===");
            for (Stock s : market.values()) {
                System.out.println(s.symbol + ": $" + String.format("%.2f", s.price));
            }
            System.out.println("1. Buy  2. Sell  3. Portfolio  0. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();

            if (choice == 0) break;
            switch (choice) {
                case 1 -> {
                    System.out.print("Symbol: ");
                    String sym = sc.next().toUpperCase();
                    System.out.print("Quantity: ");
                    int qty = sc.nextInt();
                    if (market.containsKey(sym)) pf.buy(market.get(sym), qty);
                    else System.out.println("Stock not found!");
                }
                case 2 -> {
                    System.out.print("Symbol: ");
                    String sym = sc.next().toUpperCase();
                    System.out.print("Quantity: ");
                    int qty = sc.nextInt();
                    if (market.containsKey(sym)) pf.sell(market.get(sym), qty);
                    else System.out.println("Stock not found!");
                }
                case 3 -> pf.show(market);
                default -> System.out.println("Invalid choice!");
            }
        }
        sc.close();
    }
}
