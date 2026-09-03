package structures;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic Hash Table implementation using linear probing for collision resolution.
 * Supports storing any object type <T> associated with a unique String key.
 * Provides O(1) average time complexity for insertion, retrieval, and deletion.
 *
 * @param <T> The type of objects to be stored (e.g., Patient or Doctor)
 */
public class HashTable<T> {
    /**
     * Inner class representing a key-value pair in the hash table.
     * Supports tombstone marking for lazy deletion.
     */

    public List<T> getAllValues() {

    List<T> values = new ArrayList<>();

    for (int i = 0; i < capacity; i++) {

        if (table[i] != null && table[i].isActive()) {

            values.add(table[i].value);
        }
    }

    return values;
}

    private static class HashEntry<T> {
        String key;     // Unique identifier
        T value;        // The stored object
        boolean isTombstone;

        HashEntry() {
            this.key = null;
            this.value = null;
            this.isTombstone = false;
        }

        HashEntry(String key, T value) {
            this.key = key;
            this.value = value;
            this.isTombstone = false;
        }

        boolean isEmpty() {
            return value == null && !isTombstone;
        }

        boolean isActive() {
            return value != null;
        }
    }

    private HashEntry<T>[] table;
    private int capacity;
    private int size;
    private int tombstoneCount;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    private static final int INITIAL_CAPACITY = 16;

    /**
     * Constructs a new hash table with default capacity.
     */
    public HashTable() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Constructs a new hash table with the specified capacity.
     * @param capacity The initial size of the hash table array
     */
    @SuppressWarnings("unchecked")
    public HashTable(int capacity) {
        this.capacity = Math.max(capacity, INITIAL_CAPACITY);
        this.table = new HashEntry[this.capacity];
        this.size = 0;
        this.tombstoneCount = 0;

        for (int i = 0; i < this.capacity; i++) {
            table[i] = new HashEntry<>();
        }
    }

    // ==================== HASHING FUNCTIONS ====================

    private int hash(String key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    // ==================== CORE OPERATIONS ====================

    /**
     * Inserts or updates a value associated with the given key.
     * Resizes the table if the load factor exceeds the threshold.
     *
     * @param key   The unique identifier
     * @param value The object to store
     * @return The previous value associated with the key, or null if none existed
     */
    public T put(String key, T value) {
        if ((double) (size + tombstoneCount) / capacity >= LOAD_FACTOR_THRESHOLD) {
            rehash();
        }

        int idx = findSlot(key);

        if (idx == -1) {
            rehash();
            idx = findSlot(key);
        }

        T oldValue = null;
        if (table[idx].isActive()) {
            oldValue = table[idx].value;
        } else if (table[idx].isTombstone) {
            tombstoneCount--;
            size++;
        } else {
            size++;
        }

        table[idx] = new HashEntry<>(key, value);
        return oldValue;
    }

    /**
     * Retrieves the value associated with the given key.
     *
     * @param key The unique identifier to search for
     * @return The associated object, or null if not found
     */
    public T get(String key) {
        int idx = hash(key);
        int startIdx = idx;

        while (!table[idx].isEmpty()) {
            if (table[idx].isActive() && table[idx].key.equals(key)) {
                return table[idx].value;
            }
            idx = (idx + 1) % capacity;
            if (idx == startIdx) {
                break;
            }
        }
        return null;
    }

    /**
     * Removes the mapping for the specified key.
     * Uses tombstone marking to maintain linear probing integrity.
     *
     * @param key The unique identifier of the entry to remove
     * @return The removed object, or null if not found
     */
    public T remove(String key) {
        int idx = hash(key);
        int startIdx = idx;

        while (!table[idx].isEmpty()) {
            if (table[idx].isActive() && table[idx].key.equals(key)) {
                T removed = table[idx].value;
                table[idx].value = null;
                table[idx].key = null;
                table[idx].isTombstone = true;

                size--;
                tombstoneCount++;

                if (tombstoneCount > size / 2) {
                    compact();
                }

                return removed;
            }
            idx = (idx + 1) % capacity;
            if (idx == startIdx) {
                break;
            }
        }
        return null;
    }

    /**
     * Checks if a key exists in the table.
     *
     * @param key The unique identifier to check
     * @return true if the key exists, false otherwise
     */
    public boolean contains(String key) {
        return get(key) != null;
    }

    // ==================== HELPER METHODS ====================

    private int findSlot(String key) {
        int idx = hash(key);
        int startIdx = idx;
        int firstTombstone = -1;

        while (!table[idx].isEmpty()) {
            if (table[idx].isActive() && table[idx].key.equals(key)) {
                return idx;
            }
            if (table[idx].isTombstone && firstTombstone == -1) {
                firstTombstone = idx;
            }
            idx = (idx + 1) % capacity;
            if (idx == startIdx) {
                return firstTombstone != -1 ? firstTombstone : -1;
            }
        }
        return firstTombstone != -1 ? firstTombstone : idx;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        // System.out.println("   [HASH TABLE] Rehashing table...");
        HashEntry<T>[] oldTable = table;
        int oldCapacity = capacity;

        capacity *= 2;
        table = new HashEntry[capacity];
        size = 0;
        tombstoneCount = 0;

        for (int i = 0; i < capacity; i++) {
            table[i] = new HashEntry<>();
        }

        for (int i = 0; i < oldCapacity; i++) {
            if (oldTable[i] != null && oldTable[i].isActive()) {
                put(oldTable[i].key, oldTable[i].value);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void compact() {
        HashEntry<T>[] oldTable = table;
        table = new HashEntry[capacity];

        size = 0;
        tombstoneCount = 0;

        for (int i = 0; i < capacity; i++) {
            table[i] = new HashEntry<>();
        }

        for (int i = 0; i < capacity; i++) {
            if (oldTable[i] != null && oldTable[i].isActive()) {
                put(oldTable[i].key, oldTable[i].value);
            }
        }
    }

    // ==================== QUERY METHODS ====================

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getLoadFactor() {
        return (double) (size + tombstoneCount) / capacity;
    }

    // ==================== STATE MANAGEMENT ====================

    /**
     * Creates a deep copy of the hash table.
     * Note: The stored values themselves are shallow copied in this generic implementation.
     *
     * @return A new MyHashTable containing the same mappings
     */
    public HashTable<T> deepCopy() {
        HashTable<T> copy = new HashTable<>(this.capacity);

        for (int i = 0; i < this.capacity; i++) {
            if (this.table[i].isActive()) {
                copy.put(this.table[i].key, this.table[i].value);
            }
        }

        copy.size = this.size;
        return copy;
    }
}