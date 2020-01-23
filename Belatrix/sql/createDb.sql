create database belatrix;
use belatrix;

create table app_log(
	log_id int primary key auto_increment,
    log_code varchar(10) not null,
    log_msg varchar(2048) not null
);