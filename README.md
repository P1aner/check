# Check  
Application created on Java 21.  


## Example of use with Java 21:
```shell
./gradlew shadowJar
```

```shell
java -jar .\build\libs\check-1.0-SNAPSHOT-all.jar 3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100 saveToFile=./result.csv datasource.url=jdbc:postgresql://localhost:5432/check datasource.username=postgres datasource.password=postgres
```
## Example of use with Java 22:

```shell
java -cp src ./src/main/java/ru/clevertec/check/CheckRunner.java 3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100 saveToFile=./result.csv datasource.url=jdbc:postgresql://localhost:5432/check datasource.username=postgres datasource.password=postgres
```

The set of parameters is specified in the format: __id-quantity__   __discountCard__=xxxx
- __id__ - Product ID.  
- __quantity__ - Quantity of product.    
- __discountCard__ - DiscountCardId.
- __balanceDebitCard__ - Balance debit card.
- __saveToFile__ - Path to result file.
- __datasource.url__ - URL to your Postgres database
- __datasource.username__ - Username to your Postgres database
- __datasource.password__ - Password to your user

  After that, the generated check is displayed in the file.  

## Example of check:
```text
Date;Time
04.07.2024;14:34:46

QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL
1;Yogurt 400g;2.10$;0.06$;2.10$
5;Cream 400g;2.71$;1.36$;13.55$
1;Packed cabbage 1kg;1.19$;0.04$;1.19$

DISCOUNT CARD;DISCOUNT PERCENTAGE
1111;3%

TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT
16.84$;1.46$;15.38$
```