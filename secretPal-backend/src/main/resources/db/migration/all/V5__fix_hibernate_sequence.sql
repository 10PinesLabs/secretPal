DROP SEQUENCE hibernate_sequence;

SELECT setval('admin_profile_id_seq', (SELECT max(id) FROM admin_profile));
SELECT setval('custom_participant_rule_id_seq', (SELECT max(id) FROM custom_participant_rule));
SELECT setval('default_gift_id_seq', (SELECT max(id) FROM default_gift));
SELECT setval('email_template_id_seq', (SELECT max(id) FROM email_template));
SELECT setval('friend_relation_id_seq', (SELECT max(id) FROM friend_relation));
SELECT setval('not_circular_relation_rule_id_seq', (SELECT max(id) FROM not_circular_relation_rule));
SELECT setval('not_too_close_birthdays_rule_id_seq', (SELECT max(id) FROM not_too_close_birthdays_rule));
SELECT setval('unsent_message_id_seq', (SELECT max(id) FROM unsent_message));
SELECT setval('usuario_id_seq', (SELECT max(id) FROM usuario));
SELECT setval('wish_id_seq', (SELECT max(id) FROM wish));
SELECT setval('worker_id_seq', (SELECT max(id) FROM worker));