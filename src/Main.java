import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class Generator {
    public static long iMod1 = (long) Math.pow(2, 31) - 1;
    public static long iMod2 = (long) Math.pow(2, 31) - 1;
    public static long iMultiplier1 = 1664525;
    public static long iMultiplier2 = 214013;
    public static long iInc1 = 1013904223;
    public static long iInc2 = 2531011;
    public  static int iLength =  100_000_000;
    public  static  String sFilePathName = "src/resources/FileResultArray.txt";

    // =========================================
    // ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ
    // =========================================

    // Функция получения начального параметра
    static long fGetToSeedParameter() {
        GregorianCalendar vGregorianCalendar = new GregorianCalendar();
        return (long) (vGregorianCalendar.get(Calendar.MILLISECOND) * Math.random());
    }

    // Функция вывода последовательности
    static void fPrintArray(long[] aiResultRandomNum ) {
        System.out.println("Последовательность ЛКМ ");
        for (long l : aiResultRandomNum) {
            System.out.print(l + " ");}
        System.out.println("");
    }

    // Функция получения длины итоговой последовательности
    static int fGetLengthArray() {
        System.out.println("Сколько чисел сгенерировать? ");
        Scanner sc = new Scanner(System.in);
        int number = 0;
        do {
            if(number < 0 ) {
                System.out.println("Please enter a positive number!");
            }
            while (!sc.hasNextInt()) {
                System.out.println("That not a number!");
                sc.next();
            }
            number = sc.nextInt();
        } while (number <= 0);

        return number;
    }

    // Функция пермешивания двух последовательностей
    static long[] fShuffle(long[] aiArray1, long[] aiArray2, long iModOfArray2, long k) { //
        long[] aiResult = new long[(int)k];
        for (int i = 0; i < k ; i++) {
            aiResult[i] = aiArray1[i];
        }

        for (int i = 0; i < aiResult.length; i++) {
            long x = aiArray1[i];
            long y = aiArray2[i];

            long j = (k * y) % iModOfArray2;
            if(j < k) {
                aiResult[(int) j] = x;
            }
        }

        return aiResult;
    }

    // Функция записи последовательности в текстовый файл
    public static void fWriteToTxt (long [] aiArray, String sFilePathName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(sFilePathName)));
        for (long l : aiArray) {
            writer.write(String.valueOf(l));
            writer.write(" ");
        }
        writer.flush();
    }
    // Функция факториала
    public static long factorial(double n) {
        long fact = 1;
        for (int i = 2; i <= n; i++) {
            fact = fact * i;
        }
        return fact;
    }

    // =========================================
    // ГЕНЕРАТОР
    // =========================================

    // Функции генерации псевдослучайных чисел
    static long[] fGenerate(long iSeed,
                           long iMod,
                           long iMultiplier,
                           long iInc) {

        long[] aiResultRandomNum = new long[iLength];

        // Инициализация входной точки
        aiResultRandomNum[0] = (iSeed * iMultiplier + iInc )% iMod;
        for (int i = 1; i < iLength; i++) {
            aiResultRandomNum[i] = ((aiResultRandomNum[i - 1] * iMultiplier) + iInc) % iMod;
        }
        return aiResultRandomNum;
    }

    // =========================================
    // КРИТЕРИИ
    // =========================================

    // Критерий Пирсона
    public static boolean fPirson (long [] aiArray, int amount_number) {
        int mod = 5;
        HashMap<Integer, Double> points = new HashMap<>();
        points.put(1, 0.2971);
        points.put(5, 0.7107);
        points.put(25, 1.923);
        points.put(50, 3.357);
        points.put(75, 5.385);
        points.put(95, 9.488);
        points.put(99, 13.28);

        long[] aiResult = aiArray;
        long[] aiResultMod5 = new long[5];
        double P_s = 0.2;

        HashMap<Integer, Integer> Y_s = new HashMap<>();
        int V = 0;
        ArrayList<Integer> posledovPoMod = new ArrayList<>();

        for (int i = 0; i < mod ; i++) {
            aiResultMod5[i] = i;
            Y_s.put(i,0);
        }

        // Подсчет последовательности по модулю
        for (int i = 0; i < aiResult.length; i++) {
            posledovPoMod.add((int) aiResult[i] % mod);
        }
        // Заполнение количества вхождений числе по модулю 5
        for (int i = 0; i < posledovPoMod.size(); i++) {
            for (int j = 0; j < aiResultMod5.length; j++) {
                if(posledovPoMod.get(i) == aiResultMod5[j]) {
                    Y_s.put(j,Y_s.get(j) + 1);
                }
            }
        }

        // Подсчет V
        for (int i = 0; i < posledovPoMod.size(); i++) {
            for(Map.Entry<Integer, Integer> item : Y_s.entrySet()){
                if(posledovPoMod.get(i) == item.getKey()) {
                    V += Math.pow(item.getValue() - amount_number * P_s, 2) / (amount_number * P_s);
                }
            }
        }
        System.out.println("V = "+ V);

        // Вывод результата V
        boolean bCheck = false;
        for(Map.Entry<Integer, Double> point : points.entrySet()) {
            if(V <= point.getValue()){
                System.out.println("V меньше " +  point.getValue() + "это " + point.getValue() + "%");
                bCheck = true;
                break;
            }
        }

        return bCheck;
    }

    // Критерий Колмогорова-Смирнова
    public static boolean fKS (long [] aiArray, int amount_number) {
        long mod = 1000000000;
        Map<Integer, Double> points = new HashMap<>();
        points.put(1, 0.03807);
        points.put(5, 0.1298);
        points.put(25, 0.3461);
        points.put(50, 0.5547);
        points.put(75, 0.7975);
        points.put(95, 1.839);
        points.put(99, 1.4698);

        long[] aiResult = aiArray;
        long[] aiResult_F_n = new long[aiResult.length];

        //  Деление на модуль (просто большое число) и сортировка
        for (int i = 0; i < aiResult.length; i++) {
            aiResult_F_n[i] = (int) (aiResult[i] / mod);
        }
        Arrays.sort(aiResult_F_n);

        double kPlus = 0;
        double kMinus = 0;
        //  Подсчет Мах и Мин
        for (double i = 1.0; i < amount_number; i++) {
            double max1 = (float) (i / amount_number) - aiResult_F_n[(int) i];
            double max2 = aiResult_F_n[(int) i] - ((i - 1)/ amount_number);
            if(max1 > kPlus){
                kPlus = max1;
            }
            if(max2 > kMinus){
                kMinus = max2;
            }
        }
        kPlus = Math.sqrt(amount_number) * kPlus;
        kMinus = Math.sqrt(amount_number) * kMinus;
        System.out.println("K+ = " +  kPlus +  "\nK- = " + kMinus);

        boolean bCheck = false;
        for(Map.Entry<Integer, Double> point : points.entrySet()) {
            if(kPlus <= point.getValue()){
                System.out.println("V меньше " +  point.getValue() + " это " + point.getKey() + "%");
                bCheck = true;
                break;
            }
        }

        return bCheck;
    }

    public static boolean fMonotonnosti (long [] aiArray, int amount_number) {
        //  будем брать последовательно длины 40
        Map<Integer, Double> points = new HashMap<>();
        points.put(1, 2.558);
        points.put(5, 3.940);
        points.put(25, 6.737);
        points.put(50, 9.342);
        points.put(75, 12.55);
        points.put(95, 18.31);
        points.put(99, 23.21);
        int mod = 10;
        long[] aiResult = aiArray;
        ArrayList<Integer> mas_mod = new ArrayList<>();
        System.out.println("Критерий монотонности");
        // Mассив чисел по мод 10
        for (int i = 0; i < aiResult.length; i++) {
            mas_mod.add((int) (aiResult[i] % mod));
        }
        System.out.println("Последовательность по модулю 10:");
        System.out.println(mas_mod);

        ArrayList<Integer> countMass=new ArrayList<>(5);
        ArrayList<Integer> listOfLength = new ArrayList<>();
        int curLength = 0;
        // Нахождение всех подпоследовательностей. При переходе на убывающую, данный элемент заменяется -1
        for (int i = 0; i < mas_mod.size() - 1; i++) {
            if (mas_mod.get(i + 1) <= mas_mod.get(i)) {
                mas_mod.set(i + 1, -1);
            }
        }
        // Затем идет подсчет количества шагов до -1 и т.д
        for (int i = 0; i < mas_mod.size(); i++) {
            if (mas_mod.get(i) != -1) {
                curLength += 1;
            } else {
                listOfLength.add(curLength);
                curLength = 0;
            }
        }
        // Заполнение массива возрастающих последовательностей
        if (curLength != 0) {
            listOfLength.add(curLength);
        }
        ArrayList<Integer> unsortedListNoOnes = new ArrayList<>();
        for (Integer i : mas_mod) {
            if (i != -1) {
                unsortedListNoOnes.add(i);
            }
        }
        System.out.println("Возрастающие подпоследовательности:");
        System.out.println(unsortedListNoOnes);
        System.out.println("Длины возрастающих подпоследовательностей:");
        System.out.println(listOfLength);
        System.out.println("Количество подпоследовательностей (mass_count):");
        for (int i = 0; i < listOfLength.size(); i++) {
            int index = listOfLength.get(i);
            int value = index + 1;
            countMass.set(index, value);
        }
        countMass.remove(countMass.get(0));
        System.out.println(countMass);

        ArrayList<Double> verMass=new ArrayList<>(4);
        verMass.set(0, 0.5);
        // Построение массива вероятностей встречи подпоследовательностей длинн от 1 до 4
        for (int i = 1; i < verMass.size() ; i++) {
            double k = i;
            double ver = (double) ((1/ factorial(k+1)) - (1/ factorial(k+2)));
            verMass.add(ver);
        }
        System.out.println("Вероятности встречи возрастающей подпоследовательности (verMass):");
        System.out.println(verMass);

        int V = 0;
        // Подсчет конечного значения
        for (int i = 1; i <= countMass.size(); i++) {
            if (countMass.get(i) != 0) {
                V+=(Math.pow(countMass.get(i)-amount_number*verMass.get(i), 2))/amount_number*verMass.get(i);
            }
        }
        System.out.println("V= " + V);

        boolean bCheck = false;
        for(Map.Entry<Integer, Double> point : points.entrySet()) {
            if(V <= point.getValue()){
                System.out.println("V меньше " +  point.getValue() + " это " + point.getKey() + "%");
                bCheck = true;
                break;
            }
        }
        return bCheck;
    }

    // Критерий Т
    public static void fT(long[] aiArray, int amount_number) {
        // будем брать последовательно длины 20 чтобы соответствовать строке из таблицы
        HashMap<Integer, Double> points = new HashMap<>();
        points.put(1, 0.03807);
        points.put(5, 0.1298);
        points.put(25, 0.3461);
        points.put(50, 0.5547);
        points.put(75, 0.7975);
        points.put(95, 1.139);
        points.put(99, 1.4698);
        int mod = 10;
        ArrayList<Double> posledovPoMod = new ArrayList<>();
        int lengthBlockN = 5;
        int countBlockN = amount_number / lengthBlockN;
        ArrayList<Double> mas_max = new ArrayList<>();
        double max_elem = 0;

        // Построение последовательности в диапазоне [0,1)
        for (int i = 0; i < amount_number; i++) {
            double ost = aiArray[i];
            while (ost >= 1) {
                ost = ost / mod;
            }
            posledovPoMod.add(ost);
        }

        // формирование массива максимальными элементами из n блоков длинны t
        for (int i = 0; i < countBlockN; i++) {
            for (int j = 0; j < lengthBlockN; j++) {
                double elem = posledovPoMod.get(lengthBlockN * i + j);
                // нахождение МАХ элемента и добавление его в массив
                if (elem > max_elem) {
                    max_elem = elem;
                }
            }
            mas_max.add(max_elem);
            max_elem = 0;
        }
        Collections.sort(mas_max);

        double kPlus = 0;
        double kMinus = 0;

        // Подсчет Kn_plus и Kn_minus
        for (int i = 0; i < mas_max.size(); i++) {
            double elem1 = (i + 1.0) / countBlockN - Math.pow(mas_max.get(i), lengthBlockN);
            double elem2 = Math.pow(mas_max.get(i), lengthBlockN) - i / (double) countBlockN;
            if (elem1 > kPlus) {
                kPlus = elem1;
            }
            if (elem2 > kMinus) {
                kMinus = elem2;
            }
        }
        double Kn_plus = kPlus * Math.sqrt(countBlockN);
        double Kn_minus = kMinus * Math.sqrt(countBlockN);

        System.out.println("Kn_plus=" + Kn_plus);
        for (Map.Entry<Integer, Double> point : points.entrySet()) {
            if (Kn_plus <= point.getValue()) {
                System.out.println("Kn_plus меньше " + Kn_plus + " это " + point.getKey() + "%");
                break;
            }
        }

        System.out.println("Kn_minus=" + Kn_minus);
        for (Map.Entry<Integer, Double> point : points.entrySet()) {
            if (Kn_minus <= point.getValue()) {
                System.out.println("Kn_minus меньше " + Kn_minus + " это " + point.getKey() + "%");
                break;
            }
        }
        System.out.println(points);
    }

    // =========================================
    // ФУНКЦИЯ ЗАПУСКА
    // =========================================

    public static void main(String[] args) throws IOException {
        System.out.println("Добро пожаловать в программу \"Линейный конгруэнтный генератор\".");
        int iLengthOfRandomNum = fGetLengthArray();

        long iSeed = fGetToSeedParameter();
        // Генерация первой последовательности
        long [] aiResultRandomNum1 = fGenerate(iSeed, iMod1, iMultiplier1, iInc1);
        // Генерация второй последовательности
        long [] aiResultRandomNum2 = fGenerate(iSeed, iMod2, iMultiplier2, iInc2);
        // Перемешивание последовательностей
        long[] iResult = fShuffle(aiResultRandomNum1, aiResultRandomNum2, iMod2, iLength);
        // Получение последовательности заданной длины
        long[] aiResponse = Arrays.copyOfRange(iResult, 0, iLengthOfRandomNum);
        // Запись в файл
        fWriteToTxt(aiResponse, sFilePathName);

        // Блок критериев TODO: раскоментировать для проверок полученной последовательность
        // fPirson(aiResponse, iLengthOfRandomNum);
        // fKS(aiResponse, iLengthOfRandomNum);
        // fMonotonnosti(iResult, iLengthOfRandomNum);
        // fT(iResult, iLengthOfRandomNum);

        System.out.println("Программа завершена.");
    }
}
