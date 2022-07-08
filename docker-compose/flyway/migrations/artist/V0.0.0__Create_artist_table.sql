CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SCHEMA artist AUTHORIZATION postgres;

CREATE TABLE artist.tbl_artist (
	email varchar(100) NOT NULL UNIQUE,
	login varchar(100),
	date_add timestamptz NOT NULL,
	date_update timestamptz NOT NULL,
	id uuid NOT NULL DEFAULT uuid_generate_v4(),
	CONSTRAINT tbl_artist_pk PRIMARY KEY (id)
);

CREATE TABLE artist.tbl_payment (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    artist_id varchar(100),
    payment_id varchar(100),
    rate_id varchar(100),
    date_payment timestamptz NOT NULL,
    date_end timestamptz NOT NULL,
	CONSTRAINT tbl_payment_pk PRIMARY KEY (id)
);

ALTER TABLE artist.tbl_artist OWNER TO postgres;
ALTER TABLE artist.tbl_payment OWNER TO postgres;

GRANT ALL ON TABLE artist.tbl_artist TO postgres;
GRANT ALL ON TABLE artist.tbl_payment TO postgres;