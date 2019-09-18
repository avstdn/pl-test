import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class task1 {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Не указан путь к входному файлу");
            return;
        }

        String inputFilePath = args[0];
        List<Integer> list = readInputFile(inputFilePath);

        if (list == null) return;

        Collections.sort(list);

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        String minOfList = decimalFormat.format(list.get(0));
        String maxOfList = decimalFormat.format(list.get(list.size() - 1));
        String averageOfList = decimalFormat.format(getAverage(list));
        String median = decimalFormat.format(getMedian(list));
        String percentile = decimalFormat.format(getPercentile(list));

        System.out.println(percentile);
        System.out.println(median);
        System.out.println(maxOfList);
        System.out.println(minOfList);
        System.out.println(averageOfList);
    }

    private static List<Integer> readInputFile(String inputFilePath) {
        List<Integer> list = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(inputFilePath));

            if (!scanner.hasNext()) {
                System.out.println("Файл пуст");
                return null;
            }

            while (scanner.hasNextLine()) {
                String newLine = scanner.nextLine();

                if (newLine.isEmpty()) {
                    System.out.println("В файле пустая строка");
                    return null;
                }

                try {
                    int inputValueField = Integer.parseInt(newLine);
                    list.add(inputValueField);
                } catch (NumberFormatException e) {
                    System.out.println("Некорректное значение в поле файла");
                    return null;
                }
            }

            scanner.close();
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла");
            return null;
        }

        return list;
    }

    private static double getAverage(List<Integer> inputList) {
        return inputList.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    private static double getMedian(List<Integer> inputList) {
        double median;
        int n = inputList.size();

        if (n % 2 == 0) {
            median = ((double) inputList.get(n / 2) + (double) inputList.get(n / 2 - 1)) / 2;
        } else {
            median = (double) inputList.get(n / 2);
        }

        return median;
    }

    private static double getPercentile(List<Integer> inputList) {
        int N = inputList.size();
        double n = (N - 1) * 0.9 + 1;

        if (n == 1) {
            return inputList.get(0);
        } else if (n == N) {
            return inputList.get(N - 1);
        } else {
            int k = (int) n;
            double d = n - k;

            return inputList.get(k - 1) + d * (inputList.get(k) - inputList.get(k - 1));
        }
    }
}
