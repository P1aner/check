CREATE TABLE public.discount_card
(
    id bigint NOT NULL,
    "number" integer NOT NULL,
    amount smallint NOT NULL,
    CONSTRAINT discount_card_pkey PRIMARY KEY (id),
    CONSTRAINT "amount " CHECK (0 <= amount AND amount <= 100) NOT VALID
)
TABLESPACE pg_default;

CREATE TABLE public.product
(
    id bigint NOT NULL,
    description character(50) NOT NULL,
    price numeric(10,2) NOT NULL,
    quantity_in_stock integer NOT NULL,
    wholesale_product boolean NOT NULL,
    CONSTRAINT product_pkey PRIMARY KEY (id)
)
TABLESPACE pg_default;


INSERT INTO public.discount_card (id, number, amount) VALUES ('1', '1111', '3');
INSERT INTO public.discount_card (id, number, amount) VALUES ('2', '2222', '3');
INSERT INTO public.discount_card (id, number, amount) VALUES ('3', '3333', '4');
INSERT INTO public.discount_card (id, number, amount) VALUES ('4', '4444', '5');

INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('1', 'Milk', '1.07', '10', 'true');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('2', 'Cream 400g', '2.71', '20', 'true');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('3', 'Yogurt 400g', '2.10', '7', 'true');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('4', 'Packed potatoes 1kg', '1.47', '30', 'false');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('5', 'Packed cabbage 1kg', '1.19', '15', 'false');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('6', 'Packed tomatoes 350g', '1.60', '50', 'false');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('7', 'Packed apples 1kg', '2.78', '18', 'false');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('8', 'Packed oranges 1kg', '3.20', '12', 'false');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('9', 'Packed bananas 1kg', '1.10', '25', 'true');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('10', 'Packed beef fillet 1kg', '12.8', '7', 'false');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('11', 'Packed pork fillet 1kg', '8.52', '14', 'false');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('12', 'Packed chicken breasts 1kg Sour', '10.75', '18', 'false');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('13', 'Baguette 360g', '1.30', '10', 'true');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('14', 'Drinking water 1,5l', '0.80', '100', 'false');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('15', 'Olive oil 500ml', '5.30', '16', 'false');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('16', 'Sunflower oil 1l', '1.20', '12', 'false');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('17', 'Chocolate Ritter sport 100g', '1.10', '50', 'true');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('18', 'Paulaner 0,5l', '1.10', '100', 'false');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('19', 'Whiskey Jim Beam 1l', '13.99', '30', 'false');
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product) VALUES ('20', 'Whiskey Jack Daniels 1l', '17.19', '20', 'false');