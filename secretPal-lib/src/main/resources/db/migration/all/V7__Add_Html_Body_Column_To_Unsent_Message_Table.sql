ALTER TABLE unsent_message
ADD COLUMN html_body varchar(255) DEFAULT ''
;

ALTER TABLE unsent_message
RENAME body TO plain_text_body;