package de.lander.link.util;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;

/**
 *
 * @author mvogel
 *
 */
public class DatabaseFactory {

	@Produces
	GraphDatabaseService createDatabase(final InjectionPoint injectionPoint) {
		System.out.println("annotated " + injectionPoint.getAnnotated());
		System.out.println("bean " + injectionPoint.getBean());
		System.out.println("member " + injectionPoint.getMember());
		System.out.println("qualifiers " + injectionPoint.getQualifiers());
		System.out.println("type " + injectionPoint.getType());
		System.out.println("isDelegate " + injectionPoint.isDelegate());
		System.out.println("isTransient " + injectionPoint.isTransient());

		Bean<?> bean = injectionPoint.getBean();
		TestDatabase transportConfig = bean.getBeanClass().getAnnotation(
				TestDatabase.class);
		if (transportConfig != null) {
			System.out.println(">> exits!");
		}

		System.out.println("bean.beanClass " + bean.getBeanClass());
		System.out.println("bean.injectionPoints " + bean.getInjectionPoints());
		System.out.println("bean.name " + bean.getName());
		System.out.println("bean.qualifiers " + bean.getQualifiers());
		System.out.println("bean.scope " + bean.getScope());
		System.out.println("bean.stereotypes " + bean.getStereotypes());
		System.out.println("bean.types " + bean.getTypes());

		Annotated annotated = injectionPoint.getAnnotated();
		System.out.println("annotated.annotations "
				+ annotated.getAnnotations());
		System.out.println("annotated.annotations " + annotated.getBaseType());
		System.out.println("annotated.typeClosure "
				+ annotated.getTypeClosure());

		return new TestGraphDatabaseFactory().newImpermanentDatabase();
		// String storeDir = "todo"; // TODO
		// return new GraphDatabaseFactory()
		// .newEmbeddedDatabaseBuilder(storeDir)
		// .setConfig(
		// GraphDatabaseSettings.nodestore_mapped_memory_size,
		// "10M")
		// .setConfig(GraphDatabaseSettings.string_block_size, "60")
		// .setConfig(GraphDatabaseSettings.array_block_size, "300")
		// .newGraphDatabase();
	}
}
