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