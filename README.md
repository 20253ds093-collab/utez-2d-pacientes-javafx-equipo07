# Directorio de Pacientes - Consultorio Médico (Grupo 2°D)

*Proyecto Integrador: Aplicaciones de Escritorio / POO*
*Equipo:* Equipo 7

## Contexto y Objetivo
Este sistema de escritorio está diseñado para administrar el directorio de pacientes de un consultorio pequeño. Permite registrar, consultar, actualizar y eliminar pacientes utilizando Programación Orientada a Objetos (POO).

El objetivo principal es implementar un CRUD completo con persistencia de datos en un archivo local (sin base de datos), asegurando que la información se conserve entre ejecuciones.

---

## Funcionalidades Principales

### 1. CRUD y Persistencia
* Alta, Consulta y Actualización de pacientes.
* Eliminación y Estatus: Se utiliza borrado lógico. Los pacientes cambian su estatus entre ACTIVO e INACTIVO en lugar de ser eliminados físicamente.
* Persistencia en Archivo: Los datos se guardan y cargan automáticamente desde un archivo local (.txt o .csv).

### 2. Validaciones Implementadas
* No se permiten campos vacíos.
* CURP: Único por paciente (no permite duplicados).
* Nombre: Longitud mínima de 5 caracteres.
* Edad: Valor numérico en un rango lógico (0 a 120).
* Teléfono: Solo dígitos, longitud mínima de 10 caracteres.

### 3. Interfaz Gráfica (JavaFX)
* Tabla Principal: Muestra la lista de pacientes registrados.
* Resumen en Tiempo Real: Contadores automáticos en pantalla que muestran: Total de pacientes, Activos e Inactivos.
* Formulario: Campos para CURP, nombre, edad, teléfono, alergias y estatus. Alertas integradas para mostrar errores de validación.

---

## Tecnologías Utilizadas
* Lenguaje: Java
* Interfaz: JavaFX (FXML + Controller)
* Arquitectura: POO (Modelo, Servicio/Repositorio)
* Estructura de Datos: ObservableList para el manejo de la tabla.
* Control de Versiones: Git (Flujo: main, dev, ramas personales).