# Proyecto - Entrega 1 y 2

Programa en Java para la generacion de archivos planos de prueba y la consolidacion en memoria de las ventas de una empresa.

---

## 1. Generador de Archivos: `GenerateInfoFiles` (Entrega 1)

### Descripcion
La clase `GenerateInfoFiles` genera de forma automatica los archivos necesarios para simular las ventas de una empresa:
- `productos.txt`: Contiene el listado de productos disponibles (ID, nombre y precio unitario).
- `vendedores.txt`: Contiene el listado de vendedores (tipo de documento, numero de documento, nombres y apellidos).
- `ventas_<nombre>_<id>.txt`: Archivos individuales con las ventas registradas para cada vendedor.

### Metodos principales
- `createProductsFile(int productsCount)`: Genera el archivo `productos.txt` con la cantidad de productos indicada.
- `createSalesManInfoFile(int salesmanCount)`: Genera el archivo `vendedores.txt` con informacion coherente de vendedores.
- `createSalesMenFile(int randomSalesCount, String name, long id)`: Genera el archivo de ventas individual para un vendedor dado.

### Como ejecutar `GenerateInfoFiles`

#### Desde Eclipse
1. Importar el proyecto: `File > Open Projects from File System` (o `Import > Existing Projects into Workspace`).
2. Seleccionar la carpeta del proyecto.
3. Abrir `src/GenerateInfoFiles.java`.
4. Clic derecho sobre el archivo y seleccionar `Run As > Java Application`.

#### Desde la terminal
Estando en la carpeta del proyecto:
```bash
java src/GenerateInfoFiles.java
```
Al terminar, los archivos generados apareceran en la carpeta raiz del proyecto.

---

## 2. Procesador de Ventas: `main` (Entrega 2)

### Descripcion
La clase `main` se encarga de leer los archivos planos generados y procesar las ventas en memoria utilizando listas (`ArrayList`):
- Lee `productos.txt` y almacena los IDs, nombres, precios y cantidades vendidas.
- Lee `vendedores.txt` y almacena los documentos, nombres y dinero recaudado.
- Explora la carpeta del proyecto y procesa cada archivo `ventas_*.txt`.
- Para cada venta registrada, calcula el valor (`precio * cantidad`) y acumula el dinero recaudado al vendedor correspondiente y la cantidad de unidades vendidas al producto.
- Muestra por consola un resumen detallado con el total de dinero de cada vendedor y las unidades vendidas por cada producto.

### Cuando ejecutarlo
`main` debe ejecutarse **despues** de haber ejecutado `GenerateInfoFiles`, ya que requiere que los archivos `productos.txt`, `vendedores.txt` y `ventas_*.txt` existan previamente en la carpeta del proyecto.

### Como ejecutar `main`

#### Desde la terminal
Estando en la carpeta del proyecto:
```bash
java src/main.java
```
*(O si ingresas a la carpeta `src/`: `cd src` y luego `java main.java`).*

#### Desde Eclipse
1. Abrir `src/main.java`.
2. Clic derecho sobre el archivo y seleccionar `Run As > Java Application`.
3. El resumen de ventas aparecera en la consola de Eclipse.
