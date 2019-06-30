# filesystemapioracle
File System Spring Boot API - Oracle DDBB

# compile
mvn clean install
mvn clean integration-test

#run
run Oracle DDBB
mvn clean spring-boot:run

# swagger
http://localhost:8090/swagger-ui.html

# oracle
http://192.168.56.3:8080/apex/

##### GET printPathToFile
http://localhost:8090/printPathToFile/tmp/Users%2Fmarco27%2Ftemp%2Ftmp.txt
http://localhost:8090/printPathToFile/Users%2Fmarco27%2Fopt/Users%2Fmarco27%2Ftemp%2Fopt.txt
http://localhost:8090/printPathToFile/Volumes%2FMAC200%2Fmac200/Users%2Fmarco27%2Ftemp%2Fmac200.txt
http://localhost:8090/printPathToFile/Users%2Fmarcoguastalli%2Fopt%2Fdocker/Users%2Fmarcoguastalli%2Ftemp%2Fdocker.txt
{"pathToPrint":"/Users/marcoguastalli/opt/docker","fileToPrint":"/Users/marcoguastalli/temp/docker.txt"}

##### GET findFileStructureOracleById
http://localhost:8090/findFileStructureOracleById/Users%2Fmarco27%2Ftemp
http://localhost:8090/findFileStructureOracleById/Users%2Fmarcoguastalli%2Ftemp

##### GET findFileStructureOracleByPath
http://localhost:8090/findFileStructureOracleByPath/Users%2Fmarco27%2Ftemp
http://localhost:8090/findFileStructureOracleByPath/Users%2Fmarcoguastalli%2Ftemp

##### GET(!) saveFileStructureOracle
http://localhost:8090/saveFileStructureOracle/Users%2Fmarco27%2Ftemp
http://localhost:8090/saveFileStructureOracle/Users%2Fmarcoguastalli%2Ftemp

##### DELETE deletePathStructureOracle
http://localhost:8090/deletePathStructureOracle/Users%2Fmarco27%2Ftemp
http://localhost:8090/deletePathStructureOracle/Users%2Fmarcoguastalli%2Ftemp
curl -X DELETE --header "Accept: */*" "http://localhost:8090/deletePathStructureOracle/Users%2Fmarcoguastalli%2Ftemp"
curl -X DELETE --header "Accept: */*" "http://localhost:8090/deletePathStructureOracle/Users%2Fmarco27%2Ftemp"

##### GET loadFileStructure
http://localhost:8090/loadFileStructure/Users%2Fmarco27%2Ftemp
http://localhost:8090/loadFileStructure/Users%2Fmarcoguastalli%2Ftemp