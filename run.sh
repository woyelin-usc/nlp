#!/bin/bash

javac -cp ../"*" SnookerSearch.java

### first argument "../result" is the extract result from our first test file;  "../res" is the extraction result from whatever test files the user choose
java -cp ../"*":./:../tmp  SnookerSearch ../result ../tmp/roth_relation_model_pipeline.ser ../buf ../res

rm -rf *.class *~
