package webshop;

public class Main {

	public static void main(String[] args) {
		try {
			System.out.println(new Calculation(args).GenerateOutput());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
