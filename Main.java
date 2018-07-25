package okProjekt;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;

public class Main {

	
	public final static double kara = 0.25;
	
	
	
	public static void main(String[] args) throws Exception {
		Random r = new Random();

//		for (int i = 0; i < 20; i++) {
//			GeneratorInstancji.generujInstancje(i);
//		}
		
//		GeneratorInstancji.generujInstancje(0);
//		Algorytm.ladujInstancje(0);		
		
/*		
		while(2>1) {
			
			String plik = "TUNING/tuningLKandydatow5-100"+Algorytm.getLiczbaKandydatowDoTurnieju()+"_i_LZwyciezcowStala"+Algorytm.getLiczbaZwyciezcowTurniejow()+".txt";
			BufferedOutputStream instancjaOut = new BufferedOutputStream(new FileOutputStream(plik));
			PrintWriter pisz = new PrintWriter(new OutputStreamWriter(instancjaOut));
			
			int liczbaInstancji = 10;
			int liczbaPokolen = 100;
			
			pisz.write("Liczba zadañ: "+GeneratorInstancji.n+"\nLiczba przerw: "+GeneratorInstancji.k+"\nCzas trwania operacji i przerw: 1-"+
					GeneratorInstancji.maxCzasTrwania+"\n");
			pisz.write("Liczba instancji: "+liczbaInstancji+"\n");
			
			pisz.write("\nWielkoœæ populacji: "+Algorytm.getWielkoscPopulacji()+"\nWspólczynnik mutacji: "+Algorytm.getWspolczynnikMutacji()+
					"\nL. kandydatów do turnieju: "+Algorytm.getLiczbaKandydatowDoTurnieju()+"\nL. zwyciêzców turnieju: "+
					Algorytm.getLiczbaZwyciezcowTurniejow()+"\nWspó³czynnik podzia³u: "+Algorytm.getWspolczynnikPodzialu()+
					"\nLiczba pokoleñ: "+liczbaPokolen+"\n");
			
			System.out.println("L. zwyciêzców: "+Algorytm.getLiczbaZwyciezcowTurniejow()+" L. kandydatów: "+Algorytm.getLiczbaKandydatowDoTurnieju());
			
			for (int j = 0; j < liczbaInstancji; j++) {
				
//				GeneratorInstancji.generujInstancje(j);
				Algorytm.ladujInstancje(j);
				
				Populacja pop1 = Algorytm.tworzPierwszaPopulacje();
				Algorytm.listaPopulacji.add(pop1);
				
				long srednia=0;
				for (OsobnikPopulacji o : pop1.getListaOsobnikow()) {
					srednia += o.getWynik();
				}
				srednia = srednia/pop1.getListaOsobnikow().size();
				pisz.write(srednia+";");//Œredni wynik pierwszej populacji:
				
				Populacja nastepnePokolenie=null;
				for(int i=0;i<liczbaPokolen;i++) {
					nastepnePokolenie = Algorytm.algorytmGenetyczny(Algorytm.listaPopulacji.get(i));
					Algorytm.listaPopulacji.add(nastepnePokolenie);
				}
				pisz.write(Algorytm.najlepszyZPopulacji(nastepnePokolenie).getWynik()+";");//Najlepszy wynik ostatniej populacji:
				
				double polepszenieProcentowo = (double)(srednia-Algorytm.najlepszyZPopulacji(nastepnePokolenie).getWynik())/srednia *100;
				double polepszenieZaokraglone = (int)(polepszenieProcentowo*100)/100.0;//do 2 miejsc po przecinku
				String polepszenieString = Double.toString(polepszenieZaokraglone);
				polepszenieString = polepszenieString.replace(".", ",");
				
				pisz.write(polepszenieString+"\n");//Wynik poprawi³ siê o _%
				
				System.out.println("iteracja j = "+j);
				
				Algorytm.listaPopulacji.clear();
				Algorytm.maszyna1.clear();
				Algorytm.maszyna2.clear();
			}
			pisz.close();
			
//			double nowyWspolczynnikPodzialu = Algorytm.getWspolczynnikPodzialu();
//			nowyWspolczynnikPodzialu = (int)((nowyWspolczynnikPodzialu+0.05)*100)/100.0;
//			Algorytm.setWspolczynnikPodzialu(nowyWspolczynnikPodzialu);
			
//			int nowaLZwyciezcowTurniejow = Algorytm.getLiczbaZwyciezcowTurniejow();
//			nowaLZwyciezcowTurniejow += 5;
//			Algorytm.setLiczbaZwyciezcowTurniejow(nowaLZwyciezcowTurniejow);
			
//			int nowaLKandydatow = Algorytm.getLiczbaKandydatowDoTurnieju();
//			nowaLKandydatow += 5;
//			Algorytm.setLiczbaKandydatowDoTurnieju(nowaLKandydatow);
		
		}
*/	
		
		
		
		String plik = "WYNIKI/wynikZaliczenie"+".txt";
		BufferedOutputStream instancjaOut = new BufferedOutputStream(new FileOutputStream(plik));
		PrintWriter pisz = new PrintWriter(new OutputStreamWriter(instancjaOut));
		
		int liczbaInstancji = 1;
		int liczbaPokolen = 100;
		
		pisz.write("Liczba zadañ: "+GeneratorInstancji.n+"\nLiczba przerw: "+GeneratorInstancji.k+"\nCzas trwania operacji i przerw: 1-"+
				GeneratorInstancji.maxCzasTrwania+"\n");
		pisz.write("Liczba instancji: "+liczbaInstancji+"\n");
		
		pisz.write("\nWielkoœæ populacji: "+Algorytm.getWielkoscPopulacji()+"\nWspólczynnik mutacji: "+Algorytm.getWspolczynnikMutacji()+
				"\nL. kandydatów do turnieju: "+Algorytm.getLiczbaKandydatowDoTurnieju()+"\nL. zwyciêzców turnieju: "+
				Algorytm.getLiczbaZwyciezcowTurniejow()+"\nWspó³czynnik podzia³u: "+Algorytm.getWspolczynnikPodzialu()+
				"\nLiczba pokoleñ: "+liczbaPokolen+"\n");
		
		
		for (int j = 0; j < liczbaInstancji; j++) {
			
			Algorytm.ladujInstancje(j);
			
			Populacja pop1 = Algorytm.tworzPierwszaPopulacje();
			Algorytm.listaPopulacji.add(pop1);
			
			long srednia=0;
			for (OsobnikPopulacji o : pop1.getListaOsobnikow()) {
				srednia += o.getWynik();
			}
			srednia = srednia/pop1.getListaOsobnikow().size();
			int sredniaInt = (int)srednia;
			pisz.write(srednia+";");//Œredni wynik pierwszej populacji:
			
			Populacja nastepnePokolenie=null;
			for(int i=0;i<liczbaPokolen;i++) {
				nastepnePokolenie = Algorytm.algorytmGenetyczny(Algorytm.listaPopulacji.get(i));
				Algorytm.listaPopulacji.add(nastepnePokolenie);
			}
			pisz.write(Algorytm.najlepszyZPopulacji(nastepnePokolenie).getWynik()+";");//Najlepszy wynik ostatniej populacji:
			
			zapiszWynik(j, Algorytm.najlepszyZPopulacji(nastepnePokolenie), sredniaInt, "WYNIKI/wynikZaliczenie");
			
			
			double polepszenieProcentowo = (double)(srednia-Algorytm.najlepszyZPopulacji(nastepnePokolenie).getWynik())/srednia *100;
			double polepszenieZaokraglone = (int)(polepszenieProcentowo*100)/100.0;//do 2 miejsc po przecinku
			String polepszenieString = Double.toString(polepszenieZaokraglone);
			polepszenieString = polepszenieString.replace(".", ",");
			
			pisz.write(polepszenieString+"\n");//Wynik poprawi³ siê o _%
			
			System.out.println("iteracja j = "+j);
			
			Algorytm.listaPopulacji.clear();
			Algorytm.maszyna1.clear();
			Algorytm.maszyna2.clear();
		}
		pisz.close();
	
		
		
		
		
		//TESTOWANIE LICZBY IDLI
//		int liczbaIdli=0, czasIdli=0, liczbaOperacji=0, czasOperacji=0, liczbaPrzerw=0, czasPrzerw=0;
//		int nrPopulacji=0;
//		for (Populacja pop : Algorytm.listaPopulacji) {
//			
//			System.out.println("\nPopulacja: "+nrPopulacji);
//			OsobnikPopulacji best = Algorytm.najlepszyZPopulacji(pop);//bierzemy najlepszego z ka¿dej generacji
//			System.out.println("Wynik najlepszego: "+best.getWynik());
//			
//			int czasKonca=-1, czasPoczatkuNastepnego=-1;
//			boolean flaga = true;
//			int nrOpPoprzedniej=0;
//			for (int i = 0; i < best.getMaszyna1().size(); i++) {//maszyna 1
//			//szukamy IDLE	
//				if( best.getMaszyna1().get(i) instanceof Operacja ) {
//					Operacja o = (Operacja)best.getMaszyna1().get(i);
//					liczbaOperacji++;
//					czasOperacji += o.getCzasTrwaniaPraktyczny();		
//					
//					czasKonca = o.getCzasStartu() + o.getCzasTrwaniaPraktyczny();
//					nrOpPoprzedniej=o.getNumerOperacji();
//				} else if(best.getMaszyna1().get(i) instanceof Maintenance) {
//					liczbaPrzerw++;
//					czasPrzerw += best.getMaszyna1().get(i).getCzasTrwania();
//					
//					czasKonca = best.getMaszyna1().get(i).getCzasStartu() + best.getMaszyna1().get(i).getCzasTrwania();
//					nrOpPoprzedniej=0;
//				}
//				if(i<best.getMaszyna1().size()-1 /*&& flaga*/) {//jeœli istnieje nastêpny element
//					if(best.getMaszyna1().get(i+1) instanceof Operacja) {//i jeœli to operacja
//						Operacja o = (Operacja)best.getMaszyna1().get(i+1);
//						if(o.getNumerOperacji()==2 && nrOpPoprzedniej!=2) {// w dodaku operacja nr 2
//							czasPoczatkuNastepnego = o.getCzasStartu();
//							if(czasPoczatkuNastepnego>czasKonca) {//jeœli ta operacja nie zaczyna siê od razu po koñcu poprzedniego, to idle
//								liczbaIdli++;
//								czasIdli += czasPoczatkuNastepnego - czasKonca;
//								//flaga=false;
//							}
//						}
//					}
//				}		
//				czasKonca=-1; czasPoczatkuNastepnego=-1;
//
//			}
//			System.out.println("M1:    l. idli: "+liczbaIdli+" cz. trwania: "+czasIdli+" l. operacji: "+liczbaOperacji+" cz. trwania: "+czasOperacji);
//			liczbaIdli=0; czasIdli=0; liczbaOperacji=0; czasOperacji=0; liczbaPrzerw=0; czasPrzerw=0;
//			
//			nrPopulacji++;
//		}
		
		
		
		
		
	}
	
	
	
	public static void zapiszWynik(int numerInstancji,OsobnikPopulacji osobnik, int pierwotnyWynik, String sciezka) throws FileNotFoundException {
		String plik = sciezka+numerInstancji+".wynik";
		BufferedOutputStream wynikOut = new BufferedOutputStream(new FileOutputStream(plik));
		PrintWriter x = new PrintWriter(new OutputStreamWriter(wynikOut));
		
		int licznikMaint1 = 0;
		int licznikMaint2 = 0;
		int licznikIdle1 = 0;
		int licznikIdle2 = 0;
		
		int dlugoscMaint1 = 0;
		int dlugoscMaint2 = 0;
		int dlugoscIdle1 = 0;
		int dlugoscIdle2 = 0;
		
		x.write("**** "+numerInstancji+" ****\n");
		x.write(osobnik.getWynik()+";"+pierwotnyWynik+"\n");
		x.write("M1: ");
		for(Blok blok:osobnik.getMaszyna1()) {
			if(blok instanceof Operacja) {
				Operacja op = (Operacja) blok;
				x.write("op"+op.getNumerOperacji()+"_"+op.getNumerZadania()+","+op.getCzasStartu()+","+op.getCzasTrwania()+","+op.getCzasTrwaniaPraktyczny()+";");
			}else if(blok instanceof Maintenance) {
				Maintenance m = (Maintenance) blok;
				x.write("maint"+(licznikMaint1+1)+"_"+"M1"+","+m.getCzasStartu()+","+m.getCzasTrwania()+";");
				licznikMaint1++;
				dlugoscMaint1+=m.getCzasTrwania();
			}else if(blok instanceof Idle) {
				Idle idle = (Idle) blok;
				x.write("idle"+(licznikIdle1+1)+"_M1"+","+idle.getCzasStartu()+","+idle.getCzasTrwania()+";");
				licznikIdle1++;
				dlugoscIdle1+=idle.getCzasTrwania();
			}
		}
		x.write("\nM2: ");
		for(Blok blok:osobnik.getMaszyna2()) {
			if(blok instanceof Operacja) {
				Operacja op = (Operacja) blok;
				x.write("op"+op.getNumerOperacji()+"_"+op.getNumerZadania()+","+op.getCzasStartu()+","+op.getCzasTrwania()+","+op.getCzasTrwaniaPraktyczny()+";");
			}else if(blok instanceof Maintenance) {
				Maintenance m = (Maintenance) blok;
				x.write("maint"+(licznikMaint2+1)+"_"+"M2"+","+m.getCzasStartu()+","+m.getCzasTrwania()+";");
				licznikMaint2++;
				dlugoscMaint2+=m.getCzasTrwania();
			}else if(blok instanceof Idle) {
				Idle idle = (Idle) blok;
				x.write("idle"+(licznikIdle2+1)+"_M2"+","+idle.getCzasStartu()+","+idle.getCzasTrwania()+";");
				licznikIdle2++;
				dlugoscIdle2+=idle.getCzasTrwania();
			}
		}
		
		x.write("\n"+licznikMaint1+","+dlugoscMaint1);
		x.write("\n"+licznikMaint2+","+dlugoscMaint2);
		x.write("\n"+licznikIdle1+";"+dlugoscIdle1);
		x.write("\n"+licznikIdle2+";"+dlugoscIdle2+"\n");
		x.write("*** EOF ***");
		x.close();
	 }

}
