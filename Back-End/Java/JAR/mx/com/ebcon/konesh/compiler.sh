#!/bin/bash
export CLASSPATH=/opt/portalcfdi/
export JAVA_HOME=/usr/local/java/jdk-17.0.5

$JAVA_HOME/bin/javac -cp ".:$CLASSPATH" *.java
