-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Giu 29, 2020 alle 11:34
-- Versione del server: 10.1.32-MariaDB
-- Versione PHP: 7.2.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `catering`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `events`
--

CREATE TABLE `events` (
  `id` int(11) NOT NULL,
  `name` varchar(128) DEFAULT NULL,
  `date_start` date DEFAULT NULL,
  `date_end` date DEFAULT NULL,
  `expected_participants` int(11) DEFAULT NULL,
  `organizer_id` int(11) NOT NULL,
  `chef` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `events`
--

INSERT INTO `events` (`id`, `name`, `date_start`, `date_end`, `expected_participants`, `organizer_id`, `chef`) VALUES
(1, 'Convegno Agile Community', '2020-09-25', '2020-09-25', 100, 2, 3),
(2, 'Compleanno di Sara', '2020-08-13', '2020-08-13', 25, 2, 2),
(3, 'Fiera del Sedano Rapa', '2020-10-02', '2020-10-04', 400, 1, 2);

-- --------------------------------------------------------

--
-- Struttura della tabella `kitchenjobs`
--

CREATE TABLE `kitchenjobs` (
  `id` int(11) NOT NULL,
  `time estimate` bigint(20) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `is complete` tinyint(1) DEFAULT '0',
  `assigned cook` int(11) DEFAULT NULL,
  `shift` int(11) DEFAULT NULL,
  `item task` int(11) DEFAULT NULL,
  `service` int(11) NOT NULL,
  `position` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `kitchenjobs`
--

INSERT INTO `kitchenjobs` (`id`, `time estimate`, `quantity`, `is complete`, `assigned cook`, `shift`, `item task`, `service`, `position`) VALUES
(1, NULL, NULL, 0, NULL, NULL, 9, 2, 0),
(2, NULL, NULL, 0, NULL, NULL, 9, 2, 1),
(3, NULL, NULL, 0, NULL, NULL, 10, 2, 2),
(4, 10, 4, 0, NULL, NULL, 11, 2, 3),
(6, NULL, NULL, 0, NULL, NULL, 12, 2, 4),
(7, NULL, NULL, 0, NULL, NULL, 12, 2, 5),
(9, NULL, NULL, 0, NULL, NULL, 1, 8, 0),
(10, NULL, NULL, 0, NULL, NULL, 3, 8, 1),
(11, NULL, NULL, 0, 4, 7, 23, 8, 2),
(12, NULL, NULL, 0, 4, 5, 22, 8, 3),
(13, NULL, NULL, 0, 7, 5, 25, 8, 4),
(14, NULL, NULL, 0, 5, 6, 21, 8, 5),
(15, NULL, NULL, 0, NULL, NULL, 20, 8, 6),
(16, NULL, NULL, 0, NULL, NULL, 8, 8, 7),
(17, NULL, NULL, 0, NULL, NULL, 7, 8, 8),
(18, NULL, NULL, 0, NULL, NULL, 8, 8, 9);

-- --------------------------------------------------------

--
-- Struttura della tabella `kitchenshifts`
--

CREATE TABLE `kitchenshifts` (
  `id` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is complete` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `kitchenshifts`
--

INSERT INTO `kitchenshifts` (`id`, `date`, `is complete`) VALUES
(5, '2020-09-10 08:00:00', 1),
(6, '2020-09-10 07:00:00', 0),
(7, '2020-09-10 09:00:00', 0),
(8, '2020-09-11 07:00:00', 0),
(9, '2020-09-11 08:00:00', 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `kitchentasks`
--

CREATE TABLE `kitchentasks` (
  `id` int(11) NOT NULL,
  `name` tinytext,
  `is recipe` tinyint(1) NOT NULL DEFAULT '1',
  `notes` text,
  `tags` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `kitchentasks`
--

INSERT INTO `kitchentasks` (`id`, `name`, `is recipe`, `notes`, `tags`) VALUES
(1, 'Vitello tonnato', 1, NULL, NULL),
(2, 'Carpaccio di spada', 1, NULL, NULL),
(3, 'Alici marinate', 1, NULL, NULL),
(4, 'Insalata di riso', 1, NULL, NULL),
(5, 'Penne al sugo di baccalà', 1, NULL, NULL),
(6, 'Pappa al pomodoro', 1, NULL, NULL),
(7, 'Hamburger con bacon e cipolla caramellata', 1, NULL, NULL),
(8, 'Salmone al forno', 1, NULL, NULL),
(9, 'Croissant', 1, NULL, NULL),
(10, 'Pane al cioccolato', 1, NULL, NULL),
(11, 'Girelle all\'uvetta', 1, NULL, NULL),
(12, 'Panini al latte', 1, NULL, NULL),
(13, 'Biscotti di pasta frolla', 1, NULL, NULL),
(14, 'Lingue di gatto', 1, NULL, NULL),
(15, 'Bigné farciti', 1, NULL, NULL),
(16, 'Pizzette', 1, NULL, NULL),
(17, 'Tramezzini', 1, NULL, NULL),
(18, 'Sorbetto al limone', 1, NULL, NULL),
(19, 'Torta Saint Honoré', 1, NULL, NULL),
(20, 'Risotto alla zucca', 1, NULL, NULL),
(21, 'Passata di pomodoro', 0, NULL, NULL),
(22, 'Pappa al pomodoro', 0, NULL, NULL),
(23, 'Minestra pomodoro e polpette', 1, NULL, NULL),
(24, 'Polpette di carne', 1, NULL, NULL),
(25, 'Polpette di carne', 0, NULL, NULL);

-- --------------------------------------------------------

--
-- Struttura della tabella `menufeatures`
--

CREATE TABLE `menufeatures` (
  `menu_id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL DEFAULT '',
  `value` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `menufeatures`
--

INSERT INTO `menufeatures` (`menu_id`, `name`, `value`) VALUES
(80, 'Richiede cuoco', 0),
(80, 'Buffet', 0),
(80, 'Richiede cucina', 0),
(80, 'Finger food', 0),
(80, 'Piatti caldi', 0),
(82, 'Richiede cuoco', 0),
(82, 'Buffet', 0),
(82, 'Richiede cucina', 0),
(82, 'Finger food', 0),
(82, 'Piatti caldi', 0),
(86, 'Richiede cuoco', 0),
(86, 'Buffet', 0),
(86, 'Richiede cucina', 0),
(86, 'Finger food', 0),
(86, 'Piatti caldi', 0),
(87, 'Richiede cuoco', 1),
(87, 'Buffet', 1),
(87, 'Richiede cucina', 1),
(87, 'Finger food', 1),
(87, 'Piatti caldi', 1),
(88, 'Richiede cuoco', 1),
(88, 'Buffet', 0),
(88, 'Richiede cucina', 1),
(88, 'Finger food', 0),
(88, 'Piatti caldi', 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `menuitems`
--

CREATE TABLE `menuitems` (
  `id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  `section_id` int(11) DEFAULT NULL,
  `description` tinytext,
  `recipe_id` int(11) NOT NULL,
  `position` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `menuitems`
--

INSERT INTO `menuitems` (`id`, `menu_id`, `section_id`, `description`, `recipe_id`, `position`) VALUES
(96, 80, 0, 'Croissant vuoti', 9, 0),
(97, 80, 0, 'Croissant alla marmellata', 9, 1),
(98, 80, 0, 'Pane al cioccolato mignon', 10, 2),
(99, 80, 0, 'Panini al latte con prosciutto crudo', 12, 4),
(100, 80, 0, 'Panini al latte con prosciutto cotto', 12, 5),
(101, 80, 0, 'Panini al latte con formaggio spalmabile alle erbe', 12, 6),
(102, 80, 0, 'Girelle all\'uvetta mignon', 11, 3),
(103, 82, 0, 'Biscotti', 13, 1),
(104, 82, 0, 'Lingue di gatto', 14, 2),
(105, 82, 0, 'Bigné alla crema', 15, 3),
(106, 82, 0, 'Bigné al caffè', 15, 4),
(107, 82, 0, 'Pizzette', 16, 5),
(108, 82, 0, 'Croissant al prosciutto crudo mignon', 9, 6),
(109, 82, 0, 'Tramezzini tonno e carciofini mignon', 17, 7),
(115, 86, 42, 'Penne alla messinese', 5, 0),
(116, 86, 42, 'Risotto alla zucca', 20, 1),
(118, 86, 44, 'Sorbetto al limone', 18, 0),
(119, 86, 44, 'Torta Saint Honoré', 19, 1),
(120, 86, 0, 'Vitello tonnato', 1, 0),
(121, 86, 0, 'Carpaccio di spada', 2, 1),
(122, 86, 0, 'Alici marinate', 3, 2),
(123, 86, 0, 'Salmone al forno', 8, 0),
(124, 87, 46, 'Vitello tonnato', 1, 0),
(125, 87, 46, 'Carpaccio di spada', 2, 1),
(126, 87, 46, 'Alici marinate', 3, 2),
(127, 87, 46, 'Insalata di riso', 4, 3),
(128, 87, 0, 'Risotto alla zucca', 20, 0),
(129, 87, 0, 'Torta Saint Honoré', 19, 1),
(130, 87, 0, 'Sorbetto al limone', 18, 2),
(131, 88, 47, 'Vitello tonnato', 1, 0),
(132, 88, 47, 'Alici marinate', 3, 1),
(133, 88, 48, 'Zuppa svedese', 23, 0),
(134, 88, 48, 'Risotto alla zucca', 20, 1),
(135, 88, 49, 'Salmone al forno', 8, 0),
(136, 88, 49, 'Hamburger', 7, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `menus`
--

CREATE TABLE `menus` (
  `id` int(11) NOT NULL,
  `title` tinytext,
  `owner_id` int(11) DEFAULT NULL,
  `published` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `menus`
--

INSERT INTO `menus` (`id`, `title`, `owner_id`, `published`) VALUES
(80, 'Coffee break mattutino', 2, 1),
(82, 'Coffee break pomeridiano', 2, 1),
(86, 'Cena di compleanno pesce', 3, 1),
(87, 'Obladì Obladà', 2, 1),
(88, 'Pranzo domenicale', 5, 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `menusections`
--

CREATE TABLE `menusections` (
  `id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  `name` tinytext,
  `position` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `menusections`
--

INSERT INTO `menusections` (`id`, `menu_id`, `name`, `position`) VALUES
(42, 86, 'Primi', 1),
(44, 86, 'Dessert', 3),
(45, 87, 'Antipasti', 0),
(46, 87, 'Antipasti', 0),
(47, 88, 'Antipasti', 0),
(48, 88, 'Primi', 1),
(49, 88, 'Secondi', 2);

-- --------------------------------------------------------

--
-- Struttura della tabella `roles`
--

CREATE TABLE `roles` (
  `id` char(1) NOT NULL,
  `role` varchar(128) NOT NULL DEFAULT 'servizio'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `roles`
--

INSERT INTO `roles` (`id`, `role`) VALUES
('c', 'cuoco'),
('h', 'chef'),
('o', 'organizzatore'),
('s', 'servizio');

-- --------------------------------------------------------

--
-- Struttura della tabella `services`
--

CREATE TABLE `services` (
  `id` int(11) NOT NULL,
  `event_id` int(11) NOT NULL,
  `name` varchar(128) DEFAULT NULL,
  `proposed_menu_id` int(11) NOT NULL DEFAULT '0',
  `approved_menu_id` int(11) DEFAULT '0',
  `service_date` date DEFAULT NULL,
  `time_start` time DEFAULT NULL,
  `time_end` time DEFAULT NULL,
  `expected_participants` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `services`
--

INSERT INTO `services` (`id`, `event_id`, `name`, `proposed_menu_id`, `approved_menu_id`, `service_date`, `time_start`, `time_end`, `expected_participants`) VALUES
(1, 2, 'Cena', 86, 0, '2020-08-13', '20:00:00', '23:30:00', 25),
(2, 1, 'Coffee break mattino', 0, 80, '2020-09-25', '10:30:00', '11:30:00', 100),
(3, 1, 'Colazione di lavoro', 0, 0, '2020-09-25', '13:00:00', '14:00:00', 80),
(4, 1, 'Coffee break pomeriggio', 0, 82, '2020-09-25', '16:00:00', '16:30:00', 100),
(5, 1, 'Cena sociale', 0, 0, '2020-09-25', '20:00:00', '22:30:00', 40),
(6, 3, 'Pranzo giorno 1', 0, 0, '2020-10-02', '12:00:00', '15:00:00', 200),
(7, 3, 'Pranzo giorno 2', 0, 0, '2020-10-03', '12:00:00', '15:00:00', 300),
(8, 3, 'Pranzo giorno 3', 0, 88, '2020-10-04', '12:00:00', '15:00:00', 400);

-- --------------------------------------------------------

--
-- Struttura della tabella `shiftavailablecooks`
--

CREATE TABLE `shiftavailablecooks` (
  `shift` int(11) NOT NULL,
  `cook` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `shiftavailablecooks`
--

INSERT INTO `shiftavailablecooks` (`shift`, `cook`) VALUES
(5, 4),
(5, 7),
(6, 5),
(6, 6),
(7, 4),
(7, 7),
(8, 5),
(8, 6),
(8, 7),
(9, 4),
(9, 5);

-- --------------------------------------------------------

--
-- Struttura della tabella `usedpreparations`
--

CREATE TABLE `usedpreparations` (
  `recipe` int(11) NOT NULL,
  `preparation` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `usedpreparations`
--

INSERT INTO `usedpreparations` (`recipe`, `preparation`) VALUES
(6, 21),
(16, 21),
(22, 21),
(23, 22),
(23, 25);

-- --------------------------------------------------------

--
-- Struttura della tabella `userroles`
--

CREATE TABLE `userroles` (
  `user_id` int(11) NOT NULL,
  `role_id` char(1) NOT NULL DEFAULT 's'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `userroles`
--

INSERT INTO `userroles` (`user_id`, `role_id`) VALUES
(1, 'o'),
(2, 'o'),
(2, 'h'),
(3, 'h'),
(4, 'h'),
(4, 'c'),
(5, 'c'),
(6, 'c'),
(7, 'c'),
(8, 's'),
(9, 's'),
(10, 's'),
(7, 's');

-- --------------------------------------------------------

--
-- Struttura della tabella `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(128) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `users`
--

INSERT INTO `users` (`id`, `username`) VALUES
(1, 'Carlin'),
(2, 'Lidia'),
(3, 'Tony'),
(4, 'Marinella'),
(5, 'Guido'),
(6, 'Antonietta'),
(7, 'Paola'),
(8, 'Silvia'),
(9, 'Marco'),
(10, 'Piergiorgio');

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `events`
--
ALTER TABLE `events`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `kitchenjobs`
--
ALTER TABLE `kitchenjobs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `assigned cook` (`assigned cook`),
  ADD KEY `item task` (`item task`),
  ADD KEY `shift` (`shift`),
  ADD KEY `service` (`service`);

--
-- Indici per le tabelle `kitchenshifts`
--
ALTER TABLE `kitchenshifts`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `kitchentasks`
--
ALTER TABLE `kitchentasks`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `menuitems`
--
ALTER TABLE `menuitems`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `menus`
--
ALTER TABLE `menus`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `menusections`
--
ALTER TABLE `menusections`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `services`
--
ALTER TABLE `services`
  ADD PRIMARY KEY (`id`),
  ADD KEY `event_id` (`event_id`);

--
-- Indici per le tabelle `shiftavailablecooks`
--
ALTER TABLE `shiftavailablecooks`
  ADD PRIMARY KEY (`shift`,`cook`),
  ADD KEY `cook` (`cook`);

--
-- Indici per le tabelle `usedpreparations`
--
ALTER TABLE `usedpreparations`
  ADD PRIMARY KEY (`recipe`,`preparation`),
  ADD KEY `preparation` (`preparation`);

--
-- Indici per le tabelle `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `events`
--
ALTER TABLE `events`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT per la tabella `kitchenjobs`
--
ALTER TABLE `kitchenjobs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT per la tabella `kitchenshifts`
--
ALTER TABLE `kitchenshifts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT per la tabella `kitchentasks`
--
ALTER TABLE `kitchentasks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT per la tabella `menuitems`
--
ALTER TABLE `menuitems`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=137;

--
-- AUTO_INCREMENT per la tabella `menus`
--
ALTER TABLE `menus`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=89;

--
-- AUTO_INCREMENT per la tabella `menusections`
--
ALTER TABLE `menusections`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;

--
-- AUTO_INCREMENT per la tabella `services`
--
ALTER TABLE `services`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT per la tabella `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `kitchenjobs`
--
ALTER TABLE `kitchenjobs`
  ADD CONSTRAINT `kitchenjobs_ibfk_1` FOREIGN KEY (`assigned cook`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `kitchenjobs_ibfk_2` FOREIGN KEY (`item task`) REFERENCES `kitchentasks` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `kitchenjobs_ibfk_4` FOREIGN KEY (`service`) REFERENCES `services` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `services`
--
ALTER TABLE `services`
  ADD CONSTRAINT `services_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `events` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `shiftavailablecooks`
--
ALTER TABLE `shiftavailablecooks`
  ADD CONSTRAINT `shiftavailablecooks_ibfk_2` FOREIGN KEY (`cook`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `shiftavailablecooks_ibfk_3` FOREIGN KEY (`shift`) REFERENCES `kitchenshifts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `usedpreparations`
--
ALTER TABLE `usedpreparations`
  ADD CONSTRAINT `usedpreparations_ibfk_1` FOREIGN KEY (`recipe`) REFERENCES `kitchentasks` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `usedpreparations_ibfk_2` FOREIGN KEY (`preparation`) REFERENCES `kitchentasks` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
