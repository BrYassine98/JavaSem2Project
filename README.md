# CinemaApp

Application Java desktop (MVC + DAO) pour gerer un catalogue cinema.

## Structure

- `src/models` : entites metier
- `src/dao` : acces base de donnees PostgreSQL
- `src/controllers` : logique applicative
- `src/views` : interface Swing
- `database/cinema_init.sql` : script d'initialisation

## Prerequis

- Java 17+
- PostgreSQL

## Lancer (mode simple)

Compile et execute depuis `src` avec votre IDE, ou adaptez vers Maven si besoin.

## JDBC

Si vous n'utilisez pas Maven, placez le driver PostgreSQL dans `lib/postgresql-42.7.0.jar`.
