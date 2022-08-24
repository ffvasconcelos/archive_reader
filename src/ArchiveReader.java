import lombok.*;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class ArchiveReader {

    private String name;
    private FileInputStream file;
    private Pattern numberRegex = Pattern.compile("(^[0-9]+$)", Pattern.CASE_INSENSITIVE);
    private Pattern idRegex = Pattern.compile("^([a-z])([0-9]|[a-z])*$", Pattern.CASE_INSENSITIVE);
    private Pattern opRegex = Pattern.compile("[-+*/=]", Pattern.CASE_INSENSITIVE);

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

        boolean foundOp = false;

        char operator = '$';

        try {
            while(((myData = this.file.read()) != -1)) {
                opMatcher = opRegex.matcher("" + (char)myData);

                if(opMatcher.find()) {
                    foundOp = true;
                    operator = (char)myData;
                }

                if((char)myData != ' ' && (char)myData != '\n' && (char)myData != ';' && !foundOp) {
                    str = str + (char) myData;
                } else {

                    numberMatcher = numberRegex.matcher(str);
                    idMatcher = idRegex.matcher(str);

                    if(numberMatcher.find()) {
                        System.out.println("<number, " + str + ">");
                    } else if (idMatcher.find()){
                        System.out.println("<id, " + str + ">");
                    }

                    if(operator != '$'){
                        System.out.println("<op, " + operator + ">");
                        operator = '$';
                    }

                    str = "";
                    foundOp = false;
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
