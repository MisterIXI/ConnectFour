import java.io.*;

public class ShieldedCloseOutputStream extends OutputStream{
	OutputStream out;
	
	public ShieldedCloseOutputStream(OutputStream out) {
		this.out = out;
	}
	
	@Override
	public void close() {
		//do nothing
	}
	
	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}

}
