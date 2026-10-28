# denunciasapp

App Android (Kotlin + Retrofit) que consume la API [denunciaservice](https://github.com/junniors-dev/denunciaservice)
publicada en Render.

- Lista de denuncias paginada (RecyclerView) con botón para cargar las 5 siguientes.
- Formulario para registrar una denuncia.
- Detalle de la denuncia con botón para atenderla.
- Búsqueda de denuncias por DNI.

La URL de la API está en `Api.kt` (`BASE_URL`). Para usar la API local desde el emulador: `http://10.0.2.2:8080/`.
