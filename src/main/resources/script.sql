CREATE TABLE PLAIN_USERS
(
    id SERIAL PRIMARY KEY NOT NULL,
    two_fa_code VARCHAR(4) NOT NULL,
    two_fa_code_expire_time TIMESTAMP NOT NULL,
    is_two_fa_enabled BOOLEAN NOT NULL,
    birthDate date NOT NULL,
    firstName TEXT,
    is_enabled BOOLEAN,
    lastName TEXT,
    mailAddress TEXT NOT NULL,
    registeredDate date NOT NULL,
    sessionToken TEXT NOT NULL,
    isOnline boolean,
    password TEXT NOT NULL
);

CREATE TABLE TEST (
    name TEXT PRIMARY KEY NOT NULL,
    age text
);

insert into TEST(name, age) values ('boris', '20');
insert into TEST(name, age) values ('boris1', '20');
insert into TEST(name, age) values ('boris2', '20');
insert into TEST(name, age) values ('boris3', '20');
insert into TEST(name, age) values ('boris4', '20');
insert into TEST(name, age) values ('boris5', '20');
insert into TEST(name, age) values ('boris6', '20');
insert into TEST(name, age) values ('boris7', '20');