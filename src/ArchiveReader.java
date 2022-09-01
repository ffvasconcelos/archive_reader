import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArchiveReader {

    private List<String> decl = Arrays.asList("class", "extends", "public", "static", "void", "main", "length", "this", "new");
    private List<String> fluxo = Arrays.asList("if", "else", "while", "return", "System.out.println");
    private List<String> tipo = Arrays.asList("boolean", "int", "true", "false", "String");
    private String name;
    private FileInputStream file;
    private Pattern numberRegex = Pattern.compile("(^[0-9]+$)", Pattern.CASE_INSENSITIVE);
    private Pattern idRegex = Pattern.compile("^([a-z])([0-9]|[a-z])*$", Pattern.CASE_INSENSITIVE);
    private Pattern opRegex = Pattern.compile("[-+*/=&<>!.,]", Pattern.CASE_INSENSITIVE);
    private Pattern delimRegex = Pattern.compile("[(){};]", Pattern.CASE_INSENSITIVE);
    public ArchiveReader(String name) {
        this.name = name;

        try {
            this.file = new FileInputStream(name);
        } catch (Exception ex) {
            System.out.println("Error reading the file!");
        }
    }

    public void readFile() {
        int myData;

        try {
            while((myData = this.file.read()) != -1){
                System.out.print((char)myData);
            }
        } catch (Exception ex) {
            System.out.println("Error reading the file!");
        }
    }

    public void closeFile() {
        try {
            this.file.close();
        } catch (Exception ex) {
            System.out.println("Error reading the file!");
        }
    }

    public void numberFinder() {
        int myData;

        String str = "";
        try {
            while(((myData = this.file.read()) != -1)) {
                if((char)myData != ' ' && (char)myData != '\n') {
                    str = str + (char) myData;
                } else {
                    Matcher matcher = numberRegex.matcher(str);

                    if(matcher.find()){
                        System.out.println("<number, " + str + " >");
                    }

                    str = "";
                }
            }
            Matcher matcher = numberRegex.matcher(str);

            if(matcher.find()){
                System.out.println("<number, " + str + " >");
            }
        } catch (Exception ex) {
            System.out.println("Error reading the file");
        }

        resetFile();
    }

    public void idFinder() {
        int myData;

        String str = "";
        try {
            while(((myData = this.file.read()) != -1)) {
                if((char)myData != ' ' && (char)myData != '\n' ) {
                    str = str + (char) myData;
                } else {
                    Matcher matcher = idRegex.matcher(str);

                    if(matcher.find()){
                        System.out.println("<id, " + str + " >");
                    }

                    str = "";
                }
            }
            Matcher matcher = idRegex.matcher(str);

            if(matcher.find()){
                System.out.println("<id, " + str + " >");
            }
        } catch (Exception ex) {
            System.out.println("Error reading the file");
        }

        resetFile();
    }

    public void opFinder() {
        int myData;

        String str = "";
        try {
            while(((myData = this.file.read()) != -1)) {
                if((char)myData != ' ' && (char)myData != '\n') {
                    str = str + (char) myData;
                } else {
                    Matcher matcher = opRegex.matcher(str);

                    if(matcher.find()){
                        System.out.println("<op, " + str + " >");
                    }

                    str = "";
                }
            }
            Matcher matcher = opRegex.matcher(str);

            if(matcher.find()){
                System.out.println("<op, " + str + " >");
            }
        } catch (Exception ex) {
            System.out.println("Error reading the file");
        }

        resetFile();
    }

    public void codeAnalyser () {
        this.idFinder();
        this.numberFinder();
        this.opFinder();
    }

    public void resetFile () {
        try {
            this.file = new FileInputStream(name);
        } catch (Exception ex) {
            System.out.println("Error reading the file!");
        }
    }

    public void generalFinder () {
        int myData;

        String str = "";

        Matcher numberMatcher = numberRegex.matcher(str);
        Matcher idMatcher = idRegex.matcher(str);
        Matcher opMatcher = opRegex.matcher(str);
        Matcher delimMatcher = delimRegex.matcher(str);

        boolean foundOp = false;
        boolean foundDelim = false;

        char operator = '$';
        char delim = '$';

        try {
            while(((myData = this.file.read()) != -1)) {
                opMatcher = opRegex.matcher("" + (char)myData);
                delimMatcher = delimRegex.matcher("" + (char)myData);

                if(opMatcher.find()) {
                    foundOp = true;
                    operator = (char) myData;
                }

                if(delimMatcher.find()) {
                    foundDelim = true;
                    delim = (char) myData;
                }

                if((char)myData != ' ' && (char)myData != '\n' && (char)myData != ';' && !foundOp && !foundDelim) {
                    str = str + (char) myData;
                } else {

                    numberMatcher = numberRegex.matcher(str);
                    idMatcher = idRegex.matcher(str);

                    if(numberMatcher.find()) {
                        System.out.println("<number, " + str + ">");
                    } else if (idMatcher.find()){
                        if (decl.contains(str)){
                            System.out.println("<decl, " + str + ">");
                        } else if (fluxo.contains(str)) {
                            System.out.println("<fluxo, " + str + ">");
                        } else if (tipo.contains(str)) {
                            System.out.println("<tipo, " + str + ">");
                        } else {
                            System.out.println("<id, " + str + ">");
                        }
                    }

                    if(operator != '$'){
                        System.out.println("<op, " + operator + ">");
                        operator = '$';
                    }

                    if(delim != '$'){
                        System.out.println("<delim, " + delim + ">");
                        delim = '$';
                    }

                    str = "";
                    foundOp = false;
                    foundDelim = false;
                }
            }
            Matcher matcher = idRegex.matcher(str);

            if(matcher.find()){
                System.out.println("<id, " + str + " >");
            }
        } catch (Exception ex) {
            System.out.println("Error reading the file");
        }

        resetFile();
    }

}
