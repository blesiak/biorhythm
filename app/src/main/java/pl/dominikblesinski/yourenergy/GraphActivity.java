package pl.dominikblesinski.yourenergy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class GraphActivity extends AppCompatActivity {

    String birthday;
    private static Date todayDate = new Date();
    String todayString;
    static Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat headingFormat = new SimpleDateFormat("dd MMMM yyyy");
    SimpleDateFormat dayFormat = new SimpleDateFormat("d");

    public String splitDate(String b, int i){
        String birthSplit[] = b.split(Pattern.quote("."));
        return birthSplit[i];
    }

    public static long days(String b) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        if (((todayDate.getTime() - dateFormat.parse(b).getTime())/(1000*60*60*24)) <0){
            return 0;
        } else return ((todayDate.getTime() - dateFormat.parse(b).getTime())/(1000*60*60*24));
    }

    public void graphFormat (SimpleDateFormat day, GraphView graph, Date Min, Date Max) {
        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext(), day));
        graph.getGridLabelRenderer().setNumHorizontalLabels(11); // range of visible days + 2 graphic labels
        graph.getGridLabelRenderer().setNumVerticalLabels(3);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);

        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-1);
        graph.getViewport().setMaxY(1);

        // set manual x bounds to have nice steps
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(Min.getTime());
        graph.getViewport().setMaxX(Max.getTime());

        // enable scaling and scrolling
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(false);
        graph.getViewport().setScalableY(false);

        //legend
        graph.getLegendRenderer().setVisible(false);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setSpacing(10);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
    }

    public void todayBar (Date d, GraphView graph){
        //Today Bar
        BarGraphSeries<DataPoint> todayBar = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(d, 1),
                new DataPoint(d, -1)
        });
        todayBar.setSpacing(100);
        todayBar.setColor(Color.LTGRAY);
        todayBar.setAnimated(true);
        todayBar.setTitle("TODAY");
        graph.addSeries(todayBar);
    }

    public void graphView(int day, Date D[], GraphView graph, int range, double biorhythm, String title, int color){

        double CON[] = new double[(day+16)*range];
        for (int i = 0; i <CON.length; i++)
        {
            CON[i] = Math.sin((i * Math.PI) / biorhythm*2/range );
        }
        DataPoint[] pointsCON = new DataPoint[31 * range];
        for (int i = 0; i < pointsCON.length; i++) {
            pointsCON[i] = new DataPoint(D[(day-15)*range+i], CON[(day-15)*range+i]);
        }
        LineGraphSeries<DataPoint> seriesCON = new LineGraphSeries<>(pointsCON);
        graph.addSeries(seriesCON);
        seriesCON.setTitle(title);
        seriesCON.setColor(color);
        seriesCON.setAnimated(true);
        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        birthday = getIntent().getStringExtra("Birthday");
        todayString = dateFormat.format(todayDate.getTime());
        ((TextView) findViewById(R.id.todayDateTextView)).setText(headingFormat.format(todayDate.getTime()));

        //days to int
        int day = 0;
        try {
            day = (int) days(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        GraphView graph = (GraphView) findViewById(R.id.graph);

        //view graph range
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        Date dToday = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -4);
        Date d2 = calendar.getTime();
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 6);
        Date d3 = calendar.getTime();

        //Date Range (24(h day) must be divisible by range (required 2 or 4))
        int range = 4;

        Date D[] = new Date[(day+16)*range];
        calendar.set(Integer.valueOf(splitDate(birthday, 2)), Integer.valueOf(splitDate(birthday, 1))-1, Integer.valueOf(splitDate(birthday, 0)));
        for (int i = 0; i < D.length; i++) {
            D[i] = calendar.getTime();
            calendar.add(Calendar.HOUR_OF_DAY, 24/range);
        }

        //Today Bar
        todayBar(dToday, graph);

        //PHYSICAL CONDITION (23 days)
        graphView(day, D, graph, range, 23.0, "Physical condition", Color.RED);

        //MENTAL CONDITION (28 days)
        graphView(day, D, graph, range, 28.0, "Mental condition", Color.GREEN);

        //INTELLIGENCE CONDITION (33 days)
        graphView(day, D, graph, range, 33.0, "Intelligence condition", Color.BLUE);

        //GRAPH FORMAT
        graphFormat (dayFormat, graph, d2, d3);
    }
}