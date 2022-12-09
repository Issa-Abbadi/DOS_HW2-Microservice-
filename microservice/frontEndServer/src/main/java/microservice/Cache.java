package microservice;

import java.util.HashMap;
import java.util.Map;

public class Cache<K, V> {
	
    private Map<K, CacheItem> map; //store them in hash map
    private CacheItem first, last; // to know the first and last nodes
    private int size; 
    private final int CAPACITY;
  
    public Cache(int capacity) {
        CAPACITY = capacity;  
        map = new HashMap<>(CAPACITY);
    }
    
    
    // to add node to the cache
    public void put(K key, V value) {
    	CacheItem node = new CacheItem(key, value);

        if(map.containsKey(key) == false) {
            if(size() >= CAPACITY) {
                deleteNode(first); // delete least recently used node
            }
            addNodeToLast(node); // add the new node to the last
        }
        map.put(key, node);
    }

    // get node from cache
    public V get(K key) {
    	
    if(map.containsKey(key) == false) {
        return null;
    }
    
    
    CacheItem node = (CacheItem) map.get(key); // get node
 
    // reorder the nodes in LRU order
    reorder(node);
    return (V) node.getValue();
    
    }
    
   //delete node 
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

}
