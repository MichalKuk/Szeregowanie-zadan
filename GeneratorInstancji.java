package okProjekt;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class GeneratorInstancji {

	public static Random r = new Random();
	public static  int n = 100; //liczba zadan
	public static  int k =sufit((double)n*0.5); //liczba przerw na jednej maszynie
	//public static final int k = 100;
	public static int maxCzasTrwania = 20;
	public static int maxCzasTrwaniaPrzerw = 20;
	
	public static Zadanie[] tablicaZadan;// = new Zadanie[n];
	public static Maintenance[][] tablicaPrzerw;// = new Maintenance[2][k];
	
	private static int czasMaszyna1 = 0;
	private static int czasMaszyna2 = 0;
	
	public static void generujInstancje(int numerInstancji) throws FileNotFoundException {
		
		tablicaZadan = new Zadanie[n];
		tablicaPrzerw = new Maintenance[2][k];
		
		String plik = "INSTANCJE/xxx"+(numerInstancji)+".instancja";
		//  /instancjeLPrzerw50%/instancjaLPrzerw50%_"+(numerInstancji)+".instancja";
		BufferedOutputStream instancjaOut = new BufferedOutputStream(new FileOutputStream(plik));
		PrintWriter x = new PrintWriter(new OutputStreamWriter(instancjaOut));
		x.write("**** "+numerInstancji+" ****\n");
		x.write(n+"\n");
		
		generujZadania();
		generujMaintenance1();
		generujMaintenance2();
		
		for(int i=0;i<n;i++) {
			Operacja[] tmp = new Operacja[2];
			tmp = tablicaZadan[i].getOperacje();
			x.write(tmp[0].getCzasTrwania()+";"+tmp[1].getCzasTrwania()+";"+tmp[0].getMaszyna()+";"+tmp[1].getMaszyna()+"\n");
			tmp = null;
		}
		for(int i=0;i<k;i++) {
			x.write((i+1)+";"+tablicaPrzerw[0][i].getMaszyna()+";"+tablicaPrzerw[0][i].getCzasTrwania()+";"+tablicaPrzerw[0][i].getCzasStartu()+"\n");
		}
		for(int i=0;i<k;i++) {
			x.write((i+1+k)+";"+tablicaPrzerw[1][i].getMaszyna()+";"+tablicaPrzerw[1][i].getCzasTrwania()+";"+tablicaPrzerw[1][i].getCzasStartu()+"\n");
		}
		
		x.write("*** EOF ***");
		x.close();
		//System.out.println("wygenerowano");
		czasMaszyna1=0;
		czasMaszyna2=0;
		
	}
	
	
//	public static void generujZadania() {
//		for(int i=0;i<n;i++) {
//			Zadanie z = new Zadanie(i);
//			z.getOperacje()[0].setCzasTrwania(r.nextInt(maxCzasTrwania)+1);
//			z.getOperacje()[0].setCzasTrwaniaPraktyczny(z.getOperacje()[0].getCzasTrwania());
//			z.getOperacje()[1].setCzasTrwania(r.nextInt(maxCzasTrwania)+1);
//			z.getOperacje()[1].setCzasTrwaniaPraktyczny(z.getOperacje()[1].getCzasTrwania());
//			
//			tablicaZadan[i]=z;
//			
//			
//		}
//		int dlugosc = tablicaZadan.length;
//		for(int i=0;i<(dlugosc-(dlugosc/5))/2;i++) {
//			tablicaZadan[i].getOperacje()[0].setMaszyna(1);
//			tablicaZadan[i].getOperacje()[1].setMaszyna(2);
//			tablicaZadan[i+ (dlugosc-(dlugosc/5))/2].getOperacje()[0].setMaszyna(2);
//			tablicaZadan[i+ (dlugosc-(dlugosc/5))/2].getOperacje()[1].setMaszyna(1);
//		}
//		for(int i=0;i<dlugosc;i++) {
//			if(tablicaZadan[i].getOperacje()[0].getMaszyna()==0) {
//				tablicaZadan[i].getOperacje()[0].setMaszyna(r.nextInt(2)+1);
//				if(tablicaZadan[i].getOperacje()[0].getMaszyna()==1) {
//					tablicaZadan[i].getOperacje()[1].setMaszyna(2);
//				}else {
//					tablicaZadan[i].getOperacje()[1].setMaszyna(1);
//				}
//			}
//		}
//		
//		for(int i=0;i<n;i++) {
//			if(tablicaZadan[i].getOperacje()[0].getMaszyna()==1) {
//				czasMaszyna1+=tablicaZadan[i].getOperacje()[0].getCzasTrwania();
//				czasMaszyna2+=tablicaZadan[i].getOperacje()[1].getCzasTrwania();
//			}else {
//				czasMaszyna2+=tablicaZadan[i].getOperacje()[0].getCzasTrwania();
//				czasMaszyna1+=tablicaZadan[i].getOperacje()[1].getCzasTrwania();
//			}
//		}
//		
//		
//	}
	
	public static void generujZadania() {
		for(int i=0;i<n;i++) {
			Zadanie z = new Zadanie(i);
			z.getOperacje()[0].setCzasTrwania(r.nextInt(maxCzasTrwania)+1);
			z.getOperacje()[0].setCzasTrwaniaPraktyczny(z.getOperacje()[0].getCzasTrwania());
			z.getOperacje()[1].setCzasTrwania(r.nextInt(maxCzasTrwania)+1);
			z.getOperacje()[1].setCzasTrwaniaPraktyczny(z.getOperacje()[1].getCzasTrwania());
			
			tablicaZadan[i]=z;
			
			z.getOperacje()[0].setMaszyna(r.nextInt(2)+1);
			if(z.getOperacje()[0].getMaszyna()==1) {
				z.getOperacje()[1].setMaszyna(2);
			}else {
				z.getOperacje()[1].setMaszyna(1);
			}
			

		}
		for(int i=0;i<n;i++) {
			if(tablicaZadan[i].getOperacje()[0].getMaszyna()==1) {
				czasMaszyna1+=tablicaZadan[i].getOperacje()[0].getCzasTrwania();
				czasMaszyna2+=tablicaZadan[i].getOperacje()[1].getCzasTrwania();
			}else {
				czasMaszyna2+=tablicaZadan[i].getOperacje()[0].getCzasTrwania();
				czasMaszyna1+=tablicaZadan[i].getOperacje()[1].getCzasTrwania();
			}
		}
	}
	
	static int[][] tabPrzedzialTrwaniaMaintenencow1 = new int[k][2]; //k nr maintenance, k[0] czas startu, k[1], czas zakonczenia
	static int[][] tabPrzedzialTrwaniaMaintenencow2 = new int[k][2];
	
	public static void generujMaintenance1() {
		int czasStartu = r.nextInt(czasMaszyna1+(k*maxCzasTrwaniaPrzerw/4));
		int czasTrwania = r.nextInt(maxCzasTrwaniaPrzerw)+1;
		Maintenance m0 = new Maintenance(czasStartu,czasTrwania,1);
		//Maintenance m1 = new Maintenance(losowaZPrzedzialu(0,60),losowaZPrzedzialu(1,20),true);
		tablicaPrzerw[0][0]=m0;//losowy pierwszy maintenance do maszyny 0
		tabPrzedzialTrwaniaMaintenencow1[0][0] = m0.getCzasStartu(); //czas startu
		tabPrzedzialTrwaniaMaintenencow1[0][1] = m0.getCzasStartu() + m0.getCzasTrwania(); //czas zakonczenia
		
		//System.out.println(tablicaPrzerw[0][0]);
		
		for(int j=1;j<k;j++) {
			boolean poprawnyCzas = true;
			boolean czyWhileMaDzialac = true;
			while(czyWhileMaDzialac) {
				czasStartu = r.nextInt(czasMaszyna1+(k*maxCzasTrwaniaPrzerw/4));
				czasTrwania = r.nextInt(maxCzasTrwaniaPrzerw)+1;
				for(int i=0;i<j;i++) {
					
					if(czasStartu+czasTrwania <= tabPrzedzialTrwaniaMaintenencow1[i][0] || czasStartu >= tabPrzedzialTrwaniaMaintenencow1[i][1]) {
						poprawnyCzas = true;
					}else {
						poprawnyCzas = false;
						break;
					}
				}
				if(poprawnyCzas) {
				Maintenance m = new Maintenance(czasStartu, czasTrwania,1);
				tablicaPrzerw[0][j]=m;
				//System.out.println(tablicaPrzerw[0][j]);
				tabPrzedzialTrwaniaMaintenencow1[j][0] = czasStartu;
				tabPrzedzialTrwaniaMaintenencow1[j][1] = czasStartu+czasTrwania;
				czyWhileMaDzialac = false;
				}
			}
			
			
		}
		Porownywacz porownywacz = new Porownywacz();
		Arrays.sort(tablicaPrzerw[0],porownywacz);
		
		
	}
	
	
	public static void generujMaintenance2() {
		int czasStartu = r.nextInt(czasMaszyna2+(k*maxCzasTrwaniaPrzerw/4));
		int czasTrwania = r.nextInt(maxCzasTrwaniaPrzerw)+1;		
		Maintenance m0 = new Maintenance(czasStartu,czasTrwania,2);
		
		tablicaPrzerw[1][0]=m0;//losowy pierwszy maintenance do maszyny 1
		tabPrzedzialTrwaniaMaintenencow1[0][0] = m0.getCzasStartu(); //czas startu
		tabPrzedzialTrwaniaMaintenencow1[0][1] = m0.getCzasStartu() + m0.getCzasTrwania(); //czas zakonczenia
		
		//System.out.println(tablicaPrzerw[0][0]);
		
		for(int j=1;j<k;j++) {
			boolean poprawnyCzas = true;
			boolean czyWhileMaDzialac = true;
			while(czyWhileMaDzialac) {
				czasStartu = r.nextInt(czasMaszyna2+(k*maxCzasTrwaniaPrzerw/4));
				czasTrwania = r.nextInt(maxCzasTrwaniaPrzerw)+1;
				for(int i=0;i<j;i++) {
					
					if(czasStartu+czasTrwania <= tabPrzedzialTrwaniaMaintenencow1[i][0] || czasStartu >= tabPrzedzialTrwaniaMaintenencow1[i][1]) {
						poprawnyCzas = true;
					}else {
						poprawnyCzas = false;
						break;
					}
				}
				if(poprawnyCzas) {
				Maintenance m = new Maintenance(czasStartu, czasTrwania,2);
				tablicaPrzerw[1][j]=m;
				//System.out.println(tablicaPrzerw[0][j]);
				tabPrzedzialTrwaniaMaintenencow1[j][0] = czasStartu;
				tabPrzedzialTrwaniaMaintenencow1[j][1] = czasStartu+czasTrwania;
				czyWhileMaDzialac = false;
				}
			}
			
			
		}
		Porownywacz porownywacz = new Porownywacz();
		Arrays.sort(tablicaPrzerw[1],porownywacz);
		
		
	}
	
	
	
	
	
	public static int sufit(double liczba) {
		int x = (int) liczba;
		if(liczba>(int)liczba) {
		return (int)liczba+1;
		}else {
			return (int)liczba;
		}
	}
	

	
	public Maintenance[][] getTablicaPrzerw() {
		return tablicaPrzerw;
	}
	public void setTablicaPrzerw(Maintenance[][] tablicaPrzerw) {
		this.tablicaPrzerw = tablicaPrzerw;
	}


	public int getCzasMaszyna1() {
		return czasMaszyna1;
	}


	public void setCzasMaszyna1(int czasMaszyna1) {
		this.czasMaszyna1 = czasMaszyna1;
	}


	public int getCzasMaszyna2() {
		return czasMaszyna2;
	}


	public void setCzasMaszyna2(int czasMaszyna2) {
		this.czasMaszyna2 = czasMaszyna2;
	}
	
	
}

class Porownywacz implements Comparator<Maintenance>{

	@Override
	public int compare(Maintenance arg0, Maintenance arg1) {
		if(arg0.getCzasStartu()+arg0.getCzasTrwania() > arg1.getCzasStartu()+arg1.getCzasTrwania()) {
			return 1;
		}else if(arg0.getCzasStartu()+arg0.getCzasTrwania() < arg1.getCzasStartu()+arg1.getCzasTrwania()) {
			return -1;
		}else {
		return 0;
		}
		}
		
	
}
