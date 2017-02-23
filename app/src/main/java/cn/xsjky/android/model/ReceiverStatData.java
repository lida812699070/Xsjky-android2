package cn.xsjky.android.model;

/**
 * Created by ${lida} on 2016/11/28.
 */
public class ReceiverStatData {
    private String StatPeriod;
    private String TicketCount;
    private String TotalQuantity;
    private String TotalWeight;
    private String TotalVolumn;
    private String TotalGoodsAmt;
    private String TotalPremium;
    private String TotalCarriage;
    private String TotalAmount;

    @Override
    public String toString() {
        return "ReceiverStatData{" +
                "StatPeriod='" + StatPeriod + '\'' +
                ", TicketCount='" + TicketCount + '\'' +
                ", TotalQuantity='" + TotalQuantity + '\'' +
                ", TotalWeight='" + TotalWeight + '\'' +
                ", TotalVolumn='" + TotalVolumn + '\'' +
                ", TotalGoodsAmt='" + TotalGoodsAmt + '\'' +
                ", TotalPremium='" + TotalPremium + '\'' +
                ", TotalCarriage='" + TotalCarriage + '\'' +
                ", TotalAmount='" + TotalAmount + '\'' +
                '}';
    }

    public String getStatPeriod() {
        return StatPeriod;
    }

    public void setStatPeriod(String statPeriod) {
        StatPeriod = statPeriod;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getTotalCarriage() {
        return TotalCarriage;
    }

    public void setTotalCarriage(String totalCarriage) {
        TotalCarriage = totalCarriage;
    }

    public String getTotalGoodsAmt() {
        return TotalGoodsAmt;
    }

    public void setTotalGoodsAmt(String totalGoodsAmt) {
        TotalGoodsAmt = totalGoodsAmt;
    }

    public String getTotalPremium() {
        return TotalPremium;
    }

    public void setTotalPremium(String totalPremium) {
        TotalPremium = totalPremium;
    }

    public String getTotalVolumn() {
        return TotalVolumn;
    }

    public void setTotalVolumn(String totalVolumn) {
        TotalVolumn = totalVolumn;
    }

    public String getTotalWeight() {
        return TotalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        TotalWeight = totalWeight;
    }

    public String getTicketCount() {
        return TicketCount;
    }

    public void setTicketCount(String ticketCount) {
        TicketCount = ticketCount;
    }

    public String getTotalQuantity() {
        return TotalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        TotalQuantity = totalQuantity;
    }
}
