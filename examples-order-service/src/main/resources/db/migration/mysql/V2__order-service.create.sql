create table orders (id bigint not null auto_increment, created_date datetime(6), last_modified_date datetime(6), version integer, product_id bigint not null, quantity integer not null, serial_id varchar(255), user_id bigint not null, primary key (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4