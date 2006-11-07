package org.codehaus.groovy.eclipse.model;

import java.io.File;

// import org.python.pydev.editor.codecompletion.revisited.visitors.Definition;
// import org.python.pydev.parser.jython.SimpleNode;

/**
 * Pointer points to a python resource inside a file system. 
 * 
 * You can create one of these, and use PyOpenAction to open the 
 * right editor.
 */
public class ItemPointer {

	public Object file;	// IFile or File object
	public Location start; // (first character)
	public Location end;   // (last character)
//    public Definition definition; //the definition that originated this ItemPointer (it might be null).
	
	public ItemPointer(Object file) {
		this(file, new Location(), new Location());
	}

//	public ItemPointer(Object file, SimpleNode n) {
//        int line = n.beginLine;
//        int col = n.beginColumn;
//        
//        this.file = file;
//        this.start = new Location(line-1, col-1);
//        this.end = new Location(line-1, col-1);
//    }
    
	public ItemPointer(Object file, Location start, Location end) {
		this.file = file;
		this.start = start;
		this.end = end;
	}
    
//    public ItemPointer(File file2, Location location, Location location2, Definition definition) {
//        this(file2, location, location2);
//        this.definition = definition;
//    }

    public String toString() {
        StringBuffer buffer = new StringBuffer("ItemPointer [");
        buffer.append(file);
        buffer.append(" - ");
        buffer.append(start);
        buffer.append(" - ");
        buffer.append(end);
        buffer.append("]");
        return buffer.toString();
    }
    
    public boolean equals(Object obj) {
        if(!(obj instanceof ItemPointer)){
            return false;
        }
        
        ItemPointer i = (ItemPointer) obj;
        if(!i.file.equals(file)){
            return false;
        }
        if(!i.start.equals(start)){
            return false;
        }
        if(!i.end.equals(end)){
            return false;
        }
        
        return true;
    }
    
    public int hashCode() {
        if(this.file != null){
            return this.file.hashCode() * 17;
        }else{
            return (this.end.column+1) * (this.start.line+2) * 9;
        }
    }
}
