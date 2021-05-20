create table CLIENT_BANKS
(
	CLIENT_ID INTEGER not null
		constraint FKINTDRD7G63IC64L6LP0UB0C9
			references CLIENT,
	BANK_ID INTEGER not null
		constraint FKFUHK8AG52G3YXRQ2YM15DYYIM
			references BANK,
	primary key (CLIENT_ID, BANK_ID)
);

