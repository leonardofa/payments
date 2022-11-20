CREATE TABLE IF NOT EXISTS users(
	id integer auto_increment primary key,
	name varchar(255) not null,
	email varchar(255) not null
);

CREATE TABLE IF NOT EXISTS accounts(
	id integer auto_increment primary key,
	id_user integer not null,
	name varchar(255) not null,
	iban varchar(255) not null,
	foreign key (id_user) references users(id)
);

CREATE TABLE IF NOT EXISTS products(
	id integer auto_increment primary key,
	id_account integer not null,
	name varchar(255) not null,
	price numeric(10,2) not null,
	foreign key (id_account) references accounts(id)
);

CREATE TABLE IF NOT EXISTS payments(
	id integer auto_increment primary key,
	id_user integer not null,
	id_account integer not null,
	name varchar(255) not null,
	total_price numeric(10,2) not null,
	confirmed boolean not null,
	foreign key (id_account) references accounts(id),
	foreign key (id_user) references users(id)
);

CREATE TABLE IF NOT EXISTS payments_products(
    id integer auto_increment primary key,
	id_payment integer,
	id_product integer,
    quantity    integer,
    foreign key (id_payment) references payments(id),
	foreign key (id_product) references products(id)
);

insert into users(name,email) values ('John Doe','test@test.com');
insert into users(name,email) values ('Jane Doe','test1@test.com');

insert into accounts(id_user,name,iban) values (1,'Account 1','PT000000000000');
insert into accounts(id_user,name,iban) values (2,'Account 2','PT000000000011');

insert into products(id_account,name,price) values (1,'Product 1',12.00);
insert into products(id_account,name,price) values (1,'Product 2',13.00);
insert into products(id_account,name,price) values (2,'Product A',14.00);
insert into products(id_account,name,price) values (2,'Product B',15.00);
