linklander-server
=================

Server Implementation of Link Lander

Tomcat 8
Java 8
Jersey(JAXRS)
Persistence (NoSQL: Neo4j, ...?)

Implementiert API

Gui
--------
- Vaadin
- Installs
-- ...

Backend
--------
- Neo4j (cheatsheet: http://neo4j.com/docs/2.1/cypher-refcard/)
- Install
--- Download: http://neo4j.com/download-thanks
--- Run locally with ./bin/neo4j console
- Squirrel Support
-- Download jdbc driver: http://dist.neo4j.org/neo4j-jdbc/neo4j-jdbc-2.0.1-SNAPSHOT-jar-with-dependencies.jar
-- Use Tutorial: http://tiqview.tumblr.com/post/25427068878/querying-neo4j-graph-data-with-cypher-in-squirrel-via
-- Run Query: MATCH (nineties:Movie) WHERE nineties.released > 1990 AND nineties.released < 2000 RETURN nineties.title, nineties.released;
- Maybe test for visualizing the Graph-DB: http://www.dbvis.com/


Installation
--------------

- Eclipse EE im System entpacken, starten, workspace anlegen
- Tomee Plus 1.7.1 irgendwo im System entpacken
- Tomee als Server integrieren
- Git Code auschecken in neuen Ordner



