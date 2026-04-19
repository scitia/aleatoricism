# Business Graph Software Modelling

Ideologia projektowania aplikacji jako grafów skierowanych (ścieżek biznesowych), w których istnieją punty wejścia (rozpoczynające proces) oraz potencjalnie wiele punktów wyjścia (główny - zwrócenie wyniku oraz skutki uboczne, odgałęzienia ścieżek które mogą na przykład realizwoać wysyłkę danych na kolejki poprzez protokół MQTT).

Elementy składowe grafu:
- Entry Point -> tylko teoretyczny element, w praktyce wszystko jest Waypoint
- Waypoint - element pośredni procesu (X) lub wejściowy procesu (Z)
- Closed Output Point - zwraca wartość `<T>`
- Open Output Point - `Void`

Waypoint -> parametryzowany poprzez typ wejściowy oraz wyjściowy:

```
Waypoint<Input,Output>
```

Closed Output Point -> podstawowy element końcowy ścieżki biznesowej który zwraca wartość (lub nie jeśli typ wyjściowy określony jest poprzez Void)

Open Output Point -> zarezerwowane dla skutków ubocznych, np wysyłka na kolejki, inne operacje asynchroniczne

W praktyce Open/Closed Output Point to wciąż Waypoint

Taka struktura zmusza do projektowania aplikacji w sposób przemyślany i zoptymalizowany. Daje do myślenia, które elementy procesu są wspólne.

Zawsze mamy dokładnie widoczną kolejność wykonywania poszczególnych podprocesów w ramach procesu głównego. Zwiększa to czytelność i redukuje poziom wejścia w dany projekt z punktu widzenia dewelopera.

Klasy które będą builderami ścieżek biznesowych powinny posiadać metadane (adnotacje), które będą opisywać do czego dany etap służy. W ramach adnotacji może znajdować się nazwa procesu, jej opis zrealizowany w języku natrualnym, itp.

Rozbicie struktury warstwowej aplikacji. Punktem wejścia w aplikacji webowej nadal może być enpoint, np. REST, natomiast operacje realizowane są w ramach danego punktu w ścieżce. Ścieżka jest zwykłym grafem. W warstwie kontrolerów REST jest wykonywana metoda wykonawcza dla scieżki biznesowej, która może składać się z wielu Waypoint'ów.

Pomysły implementacyjne:

W związku z tym, że graf jest strukturą, którą można rozbić na podstruktury (podgrafy) to implementację można byłoby zrealizować poprzez koncepcję algebry - sealed classes and records. Graf podlegałby dekompozycji na podgrafy, gdzie zawsze wyciągamy pierwszy Waypoint, wykonujemy i rozbijamy graf na podgrafy aż do ostatniego elementu i wykonujemy poszczególne punkty wykonawcze. Zaletą tutaj jest, że możemy zrównoleglać operacje wykonywane jako Waypoint'y podstawowe (pośrednie w ścieżce biznesowej) oraz emisyjne, które będą generowały skutki uboczne.