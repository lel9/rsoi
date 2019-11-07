DROP DATABASE notebook;

CREATE DATABASE notebook WITH TEMPLATE = template0 ENCODING = 'UTF8';

CREATE USER admin with encrypted password 'admin';

GRANT ALL PRIVILEGES on database notebook to admin;

\connect notebook admin

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

CREATE TABLE public.users (
    id uuid NOT NULL,
    name text NOT NULL,
    surname text NOT NULL,
    email text NOT NULL
);

CREATE TABLE public.notes (
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    title text NOT NULL,
    note_text text NOT NULL,
    created_at bigint NOT NULL,
    modified_at bigint NOT NULL
);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.notes
    ADD CONSTRAINT notes_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.notes
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES public.users(id);


GRANT ALL ON SCHEMA public TO PUBLIC;

INSERT INTO public.users VALUES('ab598598-e12e-4f3d-be82-40ba87ed58ad', 'olga', 'ya', 'ya@mail.ru');
INSERT INTO public.users VALUES('72c3c2cd-a431-4cd8-8eca-9e7aad115159', 'kate', 'am', 'am@mail.ru');

INSERT INTO public.notes
    VALUES('56b65658-418a-4c16-9723-384a40937f0d',
           'ab598598-e12e-4f3d-be82-40ba87ed58ad',
           'note 1',
           'hello 1',
           '1573158948',
           '1573158948');

INSERT INTO public.notes
    VALUES('8100ffac-0f55-4c37-b0c6-6e13df640c47',
           'ab598598-e12e-4f3d-be82-40ba87ed58ad',
           'note 2',
           'hello 2',
           '1573153150',
           '1573158900');

INSERT INTO public.notes
    VALUES('afc70238-a96b-40f4-8d8f-4fb406e6a3a9',
           '72c3c2cd-a431-4cd8-8eca-9e7aad115159',
           'note 3',
           'hello 3',
           '1573151111',
           '1573152222');

