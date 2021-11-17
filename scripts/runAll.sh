#!/bin/bash

currentDir0="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
echo $currentDir0
project_path=$1
echo $project_path
echo $currentDir0"/../${project_path}"
cd $currentDir0"/../agent"
mvn install -DskipTests

#cd $currentDir0"/agent-pom-modify"
#bash modify-project.sh $currentDir0"/../${project_path}"


cd $currentDir0"/../${project_path}"
mvn test
