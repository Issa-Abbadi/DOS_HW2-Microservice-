package microservice;

public class CacheItem<K, V> {
    private K key; //URL
    private V value; //Responce
    private CacheItem prev, next; //refrence to the previous and the next nodes


    public CacheItem(K key, V value) {
        this.value = value;
        this.key = key;
    }

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
