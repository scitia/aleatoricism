# Aleatoric Execution Chain as non-deterministic decision proces integrated into server side aplication modelling

Koncpecja aleatoryczna wywodzi się z techniki kompozytorskiej w muzyce współczesnej polegającej na dopuszczeniu przez kompozytora przypadkowości podczas wykonywania kompozycji w zakresie pewnych jej elementów, zakładając tym samym niepowtarzalność samego wykonania

Aleatoryczny łańcuch wywołań to wzorzec niedeterministycznego sposobu podejmowania decyzji wywoływania odpowiednich metod przez narzędzia agentyczne zintegrowane w aplikacje serwerowe.

Zamiast polegać na skompikowanych warstawch serwisów, które mogą zostać napisane w sposób trudny do interpretacji i utrzymania - aplikacja może wystawiać API które w sposb ogólny definiuje interfejs, np.: 

- ścieżki biznesowe (duże aplikacje - wdrożenia rynkowe) - sposób bezpieczny, kontrolowany
- ogólne usługi baz danych, połączeń do serwisów zewnętrznych itd. - bardzo ogólny, wydaje się, że nie na dzisiejsze czasy

Idea jest taka, aby to agent decydował na podstawie danych otrzymanych z punktu wejścia (np.: wywołanie enpoint'a REST przez dowolnego klienta) do czego te dane są przeznaczone i gdzie ma pokierować je dalej celem odpowiedniego wykonania kodu aplikacji.

W takim przypadku aplikacja serwerowa mogłaby mieć nawet tylko kilka punktów wejścia:
- POST mapping -> klasycznie dla tworzenia zasobów
- GET mapping -> pozyskiwanie treści
- DELTE mapping -> dla usuwania treści
- PATCH mapping -> aktualizacja treści

Wystawione usługi definiowałyby dane wejściowe jako mapę ```Map<String,Object>```
dzięki czemu struktura obiektów JSON które będą docierały na serwer nie będzie w żaden sposób wymuszona. 

Wtedy agent podejmuje decyzję którą ścieżkę wykonać aby zrealizować żądanie klienta. Agent mógłby dostać w REQUEST dane wspomagające/zapewniające poprawność wykonania żądania, nawet w postaci tekstu napisanego językiem naturalnym, który będzie doprecyzowywał żądanie. 

Wtedy narzędzie agentyczne będzie w stanie przygotować odpowiednie dane wejściowe dla procesu i wykonać proces. Jeśli procesu nie uda się wykonać ze względu na braki - klasycznie będziemy mogli rzucić wyjątek i odpowiednio zakończyć wykonywanie żądania HTTP z odpowiednim statusem.

Dzięki takiemu podejściu - modelujemy tylko procesy biznesowe w aplikacjach, nie potrzebujemy skompikowanej logiki. Proces wymaga danych - więc z naszej perspektywy jest to tylko rekord/klasa, etc. 

Proces ma generować dane wyjściowe, zatem również są one dobrze określone jako rekord/klasa etc.

Agent może dostać wskazania w postaci plików markdown, które będą dodatkowo definiować jak powinien się zachowywać w wątpliwych sytuacjach. To sprowadza programowanie do pisania ograniczeń językiem naturalnym i sprawia, że programiści skupiają się na biznesie a nie na przypadkach brzegowych.

Implementacja:

Wykorzystując Copilot ADK dla języka Java realizujemy wystawienie ścieżek biznesowych zdefiniowanych w aplikacji jako dodatkowe funkcje dla narzędzia agentycznego. 

Ujednloicamy API serwera aplikacyjnego tylko do wymaganych i uogólnionych puntków dostępu - zupełnie ignorując zasady tworzenia REST.