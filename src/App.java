public class App {
    public static void main(String[] args) throws Exception {
        FileCopier fileCopy = new FileCopier("input.txt", "output.txt");
        fileCopy.copyFile();
            
    }
}
