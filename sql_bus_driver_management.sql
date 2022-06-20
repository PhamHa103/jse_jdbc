/* I. CREATE TABLES */

-- route (Tuyến)
create table route(
	id number(3) primary key,
	distance number not null, -- Khoảng cách
    number_of_stop number not null -- Số điểm dừng
);

-- driver (Lái xe)
create table driver (
	id number(5) primary key,
	name nvarchar2(30) not null,
    address nvarchar2(100) not null, -- địa chỉ
	phone nvarchar2(10) not null, -- SĐT
	specialization nvarchar2(10) not null) ; -- Trình độ

-- assignment_sheet (Bảng phân công)
create table assignment_sheet(
	id number primary key,
	driver_id number(5) not null constraint driver_id references driver(id),
	route_id number(3) not null constraint route_id references route(id),
    number_of_turn number(2) not null
);