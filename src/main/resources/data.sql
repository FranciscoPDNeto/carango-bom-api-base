INSERT INTO usuario(username, password) VALUES('Maria', '$2a$10$yUz4LCqpSlv9.YnAfOKoB.uFVZHDETRLD/R14S9X9ZV/OLw1EWgN6');
INSERT INTO usuario(username, password) VALUES('luiz', '$2a$10$GnR6Qg0lUTFXc4NBBsaCpunNxR48MwVwta8OOmzfXG4kwG/bdGWIS');

INSERT INTO marca(nome) VALUES('Fiat');
INSERT INTO marca(nome) VALUES('Volkswagen');
INSERT INTO marca(nome) VALUES('Ford');

INSERT INTO veiculo(ano, modelo, valor, marca_id) VALUES(2020, 'Ecosport', 60000, 3);
INSERT INTO veiculo(ano, modelo, valor, marca_id) VALUES(2020, 'Gol', 50000, 2);
INSERT INTO veiculo(ano, modelo, valor, marca_id) VALUES(2018, 'Gol', 33000, 2);
INSERT INTO veiculo(ano, modelo, valor, marca_id) VALUES(2020, 'Uno', 42000, 1);
