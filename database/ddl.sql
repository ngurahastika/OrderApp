
CREATE TABLE allowed_domain (
  id int8 NOT NULL,
  id_form int8 NULL,
  dom varchar NULL,
  "version" numeric NULL,
  CONSTRAINT allowed_domains_pk PRIMARY KEY (id)
);
CREATE INDEX allowed_domain_id_form_idx ON public.allowed_domain USING btree (id_form);


CREATE TABLE answers (
  id int8 NOT NULL,
  id_response int8 NULL,
  id_question int8 NULL,
  value text NULL,
  created_by varchar NULL,
  created_dt timestamp NULL,
  "version" numeric NULL,
  CONSTRAINT answers_pk PRIMARY KEY (id)
);
CREATE INDEX answers_id_response_idx ON public.answers USING btree (id_response);


CREATE TABLE form (
  id int8 NOT NULL,
  nm varchar NULL,
  slug varchar NULL,
  description text NULL,
  limit_one_response bool NULL,
  id_usr int8 NULL,
  created_by varchar NULL,
  created_dt timestamp NULL,
  updated_by varchar NULL,
  updated_dt timestamp NULL,
  "version" numeric NULL,
  CONSTRAINT form_pk PRIMARY KEY (id),
  CONSTRAINT form_unique UNIQUE (slug)
);


CREATE TABLE questions (
  id int8 NOT NULL,
  id_form int8 NULL,
  nm varchar NULL,
  choice_type varchar NULL,
  choices text NULL,
  is_required bool NULL,
  created_by varchar NULL,
  created_dt timestamp NULL,
  updated_by varchar NULL,
  updated_dt timestamp NULL,
  "version" numeric NULL,
  CONSTRAINT questions_pk PRIMARY KEY (id)
);

CREATE TABLE responses (
  id int8 NOT NULL,
  id_form int8 NULL,
  id_usr int8 NULL,
  created_by varchar NULL,
  created_dt timestamp NULL,
  "version" numeric NULL,
  CONSTRAINT responses_pk PRIMARY KEY (id)
);
CREATE INDEX responses_id_form_idx ON public.responses USING btree (id_form, id_usr);

CREATE TABLE usr (
  id int8 NOT NULL,
  nm varchar NULL,
  email varchar NULL,
  pwd varchar NULL,
  created_by varchar NULL,
  created_dt timestamp NULL,
  updated_by varchar NULL,
  updated_dt timestamp NULL,
  status varchar NULL,
  login_attemp int4 NULL,
  "version" numeric NULL,
  CONSTRAINT usr_pk PRIMARY KEY (id)
);
CREATE UNIQUE INDEX usr_email_idx ON public.usr USING btree (email);


CREATE TABLE usr_session (
  id int8 NOT NULL,
  sid varchar NULL,
  refresh_count int4 NULL,
  created_by varchar NULL,
  created_dt timestamp NULL,
  updated_by varchar NULL,
  updated_dt timestamp NULL,
  "version" numeric NULL,
  CONSTRAINT usr_session_pk PRIMARY KEY (id)
);