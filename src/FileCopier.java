public class FileCopier {
    private SynchronizedStack<Integer> stack;
    private String inputFilePath;
    private String outputFilePath;
    private FileInput input;
    private FileOutput output;
    private boolean endOfStream = false;

    public FileCopier(String inputFilePath, String outputFilePath) {
        this(inputFilePath, outputFilePath, 50);
    }

    public FileCopier(String inputFilePath, String outputFilePath, int bufferSize) {
        stack = new SynchronizedStack<>(bufferSize);
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
    }

    public void copyFile() {
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

    //This function is used from the reader thread, to inform that the file has been completely read.
    public void setEndOfStream(boolean endOfStream) {
        this.endOfStream = endOfStream;
    }

}
