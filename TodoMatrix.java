import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
//import static java.util.stream.Collectors.joining; // for toCSV();
import java.io.PrintWriter;


public class TodoMatrix {
    private final int CAPACITY = 4;
    private static final String[]STATUS = {"IU", "IN","NU","NN"};
    private Map <String, TodoQuarter> matrix;
    public String view;
    
    
    TodoMatrix(){
        view = LocalDate.now().format(DateTimeFormatter.ofPattern("d-M")) + "\n";
        this.matrix = new HashMap<>(CAPACITY);
        for (int i=0; i<CAPACITY; i++) matrix.put(STATUS[i], new TodoQuarter());
    }


    public TodoQuarter getQuarter(String status){
        return matrix.get(status);
        /*if (matrix.containsKey(status)) 
            return matrix.get(status);
        else throw sth;
        */
    }

    public void addItem(ToDoItem task, boolean isImportant){
        if(task.isUrgent()){
            if (isImportant) matrix.get("IU").addItem(task);
            else matrix.get("NU").addItem(task);
            }
        else{ //probably this never takes place:
            if (isImportant) matrix.get("IN").addItem(task);
            else matrix.get("NN").addItem(task);
        }
    }
    
    public void addItem(String title, LocalDate deadline, boolean isImportant){ //possible hiding;
        ToDoItem task = new ToDoItem(title, deadline, isImportant);
       if (isImportant){
            if(task.isUrgent()) getQuarter(STATUS[0]).addItem(task);
            else getQuarter(STATUS[1]).addItem(task);
            }
        else{
            if (task.isUrgent()) getQuarter(STATUS[2]).addItem(task);
            else getQuarter(STATUS[3]).addItem(task);
        }
    }

    public void addItem(String title, String deadline, boolean isImportant){ //possible hiding;
        ToDoItem task = new ToDoItem(title, deadline, isImportant); //calls another TDI constructor;
       if (isImportant){
            if(task.isUrgent()) getQuarter(STATUS[0]).addItem(task);
            else getQuarter(STATUS[1]).addItem(task);
            }
        else{
            if (task.isUrgent()) matrix.get("NU").addItem(task);
            else getQuarter(STATUS[3]).addItem(task);
        }
    }

    public void addItemsFromFile(String fileName)throws FileNotFoundException{
        List<String[]> lines = new ArrayList<>();
        String[]splittedLine;
        Scanner sc = new Scanner(new File(fileName));
        while (sc.hasNextLine()){
            splittedLine = sc.nextLine().split("\\|"); //sc.nextLine().split("\\s*|\\s*")
            lines.add(splittedLine);
        }
        for (String[] aLine: lines){
            //System.out.println("test: "+aLine.length);
            if(aLine.length==3) addItem(aLine[0], setDeadline(aLine[1]), true);
            else addItem(aLine[0], setDeadline(aLine[1]), false);
        }
    }

    private static LocalDate setDeadline(String readDate){ //reFormatDate();
        String[] splittedDate = readDate.split("-");
        String dayOfMonth = splittedDate[0];
        String month = splittedDate[1];
        int year = 2018;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        
        return LocalDate.parse(year + "-" + month + "-" + dayOfMonth, formatter);
    }

    public void saveItemsToFile(String fileName) throws IOException{
        PrintWriter export = new PrintWriter(fileName);
        export.print(toCSV());
        export.close();
    }

    public String toCSV(){
        /*String result = "";
        matrix.forEach((k,v) -> result+= v.toCSV());  //scope of variables in lamda!
        return result;
        */
        return matrix.values().stream().map(TodoQuarter::toCSV).collect(Collectors.joining("\n"));
        /*return Stream.of(getQuarter("IU").toCSV(),
                        getQuarter("IN").toCSV(),
                        getQuarter("NU").toCSV(),
                        getQuarter("NN").toCSV()).collect(Collectors.joining("\n"));*/
    }

    public void archiveItems(){
        matrix.forEach((k,v) -> v.archiveItem());
        //alternative way:
        /*for (Map.Entry<String, TodoQuarter> entry: matrix.entrySet())
            entry.getValue().archiveItem();*/     
        }

    public void updateQuarters(){ //not in specification;//()
        getQuarter("IN").extractUrgent(getQuarter("IU")); // or:
        //matrix.get("IN").extractUrgent(matrix.get("IU"));
        getQuarter("NN").extractUrgent(getQuarter("NU")); //or:
        //matrix.get("NN").extractUrgent(matrix.get("NU"));
    }

    public String toString(){
        matrix.forEach((k,v) -> view += k +":\n" + v + "\n");
        return view;                         
    }


    public void toPrint(){ // not in specification;
        LocalDate today = LocalDate.now();
        System.out.println("Today: "+today);
        for(String status:STATUS) {
            System.out.println(status + ":");
            System.out.println(getQuarter(status));
            //getQuarter(status).toPrint();
        }
    }

    public static void main(String[] args) throws IOException, FileNotFoundException {
        TodoMatrix myTasks = new TodoMatrix();
        //myTasks.toPrint();
        myTasks.addItem("kominek", LocalDate.of(2018,06,15),false); //NN
        myTasks.addItem("as", "2018-05-25",true); //IU
        myTasks.addItem("assigment", "2018-05-24",false); //NU
        myTasks.addItem("samochód", "2018-05-30",true); //IN
        myTasks.addItem("stare", "2018-05-20",true); //IU ?
        myTasks.addItemsFromFile("things.csv");
        System.out.println(myTasks.toCSV());
        myTasks.saveItemsToFile("record.txt");

        //System.out.println(myTasks);
        //myTasks.toPrint();        
    }
}