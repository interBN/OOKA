# A2

## Troubleshooting

Deactivate annotations processing:

```
https://stackoverflow.com/questions/27223917/how-to-configure-annotations-processing-in-intellij-idea-14-for-current-project
```

## Aufgaben

> FA1: Der Component Assembler muss die Laufzeitumgebung starten können.

**Siehe Client:**

```
ComponentAssembler runnable = new ComponentAssembler();
Thread thread = new Thread(runnable);
thread.start();
```

> FA2: Der Component Assembler muss die Laufzeitumgebung stoppen können.

**Siehe Client:**

```
ComponentAssembler runnable = new ComponentAssembler();
Thread thread = new Thread(runnable);
thread.start();
try {
    thread.join();
} catch (InterruptedException e) {
    throw new RuntimeException(e);
}
runnable.close();
thread.interrupt();
```

> FA3: Der Component Assembler muss neue Komponenten aus einem lokalen Verzeichnis des Rechners in die Laufzeitumgebung
> einsetzen (deployen) können.


**Beispiel:**

```
Please select:
[0] show status
[1] load component
[2] unload component
[3] start component
[4] stop component
[5] exit
1
Please select a component:
[0] Hotel.jar
[1] Tools.jar
[2] back
1
Please select a class:
[0] PrimeTester
[1] Calculator
[2] back
0
Please select a method:
[0] main
[1] isPrime
[2] back
0
----------------------------------------
A new thread has been created.
id: 139013098
component: Tools.jar
class: PrimeTester
method: main
created: 28.04.2022 21:14:31:994 +0200
active: false
Start thread?
[0] yes
[1] no
0
Please enter number: 45
45 is not prime.
```

> FA4: Der Component Assembler muss eingesetzte Komponenten in der Laufzeitumgebung ausführen können. Eine Start-Methode
> sollte dabei mit Hilfe einer Annotation im Rahmen eines Komponentenmodells definiert werden.

> FA5: Der Component Assembler soll mehrere unterschiedliche Komponenten gleichzeitig (parallel) ausführen können.

**Beispiel:**

```
Please select:
[0] show status
[1] load component
[2] unload component
[3] start component
[4] stop component
[5] exit
0
----------------------------------------
Status
Running threads: 3
----------------------------------------
Thread #0
id: 50764288
component: Tools.jar
class: Calculator
method: main
created: 28.04.2022 21:23:20:859 +0200
active: false
----------------------------------------
Thread #1
id: 198807852
component: Tools.jar
class: PrimeTester
method: main
created: 28.04.2022 21:22:56:099 +0200
active: false
----------------------------------------
Thread #2
id: 81685045
component: Tools.jar
class: PrimeTester
method: main
created: 28.04.2022 21:23:05:831 +0200
active: false
```

> FA6: Der Component Assembler soll mehrere Instanzen von einer einzelnen Komponente gleichzeitig (parallel) ausführen
> können.

Siehe FA5.

> FA7: Der Component Assembler muss in der Lage sein, die Status der aktuell eingesetzten Komponenten über die
> Laufzeitumgebung abzurufen. Ein Status sollte pro Komponente folgendes (mindestens) beinhalten: Laufende
> Identifikationsnummer, Name, Zustand.

Siehe FA5.

> FA8: Der Component Assembler muss Komponenten in der Laufzeitumgebung stoppen können. Auch hierzu sollte das
> Komponentenmodell eine „entsprechende“ Stop-Methode bereitstellen.

**Beispiel:**

```
Please select:
[0] show status
[1] load component
[2] unload component
[3] start component
[4] stop component
[5] exit
0
----------------------------------------
Status
Running threads: 2
----------------------------------------
Thread #0
id: 96384995
component: Tools.jar
class: PrimeTester
method: main
created: 28.04.2022 21:26:52:593 +0200
active: true
duration: 00:00:19
----------------------------------------
Thread #1
id: 79680337
component: Tools.jar
class: PrimeTester
method: main
created: 28.04.2022 21:27:22:429 +0200
active: true
duration: 00:00:01
----------------------------------------
Please select:
[0] show status
[1] load component
[2] unload component
[3] start component
[4] stop component
[5] exit
4
Select to stop:
[0] stop 96384995#Tools.jar#true
[1] stop 79680337#Tools.jar#true
[2] back
1
----------------------------------------
Please select:
[0] show status
[1] load component
[2] unload component
[3] start component
[4] stop component
[5] exit
0
----------------------------------------
Status
Running threads: 2
----------------------------------------
Thread #0
id: 96384995
component: Tools.jar
class: PrimeTester
method: main
created: 28.04.2022 21:26:52:593 +0200
active: true
duration: 00:00:29
----------------------------------------
Thread #1
id: 79680337
component: Tools.jar
class: PrimeTester
method: main
created: 28.04.2022 21:27:22:429 +0200
active: false
```

> FA9: Der Component Assembler muss Komponenten aus der Laufzeitumgebung löschen können.

**Beispiel:**

```
Please select:
[0] show status
[1] load component
[2] unload component
[3] start component
[4] stop component
[5] exit
0
----------------------------------------
Status
Running threads: 2
----------------------------------------
Thread #0
id: 96384995
component: Tools.jar
class: PrimeTester
method: main
created: 28.04.2022 21:26:52:593 +0200
active: true
duration: 00:02:42
----------------------------------------
Thread #1
id: 79680337
component: Tools.jar
class: PrimeTester
method: main
created: 28.04.2022 21:27:22:429 +0200
active: false
----------------------------------------
Please select:
[0] show status
[1] load component
[2] unload component
[3] start component
[4] stop component
[5] exit
2
Select to unload:
[0] unload 96384995#Tools.jar#true
[1] unload 79680337#Tools.jar#false
[2] back
0
----------------------------------------
Please select:
[0] show status
[1] load component
[2] unload component
[3] start component
[4] stop component
[5] exit
0
----------------------------------------
Status
Running threads: 1
----------------------------------------
Thread #0
id: 79680337
component: Tools.jar
class: PrimeTester
method: main
created: 28.04.2022 21:27:22:429 +0200
active: false
```