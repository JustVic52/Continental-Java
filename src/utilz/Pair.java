package utilz;

import java.io.Serializable;

public class Pair implements Serializable {

	private static final long serialVersionUID = 2996848779673939329L;
	
	public int first, second;
    
    public Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }
	
    public int getFirst() { return first; }
    
    public int getSecond() { return second; }
    
    public void setFirst(int f) { first = f; }
    
    public void setSecond(int s) { second = s; }
}
