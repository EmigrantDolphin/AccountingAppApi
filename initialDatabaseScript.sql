create database if not exists accounting_app_store;
use accounting_app_store;

drop table if exists Records;
drop table if exists CategoryAdmins;
drop table if exists Users;
drop table if exists Categories;

create table Users(
	id int not null auto_increment primary key,
    username varchar(100) unique not null,
    name varchar(100),
    surname varchar(100),
    password varchar(100) not null,
    isSystemAdmin bit default 0
);


create table Categories(
	id int not null auto_increment primary key,
    name varchar(100),
    parentCategory int
);

create table CategoryAdmins(
	categoryId int not null,
    userId int not null,
    constraint categoryadmins_userid_fk foreign key (userId) references Users(id) on delete cascade,
    constraint categoryadmins_categoryId_fk foreign key (categoryId) references Categories(id) on delete cascade
);

create table Records(
	id int not null auto_increment primary key,
	name varchar(100) not null,
    amount double not null,
    creationDate timestamp not null,
    userCreatorId int not null,
    categoryId int not null,
    isSpending bit not null,
    constraint records_usercreatorid_fk foreign key (userCreatorId) references Users(id) on delete cascade,
    constraint records_categoryid_fk foreign key (categoryId) references Categories(id) on delete cascade
);

insert into categories (id, name, parentCategory) values (1, 'root', 0);
insert into users (id, username, name, surname, password, isSystemAdmin) values (1, 'root', 'Elon', 'Musk', 'admin' ,1);
insert into categoryAdmins (categoryId, userId) values (1, 1);

