CREATE TABLE guesses(
  id bigserial PRIMARY KEY,
  guess character varying(255),
  friend_relation_id bigint REFERENCES friend_relation
);

ALTER TABLE friend_relation
  DROP COLUMN guess_attempts
;