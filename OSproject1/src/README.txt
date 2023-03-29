CPU.java
 - It is the driver for this program and uses runtime exec to call Memory.java.
 - CPU will get the data from the memory and execute the instructions given to it until end
Memory.java
 - This file will populate memory and then communicates with the CPU file to read and write data
input5.txt
 - when run through the program, this will output a stickman strongman posing

HOW TO RUN
 - Make sure that the input files are in the same directory as the java files or the absolute path is given in command line with no quotes
 - run "javac CPU.java Memory.java" to compile
 - then run "java CPU [input file name] [timer parameter]"
 -  ex: java cpu input.txt 5