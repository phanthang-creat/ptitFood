DROP PROCEDURE IF EXISTS `insert_product`;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_product`(
    in catid int,
    in name varchar(255),
    in alias varchar(255),
    in avatar varchar(255),
    in img varchar(255),
    in sort_desc varchar(255),
    in detail varchar(255),
    in producer int,
    in number int,
    in number_buy int,
    in sale int,
    in price int,
    in price_sale int,
    in created_by int,
    in updated_by int,in status int)
BEGIN
    insert into db_product(cat_id, name, alias, avatar, img, sort_desc, detail, producer, number, number_buy, sale, price, price_sale, created, created_by, updated,updated_by, status)
    values(catid, name, alias, avatar, img, sort_desc, detail, producer, number, number_buy, sale, price, price_sale, now(), created_by, now(),updated_by, status);
END ;;
DELIMITER ;

DROP PROCEDURE IF EXISTS `insert_order`;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_order`(in customer_id int, in fullname varchar(100), in phone varchar(10), in money int, in codeDiscount varchar(255), in address varchar(255), in status int )
BEGIN
    PREPARE stmt FROM 'insert into db_order (
        customer_id,
        order_date,
        fullname,
        phone,
        money,
        code_discount,
        address,
        status
    ) value ( ?, now(), ?, ?, ?, ?, ?, ?) RETURNING id';
    set  @customer_id = customer_id, @fullname = fullname, @phone = aes_encrypt(phone, "myphone"),@money = money, @codeDiscount = codeDiscount,  @address = aes_encrypt(address, "my_address"),  @status = status;
    execute stmt using @customer_id, @fullname, @phone, @money, @codeDiscount, @address, @status;
    deallocate prepare stmt;
END ;;
DELIMITER ;

CALL insert_order(19], 'Nguyễn Văn A', '0123456789', 100000, null, 'Hà Nội', 1);

ALTER TABLE db_orderdetail add CONSTRAINT `FK_ORDER_ID` FOREIGN KEY (`order_id`) REFERENCES db_order(id) ON UPDATE CASCADE ON DELETE CASCADE;

DROP PROCEDURE IF EXISTS `select_customer_decryption_by_username`;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `select_customer_decryption_by_username`(in username_in varchar(100))
BEGIN
    select  id, fullname, username, password, cast(aes_decrypt(phone, "myphone") as char(100)) as phone, cast(aes_decrypt(email, 'myemail') as char(100)) as email, created, updated, status, role from db_customer where username = username_in;
END ;;
DELIMITER ;

CALL select_customer_decryption_by_username('userptit');

DROP PROCEDURE IF EXISTS `select_order_decryption_by_id`;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `select_order_decryption_by_id`(in id_in int)
BEGIN
    select id, customer_id, order_date, fullname, cast(aes_decrypt(phone, "myphone") as char(255)) as phone, money, code_discount, cast(aes_decrypt(address, "my_address") as char(255)) as address, status from db_order where `id` = id_in;
END ;;
DELIMITER ;

CALL select_order_decryption_by_id(5);