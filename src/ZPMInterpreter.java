
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class ZPMInterpreter {
	
	static HashMap<String, Pair<String, String>> table;
	static Scanner in;
	static int lineCounter;
	
	public static void main(String[] args) throws FileNotFoundException {
		//the key will be the variable name in the form of a string
		//the array list will hold the variable type as well as the value
		table = new HashMap<String, Pair<String, String>>();
		in = new Scanner(new File(args[0]));
		lineCounter = 0;
		while(in.hasNextLine()) {
			lineCounter++;
			String currentLine = in.nextLine();
			executeAssignment(currentLine);
		}

		in.close();
	
	}
	
	private static void executeAssignment(String currentLine) {
		if(currentLine.contains("FOR")) {
			forLoop(currentLine);
		}
		else if(currentLine.contains("PRINT")) {
			String varName = currentLine.substring(currentLine.indexOf(" ") + 1, currentLine.lastIndexOf(" "));
			printStatement(varName);
		}
		else if(currentLine.contains(" = ")) {
			assignmentStatement(currentLine);
		}
		else if(currentLine.contains(" += ")) {
			String leftSide = currentLine.substring(0, currentLine.indexOf("+") - 1);
			String rightSide = currentLine.substring(currentLine.indexOf("=") + 2, currentLine.indexOf(";") - 1);
			plusEquals(leftSide, rightSide);
		}
		else if(currentLine.contains(" -= ")) {
			String leftSide = currentLine.substring(0, currentLine.indexOf("-") - 1);
			String rightSide = currentLine.substring(currentLine.indexOf("=") + 2, currentLine.indexOf(";") - 1);
			minusEquals(leftSide, rightSide);
		}
		//if the statement is for *=
		else{
			String leftSide = currentLine.substring(0, currentLine.indexOf("*") - 1);
			String rightSide = currentLine.substring(currentLine.indexOf("=") + 2, currentLine.indexOf(";") - 1);
			timesEquals(leftSide, rightSide);
		}
	}
	
	private static void assignmentStatement(String currentLine) {
		String varName = currentLine.substring(0, currentLine.indexOf(" "));
		if(currentLine.contains("\"")){
			String value = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf('"'));
			table.put(varName, new Pair<String, String>("STRING", value));
		}
		// found from https://stackoverflow.com/questions/18590901/check-if-a-string-contains-numbers-java
		// checks to see if the assignment is for an integer
		else if(currentLine.matches(".*\\d+.*")){
			String value = currentLine.substring(currentLine.indexOf("=") + 2, currentLine.lastIndexOf(" "));
			table.put(varName, new Pair<String, String>("INT", value));
		}
		//handles if we are assigning a variable another variables value
		else {
			Pair<String, String> tempPair = table.get(currentLine.substring(currentLine.indexOf("=") + 2, currentLine.lastIndexOf(" ")));
			table.put(varName, tempPair);
		}
	}
	
	private static void printStatement(String varName) {
		System.out.println(varName + "=" + table.get(varName).y);
	}
	
	
	private static void plusEquals(String leftSide, String rightSide) {
		if(table.get(leftSide) == null) {
			System.out.println("RUNTIME ERROR: line " + lineCounter);
			System.exit(0);
		}
		String leftSideType = table.get(leftSide).x;
		String rightSideType = null;
		if(rightSide.contains("\"")) {
			 rightSideType = "STRING";
			 rightSide = rightSide.replaceAll("\"", "");
		}
		else if(rightSide.matches(".*\\d+.*")) {
			rightSideType = "INT";
		}
		else {
			
			if(leftSideType.equals(table.get(rightSide).x) == false) {
				System.out.println("RUNTIME ERROR: line " + lineCounter);
				System.exit(0);
			}
			else {
				rightSide = table.get(rightSide).y;
				rightSideType = leftSideType;
			}
		}
		if (leftSideType.equals(rightSideType) == false) {
			System.out.println("RUNTIME ERROR: line " + lineCounter);
			System.exit(0);
		}
		//actually adding the values together, use an if statement to see what the type is and I made a temp variable
		//in each case that would be inserted into the pair
		else {
			if(leftSideType.equals("STRING") == true) {
				String temp = table.get(leftSide).y;
				temp += rightSide;
				table.put(leftSide, new Pair<String, String>("STRING", temp));
			}
			else {
				int temp = (int)Integer.parseInt(rightSide);
				temp += (int)Integer.parseInt(table.get(leftSide).y);
				table.put(leftSide, new Pair<String, String>("INT", Integer.toString(temp)));
			}
		}
	}
	private static void minusEquals(String leftSide, String rightSide) {
		if(table.get(leftSide) == null) {
			System.out.println("RUNTIME ERROR: line " + lineCounter);
			System.exit(0);
		}
		String leftSideType = table.get(leftSide).x;
		String rightSideType = null;
		if(rightSide.contains("\"")) {
			 rightSideType = "STRING";
		}
		else if(rightSide.matches(".*\\d+.*")) {
			rightSideType = "INT";
		}
		else {
			rightSideType = table.get(rightSide).x;
			rightSide = table.get(rightSide).y;
		}
		if(leftSideType.equals("STRING") == true || rightSideType.equals("STRING") == true) {
			System.out.println("RUNTIME ERROR: line " + lineCounter);
			System.exit(0);
		}
		else {
			int tempRightSide = (int)Integer.parseInt(rightSide);
			int tempLeftSide = (int)Integer.parseInt(table.get(leftSide).y);
			int temp = tempLeftSide - tempRightSide;
			table.put(leftSide, new Pair<String, String>("INT", Integer.toString(temp)));
		}
		
	}

	private static void timesEquals(String leftSide, String rightSide) {
		if(table.get(leftSide) == null) {
			System.out.println("RUNTIME ERROR: line " + lineCounter);
			System.exit(0);
		}
		String leftSideType = table.get(leftSide).x;
		String rightSideType = null;
		if(rightSide.contains("\"")) {
			 rightSideType = "STRING";
		}
		else if(rightSide.matches(".*\\d+.*")) {
			rightSideType = "INT";
		}
		else {
			rightSideType = table.get(rightSide).x;
			rightSide = table.get(rightSide).y;
		}
		if(leftSideType.equals("STRING") == true || rightSideType.equals("STRING") == true) {
			System.out.println("RUNTIME ERROR: line " + lineCounter);
			System.exit(0);
		}
		else {
			int tempRightSide = (int)Integer.parseInt(rightSide);
			int tempLeftSide = (int)Integer.parseInt(table.get(leftSide).y);
			int temp = tempLeftSide * tempRightSide;
			table.put(leftSide, new Pair<String, String>("INT", Integer.toString(temp)));
		}
	}
	
	private static void forLoop(String currentLine) {
		
		//finds the number of times the for loop should be executed
		int increment = (int)Integer.parseInt(currentLine.substring(4, currentLine.indexOf(' ', 4)));
		
		int startIndex = 0;
		for(int i = 0; i < increment; i++) {
		
			//collects all of the assignment statements to be done in the for loop
			startIndex = currentLine.indexOf(' ', 4) + 1;
			String currentStatement = currentLine.substring(startIndex, currentLine.indexOf(';', startIndex) + 1);
			
			while(currentStatement.equals("ENDFOR") == false) {
				
				executeAssignment(currentStatement);
				//changes the start index to be where the next command begins
				startIndex += currentStatement.length() + 1;
				if(currentLine.substring(startIndex, startIndex + 6).equals("ENDFOR") == true) {
					break;
				}
				currentStatement = currentLine.substring(startIndex, currentLine.indexOf(';', startIndex) + 1);		
			}
		}
		
	}
	
	
}
