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
    TextView phyStat, menStat, intStat, descriptionPHY, descriptionMEN, descriptionINT;

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
    public String description (double biorhythm, double con, double trend){

        String DescriptionDisplay = "";
        //PHYSICAL DESCRIPTIONS
        String psyDescription[] = new String[10];
        psyDescription[0] = "Wartości kondycji fizycznej są dodatnie. Dobrze znosisz wysiłek fizyczny i jesteś odporny na choroby i przeciążenia.";
        psyDescription[1] = "Wartości kondycji fizycznej są wysokie. To dobry moment by stawiać sobie wymagające cele i pobijać rekordy. ";
        psyDescription[2] = "Twoja kondycja fizyczna jest na najwyższym poziomie. Osiągaj wysoko postawione cele i pobijaj rekordy. Organizm jest najmniej narażony na przeciążenia i choroby.";
        psyDescription[3] = "Wartości kondycji fizycznej są dodatnie i dobrze znosisz wysiłek fizyczny.";
        psyDescription[4] = "Wartości kondycji fizycznej są coraz niższe. Wykorzystaj jej dodatnie wartości na intensywną pracę nad sobą.";
        psyDescription[5] = "Wartości kondycji fizycznej przyjmują wartości ujemne. Unikaj przeciążeń i dbaj o swoją odporność. ";
        psyDescription[6] = "Wskaźnik kondycji fizycznej osiąga minimum potencjału. Nie stawiaj sobie w tej chwili zbyt wymagających celów do osiągnięcia, aczkolwiek nie rezygnuj z aktywności fizycznej. Dbaj o swoją odporność organizmu.";
        psyDescription[7] = "Wartości kondycji fizycznej mają ujemne wartości, lecz zwiększają się twoje predyspozycje do większych obciążeń.";
        psyDescription[8] = "Wartości kondycji fizycznej zaczęły przyjmować wartości dodatnie.";
        psyDescription[9] = "Wartości kondycji fizycznej zaczęły przyjmować wartości ujemne.";

        //MENTAL DESCRIPTIONS
        String menDescription[] = new String[10];
        menDescription[0] = "Wskaźnik emocjonalny jest dodatni. Zwiększa się Twoja odporność na stres i umiejętność kontrolowania uczuć.";
        menDescription[1] = "Twoja odporność na stres i umiejętność kontrolowania uczuć są na dużym poziomie. Wykorzystaj to do rozwiązywania problemów.";
        menDescription[2] = "Osiągasz najwyższe wartości kondycji emocjonalnej. Masz bardzo małą podatność na stres. Jest to najlepszy moment do rozwiązywania problemów uczuciowych.";
        menDescription[3] = "Mimo, że wartości kondycji emocjonalnej maleją to wciąż są na wysokim poziomie i nadal wskazuje to na Twoją dobrą odporność na stres.";
        menDescription[4] = "Wskaźnik emocjonalny maleje. Wykorzystaj jej dodatnie wartości i nie odkładaj trudnych spraw do rozwiązania na później.";
        menDescription[5] = "Wskaźnik emocjonalny przyjmuje wartości ujemne. Unikaj trudnych i stresogennych sytuacji. ";
        menDescription[6] = "Wskaźnik emocjonalny osiąga minimum potencjału. Powinieneś unikać trudnych sytuacji i rozwiązywania problemów uczuciowych. Lepiej odłożyć je na później, gdyż niebawem kondycja emocjonalna będzie wzrastać.";
        menDescription[7] = "Wskaźnik emocjonalny ma ujemne wartości, aczkolwiek z dnia na dzień są one coraz większe. Nastawiaj się na zwiększenie odporności na stresogenne sytuacje.";
        menDescription[8] = "Wskaźnik emocjonalny zaczął przyjmować wartości dodatnie.";
        menDescription[9] = "Wskaźnik emocjonalny przyjmuje wartości ujemne.";

        //INTELLIGENCE DESCRIPTIONS
        String intDescription[] = new String[10];
        intDescription[0] = "Twoja kondycja intelektualna jest dodatnia. Przygotuj się do podejmowania ważnych decyzji i zdobywania osiągnięć naukowych.";
        intDescription[1] = "Jest to dobry moment aby zacząć podejmować ważne decyzje i zdobywać osiągnięcia naukowe. Wykorzystaj to.";
        intDescription[2] = "Osiągasz najwyższe wartości swojej kondycji intelektualnej. Jest to najlepszy moment, aby podejmować ważne decyzje i zdobywać osiągnięcia naukowe.";
        intDescription[3] = "Nadal jest to dobra chwila by podejmować decyzje i zdobywać osiągnięcia naukowe, i powinieneś to wykorzystać.";
        intDescription[4] = "Twoja kondycja intelektualna maleje. Wykorzystaj jej pozytywny rytm, aczkolwiek nie stawiaj sobie zbyt dużych wymagań.";
        intDescription[5] = "Twoja kondycja intelektualna ma ujemne wartości. Unikaj skomplikowanych prac umysłowych.";
        intDescription[6] = "Twoja kondycja intelektualna osiąga minimum potencjału. Powinieneś unikać prac umysłowych i podejmowania ważnych decyzji. Lecz niebawem biorytm intelektualny zacznie mieć tendencję wzrostową.";
        intDescription[7] = "Twoja kondycja intelektualna ma ujemne wartości lecz zmierza w dobrym kierunku. Nastaw się na osiąganie nowych celów i rozwiązywanie problemów.";
        intDescription[8] = "Twoja kondycja intelektualna nareszcie przyjmuje wartości dodatnie.";
        intDescription[9] = "Twoja kondycja intelektualna niestety przyjmuje wartości ujemne.";

        //PHYSICAL CONDITION DESCRIPTION
        if (biorhythm == 23.0) {
            if (con >= 85) {
                DescriptionDisplay = psyDescription[2];
            } else if (con <= -85) {
                DescriptionDisplay = psyDescription[6];
            } else if (con < trend) {
                //rising branch
                if (con > -85 && con < 0) {
                    DescriptionDisplay = psyDescription[7];
                } else if (con >= 0 && con < 5) {
                    DescriptionDisplay = psyDescription[8];
                } else if (con >= 5 && con < 60) {
                    DescriptionDisplay = psyDescription[0];
                } else if (con >= 60 && con < 85) {
                    DescriptionDisplay = psyDescription[1];
                }
            } else if (con > trend) {
                //decreasing branch
                if (con < 85 && con > 60) {
                    DescriptionDisplay = psyDescription[3];
                } else if (con <= 60 && con > 0) {
                    DescriptionDisplay = psyDescription[4];
                } else if (con <= 0 && con > -5) {
                    DescriptionDisplay = psyDescription[9];
                } else if (con < -5 && con > -85) {
                    DescriptionDisplay = psyDescription[5];
                }
            }
        }
        //MENTAL CONDITION DESCRIPTION
        else if (biorhythm == 28.0){
            if (con >= 85) {
                DescriptionDisplay = menDescription[2];
            } else if (con <= -85) {
                DescriptionDisplay = menDescription[6];
            } else if (con < trend) {
                //rising branch
                if (con > -85 && con < 0) {
                    DescriptionDisplay = menDescription[7];
                } else if (con >= 0 && con < 5) {
                    DescriptionDisplay = menDescription[8];
                } else if (con >= 5 && con < 60) {
                    DescriptionDisplay = menDescription[0];
                } else if (con >= 60 && con < 85) {
                    DescriptionDisplay = menDescription[1];
                }
            } else if (con > trend) {
                //decreasing branch
                if (con < 85 && con > 60) {
                    DescriptionDisplay = menDescription[3];
                } else if (con <= 60 && con > 0) {
                    DescriptionDisplay = menDescription[4];
                } else if (con <= 0 && con > -5) {
                    DescriptionDisplay = menDescription[9];
                } else if (con < -5 && con > -85) {
                    DescriptionDisplay = menDescription[5];
                }
            }
        }
        //INTELLIGENCE CONDITION DESCRIPTION
        else if (biorhythm == 33.0){
            if (con >= 85) {
                DescriptionDisplay = intDescription[2];
            } else if (con <= -85) {
                DescriptionDisplay = intDescription[6];
            } else if (con < trend) {
                //rising branch
                if (con > -85 && con < 0) {
                    DescriptionDisplay = intDescription[7];
                } else if (con >= 0 && con < 5) {
                    DescriptionDisplay = intDescription[8];
                } else if (con >= 5 && con < 60) {
                    DescriptionDisplay = intDescription[0];
                } else if (con >= 60 && con < 85) {
                    DescriptionDisplay = intDescription[1];
                }
            } else if (con > trend) {
                //decreasing branch
                if (con < 85 && con > 60) {
                    DescriptionDisplay = intDescription[3];
                } else if (con <= 60 && con > 0) {
                    DescriptionDisplay = intDescription[4];
                } else if (con <= 0 && con > -5) {
                    DescriptionDisplay = intDescription[9];
                } else if (con < -5 && con > -85) {
                    DescriptionDisplay = intDescription[5];
                }
            }
        }

        return ""+DescriptionDisplay;
    }
    public void graphView(int day, Date D[], GraphView graph, int range, double biorhythm, String title, int color, TextView stat, TextView description){

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

        description.setText(description(biorhythm, (CON[day*range])*100, (CON[(day+1)*range])*100));

        if (CON[day*range]>=0) {
            stat.setTextColor(Color.GREEN);
            stat.setText("+"+String.format("%.0f", CON[day * range] * 100)+"%");
        } else if (CON[day*range]<0){
            stat.setTextColor(Color.RED);
            stat.setText(String.format("%.0f", CON[day * range] * 100)+"%");
        }
        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        birthday = getIntent().getStringExtra("Birthday");
        todayString = dateFormat.format(todayDate.getTime());
        ((TextView) findViewById(R.id.todayDateTextView)).setText(headingFormat.format(todayDate.getTime()));
        phyStat = (TextView)findViewById(R.id.phyStat);
        menStat = (TextView)findViewById(R.id.menStat);
        intStat = (TextView)findViewById(R.id.intStat);
        descriptionPHY = (TextView)findViewById(R.id.descriptionPHY);
        descriptionMEN = (TextView)findViewById(R.id.descriptionMEN);
        descriptionINT = (TextView)findViewById(R.id.descriptionINT);
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
        graphView(day, D, graph, range, 23.0, "Physical condition", Color.RED, phyStat, descriptionPHY);

        //MENTAL CONDITION (28 days)
        graphView(day, D, graph, range, 28.0, "Mental condition", Color.GREEN, menStat, descriptionMEN);

        //INTELLIGENCE CONDITION (33 days)
        graphView(day, D, graph, range, 33.0, "Intelligence condition", Color.BLUE, intStat, descriptionINT);

        //DESCRIPTION


        //GRAPH FORMAT
        graphFormat (dayFormat, graph, d2, d3);

    }
}