import java.io.File;
import java.nio.file.FileAlreadyExistsException;

public class FileCopier {
    private SynchronizedStack<Integer> stack;
    private String inputFilePath;
    private String outputFilePath;
    private FileInput input;
    private FileOutput output;
    private boolean endOfStream = false;

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

    public void copyFile() throws FileAlreadyExistsException {
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

    public static String createOutputFileName(String inputFilePath) {
        File inputFile = new File(inputFilePath);
        String[] nameArray = inputFile.getName().split("\\.");
        return nameArray[0] + " copy." + nameArray[nameArray.length - 1];
    }

}
