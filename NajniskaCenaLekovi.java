

/*Во магацинот на една фармацевтска компанија се чуваат најразлични видови лекови. За секој лек
потребно е да се чуваат податоци за името на лекот, цената во денари и намената на лекот.
За поефикасен пристап до податоците за лековите, фармацевтската компанија одлучила податоците
 да ги чува во хеш табела (CBHT) каде се сместуваат соодветните податоци.

        Хеш табелата е достапна до крајните клиенти и истите може да пребаруваат низ внесените
        податоци. Бидејќи на пазарот постојат повеќе лекови кои таргетираат иста болест, најчесто
        клиентите го бараат оној лек кој има најниска цена. Па вашата задача е со користење на хеш
        табелата, за дадена намена (болест), да го испечатите лекот кој има најниска цена на пазарот.
         Доколку за бараната намена во магацинот нема лек се печати "Nema lek za baranata namena vo
         magacin.".

        Влез:

        Најпрво е даден бројот на лекови - N, а потоа секој лек е даден во нов ред во форматот:

        “Име на лек” “Намена” “Цена во денари”

        На крај е дадена намената за која треба да се пронајде лекот со најниска цена.

        Излез:

        Името на лекот со најмала цена.



        Пример:

        Влез:

        5

        Analgin Glavobolka 80

        Daleron Glavobolka 90

        Lineks Bolki_vo_stomak 150

        Spazmeks Bolki_vo_stomak 150

        Loratadin Alergija 150

        Glavobolka

        Излез:

        Analgin
*/

package NajniskaCenaLekovi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        // Translate key to an index of the array buckets.
        return Math.abs(key.hashCode()) % buckets.length;
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
        for (SLLNode<MapEntry<K,E>> curr = buckets[b]; curr != null; curr = curr.succ) {
            if (key.equals(((MapEntry<K, E>) curr.element).key)) {
                // Make newEntry replace the existing entry ...
                curr.element = newEntry;
                return;
            }
        }
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
class apcinja{
    String ime;
    Integer cena;

    public apcinja(String ime, Integer cena){
        this.ime=ime;
        this.cena=cena;
    }

    public Integer getCena() {
        return cena;
    }

    public String getIme() {
        return ime;
    }
}

public class NajniskaCenaLekovi {
    public static void main(String[] args) throws NumberFormatException, IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(bf.readLine());
        //
        CBHT<String,apcinja> tabela=new CBHT<String, apcinja>(N*N);


        for(int i = 0; i<N; i++){
            String p []= bf.readLine().split(" ");
            String ime = p[0];
            String namena = p[1];
            Integer cena = Integer.parseInt(p[2]);

            //
            if(tabela.search(namena)==null) {
                tabela.insert(namena, new apcinja(ime, cena));

            }else {
                if(cena<tabela.search(namena).element.value.getCena()){
                    tabela.insert(namena, new apcinja(ime, cena));
                }
            }
        }


        String namena = bf.readLine();
        if(tabela.search(namena)==null){
            System.out.println("Nema lek za baranata namena vo magacin");
        }else{
            String lek = tabela.search(namena).element.value.getIme();//spored namenata
            System.out.println(lek);}

    }

}
