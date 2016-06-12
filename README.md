# Čo je gradle?
Gradle je buildovací systém pre jazyk java, ktorý je alternatívou k systému Maven od apache. Obsahuje dependency manažment, čím je jednoduché pridávať nové knižnice potrebné pre kompiláciu alebo beh programu. Podporuje viac projektový build a skripty sú napísané v jazyku Groovy.
Dá sa použiť pomocou príkazového riadku alebo ako plugin do vývojového prostredia. V tomto návode bude popísané obidva spôsoby. Ako vývojové prostredie bude použité InteliJ Idea.
# Inštalácia [[1]](https://docs.gradle.org/current/userguide/installation.html)
Potrebné súbory pre fungovanie gradle sú dostupné v troch verziach na [oficialnej stranke](http://gradle.org/gradle-download/):
- kompletná distribúcia so zdrojovým kódom a offline dokumentáciou
- distribúcia bez kódu a dokumentácie
- iba zdrojové kódy
Podmienkou pre používanie nástroja gradle je **java vo verzií 6 a vyššie**.

## Postup
1. Stiahnutý zip súbor stačí rozbaliť do priečinka podľa vlastného uváženia
2. Nastavenie premenných prostredia
	* premenná **GRADLE_HOME** musí byť nastavená na priečinok s rozbalenými súbormi gradle.
	* do premennej **PATH** je potrebné pridať cestu k priečinku bin v gradle (GRADLE_HOME/bin).
3. Úspešnost inštalácie overíme prikázom ` gradle -v ` v príkazovom riadku. Ak je všetko v poriadku zobrazí sa výpis podobný tomuto:
![](https://drscdn.500px.org/photo/157424851/m%3D900/dc6184372e224ae3b60aeecd18a92d86)

## Gradle wrapper [[2]](https://docs.gradle.org/current/userguide/gradle_wrapper.html)
Môže nastať prípad, že viaceré stroje viacerých používateľov môžu mať nainštalovanú inú verziu gradle alebo nemusia mať gradle vôbec. Preto bol vytvorený gradle wrapper, ktorý "zaobalí" gradle. Znamená to, že projekt obsahuje svoj wrapper, ktorý sa stará o verziu gradle na použivateľovom stroji, podľa toho ako je tento wraper nastavený na stroji kde bol projekt vytvorený. Ak programátor ma na svojom stroji verziu gradle 2.13 tak bude vytvorený wraper, ktorý bude využívať túto verziu. Ostatní používatelia, ktorý si stiahnu projekt a spustia build pomocou wrapera a ich verzia sa nebude zhodovať s verziou v projekte alebo gradle vôbec nebude nainštalované tak sa automaticky stiahne a nainštaluje.  

Nainštaluje sa do domovského priečinka používateľa HOME/.gradle/wrapper/dists. 

Príkazom ` gradle init ` sa vytvorí kostra gradle projektu, wrapper je automaticky zahrnutý, pretože to je odporúčane použitie gradle. Manuálne pridanie je možné príkzom ` gradle wrapper `, s nepovinnými parametrami:
* `--gradle-version 2.13` - určuje aká verzia gradle bude použitá, v tomto prípade sa použije 2.13
* `--gradle-distribution-url http://web.com` - určuje repozitár, z ktorého sa stiahne gradle
Ak tieto parametre nie sú zadané použije sa štandardný repozitár gradle a verzia nainštalovaná na počítači kde bol príkaz spustený.
### Zmena verzie gradle v už existujúcom gradle wrapperi 
Aktuálna verzia gradle wrappera je uložená v súbore `gradle/wrapper/gradle-wrapper.properties ` v projekte. Riadok s hodnotou `distributionUrl=https\://services.gradle.org/distributions/gradle-2.9-all.zip` hovorí, že použitá verzia gradle je *2.9*. Prepísaním tohto čísla na vyššie (dostupné) a spustením ľubovolného gradle príkazu pomocou wrappera bude stiahnutá a nainštalovaná nová verzia. Ak prepíšeme verziu z *2.9* na *2.13*, čo je v čase písania článku najnovšia dostupná verzia, wrapper automaticky stiahne verziu *2.13*.

Sofistikovanejšia metóda ako zmeniť verziu gradle vo wrapperi je použiť príkaz ` gradle wrapper --gradle-version 2.13`, ktorý zmeni verziu na *2.13* a pregeneruje súbory wrappera. Tento príkaz spôsobí to isté čo manualne prepísanie `distributionUrl`, ale naviac prepíše aj `gradle-wrapper.jar` na novšiu verziu.

## Gradle daemon [[3]](https://docs.gradle.org/current/userguide/gradle_daemon.html)
Spustenie gradle buildu niekedy chvíľku trvá, hlavne toho prvého. Preto bol vytvorený gradle daemon (proces bežiaci na pozadí), ktorý zrýchľuje build tým, že potrebné knižnice, ktoré gradle využíva sú načítané iba raz a každý další build ich už len využíva.
Daemon je defaultne vypnutý a neodporúča sa používa v produkčnom prostredí, kde by mohol spotrebovavať prostriedky. Naopak je odporúčaný v developerských prostrediach, kde znižuje čas potrebný pre uskotočnenie buildu. Zapnúť démona môžeme viacerými spôsobmi:
1. v konfiguračnom súbore uloženom v domovskom priečinku použivateľa, pre windows to je `C : \ Users \ username\ .gradle \ gradle.properties` (ak neexistuje tak je potrebné vytvoriť ho), riadok 
 `org.gradle.daemon=true`
2. premenná JVM `-Dorg.gradle.daemon=true`
Gradle daemon sa zastaví po troch hodinách nečinnosti, manuálne sa da vypnúť v task managerovi stopnutím procesu daemona.
# Gradle tasks
Task je označená funkcionalita ktorá niečo vykoná, napríklad task `clean` vymaže všetky pozostatky po predchadzajúcich buildoch. Gradle obsahuje viacero taskov a ich počet sa môže zvýšiť použitím pluginov alebo vytvorením vlastného tasku. 
## Build
* assemble - vytvorí všetky súbory, ktoré tvoria výstup projektu (bajtkód tried, jar súbory pre distribúciu...)
* classes - preloží triedy do bajtkódu
* testClasses - preloží triedy s testami, ktoré sú uložené v priečinku `src/test/java`
* build - preloží triedy projektu a spustí testy ak nejaké existujú
* buildNeeded - preloží a otestuje projekt aj všetky projekty, na ktorých závisi (využíva ich vo svojom kóde)
* buildDependents -  preloží a otestuje projekt aj všetky projekty, ktoré na ňom závisia (využívajú ho vo svojom kóde)
* clean - vymaže všetky pozostatky po predchadzajúcich buildoch
* jar - vytvorí spustitelný jar súbor z projektu
## Distribution
* assembleDist - preloží súbory potrebné pre distribúciu vo formátoch .tar a .zip
* distTar - vytvorí iba .tar súbor
* distZip - vytvorí iba .zip súbor
* installDist - vztvorí distribučné súbory a nainštaluje projekt na aktuálnom stroji
## Documentation
* javadoc - vygeneruje dokumentáciu pre triedy v priečinku `src/main/java`
# Pluginy
Pluginy slúžia na rozšírenie funkcionality gradle. Existuje viacero pluginov a používateľ si môže vytvárať vlastné podľa toho aký projekt potrebuje buildovať. Spomenieme si len niekoľko pluginov, ktoré využijeme neskôr v príkladoch. Pluginy sa aktivujú pridaním riadku v súbore `gradle.build`, čím získame ďalšie tasky rozširujúce funkcionalitu:
```groovy
apply plugin: 'meno pluginu'
```
## Java plugin [[4]](https://docs.gradle.org/current/userguide/java_plugin.html)
Slúži na kompiláciu, distribúciu, testovanie a generovanie dokumentácie v java projekte. 
* compileJava - skompiluje java kód z priečinku `src/main/java` prekladačom *javac*
* processResources - skopíruje súbory z `src/main/resources` k preloženým triedam
* classes - spustí obidve predchadzajúce úlohy čím dostaneme preloženy kód aj so zdrojmi
* compileTestJava	- skompiluje java kód z priečinku `src/test/java` prekladačom *javac*
* processTestResources 	- 	skopíruje súbory z `src/test/resources` k preloženým triedam
* testClasses -  spustí obidve predchadzajúce úlohy čím dostaneme preloženy kód testov aj so zdrojmi
* test - skompiluje a spustí testy v projekte
## War plugin [[5]](https://docs.gradle.org/current/userguide/war_plugin.html)
Je rozšírenie java pluginu pre vytváranie *war* archívov, ktoré sa dajú deploynúť do aplikačného servera. Tento plugin skopíruje všetky súbory z `src/main/webapp` do koreňového priečinku archívu a ostatné preložené triedy do priečinka `WEB-INF/classes`. Knižnice potrebné pre správne fungovanie projektu sú v priečinku `WEB-INF/lib`.
* war - úloha, ktorá vytvorí *war* archív z projektu, táto úloha je spustená ak sa zavolá úloha *assemble*, war po svojom spustení zavola úlohu *classes*
## Application plugin [[6]](https://docs.gradle.org/current/userguide/application_plugin.html)
Plugin, ktorý slúži na kompiláciu distribúciu a spustenie java projektu. Plugin v sebe zahrňa pluginy java a distribution, ktoré používateľ nemusi importovať (apply). Má jednu povinnú premennú, ktorú musí používateľ nastaviť pred spustením aplikácie a to triedu, v ktorej sa nachádza metóda main, ktorú chceme spustiť:
```groovy
mainClassName = "hello.HelloWorld"
```
* run - preloží všetky triedy a spustí aplikáciu
* startScripts - vytvorí spúštacie skripty špecifické pre daný OS
* distZip, distTar - vytvorí archív pre distribúciu aplikácie (zip alebo tar), ktorý zahrňa knižnice potrebné pre beh aplikácie a spúštacie skripty
## Spring boot plugin  [[7]](http://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-gradle-plugin.html)

Plugin pre preklad a spustenie Spring boot projektu. Slúži na vytváranie archvívov na deploy alebo spustenie aplikácie. Pri vytváraní spustiteľného archívu je potrebné pridať do `build.gradle`:
```groovy
configurations {
    providedRuntime
}
```
kde provided runtime je knižnica kontajnera, v ktorom má byť aplikácia spustená.
* bootRepackage - úloha, ktorá spustí projekt bez vytvarania jar, zdroje sa čítajú z pôvodného umiestnenia nikde sa nekopírujú (nepoužíva sa processResources), úloha je volaná po zavolaní assemble
# Štruktúra projektu
```
- projekt
-- sub projekt 1
-- src
--- main
---- java
---- resources
--- test
---- java
---- resources
-- build.gradle
-- sub projekt 2
--- build.gradle
- build.gradle
- settings.gradle
```
Hlavným konfiguračným súborom gradle projektu je `gradle.build`. Priečinok src obsahuje 2 podpriečinky test obsahujúci zdrojové kódy testov a main obsahujúci kódy aplikácie. Súbor settings gradle obsahuje meno koreňového projektu a v prípade multi-projektového buildu, projekty musia byt zahrnuté v tomto súbore (`include 'sub projekt 1'`).
# Príklady
## Hello world
### Vytvorenie projektu pomocou príkazového riadka

```cmd
> gradle init
> mkdir src
> cd src
> mkdir main
> mkdir test
> cd main 
> mkdir java
```
### Vytvorenie projektu pomocou InteliJ Idea IDE
1. File -&gt; New -&gt; Project
2. Vľavo vyberieme možnosť Gradle a v strednom paneli zaškrtneme možnosť Java
![](https://afnsza.dm2304.livefilestore.com/y3mj1BAuW-HstAmWK_ILhRMsqx6sC4CCpHAXjvZRfBPFmvKqxJ8FAfiEkLBgz7DsEaczA1zhD_aW22OwB3F9eR3Koyh4UmC2lHHvAd8RePwBWxUF-fiVK26LpWOUbWcEjSMpteo6bSKIW_cDSt8Ap_k7Q?width=743&amp;height=746&amp;cropmode=none)
3. Vyplníne groupid (meno skupiny projektov), artefactid (meno projektu) a verziu
4. Necháme defaultnu možnosť s gradle wrapperom a separate modul per source set
![](https://afobbg.dm2304.livefilestore.com/y3mGyZlSPvm-yfChCp-nW0qln-7F0JI4Zbk_3yvjs1P81O0luJBIdNScEBqbBffMSrV83YB3r1R2-9AHgOqYCa7aLFLM6ntLGQX_fIWG6dO9gdVEAVdGNlq7dw2E1yslj5E_lzPQFgyC_O1DnRrE-yu9w?width=747&amp;height=752&amp;cropmode=none)
5. Vyberieme umiestnenie a meno projektu a dokončíme vytváranie
![](https://afmcew.dm2304.livefilestore.com/y3mgcLtw8y2pdWfyvLDgnBZPP1ITM5EjTBY1TlsPMk6H1DNofsAPKfrk4g-6JEDgZxZA1LHYAmEzfAnGuMhepKYACYDJc_TCiCFsJGBrRQ0J2xTALswVyj0KSHJ_SEq5HzBlfpU2wzgIzudVWFM4MgYWw?width=752&amp;height=760&amp;cropmode=none)

* Vytvoríme si štruktúru projektu podľa kapitoly *štruktúra projektu*, najprv vytvoríme modul "sub projekt 1", v ktorom vytvoríme priečinok `src` s podpriečinkami `main` a `test`, v obidvoch vytvoríme podpriečinok `java`. Rovnako vytvoríme modul "sub projekt 2".
* Vytvoríme package `hello` v `src/main/java` a v ňom triedu HelloWorld:
```java
package hello;

public class HelloWorld {

    public static void main(String[] args){
        System.out.println(hello("Gradle"));
    }

    public static String hello(String name){
        return "Hello "+name+"!";
    }
}
```
* Vytvoríme package `hello` v `src/test/java` a v ňom triedu HelloWorldTest:
```java
package hello;

import static  org.junit.Assert.assertTrue;
import org.junit.Test;

public class HelloWorldTest {
    @Test
    public void helloTest(){
        String hello = HelloWorld.hello("Gradle");
        assertTrue("Hello gradle! was expected but returned "+hello, hello.equals("Hello Gradle!"));
    }
}
```
* Upravíme `build.gradle` umiestnené v sub projekte 1, pridáme nasledujúce riadky aby náš kód bol spustiteľný:
```groovy
apply plugin: 'application'
mainClassName = 'hello.HelloWorld'
```
* Spustíme to pomocu gradle okna, ktoré je možné otvoriť kliknutím na ikonu na gradle v pravej vertikálnej lište alebo kliknutím na View -&gt; Tool Windows -&gt; Gradle. V tomto okne si rozklineme sub projekt 1 -&gt; Tasks -&gt; application a spustíme task s menom `run`. Ak tasky application nie sú viditeľné je potrebné spraviť refresh projektu tlačidlom v ľavom hornom rohu gradle okna.
![](https://afnlhq.dm2304.livefilestore.com/y3mXbSCR3sxRMdM_VXH7Nc4zrCmdJY04_JlUMsGDl21XmbDe7M3Zpc5DV7oh9bGLofOiJk6JQtmGjle1rwAWHs8ADPjCBjzD0OKA9ZpH4xR7rcY1dMxp2z8GkHbujrgVjlyqife8i-5Bci2-gOc1cL2zg?width=1916&amp;height=438&amp;cropmode=none)
Príkazovým riadkom:
```cmd
gradlew ":sub projekt 1:run"
```
príkaz je v uvodzovkách len preto, lebo máme vytvorený projekt s medzerami v názve, príkaz ma formát:  **gradlew:**_modul_**:**_task_.  Ak projekt neobsahuje žiadne moduly stačí napísať: **gradlew** _task_, prípadne tento zápis spôsobi spustenie tasku vo všetkých moduloch.
* Task run sposobi spustenie prekladu a metódy main v triede `HelloWorld`:
```
Executing external task 'run'...
:sub projekt 1:compileJava
:sub projekt 1:processResources UP-TO-DATE
:sub projekt 1:classes
:sub projekt 1:run
Hello Gradle!

BUILD SUCCESSFUL
```
* Taskom `build` vykonáme len preklad a spustenie testov. Ak nejaký z testov zlyhá, tak zlyhá build. Task `run` testy *nevykonáva*.
## Dependecy management
* Vytvoríma package `library` v module "sub projekt 2" a triedu `RandomHello`
* Do `build.gradle` v tomto projekte pridáme knižnicu *apache commons lang* do sekcie `dependencies` riadkom, ktorý skopírujeme z maven repozitára:
```groovy
compile group: 'org.apache.commons', name: 'commons-lang3', version: '3.4'
```
a spravíme refresh projektu, čím sa automaticky stiahne a nalinkuje knižnica
* Do triedy `RandomHello` vložíme kód:
```java
package library;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

public class RandomHello {
    public String randomHello(int length){
        return "Hello "+ RandomStringUtils.random(length, true, true)+"!";
    }
}
```
čo je upravená verzia hello world s náhodným stringom ľubovolnej dlžky
* Vrátime sa do modulu "sub projekt 1" a súboru `build.gradle`, kde pridáme dependency na projekte 2:
```groovy
compile project(':sub projekt 2')
```
* Do metódy main v triede `HelloWorld` pridáme riadok:
```java
System.out.println(new RandomHello().randomHello(5));
```
čím využívame projekt 2 v projekte 1 a program nam vypíše Hello s náhodným reťazcom dlžky 5. Program spustíme taskom `run`cez IDE alebo príkazovým riadkom rovnako ako v predchadzajúcom prípade.

## Spring web app
* Vytvoríme si nový modul v projekte, ktorý bude mať štandardnú štruktúru avšak bude naviac obsahovať priečinok `src/main/webapp`, v ktorom sa budu nachádzať súbory súvisiace s webom
* Keďže ide o webovú aplikáciu budeme potrebovať pluginy *war, java, spring-boot* a knižnice `spring-boot-starter-web` pre kompiláciu, `spring-boot-starter-tomcat` pre spustenie aplikácie. Spring boot plugin je externý plugin, ktorý musíme pridať do classpath-u `classpath("org.springframework.boot:spring-boot-gradle-plugin:1.3.5.RELEASE")` v sekcií buildscript. Cely súbor build.gradle bude vyzerať takto:
```groovy
buildscript {
	repositories {
		mavenCentral()
	}
	dependencies {
		classpath("org.springframework.boot:spring-boot-gradle-plugin:1.3.5.RELEASE")
	}
}
apply plugin: 'java'
apply plugin: 'spring-boot'
apply plugin: 'war'
war {
	baseName = 'web'
	version = '0.0.1-SNAPSHOT'
}
sourceCompatibility = 1.8
targetCompatibility = 1.8
repositories {
	mavenCentral()
}
configurations {
	providedRuntime
}
dependencies {
	compile('org.springframework.boot:spring-boot-starter-web')
	providedRuntime('org.springframework.boot:spring-boot-starter-tomcat')
}
```
* Vytvoríme si package hello a v ňom triedu `HelloController`, ktorá presmeruje používateľa na `index.html`:
```java
package hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    @RequestMapping("/")
    public String index(){
        return "index.html";
    }
}
```
* Vytvoríme triedu `App` v tom istom balíku, ktorá bude fungovať ako konfiguračná a hlavná trieda pre spustenie aplikácie:
```java
package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(App.class, args);
    }
}
```
* V priečinku `src/main/webapp` vytvoríme jednoduchý `index.html`:
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Gradle</title>
</head>
<body>
<h1>Gradle Spring boot</h1>
<p>Hello world!</p>
</body>
</html>
```
* Aplikáciu spustíme úlohou *bootRun* z IDE alebo príkazového riadka, čím sa spustí aplikácia vo vnorenom tomcate
![](https://afpmkq.dm2304.livefilestore.com/y3mPD2WZh1vgw3BLDP6df8lA-vE6kEmcZNMAvrbKU55GLS8I_ptXtYI_AMWY34Pep9jsACFueFXFVSUy6XG0p_u8BLgUb8a5mN0mFncDp9dS5Qm8pB-zI2Pp0kG6qv9Yt12fPUVvqfaTujDE5OGHOPVFQ?width=726&height=637&cropmode=none)
## AspectJ
Pomocou gradle vieme vytvárať projekty, ktoré nepoužívajú iba štandardný *javac* kompilátor, pomocou pluginov vieme využiť *ajc* kompilátor a tak pracovať s aspektami a [AspectJ](https://eclipse.org/aspectj/). 
* Vytvoríme si nový modul so štandardnou štruktúrou
* Vytvoríme si balík *hello* a triedu `HelloWorld`:
```java
package hello;

public class HelloWorld {
    public static void main(String[] args){
        hello("Gradle");
    }
    public static void hello(String name){
        System.out.println("Hello "+name);
    }
}
```
* Do `build.gradle` pridáme *aspectj* plugin a runtime potrebný pre spustenie aspectJ, premennou *aspectjVersion* musíme určiť akú verziu aspectJ chceme použiť:
```groovy
project.ext {
    aspectjVersion = '1.8.9'
}
apply plugin: 'aspectj'
apply plugin: 'application'

mainClassName = 'hello.HelloWorld'

buildscript {
    repositories {
        maven {
            url "https://maven.eveoh.nl/content/repositories/releases"
        }
    }

    dependencies {
        classpath "nl.eveoh:gradle-aspectj:1.6"
    }
}
sourceCompatibility = 1.8

repositories {
    mavenCentral()
}

dependencies {
    testCompile group: 'junit', name: 'junit', version: '4.11'
    runtime 'org.aspectj:aspectjrt:1.8.9'
}
```
* Vytvoríme si aspekt `Hello.aj`, ktorý vypíše jednoduchý text pred zavolaním metódy `HelloWorld.hello`:
```java
package hello;

public aspect Hello {
    pointcut hello(): call(public void HelloWorld.hello(*));
		
    before(): hello(){
        System.out.println("Aspectj from gradle:");
    }
}
```
* Spustením úlohy run sa vykoná preklad, pretkávania a spustenie kódu s výpisom:
```
Executing external task 'run'...
:aspectj:compileAspect
:aspectj:compileJava
:aspectj:processResources UP-TO-DATE
:aspectj:classes
:aspectj:run
Aspectj from gradle:
Hello Gradle

BUILD SUCCESSFUL
```
