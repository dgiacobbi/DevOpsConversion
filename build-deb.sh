#!/bin/sh

TEMP_DIR=temp

echo "Starting deb package build"

echo "Making temporary directory tree"
mkdir -p $TEMP_DIR
mkdir -p $TEMP_DIR/DEBIAN
mkdir -p $TEMP_DIR/tmp/
mkdir -p $TEMP_DIR/usr/local/lib
mkdir -p $TEMP_DIR/usr/local/include

echo "Copy control file for DEBIAN/"
cp bin/DEBIAN/control $TEMP_DIR/DEBIAN/

echo "Copy install and remove scripts for DEBIAN"
cp bin/DEBIAN/postinst $TEMP_DIR/DEBIAN/

echo "conffiles setup for DEBIAN"
cp bin/DEBIAN/conffiles $TEMP_DIR/DEBIAN/

echo "Copy example test file into place"
cp bin/Example.cpp $TEMP_DIR/tmp/

echo "Copy library files into place"
cp bin/libMyString.so $TEMP_DIR/usr/local/lib
cp bin/MyString.h $TEMP_DIR/usr/local/include

echo "Building deb file"
dpkg-deb --root-owner-group --build $TEMP_DIR
mv $TEMP_DIR.deb myStrLib-v1.0.0.deb

echo "Complete"