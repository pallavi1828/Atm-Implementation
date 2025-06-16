import java.util.Scanner;

// Abstract base class for all account types
abstract class BankAccount {
    private String accountHolderName;
    private double balance;
    private int pin;

    public BankAccount(String accountHolderName, double balance, int pin) {
        this.accountHolderName = accountHolderName;
        this.balance = balance;
        this.pin = pin;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public boolean verifyPin(int enteredPin) {
        return this.pin == enteredPin;
    }

    public void setPin(int newPin) {
        this.pin = newPin;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Amount Deposited: ₹" + amount);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Amount Withdrawn: ₹" + amount);
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
    }

    public void displayDetails() {
        System.out.println("\n===== Account Summary =====");
        System.out.println("Account Holder: " + accountHolderName);
        System.out.println("Current Balance: ₹" + balance);
    }

    public abstract void accountTypeDetails();
}

// SavingsAccount subclass
class SavingsAccount extends BankAccount {
    public SavingsAccount(String accountHolderName, double balance, int pin) {
        super(accountHolderName, balance, pin);
    }

    @Override
    public void accountTypeDetails() {
        System.out.println("Account Type: Savings Account");
        System.out.println("Interest: 4% annually");
        System.out.println("Daily Withdrawal Limit: ₹25,000\n");
    }
}

// Main ATM simulation
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String accountHolder = "PALLAVI";
        int correctPin = 1234;
        double initialBalance = 10000.0;

        System.out.println("====== Welcome to the ATM Machine ======\n");
        System.out.println("Please insert your card...");

        // PIN verification
        int attempts = 0;
        boolean accessGranted = false;
        SavingsAccount savings = new SavingsAccount(accountHolder, initialBalance, correctPin);

        while (attempts < 3) {
            System.out.print("\nEnter your 4-digit PIN: ");
            int enteredPin = scanner.nextInt();

            if (savings.verifyPin(enteredPin)) {
                System.out.println("PIN verified successfully. Welcome, " + savings.getAccountHolderName() + "!");
                accessGranted = true;
                break;
            } else {
                attempts++;
                System.out.println("Invalid PIN. Attempt " + attempts + " of 3.");
            }
        }

        if (!accessGranted) {
            System.out.println("\n*** ALERT: Maximum PIN attempts reached. Please wait for some time before trying again. ***");
            System.out.println("Your card has been temporarily locked for security reasons.");
            return;
        }

        savings.accountTypeDetails();

        while (true) {
            System.out.println("\n====== ATM Main Menu ======");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Account Details");
            System.out.println("5. Change PIN");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Your current balance is: ₹" + savings.getBalance());
                    break;

                case 2:
                    System.out.print("Enter amount to deposit: ₹");
                    double depositAmount = scanner.nextDouble();
                    savings.deposit(depositAmount);
                    break;

                case 3:
                    System.out.print("Enter amount to withdraw: ₹");
                    double withdrawAmount = scanner.nextDouble();
                    if (withdrawAmount > 25000) {
                        System.out.println("Cannot withdraw more than ₹25,000 in a day (Savings Account Limit).");
                    } else {
                        savings.withdraw(withdrawAmount);
                    }
                    break;

                case 4:
                    savings.displayDetails();
                    break;

                case 5:
                    scanner.nextLine(); // consume newline
                    System.out.print("Enter OTP to verify identity (sent to your registered mobile): ");
                    String otp = scanner.nextLine();

                    int otpAttempts = 1;
                    while (!otp.equals("8989") && otpAttempts < 3) {
                        System.out.println("Incorrect OTP. Try again.");
                        System.out.print("Enter OTP: ");
                        otp = scanner.nextLine();
                        otpAttempts++;
                    }

                    if (otp.equals("8989")) {
                        System.out.print("Enter your new 4-digit PIN: ");
                        int newPin = scanner.nextInt();
                        savings.setPin(newPin);
                        System.out.println("PIN changed successfully.");
                    } else {
                        System.out.println("Failed OTP verification. PIN change cancelled.");
                    }
                    break;

                case 6:
                    System.out.println("\nThank you for using the ATM.");
                    System.out.println("Please take your card and receipt.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid selection. Please choose a valid option.");
            }

            scanner.nextLine(); // consume newline
            System.out.println("\nWould you like to perform another transaction? (yes/no): ");
            String repeat = scanner.nextLine();
            if (!repeat.equalsIgnoreCase("yes")) {
                System.out.println("Session ended. Thank you for banking with us.");
                break;
            }
        }

        scanner.close();
    }
}
