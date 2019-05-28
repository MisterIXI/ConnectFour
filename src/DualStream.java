import java.io.*;

public class DualStream extends OutputStream{
	DataOutputStream a;
	DataOutputStream b;
	@Override
	public void write(int b) throws IOException {
		// TODO Auto-generated method stub
		printToBoth(b + "");
	}
	
	public DualStream(DataOutputStream a, DataOutputStream b) {
		this.a = a;
		this.b = b;
	}
	
	public void printToBoth(String content) throws IOException{
		a.writeUTF(content);
		b.writeUTF(content);
	}


}
