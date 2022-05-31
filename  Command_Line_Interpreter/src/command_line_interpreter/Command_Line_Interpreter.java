package command_line_interpreter;

/**
 * This file contains all classes and functions we need
 */
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


class Parser {
    String commandName;
    String[] args;

    public boolean parse(String input){
        String [] Input = input.split(" ");

        commandName = Input[0];

        args = new String[Input.length-1];
        for(int i = 0; i < Input.length-1 ; i++){
            args[i] = Input[i+1];
        }
        return true;
    }
    public String getCommandName(){
        return commandName;
    }
    public String[] getArgs(){
        return args;
    }
}


class Terminal {
    Parser parser=new Parser();
    String Homep = System.getProperty("user.home") + "\\Desktop";
    String path = new File(".").getCanonicalPath();
    static  String s;

    Terminal() throws IOException {}

    public void echo(String [] string)
    {
        for (String s:string) {
            System.out.print(s+" ");
        }
        System.out.println();
    }

    /**
     * This function to print the current path
     * @throws IOException
     */
    public void pwd() throws IOException {
        System.out.println(path);
    }

    /**
     * changes the current path to home directory path.
     * @throws IOException
     */
    public void cd() throws IOException {
        System.out.println(Homep);
        path = Homep;
    }

    /**
     * changes the current directory to the previous directory or to the given path
     * @param newPath
     * @throws IOException
     */
    public void cd(String newPath) throws IOException {
        if(newPath.equals("..")) {
            path = path.substring(0,path.lastIndexOf("\\"));
            System.out.println(path);
        }
        else if (newPath.contains(":\\")){
            System.out.println(newPath);
            path = newPath;
        }
        else {
            File f1=new File(path);
            String p = path;
            p += "\\" + newPath;
            path = p;
            System.out.println(path);
        }
    }

    /**
     * this function to list the contents of the current directory sorted alphabetically.
     */
    public void ls(){
        File file = new File(path);
        ArrayList<String>files = new ArrayList<String>();

        for(int i = 0; i < file.list().length ;i++)
            files.add(file.list()[i]);

        Collections.sort(files);

        if(files.size()==0) System.out.println("Empty directory");
        else {
            for (String str : files) {
                System.out.println(str);
            }
        }
    }

    /**
     * this function to list the contents of the current directory reverse order.
     */
    public  void ls_r(){

        File file = new File(path);

        ArrayList<String>files = new ArrayList<String>();

        for(int i = 0; i < file.list().length ;i++)
            files.add(file.list()[i]);

        Collections.sort(files, Collections.reverseOrder());

        if(files.size()==0) System.out.println("Empty directory");
        else {
            for (String str : files) {
                System.out.println(str);
            }
        }
    }

    /**
     * This function to create a directory
     * @param s
     * @throws IOException
     */
    public void mkdir(String s) throws IOException {
        if(s.contains(":\\")){
            Path p = Paths.get(s);
            File f1 = new File(String.valueOf(p));
            f1.mkdir();
        }
        else {
            String filePath = path;
            filePath += "\\";
            filePath += s;
            File f1 = new File(filePath);
            f1.mkdir();
        }
    }

    /**
     * This function to removes the directory only if it is empty.
     * @param s
     * @throws IOException
     */
    public void rmdir(String s) throws IOException {
        if (s.equals("*")) {
            File f1 = new File(path);
            File[] files = f1.listFiles();
            for (File file : files) {
                if (file.length() == 0) {
                    file.delete();
                }
            }
        } else if (!s.contains(":\\")) {
            String filePath = path;
            filePath += "\\";
            filePath += s;
            File f1 = new File(filePath);
            if (f1.length() == 0) {
                f1.delete();
            }
            else{
                System.out.println(s+ " Directory not empty");
            }
        } else {
            Path P = Paths.get(s);
            File f1 = new File(String.valueOf(P));
            if (f1.length() == 0) {
                f1.delete();
            }
            else{
                System.out.println(s+ " Directory not empty");
            }
        }
    }

    /**
     * This function to create file.
     * @param string
     * @throws IOException
     */
    public void touch(String string) throws IOException {
        File file;
        if (!string.contains(":\\")) {

            String newPath = path;
            newPath += "\\";
            newPath += string;
            file = new File(newPath);
        }
        else{
            file = new File(string);
        }
        if(file.exists()){
            System.out.println("File already exists ");
        }
        else {
            file.createNewFile();
        }
    }

    /**
     * This function to copy the first file onto the second.
     * @param source
     * @param destination
     * @throws IOException
     */
    public void cp(String source, String destination) throws IOException {
        String s="",d="";
        s += path+ "\\"+ source;
        d += path+ "\\"+ destination;

        Files.copy(Paths.get(s),Files.newOutputStream(Paths.get(d)));
    }

    /**
     * This function to  copy the first directory into the second one.
     * @param source
     * @param destination
     * @throws IOException
     */
    public void cp_r (String source, String destination) throws IOException {
        String s="",d="";
        s += path+ "\\"+ source;
        d += path+ "\\"+ destination;
        File f1 = new File(s);
        File f2 = new File(d);
        if (!f2.exists()) {
            f2.mkdir();
        }

        File[] files = f1.listFiles();
        if (files != null) {
            for (File f : files) {
                String newf= String.valueOf(Paths.get(d).toAbsolutePath()+File.separator+f.getName());
                if (f.isDirectory()) {
                    cp_r(String.valueOf(source+"\\"+f.getName()), destination+"\\"+f.getName());
                } else {
                    try {
                        Files.copy(f.toPath(), Files.newOutputStream(Paths.get(newf)));
                    }
                    catch (IOException e){
                        System.out.println("File exist");
                    }
                }
            }
        }
    }

    /**
     * This function to remove a file in the current directory.
     * @param file
     * @throws IOException
     */
    public  void rm(String file) throws IOException {
        File deletedfile=new File(file);

        if (!file.contains(":\\")) {
            String newPath = path;
            newPath += "\\";
            newPath += file;
            deletedfile = new File(newPath);

        }
        if (deletedfile.delete()) {
            System.out.println("Deleted");
        } else {
            System.out.println("Failed to delete");
        }
    }

    /**
     * This function to print the file’s content
     * @param s
     * @throws IOException
     */
    public  void cat(String s) throws IOException {
        File file= new File(s);
        if (file.exists()) {
            String text = readFromFile(file);
            System.out.println(text);
        } else {
            System.out.println("The file not found");
        }
    }

    /**
     * This function to concatenate the content of the 2 files and prints it.
     * @param s1
     * @param s2
     * @throws IOException
     */
    public  void cat(String s1, String s2) throws IOException {
        File file1= new File(s1);
        File file2= new File(s2);

        if (file1.exists() && file2.exists()) {
            String text1 = readFromFile(file1);
            System.out.println(text1);

            String text2 = readFromFile(file2);
            System.out.println(text2);
        } else {
            System.out.println("One of the files not found");
        }
    }

    /**
     * This function to choose the suitable command method to be called
     * @throws IOException
     */
    public void chooseCommandAction() throws IOException {
        parser.parse(s);
        if (parser.getCommandName().equals("echo")){
            echo(parser.getArgs());
        }
        else if (parser.getCommandName().equals("pwd")){
            if (parser.getArgs().length!=0) {
                System.out.println("Error: Command not found or invalid parameters are entered ");
            }
            else {
                pwd();
            }
        }
        else if(parser.getCommandName().equals("cd")){
            if (parser.getArgs().length==0){
                cd();
            }
            else if (parser.getArgs().length==1){
                cd(parser.getArgs()[0]);
            }
            else{
                System.out.println("Error: Command not found or invalid parameters are entered ");

            }
        }
        else if (parser.getCommandName().equals("ls")){
            if (parser.getArgs().length!=0)
                System.out.println("Error: Command not found or invalid parameters are entered ");
            else{
                ls();
            }
        }
        else if (parser.getCommandName().equals("ls-r")){
            if (parser.getArgs().length!=0)
                System.out.println("Error: Command not found or invalid parameters are entered ");

            else{
                ls_r();
            }
        }

        else if(parser.getCommandName().equals("mkdir")){
            for(int i=0;i<parser.getArgs().length;i++){
                mkdir(parser.getArgs()[i]);
            }

        }
        else if (parser.getCommandName().equals("rmdir")){
            if (parser.getArgs().length!=1)
                System.out.println("Error: Command not found or invalid parameters are entered ");

            else{
                rmdir(parser.getArgs()[0]);
            }

        }
        else if(parser.getCommandName().equals("touch")){
            if (parser.getArgs().length!=1)
                System.out.println("Error: Command not found or invalid parameters are entered ");
            else{
                touch(parser.getArgs()[0]);
            }

        }
        else if (parser.getCommandName().equals("cp")) {
            if (parser.getArgs().length!=2)
                System.out.println("Error: Command not found or invalid parameters are entered ");

            else {
                cp(parser.getArgs()[0],parser.getArgs()[1]);
            }

        }

        else if (parser.getCommandName().equals("cp-r")){
            if (parser.getArgs().length!=2)
                System.out.println("Error: Command not found or invalid parameters are entered ");

            else{
                cp_r(parser.getArgs()[0],parser.getArgs()[1]);
            }
        }
        else if (parser.getCommandName().equals("rm")){
            if (parser.getArgs().length!=1)
                System.out.println("Error: Command not found or invalid parameters are entered ");

            else{
                rm(parser.getArgs()[0]);
            }

        }
        else if (parser.getCommandName().equals("cat")){
            if (parser.getArgs().length==1){
                cat(parser.getArgs()[0]);
            }
            else if (parser.getArgs().length==2) {
                cat(parser.getArgs()[0], parser.getArgs()[1]);
            }
            else{
                System.out.println("Error: Command not found or invalid parameters are entered ");

            }

        }
        else if (parser.getCommandName().equals("exit")){
            System.exit(0);
        }
        else{
            System.out.println("Error: Command not found or invalid parameters are entered ");
        }
    }

    /**
     * This function is to read from a given file
     * @param file
     * @return
     * @throws IOException
     */
    public String readFromFile(File file) throws IOException {
        Scanner reader = new Scanner(file);
        String text = "";
        while (reader.hasNextLine()) {
            text += reader.nextLine();
        }
        return text;
    }

    public static void main(String[] args) throws IOException {
        Terminal t = new Terminal();
        // input to take string with spaces
        Scanner input = new Scanner(System.in).useDelimiter("\n");
        while (true) {
            System.out.print(">");
            String  temp = new String();
            s = input.next();
            if(s.contains("cp -r") ||s.contains("ls -r")){
                temp += s.charAt(0);
                temp += s.charAt(1);temp += s.charAt(3);temp += s.charAt(4);
                temp+=' ';
                for(int i  = 6; i < s.length(); i++){
                    temp += s.charAt(i);
                }
                s = temp;
            }
            t.chooseCommandAction();
        }
    }
}
