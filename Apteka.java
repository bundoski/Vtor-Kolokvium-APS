/* 
Потребно е да се направи компјутерска апликација со која ќе се забрза работењето на една аптека. Притоа апликацијата треба да му овозможи на 
корисникот (фармацевтот) брзо да пребарува низ огромното множество со лекови кои се внесени во системот. Начинот на кој тој треба да пребарува е 
следен: доволно е да ги внесе првите 3 букви од името на лекот за да може да му се излиста листа од лекови кои ги има во системот. Работата на фармацевтот е 
да провери дали внесениот лек го има во системот и да му даде информација на клиентот. Информацијата што треба да му ја даде на клиентот е дали лекот се наоѓа
на позитивната листа на лекови, која е цената и колку парчиња од лекот има на залиха. Доколку лекот постои клиентот го нарачува со што кажува колку парчиња ќе 
kупи. Оваа акција фармацевтот треба да ја евидентира на системот (односно да ја намали залихата на лекови за онолку парчиња колку што му издал на клиентот). 
Доколку нарачката на клиентот е поголема од залихата на лекот што ја има во системот, не се презема никаква акција.

Влез: Од стандарден влез прво се дава број N кој претставува број на лекови кои ќе бидат внесени во системот. Во наредните N реда се дадени имињата на 
лековите, дали ги има на позитивната листа (1/0), цената и број на парчиња, сите разделени со по едно празно место. Потоа се даваат редови со имиња на 
лекови и број на парчиња нарачани од клиентот. За означување на крај се дава зборот KRAJ.

Излез: На стандарден излез треба да се испечати за секој од влезовите следната информација: IME POZ/NEG CENA BR_LEKOVI. Доколку лекот не е најден се печати 
Nema takov lek. Доколку нарачката на клиентот е поголема од залихата се печати Nema dovolno lekovi инаку Napravena naracka.

Забелешка: Задачата да се реши со хeш табела. Функцијата со која се врши мапирање на имињата на лековите во број е 
следна: h(w)=(29∗(29∗(29∗0+ASCII(c1))+ASCII(c2))+ASCII(c3))%102780 каде зборот w=c1c2c3c4c5…. е составен од сите големи букви.

Исто така за лековите да се направи посебна класа која како атрибути ќе ги има наведените карактеристики на лекот во системот.

Име на класата: Apteka.

*/



package Lekovi;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;

class MapEntry<K extends Comparable<K>, E> implements Comparable<K> {

    // Each MapEntry object is a pair consisting of a key (a Comparable
    // object) and a value (an arbitrary object).
    K key;
    E value;

    public MapEntry(K key, E val) {
        this.key = key;
        this.value = val;
    }

    public int compareTo(K that) {
        // Compare this map entry to that map entry.
        @SuppressWarnings("unchecked")
        MapEntry<K, E> other = (MapEntry<K, E>) that;
        return this.key.compareTo(other.key);
    }

    public String toString() {
        return "<" + key + "," + value + ">";
    }
}

class OBHT<K extends Comparable<K>, E> {

    // An object of class OBHT is an open-bucket hash table, containing entries
    // of class MapEntry.
    private MapEntry<K, E>[] buckets;

    // buckets[b] is null if bucket b has never been occupied.
    // buckets[b] is former if bucket b is formerly-occupied
    // by an entry that has since been deleted (and not yet replaced).

    static final int NONE = -1; // ... distinct from any bucket index.

    private static final MapEntry former = new MapEntry(null, null);
    // This guarantees that, for any genuine entry e,
    // e.key.equals(former.key) returns false.

    private int occupancy = 0;
    // ... number of occupied or formerly-occupied buckets in this OBHT.

    @SuppressWarnings("unchecked")
    public OBHT(int m) {
        // Construct an empty OBHT with m buckets.
        buckets = (MapEntry<K, E>[]) new MapEntry[m];
    }


    private int hash(K key) {
        // Translate key to an index of the array buckets.
        return Math.abs(key.hashCode()) % buckets.length;
    }

    public MapEntry getBucket(int key) {
        System.out.println("THIS IS THE VALUE!!!!" + buckets[key]);
        return buckets[key];
    }


    public int search(K targetKey) {
        // Find which if any bucket of this OBHT is occupied by an entry whose key
        // is equal to targetKey. Return the index of that bucket.
        int b = hash(targetKey);
        int n_search = 0;
        for (; ; ) {
            MapEntry<K, E> oldEntry = buckets[b];
            if (oldEntry == null)
                return NONE;
            else if (targetKey.equals(oldEntry.key))
                return b;
            else {
                b = (b + 1) % buckets.length;
                n_search++;
                if (n_search == buckets.length)
                    return NONE;

            }
        }
    }


    public void insert(K key, E val) {
        // Insert the entry <key, val> into this OBHT.
        MapEntry<K, E> newEntry = new MapEntry<K, E>(key, val);
        int b = hash(key);
        int n_search = 0;
        for (; ; ) {
            MapEntry<K, E> oldEntry = buckets[b];
            if (oldEntry == null) {
                if (++occupancy == buckets.length) {
                    System.out.println("Hash tabelata e polna!!!");
                }
                buckets[b] = newEntry;
                return;
            } else if (oldEntry == former
                    || key.equals(oldEntry.key)) {
                buckets[b] = newEntry;
                return;
            } else {
                b = (b + 1) % buckets.length;
                n_search++;
                if (n_search == buckets.length)
                    return;

            }
        }
    }


    @SuppressWarnings("unchecked")
    public void delete(K key) {
        // Delete the entry (if any) whose key is equal to key from this OBHT.
        int b = hash(key);
        int n_search = 0;
        for (; ; ) {
            MapEntry<K, E> oldEntry = buckets[b];

            if (oldEntry == null)
                return;
            else if (key.equals(oldEntry.key)) {
                buckets[b] = former;//(MapEntry<K,E>)former;
                return;
            } else {
                b = (b + 1) % buckets.length;
                n_search++;
                if (n_search == buckets.length)
                    return;

            }
        }
    }


    public String toString() {
        String temp = "";
        for (int i = 0; i < buckets.length; i++) {
            temp += i + ":";
            if (buckets[i] == null)
                temp += "\n";
            else if (buckets[i] == former)
                temp += "former\n";
            else
                temp += buckets[i] + "\n";
        }
        return temp;
    }


    public OBHT<K, E> clone() {
        OBHT<K, E> copy = new OBHT<K, E>(buckets.length);
        for (int i = 0; i < buckets.length; i++) {
            MapEntry<K, E> e = buckets[i];
            if (e != null && e != former)
                copy.buckets[i] = new MapEntry<K, E>(e.key, e.value);
            else
                copy.buckets[i] = e;
        }
        return copy;
    }
}

class Lek {

    String ime;
    int negativna;
    int cena;
    int brojLekovi;

    public Lek(String ime, int negativna, int cena, int brojLekovi) {
        this.ime = ime;
        this.negativna = negativna;
        this.cena = cena;
        this.brojLekovi = brojLekovi;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public int getNegativna() {
        return negativna;
    }

    public void setNegativna(int negativna) {
        this.negativna = negativna;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    public int getBrojLekovi() {
        return brojLekovi;
    }

    public void setBrojLekovi(int brojLekovi) {
        this.brojLekovi = brojLekovi;
    }
}


public class Apteka {

    public static String makeUpperCase(String in){

        String maker = "";
        for(int i = 0;i<in.length();i++){
            maker+=Character.toUpperCase(in.charAt(i));
        }
        return maker;
    }

    public static void main(String[] args) throws IOException {

    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    int n = Integer.parseInt(in.readLine());

    OBHT<String, Lek> hashtable = new OBHT<>(n*n);
    // filling the hash table up . . .
    for(int i = 0; i<n;i++){

        String line = in.readLine();
        String [] pom = line.split(" ");
        Lek lek = new Lek(pom[0], Integer.parseInt(pom[1]), Integer.parseInt(pom[2]),
                Integer.parseInt(pom[3]));
        hashtable.insert(pom[0], lek);
    }

    while(true){

        String porackaIme = in.readLine();
        if(porackaIme.equals("KRAJ")){
            break;
        }

        int brojLekovi = Integer.parseInt(in.readLine());
        porackaIme = makeUpperCase(porackaIme);
        int key = hashtable.search(porackaIme);
        if(key == -1){
            System.out.println("Nema takov lek!");
            continue;
        }
        else {
            Lek lek = (Lek) hashtable.getBucket(key).value;
            System.out.println(lek.ime);
            if(lek.negativna == 0){
                System.out.println("NEG");
            }
            else{
                System.out.println("POZ");
            }
            System.out.println(lek.cena);
            System.out.println(lek.brojLekovi);
            if(brojLekovi <= lek.brojLekovi){
                System.out.println("Napravena naracka");
                lek.brojLekovi = lek.brojLekovi - brojLekovi;
                hashtable.insert(lek.ime, lek);
            }
            else {
                System.out.println("Nema dovolno lekovi");
            }
        }
    }

    }
}
