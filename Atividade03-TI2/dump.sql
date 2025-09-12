--
-- PostgreSQL database dump
--

\restrict 5fY9CnqmbcTBxp1xeMW5aqg6DhhA9F5dVc11JxstkfKNXuottBeRreWJWchXdUN

-- Dumped from database version 17.6
-- Dumped by pg_dump version 17.6

-- Started on 2025-09-12 14:31:04

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 16392)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    codigo integer NOT NULL,
    login text,
    senha text,
    sexo character(1)
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 4787 (class 0 OID 16392)
-- Dependencies: 217
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuario (codigo, login, senha, sexo) FROM stdin;
3	maria	maria	F
5	juliana	juliana	F
6	paula	paula	F
7	carol	carol	F
8	joao	joao	M
10	paulo	paulo	M
11	arthur	123	M
9	pedro	123	M
12	arthurteste	1234	M
13	teste	123	M
4	luciana2	luciana	F
\.


--
-- TOC entry 4641 (class 2606 OID 16398)
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (codigo);


-- Completed on 2025-09-12 14:31:04

--
-- PostgreSQL database dump complete
--

\unrestrict 5fY9CnqmbcTBxp1xeMW5aqg6DhhA9F5dVc11JxstkfKNXuottBeRreWJWchXdUN

