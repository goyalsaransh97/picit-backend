-- source create_database.sql

use picit;

drop table IF EXISTS UserInGroups;
drop table IF EXISTS SharedPictures;
drop table IF EXISTS SharedAlbums;
drop table IF EXISTS PicturesInAlbums;
drop table IF EXISTS Albums;
drop table IF EXISTS Pictures;
drop table IF EXISTS Groups;
drop table IF EXISTS Users;



create table if not exists Users (
	userId int not null,
	emailId varchar(255) not null,
	userName text,
	phoneNumber varchar(10), 
	primary key (userId),
	constraint UniqueEmails unique (emailId)
);

create table if not exists Groups (
	groupId int not null,
	creatorUserId int not null, 
	groupName text not null,
	primary key (groupId),
	foreign key (creatorUserId) references Users (userId)
);

create table if not exists UserInGroups (
	userId int not null,
	groupId int not null,
	isActive boolean not null,
	primary key (userId, groupId),
	foreign key (userId) references Users (userId),
	foreign key (groupId) references Groups (groupId)
);

create table if not exists Pictures (
	picId int not null,
	userId int not null,
	date_time datetime,
	primary key (picId),
	foreign key (userId) references Users (userId)
);

create table if not exists SharedPictures (
	picId int not null,
	userId int not null, /*user who clicked the picture*/
	groupId int not null,
	foreign key (picId) references Pictures (picId),
	foreign key (userId) references Users (userId),
	foreign key (groupId) references Groups (groupId)
);

create table if not exists Albums (
	albumId int not null,
	albumName text not null,
	userId int not null,	/*creator of album*/
	primary key (albumId),
	foreign key (userId) references Users (userId)
);

create table if not exists PicturesInAlbums (
	albumId int not null,
	picId int not null,
	foreign key (picId) references Pictures (picId),
	foreign key (albumId) references Albums (albumId)
);

create table if not exists SharedAlbums (
	albumId int not null,
	groupId int not null,	/*album shared with this group*/
	foreign key (groupId) references Groups (groupId),
	foreign key (albumId) references Albums (albumId)
);
