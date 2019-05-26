Membri:

  - Jean Carlo Meza Yépez, 3925210
  - Ivan Sibilla, 4332550
  - Damiano Valenza, 3782594

Commenti: 

  Per la realizzazione del progetto è stato utilizzato il jdk versione 11, pertanto,
  è stato utilizzato il metodo parseInt (riga 84, lpp.parser.StreamTokenizer), in una versione
  non presente nelle versioni del jdk inferiori alla 9. Se si desidera utilizzare una versione inferiore,
  è possibile sostituire tale riga con:

    return Integer.parseInt(tokenString.substring(2, tokenString.length()), 16);
