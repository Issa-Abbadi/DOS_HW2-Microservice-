package microservice;

import java.util.HashMap;
import java.util.Map;

public class Cache<K, V> {
	
    private Map<K, CacheItem> map;
    private CacheItem first, last;
    private int size;
    private final int CAPACITY;
    private int hitCount = 0;
    private int missCount = 0;
  
    public Cache(int capacity) {
        CAPACITY = capacity;  // 
        map = new HashMap<>(CAPACITY);
    }
    
    public void put(K key, V value) {
    	CacheItem node = new CacheItem(key, value);

        if(map.containsKey(key) == false) {
            if(size() >= CAPACITY) {
                deleteNode(first);
            }
            addNodeToLast(node);
        }
        map.put(key, node);
    }

    public V get(K key) {
    	
    if(map.containsKey(key) == false) {
        return null;
    }
    
    
    CacheItem node = (CacheItem) map.get(key);
 
    node.incrementHitCount();
    reorder(node);
    return (V) node.getValue();
    
    }
    
   
    public void delete(K key) {
        deleteNode(map.get(key));
    }
    
    
    private void deleteNode(CacheItem node) {
        if(node == null) {
            return;
        }
        if(first != node && last != node){
        	node.getPrev().setNext(node.getNext());
        }
        if(last == node) {
            last = node.getPrev();
        }
        
        if(first == node) {
            first = node.getNext();
        } 
       
        map.remove(node.getKey());
        node = null; // Optional, collected by GC
        size--;
    }

    private void addNodeToLast(CacheItem node) {
        if(last != null) {
            last.setNext(node);
            node.setPrev(last);
        }

        last = node;
        if(first == null) {
            first = node;
        }
        size++;
    }
    
    private void reorder(CacheItem node) {
        if(last == node) {
            return;
        }
        if(first == node) {
            first = node.getNext();
        } else {
            node.getPrev().setNext(node.getNext());
        }
        last.setNext(node);
        node.setPrev(last);
        node.setNext(null);
        last = node;
    }
    
    public int size() {
        return size;
    }
    public int getHitCount() {
        return hitCount;
    }

    public int getMissCount() {
        return missCount;
    }
}
