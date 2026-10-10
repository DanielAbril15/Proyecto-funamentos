import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

// Clase principal encargada de procesar las ventas y generar los reportes
public class main {

    // Nombres de los archivos de entrada
    public static final String ARCHIVO_PRODUCTOS = "productos.txt";
    public static final String ARCHIVO_VENDEDORES = "vendedores.txt";

    // Nombres de los archivos de salida de reportes
    public static final String ARCHIVO_REPORTE_VENDEDORES = "reporte_vendedores.csv";
    public static final String ARCHIVO_REPORTE_PRODUCTOS = "reporte_productos.csv";

    // Listas para almacenar la informacion de productos
    private static ArrayList<Long> idProductos = new ArrayList<Long>();
    private static ArrayList<String> nombresProductos = new ArrayList<String>();
    private static ArrayList<Double> preciosProductos = new ArrayList<Double>();
    private static ArrayList<Integer> cantidadesProductos = new ArrayList<Integer>();

    // Listas para almacenar la informacion de vendedores
    private static ArrayList<Long> docVendedores = new ArrayList<Long>();
    private static ArrayList<String> nombresVendedores = new ArrayList<String>();
    private static ArrayList<Double> ventasVendedores = new ArrayList<Double>();

    // Constructor privado para evitar instanciacion
    private main() {
    }

    // Metodo principal para procesar ventas y generar reportes
    public static void main(String[] args) {
        try {
            System.out.println("Iniciando Carga y consolidacion en memoria...");

            // 1. Cargar productos desde productos.txt
            cargarProductos(ARCHIVO_PRODUCTOS);
            System.out.println("Productos cargados en memoria: " + idProductos.size());

            // 2. Cargar vendedores desde vendedores.txt
            cargarVendedores(ARCHIVO_VENDEDORES);
            System.out.println("Vendedores cargados en memoria: " + docVendedores.size());

            // 3. Procesar archivos individuales de ventas
            procesarArchivosVentas();
            System.out.println("Archivos de ventas procesados y acumulados en memoria correctamente.");

            // 4. Mostrar resumen preliminar en consola
            System.out.println("\n--- Resumen de Vendedores ---");
            for (int i = 0; i < docVendedores.size(); i++) {
                System.out.println("- " + nombresVendedores.get(i) + " (Doc: " + docVendedores.get(i) + "): $"
                        + ventasVendedores.get(i));
            }

            System.out.println("\n--- Resumen de Productos Vendidos ---");
            for (int i = 0; i < idProductos.size(); i++) {
                int cantidad = cantidadesProductos.get(i);
                if (cantidad > 0) {
                    System.out.println("- " + nombresProductos.get(i) + ": " + cantidad
                            + " unidades vendidas (Precio: $" + preciosProductos.get(i) + ")");
                }
            }

            // 5. Generar archivo de reporte de vendedores ordenado por dinero recaudado
            generarReporteVendedores(ARCHIVO_REPORTE_VENDEDORES);
            System.out.println("\nArchivo de reporte de vendedores generado exitosamente: " + ARCHIVO_REPORTE_VENDEDORES);

            // 6. Generar archivo de reporte de productos ordenado por cantidad vendida
            generarReporteProductos(ARCHIVO_REPORTE_PRODUCTOS);
            System.out.println("Archivo de reporte de productos generado exitosamente: " + ARCHIVO_REPORTE_PRODUCTOS);

            System.out.println("\nProceso finalizado con exito.");

        } catch (Exception e) {
            System.out.println("Error durante la ejecucion: " + e.getMessage());
        }
    }

    // Lee el archivo productos.txt y llena las listas de productos
    // Formato por linea: IDProducto;NombreProducto;PrecioPorUnidadProducto
    public static void cargarProductos(String rutaArchivo) throws Exception {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("No se encontro el archivo de productos: " + rutaArchivo);
            return;
        }

        Scanner lector = new Scanner(archivo);
        while (lector.hasNextLine()) {
            String linea = lector.nextLine().trim();
            if (!linea.isEmpty()) {
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    long id = Long.parseLong(partes[0].trim());
                    String nombre = partes[1].trim();
                    double precio = Double.parseDouble(partes[2].trim());

                    idProductos.add(id);
                    nombresProductos.add(nombre);
                    preciosProductos.add(precio);
                    cantidadesProductos.add(0);
                }
            }
        }
        lector.close();
    }

    // Lee el archivo vendedores.txt y llena las listas de vendedores
    // Formato por linea: TipoDocumento;NumeroDocumento;NombresVendedor;ApellidosVendedor
    public static void cargarVendedores(String rutaArchivo) throws Exception {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("No se encontro el archivo de vendedores: " + rutaArchivo);
            return;
        }

        Scanner lector = new Scanner(archivo);
        while (lector.hasNextLine()) {
            String linea = lector.nextLine().trim();
            if (!linea.isEmpty()) {
                String[] partes = linea.split(";");
                if (partes.length >= 4) {
                    long numeroDoc = Long.parseLong(partes[1].trim());
                    String nombres = partes[2].trim();
                    String apellidos = partes[3].trim();

                    docVendedores.add(numeroDoc);
                    nombresVendedores.add(nombres + " " + apellidos);
                    ventasVendedores.add(0.0);
                }
            }
        }
        lector.close();
    }

    // Explora la carpeta del proyecto y procesa cada archivo de ventas
    public static void procesarArchivosVentas() throws Exception {
        File carpeta = new File(".");
        File[] archivos = carpeta.listFiles();

        if (archivos == null) {
            return;
        }

        for (int i = 0; i < archivos.length; i++) {
            File archivo = archivos[i];
            if (archivo.isFile()) {
                String nombre = archivo.getName();
                if (nombre.startsWith("ventas_") && nombre.endsWith(".txt")) {
                    procesarArchivoVenta(archivo);
                }
            }
        }
    }

    // Procesa un archivo individual de ventas de un vendedor
    // Primera linea: TipoDocumento;NumeroDocumento
    // Lineas siguientes: IDProducto;CantidadProducto;
    public static void procesarArchivoVenta(File archivo) throws Exception {
        Scanner lector = new Scanner(archivo);

        if (!lector.hasNextLine()) {
            lector.close();
            return;
        }

        // 1. Leer primera linea (TipoDocumento;NumeroDocumento)
        String primeraLinea = lector.nextLine().trim();
        String[] partesCabecera = primeraLinea.split(";");
        if (partesCabecera.length < 2) {
            lector.close();
            return;
        }

        long numeroDoc = Long.parseLong(partesCabecera[1].trim());

        // Buscar el indice del vendedor en docVendedores
        int indiceVendedor = -1;
        for (int i = 0; i < docVendedores.size(); i++) {
            if (docVendedores.get(i) == numeroDoc) {
                indiceVendedor = i;
                break;
            }
        }

        // 2. Leer las lineas de transacciones de ventas (IDProducto;Cantidad;)
        while (lector.hasNextLine()) {
            String linea = lector.nextLine().trim();
            if (!linea.isEmpty()) {
                String[] partes = linea.split(";");
                if (partes.length >= 2) {
                    long idProd = Long.parseLong(partes[0].trim());
                    int cantidad = Integer.parseInt(partes[1].trim());

                    // Buscar el indice del producto en idProductos
                    int indiceProd = -1;
                    for (int j = 0; j < idProductos.size(); j++) {
                        if (idProductos.get(j) == idProd) {
                            indiceProd = j;
                            break;
                        }
                    }

                    // Si encontramos el vendedor y el producto, sumamos los valores
                    if (indiceVendedor != -1 && indiceProd != -1) {
                        double subtotal = preciosProductos.get(indiceProd) * cantidad;

                        // Sumar dinero al vendedor
                        double dineroActual = ventasVendedores.get(indiceVendedor);
                        ventasVendedores.set(indiceVendedor, dineroActual + subtotal);

                        // Sumar cantidad al producto
                        int cantidadActual = cantidadesProductos.get(indiceProd);
                        cantidadesProductos.set(indiceProd, cantidadActual + cantidad);
                    }
                }
            }
        }

        lector.close();
    }

    // Genera el archivo CSV con los vendedores ordenados por dinero recaudado (mayor a menor)
    public static void generarReporteVendedores(String rutaArchivo) throws Exception {
        ArrayList<VendedorReporte> lista = new ArrayList<VendedorReporte>();

        for (int i = 0; i < docVendedores.size(); i++) {
            lista.add(new VendedorReporte(nombresVendedores.get(i), ventasVendedores.get(i)));
        }

        // Ordenar descendentemente por dinero recaudado
        Collections.sort(lista);

        File archivo = new File(rutaArchivo);
        PrintWriter escritor = new PrintWriter(archivo);

        for (int i = 0; i < lista.size(); i++) {
            VendedorReporte v = lista.get(i);
            escritor.println(v.nombre + ";" + v.dineroRecaudado);
        }

        escritor.close();
    }

    // Genera el archivo CSV con los productos vendidos ordenados por cantidad vendida (mayor a menor)
    public static void generarReporteProductos(String rutaArchivo) throws Exception {
        ArrayList<ProductoReporte> lista = new ArrayList<ProductoReporte>();

        for (int i = 0; i < idProductos.size(); i++) {
            int cantidad = cantidadesProductos.get(i);
            if (cantidad > 0) {
                lista.add(new ProductoReporte(nombresProductos.get(i), preciosProductos.get(i), cantidad));
            }
        }

        // Ordenar descendentemente por cantidad vendida
        Collections.sort(lista);

        File archivo = new File(rutaArchivo);
        PrintWriter escritor = new PrintWriter(archivo);

        for (int i = 0; i < lista.size(); i++) {
            ProductoReporte p = lista.get(i);
            escritor.println(p.nombre + ";" + p.precio);
        }

        escritor.close();
    }

    // Clase auxiliar interna para ordenar y representar el reporte de un vendedor
    private static class VendedorReporte implements Comparable<VendedorReporte> {
        String nombre;
        double dineroRecaudado;

        VendedorReporte(String nombre, double dineroRecaudado) {
            this.nombre = nombre;
            this.dineroRecaudado = dineroRecaudado;
        }

        // Comparador para ordenar de mayor a menor por dinero recaudado
        @Override
        public int compareTo(VendedorReporte otro) {
            return Double.compare(otro.dineroRecaudado, this.dineroRecaudado);
        }
    }

    // Clase auxiliar interna para ordenar y representar el reporte de un producto
    private static class ProductoReporte implements Comparable<ProductoReporte> {
        String nombre;
        double precio;
        int cantidadVendida;

        ProductoReporte(String nombre, double precio, int cantidadVendida) {
            this.nombre = nombre;
            this.precio = precio;
            this.cantidadVendida = cantidadVendida;
        }

        // Comparador para ordenar de mayor a menor por cantidad vendida
        @Override
        public int compareTo(ProductoReporte otro) {
            return Integer.compare(otro.cantidadVendida, this.cantidadVendida);
        }
    }
}
