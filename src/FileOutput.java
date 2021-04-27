import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileOutput extends Thread {
    private FileOutputStream outputStream;
    private OutputStreamWriter outputStreamWriter;
    private SynchronizedStack<Integer> buffer;
    private FileCopier parent;
    private String path = "";

    // Parameters are: the stack buffer allocated to this thread, path to the input
    // file, parent of this thread.
    public FileOutput(SynchronizedStack<Integer> stack, String path, FileCopier parent) {
        this.path = path;
        this.buffer = stack;
        this.parent = parent;
    }

    // Thread runs until the input file has been read. Every time the buffer is
    // filled by the reader thread, this thread writes the buffers contents to file
    // specified as the output file
    public void run() {
        while (!parent.isEndOfStream()) {
            writeBuffer();
        }
        // To catch the final time, when buffer may be partially filled.
        if (parent.isEndOfStream()) {
            writeBuffer();
        }
    }

    // This function writes the contents of the stack buffer into the selected output file.
    public void writeBuffer() {
        if (buffer.isFull() || parent.isEndOfStream()) {
            int[] array = new int[buffer.getStackCapacity()];

            // We reverse the order of the elements from the stack as they are in reverse
            // order.
            for (int i = (buffer.size() - 1); i > -1; i--) {
                array[i] = buffer.pop();
            }

            try {
                // Opening FileOutputStream always overwrites
                // the opened file so we use additional parameter to
                // append instead.
                outputStream = new FileOutputStream(new File(path), true);
                outputStreamWriter = new OutputStreamWriter(outputStream);

                for (int c : array) {
                    System.out.print((char) c);
                    outputStreamWriter.write(c);
                }
            } catch (IOException e) {
                System.err.println("Error writing charaters to file.");
                e.printStackTrace();
            } finally {
                try {
                    outputStreamWriter.close();
                    outputStream.close();
                } catch (IOException e) {
                    System.err.println("Error closing stream resources.");
                    e.printStackTrace();
                }

            }

        }
    }
}
