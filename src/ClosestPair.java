
/*

Algoritmos y complejidad IST 4310 - 01
NRC: 3264
@author Elkin Luis Arteaga Sánchez
Código: 200153212
Actividad: Lab Closest Pair
Fecha: 04/11/2022
Descripción: Algoritmo que encuentra y muestra la distancia del par de coordenadas (x,y) más cercano.

https://github.com/RV-Levi/LabClosestPair
https://github.com/RV-Levi/Plot_Coords

 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/*
 *
 */
/**
 *
 * @author elarteaga
 */
public class ClosestPair {

    public static void main(String[] args) {
        ClosestPair m = new ClosestPair();

        //Datos de pruebas
        int n = 10;
        int limite_n = 50;
        int limite_experimentos = 50;
        int minValue = -200;
        int maxValue = 200 + 1; // Tiene un "+ 1" porque es un límite exclusivo en el método ints()

		//Experimentos
        for (int i = 0; i < limite_n; i++) {
		    //Llamado de puntos pseudo-aleatorios
        	List<int[]> points = randomPoints(n, minValue, maxValue);
            double avg_iterative = 0;
            double avg_recursive = 0;
            for(int j = 0; j < limite_experimentos; j++) {
                avg_iterative += m.iterativo(points, n, minValue, maxValue);
                avg_recursive += m.recursivo(points, n, minValue, maxValue);
            }
			//Tiempos de ejecución promedios
            avg_iterative /= limite_experimentos;
            avg_recursive /= limite_experimentos;
			//Creación o verificación de los archivos .txt
            create(n, avg_iterative, "dataIterativo.txt");
            create(n, avg_recursive, "dataRecursivo.txt");
            n += 10;
        }
    }

	//Organiza los experimentos de forma iterativa
    //Input: points (List int, una lista con coordenadas) , minValue (int, valor mínimo de las coordenadas), maxValue (int, valor máximo de las coordenadas)
    //Output: tiempo (double, tiempo de ejecución)
    public double iterativo(List<int[]> points, int n, int minValue, int maxValue) {
        double inicio = System.nanoTime();

        //Ordena los puntos de menor a mayor
        Collections.sort(points, (int[] o1, int[] o2) -> o1[0] - o2[0]);

        //Imprime la lista de puntos, en orden ascendente
        //System.out.println("Lista de puntos:");
        //showPoints(points, n);

        //Solución
        double p = bruteForce(points, Double.MAX_VALUE);

        //Imprime la distancia mas corta
        //System.out.println("ITERATIVO");
        //System.out.println("Distancia de los puntos más cercanos: " + p);

        //Imprime el número de pares de coordenadas
        //System.out.println("Número de pares de coordenadas: " + n);

        //Imprime el tiempo de ejecución
        double fin = System.nanoTime();
        double tiempo = (fin - inicio) / 1000000;
        //System.out.println("Tiempo de ejecución: " + tiempo + " milisegundos");
        //System.out.println("------------------------------------------------");
        
        return tiempo;
    }

	//Organiza los experimentos de forma recursiva
    //Input: points (List int, una lista con coordenadas) , minValue (int, valor mínimo de las coordenadas), maxValue (int, valor máximo de las coordenadas)
    //Output: tiempo (double, tiempo de ejecución)
    public double recursivo(List<int[]> points, int n, int minValue, int maxValue) {
        double inicio = System.nanoTime();

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
        double p = closestPair(pointsX, pointsY);

        //Imprime la distancia mas corta
        //System.out.println("RECURSIVO");
        //System.out.println("Distancia de los puntos más cercanos: " + p);

        //Imprime el número de pares de coordenadas
        //System.out.println("Número de pares de coordenadas: " + n);

        //Imprime el tiempo de ejecución
        double fin = System.nanoTime();
        double tiempo = (fin - inicio) / 1000000;
        //System.out.println("Tiempo de ejecución: " + tiempo + " milisegundos");
        //System.out.println("------------------------------------------------");
      
        return tiempo;
    }

    //Valida si existe un elemento en la lista de puntos
    //Input: points (List int, una lista con coordenadas) , x (double, coordenada x), y (double, coordenada y)
    //Output: Boolean value
    public static boolean existsElement(List<int[]> points, double x, double y) {
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i)[0] == x && points.get(i)[1] == y) {
                return true;
            }
        }
        return false;
    }

    //Organiza los llamados al generador de puntos pseudo-aleatorios 
    //Input: n (int, número de coordenadas), minValue (int, valor mínimo de las coordenadas), maxValue (int, valor máximo de las coordenadas)
    //Output: points (List int, una lista con coordenadas)
    public static List<int[]> randomPoints(int n, int minValue, int maxValue) {
        List<int[]> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x = intRandom(minValue, maxValue);
            int y = intRandom(minValue, maxValue);
            while (existsElement(points, x, y)) {
                x = intRandom(minValue, maxValue);
                y = intRandom(minValue, maxValue);
            }
            points.add(new int[]{x, y});
        }
        return points;
    }

	//Genera enteros aleatorios usando java.util.Random
	//Input: minValue (int, valor mínimo de las coordenadas), maxValue (int, valor máximo de las coordenadas)
    //Output: int value
    public static int intRandom(int minValue, int maxValue) {
        return (new Random()).ints(minValue, maxValue).findFirst().getAsInt();
    }

	//Lee los archivos .txt
	//Input: filename (String, nombre del archivo)
    //Output: tiempo (double, tiempo de ejecución)
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

	//Verifica si existen los archivos .txt, sino, los crea, también establece la forma de organización de la información
	//Input: n (int, número de pares de coordenadas) , tiempo (double, tiempo de ejecución), filename (String, nombre del archivo)
    //Output: N/A
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

	//Método que al recibir una base y una exponente, devuelve el resultado de base^exponente
	//Input: base (double, término a multiplicar cuantas veces indique exponente) , exponente (double, término que indica cuantas veces se multiplica base)
    //Output: resultado (double, resultado de la operación)
	public static double potencia(double base, double exponente){
        double resultado = 1;
        if (base > 0 && exponente == 0){
            return resultado;
		} else if (base == 0 && exponente >= 1){
            return 0;
        } else {
            for (int i = 1; i <= exponente; i++){
                resultado *= base;
            }
            return resultado;
        }
    }
	
    //Halla la distancia entre dos puntos con la ecuación d=√((x_2-x_1)²+(y_2-y_1)²), en este caso, se omite la raíz para reducir el tiempo de ejecución
    //Input: points (List int, una lista con coordenadas), i (int, funciona como índice), j (int, funciona como índice)
    //Output: double value
    public static double distance(List<int[]> points, int i, int j) {
        return (potencia(points.get(j)[0] - points.get(i)[0], 2) + potencia(points.get(j)[1] - points.get(i)[1], 2));
    }

    //Halla los puntos con menor distancia
    //Input: points (List int, una lista con coordenadas) , minDistance (double, guarda la distancia mínima)
    //Output: minDistance (double, guarda la distancia mínima)
    public static double bruteForce(List<int[]> points, double minDistance) {
        for (int i = 0; i < points.size() - 1; i++) {
            for (int j = i + 1; j < points.size(); j++) {
                minDistance = min_entre_dos(minDistance, distance(points, i, j));
            }
        }
        return minDistance;
    }

	//Halla el mínimo valor dado dos números
    //Input: num1 (double, valor a comparar) , num2 (double, valor a comparar)
    //Output: double value
	public static double min_entre_dos(double num1, double num2) {
		if (num1 < num2){
			return num1;
		} else {
			return num2;
		}
	}
	
    //Muestra la lista de puntos, se omite su uso para mejorar el tiempo de ejecución y por accesibilidad a las pc de bajos recursos
    //Input: points (List int, una lista con coordenadas) , n (int, número de pares de coordenadas)
    //Output: N/A
	public static int j = 1;
    public static void showPoints(List<int[]> points, int n) {
        for (int i = 0; i < n; i++) {
            System.out.println("(" + points.get(i)[0] + ", " + points.get(i)[1] + ")");
        }
        System.out.println(j + " --------------------------------------");
        j++;
    }

    //Divide la lista en dos (A efectos prácticos, es la creación de cuadrantes)
    //Input: px (List int, contiene las coordenadas x), py (List int, contiene las coordenadas y)
    //Output: bruteForce(faltantes, Double.MAX_VALUE) o bruteForce(px, Double.MAX_VALUE); respectivo llamado al método bruteForce
    public static double closestPair(List<int[]> px, List<int[]> py) {
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
            double minimo = min_entre_dos(closestPair(px1, py1), closestPair(px2, py2));
            List<int[]> faltantes = new ArrayList<>();

            for (int i = 0; i < py.size(); i++) {
                if (px1.get(px1.size() - 1)[0] - minimo < py.get(i)[0] && py.get(i)[0] < px1.get(px1.size() - 1)[0] + minimo) {
                    faltantes.add(py.get(i));
                }
            }
            return bruteForce(faltantes, Double.MAX_VALUE);
        } else {
            return bruteForce(px, Double.MAX_VALUE);
        }
    }

}
