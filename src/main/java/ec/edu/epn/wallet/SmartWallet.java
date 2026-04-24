package ec.edu.epn.wallet;

/**
 * Billetera digital con reglas de negocio sobre depósitos y retiros.
 *
 * Reglas:
 *  - Los depósitos deben ser positivos.
 *  - Usuarios "Standard" no pueden superar un saldo de $5,000.
 *  - Depósitos mayores a $100 reciben 1% de cashback.
 *  - Los retiros deben ser positivos y no mayores al saldo.
 *  - Si el saldo llega a 0 tras un retiro, la cuenta queda Inactiva.
 */
public class SmartWallet {
 
    public static final double MAX_STANDARD_BALANCE = 5000.0;
    public static final double CASHBACK_THRESHOLD = 100.0;
    public static final double CASHBACK_RATE = 0.01;
 
    private static final double EPSILON = 0.0001;
    private static final String STATUS_ACTIVE = "Activa";
    private static final String STATUS_INACTIVE = "Inactiva";
 
    private double balance;
    private final String userType;
    private String status;
 
    public SmartWallet(String userType) {
        this(userType, 0.0);
    }
 
    public SmartWallet(String userType, double initialBalance) {
        if (userType == null || userType.isBlank()) {
            throw new IllegalArgumentException("El tipo de usuario no puede ser nulo o vacio");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo");
        }
        this.userType = userType;
        this.balance = initialBalance;
        this.status = STATUS_ACTIVE;
    }
 
    /**
     * Realiza un deposito aplicando las reglas de negocio.
     *
     * @param amount monto a depositar
     * @return true si el deposito fue exitoso, false en caso contrario
     */
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
 
        double cashback = (amount > CASHBACK_THRESHOLD) ? amount * CASHBACK_RATE : 0.0;
        double newBalance = balance + amount + cashback;
 
        if (isStandard() && newBalance > MAX_STANDARD_BALANCE) {
            return false;
        }
 
        this.balance = newBalance;
        this.status = STATUS_ACTIVE;
        return true;
    }
 
    /**
     * Realiza un retiro aplicando las reglas de negocio.
     *
     * @param amount monto a retirar
     * @return true si el retiro fue exitoso, false en caso contrario
     */
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }
        if (amount > balance) {
            return false;
        }
 
        this.balance -= amount;
 
        // Comparacion con epsilon para manejar imprecision de punto flotante
        if (Math.abs(this.balance) < EPSILON) {
            this.balance = 0.0;
            this.status = STATUS_INACTIVE;
        }
 
        return true;
    }
 
    private boolean isStandard() {
        return "Standard".equalsIgnoreCase(userType);
    }
 
    public double getBalance() {
        return balance;
    }
 
    public String getUserType() {
        return userType;
    }
 
    public String getStatus() {
        return status;
    }
 
    public boolean isActive() {
        return STATUS_ACTIVE.equals(status);
    }
}
