#!/bin/sh

# Complier the SendMsg.java

javac -Djava.ext.dirs=./lib ./src/SendMsg.java -cp ./bin -d ./bin
echo compiler finished
