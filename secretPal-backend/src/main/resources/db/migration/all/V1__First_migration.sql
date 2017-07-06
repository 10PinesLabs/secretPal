CREATE TABLE worker (
  id bigserial PRIMARY KEY,
  date_of_birth date NOT NULL,
  e_mail character varying(255) NOT NULL,
  full_name character varying(255) NOT NULL,
  gift_date_received date,
  wants_to_participate boolean NOT NULL
);

CREATE TABLE usuario (
  id bigserial PRIMARY KEY,
  password character varying(255) NOT NULL,
  user_name character varying(255) NOT NULL,
  worker_id bigint REFERENCES worker
);

CREATE TABLE admin_profile (
  id bigserial NOT NULL,
  user_id bigint REFERENCES usuario
);

CREATE TABLE default_gift (
  id bigserial PRIMARY KEY,
  amount_default character varying(255) NOT NULL,
  gift_default character varying(255) NOT NULL
);

CREATE TABLE email_template (
  id bigserial PRIMARY KEY,
  active boolean,
  body_text character varying(255),
  date_of_birth character varying(255),
  full_name character varying(255),
  subject character varying(255)
);

CREATE TABLE friend_relation (
  id bigserial PRIMARY KEY,
  gift_giver_id bigint REFERENCES worker,
  gift_receiver_id bigint REFERENCES worker
);

CREATE TABLE unsent_message (
  id bigserial PRIMARY KEY,
  body character varying(255),
  error character varying(255),
  recipient character varying(255),
  subject character varying(255)
);

CREATE TABLE wish (
  id bigserial PRIMARY KEY,
  gift character varying(255) NOT NULL,
  created_by_id bigint NOT NULL REFERENCES worker,
  worker_id bigint NOT NULL REFERENCES worker
);

CREATE TABLE custom_participant_rule (
  id bigserial PRIMARY KEY,
  giver_id bigint REFERENCES worker,
  receiver_id bigint REFERENCES worker,
  is_active boolean NOT NULL
);

CREATE TABLE not_circular_relation_rule (
  id bigserial PRIMARY KEY,
  description character varying(255) NOT NULL,
  is_activate boolean NOT NULL
);

CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
