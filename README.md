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

## Initialiser la base

Sur Windows (exemple valide avec l'utilisateur PostgreSQL `postgres`) :

```powershell
createdb -U postgres cinema
psql -U postgres -d cinema -f database/cinema_init.sql
```

Si PostgreSQL n'est pas demarre, vous pouvez le lancer avec :

```powershell
"C:/Users/U1048315/scoop/apps/postgresql/current/bin/pg_ctl.exe" -D "C:/Users/U1048315/scoop/apps/postgresql/current/data" -l "C:/Users/U1048315/scoop/apps/postgresql/current/data/server.log" start
```

## Compiler et lancer (mode simple)

```powershell
if (Test-Path bin) { Remove-Item -Recurse -Force bin }
New-Item -ItemType Directory -Path bin | Out-Null
$sources = Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName }
javac -d bin $sources
java -cp "bin;lib/*" Main
```

Vous pouvez aussi utiliser `run.ps1` pour executer ces etapes en une seule commande.

## JDBC

Si vous n'utilisez pas Maven, placez le driver PostgreSQL dans `lib/postgresql-42.7.0.jar`.
