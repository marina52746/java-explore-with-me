package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
import java.io.PrintWriter;
import java.io.StringWriter;
*/
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        /*
        user
        category
        event
        request
        compilation
         */
/*
        Exception ex = new Exception();
        //StatClient statClient = new StatClient("");
        //statClient.add();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        String error = stringWriter.toString();
        System.out.println(error); */
    }
}
