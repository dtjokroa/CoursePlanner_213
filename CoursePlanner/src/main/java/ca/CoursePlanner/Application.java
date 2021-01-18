package ca.CoursePlanner;

import ca.CoursePlanner.Logic.CsvReader;
import ca.CoursePlanner.Logic.Entry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileReader;
import java.util.ArrayList;

@SpringBootApplication
public class Application {
    public static void main(String[] args){

       SpringApplication.run(Application.class, args);
    }
}