import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class main {

    // Nombres de los archivos principales
    public static final String ARCHIVO_PRODUCTOS = "productos.txt";
    public static final String ARCHIVO_VENDEDORES = "vendedores.txt";

    // Listas para almacenar la informacion de productos
    private static ArrayList<Long> idProductos = new ArrayList<Long>();
    private static ArrayList<String> nombresProductos = new ArrayList<String>();
    private static ArrayList<Double> preciosProductos = new ArrayList<Double>();
    private static ArrayList<Integer> cantidadesProductos = new ArrayList<Integer>();

    // Listas para almacenar la informacion de vendedores
    private static ArrayList<Long> docVendedores = new ArrayList<Long>();
    private static ArrayList<String> nombresVendedores = new ArrayList<String>();
    private static ArrayList<Double> ventasVendedores = new ArrayList<Double>();

    private main() {
    }

    // Carga y procesa las ventas
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

        } catch (Exception e) {
            System.out.println("Error durante la ejecucion: " + e.getMessage());
        }
    }

    // lee el archivo productos.txt y llena las listas de productos.
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

    // Lee el archivo vendedores.txt y llena las listas de vendedores.
    // Formato por linea:
    // TipoDocumento;NumeroDocumento;NombresVendedor;ApellidosVendedor

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
    // (ventas_*.txt).

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

    // Procesa un archivo individual de ventas de un vendedor.
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
}
