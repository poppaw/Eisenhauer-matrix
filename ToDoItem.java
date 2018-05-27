import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects; //for equals() and comparteTo2();
import java.util.stream.Collectors; // for toCSV();
import java.util.stream.Stream; // as above;
import java.util.List; //for tests in main only - remove;
import java.util.ArrayList; //as above
//import static java.util.stream.Collectors.joining; // for toCSV();


public  class ToDoItem implements Comparable<ToDoItem> { //need Comparable to enable sort() method;
    String title;
    LocalDate deadline;
    boolean isDone;
    boolean isImportant;


    ToDoItem(String title, LocalDate deadline, boolean isImportant){
        this.title = title;
        this.deadline = deadline; //use formatter (d-M);
        this.isDone = false;
        this.isImportant = isImportant;
    }

    //alter constructor
    ToDoItem(String title, String deadline, boolean isImportant){ 
        this.title = title;
        this.deadline = LocalDate.parse(deadline); //use formatter;
        this.isDone = false;
        this.isImportant = isImportant;
    }
    
    //for blooming tests only...:
    ToDoItem(String title, LocalDate deadline){
        this.title = title;
        this.deadline = deadline; //use formatter (d-M);
        this.isDone = false;
        this.isImportant = false;
    }


    public void mark(){
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;        
    }

    public boolean isDone(){
		if (this.isDone) return true;
        return false;
    }

    public boolean isUrgent(){ // not in specification
        if(this.isDone) return false;
        LocalDate today = LocalDate.now();
        if (today.until(this.deadline, ChronoUnit.DAYS) <=3) return true; //LocalDate cannot be converted to int
        return false;   
    }

    public boolean isExpired(){ //isDeadlined - not in specification
        if(this.isDone) return false;
        LocalDate today = LocalDate.now();
        if (today.until(this.deadline, ChronoUnit.DAYS) <0) return true; //LocalDate.compareTo() is inappropriate method
        return false;
    }

    public String toString(){
        if(this.isDone == true)
            return "[X] " + formatDate(deadline) + " " + title +"\n";
        if(isExpired())
            return "[!] " + formatDate(deadline) + " " + title +": Expired!\n"; //will fail the test!
        return "[ ] " + formatDate(deadline) + " " + title + "\n";
    }

    public String toCsvRow() {
        return Stream.of(title, formatDate(deadline), isImportantAsString())
                    .collect(Collectors.joining("|"));
    }

    public static String formatDate(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern("d-M"));
    }

    private String isImportantAsString(){ // needed for csv export;
        String suffix = isImportant ? "important" : "";
        return suffix;
    }

    // compareTo potrzebne do sortowania objektów TDI w liście. // not in specification;
    public int compareTo(ToDoItem that){ //it works on condition diamond: Comparable<ToDoItem>;
        if (this.deadline.equals(that.deadline)) return this.title.compareToIgnoreCase(that.title);
        return this.deadline.compareTo(that.deadline);
    }

    public int compareTo2(Object obj){ //it works also with raw Comparable(without diamond <>);
        ToDoItem other = (ToDoItem) obj; //casting necessary when raw Comparable;
        return this.deadline.compareTo(other.deadline); //what if two tasks has the same deadline?
    }

    public boolean equals(Object thatObject){
        if (this == thatObject) return true;
        if (thatObject == null) return false;
        if (getClass() != thatObject.getClass()) return false;
        ToDoItem that = (ToDoItem) thatObject;
        return Objects.equals(this.title, that.title) && Objects.equals(this.deadline, that.deadline);
    }

    //ask mentor: is myEquals sufficient equals meth. or do I really need the model method from above?
    public boolean myEquals(ToDoItem that) {
        if (this.deadline.equals(that.deadline) && this.title.equals(that.title))
            return true;
        return false;
    }

    public int hashCode(){
        return Objects.hash(title, deadline);
    }

    public static void main(String[] args) { // for tests only;
        ToDoItem zrob = new ToDoItem("kominek", LocalDate.of(2018,05,15)); //a way to create TDI
        LocalDate deadline = LocalDate.of(2014, Month.JUNE, 4);
        System.out.println(zrob.toCsvRow());

        String test = zrob.toCsvRow();
        String[]splittedLine = test.split("\\|");
        System.out.println(splittedLine.length);


        ToDoItem najwcześniej = new ToDoItem("repair the car", deadline);
        ToDoItem najwcześniej2 = new ToDoItem("no i co?", deadline);
        LocalDate date = LocalDate.parse("2018-05-27"); //other way to create TDI
        ToDoItem najpóźniej = new ToDoItem("grill", date );
        List<ToDoItem> toDo = new ArrayList<ToDoItem>();
        toDo.add(najpóźniej);
        toDo.add(najwcześniej);
        toDo.add(zrob);
        toDo.add(najwcześniej2);
        najwcześniej.mark();

        System.out.println("Unsorted:\n" + toDo);

        toDo.sort(null);
        System.out.println("Sorted with null comparator\n:" + toDo);

        toDo.sort((t1, t2) -> t1.compareTo(t2));
        System.out.println("t1.compareTo(t2):\n"+ toDo);

        toDo.sort((t1, t2) -> t2.compareTo(t1));
        System.out.println("t2.compareTo(t1):\n"+ toDo);

        System.out.println("Using double collon ::");
        toDo.forEach(System.out::println); // equivalent of: for (ToDoItem:task) println(task);


        System.out.println(deadline);
        //System.out.println("Today: "+today);
       
        System.out.println(zrob.isExpired());
        System.out.println(najpóźniej.isExpired());

        //System.out.println(najpóźniej.compareTo(zrob));
        //System.out.println(zrob.compareTo(najpóźniej));
        //System.out.println(najpóźniej.isUrgent());
        //System.out.println(zrob.equals(najpóźniej));
        //System.out.println(toDo.contains(zrob));
    }

}
