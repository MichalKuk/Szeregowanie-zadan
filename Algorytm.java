package okProjekt;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Algorytm {
	
	public static List<Blok> maszyna1 = new LinkedList<>();
	public static List<Blok> maszyna2 = new LinkedList<>();
	public static List<Populacja> listaPopulacji = new LinkedList<>();
	
	private static int wielkoscPopulacji = 200;
	private static double wspolczynnikMutacji = 1;
	private static int liczbaKandydatowDoTurnieju = 30;
	private static int liczbaZwyciezcowTurniejow = 75;
	private static double wspolczynnikPodzialu = 0.5;
	
	private static int liczbaZadan;
	
	private static List<Zadanie> listaZadan = new LinkedList<>();
	private static List<Maintenance> listaPrzerw = new LinkedList<>();
	
	
	public static void ladujInstancje(int numerInstancji) throws IOException {
		listaZadan.clear();
		listaPrzerw.clear();
		String plik = "INSTANCJE/instancjeDoStrojenia/instancja"+numerInstancji+".instancja";
		BufferedInputStream instancjaIn = new BufferedInputStream(new FileInputStream(plik));
		BufferedReader x = new BufferedReader(new InputStreamReader(instancjaIn));
		x.readLine();
		liczbaZadan = Integer.parseInt(x.readLine());
		String[] tab;
		String linia;
		for(int i=0;i<liczbaZadan;i++) {
			linia= x.readLine();
			tab = linia.split(";");
			Zadanie zadanie = new Zadanie(i);
			zadanie.getOperacje()[0].setCzasTrwania(Integer.parseInt(tab[0]));
			zadanie.getOperacje()[1].setCzasTrwania(Integer.parseInt(tab[1]));
			zadanie.getOperacje()[0].setMaszyna(Integer.parseInt(tab[2]));
			zadanie.getOperacje()[1].setMaszyna(Integer.parseInt(tab[3]));
			listaZadan.add(zadanie);
		}
		
		while(!(linia=x.readLine()).startsWith("*")){
			tab = linia.split(";");
			Maintenance przerwa = new Maintenance();
			przerwa.setMaszyna(Integer.parseInt(tab[1]));
			przerwa.setCzasTrwania(Integer.parseInt(tab[2]));
			przerwa.setCzasStartu(Integer.parseInt(tab[3]));
			listaPrzerw.add(przerwa);
			
		}
		
//		for(int i=0;i<listaZadan.size();i++) {
//			System.out.println(listaZadan.get(i));
//		}
//		for(int i=0;i<listaPrzerw.size();i++) {
//			System.out.println(listaPrzerw.get(i));
//		}
		
		x.close();
	}
	
	
	public static Populacja algorytmGenetyczny(Populacja populacjaRodzicow) throws Exception {
		Random r = new Random();
		Populacja populacjaDzieci = new Populacja();
		OsobnikPopulacji[] zwyciezcyTurniejow = new OsobnikPopulacji[liczbaZwyciezcowTurniejow];
		while(populacjaDzieci.getListaOsobnikow().size() != wielkoscPopulacji) {
			
			for(int i=0;i<liczbaZwyciezcowTurniejow;i++) {
				OsobnikPopulacji x = turniej(populacjaRodzicow);
				zwyciezcyTurniejow[i]=x;
				}
			
			OsobnikPopulacji[] poZmianach =  krzyzowanie(zwyciezcyTurniejow[r.nextInt(zwyciezcyTurniejow.length)],zwyciezcyTurniejow[r.nextInt(zwyciezcyTurniejow.length)]);
			double prawdopodobienstwo = r.nextDouble();
			if(prawdopodobienstwo <= wspolczynnikMutacji) {
				mutacja(poZmianach[0]);
			}
			prawdopodobienstwo = r.nextDouble();
			if(prawdopodobienstwo <= wspolczynnikMutacji) {
				mutacja(poZmianach[1]);
			}
			
			populacjaDzieci.getListaOsobnikow().add(poZmianach[0]);
			populacjaDzieci.getListaOsobnikow().add(poZmianach[1]);
		}
		
		int indeksNajlepszego = 0;
		for(int i=1;i<populacjaDzieci.getListaOsobnikow().size();i++) {
			if(populacjaDzieci.getListaOsobnikow().get(i).getWynik() < populacjaDzieci.getListaOsobnikow().get(indeksNajlepszego).getWynik()) {
				indeksNajlepszego = i;
			}
		}
		
		return populacjaDzieci;
		
	}
	
	private static OsobnikPopulacji turniej(Populacja populacja) {
		OsobnikPopulacji[] uczestnicyTurnieju = new OsobnikPopulacji[liczbaKandydatowDoTurnieju];
		Random r  = new Random();
		List<Integer> listaUzytychIndeksow = new LinkedList<>();
		int losowyIndeks;
		for(int i=0;i<liczbaKandydatowDoTurnieju;i++) {
			do {
				losowyIndeks = r.nextInt(populacja.getListaOsobnikow().size());
				if(!(listaUzytychIndeksow.contains(losowyIndeks))) {
					listaUzytychIndeksow.add(losowyIndeks);
					break;
				}
			}while(true);
			uczestnicyTurnieju[i] = populacja.getListaOsobnikow().get(losowyIndeks);
		}
		
		int indeksNajlepszego = 0;
		for(int i=1;i<uczestnicyTurnieju.length;i++) {
			if(uczestnicyTurnieju[i].getWynik() < uczestnicyTurnieju[indeksNajlepszego].getWynik()) {
				indeksNajlepszego = i;
			}
		}
		
		return uczestnicyTurnieju[indeksNajlepszego];
	}
	
	public static OsobnikPopulacji[] krzyzowanie(OsobnikPopulacji r1, OsobnikPopulacji r2) throws Exception {
		int indeksPodzialu = (int) (r1.getZadania().size()*wspolczynnikPodzialu);
		List<Zadanie> zadaniaDziecka1tmp = new LinkedList<>();//listy pomocnicze z zadaniami - tymi samymi obiektami co maja rodzice
		List<Zadanie> zadaniaDziecka2tmp = new LinkedList<>();
		for(int i=0;i<indeksPodzialu;i++) {
			zadaniaDziecka1tmp.add( r1.getZadania().get(i));
			zadaniaDziecka2tmp.add( r2.getZadania().get(i));
		}
		List<Zadanie> brakujaceZadaniaDziecka1 = new LinkedList<>();
		List<Zadanie> brakujaceZadaniaDziecka2 = new LinkedList<>();
		
		for(Zadanie zadanie: r1.getZadania()) {//bierze od rodzica1 zadania, których jeszcze mu brakuje
			if(!zadaniaDziecka1tmp.contains(zadanie)) {
				brakujaceZadaniaDziecka1.add(zadanie);
			}
		}
		
		for(Zadanie zadanie: r2.getZadania()) {//i dodaje te brakujace zadania w kolejnoœci takiej jak wystêpuj¹ w rodzicu 2
			for(int i=0;i<brakujaceZadaniaDziecka1.size();i++) {
				if(brakujaceZadaniaDziecka1.get(i).getNumerZadania()==zadanie.getNumerZadania()) {
					zadaniaDziecka1tmp.add(zadanie);
				}
			}
		}
		
		for(Zadanie zadanie: r2.getZadania()) {
			if(!zadaniaDziecka2tmp.contains(zadanie)) {
				brakujaceZadaniaDziecka2.add(zadanie);
			}
		}

		for(Zadanie zadanie: r1.getZadania()) {
			for(int i=0;i<brakujaceZadaniaDziecka2.size();i++) {
				if(brakujaceZadaniaDziecka2.get(i).getNumerZadania()==zadanie.getNumerZadania()) {
					zadaniaDziecka2tmp.add(zadanie);
				}
			}
		}
		
		List<Zadanie> zadaniaDziecka1 = new LinkedList<>();//listy ostateczne z nowymi, sklonowanymi zadaniami
		List<Zadanie> zadaniaDziecka2 = new LinkedList<>();
		
		for (Zadanie zadanie : zadaniaDziecka1tmp) {//dodaje na listy zadan dzieci sklonowane obiekty
			zadaniaDziecka1.add((Zadanie)zadanie.clone());
		}
		for (Zadanie zadanie : zadaniaDziecka2tmp) {
			zadaniaDziecka2.add((Zadanie)zadanie.clone());
		}
		
		List<Blok> maszyna1Dziecka1 = new LinkedList<>();
		List<Blok> maszyna2Dziecka1 = new LinkedList<>();
		
		List<Blok> maszyna1Dziecka2 = new LinkedList<>();
		List<Blok> maszyna2Dziecka2 = new LinkedList<>();
		
		int wynikDziecka1 = 0;
		int wynikDziecka2 = 0;
		
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!				
//					for (Zadanie zad : zadanieDziecka1) {
//						zad.wyczyszcOperacje();//ustawia czas startu = 0 i cz. praktyczny = czas trwania  dla obu operacji
//					}
//					for (Zadanie zad : zadanieDziecka2) {
//						zad.wyczyszcOperacje();//ustawia czas startu = 0 i cz. praktyczny = czas trwania  dla obu operacji
//					}
		
		
		try {
			wynikDziecka1 = generujRozwiazanie2(zadaniaDziecka1, maszyna1Dziecka1, maszyna2Dziecka1);
		
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		try {
			wynikDziecka2 = generujRozwiazanie2(zadaniaDziecka2, maszyna1Dziecka2, maszyna2Dziecka2);
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		} 
		
		OsobnikPopulacji dziecko1 = new OsobnikPopulacji(zadaniaDziecka1,maszyna1Dziecka1,maszyna2Dziecka1,wynikDziecka1);
		OsobnikPopulacji dziecko2 = new OsobnikPopulacji(zadaniaDziecka2,maszyna1Dziecka2,maszyna2Dziecka2,wynikDziecka2);
		OsobnikPopulacji[] wynik = new OsobnikPopulacji[2];
		wynik[0] = dziecko1;
		wynik[1] = dziecko2;
		return wynik;
		
	}
	
	public static void mutacja(OsobnikPopulacji osobnik) {
		Random r = new Random();
		double prawdopodobienstwo = r.nextDouble();
		if(prawdopodobienstwo <= wspolczynnikMutacji) {
			int indeks1 = r.nextInt(osobnik.getZadania().size());
			int indeks2 = -1;
			do {
				indeks2 = r.nextInt(osobnik.getZadania().size());
			}while(indeks1 == indeks2);
			
			Collections.swap(osobnik.getZadania(), indeks1, indeks2);
			
			int wynik = 0;
			
			try {
				wynik = generujRozwiazanie2(osobnik.getZadania(), osobnik.getMaszyna1(), osobnik.getMaszyna2());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			} 
			osobnik.setWynik(wynik);
		}
	}
	
	public static OsobnikPopulacji najlepszyZPopulacji(Populacja populacja) {
		int indeks = 0;
		for(int i=0;i<populacja.getListaOsobnikow().size();i++) {
			if(populacja.getListaOsobnikow().get(i).getWynik() < populacja.getListaOsobnikow().get(indeks).getWynik()) {
				indeks = i;
			}
		}
		return populacja.getListaOsobnikow().get(indeks);
	}
	
	/*
	public static int generujRozwiazanie(List<Zadanie> listaZadan, List<Blok> maszyna1, List<Blok> maszyna2) throws CloneNotSupportedException {
		maszyna1.clear();
		maszyna2.clear();
		
		int miejsceNaM1 = 0;
		int miejsceNaM2 = 0;
		Maintenance tmpPrzerwa;
		
		//dodawanie pierwszej operacji kazdego zadania
		for(int i=0;i<listaZadan.size();i++) {
			int licznikPrzerwPrzerywajacychOperacje = 0;
			Zadanie tmp = listaZadan.get(i);
			int czasTrwania = tmp.getOperacje()[0].getCzasTrwania();
			int maszyna = tmp.getOperacje()[0].getMaszyna();
			do {
				if(maszyna==1){
					if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(miejsceNaM1,czasTrwania,maszyna))==null) {//jesli nie ma na drodze przerwy
						tmp.getOperacje()[0].setCzasStartu(miejsceNaM1);
						tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania);
						maszyna1.add(tmp.getOperacje()[0]);
						miejsceNaM1+=czasTrwania;
						czasTrwania=0;		
					}else{//jesli jest na drodze przerwa
					if(licznikPrzerwPrzerywajacychOperacje==0){
						czasTrwania = GeneratorInstancji.sufit((double)czasTrwania + czasTrwania*Main.kara);
						int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-miejsceNaM1;
						Zadanie tmp2 = (Zadanie) tmp.clone();
						tmp2.getOperacje()[0].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
						tmp2.getOperacje()[0].setCzasStartu(miejsceNaM1);
						maszyna1.add(tmp2.getOperacje()[0]);
						tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania - czasDoPrzerwy);
						miejsceNaM1+=czasDoPrzerwy;
						czasTrwania-=czasDoPrzerwy;
						licznikPrzerwPrzerywajacychOperacje++;
						
						maszyna1.add(tmpPrzerwa);
						miejsceNaM1+=tmpPrzerwa.getCzasTrwania();
						
					}else{
						int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-miejsceNaM1;
						Zadanie tmp2 = (Zadanie) tmp.clone();
						tmp2.getOperacje()[0].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
						tmp2.getOperacje()[0].setCzasStartu(miejsceNaM1);
						maszyna1.add(tmp2.getOperacje()[0]);
						tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania - czasDoPrzerwy);
						miejsceNaM1+=czasDoPrzerwy;
						czasTrwania-=czasDoPrzerwy;
						licznikPrzerwPrzerywajacychOperacje++;
						maszyna1.add(tmpPrzerwa);
						miejsceNaM1+=tmpPrzerwa.getCzasTrwania();
					}

					}

					}else{
						if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(miejsceNaM2,czasTrwania,maszyna))==null) {//jesli nie ma na drodze przerwy
							tmp.getOperacje()[0].setCzasStartu(miejsceNaM2);
							tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania);
							maszyna2.add(tmp.getOperacje()[0]);
							miejsceNaM2+=czasTrwania;
							czasTrwania=0;		
						}else{//jesli jjest na drodze przerwa
						if(licznikPrzerwPrzerywajacychOperacje==0){
							czasTrwania = GeneratorInstancji.sufit((double)czasTrwania + czasTrwania*Main.kara);
							int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-miejsceNaM2;
							Zadanie tmp2 = (Zadanie) tmp.clone();
							tmp2.getOperacje()[0].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
							tmp2.getOperacje()[0].setCzasStartu(miejsceNaM2);
							maszyna2.add(tmp2.getOperacje()[0]);
							tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania-czasDoPrzerwy);
							miejsceNaM2+=czasDoPrzerwy;
							czasTrwania-=czasDoPrzerwy;
							licznikPrzerwPrzerywajacychOperacje++;
							maszyna2.add(tmpPrzerwa);
							miejsceNaM2+=tmpPrzerwa.getCzasTrwania();
						}else{
							int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-miejsceNaM2;
							Zadanie tmp2 = (Zadanie) tmp.clone();
							tmp2.getOperacje()[0].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
							tmp2.getOperacje()[0].setCzasStartu(miejsceNaM2);
							maszyna2.add(tmp2.getOperacje()[0]);
							tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania-czasDoPrzerwy);
							miejsceNaM2+=czasDoPrzerwy;
							czasTrwania-=czasDoPrzerwy;
							licznikPrzerwPrzerywajacychOperacje++;
							maszyna2.add(tmpPrzerwa);
							miejsceNaM2+=tmpPrzerwa.getCzasTrwania();
						}

						}	


					}
				
			}while(czasTrwania>0);
			
			

		}
		
		int miejsceZakonczenia = miejsceNaM1 > miejsceNaM2 ? miejsceNaM1 : miejsceNaM2;
		
		//dodawanie drugiej operacji kazdego zadania od miejsca w ktorym skonczylismy
		miejsceNaM1 = miejsceZakonczenia;
		miejsceNaM2 = miejsceZakonczenia;
		
		for(int i=0;i<listaZadan.size();i++) {
			int licznikPrzerwPrzerywajacychOperacje = 0;
			Zadanie tmp = listaZadan.get(i);
			int czasTrwania = tmp.getOperacje()[1].getCzasTrwania();
			int maszyna = tmp.getOperacje()[1].getMaszyna();
			do {
				if(maszyna==1){
					if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(miejsceNaM1,czasTrwania,maszyna))==null) {//jesli nie ma na drodze przerwy
						tmp.getOperacje()[1].setCzasStartu(miejsceNaM1);
						tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania);
						maszyna1.add(tmp.getOperacje()[1]);
						miejsceNaM1+=czasTrwania;
						czasTrwania=0;		
					}else{//jesli jjest na drodze przerwa
					if(licznikPrzerwPrzerywajacychOperacje==0){
						czasTrwania = GeneratorInstancji.sufit((double)czasTrwania + czasTrwania*Main.kara);
						int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-miejsceNaM1;
						Zadanie tmp2 = (Zadanie) tmp.clone();
						tmp2.getOperacje()[1].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
						tmp2.getOperacje()[1].setCzasStartu(miejsceNaM1);
						maszyna1.add(tmp2.getOperacje()[1]);
						tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania-czasDoPrzerwy);
						miejsceNaM1+=czasDoPrzerwy;
						czasTrwania-=czasDoPrzerwy;
						licznikPrzerwPrzerywajacychOperacje++;
						maszyna1.add(tmpPrzerwa);
						miejsceNaM1+=tmpPrzerwa.getCzasTrwania();

					}else{
						int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-miejsceNaM1;
						Zadanie tmp2 = (Zadanie) tmp.clone();
						tmp2.getOperacje()[1].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
						tmp2.getOperacje()[1].setCzasStartu(miejsceNaM1);
						maszyna1.add(tmp2.getOperacje()[1]);
						tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania-czasDoPrzerwy);
						miejsceNaM1+=czasDoPrzerwy;
						czasTrwania-=czasDoPrzerwy;
						licznikPrzerwPrzerywajacychOperacje++;
						maszyna1.add(tmpPrzerwa);
						miejsceNaM1+=tmpPrzerwa.getCzasTrwania();
					}

					}

					}else{
						if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(miejsceNaM2,czasTrwania,maszyna))==null) {//jesli nie ma na drodze przerwy
							tmp.getOperacje()[1].setCzasStartu(miejsceNaM2);
							tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania);
							maszyna2.add(tmp.getOperacje()[1]);
							miejsceNaM2+=czasTrwania;
							czasTrwania=0;		
						}else{//jesli jjest na drodze przerwa
						if(licznikPrzerwPrzerywajacychOperacje==0){
							czasTrwania = GeneratorInstancji.sufit((double)czasTrwania + czasTrwania*Main.kara);
							int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-miejsceNaM2;
							Zadanie tmp2 = (Zadanie) tmp.clone();
							tmp2.getOperacje()[1].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
							tmp2.getOperacje()[1].setCzasStartu(miejsceNaM2);
							maszyna2.add(tmp2.getOperacje()[1]);
							tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania-czasDoPrzerwy);
							miejsceNaM2+=czasDoPrzerwy;
							czasTrwania-=czasDoPrzerwy;
							licznikPrzerwPrzerywajacychOperacje++;
							maszyna2.add(tmpPrzerwa);
							miejsceNaM2+=tmpPrzerwa.getCzasTrwania();
						}else{
							int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-miejsceNaM2;
							Zadanie tmp2 = (Zadanie) tmp.clone();
							tmp2.getOperacje()[1].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
							tmp2.getOperacje()[1].setCzasStartu(miejsceNaM2);
							maszyna2.add(tmp2.getOperacje()[1]);
							tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania-czasDoPrzerwy);
							miejsceNaM2+=czasDoPrzerwy;
							czasTrwania-=czasDoPrzerwy;
							licznikPrzerwPrzerywajacychOperacje++;
							maszyna2.add(tmpPrzerwa);
							miejsceNaM2+=tmpPrzerwa.getCzasTrwania();
						}

						}	

					}
				
			}while(czasTrwania>0);
			
			
			
		}
		
		miejsceZakonczenia = miejsceNaM1 > miejsceNaM2 ? miejsceNaM1 : miejsceNaM2;
		
		return miejsceZakonczenia;
		
	}
	*/

	
	public static int generujRozwiazanieMichal (List<Zadanie> listaZadan, List<Blok> maszyna1, List<Blok> maszyna2) throws CloneNotSupportedException {
		
		maszyna1.clear();
		maszyna2.clear();
		
		int aktualnyCzasM1 = 0;
		int aktualnyCzasM2 = 0;
		int maszynaOperacji=0;		
		List<Maintenance> listaPrzerwM1 = new LinkedList<>();
		for (Maintenance maintenance : listaPrzerw) {
			if(maintenance.getMaszyna()==1) listaPrzerwM1.add(maintenance);
		}
		int indeksPrzerwyM1=0;
		Maintenance tmpPrzerwaM1 = listaPrzerwM1.get(indeksPrzerwyM1); //najbli¿sza przerwa na maszynie 1
		boolean czySaJeszczePrzerwyM1 = true;
		
		List<Maintenance> listaPrzerwM2 = new LinkedList<>();
		for (Maintenance maintenance : listaPrzerw) {
			if(maintenance.getMaszyna()==2) listaPrzerwM2.add(maintenance);
		}
		int indeksPrzerwyM2=0;
		Maintenance tmpPrzerwaM2 = listaPrzerwM2.get(indeksPrzerwyM2); //najbli¿sza przerwa na maszynie 2
		boolean czySaJeszczePrzerwyM2 = true;
//ZAK£ADAMY, ¯E JEST CO NAJMNIEJ JEDNA PRZERWA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		for (int i = 0; i < listaZadan.size(); i++) {
			
			Zadanie zadanie = listaZadan.get(i);
			Operacja operacja1 = zadanie.getOperacje()[0];
			Operacja operacja2 = zadanie.getOperacje()[1];
			
			//OPERACJA 1
			maszynaOperacji = operacja1.getMaszyna();
			//je¿eli na maszynie 1
			if(maszynaOperacji==1) {
				
				if(czySaJeszczePrzerwyM1) {
					
					if(aktualnyCzasM1<tmpPrzerwaM1.getCzasStartu()) { //je¿eli operacja zacznie siê przed najbli¿sz¹ przerw¹ i
						if(aktualnyCzasM1+operacja1.getCzasTrwania() < tmpPrzerwaM1.getCzasStartu()) {//skoñczy przed ni¹
							maszyna1.add(operacja1);
							operacja1.setCzasStartu(aktualnyCzasM1);
							operacja1.setCzasTrwaniaPraktyczny(operacja1.getCzasTrwania());
							aktualnyCzasM1+=operacja1.getCzasTrwania();
						}
						else if( aktualnyCzasM1+operacja1.getCzasTrwania()==tmpPrzerwaM1.getCzasStartu() ) {//skoñczy wtedy, kiedy przerwa siê zaczyna
							maszyna1.add(operacja1);
							operacja1.setCzasStartu(aktualnyCzasM1);
							operacja1.setCzasTrwaniaPraktyczny(operacja1.getCzasTrwania());
							aktualnyCzasM1+=operacja1.getCzasTrwania();
							
							//teraz wrzucamy jeszcze przerwê
							maszyna1.add(tmpPrzerwaM1);
							aktualnyCzasM1+=tmpPrzerwaM1.getCzasTrwania();
							
							//oraz przypisujemy nastêpn¹ najbli¿sz¹ przerwê
							indeksPrzerwyM1++;
							if(indeksPrzerwyM1<listaPrzerwM1.size()) {//je¿eli nie przeszliœmy ju¿ ca³ej listy przerw
								tmpPrzerwaM1 = listaPrzerwM1.get(indeksPrzerwyM1);//to bierzemy kolejn¹
							} else {
								tmpPrzerwaM1 = null;
								czySaJeszczePrzerwyM1 = false;//jak nie ma ju¿ przerw, zaznaczamy to zmienn¹, ¿eby dalej nie sprawdza³o przerw
							}
						}
						else if( aktualnyCzasM1+operacja1.getCzasTrwania()>tmpPrzerwaM1.getCzasStartu() ) {//przed koñcem operacji zacznie siê maintenance
							int pozostalyCzasDoWykonania = (int) Math.ceil( operacja1.getCzasTrwania()*(1+Main.kara));
							
							do {//istnieje mo¿liwoœæ, ¿e operacjê przerwie wiêcej ni¿ jeden maintenance
								int dostepnyCzasPrzedPrzerwa = tmpPrzerwaM1.getCzasStartu() - aktualnyCzasM1;
								
								Operacja operacja1klon = (Operacja)operacja1.clone();
								operacja1klon.setCzasStartu(aktualnyCzasM1);
								
								if(dostepnyCzasPrzedPrzerwa>pozostalyCzasDoWykonania) {//je¿eli operacja zmieœci siê przed przerw¹
									operacja1klon.setCzasTrwaniaPraktyczny(pozostalyCzasDoWykonania);
									maszyna1.add(operacja1klon);
									aktualnyCzasM1+=dostepnyCzasPrzedPrzerwa;
									pozostalyCzasDoWykonania-=dostepnyCzasPrzedPrzerwa;
									break;
								} else {//operacja nie zmieœci siê przed przerw¹, albo zmieœci siê idealnie, w obu przypadkach wstawiamy op i przerwê
									operacja1klon.setCzasTrwaniaPraktyczny(dostepnyCzasPrzedPrzerwa);
									maszyna1.add(operacja1klon);
									aktualnyCzasM1+=dostepnyCzasPrzedPrzerwa;
									pozostalyCzasDoWykonania -= dostepnyCzasPrzedPrzerwa;
									
									//umieszczamy przerwê
									maszyna1.add(tmpPrzerwaM1);
									aktualnyCzasM1+=tmpPrzerwaM1.getCzasTrwania();
									
									//przypisujemy nastêpn¹ najbli¿sz¹ przerwê
									indeksPrzerwyM1++;
									if(indeksPrzerwyM1<listaPrzerwM1.size()) {//je¿eli nie przeszliœmy ju¿ ca³ej listy przerw
										tmpPrzerwaM1 = listaPrzerwM1.get(indeksPrzerwyM1);//to bierzemy kolejn¹
									} else {
										tmpPrzerwaM1 = null;
										czySaJeszczePrzerwyM1 = false;//jak nie ma ju¿ przerw, zaznaczamy to zmienn¹, ¿eby dalej nie sprawdza³o przerw
									}
								}
							} while(pozostalyCzasDoWykonania>0);
							
						}
					}
					else if(aktualnyCzasM1==tmpPrzerwaM1.getCzasStartu()) {//obecny czas to pocz¹tek przerwy, prawdopodobnie wystêpuje tylko, 
																			//gdy przerwa zaczyna siê w chwili 0
						maszyna1.add(tmpPrzerwaM1);
						aktualnyCzasM1+=tmpPrzerwaM1.getCzasTrwania();
						// przypisujemy nastêpn¹ najbli¿sz¹ przerwê
						indeksPrzerwyM1++;
						if(indeksPrzerwyM1<listaPrzerwM1.size()) {//je¿eli nie przeszliœmy ju¿ ca³ej listy przerw
							tmpPrzerwaM1 = listaPrzerwM1.get(indeksPrzerwyM1);//to bierzemy kolejn¹
						} else {
							tmpPrzerwaM1 = null;
							czySaJeszczePrzerwyM1 = false;//jak nie ma ju¿ przerw, zaznaczamy to zmienn¹, ¿eby dalej nie sprawdza³o przerw
						}
					}
					
				} 
				else {	//NIE MA JU¯ PRZERW, WRZUCAMY ZADANKA NA CZILU
					maszyna1.add(operacja1);
					operacja1.setCzasStartu(aktualnyCzasM1);
					operacja1.setCzasTrwaniaPraktyczny(operacja1.getCzasTrwania());
					aktualnyCzasM1+=operacja1.getCzasTrwania();
				}
			
			//je¿eli na maszynie 2
			} 
			else if (maszynaOperacji==2) {
				
				if(czySaJeszczePrzerwyM2) {
					
					if(aktualnyCzasM2<tmpPrzerwaM2.getCzasStartu()) { //je¿eli operacja zacznie siê przed najbli¿sz¹ przerw¹ i
						
						if(aktualnyCzasM2+operacja1.getCzasTrwania() < tmpPrzerwaM2.getCzasStartu()) {//skoñczy przed ni¹
							maszyna2.add(operacja1);
							operacja1.setCzasStartu(aktualnyCzasM2);
							operacja1.setCzasTrwaniaPraktyczny(operacja1.getCzasTrwania());
							aktualnyCzasM2+=operacja1.getCzasTrwania();
						}
						else if( aktualnyCzasM2+operacja1.getCzasTrwania()==tmpPrzerwaM2.getCzasStartu() ) {//skoñczy wtedy, kiedy przerwa siê zaczyna
							maszyna2.add(operacja1);
							operacja1.setCzasStartu(aktualnyCzasM2);
							operacja1.setCzasTrwaniaPraktyczny(operacja1.getCzasTrwania());
							aktualnyCzasM2+=operacja1.getCzasTrwania();
							
							//teraz wrzucamy jeszcze przerwê
							maszyna2.add(tmpPrzerwaM2);
							aktualnyCzasM2+=tmpPrzerwaM2.getCzasTrwania();
							
							//oraz przypisujemy nastêpn¹ najbli¿sz¹ przerwê
							indeksPrzerwyM2++;
							if(indeksPrzerwyM2<listaPrzerwM2.size()) {//je¿eli nie przeszliœmy ju¿ ca³ej listy przerw
								tmpPrzerwaM2 = listaPrzerwM2.get(indeksPrzerwyM2);//to bierzemy kolejn¹
							} else {
								tmpPrzerwaM2 = null;
								czySaJeszczePrzerwyM2 = false;//jak nie ma ju¿ przerw, zaznaczamy to zmienn¹, ¿eby dalej nie sprawdza³o przerw
							}
						}
						else if( aktualnyCzasM2+operacja1.getCzasTrwania()>tmpPrzerwaM2.getCzasStartu() ) {//przed koñcem operacji zacznie siê maintenance
							int pozostalyCzasDoWykonania = (int) Math.ceil( operacja1.getCzasTrwania()*(1+Main.kara));
							
							do {//istnieje mo¿liwoœæ, ¿e operacjê przerwie wiêcej ni¿ jeden maintenance
								int dostepnyCzasPrzedPrzerwa = tmpPrzerwaM2.getCzasStartu() - aktualnyCzasM2;
								
								Operacja operacja1klon = (Operacja)operacja1.clone();
								operacja1klon.setCzasStartu(aktualnyCzasM2);
								
								if(dostepnyCzasPrzedPrzerwa>pozostalyCzasDoWykonania) {//je¿eli operacja zmieœci siê przed przerw¹
									operacja1klon.setCzasTrwaniaPraktyczny(pozostalyCzasDoWykonania);
									maszyna2.add(operacja1klon);
									aktualnyCzasM2+=dostepnyCzasPrzedPrzerwa;
									pozostalyCzasDoWykonania-=dostepnyCzasPrzedPrzerwa;
									break;
								} else {//operacja nie zmieœci siê przed przerw¹, albo zmieœci siê idealnie, w obu przypadkach wstawiamy op. i przerwê
									operacja1klon.setCzasTrwaniaPraktyczny(dostepnyCzasPrzedPrzerwa);
									maszyna2.add(operacja1klon);
									aktualnyCzasM2+=dostepnyCzasPrzedPrzerwa;
									pozostalyCzasDoWykonania -= dostepnyCzasPrzedPrzerwa;
									
									//umieszczamy przerwê
									maszyna2.add(tmpPrzerwaM2);
									aktualnyCzasM2+=tmpPrzerwaM2.getCzasTrwania();
									
									//przypisujemy nastêpn¹ najbli¿sz¹ przerwê
									indeksPrzerwyM2++;
									if(indeksPrzerwyM2<listaPrzerwM2.size()) {//je¿eli nie przeszliœmy ju¿ ca³ej listy przerw
										tmpPrzerwaM2 = listaPrzerwM2.get(indeksPrzerwyM2);//to bierzemy kolejn¹
									} else {
										tmpPrzerwaM2 = null;
										czySaJeszczePrzerwyM2 = false;//jak nie ma ju¿ przerw, zaznaczamy to zmienn¹, ¿eby dalej nie sprawdza³o przerw
									}
								}
							} while(pozostalyCzasDoWykonania>0);
							
						}
					}
					else if(aktualnyCzasM2==tmpPrzerwaM2.getCzasStartu()) {//obecny czas to pocz¹tek najbli¿szej przerwy, prawdopodobnie wystêpuje tylko, 
																			//gdy przerwa zaczyna siê w chwili 0
						maszyna2.add(tmpPrzerwaM2);
						aktualnyCzasM2+=tmpPrzerwaM2.getCzasTrwania();
						// przypisujemy nastêpn¹ najbli¿sz¹ przerwê
						indeksPrzerwyM2++;
						if(indeksPrzerwyM2<listaPrzerwM2.size()) {//je¿eli nie przeszliœmy ju¿ ca³ej listy przerw
							tmpPrzerwaM2 = listaPrzerwM2.get(indeksPrzerwyM2);//to bierzemy kolejn¹
						} else {
							tmpPrzerwaM2 = null;
							czySaJeszczePrzerwyM2 = false;//jak nie ma ju¿ przerw, zaznaczamy to zmienn¹, ¿eby dalej nie sprawdza³o przerw
						}
					}
					
				}
				else {	//NIE MA JU¯ PRZERW, WRZUCAMY ZADANKA NA CZILU
					maszyna2.add(operacja1);
					operacja1.setCzasStartu(aktualnyCzasM2);
					operacja1.setCzasTrwaniaPraktyczny(operacja1.getCzasTrwania());
					aktualnyCzasM2+=operacja1.getCzasTrwania();
				}
				
			}
			
			//OPERACJA 2
			
			
		}
		
		
		
		
		return 1;
	}
	
	/*
	public static int generujRozwiazanie2(List<Zadanie> listaZadan, List<Blok> maszyna1, List<Blok> maszyna2) throws CloneNotSupportedException {
		maszyna1.clear();
		maszyna2.clear();
		
		int czasM1 = 0;
		int czasM2 = 0;
		Maintenance tmpPrzerwa;
		
		
		for(int i=0;i<listaZadan.size();i++) {
			
			int licznikPrzerwPrzerywajacychOperacje = 0;
			Zadanie tmp = listaZadan.get(i);
			
			//najpierw pierwsza operacja zadania
			int czasTrwania1 = tmp.getOperacje()[0].getCzasTrwania();
			int maszynaOp1 = tmp.getOperacje()[0].getMaszyna();	
			int czasTrwania2 = tmp.getOperacje()[1].getCzasTrwania();
			int maszynaOp2 = tmp.getOperacje()[1].getCzasTrwania();
			if(maszynaOp1==1) {
				do {
				if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM1,czasTrwania1,maszynaOp1))==null) {//jesli nie ma na drodze przerwy
					tmp.getOperacje()[0].setCzasStartu(czasM1);
					tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania1);
					maszyna1.add(tmp.getOperacje()[0]);
					czasM1+=czasTrwania1;
					czasTrwania1=0;		
				}else{//jesli jest na drodze przerwa
				if(licznikPrzerwPrzerywajacychOperacje==0){
					czasTrwania1 = GeneratorInstancji.sufit((double)czasTrwania1 + czasTrwania1*Main.kara);
					int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM1;
					Zadanie tmp2 = (Zadanie) tmp.clone();
					tmp2.getOperacje()[0].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
					tmp2.getOperacje()[0].setCzasStartu(czasM1);
					maszyna1.add(tmp2.getOperacje()[0]);
					tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania1 - czasDoPrzerwy);
					czasM1+=czasDoPrzerwy;
					czasTrwania1-=czasDoPrzerwy;
					licznikPrzerwPrzerywajacychOperacje++;
					
					maszyna1.add(tmpPrzerwa);
					czasM1+=tmpPrzerwa.getCzasTrwania();
					
				}else{
					int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM1;
					Zadanie tmp2 = (Zadanie) tmp.clone();
					tmp2.getOperacje()[0].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
					tmp2.getOperacje()[0].setCzasStartu(czasM1);
					maszyna1.add(tmp2.getOperacje()[0]);
					tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania1 - czasDoPrzerwy);
					czasM1+=czasDoPrzerwy;
					czasTrwania1-=czasDoPrzerwy;
					licznikPrzerwPrzerywajacychOperacje++;
					maszyna1.add(tmpPrzerwa);
					czasM1+=tmpPrzerwa.getCzasTrwania();
				}

			}
		}while(czasTrwania1 > 0);
				
				 if (i == 0) {
		                Idle idle = new Idle(czasM1, 2);
		                maszyna2.add(idle);
		                czasM2 = czasM1;
		            }	
				 
				 if (czasM1 - czasM2 > 0) {
		                Idle idle = new Idle(czasM1-czasM2, 2);
		                maszyna2.add(idle);
		                czasM2 += czasM1 - czasM2;
		            }
				 
				 do {
						if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM2,czasTrwania2,maszynaOp2))==null) {//jesli nie ma na drodze przerwy
							tmp.getOperacje()[1].setCzasStartu(czasM2);
							tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania2);
							maszyna2.add(tmp.getOperacje()[1]);
							czasM2+=czasTrwania2;
							czasTrwania2=0;		
						}else{//jesli jest na drodze przerwa
						if(licznikPrzerwPrzerywajacychOperacje==0){
							czasTrwania2 = GeneratorInstancji.sufit((double)czasTrwania2 + czasTrwania2*Main.kara);
							int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM2;
							Zadanie tmp2 = (Zadanie) tmp.clone();
							tmp2.getOperacje()[1].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
							tmp2.getOperacje()[1].setCzasStartu(czasM2);
							maszyna2.add(tmp2.getOperacje()[1]);
							tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania2 - czasDoPrzerwy);
							czasM2+=czasDoPrzerwy;
							czasTrwania2-=czasDoPrzerwy;
							licznikPrzerwPrzerywajacychOperacje++;
							
							maszyna2.add(tmpPrzerwa);
							czasM2+=tmpPrzerwa.getCzasTrwania();
							
						}else{
							int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM2;
							Zadanie tmp2 = (Zadanie) tmp.clone();
							tmp2.getOperacje()[1].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
							tmp2.getOperacje()[1].setCzasStartu(czasM2);
							maszyna2.add(tmp2.getOperacje()[1]);
							tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania2 - czasDoPrzerwy);
							czasM2+=czasDoPrzerwy;
							czasTrwania2-=czasDoPrzerwy;
							licznikPrzerwPrzerywajacychOperacje++;
							maszyna2.add(tmpPrzerwa);
							czasM2+=tmpPrzerwa.getCzasTrwania();
						}
					}
				}while(czasTrwania2 > 0);	 

		}else {//jesli maszyna operacji1 jest rowna 2
			do {//operacja 1 na maszyne2
				if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM2,czasTrwania1,maszynaOp1))==null) {//jesli nie ma na drodze przerwy
					tmp.getOperacje()[0].setCzasStartu(czasM2);
					tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania1);
					maszyna2.add(tmp.getOperacje()[0]);
					czasM2+=czasTrwania1;
					czasTrwania1=0;		
				}else{//jesli jest na drodze przerwa
				if(licznikPrzerwPrzerywajacychOperacje==0){
					czasTrwania1 = GeneratorInstancji.sufit((double)czasTrwania1 + czasTrwania1*Main.kara);
					int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM2;
					Zadanie tmp2 = (Zadanie) tmp.clone();
					tmp2.getOperacje()[0].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
					tmp2.getOperacje()[0].setCzasStartu(czasM2);
					maszyna2.add(tmp2.getOperacje()[0]);
					tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania1 - czasDoPrzerwy);
					czasM2+=czasDoPrzerwy;
					czasTrwania1-=czasDoPrzerwy;
					licznikPrzerwPrzerywajacychOperacje++;
					
					maszyna2.add(tmpPrzerwa);
					czasM2+=tmpPrzerwa.getCzasTrwania();
					
				}else{
					int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM2;
					Zadanie tmp2 = (Zadanie) tmp.clone();
					tmp2.getOperacje()[0].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
					tmp2.getOperacje()[0].setCzasStartu(czasM2);
					maszyna2.add(tmp2.getOperacje()[0]);
					tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania1 - czasDoPrzerwy);
					czasM2+=czasDoPrzerwy;
					czasTrwania1-=czasDoPrzerwy;
					licznikPrzerwPrzerywajacychOperacje++;
					maszyna2.add(tmpPrzerwa);
					czasM2+=tmpPrzerwa.getCzasTrwania();
				}
			}
		}while(czasTrwania1 > 0);
		
			 if (i == 0) {
	                Idle idle = new Idle(czasM2, 1);
	                maszyna1.add(idle);
	                czasM1 = czasM2;
	            }	
			 
			 if (czasM2 - czasM1 > 0) {
	                Idle idle = new Idle(czasM2-czasM1, 1);
	                maszyna1.add(idle);
	                czasM1 += czasM2 - czasM1;
	            }
			 
			 do {//operacja 2 na maszyne 1
					if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM1,czasTrwania2,maszynaOp2))==null) {//jesli nie ma na drodze przerwy
						tmp.getOperacje()[1].setCzasStartu(czasM1);
						tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania2);
						maszyna1.add(tmp.getOperacje()[1]);
						czasM1+=czasTrwania2;
						czasTrwania2=0;		
					}else{//jesli jest na drodze przerwa
					if(licznikPrzerwPrzerywajacychOperacje==0){
						czasTrwania2 = GeneratorInstancji.sufit((double)czasTrwania2 + czasTrwania2*Main.kara);
						int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM1;
						Zadanie tmp2 = (Zadanie) tmp.clone();
						tmp2.getOperacje()[1].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
						tmp2.getOperacje()[1].setCzasStartu(czasM1);
						maszyna1.add(tmp2.getOperacje()[1]);
						tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania2 - czasDoPrzerwy);
						czasM1+=czasDoPrzerwy;
						czasTrwania2-=czasDoPrzerwy;
						licznikPrzerwPrzerywajacychOperacje++;
						
						maszyna1.add(tmpPrzerwa);
						czasM1+=tmpPrzerwa.getCzasTrwania();
						
					}else{
						int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM1;
						Zadanie tmp2 = (Zadanie) tmp.clone();
						tmp2.getOperacje()[1].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
						tmp2.getOperacje()[1].setCzasStartu(czasM1);
						maszyna1.add(tmp2.getOperacje()[1]);
						tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania2 - czasDoPrzerwy);
						czasM1+=czasDoPrzerwy;
						czasTrwania2-=czasDoPrzerwy;
						licznikPrzerwPrzerywajacychOperacje++;
						maszyna1.add(tmpPrzerwa);
						czasM1+=tmpPrzerwa.getCzasTrwania();
					}

				}
			}while(czasTrwania2 > 0);
		
			
			
		}
			
		}
		int miejsceZakonczenia = czasM1 > czasM2 ? czasM1 : czasM2;
		
		return miejsceZakonczenia;
	}
	*/
 
	public static int generujRozwiazanie2(List<Zadanie> listaZadan, List<Blok> maszyna1, List<Blok> maszyna2) throws CloneNotSupportedException {
		maszyna1.clear();
		maszyna2.clear();
		
		int czasM1 = 0;
		int czasM2 = 0;
		Maintenance tmpPrzerwa;
		
		
		for(int i=0;i<listaZadan.size();i++) {
			
			int licznikPrzerwPrzerywajacychOperacje = 0;
			Zadanie tmp = listaZadan.get(i);
			
			//najpierw pierwsza operacja zadania
			int czasTrwania1 = tmp.getOperacje()[0].getCzasTrwania();
			int maszynaOp1 = tmp.getOperacje()[0].getMaszyna();	
			int czasTrwania2 = tmp.getOperacje()[1].getCzasTrwania();
			int maszynaOp2 = tmp.getOperacje()[1].getMaszyna();
			if(maszynaOp1==1) {
				
				while ((tmpPrzerwa = sprawdzCzyNieMaPrzerwy(czasM1, 1, 1)) != null) {
	                maszyna1.add(tmpPrzerwa);
	                czasM1 += tmpPrzerwa.getCzasTrwania();
	            }
				
				do {
				if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM1,czasTrwania1,maszynaOp1))==null) {//jesli nie ma na drodze przerwy
					tmp.getOperacje()[0].setCzasStartu(czasM1);
					tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania1);
					maszyna1.add(tmp.getOperacje()[0]);
					czasM1+=czasTrwania1;
					czasTrwania1=0;		
				}else{//jesli jest na drodze przerwa
				if(licznikPrzerwPrzerywajacychOperacje==0){
					czasTrwania1 = GeneratorInstancji.sufit((double)czasTrwania1 + czasTrwania1*Main.kara);
					int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM1;
					Zadanie tmp2 = (Zadanie) tmp.clone();
					tmp2.getOperacje()[0].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
					tmp2.getOperacje()[0].setCzasStartu(czasM1);
					maszyna1.add(tmp2.getOperacje()[0]);
					tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania1 - czasDoPrzerwy);
					czasM1+=czasDoPrzerwy;
					czasTrwania1-=czasDoPrzerwy;
					licznikPrzerwPrzerywajacychOperacje++;
					
					maszyna1.add(tmpPrzerwa);
					czasM1+=tmpPrzerwa.getCzasTrwania();
					
				}else{
					int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM1;
					Zadanie tmp2 = (Zadanie) tmp.clone();
					tmp2.getOperacje()[0].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
					tmp2.getOperacje()[0].setCzasStartu(czasM1);
					maszyna1.add(tmp2.getOperacje()[0]);
					tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania1 - czasDoPrzerwy);
					czasM1+=czasDoPrzerwy;
					czasTrwania1-=czasDoPrzerwy;
					licznikPrzerwPrzerywajacychOperacje++;
					maszyna1.add(tmpPrzerwa);
					czasM1+=tmpPrzerwa.getCzasTrwania();
				}

			}
		}while(czasTrwania1 > 0);
				
				if (i == 0) {
	                do {
	   				 if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM2,czasM1-czasM2,2))==null){
	   				 		Idle idle = new Idle(czasM1-czasM2, 2);
	   		                maszyna2.add(idle);
	   		                czasM2 += idle.getCzasTrwania();
	   				 	}else {
	   				 		Idle idle = new Idle(tmpPrzerwa.getCzasStartu() - czasM2,2);
	   				 		maszyna2.add(idle);
	   				 		maszyna2.add(tmpPrzerwa);
	   				 		czasM2+=idle.getCzasTrwania()+tmpPrzerwa.getCzasTrwania();			 		
	   				 	
	   				 	}
	   				 }while(czasM2<czasM1);
	                
	            }	
			 
			 if (czasM1 - czasM2 > 0) {
				 do {
				 if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM2,czasM1-czasM2,2))==null){
				 		Idle idle = new Idle(czasM1-czasM2, 2);
		                maszyna2.add(idle);
		                czasM2 += idle.getCzasTrwania();
				 	}else {
				 		Idle idle = new Idle(tmpPrzerwa.getCzasStartu() - czasM2,2);
				 		maszyna2.add(idle);
				 		maszyna2.add(tmpPrzerwa);
				 		czasM2+=idle.getCzasTrwania()+tmpPrzerwa.getCzasTrwania();			 		
				 	
				 	}
				 }while(czasM2<czasM1);
	            }
				 
				 
				 while ((tmpPrzerwa = sprawdzCzyNieMaPrzerwy(czasM2, 1, 2)) != null) {
		                maszyna2.add(tmpPrzerwa);
		                czasM2 += tmpPrzerwa.getCzasTrwania();
		            }
				 
				 do {
						if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM2,czasTrwania2,maszynaOp2))==null) {//jesli nie ma na drodze przerwy
							tmp.getOperacje()[1].setCzasStartu(czasM2);
							tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania2);
							maszyna2.add(tmp.getOperacje()[1]);
							czasM2+=czasTrwania2;
							czasTrwania2=0;		
						}else{//jesli jest na drodze przerwa
						if(licznikPrzerwPrzerywajacychOperacje==0){
							czasTrwania2 = GeneratorInstancji.sufit((double)czasTrwania2 + czasTrwania2*Main.kara);
							int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM2;
							Zadanie tmp2 = (Zadanie) tmp.clone();
							tmp2.getOperacje()[1].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
							tmp2.getOperacje()[1].setCzasStartu(czasM2);
							maszyna2.add(tmp2.getOperacje()[1]);
							tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania2 - czasDoPrzerwy);
							czasM2+=czasDoPrzerwy;
							czasTrwania2-=czasDoPrzerwy;
							licznikPrzerwPrzerywajacychOperacje++;
							
							maszyna2.add(tmpPrzerwa);
							czasM2+=tmpPrzerwa.getCzasTrwania();
							
						}else{
							int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM2;
							Zadanie tmp2 = (Zadanie) tmp.clone();
							tmp2.getOperacje()[1].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
							tmp2.getOperacje()[1].setCzasStartu(czasM2);
							maszyna2.add(tmp2.getOperacje()[1]);
							tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania2 - czasDoPrzerwy);
							czasM2+=czasDoPrzerwy;
							czasTrwania2-=czasDoPrzerwy;
							licznikPrzerwPrzerywajacychOperacje++;
							maszyna2.add(tmpPrzerwa);
							czasM2+=tmpPrzerwa.getCzasTrwania();
						}
					}
				}while(czasTrwania2 > 0);	 

		}else {//jesli maszyna operacji1 jest rowna 2
			
			while ((tmpPrzerwa = sprawdzCzyNieMaPrzerwy(czasM2, 1, 2)) != null) {
                maszyna2.add(tmpPrzerwa);
                czasM2 += tmpPrzerwa.getCzasTrwania();
            }
			
			do {//operacja 1 na maszyne2
				if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM2,czasTrwania1,maszynaOp1))==null) {//jesli nie ma na drodze przerwy
					tmp.getOperacje()[0].setCzasStartu(czasM2);
					tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania1);
					maszyna2.add(tmp.getOperacje()[0]);
					czasM2+=czasTrwania1;
					czasTrwania1=0;		
				}else{//jesli jest na drodze przerwa
				if(licznikPrzerwPrzerywajacychOperacje==0){
					czasTrwania1 = GeneratorInstancji.sufit((double)czasTrwania1 + czasTrwania1*Main.kara);
					int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM2;
					Zadanie tmp2 = (Zadanie) tmp.clone();
					tmp2.getOperacje()[0].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
					tmp2.getOperacje()[0].setCzasStartu(czasM2);
					maszyna2.add(tmp2.getOperacje()[0]);
					tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania1 - czasDoPrzerwy);
					czasM2+=czasDoPrzerwy;
					czasTrwania1-=czasDoPrzerwy;
					licznikPrzerwPrzerywajacychOperacje++;
					
					maszyna2.add(tmpPrzerwa);
					czasM2+=tmpPrzerwa.getCzasTrwania();
					
				}else{
					int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM2;
					Zadanie tmp2 = (Zadanie) tmp.clone();
					tmp2.getOperacje()[0].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
					tmp2.getOperacje()[0].setCzasStartu(czasM2);
					maszyna2.add(tmp2.getOperacje()[0]);
					tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania1 - czasDoPrzerwy);
					czasM2+=czasDoPrzerwy;
					czasTrwania1-=czasDoPrzerwy;
					licznikPrzerwPrzerywajacychOperacje++;
					maszyna2.add(tmpPrzerwa);
					czasM2+=tmpPrzerwa.getCzasTrwania();
				}
			}
		}while(czasTrwania1 > 0);
		
			 if (i == 0) {
	                do {
	   				 if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM1,czasM2-czasM1,1))==null){
	   				 		Idle idle = new Idle(czasM2-czasM1, 1);
	   		                maszyna1.add(idle);
	   		                czasM1 += idle.getCzasTrwania();
	   				 	}else {
	   				 		Idle idle = new Idle(tmpPrzerwa.getCzasStartu() - czasM1,1);
	   				 		maszyna1.add(idle);
	   				 		maszyna1.add(tmpPrzerwa);
	   				 		czasM1+=idle.getCzasTrwania()+tmpPrzerwa.getCzasTrwania();			 		
	   				 	
	   				 	}
	   				 }while(czasM1<czasM2);
	                
	            }	
			 
			 if (czasM2 - czasM1 > 0) {
				 do {
				 if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM1,czasM2-czasM1,1))==null){
				 		Idle idle = new Idle(czasM2-czasM1, 1);
		                maszyna1.add(idle);
		                czasM1 += czasM2 - czasM1;
				 	}else {
				 		Idle idle = new Idle(tmpPrzerwa.getCzasStartu() - czasM1,1);
				 		maszyna1.add(idle);
				 		maszyna1.add(tmpPrzerwa);
				 		czasM1+=idle.getCzasTrwania()+tmpPrzerwa.getCzasTrwania();			 		
				 	
				 	}
				 }while(czasM1<czasM2);
	            }
			 
			 while ((tmpPrzerwa = sprawdzCzyNieMaPrzerwy(czasM1, 1, 1)) != null) {
	                maszyna1.add(tmpPrzerwa);
	                czasM1 += tmpPrzerwa.getCzasTrwania();
	            }
			 
			 do {//operacja 2 na maszyne 1
					if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM1,czasTrwania2,maszynaOp2))==null) {//jesli nie ma na drodze przerwy
						tmp.getOperacje()[1].setCzasStartu(czasM1);
						tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania2);
						maszyna1.add(tmp.getOperacje()[1]);
						czasM1+=czasTrwania2;
						czasTrwania2=0;		
					}else{//jesli jest na drodze przerwa
					if(licznikPrzerwPrzerywajacychOperacje==0){
						czasTrwania2 = GeneratorInstancji.sufit((double)czasTrwania2 + czasTrwania2*Main.kara);
						int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM1;
						Zadanie tmp2 = (Zadanie) tmp.clone();
						tmp2.getOperacje()[1].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
						tmp2.getOperacje()[1].setCzasStartu(czasM1);
						maszyna1.add(tmp2.getOperacje()[1]);
						tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania2 - czasDoPrzerwy);
						czasM1+=czasDoPrzerwy;
						czasTrwania2-=czasDoPrzerwy;
						licznikPrzerwPrzerywajacychOperacje++;
						
						maszyna1.add(tmpPrzerwa);
						czasM1+=tmpPrzerwa.getCzasTrwania();
						
					}else{
						int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM1;
						Zadanie tmp2 = (Zadanie) tmp.clone();
						tmp2.getOperacje()[1].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
						tmp2.getOperacje()[1].setCzasStartu(czasM1);
						maszyna1.add(tmp2.getOperacje()[1]);
						tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania2 - czasDoPrzerwy);
						czasM1+=czasDoPrzerwy;
						czasTrwania2-=czasDoPrzerwy;
						licznikPrzerwPrzerywajacychOperacje++;
						maszyna1.add(tmpPrzerwa);
						czasM1+=tmpPrzerwa.getCzasTrwania();
					}

				}
			}while(czasTrwania2 > 0);
		
			
			
		}
			
		}
		int miejsceZakonczenia = czasM1 > czasM2 ? czasM1 : czasM2;
		
		return miejsceZakonczenia;
	}
	
	
	public static Maintenance sprawdzCzyNieMaPrzerwy(int start, int czasTrwania, int maszyna) {
	        for (int i = 0; i < listaPrzerw.size(); i++) {
	            Maintenance przerwa = listaPrzerw.get(i);
	            if (przerwa.getMaszyna() != maszyna) continue;

	            
	            if ((start >= przerwa.getCzasStartu() && start < przerwa.getCzasStartu()+przerwa.getCzasTrwania()) ||
	                    (przerwa.getCzasStartu() >= start && przerwa.getCzasStartu() < start + czasTrwania)) {
	                return przerwa;
	            }
	        }
	        return null;
	    }


	public static Populacja tworzPierwszaPopulacje() {
		Populacja populacja = new Populacja();
		
		for(int i=0;i<wielkoscPopulacji;i++) {
			List<Zadanie> tmpListaZadan = new LinkedList<>();
			List<Blok> tmpMaszyna1 = new LinkedList<>();
			List<Blok> tmpMaszyna2 = new LinkedList<>();
			for(int j=0;j<listaZadan.size();j++) {
				try {
					tmpListaZadan.add((Zadanie) listaZadan.get(j).clone());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
			Collections.shuffle(tmpListaZadan);
			
			int wynik = 0;
			
			try {
				wynik = generujRozwiazanie2(tmpListaZadan, tmpMaszyna1, tmpMaszyna2);
				//osobnik = generujRozwiazanie(tmpListaZadan);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			} 

			OsobnikPopulacji osobnik = new OsobnikPopulacji(tmpListaZadan,tmpMaszyna1,tmpMaszyna2,wynik);
			populacja.getListaOsobnikow().add(osobnik);
		}
		return populacja;
	}
	
	
	public static List<Zadanie> getListaZadan() {
		return listaZadan;
	}


	public static void setListaZadan(List<Zadanie> listaZadan) {
		Algorytm.listaZadan = listaZadan;
	}


	public static List<Maintenance> getListaPrzerw() {
		return listaPrzerw;
	}


	public static void setListaPrzerw(List<Maintenance> listaPrzerw) {
		Algorytm.listaPrzerw = listaPrzerw;
	}

	public static int getWielkoscPopulacji() {
		return wielkoscPopulacji;
	}

	public static void setWielkoscPopulacji(int wielkoscPopulacji) {
		Algorytm.wielkoscPopulacji = wielkoscPopulacji;
	}

	public static double getWspolczynnikMutacji() {
		return wspolczynnikMutacji;
	}

	public static void setWspolczynnikMutacji(double wspolczynnikMutacji) {
		Algorytm.wspolczynnikMutacji = wspolczynnikMutacji;
	}



	public static int getLiczbaKandydatowDoTurnieju() {
		return liczbaKandydatowDoTurnieju;
	}


	public static void setLiczbaKandydatowDoTurnieju(int liczbaKandydatowDoTurnieju) {
		Algorytm.liczbaKandydatowDoTurnieju = liczbaKandydatowDoTurnieju;
	}


	public static int getLiczbaZwyciezcowTurniejow() {
		return liczbaZwyciezcowTurniejow;
	}


	public static void setLiczbaZwyciezcowTurniejow(int liczbaZwyciezcowTurniejow) {
		Algorytm.liczbaZwyciezcowTurniejow = liczbaZwyciezcowTurniejow;
	}


	public static double getWspolczynnikPodzialu() {
		return wspolczynnikPodzialu;
	}


	public static void setWspolczynnikPodzialu(double wspolczynnikPodzialu) {
		Algorytm.wspolczynnikPodzialu = wspolczynnikPodzialu;
	}
}
