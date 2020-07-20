# Centenarian Birthdays

## Pre requisites
 - Java 8
 - Junit
 - Maven
    
## Building the jar
 - Run `mvn package`

## Executing the code

This program is run with the next arguments: citizen, opt-out-emails, date 

    --citizen data/citizens.csv
    --opt-out-emails data/optout.csv
    --date 2020-07-17

 - To override date and run for a given date
```
java -jar target/centenarian-birthdays-1.0-SNAPSHOT-jar-with-dependencies.jar --citizen data/citizens.csv --opt-out-emails data/optout.csv --date 2020-07-17
```

 - For current data run (Note that depends on dates on CSV)
```
java -jar target/centenarian-birthdays-1.0-SNAPSHOT-jar-with-dependencies.jar --citizen data/citizens.csv --opt-out-emails data/optout.csv
```

 - I have used Intellij as compiler tool, and it can be easily configured:
    
    Run/Debug configurations choose type Application: 
    ```
    Main
    ```
    
    Arguments:
    ```
    --citizen data/citizens.csv --opt-out-emails data/optout.csv --date 2020-07-17
    ```


### Data extract
There is no information about how to get data from. We could use an in memory H2 Database a flat file or a CSV file.
Since there is no description I decided to use this last option.
 
