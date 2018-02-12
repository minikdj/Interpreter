
public class Pair<S, T> {
    public S x; //Type of variable being stored
    public T y; //value of the variable stored a string

    public Pair(S x, T y) { 
        this.x = x;
        this.y = y;
    }
    
    public Pair(Pair<S, T> pair) {
    		pair.x = x;
    		pair.y = y;
    }
    
}