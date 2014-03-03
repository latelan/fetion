#!/bin/sh

# Complier the SendMsg.java

javac ./src/Encrypt.java -d ./bin
javac ./src/Encode.java -cp ./bin -d ./bin
javac -Djava.ext.dirs=./lib ./src/SendMsg.java -cp ./bin -d ./bin
echo compiler finished
