# java-ddd-example

This project is an example for DDD.

## domain models are followings.

- [Money](https://github.com/j5ik2o/java-ddd-example/blob/master/domain/src/main/java/com/github/j5ik2o/ddd_example/domain/Money.java)
- [BankAccount](https://github.com/j5ik2o/java-ddd-example/blob/master/domain/src/main/java/com/github/j5ik2o/ddd_example/domain/BankAccount.java)
  - [BankAccountEvent](https://github.com/j5ik2o/java-ddd-example/blob/master/domain/src/main/java/com/github/j5ik2o/ddd_example/domain/BankAccountEvent.java)
- [BankAccountService](https://github.com/j5ik2o/java-ddd-example/blob/master/domain/src/main/java/com/github/j5ik2o/ddd_example/domain/BankAccountService.java)

## Ubiquitous Language

- Money Domain Model 
    - A Money can be added another money.
    - A Money can be subtracted another money.
    - A Money can be compared another money.
- Bank Account Domain Model
    - A BankAccount can be gotten the balance.
    - A Bank Account can be added events of cash deposit and cash withdrawal.
    - A Bank Account can be added deposit event from another Bank Account, and withdrawal event to another Bank Account.
    - The Balance of the Bank Account must not be less than 0.
- Bank Account Domain Service
    - The Bank Account Service can transfer a money from a Bank Account to a Bank Account.
    
## Domain Codes



### Money
    
A Money can be added another money.

```java
Money money1 = Money.of(10000);
Money money2 = Money.of(10000);
Money result = money1.plus(money2).substract(money2);
System.out.println(result);
```

A Money can be compared another money.

```java
Money money3 = Money.of(10000);
Money money4 = Money.of(20000);
Money result = money3.substract(money2);
if (result.isLessThan(Money.zero())) {
    System.out.println("result is less than zero!");
}
```

### BankAccount

A Bank Account can be added events of cash deposit and cash withdrawal.

```java
BankAccount bankAccount = new BankAccount(1L, ImmutableList.of())
    .depositCash(Money.of(10000))
    .withdrawCash(Money.of(10000));
System.out.println(bankAccount);
```

A BankAccount can be gotten the balance.

```java
BankAccount bankAccount = new BankAccount(1L, ImmutableList.of()).depositCash(Money.of(10000));
System.out.println(bankAccount.getBalance());
```

A Bank Account can be added deposit event from another Bank Account, and withdrawal event to another Bank Account.

```java
public final class BankAccountService {
    
    public static TransferResult transfer(BankAccount to, BankAccount from, Money money) {
        BankAccount updatedFrom = from.withdrawTo(to, money);
        BankAccount updatedTo = to.depositFrom(from, money);
        return new TransferResult(updatedTo, updatedFrom);
    }

}
```

The Balance of the Bank Account must not be less than 0.

```java
@Value
public final class BankAccount {
    
    // ...
    
    public static BankAccount of(Long id, List<BankAccountEvent> events) {
        if (getBalanceByEvents(events).isLessThan(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("total money is less than zero!");
        }
        return new BankAccount(id, events);
    }
    
    // ...
}
```

### BankAccountService 

The Bank Account Service can transfer a money from a Bank Account to a Bank Account.

```java
Money initialMoney = Money.of(10000);
BankAccount bankAccount1 = BankAccount.of(IdGenerator.generateId()).depositCash(initialMoney);
BankAccount bankAccount2 = BankAccount.of(IdGenerator.generateId()).depositCash(initialMoney);

System.out.println("bankAccount1 = " + bankAccount1.getBalance());
System.out.println("bankAccount2 = " + bankAccount2.getBalance());

BankAccountService.TransferResult transferResult = BankAccountService.transfer(
        bankAccount1,
        bankAccount2,
        Money.of(10000)
);

System.out.println("to = " + transferResult.getTo().getBalance());
System.out.println("from = " + transferResult.getFrom().getBalance());
```
