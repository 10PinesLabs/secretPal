
ALTER TABLE friend_relation
ADD COLUMN is_guessed BOOLEAN DEFAULT FALSE,
ADD COLUMN remaining_guess_attempts INTEGER DEFAULT 3
;
