/*
Треба да изработите автоматски преведувач за зборови од анлиски јазик на македонски. Влезот се состои од парови од зборови разделени со 
празно место. Така прво е даден зборот на македонски, па има едно празно место, па преводот на зборот на англиски јазик Потоа на влез се 
добиваат странски зборови (секој во посебен ред). За излез треба да се преведат овие зборови. Доколку не е познат преводот на зборот на излез се печати "/"

Влез. Прво се дава број N на поими кои ќе ги содржи речникот. Потоа во наредните N реда се дадени поимите, прв на македонски, потоа на англиски. 
Потоа следуваат зборови на англиски (секој збор во посебен ред), кои треба да се преведат на македонски. За означување на крај во редицата се дава зборот KRAJ

Излез. За секој од дадените зборови на англиски во посебен ред е даден преводот на зборот на македонски. Доколку не е познат преводот на зборот 
се печати /

Забелешка. Работете со хеш табела со отворени кофички. Сами треба да го одредите бројот на кофички и хеш функцијата.

Sample input
5
machka cat
kuche dog
prase pig
papagal parrot
riba fish
cat
parrot
fish
KRAJ


Sample output
machka
papagal
riba

Име на класа: Preveduvac

*/


package Preveduvacc;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


class OBHT<K extends Comparable<K>,E> {

    // An object of class OBHT is an open-bucket hash table, containing entries
    // of class MapEntry.
    private MapEntry<K,E>[] buckets;

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
    public OBHT (int m) {
        // Construct an empty OBHT with m buckets.
        buckets = (MapEntry<K,E>[]) new MapEntry[m];
    }


    private int hash (K key) {
        // Translate key to an index of the array buckets.
        return Math.abs(key.hashCode()) % buckets.length;
    }

    public E getValue(int key){
        return buckets[key].value;
    }

    public int search (K targetKey) {
        // Find which if any bucket of this OBHT is occupied by an entry whose key
        // is equal to targetKey. Return the index of that bucket.
        int b = hash(targetKey); int n_search=0;
        for (;;) {
            MapEntry<K,E> oldEntry = buckets[b];
            if (oldEntry == null)
                return NONE;
            else if (targetKey.equals(oldEntry.key))
                return b;
            else
            {
                b = (b + 1) % buckets.length;
                n_search++;
                if(n_search==buckets.length)
                    return NONE;

            }
        }
    }


    public void insert (K key, E val) {
        // Insert the entry <key, val> into this OBHT.
        MapEntry<K,E> newEntry = new MapEntry<K,E>(key, val);
        int b = hash(key); int n_search=0;
        for (;;) {
            MapEntry<K,E> oldEntry = buckets[b];
            if (oldEntry == null) {
                if (++occupancy == buckets.length) {
                    System.out.println("Hash tabelata e polna!!!");
                }
                buckets[b] = newEntry;
                return;
            }
            else if (oldEntry == former
                    || key.equals(oldEntry.key)) {
                buckets[b] = newEntry;
                return;
            }
            else
            {
                b = (b + 1) % buckets.length;
                n_search++;
                if(n_search==buckets.length)
                    return;

            }
        }
    }


    @SuppressWarnings("unchecked")
    public void delete (K key) {
        // Delete the entry (if any) whose key is equal to key from this OBHT.
        int b = hash(key); int n_search=0;
        for (;;) {
            MapEntry<K,E> oldEntry = buckets[b];

            if (oldEntry == null)
                return;
            else if (key.equals(oldEntry.key)) {
                buckets[b] = former;//(MapEntry<K,E>)former;
                return;
            }
            else{
                b = (b + 1) % buckets.length;
                n_search++;
                if(n_search==buckets.length)
                    return;

            }
        }
    }


    public String toString () {
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


    public OBHT<K,E> clone () {
        OBHT<K,E> copy = new OBHT<K,E>(buckets.length);
        for (int i = 0; i < buckets.length; i++) {
            MapEntry<K,E> e = buckets[i];
            if (e != null && e != former)
                copy.buckets[i] = new MapEntry<K,E>(e.key, e.value);
            else
                copy.buckets[i] = e;
        }
        return copy;
    }
}

class MapEntry<K extends Comparable<K>,E> implements Comparable<K> {

    // Each MapEntry object is a pair consisting of a key (a Comparable
    // object) and a value (an arbitrary object).
    K key;
    E value;

    public MapEntry (K key, E val) {
        this.key = key;
        this.value = val;
    }

    public int compareTo (K that) {
        // Compare this map entry to that map entry.
        @SuppressWarnings("unchecked")
        MapEntry<K,E> other = (MapEntry<K,E>) that;
        return this.key.compareTo(other.key);
    }

    public String toString () {
        return "<" + key + "," + value + ">";
    }
}

public class Preveduvac {


    public static void main(String[] args) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(in.readLine());

        OBHT<String,String> translateTable = new OBHT<>(n*n);

        for(int i = 0; i<n; i++){

            String line = in.readLine();
            String pom[] = line.split(" ");
            translateTable.insert(pom[1],pom[0]);
        }

        String line = in.readLine();
        while(!line.equals("KRAJ")){
            line = in.readLine();
            int key = translateTable.search(line);
            if(key == -1){
                System.out.println("/");
                continue;
            }
            String val = translateTable.getValue(key);
            System.out.println(val);
        }

    }
}







