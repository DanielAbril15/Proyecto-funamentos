# Proyecto - Sistema de Procesamiento de Ventas y Generacion de Reportes

Programa en Java para la generacion de archivos de prueba y el procesamiento, consolidacion y generacion de reportes ordenados de ventas.

Desarrollado para la asignatura **Fundamentos de Programacion** (Politecnico Grancolombiano).

---

## Estructura del Proyecto

El repositorio contiene exclusivamente dos clases con metodo `main`, ubicadas en la carpeta `src/`:

1. **`src/GenerateInfoFiles.java`**: Generador de archivos de prueba pseudoaleatorios.
2. **`src/main.java`**: Procesador de ventas y generador de reportes ordenados.

Adicionalmente, se incluye en la raiz del proyecto:
- **`conslusion.txt`**: Documento de reflexion sobre lo aprendido, aplicaciones profesionales y dificultades enfrentadas.

---

## 1. Generador de Archivos: `GenerateInfoFiles`

### Descripcion
Genera de forma automatica y coherente los archivos de entrada requeridos por el sistema:
- `productos.txt`: Catalogo de productos (`IDProducto;NombreProducto;PrecioPorUnidad`).
- `vendedores.txt`: Registro de vendedores (`TipoDocumento;NumeroDocumento;Nombres;Apellidos`).
- `ventas_<Nombre>_<ID>.txt`: Archivos individuales con las ventas registradas por cada vendedor (`IDProducto;Cantidad;`).

### Metodos requeridos implementados
- `createProductsFile(int productsCount)`: Crea el archivo de productos.
- `createSalesManInfoFile(int salesmanCount)`: Crea el archivo de vendedores.
- `createSalesMenFile(int randomSalesCount, String name, long id)`: Crea el archivo de ventas individual para un vendedor dado.

### Como ejecutar `GenerateInfoFiles`

#### Desde Eclipse
1. Abrir el proyecto en Eclipse.
2. Abrir el archivo `src/GenerateInfoFiles.java`.
3. Hacer clic derecho y seleccionar `Run As > Java Application`.

#### Desde la terminal
Estando en la raiz del proyecto:
```bash
java src/GenerateInfoFiles.java
```

---

## 2. Procesador y Generador de Reportes: `main`

### Descripcion
Lee los archivos generados, consolida la informacion en memoria y produce los archivos de reporte finales solicitados:

1. **Carga y consolidacion:**
   - Lee `productos.txt` y `vendedores.txt`.
   - Explora el directorio actual y procesa cada archivo `ventas_*.txt`.
   - Acumula el dinero total recaudado por cada vendedor y la cantidad de unidades vendidas por producto.

2. **Reporte de Vendedores (`reporte_vendedores.csv`):**
   - Contiene a todos los vendedores con el dinero total recaudado.
   - Formato por linea: `NombreCompleto;TotalDinero`
   - **Orden:** De mayor a menor dinero recaudado (descendente).

3. **Reporte de Productos (`reporte_productos.csv`):**
   - Contiene los productos vendidos por cantidad.
   - Formato por linea: `NombreProducto;Precio`
   - **Orden:** De mayor a menor cantidad de unidades vendidas (descendente).

### Requisito previo para la ejecucion
`main` debe ejecutarse **despues** de `GenerateInfoFiles`, ya que requiere que los archivos `productos.txt`, `vendedores.txt` y `ventas_*.txt` existan en la carpeta del proyecto.

### Como ejecutar `main`

#### Desde Eclipse
1. Abrir el archivo `src/main.java`.
2. Hacer clic derecho y seleccionar `Run As > Java Application`.
3. Se mostrara el mensaje de finalizacion exitosa y el resumen en la consola, y se generaran los archivos `reporte_vendedores.csv` y `reporte_productos.csv` en el proyecto.

#### Desde la terminal
Estando en la raiz del proyecto:
```bash
java src/main.java
```

---

## Caracteristicas Tecnicas y Buenas Practicas
- **Compatibilidad:** Java 8 o superior.
- **Sin interrupcion:** Los programas se ejecutan de forma autonoma sin solicitar datos por consola al usuario.
- **Documentacion:** Uso riguroso de comentarios de una sola linea (`//`), nombramiento claro de variables en camelCase y espaciado estructurado.
- **Restriccion de archivos:** Se mantiene estrictamente el limite de unicamente dos archivos `.java` en el proyecto.
