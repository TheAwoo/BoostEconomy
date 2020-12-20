package boostdevteam.misc;

import boostdevteam.vaultapi.VEconomy;
import org.bukkit.entity.Player;
import boostdevteam.boosteconomy.*;

public class Economy extends VEconomy{

    private Player p;
    private double money;

    public Economy(Player p, double money) {
        super(BoostEconomy.getInstance());
        this.p = p;
        this.money = money;
    }

    public double getBalance() {
        return super.getBalance(this.p.getName());
    }

    public void setBalance() {

        BoostEconomy.getData().saveData(this.p, this.money);
    }

    public void addBalance() {
        super.depositPlayer(p.getName(), money);
    }

    public void takeBalance() {
        super.withdrawPlayer(this.p.getName(), this.money);
    }

    public boolean detractable() {
        return getBalance() - this.money >= 0;
    }
}