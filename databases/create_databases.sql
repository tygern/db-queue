drop database if exists messages;
drop database if exists messages_test;

drop role if exists messages;
create user messages with password 'messages';

create database messages with owner messages;
create database messages_test with owner messages;
