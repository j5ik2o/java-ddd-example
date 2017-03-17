# java-ddd-example

This project is an example for DDD.

## Domain

This domain of example is what related to the Bank Account.

## Bounded Context

Resolve transfer between Bank Accounts.

## Ubiquitous Languages

- Money Domain Model 
    - A Money can be added another money.
    - A Money can be subtracted another money.
    - A Money can be compared another money.
- Bank Account Domain Model
    - A Bank Account can be gotten the balance.
    - A Bank Account can be added events of cash deposit or cash withdrawal.
    - A Bank Account can be added deposit event from another Bank Account, and withdrawal event to another Bank Account.
    - The Balance of the Bank Account must not be less than 0.
- Bank Account Domain Service
    - The Bank Account Service can transfer a money from a Bank Account to a Bank Account.

## Domain Models

Domain models are followings.

- [Money](https://github.com/j5ik2o/java-ddd-example/blob/master/domain/src/main/java/com/github/j5ik2o/ddd_example/domain/Money.java)
- [BankAccount](https://github.com/j5ik2o/java-ddd-example/blob/master/domain/src/main/java/com/github/j5ik2o/ddd_example/domain/BankAccount.java)
  - [BankAccountEvent](https://github.com/j5ik2o/java-ddd-example/blob/master/domain/src/main/java/com/github/j5ik2o/ddd_example/domain/BankAccountEvent.java)
- [BankAccountService](https://github.com/j5ik2o/java-ddd-example/blob/master/domain/src/main/java/com/github/j5ik2o/ddd_example/domain/BankAccountService.java)

## Domain Codes

Followings are examples reflecting domain models.

These objects constructed by reasonably brief and abstract ubiquitous languages are easy to understand. 
It is also relatively easy to make the implementation follow changes to domain models.

### Money
    
A Money can be added another money.

```java
Money money1 = Money.of(10000);
Money money2 = Money.of(10000);
Money result = money1.add(money2).substract(money2);
System.out.println(result);
```

A Money can be compared another money.

```java
Money money3 = Money.of(10000);
Money money4 = Money.of(20000);
Money result = money3.substract(money4);
if (result.isLessThan(Money.zero())) {
    System.out.println("result is less than zero!");
}
```

### BankAccount

A Bank Account can be added events of cash deposit or cash withdrawal.

```java
BankAccount bankAccount = new BankAccount(1L)
    .depositCash(Money.of(10000))
    .withdrawCash(Money.of(10000));
System.out.println(bankAccount);
```

A BankAccount can be gotten the balance.

```java
BankAccount bankAccount = new BankAccount(1L).depositCash(Money.of(10000));
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

## An example of transfer between Bank Accounts in Anemic objects.

This example is overwhelmingly detailed.
Because there is no mental model, this codes are difficult to understand.
Changing the codes are also difficult if the domain models is changed.

```java
public static Money getBalance(BankAccount bankAccount){
    BigDecimal totalAmount = BigDecimal.ZERO;
    for(BankAccountEvent event : bankAccount.getEvents()) {
        totalAmount = totalAmount.add(event.getMoney().getAmount());
    }
    Money money = new Money();
    money.setCurrency(bankAccount.getEvents().get(0).getMoney().getCurrency());
    money.setAmount(totalAmount);
    return money;
}


Money baseMoney = new Money();
baseMoney.setCurrency(Currency.getInstance("JPY"));
baseMoney.setAmount(BigDecimal.valueOf(10000));

BankAccount bankAccount1 = new BankAccount();
bankAccount1.setId(IdGenerator.generateId());
List<BankAccountEvent> events1 = Lists.newArrayList();
BankAccountEvent incrementEvent1 = new BankAccountEvent();
incrementEvent1.setId(IdGenerator.generateId());
incrementEvent1.setToBankAccountId(bankAccount1.getId());
incrementEvent1.setFromBankAccountId(null);
incrementEvent1.setMoney(baseMoney);
incrementEvent1.setOccurredAt(ZonedDateTime.now());
events1.add(incrementEvent1);
bankAccount1.setEvents(events1);


BankAccount bankAccount2 = new BankAccount();
bankAccount2.setId(IdGenerator.generateId());
List<BankAccountEvent> events2 = Lists.newArrayList();
BankAccountEvent incrementEvent2 = new BankAccountEvent();
incrementEvent2.setId(IdGenerator.generateId());
incrementEvent2.setToBankAccountId(bankAccount1.getId());
incrementEvent2.setFromBankAccountId(null);
incrementEvent2.setMoney(baseMoney);
incrementEvent2.setOccurredAt(ZonedDateTime.now());
events2.add(incrementEvent2);
bankAccount2.setEvents(events2);

Money totalAmount1 = getBalance(bankAccount1);
Money totalAmount2 = getBalance(bankAccount2);

System.out.println("bankAccount1 = " + totalAmount1);
System.out.println("bankAccount2 = " + totalAmount2);

Money data = new Money();
data.setCurrency(bankAccount1.getEvents().get(0).getMoney().getCurrency());
data.setAmount(BigDecimal.valueOf(10000));

BankAccountService.Result result = BankAccountService.moveData(
        bankAccount1,
        bankAccount2,
        data
);

Money newTotalAmount1 = getBalance(result.getTo());
Money newTotalAmount2 = getBalance(result.getFrom());

System.out.println("toTotalAmount = " + newTotalAmount1);
System.out.println("fromTotalAmount = " + newTotalAmount2);

```
```java
public class BankAccountService {
    @Data
    public static class Result {
        private BankAccount to;
        private BankAccount from;
    }

    public static Result moveData(BankAccount to, BankAccount from, Money money) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for(BankAccountEvent event : to.getEvents()) {
            totalAmount = totalAmount.add(event.getMoney().getAmount());
        }
        if (totalAmount.compareTo(money.getAmount()) < 0) {
            throw new IllegalArgumentException("total money is less than zero!");
        }
        BankAccountEvent decrementEvent = new BankAccountEvent();
        decrementEvent.setId(IdGenerator.generateId());
        decrementEvent.setFromBankAccountId(from.getId());
        decrementEvent.setToBankAccountId(to.getId());
        Money negated = new Money();
        negated.setCurrency(money.getCurrency());
        negated.setAmount(money.getAmount().negate());
        decrementEvent.setMoney(negated);
        decrementEvent.setOccurredAt(ZonedDateTime.now());
        BankAccount newFrom = new BankAccount();
        List<BankAccountEvent> newFromEvent = Lists.newArrayList(from.getEvents());
        newFromEvent.add(decrementEvent);
        newFrom.setId(from.getId());
        newFrom.setEvents(newFromEvent);

        BankAccountEvent incrementEvent = new BankAccountEvent();
        incrementEvent.setId(IdGenerator.generateId());
        incrementEvent.setFromBankAccountId(from.getId());
        incrementEvent.setToBankAccountId(to.getId());
        incrementEvent.setMoney(money);
        incrementEvent.setOccurredAt(ZonedDateTime.now());
        BankAccount newTo = new BankAccount();
        List<BankAccountEvent> newToEvent = Lists.newArrayList(to.getEvents());
        newToEvent.add(incrementEvent);
        newTo.setId(to.getId());
        newTo.setEvents(newToEvent);

        Result result = new Result();
        result.setFrom(newFrom);
        result.setTo(newTo);
        return result;

    }

}
```
