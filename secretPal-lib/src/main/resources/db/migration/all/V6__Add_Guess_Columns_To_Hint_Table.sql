ALTER TABLE friend_relation
ADD COLUMN is_guessed BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN remaining_guess_attempts int NOT NULL DEFAULT 3;