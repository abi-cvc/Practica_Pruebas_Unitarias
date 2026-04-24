# Ejercicio JUnit — SmartWallet

**Autor:** Carol Velásquez

**Materia:** Verificación y Validación de Software — EPN

**Stack:** Java 17 · Maven · JUnit 5 (Jupiter)

Implementación de una clase `SmartWallet` con su suite de pruebas unitarias.

## Requisitos

- Java 17 o superior
- Maven 3.6+

## Reglas de negocio

### `boolean deposit(double amount)`
- El monto debe ser mayor a `0`.
- Si el usuario es `Standard`, el saldo máximo no puede superar los `$5,000`.
- Si el monto es mayor a `$100`, el usuario recibe un cashback del **1%** del depósito (se suma al saldo).

### `boolean withdraw(double amount)`
- No se pueden retirar montos negativos o cero.
- No se puede retirar más de lo que hay en el saldo.
- Si el saldo queda en exactamente `0`, la cuenta se marca como `Inactiva`.

## Ejecutar los tests

Desde la raíz del proyecto:

```bash
mvn test
```

## Estructura del proyecto

```
Practica_Pruebas_Unitarias/
├── pom.xml
├── README.md
├── .gitignore
└── src/
    ├── main/java/ec/edu/epn/wallet/
    │   └── SmartWallet.java
    └── test/java/ec/edu/epn/wallet/
        └── SmartWalletTest.java
```

## Cobertura de pruebas

La suite incluye **14 casos** organizados en cuatro categorías:

- **Caminos felices** — depósitos y retiros válidos.
- **Valores límite** — exactamente `$100` de depósito, exactamente `$5,000` de saldo.
- **Casos de error** — montos negativos, cero, saldo insuficiente.
- **Reglas especiales** — marcado de cuenta como `Inactiva`, diferenciación Standard vs Premium.

Se utiliza `@BeforeEach` para inicializar una billetera `Standard` vacía antes de cada test.