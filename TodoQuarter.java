import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.time.LocalDate;


public class TodoQuarter implements Iterable<ToDoItem>  {  //może extends ArrayList ?// implements Comparable

    private List<ToDoItem> listOfToDoItems = new ArrayList<ToDoItem>(); //konstruktor domyślny? albo:
                                        
   /*TodoQuarter(){  //konstruktor jawny, na wypadek innego rodzaju listy niż ArrayList;
        this.listOfToDoItems = new ArrayList<ToDoItem>();
    }*/
    @Override //?necessary Override?
    public Iterator<ToDoItem> iterator(){ // not in specification;
        return listOfToDoItems.iterator();
    }

    public boolean contains(ToDoItem task){ // not in specification;
        return listOfToDoItems.contains(task);
    }
  
    public int size(){ // not in specification;
        return listOfToDoItems.size();
    }

    public void addItem(String title, LocalDate deadline, boolean isImportant){
        ToDoItem task = new ToDoItem(title, deadline, isImportant);
        listOfToDoItems.add(task);
        listOfToDoItems.sort(null);
    }

    //alternative constructor:
    public void addItem(String title, String deadline, boolean isImportant){
        ToDoItem task = new ToDoItem(title, deadline, isImportant);
        listOfToDoItems.add(task);
        listOfToDoItems.sort(null);
    }
    
    //direct constructor:
    public void addItem(ToDoItem task){
        if (!(task.isDone())) listOfToDoItems.add(task);
        listOfToDoItems.sort(null);
    }

    // for blooming tests... only:
    public void addItem(String title, LocalDate deadline){
        ToDoItem task = new ToDoItem(title, deadline);
        listOfToDoItems.add(task);
        listOfToDoItems.sort(null);
    }

    public ToDoItem getItem(int index){ // not in specification;
        return this.listOfToDoItems.get(index);
    }

    //example how not to do - to remove:
    public void archiveItm(){ //wrong; never remove items with for each loop!
        for(ToDoItem toDo: listOfToDoItems){
            if(toDo.isDone()) 
                listOfToDoItems.remove(toDo);
        }           
    } // interrupted iteration (list.size() is changing!) during removing items!!

    public void archiveItem(){ //possible name: removeIfIsDone();
        listOfToDoItems.removeIf(item_-> item_.isDone());
    }
    
    public void removeItem(int index){
        listOfToDoItems.remove(index);
    }

    private void removeIfUrgent(){
        listOfToDoItems.removeIf(item_-> item_.isUrgent());
    }

    public void toPrint(){ //for test only; not in specification;
        listOfToDoItems.forEach(System.out::println);
    }

    public String toString(){
        String str = "";
        for (ToDoItem task: listOfToDoItems) {
            str += listOfToDoItems.indexOf(task)+1 + ". " + task;
        }
        return str;       
    }

    public String toCSV(){
        return listOfToDoItems.stream().map(ToDoItem::toCsvRow).collect(Collectors.joining("\n"));
    }

    public void extractUrgent(TodoQuarter another){ // tworzy listę urgent itemów; // not in specification;
        for(ToDoItem task: listOfToDoItems){
            if (task.isUrgent()){
                another.addItem(task); 
            }       
        }
        removeIfUrgent();
        //alternative:
        //listOfToDoItems.removeIf(task -> another.contains(task));
    }  
            
    public static void main(String[] args) {
        TodoQuarter taskList = new TodoQuarter();
        TodoQuarter another = new TodoQuarter();
        taskList.addItem("kominek", LocalDate.of(2018,05,30));
        taskList.addItem("assigment", "2018-05-24", true);
        LocalDate date = LocalDate.parse("2018-05-25"); //other way to create TDI
        ToDoItem nieRob = new ToDoItem("grill", date);
        taskList.addItem(nieRob);
        System.out.println("First printing: \n" +taskList);

        System.out.println(taskList.toCSV());

        taskList.extractUrgent(another);
        System.out.println("main quarter after extract(): \n" +taskList);
        System.out.println("another quarter after import: \n" +another);


        nieRob.mark();
        System.out.println("Marked: \n" + taskList);
        //System.out.println(taskList.size());
        taskList.archiveItem();
        //System.out.println(taskList.size());
        System.out.println("After archive: \n" + taskList);
        



        //taskList.toPrint();
        
    }



}
