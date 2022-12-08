package microservice;

public class CacheItem<K, V> {
    private K key;
    private V value;
    private int hitCount = 0; // LFU require this
    private CacheItem prev, next;


    public CacheItem(K key, V value) {
        this.value = value;
        this.key = key;
    }
    


	public void incrementHitCount() {
        this.hitCount++;
    }
    // getter / setter

	public CacheItem getPrev() {
		return prev;
	}

	public K getKey() {
		return key;
	}

	

	public V getValue() {
		return value;
	}

	public void setPrev(CacheItem prev) {
		this.prev = prev;
	}

	public CacheItem getNext() {
		return next;
	}

	public void setNext(CacheItem next) {
		this.next = next;
	}
}
