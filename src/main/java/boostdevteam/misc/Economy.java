package boostdevteam.misc;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.vaultapi.VEconomy;
import org.bukkit.entity.Player;

public class Economy extends VEconomy {

    private Player p;
    private double money;

    public Economy(Player p, double money) {
        super(BoostEconomy.getInstance());
        this.p = p;
        this.money = money;
    }

    private String toLong(double amt) {
        return String.valueOf((long) amt);
    }

    public long getBalance() {
        return (long) super.getBalance(this.p.getName());
    }

    public void setBalance() {
        BoostEconomy.getData().saveData(this.p, Long.parseLong(toLong(this.money)));
    }

    public void addBalance() {
        super.depositPlayer(this.p.getName(), money);
    }

    public void takeBalance() {
        super.withdrawPlayer(this.p.getName(), this.money);
    }

    public boolean detractable() {
        return getBalance() - this.money >= 0;
    }
}