-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le :  mer. 27 mars 2019 à 15:23
-- Version du serveur :  10.1.37-MariaDB
-- Version de PHP :  7.2.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `admiral`
--

-- --------------------------------------------------------

--
-- Structure de la table `classe`
--

CREATE TABLE `classe` (
  `IDCLASSE` int(5) NOT NULL,
  `NOMCLASSE` varchar(55) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `classe`
--

INSERT INTO `classe` (`IDCLASSE`, `NOMCLASSE`) VALUES
(1, 'I2G1'),
(2, 'I2G2');

-- --------------------------------------------------------

--
-- Structure de la table `etudiant`
--

CREATE TABLE `etudiant` (
  `IDETUDIANT` int(5) NOT NULL,
  `IDCLASSE` int(11) NOT NULL,
  `NOMETUDIANT` varchar(55) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `etudiant`
--

INSERT INTO `etudiant` (`IDETUDIANT`, `IDCLASSE`, `NOMETUDIANT`) VALUES
(1, 1, 'Sielinou'),
(2, 2, 'nana'),
(5, 1, 'Taheu');

-- --------------------------------------------------------

--
-- Structure de la table `professeur`
--

CREATE TABLE `professeur` (
  `idprof` int(5) NOT NULL,
  `nomProf` varchar(55) NOT NULL,
  `login` varchar(30) NOT NULL,
  `mdp` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `professeur`
--

INSERT INTO `professeur` (`idprof`, `nomProf`, `login`, `mdp`) VALUES
(1, 'Sielinou', 'juniorsielinou@yahoo.com', 'junior'),
(2, 'Tchoule', 'morvanne@yahoo.com', 'morvanne');

-- --------------------------------------------------------

--
-- Structure de la table `statetudiant`
--



--
-- Déchargement des données de la table `statetudiant`
--


-- --------------------------------------------------------

--
-- Structure de la table `statut`
--

CREATE TABLE `statut` (
  `IDSTATUT` int(5) NOT NULL,
  `libelle` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `statut`
--

INSERT INTO `statut` (`IDSTATUT`, `libelle`) VALUES
(1, 'present'),
(2, 'absent'),
(3, 'retard');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `classe`
--
ALTER TABLE `classe`
  ADD PRIMARY KEY (`IDCLASSE`);

--
-- Index pour la table `etudiant`
--
ALTER TABLE `etudiant`
  ADD PRIMARY KEY (`IDETUDIANT`),
  ADD KEY `FK_FAIRE` (`IDCLASSE`);

--
-- Index pour la table `professeur`
--
ALTER TABLE `professeur`
  ADD PRIMARY KEY (`idprof`);


-- Index pour la table `statut`
--
ALTER TABLE `statut`
  ADD PRIMARY KEY (`IDSTATUT`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `classe`
--
ALTER TABLE `classe`
  MODIFY `IDCLASSE` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `etudiant`
--
ALTER TABLE `etudiant`
  MODIFY `IDETUDIANT` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `professeur`
--
ALTER TABLE `professeur`
  MODIFY `idprof` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `statut`
--
ALTER TABLE `statut`
  MODIFY `IDSTATUT` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `etudiant`
--
ALTER TABLE `etudiant`
  ADD CONSTRAINT `FK_FAIRE` FOREIGN KEY (`IDCLASSE`) REFERENCES `classe` (`IDCLASSE`);


CREATE TABLE statetudiant (
  IDSTATUT int(11) NOT NULL,
  IDETUDIANT int(11) NOT NULL,
  DATE_PERIODE varchar(25) NOT NULL,
  primary key(IDSTATUT,IDETUDIANT,DATE_PERIODE),
  FOREIGN KEY (IDSTATUT) REFERENCES statut (IDSTATUT),
  FOREIGN KEY (IDETUDIANT) REFERENCES etudiant (IDETUDIANT)
);

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
