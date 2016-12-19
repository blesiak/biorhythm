package pl.dominikblesinski.yourenergy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    Button date;
    DatePicker picker;
    static String birthday;
    private static Date today = new Date();
    static Calendar calendar = Calendar.getInstance();


    public static String splitDate(int i){
        String birthSplit[] = birthday.split(Pattern.quote("."));
        return birthSplit[i];
    }

    public static long days(String b) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        if (((today.getTime() - dateFormat.parse(b).getTime())/(1000*60*60*24)) <0){
            return 0;
        } else return ((today.getTime() - dateFormat.parse(b).getTime())/(1000*60*60*24));
    }

    public static int countBirthYear(){
        int w = (Integer.valueOf(calendar.get(Calendar.YEAR))-Integer.valueOf(splitDate(2)));
        if ((Integer.valueOf(splitDate(0)) < Integer.valueOf(calendar.get(Calendar.DAY_OF_MONTH)))
                && (Integer.valueOf(splitDate(1)) >= (Integer.valueOf(calendar.get(Calendar.MONTH))+1)))
        {
            w--;
        }
        if (w<0) return w=0;

        return w;
    }

    public static int leapYear(){
        int yB = Integer.valueOf(splitDate(2));
        int leapYear = 0;
        for (int i = 0; i < countBirthYear()+1; i++){
            if (((yB % 4) == 0 && (yB % 100) != 0) || (yB % 400) == 0) leapYear++;
            yB++;
        }
        return leapYear;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picker = (DatePicker)findViewById(R.id.datePicker);
        date = (Button) findViewById(R.id.picDate);
        date.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                // get the values for day of month , month and year from a date picker
                int day = picker.getDayOfMonth();
                int month = (picker.getMonth() + 1);
                int year = picker.getYear();
                birthday = day+"."+month+"."+year;

                ((TextView)findViewById(R.id.t1)).setText("Your age: "+countBirthYear());
                try {
                    ((TextView)findViewById(R.id.t2)).setText("Lived days: "+days(birthday));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ((Button)findViewById(R.id.chechGraphButton)).setText("Check graph");
                ((Button)findViewById(R.id.chechGraphButton)).setVisibility(View.VISIBLE);
            }
        });

    }

    public void graph(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra("Birthday", birthday);
        startActivity(intent);
    }
}
