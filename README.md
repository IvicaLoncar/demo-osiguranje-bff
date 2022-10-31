# Opis funkcionalnosti Demo osiguranje react FE 

## Programirano u 

Eclipse

## Kratki opis

Demo služi za demonstraciju BFF dijela poslovne aplikacije koja se u pozadini spaja na H2 in-memory sql bazu podataka, arihtektura se sastoji od tri sloja: kontrolera, servisa i data access sloja. Dodatno je dorađena s nekim funkcionalnostima kao što su slanje poruka u bilo kojem slučaju i ručno upravljanje tranaskcijama.

## Tehnologije

Koriste se:
- java spring boot
- filteri - za logiranje request i response u konzolu
- aop - za logiranje poziva metoda u konzolu 
- H2 baza podataka - za spremanje podataka
- lombok
- log4j2
- spring boot devtools

## Implementirano

Unutar demo projekta implementirano je:
- logiranje u konzolu 
- rest endpointovi GET, POST, PUT, PATCH, DELETE
- parcijalni GET (vraća samo željene kolone)
- data pagging
- podrška za 3 tablice u bazi podataka (klijenti, vrste osiguranja i police)
- dodatna dorada slanja poruka
- mogućnost ručnog upravljanja transakcijama

