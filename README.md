# JUNIT

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
- Buscar siempre tener el mayor porcentaje de cobertura posible de las pruebas, en el mejor de los casos 70% u 80% está perfecto. Para esto, se puede utilizar el plugin **EclEmma** desde marketplace de Spring Tool Suite, que mostrará en una pestaña del IDE la cantidad de cobertura. También es posible crear un reporte que se puede subir a JIRA como prueba de la cobertura que están entregando, esto se hace con JaCoCo.  ([Instalación JaCoCo](https://www.codeproject.com/Articles/832744/Getting-Started-with-Code-Coverage-by-Jacoco))

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
	public Arrival getArrivalById(@PathVariable(value="id") Integer id) throws ArrivalException {
		Arrival arrival = arrivalService.getArrivalById(id);
		if(arrival.getCity().isEmpty()) {
			throw new ArrivalException("Arrival doesn't exist");
		}
		return arrival;
	}
```
2.  Se debe crear un archivo Test para cada controlador, en este caso **ArrivalControllerTest.java**, hacemos un test para cada método en el controlador y unos cuantos para verificar los casos de errores:
```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringJunitTestApplication.class)
@SpringBootTest
public class ArrivalControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	Arrival arrival;
	

	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		arrival = new Arrival();
		arrival.setId(1);
		arrival.setCity("Santiago");
	}

	@Test
	public void getAllArrivals() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(VERSION+ARRIVAL+"all")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].city", is(arrival.getCity())))
				.andDo(print());
		
		
	}
	
	
	@Test
	public void getArrivalsById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(VERSION + ARRIVAL + arrival.getId())
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(jsonPath("city", is(arrival.getCity())))
					.andDo(print());
		
		
	}

	@Test
	public void verifyInvalidArrivalPath() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(VERSION + ARRIVAL + "pepito/").accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.errorCode").value(400))
				.andExpect(jsonPath("$.message").value("The request could not be understood by the server"))
				.andDo(print());
	}

	@Test
	public void verifyInvalidadArrivalId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(VERSION + ARRIVAL + "4").accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.errorCode").value(404))
				.andExpect(jsonPath("$.message").value("Arrival doesn't exist"))
				.andDo(print());
	}
```

- Las pruebas del controlador son las siguientes:
 
 El método **setUp** tiene la anotación *Before*, esto hace que se ejecute este bloque antes de cada prueba, aquí se realiza lo siguiente:
 -- Seteamos el WebApplicationContext de la aplicación dentro de *MockMvc*. Esto nos permitirá realizar las peticiones get que probaremos más adelante.
 -- Seteamos un elemento **Arrival**, este lo llenamos con la información que vamos a probar. Por ejemplo, en la base de datos, existe el id = 1, city = Santiago, tomamos esto y llenamos este objeto, que nos servirá más adelante para probar si lo que retorna los controladores es igual a nuestro *arrival*.
 
El método **getAllArrivals()** hace lo siguiente: 

 -- Realiza una petición de tipo **get** a api/v1/arrival/all. (esto es similar a lo que llamamos desde postman cuando hacemos pruebas regularmente. 
 --  Verificamos que el tipo que retorna el controlador es JSON. 
 -- Finalmente, colocamos todo lo que queremos probar sobre la respuesta. Aquí verificamos que retorne un OK (**200**) y analizamos el JSON de respuesta, que tiene 3 elementos y verificamos que el primero de ellos sea igual a *arrival.getCity()*que definimos en el **setUp()**. Además, el *andDo(print()* mostrará la información por consola.
   
-- El método **getArrivalsById()** hace lo mismo, la única diferencia es que asume un objeto JSON en vez de la lista de Arrivals

-- El método **verifyInvalidArrivalPath()** hace una llamada pero con una ruta inexistente, en este caso: **api/v1/arrival/pepito/** y verificamos que lo que retorne sea un error de tipo 400 con el mensaje correspondiente.

-- El método **verifyInvalidArrivalId()** hace lo mismo que en el *getArrivalsById** con la diferencia que tiene un parámetro inexistente en la base de datos. En el código tenemos una validación que retorna un error de que no existe, y en la prueba verificamos que se lance ese código y mensaje.
____


 3- Ahora, se debe crear las pruebas de los **Servicios**. Esta prueba cubre el testing en la base de datos, como por ejemplo, escribir data en la BD y luego verificar si se almacenó correctamente. Para lo cual, se crea un **ArrivalRepositoryTest** dentro del package correspondiente a repository.
```java
@RunWith(SpringJUnit4ClassRunner.class)
public class ArrivalServiceTest {
	
	@Mock
	private ArrivalRepository arrivalRepository;
	
	@InjectMocks
	private ArrivalServiceImpl arrivalService;

	Arrival firstArrival, secondArrival; 
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		firstArrival = new Arrival();
		secondArrival = new Arrival();
		
		firstArrival.setId(1);
		firstArrival.setCity("Santiago");

		secondArrival.setId(2);
		secondArrival.setCity("Buenos Aires");
	}
	
	@Test
	public void testGetAllArrivals() {
		List<Arrival> arrivalList = new ArrayList<Arrival>();
		
		arrivalList.add(firstArrival);
		arrivalList.add(secondArrival);
		
		when(arrivalRepository.findAll()).thenReturn(arrivalList);
		
		List<Arrival> result = arrivalService.getAllArrivals();
		assertThat(result.size()).isEqualTo(arrivalList.size());
		assertThat(result.get(0)).isEqualToComparingFieldByField(firstArrival);
		assertThat(result.get(1)).isEqualToComparingFieldByField(secondArrival);
	}
	
	@Test
	public void testGetArrivalById() {
		when(arrivalRepository.findById(1)).thenReturn(Optional.of(firstArrival));
		Arrival result = arrivalService.getArrivalById(1);
		assertThat(result.getCity()).isEqualTo(firstArrival.getCity());
	}
```

- Las pruebas del controlador son las siguientes:
 
 Las primeras líneas de la clase definen nuestro **Mock**, Usamos este anotación para crear e injectar instancias. **InjectMocks** inyecta los campos mock dentro del objeto testeado automáticamente. Por lo que lo que hacemos aquí es finalmente inyectar el repositorio en la implementación del servicio. Además, creamos un unos objetos de prueba **Arrival** que tal como en las pruebas anteriores, permitirá chequear que lo que se obtenga de los servicios esté correcto.

 El método **testGetAllArrivals()** :
 -- Creamos una lista y agregamos nuestro *firstArrival* y *secondArrival* que definimos en el SetUp. luego definimos que cuando el repositorio ejecute el método **findAll**. debe traer la lista de Arrivals que creamos antes.
 -- Luego viene la prueba. Se llama al método desde el **Service** y almacenamos en una lista result. 
 -- Los siguientes líneas del tipo **assertThat** verifica que el tamaño de la lista obtenida del servicio sea igual a la que definimos cuando escribimos el **when**. Las otras líneas toman el primer y segundo elemento y comparan que sean equivalentes sus campos con el *firstArrival* y *secondArrival*.
 
El método **testGetArrivalById()** :
 -- Definimos con un **when** qué es lo que debe retornar al ejecutar la llamada *findById(1)* desde el repositorio. En este caso debería retornarnos el *firstArrival*.
 -- Luego, hacemos la llamada desde el **Servicio** y verificamos con **assertThat** que la ciudad obtenida del resultado sea igual a la ciudad que definimos en **firstArrival**.