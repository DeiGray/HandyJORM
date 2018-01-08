# For easy database interacting


## _What is HandyJORM_

HandyJORM is a `Java Object-Relationnal Mapping` library that permit an user-friendly interaction with database. The main use case is to create one object for each table in your database.  

---

## How to use HandyJORM ?

#### _Creating DBSession class_

Firstly, you need a DBSession class to connect to the database. The better way to do this is to create a class which extends DBSession. Then you make a `Singleton`, because you only need one instance per application.

```java
public class MyDBSess extends BaseDBSession {
    private static MyDBSess instance;
    /*
        Other params (connection Strings, passwords, etc.)
    */
    
    private MyDBSess(/*params*/){
        super(/*params*/);
    }
    
    public static MyDBSess getInstance(){
        if(null == this.instance){
            this.instance = new MyDBSess(/*params*/);
        }
        return this.instance;
    }
}
```

When your DBSess is correclty define, you can create your models.

#### Creating models
    
After creating DBSess, you can now create your first model to interact with a table.

You need to create a class that extends BaseModel class.
Then override the abstract method, and the refrences method if you want to interact with other models.


```java
public class Note extends BaseModel {
    public final static String TAB_NAME = "Note";
    public final static String COL_ID = "id_col",
    //other fields in the table.

    public int id_PK; //the primary key
    public Timestamp dateInsert_AT; //an other field
    public int idUser_FK; // a foreign key
```
**IMPORTANT :**
Extensions `_PK`,`_AT`,`_FK` are essential to make BaseModel work. They are interpreted by HandyJORM :
- `_PK` represent an attribute in the BaseModel class as a **P**rimary **K**ey in a table.
- `_AT` represent a casual field in a table in database.
- `_FK` represent a **F**oreign **K**ey in database.

If you don't use this extensions, your attribute will not be interpreted by HandyJORM.
```java
    private NotePriorite _notePriorite;

    public Note() throws UnknowFieldModelException, IllegalSoftQueryException {
        // NEVER FORGET this line.
        super();
        
        //default value for this class
        idUser_FK = 1;
    }

    @Override
    public String getTableName() {
        return TAB_NOTE;
    }

    @Override
    public String[][] bindColumns() {
        String[][] columns = {
                //the column name in database,  the field name in the class 
                {COL_NO_ID, "id_PK"},
                {COL_NO_LIB, "lib_AT"},
                {COL_US_ID, "idUser_FK"},};
        return columns;
    }

    //if you want to find the other class, linked by foreign key, you can give the name.
    @Override
    public String [][] references(){
        String [][] references = {
                //my colonnes name(in Note class)     
                           //class name of the linked class.
                {COL_NP_ID,NotePriorite.COL_NP_ID,NotePriorite.class.toString()},
                {COL_NT_ID,NoteType.COL_NT_ID,NoteType.class.toString()},
        };

        return references;
    }
```

If you want to easy access to linked model (by foreign key);

```javascript
    // ...
    
    public NotePriorite getNotePriorite(){
        return _notePriorite;
    }

    public void searchNotePriorite(){
        try {
            _notePriorite =  this.hasOne(NotePriorite.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvalidFieldNameGiven invalidFieldNameGiven) {
            invalidFieldNameGiven.printStackTrace();
        }
    }
```

#### Using a model

```java
public static void main(String args[]){
    //FIRST, init all models you will use (that extends BaseDbSession), with this connection
    Note.initDbSession(MyDBSess.getInstance());
    //...
```
Now, you can use all models you wants to.

I'm going to explain some `CRUD` examples. 

>_`CRUD` means `Create Read Update Delete`. It is a casual development case in web devs. I use it in `Java` to explane data interactions with database, like **inserting**, **deleting**, **updating** or just **selecting** in the database you specified with your DBSess class._

In the next code example, I describe how to **CRUD** with a model on HandyJORM. 
I'm using the same example as before. I'm gonna create a model, insert it, then find the new line, modify, find again and delete it.

>Note that no try/catch block are represented here, be they actually exists.
```java
    //Create a note
    Note note = new Note();
    
    //set a primary key to the note
    note.id_PK = 1;
    
    //insert it
    note.save();
    
    //search in database a note like our note (that has primary key = 1), and store it as note2
    Note note2 = note.findOneLikeMe(Note.class);
    
    //edit and update it in database.
    note2.lib_AT = "a note";
    note2.update();
    
    Note note3 = note.findOneLikeMe(Note.class);
    System.out.println(note3.lib_AT+" with primary key : "+note3.id_PK); //print "a note".
    
    //delete the value in database.
    note3.delete();    
```    

#### Others `find` uses    
```java

//Ask a group of notes, with advances request.
ArrayList<Note> notes;
notes = Note.find(Note.class)
            .where(new SqlFilter(Note.COL_NT_ID, DBPropertyTypeEnum.Integer,_filter.id_PK,ComparisonStrategyEnum.Equals))
            .orderBy(new SqlOrder(Note.COL_NO_DATE_INSERT, DirectionOrderEnum.DESC))
            .all();
            //OR
            .one();
            //Give you the first selected Note instance.
```
