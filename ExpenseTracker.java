import java.io.*;
import java.util.*;

public class ExpenseTracker {
    private static final String FILE_NAME = "expenses.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Expense Tracker ---");
            System.out.println("1. Add Expense");
            System.out.println("2. View Total Expense");
            System.out.println("3. View Expense by Category");
            System.out.println("4. View Highest Spending Day");
            System.out.println("5. View All Expenses");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addExpense(sc);
                case 2 -> showTotalExpense();
                case 3 -> showCategoryWise();
                case 4 -> showHighestSpendingDay();
                case 5 -> showAllExpenses();
                case 6 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addExpense(Scanner sc) {
        try {
            System.out.print("Enter date (yyyy-mm-dd): ");
            String date = sc.nextLine();

            System.out.print("Enter category: ");
            String category = sc.nextLine();

            System.out.print("Enter amount: ");
            double amount = sc.nextDouble();

            Expense exp = new Expense(date, category, amount);
            FileWriter fw = new FileWriter(FILE_NAME, true);
            fw.write(exp.toString() + "\n");
            fw.close();
            System.out.println("Expense added successfully.");

        } catch (Exception e) {
            System.out.println("Error adding expense.");
        }
    }

    private static List<Expense> readExpenses() {
        List<Expense> list = new ArrayList<>();
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) return list;

            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    list.add(new Expense(parts[0], parts[1], Double.parseDouble(parts[2])));
                }
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Error reading file.");
        }
        return list;
    }

    private static void showTotalExpense() {
        List<Expense> list = readExpenses();
        double total = 0;
        for (Expense e : list) total += e.getAmount();
        System.out.println("Total Expense: " + total);
    }

    private static void showCategoryWise() {
        List<Expense> list = readExpenses();
        Map<String, Double> map = new HashMap<>();
        for (Expense e : list) {
            map.put(e.getCategory(), map.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
        }
        System.out.println("Category-wise Expense:");
        for (String c : map.keySet()) System.out.println(c + " -> " + map.get(c));
    }

    private static void showHighestSpendingDay() {
        List<Expense> list = readExpenses();
        Map<String, Double> map = new HashMap<>();
        for (Expense e : list) {
            map.put(e.getDate(), map.getOrDefault(e.getDate(), 0.0) + e.getAmount());
        }

        String maxDay = null;
        double maxAmount = 0;
        for (String d : map.keySet()) {
            if (map.get(d) > maxAmount) {
                maxAmount = map.get(d);
                maxDay = d;
            }
        }

        if (maxDay != null)
            System.out.println("Highest Spending Day: " + maxDay + " (Amount: " + maxAmount + ")");
        else
            System.out.println("No expenses recorded.");
    }

    private static void showAllExpenses() {
        List<Expense> list = readExpenses();
        if (list.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }
        System.out.println("All Expenses:");
        for (Expense e : list)
            System.out.println(e.getDate() + " | " + e.getCategory() + " | " + e.getAmount());
    }
}
