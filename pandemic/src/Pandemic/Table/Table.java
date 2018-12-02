package Pandemic.Table;

import Pandemic.Cards.Card;
import Pandemic.Cards.CityCard;
import Pandemic.Core.Virus;
import javafx.geometry.Point2D;

import java.io.Serializable;
import java.util.*;

public class Table  implements Serializable {
    private HashMap<String, Field> fields = new HashMap<>(96);

    public Table(){
        createMap();
    }

    public Field getField(String name){
        return fields.get(name);
    }

    /**
     * Initialization of the board
     */
    private void createMap(){
        Field atlanta = createField("Atlanta", Virus.BLUE,  5.9, 8.1);
        Field chicago = createField("Chicago", Virus.BLUE, 5.1, 6.6);
        Field montreal = createField("Montreal", Virus.BLUE,  7.2, 6.4);
        Field washington = createField("Washington", Virus.BLUE, 8, 8);
        Field newyork = createField("New York", Virus.BLUE,  8.8, 6.7);
        Field sanFr = createField("San Fransisco", Virus.BLUE, 2.5, 7.3);
        Field london = createField("London", Virus.BLUE, 12.5, 5.5);
        Field madrid = createField("Madrid", Virus.BLUE, 12.2, 7.6);
        Field paris = createField("Paris", Virus.BLUE, 14, 6.5);
        Field stPet = createField("St. Petersburg", Virus.BLUE, 16.9, 4.7);
        Field essen = createField("Essen", Virus.BLUE, 14.6, 5.1);
        Field milan = createField("Milan", Virus.BLUE, 15.4, 6.1);


        Field miami = createField("Miami", Virus.YELLOW, 7.2, 9.8);
        Field la = createField("Los Angeles", Virus.YELLOW, 2.9, 9.3);
        Field mexico = createField("Mexico City", Virus.YELLOW, 4.8, 10);
        Field bogota = createField("Bogota", Virus.YELLOW, 7, 11.8);
        Field lima = createField("Lima", Virus.YELLOW, 6.3, 13.9);
        Field santiago = createField("Santiago", Virus.YELLOW, 6.5, 16.2);
        Field saopaolo = createField("Sao Paolo", Virus.YELLOW, 9.9, 14.2);
        Field buenos = createField("Buenos Aires", Virus.YELLOW, 8.7, 15.9);
        Field lagos = createField("Lagos", Virus.YELLOW, 13.9, 11.4);
        Field kinshasha = createField("Kinshahsa", Virus.YELLOW, 15.2, 12.9);
        Field katum = createField("Kartum", Virus.YELLOW, 16.5, 11);
        Field jburg = createField("Johannesburg", Virus.YELLOW, 16.2, 14.9);


        Field algir = createField("Algires", Virus.BLACK, 14.5, 8.6);
        Field istambul = createField("Istambul", Virus.BLACK, 16.2, 7.3);
        Field moskva = createField("Moscow", Virus.BLACK, 18, 6);
        Field teheran = createField("Tehran", Virus.BLACK, 19.3, 6.9);
        Field cairo = createField("Cairo", Virus.BLACK, 16, 9);
        Field ryadh = createField("Riyadh", Virus.BLACK, 18, 10.2);
        Field karachi = createField("Karachi", Virus.BLACK, 19.7, 9);
        Field mumbai = createField("Mumbai", Virus.BLACK, 19.9, 10.5);
        Field chennai = createField("Chennai", Virus.BLACK, 21.55, 11.6);
        Field kalkuta = createField("Kolkata", Virus.BLACK, 22.6, 9);
        Field delhi = createField("Delhi", Virus.BLACK, 21.25, 8.5);
        Field baghdad = createField("Baghdad", Virus.BLACK, 17.8, 8.4);


        Field bejing = createField("Bejing", Virus.RED, 23.8, 6.6);
        Field shanghai = createField("Shanghai", Virus.RED, 24, 8);
        Field hongkong = createField("Hong Kong", Virus.RED, 24.1, 9.8);
        Field bangkok = createField("Bangkok", Virus.RED, 23, 10.8);
        Field seoul = createField("Seoul", Virus.RED, 25.6, 6.5);
        Field osaka = createField("Osaka", Virus.RED, 27.2, 8.9);
        Field tokyo = createField("Tokyo", Virus.RED, 27.1, 7.3);
        Field taipei = createField("Taipei", Virus.RED, 25.8, 9.5);
        Field manila = createField("Manila", Virus.RED, 26.2, 12);
        Field hochimin = createField("Ho Chi Minh City", Virus.RED, 24.3, 12.1);
        Field jakarta = createField("Jakarta", Virus.RED, 23, 13.4);
        Field sidney = createField("Sidney", Virus.RED, 27.4, 16.2);

        atlanta.connect(washington).connect(chicago).connect(miami);
        washington.connect(montreal).connect(newyork);
        chicago.connect(montreal).connect(sanFr).connect(la).connect(mexico);
        montreal.connect(newyork);
        newyork.connect(london).connect(madrid);
        sanFr.connect(tokyo).connect(manila);
        london.connect(madrid).connect(paris).connect(essen);
        madrid.connect(paris).connect(algir);
        paris.connect(essen).connect(milan).connect(algir);
        essen.connect(milan).connect(stPet);
        milan.connect(stPet).connect(istambul);
        stPet.connect(moskva).connect(istambul);

        la.connect(mexico).connect(sidney);
        mexico.connect(miami).connect(bogota).connect(lima);
        miami.connect(bogota);
        bogota.connect(lima).connect(saopaolo).connect(buenos);
        lima.connect(santiago);
        saopaolo.connect(buenos).connect(lagos);
        lagos.connect(katum).connect(kinshasha);
        kinshasha.connect(katum).connect(jburg);
        katum.connect(jburg).connect(cairo);

        algir.connect(istambul).connect(cairo);
        istambul.connect(cairo).connect(moskva).connect(baghdad);
        cairo.connect(baghdad).connect(ryadh);
        moskva.connect(teheran);
        baghdad.connect(karachi).connect(teheran).connect(ryadh);
        ryadh.connect(karachi);
        teheran.connect(karachi).connect(delhi);
        karachi.connect(delhi).connect(mumbai);
        delhi.connect(kalkuta).connect(chennai).connect(mumbai);
        mumbai.connect(chennai);
        chennai.connect(kalkuta).connect(jakarta).connect(bangkok);
        kalkuta.connect(bangkok).connect(hongkong);

        sidney.connect(jakarta).connect(manila);
        jakarta.connect(hochimin).connect(bangkok);
        manila.connect(taipei).connect(hochimin).connect(hongkong);
        hochimin.connect(bangkok).connect(hongkong);
        bangkok.connect(hongkong);
        hongkong.connect(taipei).connect(shanghai);
        taipei.connect(osaka).connect(shanghai);
        shanghai.connect(bejing).connect(seoul).connect(tokyo);
        osaka.connect(tokyo);
        tokyo.connect(seoul);
        seoul.connect(bejing);
    }

    private Field createField(String name, Virus color, double x, double y){
        Field f = new Field(name, color, new Field.Coordinates(x - 1, y - 3.2));
        fields.put(name, f);
        return f;
    }

    /**
     * Returns a collection of cityCards for every field on the board
     */
    public List<Card> getCityCards(){
        List<Card> cards = new ArrayList<>();
        for (Field f: fields.values()) {
            cards.add(new CityCard(f, 0));
        }
        return cards;
    }


    public HashMap<String, Field> getFields() {
        return fields;
    }
}
