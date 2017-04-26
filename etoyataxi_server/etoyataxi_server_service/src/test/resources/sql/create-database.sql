
drop table if exists providersetting;

drop table if exists providerdata;

drop table if exists providerdatamangocallrecord;

drop table if exists provider;

drop table if exists appuser_usergroup;

drop table if exists user_roles;

drop table if exists role;

drop table if exists usergroup;

drop table if exists appuser;

create table provider
(
  id bigserial not null
  constraint provider_pkey
  primary key,
  name varchar(255),
  driverbean varchar(255),
  enabled boolean,
  lastupdate timestamp,
  checkinterval bigint
)
;

create table providersetting
(
  id bigserial not null
  constraint providersetting_pkey
  primary key,
  name varchar(255),
  value varchar(255),
  provider_id bigint
  constraint fk_hc7wolmv1u0krcrx370fk1ldo
  references provider
  constraint fkpqc41mufc75yrxghu0yk5to53
  references provider
)
;

create table providerdata
(
  id bigserial not null
  constraint providerdata_pkey
  primary key,
  data jsonb,
  datetime timestamp,
  provider_id bigint
  constraint fk_fk6sy2k29rfuy0leukyr7iqy5
  references provider
  constraint fkflc2j0vivpkmnyf3ixyyqocrl
  references provider
)
;

create table providerdatamangocallrecord
(
  id bigserial not null
  constraint providerdatamangocallrecord_pkey
  primary key,
  contenttype varchar(255),
  data oid,
  recordid bigint not null
)
;

create table appuser
(
  id bigserial not null
  constraint appuser_pkey
  primary key,
  confirmationtoken varchar(255),
  credentialsexpired boolean,
  credentialsexpiredat timestamp,
  email varchar(255),
  emailcanonical varchar(255),
  enabled boolean,
  expired boolean,
  expiresat timestamp,
  lastlogin timestamp,
  locked boolean,
  name varchar(255),
  passwordhash varchar(255),
  passwordrequestedat timestamp,
  salt varchar(255),
  username varchar(255),
  usernamecanonical varchar(255)
)
;

create table appuser_usergroup
(
  user_id bigint not null
  constraint fke5qetx0iujmwe6cf6wfhoo4q4
  references appuser,
  groups_id bigint not null,
  constraint appuser_usergroup_pkey
  primary key (user_id, groups_id)
)
;

create table role
(
  id bigserial not null
  constraint role_pkey
  primary key,
  name varchar(255)
)
;

create table user_roles
(
  user_id bigint not null
  constraint fkdrqlnn9xwid4ljmpagq4omrmy
  references appuser,
  roles_id bigint not null
  constraint fkbcsalslurw33on7738rj3d8cp
  references role,
  constraint user_roles_pkey
  primary key (user_id, roles_id)
)
;

create table usergroup
(
  id bigserial not null
  constraint usergroup_pkey
  primary key,
  name varchar(255)
)
;

alter table appuser_usergroup
  add constraint fk5bs6ulbme7o5jatmgkar1258l
foreign key (groups_id) references usergroup
;


INSERT INTO role (id, name) VALUES (1, 'ROLE_USER');

INSERT INTO provider (id, name, driverbean, enabled, lastupdate, checkinterval)
VALUES (2, 'mango', 'mangoDataProviderService', true, null, 86400);

INSERT INTO provider (id, name, driverbean, enabled, lastupdate, checkinterval)
VALUES (1, 'rbt', 'rbtDataProviderService', true, null, 86400);