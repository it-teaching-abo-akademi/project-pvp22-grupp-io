--
-- PostgreSQL database dump
--

-- Dumped from database version 14.5 (Ubuntu 14.5-0ubuntu0.22.04.1)
-- Dumped by pg_dump version 14.5 (Ubuntu 14.5-0ubuntu0.22.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
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
-- Name: order; Type: TABLE; Schema: public; Owner: pvp_user
--

CREATE TABLE public."order" (
    id integer NOT NULL,
    user_id integer,
    total_price integer
);


ALTER TABLE public."order" OWNER TO pvp_user;

--
-- Name: order_id_seq; Type: SEQUENCE; Schema: public; Owner: pvp_user
--

CREATE SEQUENCE public.order_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.order_id_seq OWNER TO pvp_user;

--
-- Name: order_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pvp_user
--

ALTER SEQUENCE public.order_id_seq OWNED BY public."order".id;


--
-- Name: order_line; Type: TABLE; Schema: public; Owner: pvp_user
--

CREATE TABLE public.order_line (
    id integer NOT NULL,
    total_price integer NOT NULL,
    unit_price integer,
    quantity integer NOT NULL,
    order_id integer NOT NULL,
    product_id integer
);


ALTER TABLE public.order_line OWNER TO pvp_user;

--
-- Name: order_line_id_seq; Type: SEQUENCE; Schema: public; Owner: pvp_user
--

CREATE SEQUENCE public.order_line_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.order_line_id_seq OWNER TO pvp_user;

--
-- Name: order_line_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pvp_user
--

ALTER SEQUENCE public.order_line_id_seq OWNED BY public.order_line.id;


--
-- Name: payment; Type: TABLE; Schema: public; Owner: pvp_user
--

CREATE TABLE public.payment (
    id integer NOT NULL,
    amount integer NOT NULL,
    payment_type_id integer NOT NULL,
    order_id integer NOT NULL
);


ALTER TABLE public.payment OWNER TO pvp_user;

--
-- Name: payment_id_seq; Type: SEQUENCE; Schema: public; Owner: pvp_user
--

CREATE SEQUENCE public.payment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.payment_id_seq OWNER TO pvp_user;

--
-- Name: payment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pvp_user
--

ALTER SEQUENCE public.payment_id_seq OWNED BY public.payment.id;


--
-- Name: payment_type; Type: TABLE; Schema: public; Owner: pvp_user
--

CREATE TABLE public.payment_type (
    id integer NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.payment_type OWNER TO pvp_user;

--
-- Name: payment_type_id_seq; Type: SEQUENCE; Schema: public; Owner: pvp_user
--

CREATE SEQUENCE public.payment_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.payment_type_id_seq OWNER TO pvp_user;

--
-- Name: payment_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pvp_user
--

ALTER SEQUENCE public.payment_type_id_seq OWNED BY public.payment_type.id;


--
-- Name: product; Type: TABLE; Schema: public; Owner: pvp_user
--

CREATE TABLE public.product (
    id integer NOT NULL,
    sku character varying(100) NOT NULL,
    price integer NOT NULL,
    name character varying(100)
);


ALTER TABLE public.product OWNER TO pvp_user;

--
-- Name: product_id_seq; Type: SEQUENCE; Schema: public; Owner: pvp_user
--

CREATE SEQUENCE public.product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.product_id_seq OWNER TO pvp_user;

--
-- Name: product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pvp_user
--

ALTER SEQUENCE public.product_id_seq OWNED BY public.product.id;


--
-- Name: user; Type: TABLE; Schema: public; Owner: pvp_user
--

CREATE TABLE public."user" (
    id integer NOT NULL,
    customer_reference uuid NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public."user" OWNER TO pvp_user;

--
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: pvp_user
--

CREATE SEQUENCE public.user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_id_seq OWNER TO pvp_user;

--
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pvp_user
--

ALTER SEQUENCE public.user_id_seq OWNED BY public."user".id;


--
-- Name: order id; Type: DEFAULT; Schema: public; Owner: pvp_user
--

ALTER TABLE ONLY public."order" ALTER COLUMN id SET DEFAULT nextval('public.order_id_seq'::regclass);


--
-- Name: order_line id; Type: DEFAULT; Schema: public; Owner: pvp_user
--

ALTER TABLE ONLY public.order_line ALTER COLUMN id SET DEFAULT nextval('public.order_line_id_seq'::regclass);


--
-- Name: payment id; Type: DEFAULT; Schema: public; Owner: pvp_user
--

ALTER TABLE ONLY public.payment ALTER COLUMN id SET DEFAULT nextval('public.payment_id_seq'::regclass);


--
-- Name: payment_type id; Type: DEFAULT; Schema: public; Owner: pvp_user
--

ALTER TABLE ONLY public.payment_type ALTER COLUMN id SET DEFAULT nextval('public.payment_type_id_seq'::regclass);


--
-- Name: product id; Type: DEFAULT; Schema: public; Owner: pvp_user
--

ALTER TABLE ONLY public.product ALTER COLUMN id SET DEFAULT nextval('public.product_id_seq'::regclass);


--
-- Name: user id; Type: DEFAULT; Schema: public; Owner: pvp_user
--

ALTER TABLE ONLY public."user" ALTER COLUMN id SET DEFAULT nextval('public.user_id_seq'::regclass);


--
-- Data for Name: order; Type: TABLE DATA; Schema: public; Owner: pvp_user
--

COPY public."order" (id, user_id, total_price) FROM stdin;
\.


--
-- Data for Name: order_line; Type: TABLE DATA; Schema: public; Owner: pvp_user
--

COPY public.order_line (id, total_price, unit_price, quantity, order_id, product_id) FROM stdin;
\.


--
-- Data for Name: payment; Type: TABLE DATA; Schema: public; Owner: pvp_user
--

COPY public.payment (id, amount, payment_type_id, order_id) FROM stdin;
\.


--
-- Data for Name: payment_type; Type: TABLE DATA; Schema: public; Owner: pvp_user
--

COPY public.payment_type (id, name) FROM stdin;
\.


--
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: pvp_user
--

COPY public.product (id, sku, price, name) FROM stdin;
1	new-product	10	\N
2	new-product	10	\N
3	new-product	10	\N
4	new-product	10	\N
5	new-product1	10	new-product1
6	new-product11	10	\N
7	new-product11	10	Name of product
\.


--
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: pvp_user
--

COPY public."user" (id, customer_reference, name) FROM stdin;
1	abaf5146-333f-4a44-8b56-55790a79e8eb	testing name
2	3662c8c9-4beb-4538-a3c4-f323a26baa44	testing name
3	87c7b4a4-adc8-4824-8266-16e6dc985b11	testing name
\.


--
-- Name: order_id_seq; Type: SEQUENCE SET; Schema: public; Owner: pvp_user
--

SELECT pg_catalog.setval('public.order_id_seq', 1, false);


--
-- Name: order_line_id_seq; Type: SEQUENCE SET; Schema: public; Owner: pvp_user
--

SELECT pg_catalog.setval('public.order_line_id_seq', 1, false);


--
-- Name: payment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: pvp_user
--

SELECT pg_catalog.setval('public.payment_id_seq', 1, false);


--
-- Name: payment_type_id_seq; Type: SEQUENCE SET; Schema: public; Owner: pvp_user
--

SELECT pg_catalog.setval('public.payment_type_id_seq', 1, false);


--
-- Name: product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: pvp_user
--

SELECT pg_catalog.setval('public.product_id_seq', 7, true);


--
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: pvp_user
--

SELECT pg_catalog.setval('public.user_id_seq', 3, true);


--
-- Name: order_line order_line_pkey; Type: CONSTRAINT; Schema: public; Owner: pvp_user
--

ALTER TABLE ONLY public.order_line
    ADD CONSTRAINT order_line_pkey PRIMARY KEY (id);


--
-- Name: order order_pkey; Type: CONSTRAINT; Schema: public; Owner: pvp_user
--

ALTER TABLE ONLY public."order"
    ADD CONSTRAINT order_pkey PRIMARY KEY (id);


--
-- Name: payment payment_pkey; Type: CONSTRAINT; Schema: public; Owner: pvp_user
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT payment_pkey PRIMARY KEY (id);


--
-- Name: payment_type payment_type_pkey; Type: CONSTRAINT; Schema: public; Owner: pvp_user
--

ALTER TABLE ONLY public.payment_type
    ADD CONSTRAINT payment_type_pkey PRIMARY KEY (id);


--
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: pvp_user
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: pvp_user
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: order_line order_fk; Type: FK CONSTRAINT; Schema: public; Owner: pvp_user
--

ALTER TABLE ONLY public.order_line
    ADD CONSTRAINT order_fk FOREIGN KEY (order_id) REFERENCES public."order"(id) ON DELETE RESTRICT;


--
-- Name: payment payment_line_fk; Type: FK CONSTRAINT; Schema: public; Owner: pvp_user
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT payment_line_fk FOREIGN KEY (order_id) REFERENCES public."order"(id) ON DELETE RESTRICT;


--
-- Name: payment payment_type_fk; Type: FK CONSTRAINT; Schema: public; Owner: pvp_user
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT payment_type_fk FOREIGN KEY (payment_type_id) REFERENCES public.payment_type(id) ON DELETE RESTRICT;


--
-- Name: order_line product_fk; Type: FK CONSTRAINT; Schema: public; Owner: pvp_user
--

ALTER TABLE ONLY public.order_line
    ADD CONSTRAINT product_fk FOREIGN KEY (product_id) REFERENCES public.product(id) ON DELETE RESTRICT;


--
-- Name: order user_fk; Type: FK CONSTRAINT; Schema: public; Owner: pvp_user
--

ALTER TABLE ONLY public."order"
    ADD CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES public."user"(id) ON DELETE RESTRICT;


--
-- PostgreSQL database dump complete
--

