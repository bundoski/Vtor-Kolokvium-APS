/*
Потребно е да се симулира најава на еден систем. Притоа корисникот внесува корисничко име и лозинка. Доколку корисничкото име одговара со лозинката тогаш се 
печати Najaven, доколку не одговара се печати Nenajaven и на корисникот му се дава повторна шанса на корисникот да внесе корисничко име и лозинка. Во моментот кога корисникот ќе биде најавен престануваат обидите за најава.

Влез: Прво се дава број N на кориснички имиња и лозинки кои ќе бидат внесени во системот. Во наредните N реда се дадени корисничките имиња и лозинки разделени со 
едно празно место. Потоа се даваат редови со кориснички имиња и лозинки на корисници кои се обидувата да се најават (Пр. ana banana) За означување на крај на 
обидите во редицата се дава зборот KRAJ

Излез: За секој од влезовите кои се обид за најава се печати Nenajaven се додека не дoбиеме Najaven или додека имаме обиди за најава.

Пример. Влез: 3 ana banana pero zdero trpe trpi ana ana ana banana trpe trpi KRAJ

Излез: Nenajaven Najaven

Забелешка: Работете со хеш табела со затворени кофички. Самите решавате за големината на хеш табела, а хеш функцијата ви е дадена.

Име на класа: Lozinki
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;


public class Lozinki {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(in.readLine());

        CBHT<String, String> translatetable = new CBHT<String,String>(N*N);

        for(int i=0;i<N;i++){

            String line = in.readLine();
            String [] parts = line.split(" ");
            translatetable.insert(parts[0], parts[1]);
        }

        while(true){

            String line = in.readLine();
            String [] parts = line.split(" ");
            SLLNode<MapEntry<String,String>> tmp = translatetable.search(parts[0]);
            if(tmp.getElement().value.equals(parts[1])){
                System.out.println("Najaven");
                break;
            }
            else{
                System.out.println("Nenajaven");
            }
        }

    }
}

class CBHT<K extends Comparable<K>,E> {

    private SLLNode<MapEntry<K,E>>[] buckets;

    @SuppressWarnings("unchecked")
    public CBHT(int m) {

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
        SLLNode<MapEntry<K,E>> curr = buckets[b];
        for (; curr != null; curr = curr.succ) {
            if (targetKey.equals(curr.getElement().key)) {
                return curr;
            }
        }
        return null;

    }

    public void insert(K key, E value) {

        MapEntry<K,E> newEntry = new MapEntry<K,E>(key, value);
        int b = hash(key);
        SLLNode<MapEntry<K,E>> curr = buckets[b];
        for (; curr != null; curr = curr.succ) {
            if (key.equals(curr.getElement().key)) {
                curr.setElement(newEntry);
                return;
            }
        }
        buckets[b] = new SLLNode<MapEntry<K,E>>(newEntry, buckets[b]);
    }

    public void delete (K key) {
        // Delete the entry (if any) whose key is equal to key from this CBHT.
        int b = hash(key);
        SLLNode<MapEntry<K,E>> pred = null, curr = buckets[b];
        for (; curr != null; pred = curr, curr = curr.succ) {
            if (key.equals(curr.getElement().key)) {
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
                temp += curr.getElement().toString() + " ";
            }
            temp += "\n";
        }
        return temp;
    }

}

class MapEntry<K extends Comparable<K>,E> implements Comparable<K> {

    K key;
    E value;

    public MapEntry(K key, E value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int compareTo(K that) {
        return this.key.compareTo(that);
    }

    @Override
    public String toString () {
        return "<" + key + "," + value + ">";
    }
}

class SLLNode<E> {

    private E element;
    public SLLNode<E> succ;

    public SLLNode(E element, SLLNode<E> succ) {
        this.setElement(element);
        this.succ = succ;
    }

    @Override
    public String toString() {
        return getElement().toString();
    }

    public E getElement() {
        return element;
    }

    public void setElement(E element) {
        this.element = element;
    }

}
