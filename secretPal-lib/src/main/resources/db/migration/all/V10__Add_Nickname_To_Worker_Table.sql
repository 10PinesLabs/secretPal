ALTER TABLE worker
ADD COLUMN nickname character varying(255)
;

UPDATE worker SET nickname = full_name;

ALTER TABLE worker
ALTER COLUMN nickname SET NOT NULL;