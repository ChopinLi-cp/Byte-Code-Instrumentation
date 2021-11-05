#!/bin/bash

currentDir0="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

cd $currentDir0"/../agent"
mvn install -DskipTests

cd $currentDir0"/agent-pom-modify"
bash modify-project.sh $currentDir0"/../other"

cd $currentDir0"/../other"
mvn test