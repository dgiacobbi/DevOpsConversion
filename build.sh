#!/bin/sh

cd bin
g++ -fPIC -shared -o libMyString.so MyString.cpp
echo "myString build success"