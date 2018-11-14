package Pandemic.Exceptions;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

abstract public class PandemicException extends Exception {
    private static List<PandemicException> errors;
    private static FileOutputStream errorLog;
    static {
        try {
            errorLog = new FileOutputStream(new File("errorlog.txt"));
            errors = new ArrayList<>();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected PandemicException(String message){
        super(message);
        log();
        errors.add(this);
    }

    private void log(){
        PrintStream writer = new PrintStream(errorLog);
        writer.println(this.getTime() + "\t" + this.getType() + "\t" + this.getMessage());
    }

    private String getTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    abstract protected String getType();
    abstract public String getHelp();

    public static PandemicException getError(){
        return errors.get(errors.size() - 1);
    }

    public static PandemicException getError(int i){
        if(i >= 0 && i < errors.size())
            return errors.get(i);
        return null;
    }
}
