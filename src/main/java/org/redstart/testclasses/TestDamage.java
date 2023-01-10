package org.redstart.testclasses;

public class TestDamage {
    public static void main(String[] args) {
        int playerShield = 50;
        int playerHp = 50;

        while (playerHp > 0) {
            int damage = (int) (Math.random() * 10);
            System.out.println("Damage - " + damage);
            int damageToHp = damage / 2;
            int damageToShield = damage - damageToHp;

            System.out.println("Damage to player hp - " + damageToHp + ", to shield - " + damageToShield);

            playerShield -= damageToShield;
            if (playerShield < 0) {
                damageToHp += playerShield * (-1);
                playerShield = 0;
            }
            playerHp -=damageToHp;

            System.out.println("playerHp - " + playerHp + ". playerShield - " + playerShield);
        }

    }
}
