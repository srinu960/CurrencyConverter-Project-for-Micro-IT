import java.util.*;
import java.text.DecimalFormat;

public class CurrencyConverter {
    private static final Map<String, Double> exchangeRates = new HashMap<>();
    private static final DecimalFormat df = new DecimalFormat("#,##0.00");
    private static final Scanner scanner = new Scanner(System.in);

    static {
        exchangeRates.put("USD", 1.00);
        exchangeRates.put("EUR", 0.93);
        exchangeRates.put("GBP", 0.79);
        exchangeRates.put("JPY", 151.50);
        exchangeRates.put("INR", 83.30);
        exchangeRates.put("AUD", 1.52);
        exchangeRates.put("CAD", 1.37);
        exchangeRates.put("CNY", 7.24);
        exchangeRates.put("CHF", 0.91);
        exchangeRates.put("MXN", 16.75);
    }

    public static void main(String[] args) {
        System.out.println("Currency Converter (Manual Rates)");
        System.out.println("---------------------------------");

        while (true) {
            try {
                displayMainMenu();
                int choice = getIntegerInput("Enter your choice (1-4): ", 1, 4);

                switch (choice) {
                    case 1:
                        performConversion();
                        break;
                    case 2:
                        displayAllCurrencies();
                        break;
                    case 3:
                        updateExchangeRate();
                        break;
                    case 4:
                        System.out.println("Exiting currency converter. Goodbye!");
                        scanner.close();
                        return;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); // Clear buffer
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Convert Currency");
        System.out.println("2. View All Supported Currencies");
        System.out.println("3. Update Exchange Rate");
        System.out.println("4. Exit");
    }

    private static void performConversion() {
        System.out.println("\nCurrency Conversion");
        System.out.println("-------------------");

        double amount = getDoubleInput("Enter amount to convert: ");
        String fromCurrency = getCurrencyInput("Enter source currency code: ");
        String toCurrency = getCurrencyInput("Enter target currency code: ");

        double convertedAmount = convert(amount, fromCurrency, toCurrency);
        double exchangeRate = exchangeRates.get(toCurrency) / exchangeRates.get(fromCurrency);

        System.out.println("\nConversion Result:");
        System.out.println(df.format(amount) + " " + fromCurrency + " = " +
                df.format(convertedAmount) + " " + toCurrency);
        System.out.printf("Exchange Rate: 1 %s = %.6f %s\n",
                fromCurrency, exchangeRate, toCurrency);
    }

    private static double convert(double amount, String fromCurrency, String toCurrency) {
        // Convert to USD first, then to target currency
        double amountInUSD = amount / exchangeRates.get(fromCurrency);
        return amountInUSD * exchangeRates.get(toCurrency);
    }

    private static void displayAllCurrencies() {
        System.out.println("\nSupported Currencies:");
        System.out.println("Code\tCurrency Name\t\tRate per USD");
        System.out.println("----\t-------------\t\t-----------");

        // Sort currencies alphabetically by code
        exchangeRates.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String code = entry.getKey();
                    String name = getCurrencyName(code);
                    System.out.printf("%-4s\t%-20s\t%-10.4f\n",
                            code, name, entry.getValue());
                });
    }

    private static String getCurrencyName(String code) {
        // Map currency codes to full names
        Map<String, String> currencyNames = Map.of(
                "USD", "US Dollar",
                "EUR", "Euro",
                "GBP", "British Pound",
                "JPY", "Japanese Yen",
                "INR", "Indian Rupee",
                "AUD", "Australian Dollar",
                "CAD", "Canadian Dollar",
                "CNY", "Chinese Yuan",
                "CHF", "Swiss Franc",
                "MXN", "Mexican Peso"
        );
        return currencyNames.getOrDefault(code, "Unknown Currency");
    }

    private static void updateExchangeRate() {
        System.out.println("\nUpdate Exchange Rate");
        System.out.println("--------------------");

        String currency = getCurrencyInput("Enter currency code to update: ");
        double newRate = getDoubleInput("Enter new exchange rate (per USD): ");

        if (newRate <= 0) {
            throw new IllegalArgumentException("Exchange rate must be positive");
        }

        exchangeRates.put(currency, newRate);
        System.out.println("Exchange rate for " + currency + " updated successfully.");
    }

    private static String getCurrencyInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.next().toUpperCase();
            if (exchangeRates.containsKey(input)) {
                return input;
            }
            System.out.println("Invalid currency code. Supported codes are: " +
                    String.join(", ", exchangeRates.keySet()));
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = scanner.nextDouble();
                if (value < 0) {
                    System.out.println("Please enter a positive number");
                    continue;
                }
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Clear invalid input
            }
        }
    }

    private static int getIntegerInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                if (value < min || value > max) {
                    System.out.printf("Please enter a number between %d and %d\n", min, max);
                    continue;
                }
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next();
            }
        }
    }
}