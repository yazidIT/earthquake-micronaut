-- quakedb.quake definition

CREATE TABLE quake (
	id bigserial NOT NULL,
	latitude float8 NOT NULL,
	longitude float8 NOT NULL,
	magnitude float8 NOT NULL,
	quakeid varchar(200) NOT NULL,
	quaketime varchar(200) NULL DEFAULT NULL::character varying,
	title varchar(200) NULL DEFAULT NULL::character varying,
	CONSTRAINT quake_pkey PRIMARY KEY (id),
	CONSTRAINT quake_quakeid_key UNIQUE (quakeid)
);