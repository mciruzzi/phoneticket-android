Instrucciones para levantar el proyecto (15/9):

1) Asegurarse de tener instalados en eclipse ADT. (Help --> Install New Software):
	- Maven Integration for Eclipse	(si se selecciona Work with: "All Available Sites") se deberia poder ver
	- Android Configurator for M2E (es necesario agregar el repo...	http://rgladwell.github.com/m2e-android/updates/)

2) Desde el directorio git del proyecto forzar el bajado de la libreria de requests:
	- mvn install:install-file -Dfile=android-async-http-1.4.3.jar -DgroupId=com.loopj -DartifactId=android-async-http -Dversion=1.4.3 -Dpackaging=jar
	- export ANDROID_HOME="path a la sdk"
	- pueden tirar un mvn install (deberia pasar el proyecto de la app, el de los tests no pasa porque no puede correr los test sin conexion al emulador, con un -DskipTests pasa)

3) En ecplise se importan los proyectos como File --> Import --> Maven --> Existing Maven Projects. 
	- Seleccionan la ruta a la carpeta git y tilden los proyetos pohneticket-app/pom.xml y phoneticket-tests/pom.xml
	- En la siguiente pantalla todos los plugins deberian aparecer mapeados automaticamente 

4) Borrar /gen y /bin de las carpetas de los 2 modulos y hacer un click derecho en el modulo --> Maven --> Update Project

5) Para correr la aplicacion encontramos necesario tener que borrar del build path del modulo de la aplicacion las dependencias de Android. ( BuildPath --> Configure Build Path... --> Java Build Path --> destilden todos los de android "Android 4.3", "Android Private Libraries", "Android Dependencis"

6) No deberia haber errores de compilacion, pero si nos paso que lloraba por que faltaban algunas carpetas, creenlas vacias. La que me acuerdo:
	- ./workspace/Phone-Ticket/phoneticket-test/src/test/java

7) Run
	- Aplicacion: en phoneticket-tests --> RunAs --> Android Application
	- Tests: en phoneticket-tests --> RunAs --> AndroidJUnitTest



