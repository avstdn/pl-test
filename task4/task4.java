import java.io.File;
import java.io.IOException;
import java.util.*;

public class task4 {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Не указан путь к входному файлу");
            return;
        }

        String inputFilePath = args[0];
        List<String[]> list = readInputFile(inputFilePath);

        if (list == null) return;

        findMaxIntervals(list);
    }

    private static List<String[]> readInputFile(String inputFilePath) {
        List<String[]> list = new ArrayList<>();
        Map<String, Integer> inTimeMap = new HashMap<>();
        Map<String, Integer> outTimeMap = new HashMap<>();

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

                String[] newLineColumns = newLine.split(" ");

                if (newLineColumns.length < 2) {
                    System.out.println("В файле меньше 2х столбцов");
                    return null;
                }

                String inTimeField = newLineColumns[0];
                String outTimeField = newLineColumns[1];

                removeDuplicateTimePairs(inTimeField, inTimeMap, outTimeMap);
                removeDuplicateTimePairs(outTimeField, outTimeMap, inTimeMap);
            }

            scanner.close();
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла");
            return null;
        }

        joinMapToList(inTimeMap, list, "in");
        joinMapToList(outTimeMap, list, "out");

        list.sort(Comparator.comparing(time -> time[0]));

        return list;
    }

    private static void removeDuplicateTimePairs(String time, Map<String, Integer> firstMap, Map<String, Integer> secondMap) {
        if (secondMap.getOrDefault(time, 0) == 0) {
            firstMap.put(time, firstMap.getOrDefault(time, 0) + 1);
        } else {
            secondMap.put(time, secondMap.get(time) - 1);
        }
    }

    private static void joinMapToList(Map<String, Integer> map, List<String[]> list, String type) {
        for (String time : map.keySet()) {
            int timeCount = map.get(time);

            if (timeCount > 0) {
                while (timeCount-- > 0) {
                    list.add(new String[]{time, type});
                }
            }
        }
    }

    private static void findMaxIntervals(List<String[]> list) {
        List<String[]> resultList = new ArrayList<>();
        int guestCount = 0;
        int maxGuests = 0;

        for (int i = 0; i < list.size(); i++) {
            String guestType = list.get(i)[1];

            if (guestType.equals("in")) {
                guestCount++;

                if (guestCount >= maxGuests) {
                    maxGuests = guestCount;
                    String timeBefore = list.get(i)[0];
                    String timeAfter = i < list.size() - 1 ? list.get(i + 1)[0] : null;
                    resultList.add(new String[] {maxGuests + "", timeBefore, timeAfter});
                }
            } else {
                guestCount--;
            }
        }

        printResultIntervals(resultList, maxGuests);
    }

    private static void printResultIntervals(List<String[]> resultList, int maxGuests) {
        for (String[] interval : resultList) {
            String intervalMaxGuests = interval[0];

            if (intervalMaxGuests.equals(maxGuests + "")) {
                String intervalIn = interval[1];
                String intervalOut = interval[2];

                System.out.println(intervalIn + " " + intervalOut);
            }
        }
    }
}