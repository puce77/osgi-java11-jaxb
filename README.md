# OSGi - Java 11 - JAXB

Related StackOverflow question: https://stackoverflow.com/questions/65868619/jaxb-osgi-with-java-11

Related JAXB issue: 
https://github.com/eclipse-ee4j/jaxb-ri/issues/1507

Note: Current state: the implementation is still Java 8 based. Please have a look at the branches for the Java 11 migration (work in progress)

## Build and Run
```shell
mvn clean install
cd osgi-java11-jaxb-application
mvn exec:exec
```

## Structure
 - **startup-main**: 
   - executable JAR which starts up the OSGi Framework
   - loads the OSGi bundles from the bundle directory
 - **application**: 
    - copies all artifacts and resources to the correct place
    - configures the Exec Maven Plugin to run this sample
 - **extender**:
    - uses a BundleTracker to locate and unmarshal specific XML documents of other bundles
    - provides XSD and JAXB classes
    - defines an extension point for further JAXB classes
    - registers unmarhalled extension JAXB objects as OSGi services 
 - **sampleextension**:
   - provides further XSD and JAXB classes
   - registers an extension point for these JAXB classes (OSGi Declarative Service)
   - provides a handler which listens for the unmarshalled extension JAXB objects registered as OSGi services to do something with them (here: logs the message of the sample extension)
 - **extendee**: provides an XML document based on the XSDs defined by extender and sampleextension
   
