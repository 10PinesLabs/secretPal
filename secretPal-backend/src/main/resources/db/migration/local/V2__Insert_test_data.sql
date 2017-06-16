INSERT INTO worker
  (date_of_birth, e_mail, full_name, wants_to_participate) VALUES
  (DATE '1941-4-19', 'roberto@10pines.com', 'Roberto Carlos', TRUE),
  (DATE '1992-12-26', 'ayelen.garcia@10pines.com', 'Ayelen García', TRUE);

INSERT INTO usuario
(password, user_name, worker_id) VALUES
  ('password', 'aye', (SELECT id from worker WHERE full_name='Ayelen García')),
  ('password', 'roberto', (SELECT id from worker WHERE full_name='Roberto Carlos'));

INSERT INTO admin_profile
(user_id) VALUES
  ((SELECT id from usuario WHERE user_name='aye'));