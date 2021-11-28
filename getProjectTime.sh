#!/usr/bin/env bash

# NOTE: This script is used to process tests and put the corresponding STARTS initial SHA at the last column

if [[ $1 == "" ]]; then
    echo "arg1 - full path to the test file (eg. tmp.csv)"
    exit
fi

currentDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

echo -n "" > $currentDir/projectInfoTime.csv

input=$1
inputProj=$currentDir"/projects"

echo ${inputProj}

while IFS= read -r line
do
  if [[ ${line} =~ ^\# ]]; then
    echo ${line} >> $currentDir/projectInfoTime.csv
    continue
  fi

  slug=$(echo $line | cut -d',' -f1)
  initial_sha=$(echo $line | cut -d',' -f2)

  if [[ ! -d ${inputProj}/${slug} ]]; then
    git clone "https://github.com/$slug" $inputProj/$slug
  fi

  (
    cd $inputProj/$slug
    git stash
    # initial_sha=`git rev-parse HEAD`
    git checkout ${initial_sha}
    echo -n "${slug}" >> $currentDir/projectInfoTime.csv
    echo -n ",${initial_sha}" >> $currentDir/projectInfoTime.csv
    MVNOPTIONS="-Ddependency-check.skip=true -Denforcer.skip=true -Drat.skip=true -Dmdep.analyze.skip=true -Dmaven.javadoc.skip=true -Dgpg.skip -Dlicense.skip=true"
    start=$(date +%s.%N)
    mvn test ${MVNOPTIONS} -fn # >> output.msg 2>> output.err
    end=$(date +%s.%N)
    take=$(echo "scale=2; ${end} - ${start}" | bc)
    # if [[ $? -eq 0 ]]; then
    #   echo ",1" >> $currentDir/projectInfo.csv
    # else
    #   echo ",0" >> $currentDir/projectInfo.csv
    # fi
    echo ",${take}" >> $currentDir/projectInfoTime.csv
  )
  rm -rf $inputProj/$slug
  # echo -n "" > error.txt

done < "$input"
