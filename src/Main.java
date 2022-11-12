
/*

Algoritmos y complejidad IST 4310 - 01
NRC: 3264
@author Elkin Luis Arteaga Sánchez
Código: 200153212
Actividad: Lab Closest Pair
Fecha: 04/11/2022
Descripción: Algoritmo que encuentra y muestra la distancia del par de coordenadas (x,y) más cercano.

https://github.com/RV-Levi/LabClosestPair
https://github.com/RV-Levi/Plot_Coords/tree/main

 */
import java.util.Collections;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

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
        int limite_n = 50;
        int limite_experimentos = 500;
        int minValue = -100;
        int maxValue = 100;

        //Llamado de puntos pseudo-aleatorios
        List<int[]> points = randomPoints(n, minValue, maxValue);

        for (int i = 0; i < limite_n; i++) {
            double avg_iterative = 0;
            double avg_recursive = 0;
            for(int j = 0; j < limite_experimentos; j++) {
                avg_iterative += m.iterativo(points, n, minValue, maxValue);
                avg_recursive += m.recursivo(points, n, minValue, maxValue);
            }
            avg_iterative /= limite_experimentos;
            avg_recursive /= limite_experimentos;
            create(n, avg_iterative, "dataIterativo.txt");
            create(n, avg_recursive, "dataRecursivo.txt");
            n += 10;
        }
    }

    public double iterativo(List<int[]> points, int n, int minValue, int maxValue) {
        long inicio = System.nanoTime();

        //Ordena los puntos de menor a mayor
        Collections.sort(points, (int[] o1, int[] o2) -> o1[0] - o2[0]);

        //Imprime la lista de puntos, en orden ascendente
        //System.out.println("Lista de puntos:");
        //showPoints(points, n);

        //Solución
        float p = bruteForce(points, Float.MAX_VALUE);

        //Imprime la distancia mas corta
        //System.out.println("ITERATIVO");
        //System.out.println("Distancia de los puntos más cercanos: " + p);

        //Imprime el número de pares de coordenadas
        //System.out.println("Número de pares de coordenadas: " + n);

        //Imprime el tiempo de ejecución
        long fin = System.nanoTime();
        double tiempo = (double) ((fin - inicio) / 1000000);
        //System.out.println("Tiempo de ejecución: " + tiempo + " milisegundos");
        //System.out.println("------------------------------------------------");
        
        return tiempo;
    }

    public double recursivo(List<int[]> points, int n, int minValue, int maxValue) {
        long inicio = System.nanoTime();

        //Ordenamos los puntos
        List<int[]> pointsX = new ArrayList<>(points);
        List<int[]> pointsY = new ArrayList<>(points);

        //Ordena los puntos de menor a mayor
        Collections.sort(pointsX, (int[] o1, int[] o2) -> o1[0] - o2[0]);
        Collections.sort(pointsY, (int[] o1, int[] o2) -> o1[0] - o2[0]);

        //Imprime la lista de puntos, en orden ascendente
        //System.out.println("Lista de puntos:");
        //showPoints(points, n);

        //Solución
        float p = closestPair(pointsX, pointsY);

        //Imprime la distancia mas corta
        //System.out.println("RECURSIVO");
        //System.out.println("Distancia de los puntos más cercanos: " + p);

        //Imprime el número de pares de coordenadas
        //System.out.println("Número de pares de coordenadas: " + n);

        //Imprime el tiempo de ejecución
        long fin = System.nanoTime();
        double tiempo = (double) ((fin - inicio) / 1000000);
        //System.out.println("Tiempo de ejecución: " + tiempo + " milisegundos");
        //System.out.println("------------------------------------------------");
      
        return tiempo;
    }

    //Valida si existe un elemento en la lista de puntos
    //Input: points (List int, una lista con coordenadas) , x (float, coordenada x), y (float, coordenada y)
    //Output: boolean (True or False)
    public static boolean existsElement(List<int[]> points, float x, float y) {
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i)[0] == x && points.get(i)[1] == y) {
                return true;
            }
        }
        return false;
    }

    //Generador de puntos pseudo-aleatorios 
    //Input: n (int, número de coordenadas) , minValue (int, valor mínimo de las coordenadas), maxValue (int, valor máximo de las coordenadas)
    //Output: points (List int, una lista con coordenadas)
    public static List<int[]> randomPoints(int n, int minValue, int maxValue) {
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

    public static double read(String filename) {
        File f = new File(filename);
        double tiempo = 0;

        try {
            Scanner sc = new Scanner(f);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] data = line.split(" ");
                tiempo += Double.parseDouble(data[1]);
            }
            sc.close();
        } catch (Exception err) {
            err.printStackTrace();
        }

        return tiempo;
    }

    public static void create(int n, double tiempo, String filename) {
        try {
            File f = new File(filename);
            //Si no existe el archivo txt, lo creamos
            if (!f.exists()) {
                f.createNewFile();
            }

            double tiempo_acumulado = read(filename);
            FileWriter w = new FileWriter(f, true);
            BufferedWriter bw = new BufferedWriter(w);
            PrintWriter wr = new PrintWriter(bw);
            //Escribimos en el archivo
            wr.write(n + " ");
            wr.append((tiempo + tiempo_acumulado) + "\n");
            wr.close();
            bw.close();
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    //Halla la distancia entre dos puntos con la ecuación d=√((x_2-x_1)²+(y_2-y_1)²)
    //Input: points (List int, una lista con coordenadas), i (int, funciona como índice), j (int, funciona como índice)
    //Output: float value, se castea el resultado de la ecuación
    public static float distance(List<int[]> points, int i, int j) {
        return (float) (Math.sqrt(Math.pow(points.get(j)[0] - points.get(i)[0], 2) + Math.pow(points.get(j)[1] - points.get(i)[1], 2)));
    }

    //Halla los puntos con menor distancia
    //Input: points (List int, una lista con coordenadas) , minDistance (float, guarda la mínima distancia)
    //Output: minDistance (float, guarda la mínima distancia)
    public static float bruteForce(List<int[]> points, float minDistance) {
        for (int i = 0; i < points.size() - 1; i++) {
            for (int j = i + 1; j < points.size(); j++) {
                minDistance = Math.min(minDistance, distance(points, i, j));
            }
        }
        return minDistance;
    }

    //Muestra la lista de puntos
    //Input: points (List int, una lista con coordenadas) , n (int, número de pares de coordenadas)
    //Output: N/A
    public static void showPoints(List<int[]> points, int n) {
        for (int i = 0; i < n; i++) {
            System.out.println("(" + points.get(i)[0] + ", " + points.get(i)[1] + "),");
        }
    }

    //Divide la lista en dos (A efectos prácticos, es la creación de cuadrantes)
    //Input: px (List int, contiene las coordenadas x), py (List int, contiene las coordenadas y)
    //Output: bruteForce(faltantes, Float.MAX_VALUE) o bruteForce(px, Float.MAX_VALUE); respectivo llamado al método bruteForce
    public static float closestPair(List<int[]> px, List<int[]> py) {
        if (px.size() > 3) {
            int n = (int) (px.size() / 2);
            List<int[]> px1 = new ArrayList<>(px.subList(0, n));
            List<int[]> px2 = new ArrayList<>(px.subList(n, px.size()));
            List<int[]> py1 = new ArrayList<>();
            List<int[]> py2 = new ArrayList<>();

            //Añade las coordenadas "y" al py1 o py2 a partir del punto de división
            for (int i = 0; i < py.size(); i++) {
                if (py.get(i)[0] < px.get(n)[0]) {
                    py1.add(py.get(i));
                } else {
                    py2.add(py.get(i));
                }
            }

            //Halla el valor mínimo entre dos coordenadas
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

}
