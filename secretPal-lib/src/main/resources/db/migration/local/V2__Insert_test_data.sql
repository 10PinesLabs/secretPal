INSERT INTO worker
  (id, date_of_birth, e_mail, full_name, wants_to_participate) VALUES
  (nextval('hibernate_sequence'), DATE '1992-12-26', 'ayelen.garcia@10pines.com', 'Ayelen García', TRUE),
  (nextval('hibernate_sequence'), DATE '1941-08-19', 'roberto@10pines.com', 'Roberto Carlos', TRUE),
  (nextval('hibernate_sequence'), DATE '1991-10-16', 'eseidler@10pines.com', 'Ezequiel Seidler', TRUE),
  (nextval('hibernate_sequence'), DATE '1992-09-22', 'mmileto@10pines.com', 'Maggie Mileto', TRUE);

INSERT INTO usuario
(id, password, user_name, worker_id) VALUES
  (nextval('hibernate_sequence'), 'password', 'aye', (SELECT id from worker WHERE full_name='Ayelen García')),
  (nextval('hibernate_sequence'), 'password', 'roberto', (SELECT id from worker WHERE full_name='Roberto Carlos')),
  (nextval('hibernate_sequence'), 'password', 'eze', (SELECT id from worker WHERE full_name='Ezequiel Seidler')),
  (nextval('hibernate_sequence'), 'password', 'maggie', (SELECT id from worker WHERE full_name='Maggie Mileto'));

INSERT INTO admin_profile
(id, user_id) VALUES
  (nextval('hibernate_sequence'), (SELECT id from usuario WHERE user_name='aye'));