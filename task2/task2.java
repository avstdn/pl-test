import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class task2 {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Не указан путь к входному файлу");
            return;
        }

        String inputFilePathToShape = args[0];
        String inputFilePathToPoint = args[1];

        List<Point> shapePoints = readInputFile(inputFilePathToShape);
        List<Point> checkPoints = readInputFile(inputFilePathToPoint);

        if (shapePoints == null || checkPoints == null) return;

        List<Integer> resultList = getResultList(checkPoints, shapePoints);

        printResult(resultList);
    }

    private static void printResult(List<Integer> resultList) {
        for (Integer i : resultList) {
            System.out.println(i);
        }
    }

    private static List<Integer> getResultList(List<Point> checkPoints, List<Point> shapePoints) {
        List<Integer> resultList = new ArrayList<>();

        for (Point checkPoint : checkPoints) {
            if (isVertex(checkPoint, shapePoints)) resultList.add(0);
            else if (isEdge(checkPoint, shapePoints)) resultList.add(1);
            else if (isInside(checkPoint, shapePoints)) resultList.add(2);
            else resultList.add(3);
        }

        return resultList;
    }

    private static List<Point> readInputFile(String inputFilePath) {
        List<Point> points = new ArrayList<>();

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

                try {
                    double xCoordinates = Double.parseDouble(newLineColumns[0]);
                    double yCoordinates = Double.parseDouble(newLineColumns[1]);

                    points.add(new Point(xCoordinates, yCoordinates));
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

        return points;
    }

    private static boolean isInside(Point checkPoint, List<Point> points) {
        boolean result = false;

        for (int i = 0, j = points.size() - 1; i < points.size(); j = i++) {
            Point firstShapePoint = points.get(i);
            Point secondShapePoint = points.get(j);

            if ((firstShapePoint.getY() > checkPoint.getY()) != (secondShapePoint.getY() > checkPoint.getY())) {
                if (checkPoint.getX() < (secondShapePoint.getX() - firstShapePoint.getX()) * (checkPoint.getY() - firstShapePoint.getY()) / (secondShapePoint.getY() - firstShapePoint.getY()) + firstShapePoint.getX()) {
                    result = !result;
                }
            }
        }

        return result;
    }

    private static boolean isVertex(Point checkPoint, List<Point> shapePoints) {
        return (checkPoint.getX() == shapePoints.get(0).getX() && checkPoint.getY() == shapePoints.get(0).getY())
                || (checkPoint.getX() == shapePoints.get(1).getX() && checkPoint.getY() == shapePoints.get(1).getY())
                || (checkPoint.getX() == shapePoints.get(2).getX() && checkPoint.getY() == shapePoints.get(2).getY())
                || (checkPoint.getX() == shapePoints.get(3).getX() && checkPoint.getY() == shapePoints.get(3).getY());
    }

    private static boolean isEdge(Point checkPoint, List<Point> shapePoints) {
        for (int i = 0; i < 4; i++) {
            Point firstEdgePoint = shapePoints.get(i);
            Point secondEdgePoint = shapePoints.get((i + 1) % 4);

            // x1 <= x <= x2 и y1 <= y <= y2
            boolean lineEquationClause = (firstEdgePoint.getX() <= checkPoint.getX() && checkPoint.getX() <= secondEdgePoint.getX()) &&
                    (firstEdgePoint.getY() <= checkPoint.getY() && checkPoint.getY() <= secondEdgePoint.getY());

            if (!lineEquationClause) continue;

            // (x - x1) * (y2 - y1) - (y - y1) * (x2 - x1) */
            double lineEquationResult = (checkPoint.getX() - firstEdgePoint.getX()) * (secondEdgePoint.getY() - firstEdgePoint.getY()) -
                    (checkPoint.getY() - firstEdgePoint.getY()) * (secondEdgePoint.getX() - firstEdgePoint.getX());

            if (lineEquationResult == 0) return true;
        }

        return false;
    }
}

class Point {
    private double x;
    private double y;

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }
}
