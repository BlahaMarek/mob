TODO
•  pridanie dat do modelview - kontakty aj roomky su, pridat posty a chat (po notifikaciach)

•  Obrazovka konverzacie
	    umoznuje napisanie novej spravy obsahujucej len jeden gif pouzivatelovi kedykolvek na spodku obrazovky.

•  Uložiť FID (Id pre firebase cloud messaging) na server pomocou webservisu user/fid.php
•  Odoslanie notifikacie používateľovi (FCM nižšie), ktorému sme poslali privátnu správu. Zachytenie a zobrazenie notifikácii u príjemcu. (Použiť FID, ktoré vráti webservis pri čítaní konverzácii)
•  Odoslanie notifikacie WiFi skupine (FCM nižšie), do ktorej sme poslali príspevok. Zachytenie a zobrazenie notifikácii u člena skupiny len v prípade, že je aktuálne pripojený na WiFi sieť skupiny. (Použiť FID, ako názov WiFi skupiny)

+ pridat lokalnu db

OTAZKY
•	posty – realtime ? chyba fid - ano, treba real time
•	TokenAutenticator -> je tam execute metoda spravna ?
