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