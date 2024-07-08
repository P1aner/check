# Check  
Application created on Java 21.  


## Example of use:
**1.** Build project
```shell
./gradlew war
```
**2.** Setup system properties
```properties
datasource.url=jdbc:postgresql://localhost:5432/check
datasource.username=postgres
datasource.password=postgres
```
Where:
- __datasource.url__ - URL to your Postgres database
- __datasource.username__ - Username to your Postgres database
- __datasource.password__ - Password to your user

**3.** Insert war file in container servlet.
##  Example of REST:
### Products:
- Get product from bd: GET http://localhost:8080/products?id=1
- Create product in bd: POST http://localhost:8080/products
```html
{
"description": "Eat 100g.",
"price": 3.25,
"quantity": 5,
"isWholesale": true
}
```
- Update product in bd: PUT http://localhost:8080/products?id=1
```html
{
"description": "Chocolate Ritter sport 100g.",
"price": 3.25,
"quantity": 5,
"isWholesale ": true
}
```
- Delete product in bd: DELETE http://localhost:8080/products?id=1

### Discount card:
- Get product from bd: GET http://localhost:8080/discountcards?id=1
- Create product in bd: POST http://localhost:8080/discountcards
```html
{
"discountCard": 5265,
"discountAmount": 2
}
```
- Update product in bd: PUT http://localhost:8080/discountcards?id=1
Body:
{
"discountCard": 6786,
"discountAmount": 3
}
- Delete product in bd: DELETE http://localhost:8080/discountcards?id=1


### Check:
- Get check
POST http://localhost:8080/check
```html
{
"products": [
{
"id": 1,
"quantity": 5
},
{
"id": 1,
"quantity": 5
}
],
"discountCard": 1234,
"balanceDebitCard": 100
}
```
- __id__ - Product ID.
- __quantity__ - Quantity of product.
- __discountCard__ - DiscountCardId.
- __balanceDebitCard__ - Balance debit card.

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