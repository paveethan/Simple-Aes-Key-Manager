## Synopsis

This is a simple key manager that utilzies AES (128-bit) encryption, written in Java. It is a proof of concept, it was written to utilize SHA-1 and AES-128 for an assignment (MTH-816). 


## Installation

C:\Users\paveethan\eclipse\java-neon\eclipse\eclipse.exe

*Ignore if using executable version (exe/jar):

Ensure you have java jdk installed, then in terminal/command use cd to enter correct location and then:


To compile:

javac guiClient.java 

to Run:

java guiClient


## Important Notes

Since this is a proof of concept, it is not usable as-is in the real world. It utilizes SHA-1 to produce a hash of the master password, and then the first 128 bits of the hashed password is then encrypted via AES. This has a couple of issues, the usage of SHA-1 is discouraged since it is prone to collisions. It should be replaced by SHA-2 or SHA-3 and utilize AES-256 for encrpytion. 