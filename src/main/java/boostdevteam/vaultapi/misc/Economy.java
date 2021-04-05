package boostdevteam.vaultapi.misc;

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
        return plugin.getRDatabase().getTokens(this.p.getName());
        //return (long) super.getBalance(this.p.getName());
    }

    public void setBalance() {
        plugin.getRDatabase().setTokens(this.p.getName(), Long.parseLong(toLong(this.money)));
    }

    public void addBalance() {
        super.depositPlayer(this.p.getName(), money);
        long x = getBalance();
        plugin.getRDatabase().setTokens(this.p.getName(), Long.parseLong(toLong(this.money + x)));
    }

    public void takeBalance() {
        super.withdrawPlayer(this.p.getName(), this.money);
        long x = getBalance();
        plugin.getRDatabase().setTokens(this.p.getName(), Long.parseLong(toLong(Math.max(x - this.money, 0))));
    }

    public boolean detractable() {
        return getBalance() - this.money >= 0;
    }
}