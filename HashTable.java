import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

/**
 * Implement the hash table interface here.
 * 
 * @author William Gu
 * 
 */
public class HashTable<K, V> implements HashTableInterface<K, V> {

 /**
  * DO NOT CHANGE THIS NUMBER.
  * 
  * This is the constant determining max load factor, or when you will
  * have to regrow the table.
  */
 private static final double MAX_LOAD_FACTOR = .71;
 
 /**
  * DO NOT CHANGE THIS NUMBER.
  * 
  * This is the constant determining what size you will initialize your
  * table array to.
  */
 private static final int INITIAL_CAPACITY = 11;
 
 /**
  * The number of entries in the table.
  */
 private int size;
 
 private int locationsUsed;
 
 /**
  * The backing array of your hash table.
  */
 private MapEntry<K, V>[] table;
 
 /**
  * Initialize the backing array to the initial capacity and the size
  * to the appropriate starting size.
  */
 public HashTable() {
    table = new MapEntry[INITIAL_CAPACITY];
    size = 0;
    locationsUsed = 0;
 }
 
 private int getHashIndex(K key) {
   int hashIndex = key.hashCode() % table.length;
   if (hashIndex < 0) {
     hashIndex = hashIndex + table.length;
   }
   return hashIndex;
 }
 
 private void rehash() {
   MapEntry<K, V>[] oldTable = table;
   int oldSize = table.length;
   int newSize = 2 * oldSize + 1;
   table = new MapEntry[newSize];
   size = 0;
   locationsUsed = 0;
   for (int index = 0; index < oldSize; index++) {
     if ((oldTable[index] != null) && !oldTable[index].isRemoved()) {
       put(oldTable[index].getKey(), oldTable[index].getValue());
     }
   }
 }
 
 private int locate(int index, K key) {
   boolean found = false;
   while (!found && (table[index] != null)) {
     if (!table[index].isRemoved() && key.equals(table[index].getKey())) {
       found = true;
     } else {
       index = (index + 1) % table.length;
     }
   }
   int result = -1;
   if (found) {
     result = index;
   }
   return result;
 }
 
 private int probe(int index, K key) {
   boolean found = false;
   int removedStateIndex = -1;
   while (!found && table[index] != null) {
     if (!table[index].isRemoved()) {
       if (key.equals(table[index].getKey())) {
         found = true;
       } else {
         index = (index + 1) % table.length;
       }
     } else {
       if (removedStateIndex == -1) {
         removedStateIndex = index;
       }
       index = (index + 1) % table.length;
     }
   }
   if (found || (removedStateIndex == -1)) {
     return index;
   } else {
     return removedStateIndex;
   }
 }
 
 @Override
 public V put(K key, V value) {
   if (key == null) {
     throw new IllegalArgumentException();
   }
   if (value == null) {
     throw new IllegalArgumentException();
   }
   V oldValue;
   if ((double) locationsUsed / table.length > MAX_LOAD_FACTOR) {
     rehash();
   }
   int index = getHashIndex(key);
   index = probe(index, key);
   if ((table[index] == null) || table[index].isRemoved()) {
     table[index] = new MapEntry<K, V>(key, value);
     size++;
     locationsUsed++;
     oldValue = null;
   } else {
     oldValue = table[index].getValue();
     table[index].setValue(value);
   }
   return oldValue;
 }

 @Override
 public V get(K key) {
   if (key == null) {
     throw new IllegalArgumentException();
   }
   V result = null;
   int index = getHashIndex(key);
   index = locate(index, key);
   if (index != -1) {
     result = table[index].getValue();
   }
   return result;
 }

 @Override
 public V remove(K key) {
   if (key == null) {
     throw new IllegalArgumentException();
   }
   V removedValue = null;
   int index = getHashIndex(key);
   index = locate(index, key);
   if (index != -1) {
     removedValue = table[index].getValue();
     table[index].setRemoved(true);
     locationsUsed--;
     size--;
   }
   return removedValue;
 }

 @Override
 public boolean contains(V value) {
   if (value == null) {
     throw new IllegalArgumentException();
   }
   for (int i = 0; i < table.length; i++) {
     if (table[i] != null) {
       if (table[i].getValue().equals(value) && !table[i].isRemoved()) {
         return true;
       }
     }
   }
   return false;
 }

 @Override
 public boolean containsKey(K key) {
   if (key == null) {
     throw new IllegalArgumentException();
   }
   boolean result = false;
   int index = getHashIndex(key);
   index = locate(index, key);
   if (index != -1) {
     result = true;
   }
   return result;
 }

 @Override
 public Set<K> keySet() {
   Set<K> keys = new HashSet();
   for (int i = 0; i < table.length; i++) {
     if (table[i] != null) {
       if (!table[i].isRemoved()) {
         keys.add(table[i].getKey());
       }
     }
   }
   return keys;
 }

 @Override
 public Collection<V> values() {
   ArrayList<V> values = new ArrayList();  
   for (int i = 0; i < table.length; i++) {
     if (table[i] != null) {
       if (!table[i].isRemoved()) {
         values.add(table[i].getValue());
       }
     }
   }
   return values;
 }

 @Override
 public Set<MapEntry<K, V>> entrySet() {
   Set<MapEntry<K, V>> maps = new HashSet();
   for (int i = 0; i < table.length; i++) {
     if (table[i] != null) {
       if (!table[i].isRemoved()) {
         maps.add(table[i]);
       }
     }
   }
   return maps;
 }
 
 @Override
 public int size() {
  return size;
 }

 @Override
 public boolean isEmpty() {
   if (size == 0) {
     return true;
   }
   return false;
 }

 @Override
 public void clear() {
    table = new MapEntry[INITIAL_CAPACITY];
    size = 0;
    locationsUsed = 0;
 }

}
