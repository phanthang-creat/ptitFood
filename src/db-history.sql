CREATE TABLE db_order_status (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO db_order_status (id, name) VALUES
(0, 'Đang chờ xử lý'),
(1, 'Đã xác nhận'),
(2, 'Đang giao hàng'),
(3, 'Đã giao hàng'),
(4, 'Đã hủy');


UPDATE db_order_status SET name = 'Đang chờ xử lý' WHERE id = 1;
UPDATE db_order_status SET name = 'Đã xác nhận' WHERE id = 2;
UPDATE db_order_status SET name = 'Đang giao hàng' WHERE id = 3;
UPDATE db_order_status SET name = 'Đã giao hàng' WHERE id = 4;

INSERT INTO db_order_status (id, name) VALUES
(5, 'Đã hủy');

UPDATE db_order_status SET name = 'Đã hủy' WHERE id = 5;