package Pandemic.Table;

import Pandemic.Cards.Card;
import Pandemic.Cards.CityCard;
import Pandemic.Virus;

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

    private void createMap(){
        Field atlanta = createField("Atlanta", Virus.BLUE);
        Field chicago = createField("Chicago", Virus.BLUE);
        Field montreal = createField("Montreal", Virus.BLUE);
        Field washington = createField("Washington", Virus.BLUE);
        Field newyork = createField("New York", Virus.BLUE);
        Field sanFr = createField("San Fransisco", Virus.BLUE);
        Field london = createField("London", Virus.BLUE);
        Field madrid = createField("Madrid", Virus.BLUE);
        Field paris = createField("Paris", Virus.BLUE);
        Field stPet = createField("St. Petersburg", Virus.BLUE);
        Field essen = createField("Essen", Virus.BLUE);
        Field milan = createField("Milan", Virus.BLUE);


        Field miami = createField("Miami", Virus.YELLOW);
        Field la = createField("Los Angeles", Virus.YELLOW);
        Field mexico = createField("Mexico City", Virus.YELLOW);
        Field bogota = createField("Bogota", Virus.YELLOW);
        Field lima = createField("Lima", Virus.YELLOW);
        Field santiago = createField("Santiago", Virus.YELLOW);
        Field saopaolo = createField("Sao Paolo", Virus.YELLOW);
        Field buenos = createField("Buenos Aires", Virus.YELLOW);
        Field lagos = createField("Lagos", Virus.YELLOW);
        Field kinshasha = createField("Kinshahsa", Virus.YELLOW);
        Field katum = createField("Kartum", Virus.YELLOW);
        Field jburg = createField("Johannesburg", Virus.YELLOW);


        Field algir = createField("Algires", Virus.BLACK);
        Field istambul = createField("Istambul", Virus.BLACK);
        Field moskva = createField("Moscow", Virus.BLACK);
        Field teheran = createField("Tehran", Virus.BLACK);
        Field cairo = createField("Cairo", Virus.BLACK);
        Field ryadh = createField("Riyadh", Virus.BLACK);
        Field karachi = createField("Karachi", Virus.BLACK);
        Field mumbai = createField("Mumbai", Virus.BLACK);
        Field chennai = createField("Chennai", Virus.BLACK);
        Field kalkuta = createField("Kolkata", Virus.BLACK);
        Field delhi = createField("Delhi", Virus.BLACK);
        Field baghdad = createField("Baghdad", Virus.BLACK);


        Field bejing = createField("Bejing", Virus.RED);
        Field shanghai = createField("Shanghai", Virus.RED);
        Field hongkong = createField("Hong Kong", Virus.RED);
        Field bangkok = createField("Bangkok", Virus.RED);
        Field seoul = createField("Seoul", Virus.RED);
        Field osaka = createField("Osaka", Virus.RED);
        Field tokyo = createField("Tokyo", Virus.RED);
        Field taipei = createField("Taipei", Virus.RED);
        Field manila = createField("Manila", Virus.RED);
        Field hochimin = createField("Ho Chi Minh City", Virus.RED);
        Field jakarta = createField("Jakarta", Virus.RED);
        Field sidney = createField("Sidney", Virus.RED);

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
        miami.connect(bogota).connect(lima);
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

    private Field createField(String name, Virus color){
        Field f = new Field(name, color);
        fields.put(name, f);
        return f;
    }

    public List<Card> getCityCards(){
        List<Card> cards = new ArrayList<>();
        for (Field f: fields.values()) {
            cards.add(new CityCard(f, 0)); //BUG: population
        }
        return cards;
    }


    public HashMap<String, Field> getFields() {
        return fields;
    }
}
