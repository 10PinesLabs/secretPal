CREATE TABLE not_circular_relation_rule (
  id bigserial PRIMARY KEY,
  description character varying(255) NOT NULL,
  is_active boolean NOT NULL
);

CREATE TABLE not_too_close_birthdays_rule (
  id bigserial PRIMARY KEY,
  description character varying(255) NOT NULL,
  is_active boolean NOT NULL
);