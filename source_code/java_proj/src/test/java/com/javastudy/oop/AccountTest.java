package com.javastudy.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void testInitialBalance() {
        Account account = new Account(100);
        assertEquals(100, account.getBalance());
    }

    @Test
    void testDeposit() {
        Account account = new Account(100);
        account.deposit(50);
        assertEquals(150, account.getBalance());
    }

    @Test
    void testDepositNegativeThrows() {
        Account account = new Account(100);
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-10));
    }

    @Test
    void testWithdraw() {
        Account account = new Account(100);
        account.withdraw(30);
        assertEquals(70, account.getBalance());
    }

    @Test
    void testWithdrawInsufficientBalance() {
        Account account = new Account(100);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(200));
    }

    @Test
    void testNegativeInitialBalance() {
        assertThrows(IllegalArgumentException.class, () -> new Account(-1));
    }
}
