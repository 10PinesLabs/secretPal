DELETE FROM usuario;

ALTER TABLE usuario
ADD COLUMN backoffice_id bigint,
DROP COLUMN user_name,
DROP COLUMN password
;
