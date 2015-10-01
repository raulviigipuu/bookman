#Bookman

Just a little toy app to experiment a bit with JavaFX.
Book collection management with xml file as persistence.

## Running

	mv books_sample.xml books.xml
	mvn package
	java -jar target/bookman.jar

## Notes

App uses books.xml in root folder as data file. If it doesn't exist, it will be created. Sample file is books_sample.xml

## Todo

- Text wrap on detail view
- Switch to JAXB to get rid of the XStream dependency
- Add sorting and filtering


