package pack1;
import java.io.FileWriter;

class MainClass {
	static void main(args){
		tempFileName = args[0]
		FileWriter writer = new FileWriter(tempFileName)
		for (arg in args){writer.write(arg)}
		writer.write('the end')
		writer.close()
	}
}