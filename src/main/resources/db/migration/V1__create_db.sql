create table user (
      id bigint not null auto_increment,
      username varchar(255),
      email varchar(255),
      password varchar(255),
      active bit,
      activation_code varchar(255),
      primary key (id)
);

create table contact (
     user_id bigint,
     name varchar(255),
     lastname varchar(255),
     homephone varchar(255),
     businessphone varchar(255),
     email varchar(255),
     photo varchar(255),
     id bigint not null auto_increment,
     primary key (id)
);

alter table contact
    add constraint FK_user foreign key (user_id) references user(id);