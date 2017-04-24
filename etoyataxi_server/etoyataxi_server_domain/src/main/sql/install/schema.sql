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
)
;

