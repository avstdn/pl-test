import java.io.File;
import java.io.IOException;
import java.util.*;

public class task3 {
    public static void main(String[] args) {
        if (filesAreIncorrect(args)) return;

        String[] inputFilePaths = getInputFilePaths(args);
        List<Double> sumOfIntervalsList = getSumOfIntervals(inputFilePaths);

        if (sumOfIntervalsList == null) return;

        System.out.println(getMaxLoadInterval(sumOfIntervalsList));
    }

    private static boolean filesAreIncorrect(String[] args) {
        if (args.length < 1) {
            System.out.println("Не указан путь к каталогу");
            return true;
        }

        String pathToFolder = args[0];
        File folder = new File(pathToFolder);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) {
            System.out.println("Каталог пуст");
            return true;
        } else if (listOfFiles.length < 5) {
            System.out.println("В каталоге должно быть не менее 5 файлов");
            return true;
        }

        for (int i = 0; i < 5; i++) {
            String fileName = "Cash" + (i + 1) + ".txt";
            File file = new File(pathToFolder + File.separator + fileName);

            if (!file.exists()) {
                System.out.println("В каталоге не найден файл: " + fileName);
                return true;
            }
        }

        return false;
    }

    private static String[] getInputFilePaths(String[] args) {
        String[] inputFilePaths = new String[5];
        File folder = new File(args[0]);

        for (int i = 0; i < 5; i++) {
            String fileName = "Cash" + (i + 1) + ".txt";
            File file = new File(folder.getPath() + File.separator + fileName);
            inputFilePaths[i] = file.getPath();
        }

        return inputFilePaths;
    }

    private static List<Double> getSumOfIntervals(String[] inputFilePaths) {
        List<Double> sumOfIntervalsList = readInputFile(inputFilePaths[0]);

        if (sumOfIntervalsList == null) return null;

        for (int i = 1; i < inputFilePaths.length; i++) {
            List<Double> intervalsList = readInputFile(inputFilePaths[i]);

            if (intervalsList == null) return null;

            for (int j = 0; j < sumOfIntervalsList.size(); j++) {
                sumOfIntervalsList.set(j, sumOfIntervalsList.get(j) + intervalsList.get(j));
            }
        }

        return sumOfIntervalsList;
    }

    private static List<Double> readInputFile(String inputFilePath) {
        List<Double> inputData = new ArrayList<>();
        File file = new File(inputFilePath);

        try {
            Scanner scanner = new Scanner(file);

            if (!scanner.hasNext()) {
                System.out.println("Файл " + file.getName() + " пуст");
                return null;
            }

            while (scanner.hasNextLine()) {
                String newLine = scanner.nextLine();

                if (newLine.isEmpty()) {
                    System.out.println("В файле " + file.getName() + " пустая строка");
                    return null;
                }

                try {
                    double averageLengthField = Double.parseDouble(newLine);
                    inputData.add(averageLengthField);
                } catch (NumberFormatException e) {
                    System.out.println("Некорректное значение в поле файла " + file.getName());
                    return null;
                }
            }

            if (inputData.size() != 16) {
                System.out.println("Количество интервалов в файле " + file.getName() + " не равно 16");
                return null;
            }

            scanner.close();
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла " + file.getName());
            return null;
        }

        return inputData;
    }

    private static int getMaxLoadInterval(List<Double> sumOfIntervalsList) {
        int intervalIndex = sumOfIntervalsList.size() - 1;

        for (int i = sumOfIntervalsList.size() - 2; i >= 0; i--) {
            if (sumOfIntervalsList.get(intervalIndex) < sumOfIntervalsList.get(i)) {
                intervalIndex = i;
            }
        }

        return intervalIndex + 1;
    }
}