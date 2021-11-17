#!/bin/bash

if [[ $1 == "" ]]; then
    echo "arg1 - the slug of the project"
    exit
fi

input1=$1

currentDir0="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

echo $currentDir0

cd $currentDir0"/../agent"
mvn install -DskipTests

cd $currentDir0"/../$input1"
echo pwd
mvn install  -DskipTests -Ddependency-check.skip=true -Denforcer.skip=true -Drat.skip=true -Dmdep.analyze.skip=true -Dmaven.javadoc.skip=true -Dgpg.skip -Dlicense.skip=true -Dskip=true

mvn test  -Ddependency-check.skip=true -Denforcer.skip=true -Drat.skip=true -Dmdep.analyze.skip=true -Dmaven.javadoc.skip=true -Dgpg.skip -Dlicense.skip=true -Dskip=true
