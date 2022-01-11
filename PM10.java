/*
/* Квалитет на воздух Problem 1 (4 / 6)
Дадени се мерења на PM10 честички за населбите во Скопје. Ваша задача е за дадена населба да се најде просечната концентрација на PM10 честички.
Влез: Во првиот ред од влезот е даден бројот на мерења N (N<=10 000), а во секој нареден ред е прво дадена населбата и концентрацијата на PM10 разделени со празно место.
Во последниот ред е дадена населбата за која треба да најдете просечна концентрација на PM10 честички.
Излез: Просечната концентрација на PM10 честички за дадената населба (заокружена на 2 децимали).
Делумно решение: Задачата се смета за делумно решена доколку се поминати 7 тест примери.
Забелешка: При реализација на задачите не е дозволено да се користат помошни структури како низи или сл. На располагање од структурите имате само хеш структура.
Име на класа (Јава):PM10
Помош: 
За заокружување во Java може да го користите следниот код:
DecimalFormat df = new DecimalFormat("######.##");
double a = 335.453333;
df.format(a);
За заокружување во C може да го користите следниот код:
#include <math.h>
float val = 37.777779;
float nearest = roundf(val * 100) / 100; 
 
Sample input:
8
Centar 319.61
Karposh 296.74
Centar 531.98
Karposh 316.44
GaziBaba 384.05
GaziBaba 319.3
Karposh 393.18
GaziBaba 326.42
Karposh
 
Sample output: 
335.45 */
*/


package PastebinProblems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

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

class OBHT<K extends Comparable<K>,E> {

    // An object of class OBHT is an open-bucket hash table, containing entries
    // of class MapEntry.
    public MapEntry<K,E>[] buckets;

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


public class PM10 {

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        int n = Integer.parseInt(s);
        OBHT<String,String> tabela = new OBHT<>(2*n);

        String opshtina, merenje;
        String [] pom = null;
        double rez = 0;
        int koficka;
        for(int i=0;i<n;i++){

            s = br.readLine();
            pom = s.split(" ");
            opshtina = pom[0];
            merenje = pom[1];
            koficka = tabela.search(opshtina);
            if(koficka ==-1) tabela.insert(opshtina,merenje);
            else{    // ova proverka se praj za da ne se prepokrija duplikat kluch so vrednost bez istata da bidi zapisana vo kofickata

                merenje = merenje + " " + tabela.buckets[koficka].value;
                tabela.insert(opshtina,merenje);
            }
        }

        opshtina = br.readLine(); // koja opshtina sakame da ja analizirame..
        koficka = tabela.search(opshtina);

        if(koficka==-1)
            System.out.println("Nema merenja za opshtinata");
        else{
            String vkupnoMerenja = tabela.buckets[koficka].value;
            pom = vkupnoMerenja.split(" ");
            for(int i=0;i<pom.length;i++)
                rez += Double.parseDouble(pom[i]);
        }
        rez = rez/pom.length;
        DecimalFormat df = new DecimalFormat("######.##");
        String g = df.format(rez);
        System.out.println(g);
    }

}
