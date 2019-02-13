# Test JUnit Tutorial

# JUNIT y Test Driven Development

## Terminología 

### Unit Test / Pruebas Unitarias
- Diseñado para testear secciones específicas de código, métodos.
- Porcentaje de líneas de código testeado debe estar entre 70% u 80%.
- Debe ser rápido y pequeño.
- No debe tener dependencias externas. (No DB, no Spring Context, etc)

### Integration Test / Pruebas de Integración
- Diseñado para testear comportamientos entre objetos y partes de todo el sistema.
- Mucho más grande Scope.
- Puede incluir Spring Context, DB.
- Puede ser mucho más lento que las pruebas unitarias.

### Functional Test / Pruebas Funcionales
Testear la aplicación corriendo.
- Testear endpoints
- Pruebas de Caja Negra
- Pruebas de Caja Blanca

### Mock
Es una implementación falsa de una clase que se puede usar para probar.

### Spy
Un mock parcial, permitiendo hacer override a algunos métodos de una clase real.

## Dependencias

- **Sprint Boot Starter Test**: repositorio: *spring-boot-starter-test*
- **Mockito**:  Librería para crear Mocks. Un mock es un objeto de prueba que imita el comportamiento de objetos reales de forma controlada.
- **AssertJ**: Librería que permite comparar resultados de manera entendible.

## Anotaciones JUnit
Las anotaciones más utilizadas son:

- **@Test**: Marca un método como un Test.
- **@BeforeClass**: El método que posea esta anotación correrá **una vez** antes de cualquiera de los métodos test de la clase. Es útil para hacer un setup general.
- **@Before**: Método correrá antes que cada método de test. 
- **@AfterClass**: Método se ejecutará una vez después de que todos los test sean ejecutados.
- **@After**: Método se ejecutará una vez después de que cada test termine.

## Consideraciones al usar JUnit
- Se debe tener la misma estructura de packages en **test** que la que existe en **main**. Por ejemplo:
Si estamos haciendo tests de la clase **User.java** ubicada en esta ruta:
```
main/java/cl/sodexo/testApp/domain/User.java
```
Las pruebas de dicho elemento deberían estar en una ruta como esta:
```
test/java/cl/sodexo/testApp/domain/UserTest.java
```
- Todas las clases de prueba deben tener añadido al final la palabra **Test**.
- Testear un 70% u 80% de las clases realizadas. Para esto, utilizar el plugin **EclEmma** desde marketplace de Spring Tool Suite ([Instalación JaCoCo](https://www.codeproject.com/Articles/832744/Getting-Started-with-Code-Coverage-by-Jacoco))

- Los JUnit Test deben correr rápido, si demoran más de 10 - 15 segundos, significa que hay algo mal, y que se debe refactorizar las pruebas.

## Cómo testear Spring Boot REST APIs

Tenemos el siguiente ejemplo. 

 1. Contamos con un controlador Rest con dos métodos:
 ```java
 @RestController
@RequestMapping(value= VERSION + ARRIVAL)
public class ArrivalController {
    @Autowired
	private ArrivalRepository arrivalRepository;
	
	@GetMapping(value="all")
	@ResponseBody
	public List<Arrival> getAllArrivals(){
		return arrivalRepository.findAll();
	}
	
	@GetMapping(value="{id}")
	@ResponseBody
	public Arrival getArrivalById(@PathVariable(value="id") Integer id) {
		return arrivalRepository.findAllById(id);
	}
```
2.  Se debe crear un archivo Test para cada controlador, en este caso **ArrivalControllerTest.java**, hacemos un test para cada método en el controlador:
```java
@RunWith(SpringRunner.class)
@WebMvcTest(ArrivalController.class)
public class ArrivalControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ArrivalController arrivalController;

	Arrival arrival;
	List<Arrival> allArrivals;

	@Before
	public void setUp() throws Exception {
		arrival = new Arrival();
		arrival.setId(1);
		arrival.setCity("Santiago");
		allArrivals = singletonList(arrival);
	}

	@Test
	public void getArrivals() throws Exception {
      given(arrivalController.getAllArrivals()).willReturn(allArrivals);
		mvc.perform(get(VERSION + ARRIVAL + "all").contentType(APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(1)))   
		.andExpect(jsonPath("$[0].city", is(arrival.getCity()))); 
	}

	@Test
	public void getArrivalsById() throws Exception {
	      given(arrivalController.getArrivalById(arrival.getId()))
	      .willReturn(arrival);

		mvc.perform(get(VERSION + ARRIVAL + arrival.getId())
		.contentType(APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("city", is(arrival.getCity())));
	}
}
```
El método **getArrivals()** hace lo siguiente: 

 -- Crea un método setUp con la anotación **@Before**, que hará que antes
   de cada test, se cree la entidad **Arrival** y se seteará el valor a
   probar. Además, creamos una lista de Arrivals, **allArrivals** y le
   añadimos la entidad creada.
 --  Usando **given** de mockito, nos aseguramos de que el **ArrivalController** que marcamos como *MockBean* retorne una lista de todos los arrivals.
 -- Finalmente realizamos una petición **get**, qué revisará si retorna un status OK, verificará que la respuesta JSON tiene sólo un elemento, y finalmente, si el cuerpo del JSON tiene una llave 'city'   con el value que seteamos anteriormente.
   
-- El método **getArrivalsById()** hace lo mismo, la única diferencia es que asume un objeto JSON en vez de la lista de Arrivals
   en el objeto.
____


 3- Ahora, se debe crear las pruebas de los **Repository**. Esta prueba cubre el testing en la base de datos, como por ejemplo, escribir data en la BD y luego verificar si se almacenó correctamente. Para lo cual, se crea un **ArrivalRepositoryTest** dentro del package correspondiente a repository.
```java
@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class ArrivalRepositorytTest{

	@Autowired
	private TestEntityManager em;
	
	@Autowired
	private ArrivalRepository arrivalRepository;
	
	Arrival firstArrival, secondArrival;
	
	@Before
	public void setUp() {
		//Seteamos los valores que probaremos
		firstArrival = new Arrival();
		secondArrival = new Arrival();
		
		firstArrival.setId(1);
		firstArrival.setCity("Santiago");
		
		secondArrival.setId(2);
		secondArrival.setCity("Buenos Aires");
	}
	
	@Test
	public void whenFindAll() {
		
		//when
		List<Arrival> arrivals = arrivalRepository.findAll();
		
		//then
		assertThat(arrivals.size()).isEqualTo(3);
		assertThat(arrivals.get(0)).isEqualToComparingFieldByField(firstArrival);
		assertThat(arrivals.get(1)).isEqualToComparingFieldByField(secondArrival);
	}
	
	@Test
	public void whenFindAllByID() {
		//when
		Arrival testArrival = arrivalRepository.findAllById(firstArrival.getId());
		
		//then
		assertThat(testArrival.getCity()).isEqualTo(firstArrival.getCity());
	}
}
```
En este ejemplo, se usa la database H2 para probar. Esto es una práctica común, de otra forma, se tendría que mantener la data real, y limpiarla después de cada prueba, en cambio H2 es una base de datos en memoria, luego del test, la BD es eliminada.  Los tests son los siguientes:
El método **whenFindAll()**:
- Trae desde el repositorio un findAll(), que llenará *arrivals* con los 3 Arrival que existen por defecto.
- Luego, se verifica que el tamaño de la lista obtenida sea de tres, y verifica si el valor 0 de la lista es igual a firstArrival y el valor 1 es igual a secondArrival. 

El método **whenFindAllById** hace lo mismo que el anterior, sólo que trae un valor. 


Finalmente, podemos verificar que las pruebas tengan una cobertura de 70% u 80% utilizando el plugin de **JaCoCo**. Para ello, agregamos el plugin en el *pom.xml* del proyecto:

```xml
			<plugin>
				<groupId>org.jacoco</groupId>
				<artifactId>jacoco-maven-plugin</artifactId>

				<executions>
					<execution>
						<id>default-prepare-agent</id>
						<goals>
							<goal>prepare-agent</goal>
						</goals>
					</execution>
					<execution>
						<id>default-report</id>
						<phase>prepare-package</phase>
						<goals>
							<goal>report</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
```

Y hay que verificar en el **MarketPlace** de STS que esté instalado **EclEMMA**. Con esto listo, se debe ejecutar un **Maven Install**. Esto creará dentro de la carpeta *target* un directorio de jacoco. 
Para verificar esto, abrir el archivo **target/site/jacoco/index.html**
## Pasos para realizar TDD
TDD es un proceso que trata de repetición de un ciclo de desarrollo muy corto. El procedimiento es muy corto

- **Escribir un test**: Escribir un test que pruebe una funcionalidad de la historia de usuario o tarea.
- **Correr todos los tests**: El test debe fallar porque no hay código que lo respalde.
- **Escribir el código de implementación**: Se escribe la mínima cantidad de código para hacer que el test pase.
- **Correr todos los tests**: El test debería pasar.
- **Refactorizar**: Se arregla el código, mejorándolo.
- **Correr todos los tests**: El test debe seguir pasando.

