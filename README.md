## FileCopier

Implemented in Java. Allows copying of files. Uses a thread to read file to copy and a thread to create the copy.

### Features
Needs path to file to be copied and the path to where the copied file is saved.

FileCopier filecopier = new Filecopier("Path to file to be copied", "Path to copied file to be saved");

fileCopier.copyFile();

It is also possible to create Filecopier by giving it only the source file. In this case the copy will be placed in the same folder as the source file with the name of sourcefile copy. Name of sourcefile: file.txt name of copy: file copy.txt

FileCopier filecopier = new Filecopier("Path to file to be copied");

filecopier.copyFile();

It is also possible to change the internal buffer size.

FileCopier filecopier = new FileCopier(String inputFilePath, String outputFilePath, int bufferSize);

fileCopier.copyFile();
