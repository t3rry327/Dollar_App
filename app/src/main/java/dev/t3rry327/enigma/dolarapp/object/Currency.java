package dev.t3rry327.enigma.dolarapp.object;


public class Currency {

    private String name;
    private String buyprice;
    private String sellprice;

    public Currency(String name, String buyprice, String sellprice) {
        this.name = name;
        this.buyprice = buyprice;
        this.sellprice = sellprice;
    }

    public String getCurrencyName() {
        return name;
    }

    public String getCurrencyBuyPrice() {
        return buyprice;
    }

    public String getCurrencySellPrice() {
        return sellprice;
    }

}
