#!/bin/sh

cd bin
g++ -o calculator calcTst.cpp calc.cpp list.cpp stackl.cpp
echo "Calculator build success"