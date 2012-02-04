trnySim
=======
Overview
--------
The purpose of this program is to study the effectiveness of different tournament 
formats through simulation. Although still in development, trnySim is able to 
analyze the fairness of the results of a tournament in terms of the position 
achieved by a team against the expected position according to a team's rating. 
It is intended that once finished, this program will allow users to analyze 
their ideas for tournament designs.

The following formats are currently implemented:

* Leagues: all against all in sequence for a predefined amount of rounds.
* Swiss style: all against all in sequence for a predefined amount of rounds 
following swiss style tournament scheduling.
* Knock-out: bracket style match-up, supports multiple games per match-up (series)
 and multiple elimination.
* Full ranking knock-out: bracket style match-ups in single elimination  where 
losing teams keep playing to define their position among other losing teams.

Future development is planned for:

* Multi-phase tournaments: i.e. a league tournament where top 4 classify to a play-off
* Parallel tournaments: i.e. 16 groups playing a league and winners classify to
 a play-off.
* Custom pairing systems for knock-out tournaments: i.e. 1-8 vs 4-6; 2-7 vs 3-5 
instead of 1-8 vs 4-5; 3-6 vs 2-7.
* UI project to create and test tournament formats without programming. 
 
Although not its main purpose, this program can be used as a library to create 
tournament schedules

Installation & Usage
--------------------
Building the source requires a working Java programming environment (JDK 5+), 
maven (http://maven.apache.org) and enough programming skills to create a simple 
program similar to what is demonstrated in the trnySim.java file.
Using maven the demo simulation can be easily run with the "mvn exec:java" command; 
in the future a simple UI will allow users to set up their tournament formats 
and simulate them, but meanwhile it is necessary to modify the TrnySim.java to 
create different scenarios.

Author & License
----------------
This program is copyright of Ignacio Cavero, a.k.a. ilcavero, you can contact me 
at <ilcavero@gmail.com>. It is licensed under GPLv3 with classpath exception, 
which means that it is open source and can be used as jar library from closed 
source programs, please refer to license.txt for the actual license. 