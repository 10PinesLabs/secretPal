INSERT INTO worker
  (date_of_birth, e_mail, full_name, wants_to_participate) VALUES
  (DATE '1992-12-26', 'ayelen.garcia@10pines.com', 'Ayelen García', TRUE),
  (DATE '1941-08-19', 'roberto@10pines.com', 'Roberto Carlos', TRUE),
  (DATE '1991-10-16', 'eseidler@10pines.com', 'Ezequiel Seidler', TRUE),
  (DATE '1992-09-22', 'mmileto@10pines.com', 'Maggie Mileto', TRUE);

INSERT INTO usuario
(password, user_name, worker_id) VALUES
  ('password', 'aye', (SELECT id from worker WHERE full_name='Ayelen García')),
  ('password', 'roberto', (SELECT id from worker WHERE full_name='Roberto Carlos')),
  ('password', 'eze', (SELECT id from worker WHERE full_name='Ezequiel Seidler')),
  ('password', 'maggie', (SELECT id from worker WHERE full_name='Maggie Mileto'));

INSERT INTO admin_profile
(user_id) VALUES
  ((SELECT id from usuario WHERE user_name='aye'));

INSERT INTO not_circular_relation_rule
(id, description, is_activate) VALUES
  (1, 'Si el Pino A le regala a el Pino B, el Pino B no le puede regalar a el Pino A.', FALSE);