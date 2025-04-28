#!/bin/sh

cd bin

echo "Compiling unit tests"
g++ -o test -DUNIT_TEST MyStringTest.cpp -lgtest -lgtest_main

echo "Executing unit tests"
./test

echo "Cleaning up testing files"
rm -f test

echo "Testing session complete"