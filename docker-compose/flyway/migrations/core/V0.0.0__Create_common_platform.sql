CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SCHEMA common AUTHORIZATION postgres;

CREATE TABLE common.tbl_rate (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
	rate_name varchar(500) NOT NULL,
	description varchar(500),
	rate_prise real,
	term int,
	is_valid bool NULL,
	date_add timestamp NOT NULL,
	CONSTRAINT tbl_artist_pk PRIMARY KEY (id)
);

ALTER TABLE common.tbl_rate OWNER TO postgres;
GRANT ALL ON TABLE common.tbl_rate TO postgres;

INSERT INTO common.tbl_rate
(rate_name,
description,
rate_prise,
term,
is_valid,
date_add)
VALUES('simple', 'simple', 100, 5, true, current_timestamp);

INSERT INTO common.tbl_rate
(rate_name,
description,
rate_prise,
term,
is_valid,
date_add)
VALUES('vip', 'vip', 200, 5, true, current_timestamp);

commit;