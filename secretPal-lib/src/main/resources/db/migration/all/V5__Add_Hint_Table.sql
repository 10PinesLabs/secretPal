CREATE TABLE hint(
  id bigserial PRIMARY KEY,
  message character varying(255),
  friend_relation_id bigint REFERENCES friend_relation
);

