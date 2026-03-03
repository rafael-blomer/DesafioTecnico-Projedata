INSERT INTO raw_material (code, name, stock_quantity, unit) VALUES ('RM001', 'Wheat Flour',   5000.0, 'g');
INSERT INTO raw_material (code, name, stock_quantity, unit) VALUES ('RM002', 'Sugar',         3000.0, 'g');
INSERT INTO raw_material (code, name, stock_quantity, unit) VALUES ('RM003', 'Eggs',            30.0, 'un');
INSERT INTO raw_material (code, name, stock_quantity, unit) VALUES ('RM004', 'Butter',        2000.0, 'g');
INSERT INTO raw_material (code, name, stock_quantity, unit) VALUES ('RM005', 'Cocoa Powder',  1000.0, 'g');
INSERT INTO raw_material (code, name, stock_quantity, unit) VALUES ('RM006', 'Milk',          3000.0, 'ml');
INSERT INTO raw_material (code, name, stock_quantity, unit) VALUES ('RM007', 'Baking Powder',  200.0, 'g');

INSERT INTO product (code, name, price) VALUES ('PR001', 'Chocolate Cake',   45.00);
INSERT INTO product (code, name, price) VALUES ('PR002', 'Plain Cake',       30.00);
INSERT INTO product (code, name, price) VALUES ('PR003', 'Butter Cookies',   20.00);
INSERT INTO product (code, name, price) VALUES ('PR004', 'Chocolate Mousse', 35.00);
INSERT INTO product (code, name, price) VALUES ('PR005', 'Milk Bread',       15.00);

INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (1, 1, 500.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (1, 2, 300.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (1, 3,   4.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (1, 4, 200.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (1, 5, 100.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (1, 7,  10.0);

INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (2, 1, 400.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (2, 2, 200.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (2, 3,   3.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (2, 4, 150.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (2, 6, 200.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (2, 7,  10.0);

INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (3, 1, 200.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (3, 2, 100.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (3, 3,   1.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (3, 4, 150.0);

INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (4, 2, 200.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (4, 3,   3.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (4, 5, 100.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (4, 6, 500.0);

INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (5, 1, 300.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (5, 2,  50.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (5, 3,   1.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (5, 6, 200.0);
INSERT INTO product_ingredient (product_id, raw_material_id, quantity_required) VALUES (5, 7,   5.0);