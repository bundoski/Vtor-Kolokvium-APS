/*
Поради ковид пандемијата при секое тестирање на даден пациент се зачувува општината во која живее, неговото презиме и дали е
позитивен или негативен на корона вирусот.
Потребни се статистички податоци во коишто ќе се одреди ризик факторот за дадена општина. За таа цел се чуваат две хеш табели
(CBHT) едната за позитивни, а другата за негативни
пациенти од корона вирусот. Ваша задача е за дадена оштина на излез да го испечатите ризик факторот во дадената општина.

Ризик фактор = Број на позитивни пациенти / број на негативни пациенти + број на позитивни пациенти

Забелешка : Можно е да се појават пациенти со исто презиме. Истите треба да се земат како посебни вредности во статистиката.

Влез:
На влез најпрво е даден бројот на пациенти - N, а потоа секој пациент е даден во нов ред во форматот:
" Opshtina vo koja zhivee" "Prezime na pacient" "Rezultati od testot(pozitiven / negativen)"

На крај е дадена општината за која треба да се пресмета ризик факторот.

Излез
Децимален број заокружен на две децимали кој го претставува ризик факторот за дадената општина
Пример
Влез:
6

Центар Стојаноски негативен
Центар Трајковски позитивен
Центар Петковски позитивен
Карпош Стојаноски позитивен
Карпош Трајковски негативен
Центар Трајковски позитивен
Центар

Излез: 0.75
( Izlezot se dobiva kako 3/(1+3) za opshtinata centar.

 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


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

class SLLNode<E> {
    protected E element;
    protected SLLNode<E> succ;

    public SLLNode(E elem, SLLNode<E> succ) {
        this.element = elem;
        this.succ = succ;
    }

    @Override
    public String toString() {
        return element.toString();
    }
}

class CBHT<K extends Comparable<K>, E> {

    // An object of class CBHT is a closed-bucket hash table, containing
    // entries of class MapEntry.
    private SLLNode<MapEntry<K,E>>[] buckets;

    @SuppressWarnings("unchecked")
    public CBHT(int m) {
        // Construct an empty CBHT with m buckets.
        buckets = (SLLNode<MapEntry<K,E>>[]) new SLLNode[m];
    }

    private int hash(K key) {
        // Napishete ja vie HASH FUNKCIJATA
        return Math.abs(key.hashCode()%buckets.length);

    }

    public SLLNode<MapEntry<K,E>> search(K targetKey) {
        // Find which if any node of this CBHT contains an entry whose key is
        // equal
        // to targetKey. Return a link to that node (or null if there is none).
        int b = hash(targetKey);
        for (SLLNode<MapEntry<K,E>> curr = buckets[b]; curr != null; curr = curr.succ) {
            if (targetKey.equals(((MapEntry<K, E>) curr.element).key))
                return curr;
        }
        return null;
    }

    public void insert(K key, E val) {		// Insert the entry <key, val> into this CBHT.
        MapEntry<K, E> newEntry = new MapEntry<K, E>(key, val);
        int b = hash(key);
        // TODO iskomentiran for poradi blokiranje na vnesuvanje na koficka ako e vnesena prethodno so isto prezime, da ne prepokriva vrednost.
        /*  for (SLLNode<MapEntry<K,E>> curr = buckets[b]; curr != null; curr = curr.succ) {
             if (key.equals(((MapEntry<K, E>) curr.element).key)) {
                // Make newEntry replace the existing entry ...
                curr.element = newEntry;
                return;
            }
        }*/
        // Insert newEntry at the front of the 1WLL in bucket b ...
        buckets[b] = new SLLNode<MapEntry<K,E>>(newEntry, buckets[b]);
    }

    public void delete(K key) {
        // Delete the entry (if any) whose key is equal to key from this CBHT.
        int b = hash(key);
        for (SLLNode<MapEntry<K,E>> pred = null, curr = buckets[b]; curr != null; pred = curr, curr = curr.succ) {
            if (key.equals(((MapEntry<K,E>) curr.element).key)) {
                if (pred == null)
                    buckets[b] = curr.succ;
                else
                    pred.succ = curr.succ;
                return;
            }
        }
    }

    public String toString() {
        String temp = "";
        for (int i = 0; i < buckets.length; i++) {
            temp += i + ":";
            for (SLLNode<MapEntry<K,E>> curr = buckets[i]; curr != null; curr = curr.succ) {
                temp += curr.element.toString() + " ";
            }
            temp += "\n";
        }
        return temp;
    }

}

public class CoronaRiskFactor {

    public static void main(String[] args) throws IOException {

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(bf.readLine());

        CBHT<String,String> pozitivni = new CBHT<>(N*3);
        CBHT<String,String> negativni = new CBHT<>(N*3);


        for(int i=0; i<N;i++){
            String p[] = bf.readLine().split(" ");
            String opstina = p[0];
            String prezime = p[1];
            String rezultat = p[2];
            if(rezultat.equals("позитивен"))
                pozitivni.insert(opstina,prezime);
            else
                negativni.insert(opstina,prezime);
        }
        String opstina = bf.readLine();

        SLLNode<MapEntry<String,String>> tmp = pozitivni.search(opstina);

        int pozitivnislucaevi = 0;
        if(pozitivni.search(opstina)!=null) {
            while (tmp != null) {
                pozitivnislucaevi++;
                tmp = tmp.succ;
            }
        }

        tmp = negativni.search(opstina);
        int negativnislucaevi = 0;
        if(negativni.search(opstina)!=null) {
            while (tmp != null) {
                negativnislucaevi++;
                tmp = tmp.succ;
            }
        }

        float riskFactor = (float) (pozitivnislucaevi * 1.00/(negativnislucaevi+pozitivnislucaevi));

        System.out.println(riskFactor);
    }

}










