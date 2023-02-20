package program;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class generator {
	public static void main(String[] args) {
		String vstupy = "";
		int pocet_znakov = 15;
		int pocet_formul = 100;
		int pocet_vstupov = 1000;

		Random random = new Random();
		
		for(int i = 0; i < pocet_znakov; i++) {	//String použitých znakov abc...
			vstupy = vstupy + (char)('a' + i);
		}
		System.out.println(vstupy);
		ArrayList<String> formuly = new ArrayList<>();	//Vytvorím náhodné formuly
		ArrayList<String> formulyInt = new ArrayList<>();
		for(int i = 0; i < pocet_formul; i++) {			//môžu sa opakovať / byť nadbytočné, ale pre moje riešenie to nevadí
			formuly.add("");
			formulyInt.add("");
			for(int j = 0; j < pocet_znakov; j++) {		//každý znak tam buď: nie je / je / je negovaný
				switch(random.nextInt(3)) {
				case(0):
					formulyInt.set(i, formulyInt.get(i) + "2");
					break;
				case(1):
					formulyInt.set(i, formulyInt.get(i) + "1");
					formuly.set(i, formuly.get(i) + vstupy.charAt(j));
					break;
				case(2):
					formulyInt.set(i, formulyInt.get(i) + "0");
					formuly.set(i, formuly.get(i) + '!' + vstupy.charAt(j));
					break;
				}
			}
		}
		for(int i = 0; i < formuly.size(); i++)
			System.out.println(formuly.get(i));
		
		
		
		ArrayList<Vstup> vysledky = new ArrayList<>();	//podobne vygenerujem náhodné vstupy
		for(int i = 0; i < pocet_vstupov; i++) {		//zas môžu byť duplicitné, ale tu je to duplom jedno
			vysledky.add(new Vstup());
			for(int j = 0; j < pocet_znakov; j++) {
				switch(random.nextInt(2)) {
				case(0):
					vysledky.get(i).addHladany('0');
					break;
				case(1):
					vysledky.get(i).addHladany('1');
					break;
				}
			}
		}
		/*for(int i = 0; i < vysledky.size(); i++)
			System.out.println(vysledky.get(i).getHladany());*/
		
		//pridám pravdivostné hodnoty k vstupom '0' / '1'
		boolean hodnota;
		for(int i = 0; i < pocet_vstupov; i++) {
			hodnota = false;
			for(int j = 0; j < formuly.size(); j++) {	//girl???
				for(int k = 0; k < pocet_znakov; k++) {
					if(formulyInt.get(j).charAt(k) == '2' || vysledky.get(i).getHladany().charAt(k) == formulyInt.get(j).charAt(k)) {
						if(k == pocet_znakov - 1)
							hodnota = true;
						continue;
					}
					else {
						break;
					}
					
				}
				if(hodnota) {
					vysledky.get(i).setValueTrue();
					break;
				}
				//prechádzaj formuly
				//ak prejdem nejakú formulu a vstup vyhovuje, tak vysledok = 1
				//ak prejdem všetky a žiadna nevyhovuje, tak vysledok ostáva 0
			}
		}
		
		for(int i = 0; i<pocet_vstupov; i++) {
			System.out.println(vysledky.get(i).getHladany() + " : " +vysledky.get(i).getValue());
		}
		
		try {	//zapíšem výsledky to súboru Test.txt vo forme aby vedel výsledny môj program sprcovať
		File file = new File("Test.txt");
		file.createNewFile();
		}catch(Exception e) {
			System.out.println("error creating file");
		}
		
		FileWriter writer;
		try {
			writer = new FileWriter("Test.txt");
			writer.write(vstupy + ' ');
			for(int i = 0; i < formuly.size(); i++) {
				writer.write(formuly.get(i));
				if(i != formuly.size() - 1)
					writer.write('+');
			}
			writer.write('\n');
			for(int i = 0; i<pocet_vstupov; i++) {
				writer.write(vysledky.get(i).getHladany() + " " +vysledky.get(i).getValue() + '\n');
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
