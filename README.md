# Motores JEY · Módulo Gestión de Clientes
Módulo del sistema de gestión **Motores JEY**, desarrollado por **YIE Software Group S.A.S.** para el taller de motocicletas Motores Jey.
Permite **registrar, consultar, actualizar y eliminar clientes** conectándose a una base de datos MySQL por medio de **JDBC**.
- 
**Aprendiz:** Iván Sierra
- 
**Programa:** Análisis y Desarrollo de Software (SENA) · Ficha 3336046
- 
**Evidencia:** GA7-220501096-AA2-EV01 · Codificación de módulos del software
## Requisitos que cubre
Este módulo implementa el **Módulo 2: Clientes** de la propuesta técnica del proyecto:
- 
**RF-03 · Registro y CRUD de Clientes** (prioridad alta): crear, consultar, actualizar e inactivar perfiles de clientes, validando la unicidad del número de identificación.
  - 
Crear → opción 1 · Consultar → opciones 2 y 3 · Actualizar e inactivar → opción 4 · Eliminar → opción 5.
  - 
La unicidad del documento la garantiza el índice único `uq_personas_documento` de la base de datos, y el menú muestra un mensaje claro si se repite.
## Tecnologías
| Tecnología | Versión | Uso |
|---|---|---|
| Java (JDK) | 26 | Lenguaje de programación |
| Apache Maven | 3.x | Gestión del proyecto y dependencias |
| JDBC (`java.sql`) | — | Conexión a la base de datos |
| MySQL Connector/J | 9.7.0 | Driver JDBC para MySQL |
| MySQL Server | 8.x | Base de datos relacional |
| Apache NetBeans | — | Entorno de desarrollo |
| Git y GitHub | — | Control de versiones |
## Estructura del proyecto
```
src/main/java/com/yiesoftware/motoresjey/
├── Principal.java              → punto de entrada, abre el menú
├── conexion/ConexionBD.java    → entrega conexiones JDBC (lee db.properties)
├── modelo/Cliente.java         → representa a un cliente (personas + clientes)
├── dao/ClienteDAO.java         → interfaz con las operaciones CRUD
├── dao/ClienteDAOImpl.java     → implementación JDBC con PreparedStatement y transacciones
└── vista/MenuClientes.java     → menú de consola con validaciones
src/main/resources/
└── db.properties.ejemplo       → plantilla de configuración de la conexión
bd/
└── script_final_motores_jey.sql → script para crear la base de datos
```
Se aplica el patrón **DAO** (Data Access Object): solo la capa `dao` accede a la base de datos, y el menú trabaja contra la interfaz `ClienteDAO`.
## Funcionalidades
| Opción del menú | Operación | Método del DAO |
|---|---|---|
| 1. Registrar cliente | Inserción (con transacción en `personas` y `clientes`) | `insertar` |
| 2. Buscar cliente por id | Consulta | `buscarPorId` |
| 3. Listar clientes | Consulta | `listarTodos` |
| 4. Actualizar cliente | Actualización (datos de contacto y estado ACTIVO/INACTIVO) | `actualizar` |
| 5. Eliminar cliente | Eliminación (con transacción; se bloquea si el cliente tiene motos) | `eliminar` |
## Cómo ejecutarlo
1. 
**Crear la base de datos:** abrir `bd/script_final_motores_jey.sql` en MySQL Workbench y ejecutarlo.
2. 
**Configurar la conexión:** copiar `src/main/resources/db.properties.ejemplo` como `db.properties` en la misma carpeta y escribir el usuario y la contraseña de MySQL.
3. 
**Abrir el proyecto** en NetBeans (File → Open Project) y hacer *Clean and Build*.
4. 
**Ejecutar** la clase `Principal` (F6) y usar el menú en la ventana Output.
## Estándares de codificación aplicados
| Elemento | Convención | Ejemplo |
|---|---|---|
| Paquetes | minúsculas, dominio invertido | `com.yiesoftware.motoresjey.dao` |
| Clases e interfaces | PascalCase, sustantivo | `Cliente`, `ClienteDAO`, `ClienteDAOImpl` |
| Métodos | camelCase, empiezan con verbo | `insertar()`, `buscarPorId()`, `obtenerConexion()` |
| Variables y atributos | camelCase descriptivo | `numeroDocumento`, `filasAfectadas` |
| Constantes | MAYÚSCULAS_CON_GUION_BAJO | `SQL_INSERTAR_PERSONA` |
Además: atributos `private` con getters y setters, Javadoc en clases y métodos públicos, `PreparedStatement` en todas las consultas (previene inyección SQL), try-with-resources para cerrar conexiones y transacciones con `commit` / `rollback`.
## Control de versiones
Cada etapa del desarrollo se registró en un commit siguiendo la convención *Conventional Commits* (`feat:`, `fix:`, `docs:`, `chore:`, `test:`). La documentación se integró a `main` mediante un Pull Request desde la rama `develop`.
## Autor
Iván Sierra · Noviembre de 2026
