#/bin/bash

rm -Rf .build
mkdir .build/

NC='\033[0m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'

echo -e "${GREEN}Building library.${NC}"
mvn -q -f ../pom.xml clean install -DskipTests

echo -e "${GREEN}Building sample:${NC}"
PROJS=(parent-proj my-app-1  my-app-2  my-app-3 main-proj)
for i in ${PROJS[@]}; do
  echo -e "  * ${BLUE}${i}${NC}"
  cp ./projects/${i}.tar.gz .build/ 
  tar xzf .build/${i}.tar.gz -C .build/
  mvn -q -f .build/${i}/pom.xml clean install -DskipTests
done
echo -e "${GREEN}Running sample:${NC}"

mvn -q dependency:copy -Dartifact=org.slf4j:slf4j-simple:1.7.12 -Dtransitive=false -DoutputDirectory=./.build
mvn -q dependency:copy -Dartifact=com.mycompany.app:deployable-app:1.0-SNAPSHOT -Dtransitive=false -DoutputDirectory=./.build
java -cp ./.build/slf4j-simple-1.7.12.jar:./.build/deployable-app-1.0-SNAPSHOT.jar com.mycompany.app.Main 

echo -e "${GREEN}-FIN-${NC}\n"
