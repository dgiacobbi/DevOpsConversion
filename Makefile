# Name: David Giacobbi
# Class: CPSC-334
# Assignment: Final Project
# Description: Make file to handle packaging and DevOps for MyString library for CPSC122.

build:
	./build.sh

test:
	./test.sh

build-deb:
	./build-deb.sh	

lint-deb:
	-lintian mystrlib-v1.0.0.deb

clean:
	rm -rf temp
	echo "Binaries, executables, and packages cleaned in repo"