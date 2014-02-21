*ecco* webui
====

#### a Web front end for the *ecco* diff ####

This is a simple JSP-based frontend for the *ecco* diff tool; it accepts ontology input either from the local file system (via the *Browse* buttons) or from remote URIs. An instance of this app is deployed [here](http://owl.cs.manchester.ac.uk/diff) (alternatively [here](http://rpc440.cs.man.ac.uk:8080/diff)).

For more information about the tool check the [*ecco* repository](https://github.com/rsgoncalves/ecco).


deployment
--------------------
Note that the application has only been tested with **Apache Tomcat v7**, using **Java 1.6 (or above)**. In order to execute properly, *ecco-webui* needs the appropriate native library of FaCT++ for the operating system the Web server is running on. This library, denoted *FaCT++ version 1.6.2; precompiled [OS] binaries* should be obtained from [here](https://code.google.com/p/factplusplus/downloads/list). Afterwards, the *single* appropriate file (in Windows a **.dll**, in Linux a **.so**, or in Mac OS X a **.jnlib** file) should be moved into the **_WebContent/WEB-INF/lib_** folder.
