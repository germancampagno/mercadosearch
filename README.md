# MercadoSearch 📱
## Resumen 

Esta aplicación está diseñada para explorar productos en **MercadoLibre** de manera eficiente e intuitiva, siguiendo tanto el estilo visual de **MercadoLibre** como las guías de diseño de **Material Design**, e implementando funcionalidades clave para mejorar la experiencia del usuario. Está desarrollada con tecnologías modernas de Android como **Jetpack Compose**, **Hilt**, y **Room**, siguiendo la arquitectura **MVVM** para garantizar escalabilidad y mantenibilidad.

### Funcionalidades Principales:

1. **Pantalla Principal**:
   - Muestra una barra de búsqueda para iniciar consultas personalizadas.
   - Lista las principales categorías de MercadoLibre, permitiendo explorar productos destacados al seleccionar una categoría.

2. **Pantalla de Búsqueda y Resultados**:
   - Permite realizar búsquedas de productos en toda la plataforma mediante un campo de texto.
   - Si se selecciona una categoría desde la pantalla principal, las búsquedas se limitan a esa categoría específica.
   - Muestra una lista de productos encontrados que incluye imagen, título, precio y detalles clave.
   - Implementación de carga paginada al llegar al final de la lista de resultados.
     
3. **Pantalla de Detalles del Producto**:
   - Muestra información detallada del producto seleccionado, incluyendo:
     - Condición (Nuevo/Usado).
     - Precio formateado y ubicación del vendedor.
     - Descripción extendida del producto.
     - Incluye un botón (Call to Action) para abrir el producto directamente en la web oficial de MercadoLibre mediante Deep Link.

### Aspectos Técnicos:

- **Single Activity**: Utiliza solo un Activity manejando el resto del UI mediante funciones Composables, siendo consistente con las buenas prácticas recomendadas por Android.
- **Paginación**: Carga progresiva de resultados para optimizar el rendimiento.
- **Formatter**: Utiliza un formatter para precios y direcciones, adaptádondose a las respuestas proveídas por las APIs públicas, cuyas propiedades a ser utilizadas pueden variar según el endpoint.
- **Gestión de Estados**: Implementación con `StateFlow` para garantizar una experiencia fluida e intuitiva.

### Futuras Extensiones:
Aunque esta versión no incluye funcionalidades como favoritos, la estructura está diseñada para facilitar su incorporación en futuras iteraciones.

# Screenshots 📷

| Splash    | Categorías     |  Búsqueda    |
|------------|-------------|------------|
| <img src="https://github.com/user-attachments/assets/4a37a399-38ac-4dc8-a699-0fc1d6205d85" width="250"> | <img src="https://github.com/user-attachments/assets/310bcea3-18e3-436b-b754-21aa3514c56d" width="250"> |<img src="https://github.com/user-attachments/assets/d505ecca-0f58-4c23-a582-fa033f449490" width="250"> | 

| Búsqueda (Pocos resultados)     |  Detalle producto ARS    | Detalle producto USD  |
|------------|-------------|------------|
| <img src="https://github.com/user-attachments/assets/de300cfe-b469-4b9c-9498-58cf84185424" width="250">| <img src="https://github.com/user-attachments/assets/c7388d06-05ab-4dde-8ed1-a4f46050ab4d" width="250">|<img src="https://github.com/user-attachments/assets/6276db79-bfda-4fe9-a0c5-ccdc8e69b6fa" width="250"> |


# GIFs 🎥

| Navegación Completa   | Buscar por categoría     | 
|------------|-------------|
| <img src="https://github.com/user-attachments/assets/c46b714f-5047-4159-adbe-29cc80f55060" width="250"> | <img src="https://github.com/user-attachments/assets/b0d14022-9340-41bd-a9d4-0a45601af189" width="250"> |

| Scroll Infinito (pagination)                                                         | Explorar Categoria                                                     | 
|---------------------------------------------------------------|----------------------------------------------------------------|
| <img src="https://github.com/user-attachments/assets/67cb8d0d-b64e-44d3-a443-074a88f9e639" width="250"> | <img src="https://github.com/user-attachments/assets/6fbbf929-0f45-4ec1-a6d9-a443163bf0ad" width="250"> |


# Librerías utilizadas 📚

- [Jetpack Compose](https://developer.android.com/compose) - UI Toolkit
- [Jetpack Navigation](https://developer.android.com/guide/navigation) - Navegación
- [Retrofit](https://github.com/square/retrofit) - Networking
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android?hl=es-419) - Dependency Injection
- [Coroutines](https://developer.android.com/kotlin/coroutines) - Async
- [Coil](https://github.com/coil-kt/coil#jetpack-compose) - Image Loader
- [Mockito](https://github.com/mockito/mockito) - Unit Testing

# Nice to have 👀

Estas son algunas funcionalidades y prácticas que se consideran para próximas versiones, con el objetivo de expandir la funcionalidad y la calidad del proyecto:

- Lint: Garantizar un código limpio y estandarizado.
- UI Tests: Añadir pruebas automatizadas para validar interacciones de usuario.
- Feature: Cart/Checkout: Integrar funcionalidades de carrito y procesos de compra.
- Modularización: Separar el proyecto en módulos independientes
