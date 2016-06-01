		create table user_auth(
				id integer IDENTITY PRIMARY KEY , 
				email varchar(50) not null,
				password varchar(30) not null,
				unique (email));

		create table user(
				id integer not null, 
				firstname varchar(50) not null,
				lastname varchar(50) not null,
				phonenumber varchar(30) not null,
				primary key(id),
				foreign key (id) references user_auth(id));
				
		insert into user_auth (email,password) values('srikanthvarma.vadapalli@gmail.com','lancer');
	    insert into user (id,firstname,lastname,phonenumber) values(0,'Satyanandana','vadapalli','6178491980');
						
		

	


