/*
Даден е стринг. Треба да се најде најчестиот под-стринг кој е дел од него и да се испечати. Доколку два под-стринга се исто 
фреквентни, тогаш се печати подолгиот. Доколку и овој услов го исполнуваат тогаш се печати лексикографски помалиот.

Пример: За стрингот "abc" под-стрингови се "a", "b", "c", "ab", "bc", "abc". Сите имаат иста честота па затоа се печати најдолгиот "abc".

Име на класата (Java): MostFrequentSubstring.

*/


package FrequentSubstring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class MapEntry<K extends Comparable<K>,E> implements Comparable<K> {

    K key;
    E value;

    public MapEntry (K key, E val) {
        this.key = key;
        this.value = val;
    }

    public int compareTo (K that) {
        @SuppressWarnings("unchecked")
        MapEntry<K,E> other = (MapEntry<K,E>) that;
        return this.key.compareTo(other.key);
    }

    public String toString () {
        return "(" + key + "," + value + ")";
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

    private SLLNode<MapEntry<K,E>>[] buckets;

    @SuppressWarnings("unchecked")
    public CBHT(int m) {
        buckets = (SLLNode<MapEntry<K,E>>[]) new SLLNode[m];
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    public SLLNode<MapEntry<K,E>> search(K targetKey) {
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
                curr.element = newEntry;
                return;
            }
        }
        buckets[b] = new SLLNode<MapEntry<K,E>>(newEntry, buckets[b]);
    }

    public void delete(K key) {
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
    public SLLNode<MapEntry<K, E>>[] getBuckets(){
        return buckets;
    }

}

public class MostFrequentSubstring {
    public static void main (String[] args) throws IOException {
        CBHT<String,Integer> tabela = new CBHT<String,Integer>(300);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String word = br.readLine().trim();

        /*
         *
         * Vashiot kod tuka....
         *
         */
        int n = word.length();
        int t=0; // pomoshen brojach za dodavanje na odredeno mesto vo nizata na zboroj
        String [] words = new String[n*n];
        for(int i=0;i<n;i++){
            for(int j=i;j<n;j++){
                words[t++] = word.substring(i,j+1);  // i e inclusive, j exclusive, t e zgolemen posle izvrshuvanje na substring funkcijata i dodavanje na tie elementi na odredenoto mesto.
            }

        }

        // proverka na cestota

        for(int i=0;i<t;i++){
            int cestota=0;
            for(int j=0;j<t;j++){
                if(words[i].equals(words[j]))
                    cestota++;
            }
            // od ko ce gi izbroj cestotite gi dodava vo hesh
            tabela.insert(words[i], cestota);
        }

        // proverka na najcest string..

        int max = 0;
        String maxString = "";
        for(int i=0; i < tabela.getBuckets().length; i++){
            SLLNode<MapEntry<String, Integer>> curr = tabela.getBuckets()[i];
            while(curr != null){
                if(curr.element.value == max && curr.element.key.length() > maxString.length()){
                    max = curr.element.value;
                    maxString = curr.element.key;
                } else if(curr.element.value > max){
                    max = curr.element.value;
                    maxString = curr.element.key;
                }

                curr = curr.succ;
            }
        }
        System.out.println(maxString);



    }
}
