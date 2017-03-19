# java-ddd-example

このプロジェクトはDDDの例です。

## 以下がドメインモデルです

- [Money](https://github.com/j5ik2o/java-ddd-example/blob/master/domain/src/main/java/com/github/j5ik2o/ddd_example/domain/Money.java)
- [BankAccount](https://github.com/j5ik2o/java-ddd-example/blob/master/domain/src/main/java/com/github/j5ik2o/ddd_example/domain/BankAccount.java)
  - [BankAccountEvent](https://github.com/j5ik2o/java-ddd-example/blob/master/domain/src/main/java/com/github/j5ik2o/ddd_example/domain/BankAccountEvent.java)
- [BankAccountService](https://github.com/j5ik2o/java-ddd-example/blob/master/domain/src/main/java/com/github/j5ik2o/ddd_example/domain/BankAccountService.java)

## ユビキタス言語とドメインモデル

- お金ドメインモデル
    - お金はお金を追加できる
    - お金はお金を差し引ける
    - お金とお金は比較できる
- 銀行口座ドメインモデル
    - 銀行口座から残高を取得できる
    - 銀行口座に現金での入金および出金イベントを追加できる
    - 銀行口座に別の口座からの入金イベントを追加できる。もしくは、口座から別の口座への出金イベントを追加できる
    - 銀行口座の残高は0未満になってはならない
- 銀行口座ドメインサービス
    - 銀行口座ドメインサービスは、口座間の送金ができる。

## ドメインコード

ドメインモデルを反映した例は以下。

### お金

- お金はお金を追加できる。
- お金はお金を差し引ける。

```java
Money money1 = Money.of(10000);
Money money2 = Money.of(10000);
Money result = money1.plus(money2).substract(money2);
System.out.println(result);
```

- お金とお金は比較できる。

```java
Money money3 = Money.of(10000);
Money money4 = Money.of(20000);
Money result = money3.substract(money2);
if (result.isLessThan(Money.zero())) {
    System.out.println("result is less than zero!");
}
```

### 銀行口座

口座に現金での入金および出金イベントを追加できる。

```java
BankAccount bankAccount = new BankAccount(1L, ImmutableList.of())
    .depositCash(Money.of(10000))
    .withdrawCash(Money.of(10000));
System.out.println(bankAccount);
```

口座から残高を取得できる。

```java
BankAccount bankAccount = new BankAccount(1L, ImmutableList.of()).depositCash(Money.of(10000));
System.out.println(bankAccount.getBalance());
```

口座に別の口座からの入金イベントを追加できる。もしくは、口座から別の口座への出金イベントを追加できる。

```java
public final class BankAccountService {

    public static TransferResult transfer(BankAccount to, BankAccount from, Money money) {
        BankAccount updatedFrom = from.withdrawTo(to, money);
        BankAccount updatedTo = to.depositFrom(from, money);
        return new TransferResult(updatedTo, updatedFrom);
    }

}
```

口座の残高は0未満になってはならない

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

### 銀行口座ドメインサービス

銀行口座ドメインサービスは、口座間の送金ができる。

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

## 貧血症オブジェクトにおける、銀行口座間転送の例

この例は圧倒的に詳細です。メンタルモデルがないため、コードは理解しがたい。
ドメインモデルが変更された場合、コードを変更することも難しい。

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
