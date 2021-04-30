package app;
import java.io.File;

import app.threads.FileInput;
import app.threads.FileOutput;

public class FileCopier {
    private SynchronizedStack<Integer> stack;
    private String inputFilePath;
    private String outputFilePath;
    private FileInput input;
    private FileOutput output;
    private boolean endOfStream = false;

    public FileCopier(){
        this("", "", 50);
    }

    public FileCopier(String inputFilePath) {
        this(inputFilePath, createOutputFileName(inputFilePath), 50);
    }

    public FileCopier(String inputFilePath, String outputFilePath) {
        this(inputFilePath, outputFilePath, 50);
    }

    public FileCopier(String inputFilePath, String outputFilePath, int bufferSize) {
        stack = new SynchronizedStack<>(bufferSize);
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
    }

    // This method is synchronized, so that other threads have to wait until the
    // copying is done. Returns true if copying is successfull false otherwise.
    public synchronized boolean copyFile() {
        File outputFile = new File(outputFilePath);

        while (outputFile.isFile() || outputFilePath.equals("")) {
            outputFile = new File(createOutputFileName(outputFile.getName()));
            outputFilePath = outputFile.getName();
        }

        this.endOfStream = false;

        input = new FileInput(stack, inputFilePath, this);
        output = new FileOutput(stack, outputFilePath, this);

        input.start();
        output.start();

        //We join the current thread to the FileOutput thread, so that threads aren't interrupted by new function call of copyfile.
        try {
            output.join();
        } catch (InterruptedException e) {
            System.err.println("FileOutput thread interrupted.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    public boolean isEndOfStream() {
        return endOfStream;
    }

    // This function is used from the reader thread, to inform that the file has
    // been completely read.
    public void setEndOfStream(boolean endOfStream) {
        this.endOfStream = endOfStream;
    }

    // This function is used to generate the output file name in case it hasn't been
    // given or the output file already exists.
    public static String createOutputFileName(String FilePath) {
        File inputFile = new File(FilePath);
        // We get the folder of the file is in.
        String inputFileFolder = inputFile.getAbsoluteFile().getParent();
        String[] nameArray = inputFile.getName().split("\\.");
        return inputFileFolder + "\\" + nameArray[0] + " copy." + nameArray[nameArray.length - 1];
    }

}
