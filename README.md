Computer Networks and Distributed Systems — RMI and UDP

The purpose of the exercise is to gain experience of coding RMI and UDP as well as to compare them for relative reliability and ease of use.

Support files:
After extracting the files from the archive provided, you should run install.sh (or install.bat on Windows) to obtain the appropriate build and execution scripts which are described below.
The class MessageInfo (in the common folder) provides a container for the data to be sent and also has a constructor that extracts the data from a string representation. Outline code for each of the client/server pairs can be found in the rmi and udp folders.
The file policy is a simple configuration file required for the RMI code. More constrained policies are possible, but this should provide the lowest barrier to testing.
The Makefile allows Linux users to use make to compile the various parts of the exercise. It can also be used to help configure your preferred development environment with the correct commands, flags and parameters. Windows users can use the build.bat script to compile the exercise in the same way. The other shell scripts (rmiclient (.bat or .sh) etc.) allow users to execute the various parts of the exercise.

