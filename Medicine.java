/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author ASUS
 */
public class Medicine {
    //ilaç ile ilgili düzenleme ve metodların bulunduğu sınıf
    private String name;
    
    private String codeChar;
    
    public Medicine(String codeChar){
        this.codeChar = codeChar;
    }
    
    public String getCodeChar() {
        return codeChar;
    }

    public String getName() {
        return name;
    }

    public Medicine(String name, String codeChar) {
        this.name = name;
        this.codeChar = codeChar;
    }
    
    public String [] getList(String code){
        String [] typeMedicine = hashMap().get(code);
        return typeMedicine;
    }
    static String [] assignList(String [] medicineList){
        int upperbound = 4, lowerbound = 1;
        Random rd = new Random();
        //random number generate in a range 1-4
        int range = rd.nextInt((upperbound - lowerbound) + 1) + lowerbound; 
        //bulunan random sayı listenin boyutunu belirliyor
        String [] selectedMedicines = new String[range];
        //ilaç isimlerinin ekleneceği arraylist
        ArrayList list = new ArrayList();
        // aynı ilacı ikinci kez yazmamalı
        for(int i  = 0; i<selectedMedicines.length; i++){
            int choice = rd.nextInt(medicineList.length);
            //eğer random seçilen ilaç zaten var ise
            //choice değişkeninin ataması ilaç bulunmayıncaya kadar devam eder
            while(list.contains(choice)){
              choice = rd.nextInt(medicineList.length);
            }
            //listeye seçilen ilaç ekleniyor
            list.add(choice);
            //kullanıcının bu alınan ilaçları kullanabilmesi için
            //azaltma fonksiyonunda kullanılmak üzere yapılan atama 
            selectedMedicines[i] = medicineList[choice];
        }
        //sonuçta oluşturulan liste döndürülür
        return selectedMedicines;
   }
    /*
    ilaç listeleri
    her bir harf için belirli ilaçlar var
    ilaç uyuşmazlığı gibi bir durum söz konusu değil
    bunları HashMap sınıfından map isimli nesneyi oluşturarak tuttum
    bir key ve bir value eşleşmesi var
    her harfin karşısında bir ilaç listesi mevcut
    */
    private HashMap<String, String[]> hashMap(){
        HashMap<String, String []> map = new HashMap<>();
        String [] Alist = {"ABSTRAL","MORFIA","ACTIQ",
        "FENTANYL","SUBOXONE","ULTIVA"};
        String [] Blist = {"IMMUNOGLOBULIN","HUMAN ALBUMIN","ANTI T LENFOSIT","ANTIHEPATIT B"};
        String [] Clist = {"FACTOR VIII","FACTOR IX", "KOATE", "HEMOFIL-M"};
        String [] Dlist = {"ANSIOX","CONTRAMAL","DALIZOM","DIAZEM","DORMICUM",
        "KETALAR"};
        String [] EList = {"ALOND","BENICAL","APRANAX","GABENYL","LYRICA","NERUDA"};
        map.put("A",Alist);
        map.put("B", Blist);
        map.put("C", Clist);
        map.put("D", Dlist);
        map.put("E", EList);
        return map;
    }
    
    
}
