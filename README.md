configure the board by using
config.json

"jsonWithReleaseVersionInfo" and "webServiceAdress" need to point to mashine that is to be your server

to start the spring-boot application use:
mvn spring-boot:run

to pack it to jar use: 
mvn package

this will generate a jar in target folder.
after executing the jar service and testing-board will be up and running

testing-board will be available under ${ipOfMashineWhereJarHasBeenStarted}:8282/test-results.html


