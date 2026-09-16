import java.io.File;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

// Clase encargada de generar los archivos planos de prueba
public class GenerateInfoFiles {

    // Nombres de los archivos principales
    public static final String ARCHIVO_PRODUCTOS = "productos.txt";
    public static final String ARCHIVO_VENDEDORES = "vendedores.txt";

    // Generador de numeros aleatorios
    private static final Random random = new Random();

    // Listas base para generar datos
    private static final String[] TIPOS_DOCUMENTO = { "CC", "CC", "CC", "TI", "CE" };

    private static final String[] NOMBRES = {
        "Carlos", "Maria", "Juan", "Laura", "Andres",
        "Sofia", "David", "Valentina", "Daniel", "Camilo",
        "Paula", "Felipe", "Isabella", "Mateo", "Diana",
        "Alejandro", "Mariana", "Sebastian", "Natalia", "Santiago"
    };

    private static final String[] APELLIDOS = {
        "Gomez", "Rodriguez", "Martinez", "Lopez", "Perez",
        "Gonzalez", "Hernandez", "Sanchez", "Ramirez", "Torres",
        "Diaz", "Vargas", "Castro", "Morales", "Romero"
    };

    // Lista base de productos
    private static final String[][] PRODUCTOS_BASE = {
        { "Arroz Diana 1kg", "4500.0" },
        { "Aceite Gourmet 1L", "14500.0" },
        { "Leche Entera Colanta 1L", "4200.0" },
        { "Azucar Manuelita 1kg", "3800.0" },
        { "Cafe Sello Rojo 500g", "16000.0" },
        { "Pasta Doria 500g", "3200.0" },
        { "Huevos AA x30", "18000.0" },
        { "Pan Tajado Bimbo", "6500.0" },
        { "Lentejas Diana 500g", "4000.0" },
        { "Atun Van Camps 160g", "7500.0" },
        { "Jabon Rey Barra", "2500.0" },
        { "Detergente Ariel 1kg", "12500.0" },
        { "Crema Dental Colgate 100ml", "5800.0" },
        { "Papel Higienico Familia x4", "8900.0" },
        { "Shampoo Sedal 350ml", "11000.0" },
        { "Galletas Noel 200g", "4200.0" },
        { "Chocolate Corona 500g", "7800.0" },
        { "Sal Refisal 1kg", "2100.0" },
        { "Frijol Bola Roja 500g", "6200.0" },
        { "Harina PAN 1kg", "4800.0" }
    };

    // Metodo principal
    public static void main(String[] args) {
        try {
            System.out.println("Iniciando generacion de archivos de prueba...");

            // Crear archivo productos
            int cantidadProductos = 10;
            createProductsFile(cantidadProductos);
            System.out.println("Archivo de productos creado exitosamente.");

            // Crear archivo vendedores
            int cantidadVendedores = 5;
            createSalesManInfoFile(cantidadVendedores);
            System.out.println("Archivo de vendedores creado exitosamente.");

            // Crear archivos ventas para cada vendedor
            generarArchivosVentasParaVendedores();
            System.out.println("Archivos de ventas creados exitosamente.");

            System.out.println("Proceso finalizado con exito.");

        } catch (Exception e) {
            System.out.println("Error al generar los archivos: " + e.getMessage());
        }
    }

    // Crea archivo con informacion de productos
    public static void createProductsFile(int productsCount) throws Exception {
        if (productsCount <= 0) {
            System.out.println("La cantidad de productos debe ser mayor a cero.");
            return;
        }

        File archivo = new File(ARCHIVO_PRODUCTOS);
        PrintWriter escritor = new PrintWriter(archivo);

        for (int i = 1; i <= productsCount; i++) {
            String nombre;
            String precio;

            if (i <= PRODUCTOS_BASE.length) {
                nombre = PRODUCTOS_BASE[i - 1][0];
                precio = PRODUCTOS_BASE[i - 1][1];
            } else {
                nombre = "Producto " + i;
                double precioCalculado = 2000.0 + (random.nextInt(30) * 500.0);
                precio = String.valueOf(precioCalculado);
            }

            escritor.println(i + ";" + nombre + ";" + precio);
        }

        escritor.close();
    }

    // Crea archivo con informacion de vendedores
    public static void createSalesManInfoFile(int salesmanCount) throws Exception {
        if (salesmanCount <= 0) {
            System.out.println("La cantidad de vendedores debe ser mayor a cero.");
            return;
        }

        File archivo = new File(ARCHIVO_VENDEDORES);
        PrintWriter escritor = new PrintWriter(archivo);

        long documentoBase = 1001000L;

        for (int i = 1; i <= salesmanCount; i++) {
            String tipoDoc = TIPOS_DOCUMENTO[random.nextInt(TIPOS_DOCUMENTO.length)];
            long numeroDoc = documentoBase + i;

            String primerNombre = NOMBRES[random.nextInt(NOMBRES.length)];
            String primerApellido = APELLIDOS[random.nextInt(APELLIDOS.length)];
            String segundoApellido = APELLIDOS[random.nextInt(APELLIDOS.length)];
            while (segundoApellido.equals(primerApellido)) {
                segundoApellido = APELLIDOS[random.nextInt(APELLIDOS.length)];
            }

            String apellidosCompletos = primerApellido + " " + segundoApellido;

            escritor.println(tipoDoc + ";" + numeroDoc + ";" + primerNombre + ";" + apellidosCompletos);
        }

        escritor.close();
    }

    // Crea archivo de ventas de un vendedor
    public static void createSalesMenFile(int randomSalesCount, String name, long id) throws Exception {
        if (randomSalesCount < 0) {
            System.out.println("La cantidad de ventas no puede ser negativa.");
            return;
        }

        String nombreSeguro = (name != null && !name.trim().isEmpty())
                ? name.trim().replaceAll("\\s+", "_")
                : "Vendedor";

        String nombreArchivo = "ventas_" + nombreSeguro + "_" + id + ".txt";
        File archivo = new File(nombreArchivo);
        PrintWriter escritor = new PrintWriter(archivo);

        String tipoDocumento = obtenerTipoDocumentoPorId(id);
        int maxIdProducto = obtenerCantidadProductosDisponibles();

        // Primera linea: TipoDocumento;NumeroDocumento
        escritor.println(tipoDocumento + ";" + id);

        // Lineas de ventas: IDProducto;Cantidad;
        for (int i = 0; i < randomSalesCount; i++) {
            int idProducto = 1 + random.nextInt(maxIdProducto);
            int cantidadVendida = 1 + random.nextInt(10);

            escritor.println(idProducto + ";" + cantidadVendida + ";");
        }

        escritor.close();
    }

    // Lee vendedores.txt y genera un archivo de ventas para cada uno
    private static void generarArchivosVentasParaVendedores() throws Exception {
        File archivoVendedores = new File(ARCHIVO_VENDEDORES);
        if (!archivoVendedores.exists()) {
            return;
        }

        Scanner lector = new Scanner(archivoVendedores);
        while (lector.hasNextLine()) {
            String linea = lector.nextLine().trim();
            if (!linea.isEmpty()) {
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    long numeroDoc = Long.parseLong(partes[1].trim());
                    String nombres = partes[2].trim();

                    int cantidadVentas = 3 + random.nextInt(6);
                    createSalesMenFile(cantidadVentas, nombres, numeroDoc);
                }
            }
        }
        lector.close();
    }

    // Obtiene el tipo de documento de un vendedor buscando por su ID
    private static String obtenerTipoDocumentoPorId(long id) {
        File archivoVendedores = new File(ARCHIVO_VENDEDORES);
        if (!archivoVendedores.exists()) {
            return "CC";
        }

        try {
            Scanner lector = new Scanner(archivoVendedores);
            while (lector.hasNextLine()) {
                String linea = lector.nextLine().trim();
                String[] partes = linea.split(";");
                if (partes.length >= 2) {
                    long docLeido = Long.parseLong(partes[1].trim());
                    if (docLeido == id) {
                        lector.close();
                        return partes[0].trim();
                    }
                }
            }
            lector.close();
        } catch (Exception e) {
            return "CC";
        }

        return "CC";
    }

    // Cuenta cuantos productos hay en productos.txt
    private static int obtenerCantidadProductosDisponibles() {
        File archivoProductos = new File(ARCHIVO_PRODUCTOS);
        if (!archivoProductos.exists()) {
            return 10;
        }

        int contador = 0;
        try {
            Scanner lector = new Scanner(archivoProductos);
            while (lector.hasNextLine()) {
                lector.nextLine();
                contador++;
            }
            lector.close();
        } catch (Exception e) {
            return 10;
        }

        return (contador > 0) ? contador : 10;
    }
}
