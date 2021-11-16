# Byte-Code-Instrumentation
The simplest way to use our agent tool.

```shell
mvn install

java -javaagent:agent/target/agent-0.1-SNAPSHOT.jar -jar other/target/other-0.1-SNAPSHOT.jar
```

## Quickstart
There is a ready project named other in our respository. By using the following shell script, you can quickly get the results of other project.

```shell
bash scripts/runAll.sh dataset/spring-boot-demo
```

### Automatically setting up the pom.xml

Run the following command to automatically setup the pom.xml for our agent part.

```shell
bash scripts/agent-pom-modify/modify-project.sh path_to_maven_project
```

For example, 
```shell
bash scripts/agent-pom-modify/modify-project.sh dataset/spring-boot-demo/
```
