# A3

## 4-Sichten-Modell
![](resources/Komponentendiagramm.png)

## Change Requests
>CR1: Die aktuelle Konfiguration einer Laufzeitumgebung (welche Komponenten wurden in welcher Reihenfolge eingesetzt)
> soll stets persistent auf einem externen Server abgespeichert werden, so dass bei einem Absturz der lokalen 
> Laufzeitumgebung der Zustand (= die Konfiguration) der Laufzeitumgebung wiederhergestellt werden kann.

>CR2: Erweitern sie ihre Laufzeitumgebung um einen horizontalen Logging-Dienst, der von ihrer Laufzeitumgebung für alle
> kompatiblen Komponenten angeboten werden soll.

**Siehe:**
- [Component](Component.java)
```
    void inject(Object object){
        Class<?> c = object.getClass();
        for (Field field: c.getDeclaredFields()) {
            if (field.isAnnotationPresent(A3.Inject.class)){
                try {
                    field.set(object, new Logger());
                }
                catch (Exception e){
                    System.err.println("Error injecting Object: " + e.getMessage());
                }
            }
        }
    }
```
- [Inject](Inject.java)
- [Logger](Logger.java)
- [LoggerFactory](LoggerFactory.java)

