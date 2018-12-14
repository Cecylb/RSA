import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA {
    private BigInteger n, d, e;
    private int length; // Разрядность в битах
// Конструктор
    public RSA(int _length) { // Получаем разрядность из main
        length = _length;
        SecureRandom random = new SecureRandom();
        BigInteger p = new BigInteger(length/2, 100, random); // Выбрать большие простые P и Q
        BigInteger q = new BigInteger(length/2, 100, random);
        n = p.multiply(q); // Вычислить N=PQ
        BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE)); // вычислить вспомогательное число m=(P-1)(Q-1)
        e = new BigInteger("3"); // Выбирается целое число e (1<e<f(n)), взаимно простое со значением функции f(n)
        while (m.gcd(e).intValue() > 1) { // Пока наибольший общий делитель больше одного
            e = e.add(new BigInteger("2")); // е = е + 2
        }
        d = e.modInverse(m); // Вычисляется число d, мультипликативно обратное к числу e по модулю f(n)
    }
    // Зашифровываем входящее сообщение
    public synchronized BigInteger encrypt(BigInteger message) {
        return message.modPow(e, n); // Пара (e,n) - открытый ключ RSA
    } // c = E(m) = m^e mod n
    // Расшифровываем входящее сообщение
    public synchronized BigInteger decrypt(BigInteger message) {
        return message.modPow(d, n); // Пара (d,n) - закрытый ключ RSA
    } // m = D(c) = c^d mod n

    public static void main(String[] args) {
        RSA rsa = new RSA(2048);
        String text1 = "RSA проверка 1 2 3 4 5 ! .";
        System.out.println("Исходное сообщение: " + text1);
        System.out.println("Шифрование текста . . . ");
        BigInteger plaintext = new BigInteger(text1.getBytes());
        BigInteger ciphertext = rsa.encrypt(plaintext);
        System.out.println("Шифрование завершено.");
        System.out.println("Зашифрованное сообщение: " + ciphertext);
        System.out.println("Расшифровка текста . . . ");
        plaintext = rsa.decrypt(ciphertext);
        String text2 = new String(plaintext.toByteArray());
        System.out.println("Расшифровка завершена.");
        System.out.println("Расшифрованное сообшение: " + text2);
    }
}