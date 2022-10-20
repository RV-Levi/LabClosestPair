
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
 *
 */
/**
 *
 * @author elarteaga
 */
public class Main {

    public static void main(String[] args) {
        Main m = new Main();

        //Datos de prueba
        int n = 10;
        int minValue = 0;
        int maxValue = 100;

        //Puntos aleatorios
        List<int[]> points = m.randomPoints(n, minValue, maxValue);
        //Ordena los puntos de menor a mayor
        Collections.sort(points, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        });

        System.out.println("Lista de puntos:");
        m.showPoints(points, n);

        //Solucion
        System.out.println("\nPuntos mas cercanos:");
        float p = m.closestPair(points, points);

        //Resultado
        System.out.print("Distancia: " + p);
    }

    //Generan puntos aleatorios
    public List<int[]> randomPoints(int n, int minValue, int maxValue) {
        List<int[]> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x = (int) (Math.random() * (maxValue - minValue) + minValue);
            int y = (int) (Math.random() * (maxValue - minValue) + minValue);
            while (existsElement(points, x, y)) {
                x = (int) (Math.random() * (maxValue - minValue) + minValue);
                y = (int) (Math.random() * (maxValue - minValue) + minValue);
            }
            points.add(new int[]{x, y});
        }
        return points;
    }

    //Valida si existe un elemento en la lista de puntos
    public boolean existsElement(List<int[]> points, float x, float y) {
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i)[0] == x && points.get(i)[1] == y) {
                return true;
            }
        }
        return false;
    }

    //Muestro la lista de puntos
    public void showPoints(List<int[]> points, int n) {
        for (int i = 0; i < n; i++) {
            System.out.println("(" + points.get(i)[0] + ", " + points.get(i)[1] + "),");
        }
    }

    //Hallo los puntos con menor distancia
    public float bruteForce(List<int[]> points, float minDistance) {
        for (int i = 0; i < points.size() - 1; i++) {
            for (int j = i + 1; j < points.size(); j++) {
                minDistance = Math.min(minDistance, distance(points, i, j));
            }
        }
        return minDistance;
    }

    public float closestPair(List<int[]> px, List<int[]> py) {
        if (px.size() > 3) {
            int n = (int) (px.size() / 2);
            List<int[]> px1 = new ArrayList<>(px.subList(0, n));//Arrays.asList(Arrays.copyOfRange(px, 0, n));
            List<int[]> px2 = new ArrayList<>(px.subList(n, px.size()));
            List<int[]> py1 = new ArrayList<>();
            List<int[]> py2 = new ArrayList<>();

            for (int i = 0; i < py.size(); i++) {
                if (py.get(i)[0] < px.get(n)[0]) {
                    py1.add(py.get(i));
                } else {
                    py2.add(py.get(i));
                }
            }

            float minimo = Math.min(closestPair(px1, py1), closestPair(px2, py2));
            List<int[]> faltantes = new ArrayList<>();

            for (int i = 0; i < py.size(); i++) {
                if (px1.get(px1.size() - 1)[0] - minimo < py.get(i)[0] && py.get(i)[0] < px1.get(px1.size() - 1)[0] + minimo) {
                    faltantes.add(py.get(i));
                }
            }

            return bruteForce(faltantes, Float.MAX_VALUE);
        } else {
            return bruteForce(px, Float.MAX_VALUE);
        }
    }

    //Hallo la distancia entre 2 puntos
    public float distance(List<int[]> points, int i, int j) {
        return (float) (Math.sqrt(Math.pow(points.get(j)[0] - points.get(i)[0], 2) + Math.pow(points.get(j)[1] - points.get(i)[1], 2)));
    }
}
