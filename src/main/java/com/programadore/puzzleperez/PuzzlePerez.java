/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

 package com.programadore.puzzleperez;

 /**
  *
  * @author perez
  */
 /*  
  * Proyecto PuzzlePerez utilizando búsqueda en amplitud (BFS)
  * Universidad Mariano Gálvez – Programación III
  * Bajo la dirección del ing.Samuel Bulux
  * 
  * Se implementa la solución del 8-puzzle en Java. El programa incluye:
  *  - Lectura del estado inicial y final (ingresados como 9 números separados por comas).
  *  - Búsqueda en amplitud para encontrar la secuencia de movimientos.
  *  - Verificación de la viabilidad de la ruta: si la cantidad de movimientos excede un umbral (por ejemplo, 30),
  *  - se considera que el árbol de búsqueda es muy laborioso.
  *  - Generación de un archivo DOT que muestra, de forma simplificada, el camino solución con direcciones (para 
  *    visualización con Graphviz).
  * 
  * Se integra una interfaz gráfica utilizando Swing.
  */
 
import java.awt.Color;
 import java.util.*;
 import javax.swing.*;
 import java.awt.GridLayout;
 //imports de java.awt
 
 import java.awt.event.*;
 import java.io.*;
 
 public class PuzzlePerez extends JFrame {
     
     //Interfaz gráfica
     private JTextField estadoInicialField;
     private JTextField estadoFinalField;
     private JButton btnResolver;
     private JButton btnMostrarArbol;
     private JTextArea areaSolucion;
     
     // Umbral para considerar que la ruta es muy extensa
     private final int UMBRAL_MOVIMIENTOS = 30;
     
     // Variable para almacenar la secuencia de movimientos de la solución
     private List<String> solucion;
     
     // Nodo solución obtenido de la búsqueda; se usa para generar el gráfico del camino
     private Nodo nodoSolucion;
     
     // Clase interna para representar el estado el puzzlePerez
     public class Estado {
         public int[][] tablero;
         
         // Constructor que recibe un arreglo de 9 enteros para formar el tablero 3x3
         public Estado(int[] arreglo) {
             tablero = new int[3][3];
             for (int i = 0; i < 9; i++) {
                 tablero[i / 3][i % 3] = arreglo[i];
             }
         }
         
         // Método para comparar dos estados (se sobrescribe equals)
         @Override
         public boolean equals(Object o) {
             if (o == this) return true;
             if (!(o instanceof Estado)) return false;
             Estado otro = (Estado) o;
             for (int i = 0; i < 3; i++) {
                 for (int j = 0; j < 3; j++) {
                     if (this.tablero[i][j] != otro.tablero[i][j])
                         return false;
                 }
             }
             return true;
         }
         
         // Se sobrescribe hashCode para poder usar este objeto en colecciones
         @Override
         public int hashCode() {
             return Arrays.deepHashCode(tablero);
         }
         
         // Método para clonar el estado, se crea un nuevo estado con el mismo arreglo
         public Estado clonar() {
             int[] copia = new int[9];
             for (int i = 0; i < 9; i++) {
                 copia[i] = tablero[i / 3][i % 3];
             }
             return new Estado(copia);
         }
         
         // Método para obtener una representación en String del estado
         @Override
         public String toString() {
             StringBuilder sb = new StringBuilder();
             for (int i = 0; i < 3; i++) {
                 sb.append(Arrays.toString(tablero[i])).append("\n");
             }
             return sb.toString();
         }
     }
     
     // Clase interna para representar un nodo del árbol de búsqueda
     public class Nodo {
         public Estado estado;       // Estado del puzzlePerez en este nodo
         public Nodo padre;          // Referencia al nodo padre (para reconstruir la solución)
         public String movimiento;   // Movimiento que se realizó para llegar a este estado
         public int profundidad;     // Profundidad del nodo en el árbol
         
         // Constructor del nodo
         public Nodo(Estado estado, Nodo padre, String movimiento, int profundidad) {
             this.estado = estado;
             this.padre = padre;
             this.movimiento = movimiento;
             this.profundidad = profundidad;
         }
     }
     
     // Constructor de la ventana principal
     public PuzzlePerez() {
         super("8 PuzzlePerez");
         // Configuración básica de la ventana
         setSize(700, 500);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
         // Se crea un panel principal con un layout en cuadrícula
         JPanel panel = new JPanel();
         panel.setLayout(new GridLayout(7, 2));
         panel.setBackground(Color.GRAY);  // Cambia el fondo del panel
         panel.setForeground(Color.red);
         
         // Etiqueta entrada para el estado inicial
         panel.add(new JLabel("\nInicio, ingrese 9 números separados por comas(,): Recuerde tomar en cuenta el numero 0"));
         estadoInicialField = new JTextField();
         panel.add(estadoInicialField);
         
         
         // Etiqueta entrada para el estado final hacia el objetivo
         panel.add(new JLabel("Fin, Ingrese 9 números separados por comas(,): Recuerde tomar en cuenta el número 0\n"));
         estadoFinalField = new JTextField();
         panel.add(estadoFinalField);
         
         // Botón para ejecutar la solución
         btnResolver = new JButton("Buscar Solución");
         //btnResolver.setBackground(Color.CYAN);  // Cambia el color del botón
         //btnResolver.setForeground(Color.BLACK);
         panel.add(btnResolver);
         
         // Botón para mostrar el árbol (en este caso, se genera el DOT del camino solución)
         btnMostrarArbol = new JButton("Generar árbol de Búsqueda.dot");
        //btnMostrarArbol.setBackground(Color.WHITE);  // Ejemplo: fondo rosa
        //btnMostrarArbol.setForeground(Color.BLACK);
         panel.add(btnMostrarArbol);
         
         // Área de texto para mostrar resultados
         areaSolucion = new JTextArea();
         JScrollPane scrollPane = new JScrollPane(areaSolucion);
         // Se agrega el scroll al panel (ocupa dos celdas de la cuadrícula)
         areaSolucion.setBorder(BorderFactory.createTitledBorder("Solución"));
         areaSolucion.setLineWrap(true);         // Permite el ajuste automático de línea
         areaSolucion.setWrapStyleWord(true);    // Ajusta las líneas sin cortar palabras
         panel.add(scrollPane);
         
         add(panel);
         // Acción del botón "Ejecutar Solución"
         btnResolver.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                 ejecutarSolucion();
             }
         });
         
         // Acción del botón "Mostrar Árbol de Búsqueda"
         btnMostrarArbol.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                 generarArchivoDot();
             }
         });
     }
     
     // Método para ejecutar la solución del puzzlePerez
     public void ejecutarSolucion() {
         // Se leen las cadenas ingresadas para estado inicial y final
         String inicialStr = estadoInicialField.getText();
         String finalStr = estadoFinalField.getText();
         
         // Se convierten las cadenas a arreglos de enteros
         int[] inicial = convertirStringAArreglo(inicialStr);
         int[] objetivo = convertirStringAArreglo(finalStr);
         
         if (inicial == null || objetivo == null) {
             areaSolucion.setText("Error en el formato de entrada. Ingrese 9 números separados por comas(,).");
             return;
         }
         
         // Se crean los estados inicial y objetivo
         Estado estadoInicial = new Estado(inicial);
         Estado estadoObjetivo = new Estado(objetivo);
         
         // Se inicia la búsqueda en amplitud (BFS)
         long inicioTiempo = System.currentTimeMillis();
         Nodo nodoSol = busquedaEnAmplitud(estadoInicial, estadoObjetivo);
         long finTiempo = System.currentTimeMillis();
         long tiempoEjecucion = finTiempo - inicioTiempo;
         
         // Se muestra la solución (o mensaje de error si no se encuentra solución)
         if (nodoSol != null) {
             // Se reconstruye la secuencia de movimientos a partir del nodo solución
             solucion = reconstruirSolucion(nodoSol);
             StringBuilder sb = new StringBuilder();
             sb.append("Solución encontrada en ").append(tiempoEjecucion).append(" milisegundos.\n");
             sb.append("Número de movimientos: ").append(solucion.size()).append("\n");
             sb.append("Movimientos: ").append(solucion.toString()).append("\n");
             // Verificar si la ruta es demasiado extensa
             if (solucion.size() > UMBRAL_MOVIMIENTOS) {
                 sb.append("La ruta de solución es muy extensa, lo que indica que el árbol de búsqueda es muy laborioso.\n");
             }
             areaSolucion.setText(sb.toString());
             // Se guarda el nodo solución para la generación del gráfico del camino solución
             nodoSolucion = nodoSol;
         } else {
             areaSolucion.setText("No se encontró solución.");
         }
     }
     
     // Método para convertir una cadena (con números separados por comas) a un arreglo de 9 enteros
     public int[] convertirStringAArreglo(String s) {
         try {
             String[] partes = s.split(",");
             if (partes.length != 9) return null;
             int[] arreglo = new int[9];
             for (int i = 0; i < 9; i++) {
                 arreglo[i] = Integer.parseInt(partes[i].trim());
             }
             return arreglo;
         } catch (Exception e) {
             return null;
         }
     }
     
     // Implementación de la búsqueda en amplitud (BFS) para el puzzlePerez
     public Nodo busquedaEnAmplitud(Estado inicio, Estado objetivo) {
         Queue<Nodo> cola = new LinkedList<>();         // Cola de nodos a explorar
         Set<Estado> visitados = new HashSet<>();         // Conjunto de estados ya visitados
         
         // Se crea el nodo inicial
         Nodo nodoInicio = new Nodo(inicio, null, "Inicio", 0);
         cola.add(nodoInicio);
         visitados.add(inicio);
         
         // Bucle principal de la búsqueda
         while (!cola.isEmpty()) {
             Nodo actual = cola.poll();
             
             // Si el estado actual es igual al estado objetivo, se retorna el nodo solución
             if (actual.estado.equals(objetivo)) {
                 return actual;
             }
             
             // Se generan los nodos hijos (movimientos válidos)
             List<Nodo> hijos = generarHijos(actual);
             for (Nodo hijo : hijos) {
                 if (!visitados.contains(hijo.estado)) {
                     cola.add(hijo);
                     visitados.add(hijo.estado);
                 }
             }
         }
         // Si se agotan los nodos sin hallar solución, se retorna null
         return null;
     }
     
     // Método para generar nodos hijos a partir de un nodo dado
     public List<Nodo> generarHijos(Nodo nodo) {
         List<Nodo> hijos = new ArrayList<>();
         int fila = -1, col = -1;
         
         // Se busca la posición del espacio vacío (representado por 0)
         for (int i = 0; i < 3; i++) {
             for (int j = 0; j < 3; j++) {
                 if (nodo.estado.tablero[i][j] == 0) {
                     fila = i;
                     col = j;
                 }
             }
         }
         
         // Definición de los movimientos: arriba, abajo, izquierda y derecha
         int[][] direcciones = { {-1,0}, {1,0}, {0,-1}, {0,1} };
         String[] nombresMovimientos = { "Arriba", "Abajo", "Izquierda", "Derecha" };
         
         // Se itera sobre cada posible movimiento
         for (int k = 0; k < 4; k++) {
             int nuevaFila = fila + direcciones[k][0];
             int nuevaCol = col + direcciones[k][1];
             // Se verifica que la nueva posición esté dentro del tablero
             if (nuevaFila >= 0 && nuevaFila < 3 && nuevaCol >= 0 && nuevaCol < 3) {
                 // Se clona el estado actual para modificarlo
                 Estado nuevoEstado = nodo.estado.clonar();
                 // Se intercambian la posición del espacio y la ficha en la nueva posición
                 nuevoEstado.tablero[fila][col] = nuevoEstado.tablero[nuevaFila][nuevaCol];
                 nuevoEstado.tablero[nuevaFila][nuevaCol] = 0;
                 // Se crea un nuevo nodo con el estado generado
                 Nodo nuevoNodo = new Nodo(nuevoEstado, nodo, nombresMovimientos[k], nodo.profundidad + 1);
                 hijos.add(nuevoNodo);
             }
         }
         return hijos;
     }
     
     // Método para reconstruir la secuencia de movimientos (camino solución) desde el nodo solución hasta el nodo inicial
     public List<String> reconstruirSolucion(Nodo nodo) {
         List<String> movimientos = new ArrayList<>();
         // Se recorre la cadena de nodos utilizando el puntero padre
         while (nodo.padre != null) {
             movimientos.add(0, nodo.movimiento); // Se inserta al inicio para mantener el orden correcto
             nodo = nodo.padre;
         }
         return movimientos;
     }
     
     // Método para generar un archivo DOT que represente el camino solución, no se genera el árbol completo para evitar
     // que sea muy extenso si el número de nodos es muy alto.
     public void generarArchivoDot() {
         if (nodoSolucion == null) {
             areaSolucion.setText("No se ha ejecutado la búsqueda o no se encontró solución.");
             return;
         }
         
         StringBuilder dot = new StringBuilder();
         dot.append("digraph CaminoSolucion {\n");
         dot.append("node [shape=record];\n");
         
         // Se reconstruye el camino desde el nodo solución hasta el inicio
         List<Nodo> camino = new ArrayList<>();
         Nodo actual = nodoSolucion;
         while (actual != null) {
             camino.add(0, actual);
             actual = actual.padre;
         }
         
         // Se agregan los nodos y aristas al archivo DOT
         for (int i = 0; i < camino.size(); i++) {
             Nodo nodo = camino.get(i);
             String nodoId = "n" + nodo.hashCode();
             dot.append(nodoId + " [label=\"{" + nodoId + " | " + nodo.movimiento + " | Prof:" + nodo.profundidad + "}\"];\n");
             if (i < camino.size() - 1) {
                 Nodo siguiente = camino.get(i + 1);
                 String siguienteId = "n" + siguiente.hashCode();
                 dot.append(nodoId + " -> " + siguienteId + " [label=\"" + siguiente.movimiento + "\"];\n");
             }
         }
         dot.append("}\n");
         
         // Se guarda el archivo DOT en la carpeta raíz
         try {
             FileWriter writer = new FileWriter("Solucion.dot");
             writer.write(dot.toString());
             writer.close();
             areaSolucion.setText(areaSolucion.getText() + "\nArchivo Solucion.dot generado exitosamente.\nUtiliza Graphviz para visualizar el camino.");
         } catch (IOException e) {
             areaSolucion.setText("Error al generar el archivo DOT.");
         }
     }
     
     // Método main para iniciar la aplicación
     public static void main(String[] args) {
         // Se arranca la aplicación en el hilo de eventos de Swing
         SwingUtilities.invokeLater(new Runnable() {
             public void run() {
                 new PuzzlePerez().setVisible(true);
             }
         });
     }
 }
 
 