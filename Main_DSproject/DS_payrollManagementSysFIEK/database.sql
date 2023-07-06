
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `payrollManagmentSystemFIEK` ;
USE `payrollManagmentSystemFIEK` ;
create table CHATS(
senderId INT,
receiverId INT,
message varchar(500),
sendTime datetime,
path varchar(255),
primary key(senderId,receiverId,sendTime),
foreign key(senderId) references STAFF(SSN),
foreign key(receiverId) references STAFF(SSN)
);

-- -----------------------------------------------------
-- Table `payrollManagmentSystemFIEK`.`STAFF`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `payrollManagmentSystemFIEK`.`STAFF` (
--  varchar
  `ssn` INT NOT NULL,
  `emri` VARCHAR(255) NOT NULL,
  `mbiemri` VARCHAR(255),
  `email` VARCHAR(255) NOT NULL,
  `department` VARCHAR(255) NOT NULL,
  `experience` INT NULL,
  `birthday` DATE NULL,
  `phoneNumber` VARCHAR(40) NOT NULL,
  `position` VARCHAR(255),
  `salary` INT NOT NULL,
  `address` VARCHAR(255),
  `gender` CHAR(1) NOT NULL,
  `dateHired` DATE NULL,
  `bankAccountNr` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `salt` VARCHAR(255) NOT NULL,
  `admin` TINYINT NOT NULL,
  PRIMARY KEY (`ssn`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `phoneNumber_UNIQUE` (`phoneNumber` ASC) VISIBLE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


use payrollmanagmentsystemfiek;
insert into staff values(1,"Enes","Hasani","enes@gmail.com","Computer Engineering",8,curdate(),"+38312512452","engineer", 1000, "Vushtrri, 42000, 13", 'M', curdate(), "123", "04dd4bf690c7d38e264bc9b46745b4ddf7e02a84399a898fadaed03f73c1e5f4", 123456, true);
insert into staff values(2,"Fjoralba","Krasniqi","fjoralbakrasniqi@gmail.com","Computer Engineering",8,curdate(),"+38345111111","engineer", 1000, "Vushtrri, 42000, 13", 'M', curdate(), "123", "8303c654ae40614514bef6d430b3de67d18be7b5dc7efed76e3615be0ba7b251", "5703142b8032", true);
insert into staff values(3,"Enes","Hasani","enesi@gmail.com","Computer Engineering",8,curdate(),"+383125512452","engineer", 10080, "Vushtrri, 42000, 13", 'M', curdate(), "123", "04dd4bf690c7d38e264bc9b46745b4ddf7e02a84399a898fadaed03f73c1e5f4", 123456, true);
