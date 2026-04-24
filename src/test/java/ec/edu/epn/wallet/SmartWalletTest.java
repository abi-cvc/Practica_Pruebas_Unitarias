package ec.edu.epn.wallet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ec.edu.epn.wallet.SmartWallet;

import static org.junit.jupiter.api.Assertions.*;
 
@DisplayName("Pruebas unitarias de SmartWallet")
class SmartWalletTest {
 
    private static final double DELTA = 0.0001;
 
    private SmartWallet wallet;
 
    @BeforeEach
    void setUp() {
        // Se inicializa una billetera Standard vacia antes de cada test
        wallet = new SmartWallet("Standard");
    }
 
    // ---------- Camino feliz ----------
 
    @Test
    @DisplayName("Deposito valido suma al saldo y mantiene la cuenta activa")
    void testDepositoValido() {
        boolean resultado = wallet.deposit(50.0);
 
        assertTrue(resultado, "El deposito deberia ser exitoso");
        assertEquals(50.0, wallet.getBalance(), DELTA);
        assertTrue(wallet.isActive());
    }
 
    @Test
    @DisplayName("Retiro valido resta del saldo correctamente")
    void testRetiroValido() {
        wallet.deposit(200.0); // 200 + 2 de cashback = 202
 
        boolean resultado = wallet.withdraw(50.0);
 
        assertTrue(resultado);
        assertEquals(152.0, wallet.getBalance(), DELTA);
        assertTrue(wallet.isActive());
    }
 
    // ---------- Limites (valores borde) ----------
 
    @Test
    @DisplayName("Deposito de exactamente $100 NO genera cashback")
    void testDepositoExacto100NoGeneraCashback() {
        boolean resultado = wallet.deposit(100.0);
 
        assertTrue(resultado);
        // Debe ser exactamente 100, no 101
        assertEquals(100.0, wallet.getBalance(), DELTA);
    }
 
    @Test
    @DisplayName("Deposito mayor a $100 aplica 1% de cashback")
    void testDepositoMayorA100AplicaCashback() {
        boolean resultado = wallet.deposit(200.0);
 
        assertTrue(resultado);
        // 200 + (200 * 0.01) = 202
        assertEquals(202.0, wallet.getBalance(), DELTA);
    }
 
    @Test
    @DisplayName("Saldo Standard de exactamente $5,000 es permitido")
    void testSaldoExacto5000EsPermitido() {
        SmartWallet casiLleno = new SmartWallet("Standard", 4900.0);
 
        // $100 no genera cashback, asi que el saldo final sera exactamente 5000
        boolean resultado = casiLleno.deposit(100.0);
 
        assertTrue(resultado);
        assertEquals(5000.0, casiLleno.getBalance(), DELTA);
    }
 
    @Test
    @DisplayName("Deposito que excederia $5,000 en Standard es rechazado")
    void testDepositoExcede5000EsRechazado() {
        SmartWallet casiLleno = new SmartWallet("Standard", 4900.0);
 
        // 4900 + 101 + 1.01 (cashback) = 5002.01 -> excede
        boolean resultado = casiLleno.deposit(101.0);
 
        assertFalse(resultado, "Se deberia rechazar por exceder 5000");
        assertEquals(4900.0, casiLleno.getBalance(), DELTA);
    }
 
    @Test
    @DisplayName("Usuario Premium no tiene tope de $5,000")
    void testUsuarioPremiumSuperaTopeStandard() {
        SmartWallet premium = new SmartWallet("Premium", 4900.0);
 
        // 4900 + 1000 + 10 (cashback) = 5910
        boolean resultado = premium.deposit(1000.0);
 
        assertTrue(resultado);
        assertEquals(5910.0, premium.getBalance(), DELTA);
    }
 
    // ---------- Casos de error ----------
 
    @Test
    @DisplayName("Deposito negativo es rechazado")
    void testDepositoNegativoRechazado() {
        boolean resultado = wallet.deposit(-50.0);
 
        assertFalse(resultado);
        assertEquals(0.0, wallet.getBalance(), DELTA);
    }
 
    @Test
    @DisplayName("Deposito de cero es rechazado")
    void testDepositoCeroRechazado() {
        boolean resultado = wallet.deposit(0.0);
 
        assertFalse(resultado);
        assertEquals(0.0, wallet.getBalance(), DELTA);
    }
 
    @Test
    @DisplayName("Retiro negativo es rechazado y no altera el saldo")
    void testRetiroNegativoRechazado() {
        wallet.deposit(500.0); // saldo: 505
 
        boolean resultado = wallet.withdraw(-100.0);
 
        assertFalse(resultado);
        assertEquals(505.0, wallet.getBalance(), DELTA);
    }
 
    @Test
    @DisplayName("Retiro de cero es rechazado")
    void testRetiroCeroRechazado() {
        wallet.deposit(200.0); // saldo: 202
 
        boolean resultado = wallet.withdraw(0.0);
 
        assertFalse(resultado);
        assertEquals(202.0, wallet.getBalance(), DELTA);
    }
 
    @Test
    @DisplayName("Retiro mayor al saldo disponible es rechazado")
    void testRetiroMayorAlSaldoRechazado() {
        wallet.deposit(50.0); // saldo: 50 (sin cashback)
 
        boolean resultado = wallet.withdraw(100.0);
 
        assertFalse(resultado);
        assertEquals(50.0, wallet.getBalance(), DELTA);
    }
 
    // ---------- Reglas especiales ----------
 
    @Test
    @DisplayName("Cuando el saldo llega a 0, la cuenta se marca Inactiva")
    void testSaldoCeroMarcaCuentaInactiva() {
        wallet.deposit(50.0); // saldo: 50
 
        boolean resultado = wallet.withdraw(50.0);
 
        assertTrue(resultado);
        assertEquals(0.0, wallet.getBalance(), DELTA);
        assertFalse(wallet.isActive(), "La cuenta deberia estar Inactiva");
        assertEquals("Inactiva", wallet.getStatus());
    }
 
    @Test
    @DisplayName("Retiro parcial NO cambia el estado a Inactiva")
    void testRetiroParcialMantieneCuentaActiva() {
        wallet.deposit(50.0);
 
        boolean resultado = wallet.withdraw(30.0);
 
        assertTrue(resultado);
        assertEquals(20.0, wallet.getBalance(), DELTA);
        assertTrue(wallet.isActive());
        assertEquals("Activa", wallet.getStatus());
    }
}
