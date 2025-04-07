# 8-puzzle
Amplitud (BFS)
# Implementación de la solución del 8-puzzle en Java.
- Búsqueda en amplitud para encontrar la secuencia de movimientos.
- Verificación de la viabilidad de la ruta: si la cantidad de movimientos excede un umbral 30 movimientoss,
se considera que el árbol de búsqueda es muy laborioso.
- Generación de un archivo DOT que muestra, de forma simplificada, el camino solución con direcciones (para
visualización con Graphviz).
- Se integra una interfaz gráfica utilizando Swing.
# Explicación y Justificación de la Ruta de Solución
Viabilidad del camino:
Utilicé el algoritmo de búsqueda en amplitud (BFS) que garantiza hallar el camino de solución con el menor número de movimientos posibles. Sin embargo, en el 8-puzzle el espacio de estados es limitado, pero en algunos casos el camino puede exceder un umbral. Si la solución es demasiado extensa, se considera que el árbol de búsqueda es muy laborioso. Esto se informa en la interfaz para que el usuario pueda justificar si vale la pena continuar.
# Generación del árbol completo:
Debido a que el árbol de búsqueda completo puede contener muchos nodos lo que lo haría inviable para procesar y visualizar, se ha optado por generar el archivo DOT únicamente del camino, solución encontrado. Esto permite ver el recorrido óptimo, incluyendo las direcciones y punteros, se utilizan los hashcode para simular las direcciones de memoria.
# La Clase
- La clase PuzzlePerez que se extiende JFrame: Se encarga de crear la interfaz gráfica.
Clases internas:
- Estado, modela la configuración del tablero (una matriz 3x3)
- Nodo, representa cada nodo en el árbol de búsqueda, guardando el estado, el nodo padre, el movimiento realizado y la profundidad.
# Métodos esenciales: 
- ejecutarSolucion(): Lee los estados ingresados, convierte los datos y lanza la búsqueda.
- busquedaEnAmplitud(): Implementa el algoritmo BFS para encontrar la solución.
- generarHijos(): Crea los nodos hijos con los movimientos válidos.
- reconstruirSolucion(): Recorre la cadena de nodos para reconstruir el camino solución.
- generarArchivoDot(): Genera un archivo DOT usado con Graphviz del camino solución encontrado.
- ## Compilar y Ejecutar el Proyecto:
- Copia y pega el código completo en tu proyecto de Apache NetBeans, (en este caso )IDE 22, -para que lo tomen en cuenta-.  
- Ejecuta la aplicación haciendo clic derecho sobre el proyecto y seleccionando Run 
# Se abrirá una ventana con la interfaz gráfica:
- Ingresar el Estado Inicial y el Estado Final (por ejemplo, 1,2,3,4,5,6,7,8,0 para el estado ordenado, donde 0 representará el espacio vacío).
- Pulsar Ejecutar Solución para iniciar la búsqueda.
# Como se muestra en esta imagen:
## <img width="707" alt="Captura de pantalla 2025-04-07 a la(s) 13 59 30" src="https://github.com/user-attachments/assets/65cd6ddd-9169-41dd-94ce-77b09405b0c1" />
# Árbol de Búsqueda:
- Pulsar Mostrar Árbol de Búsqueda paso 4 para generar el archivo DOT que podrás visualizar con Graphviz.
## <img width="707" alt="Captura de pantalla 2025-04-07 a la(s) 13 59 30" src="https://github.com/user-attachments/assets/f05a9419-90cd-46dd-aafd-ff82a6780694" />
- Una vez generado el archivo Solucion.dot en la carpeta raíz del proyecto
# Como se muestra en esta imagen:
## <img width="580" alt="Captura de pantalla 2025-04-07 a la(s) 15 35 28" src="https://github.com/user-attachments/assets/3132b704-92da-4617-8aef-48e2ce2e4c41" />
## Instala Graphviz:
- Para el caso de MacOs
- Instala Homebrew con este comando, sino lo tienes instalado
- /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
- Si ya lo tienes sigue este paso:
- Abre el terminal y agrega este comando
- brew install graphviz
- Para asegurarte que ya tienes instalado Graphviz puedes ejecutar este comando en el terminal:
- dot -V, //te devolverá la version de Graphviz instalada//
- Abre una terminal y navega hasta la carpeta del proyecto.
- O puedes dar clic derecho, luego te vas a servicios y Nuevo terminal en la carpeta
- ingresas este comando:
  dot -Tpng Solucion.dot -o Solucion.png
## Como se muestra en esta imagen:
## <img width="579" alt="Captura de pantalla 2025-04-07 a la(s) 15 48 51" src="https://github.com/user-attachments/assets/c7adc13c-a8a9-4587-9927-2ca135b8b1ed" />
## Genera el archivo .png como se muestra:
## <img width="620" alt="Captura de pantalla 2025-04-07 a la(s) 15 50 13" src="https://github.com/user-attachments/assets/4d45fb14-05b5-486b-a48c-d03714978c72" />
- Dentro de la misma ubicación se genera el archivo.png con el nombre de solución que podrás abrir con cualquier visualizador de image de tu preferencia.
- Imagen Solucion.png
## ![Solucion](https://github.com/user-attachments/assets/640dced7-f968-4c69-9fb5-e75e7cfdaa8a)

  
# Modelos. 
- debe crear las clases .java, con los siguientes nombres.
-	Contacto.java	-	Empresa.java
-	Pedido.java	-	Producto.java
-	Usuario.java
# Repositorios. 
- debe crear clases .java, con los siguientes nombres. 
-	ContactoRepositorio.java		-	EmpresaRepositorio.java
-	PedidoRepositorio.java		-	ProductoRepositorio.java
-	UsuarioRepositorio.java
