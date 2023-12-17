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