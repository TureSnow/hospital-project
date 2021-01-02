use hospital;
create table user(
id int(10) not null auto_increment,
name varchar(20) not null,
password varchar(20) not null,
role enum('0','1','2','3') default '3',
-- 0:doctor; 1:head_nurse; 2:ward_nurse; 3:emergency_nurse
area enum('0','1','2','3') default '0',
-- 0:isolation; 1:mild; 2:severe; 3:critical
primary key (id)
)default CHARSET=utf8;
-- insert user (name,password,gender,age,role,area) values

create table patient(
id int(18) not null ,
name varchar(20) not null ,
age int not null ,
phone varchar (20) not null ,
gender enum('male','female') default 'female',
life_state enum('0','1','2')default '0',
-- 0:healthy; 1:treating; 2:dead
area_level enum('0','1','2','3','4') default '0',
-- 0:isolation; 1:mild; 2:severe; 3:critical; 4:home
illness_level enum('0','1','2','3') default '0',
-- 0:health; 1:mild; 2:severe; 3:critical
primary key (id),
check (age between 1 and 150)
)default CHARSET=utf8;

create table bed(
id int not null ,
nurse_id int(10),
patient_id int(18),
level enum('1','2','3') default '1',
-- 1:mild; 2:severe; 3:critical
bed_id int not null,
ward_id int not null,
primary key (id),
foreign key(nurse_id) references user(id),
foreign key(patient_id) references patient(id)
)default CHARSET=utf8;
insert into bed(id,level,ward_id,bed_id) values
(1,1,201,1),(2,1,201,2),(3,1,201,3),(4,1,201,4),
(5,1,202,1),(6,1,202,2),(7,1,202,3),(8,1,202,4),
(9,2,301,1),(10,2,301,2),
(11,2,302,1),(12,2,302,2),
(13,3,401,1),
(14,3,402,1);

create table NA_sheet(
patient_id int(18) not null ,
date DATE not null ,
NA_result enum('0','1') default '1',
-- 0:negative; 1:positive
illness_level enum('1','2','3') default '1',
-- 1:mild; 2:severe; 3:critical
primary key(patient_id,date),
foreign key(patient_id) references patient(id)
)default CHARSET=utf8;

create table daily_status(
patient_id int(18) not null ,
date DATE not null ,
temperature float not null ,
life_state enum('0','1','2')default '0', -- 0:healthy; 1:treating; 2:dead
NA_result enum('0','1') default '1', -- 0:negative; 1:positive
symptom varchar(500),
primary key(patient_id,date),
foreign key(patient_id) references patient(id)
)default CHARSET=utf8;

create table notify(
user_id int(10) not null,
content varchar (500) not null,
is_read enum('0', '1') default '0',-- 0:not read; 1:read
date DATE not null ,
primary key(user_id,date),
foreign key(user_id) references user(id)
)default CHARSET=utf8;