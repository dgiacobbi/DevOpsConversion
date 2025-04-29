#!/bin/sh

cd bin

echo "Compiling unit tests"
g++ -o teststr -DUNIT_TEST MyStringTest.cpp -lgtest -lgtest_main

echo "Executing unit tests"
./teststr

echo "Cleaning up testing files"
rm -f teststr

echo "Testing session complete"