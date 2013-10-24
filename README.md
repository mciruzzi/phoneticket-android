# Instrucciones para levantar el proyecto (14/10):

1. Asegurarse de tener instalados en eclipse ADT. (Help --> Install New Software):
	* Maven Integration for Eclipse	(si se selecciona Work with: "All Available Sites") se deberia poder ver
	* Android Configurator for M2E (es necesario agregar el repo...	http://rgladwell.github.com/m2e-android/updates/)

2. Desde el directorio git del proyecto forzar el bajado de la libreria de requests:
	* `mvn install:install-file -Dfile=android-async-http-1.4.3.jar -DgroupId=com.loopj -DartifactId=android-async-http -Dversion=1.4.3 -Dpackaging=jar`
	* `mvn install:install-file -Dfile=core-2.2.jar -DgroupId=com.google.zxing -DartifactId=core -Dversion=2.2 -Dpackaging=jar` (Puede que sea necesario tambien)
	* `export ANDROID_HOME=<path a la sdk>` (es conveniente agregarlo a su `.bashrc`, o `.bash_profile` dependiendo lo que use su distribución)
	* pueden hacer `mvn install` (si no tienen un emulador levantado van a fallar los tests)

3. En eclipse se importan los proyectos como File --> Import --> Maven --> Existing Maven Projects. 
	* Seleccionan la ruta a la carpeta git y tilden los proyetos `phoneticket-app/pom.xml` y `phoneticket-tests/pom.xml`
	* En la siguiente pantalla todos los plugins deberian aparecer mapeados automaticamente 

4. Run
	* Aplicacion: en phoneticket-tests --> RunAs --> Android Application
	* Tests: en phoneticket-tests --> RunAs --> AndroidJUnitTest
