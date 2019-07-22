package sample.Enum;

public enum Country {
    Grecja,
    Włochy,
    Hiszpania,
    Wielka_Brytania,
    Beneluks,
    Szwecja,
    RFN,
    Austria;

    public static String getNameByValue(int code){
        for(Country e : Country.values()){
            if(code == e.ordinal()) return e.name();
        }
        return null;
    }
}
