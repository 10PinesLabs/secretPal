ALTER TABLE worker
RENAME COLUMN wants_to_participate TO wants_to_give;

ALTER TABLE worker
ADD COLUMN wants_to_receive BOOLEAN NOT NULL DEFAULT TRUE,
ADD COLUMN wants_to_receive_mail BOOLEAN NOT NULL DEFAULT TRUE;