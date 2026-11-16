# POO_Java_TF_Gr2

Sistema de gestión de proyectos desarrollado en Java 17 con interfaz gráfica Swing, basado en el diagrama de clases entregado.

## Requisitos
- JDK 17 o superior.
- Maven 3.8 o superior para ejecutar las pruebas JUnit.
- NetBeans, IntelliJ IDEA o VS Code con extensiones Java.

## Estructura
- `src/Modelo`: Proyecto, Empleado, Cliente, Empresa, PersonaNatural y Contacto.
- `src/Vista`: ventana principal `UsaProyecto`.
- `test/Modelo`: pruebas unitarias de Proyecto.

## Ejecutar
Desde la raíz del proyecto:

```bash
mkdir -p out
javac -d out src/Modelo/*.java src/Vista/*.java
java -cp out Vista.UsaProyecto
```

Con Maven:

```bash
mvn test
mvn package
```

En NetBeans: File > Open Project, seleccionar la carpeta `POO_Java_TF_Gr2`. Configurar JDK 17.

## Reglas implementadas
- Código automático: año actual y consecutivo de tres dígitos (`AAAA-001`).
- Título: entre 20 y 100 caracteres.
- Fecha: formato ISO `AAAA-MM-DD`.
- Duración: semanas (1-3), meses (1-11, 13-23 o 25-35), años (1-3).
- Estado inicial `INICIADO`; estados permitidos: `EN CURSO`, `CANCELADO`, `FINALIZADO`, `SUSPENDIDO`, `APLAZADO`.
- Proyecto puede crearse sin integrantes; el gerente se registra por separado.
- Serialización de proyectos en `datosProyectos` y clientes únicos en `datosClientes`.

## GitHub
1. Crear un repositorio llamado `POO_Java_TF_Gr2` en GitHub.
2. En la terminal, ubicarse en esta carpeta y ejecutar:

```bash
git init
git add .
git commit -m "Estructura inicial del proyecto POO"
git branch -M main
git remote add origin https://github.com/USUARIO/POO_Java_TF_Gr2.git
git push -u origin main
```

Reemplazar `USUARIO` por el usuario real de GitHub. No subir contraseñas ni tokens.

## Evidencias sugeridas
- Captura del proyecto abierto y estructura de paquetes.
- Captura del repositorio GitHub con archivos y commits.
- Capturas de commits de cada integrante y pull requests/revisión de código.
- Capturas de ejecución: registro, consulta, actualización y eliminación.
- Captura de `mvn test` mostrando las pruebas exitosas.
- Captura de depuración con breakpoint y variables inspeccionadas.

## Trabajo grupal
Cada integrante debe trabajar en una rama propia, por ejemplo `feature/modelo-proyecto`, `feature/clientes` y `feature/interfaz`. Integrar mediante pull requests revisados por otro integrante.

## Nota
El diagrama no especifica un límite numérico máximo de proyectos ni todos los detalles de la interfaz. Esta versión usa una lista dinámica (`ArrayList`) y una interfaz Swing de botones/diálogos para cubrir las operaciones descritas.
