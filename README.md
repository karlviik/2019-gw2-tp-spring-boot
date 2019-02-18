# API -> DB -> web

Tõmbab Guild Wars 2 APIst live infot, salvestab andmebaasi, vastavatele esemetele 
arvutab loomise hinna ja lõhkumise väärtuse. Otsa veebisait, kus seda infot saab 
visuaalselt esitada.

## Liikmed

- Karl Viik (kaviik)
- Costa Rica Tarrazu, keskmine rõst
- Brasiilia Cerrado Bandeirante, keskmine rõst

## Funktsionaalsus

#### API -> DB osa
- tõmbab vastavatest endpointidest APIst infot
  - https://wiki.guildwars2.com/wiki/API:2/items Siit tõmbab kõik esemed eraldi 
  database'i, ainult olulise info, nt game_types pole oluline. Esemete detailid 
  vastavalt tüübi järgi ka eraldi database'idesse arvatavasti.
  - https://wiki.guildwars2.com/wiki/API:2/files Seda kasutab, et esemete ikoonid alla
  laadida, et veebis näitamiseks igal esemel seda ei küsiks. Eraldi andmebaas.
  - https://wiki.guildwars2.com/wiki/API:2/build Siit saab praeguse game build ID, saab
  kasutada update'i detectimiseks, et alustada tihedamat uuenduste otsimist teistes
  API endpointides.
  - https://wiki.guildwars2.com/wiki/API:2/currencies Siit saab "valuutade" IDd, nimed
  ja ikoonid kätte, mida kasutaks just ikoonide jaoks.
  - https://wiki.guildwars2.com/wiki/API:2/recipes Siit saab kätte mängus eksisteerivad 
  retseptid, mida kasutab odavaima "loomise" puu arvutamiseks igale esemele.
  - https://wiki.guildwars2.com/wiki/API:2/commerce/prices Iga mingi ajahetk tõmbab 
  kõikide "vahetatavate" esemete ostu- ja müügihinnad sealt ja paneb database'i.
  - https://wiki.guildwars2.com/wiki/API:2/commerce/listings Iga mingi ajahetk tõmbab 
  ja salvestab ajutiselt database'i, ehk ei salvesta ajaloolist infot, mahud liiga suured.
- kalkuleerib andmebaasi loomise hinna retseptide database'i ja ostu-müügihindade põhjal
- kalkuleeerib valitud esemete "lõhkumise" hinna eseme /items database'i ja praeguste 
hindade põhjal (piiratud esemete hulk, nõuab mängusisest research'i)

#### DB -> web osa
- veebilehel saab minna iga individuaalse eseme lehele, kus vahib vastu praegune 
ostu-müügihind kui ka ajalooline graafik, kus kui eset on võimalik "luua", siis 
on ka selle väärtus graafil. Ie https://www.gw2tp.com/
- graafi info saadakse maagiaga database'ist kasutaja browserini toredal kujul.



## Tehniline info

Kasutatav API on Guild Wars 2 API version 2 https://api.guildwars2.com/v2. Veebilehe osa 
pole veel väga uurinud, aga sinna midagi ikka tuleb. Bootstrap? API DB vahel 
Java, pole veel 100% kindel, kas midagi keerulisemat on vaja. Nagunii on.

## Punktisoov

55 - 75p oleks tore saada.

## Ekraanivaated

Ainuke asi, mis kasutajale on nähtav, on veebisaidi lehed. Need umbes nagu 
https://www.gw2tp.com/, kuid enda disainini jõuab tulevikus ohvrite feedbacki abil.
Ka search funktsiooni peab implementeerima siiski, mis laseb vastavate filtrite põhjal 
otsida andmebaasi esemete seast.

## Ajaplaan

- 2\. nädal: nokitsemine GW2 APIga
- 3\. nädal: veel rohkem torkimist, kiirelt selle README kirjutamine
- 4\. nädal: projektiplaani esitamine, esimesed sammud API ja DB vahelise osa jaoks
- 5\. nädal: items endpointist info tõmbamise funktsionaalsus ehk tööle saada
- 6\. nädal: prices endpointist info tõmbamise funktsionaalsus
- 7\. nädal: ülejäänud endpointidest, veebilehe alused
- 8\. nädal: retsepti väärtuse kalkuleerimise funktsionaalsus, veebilehe ehitamine
- 9\. nädal: ehk saada kõik see elama panna serverile, veel veebilehte
- 10\. nädal: search funktsionaalsus, ehk veebileht juba näeb veebileht välja?
- 11\. nädal: märkamine, et 3 nädalat maas, paaniliselt ringiratast jooksmine 
- 12\. nädal: 15 magamata ööd ühe nädalaga.
- 13\. nädal: Veel 15 magamata ööd teise nädalaga.
- 14\. nädal: Lõplik esitamine nii siia kui ka Guild Wars 2 communityle ja selle läbi 
serveri tapmine.