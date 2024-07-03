# Check  
Application created on Java 21.  


## Example of use with Java 21:
```shell
./gradlew jar
```

```shell
java -jar .\build\libs\check-1.0-SNAPSHOT.jar 13-5 9-1 4-30 1-6 discountCard=1111 balanceDebitCard=1120
```
## Example of use with Java 22:

```shell
java -cp src ./src/main/java/ru/clevertec/check/CheckRunner.java 3-1 2-5 5-1 discountCard=2131 balanceDebitCard=1112
```

The set of parameters is specified in the format: __id-quantity__   __discountCard__=xxxx
__balanceDebitCard__=xxxx
- __id__ - Product ID.  
- __quantity__ - Quantity of product.    
- __discountCard__ - DiscountCardId.
- __balanceDebitCard__ - Balance debit card.

  After that, the generated check is displayed in the file __result.csv__.  

## Example of check:
```text
Date;Time;
03.07.2024;01:23:18;

QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL;
5;Baguette 360g;1.30$;0.65$;6.50$;
1;Packed bananas 1kg;1.10$;0.03$;1.10$;
30;Packed potatoes 1kg;1.47$;1.32$;44.10$;
6;Milk;1.07$;0.64$;6.42$;
12;Packed tomatoes 350g;1.60$;0.58$;19.20$;

DISCOUNT CARD;DISCOUNT PERCENTAGE;
1111;3%;

TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT;
77.32$;3.22$;74.10$;
```