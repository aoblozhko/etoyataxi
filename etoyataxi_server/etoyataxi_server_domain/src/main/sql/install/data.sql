INSERT INTO role (id, name) VALUES (1, 'ROLE_USER');

INSERT INTO provider (id, name, driverbean, enabled, lastupdate, checkinterval)
  VALUES (2, 'mango', 'mangoDataProviderService', true, null, 86400);

INSERT INTO provider (id, name, driverbean, enabled, lastupdate, checkinterval)
  VALUES (1, 'rbt', 'rbtDataProviderService', true, null, 86400);