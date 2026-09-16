# Proyecto - Entrega 1

Programa en Java para la generacion de archivos planos de prueba para el proyecto de ventas.

## Descripcion

La clase `GenerateInfoFiles` genera de forma automatica los archivos necesarios para simular las ventas de una empresa:
- `productos.txt`: Contiene el listado de productos disponibles (ID, nombre y precio unitario).
- `vendedores.txt`: Contiene el listado de vendedores (tipo de documento, numero de documento, nombres y apellidos).
- `ventas_<nombre>_<id>.txt`: Archivos individuales con las ventas registradas para cada vendedor.

## Metodos principales

- `createProductsFile(int productsCount)`: Genera el archivo `productos.txt` con la cantidad de productos indicada.
- `createSalesManInfoFile(int salesmanCount)`: Genera el archivo `vendedores.txt` con informacion coherente de vendedores.
- `createSalesMenFile(int randomSalesCount, String name, long id)`: Genera el archivo de ventas individual para un vendedor dado.

## Como ejecutar el programa

### Desde Eclipse
1. Importar el proyecto: `File > Open Projects from File System` (o `Import > Existing Projects into Workspace`).
2. Seleccionar la carpeta del proyecto.
3. Abrir `src/GenerateInfoFiles.java`.
4. Clic derecho sobre el archivo y seleccionar `Run As > Java Application`.

### Desde la terminal
Estando en la carpeta del proyecto:
```bash
java src/GenerateInfoFiles.java
```

Al terminar, los archivos generados apareceran en la carpeta raiz del proyecto.
