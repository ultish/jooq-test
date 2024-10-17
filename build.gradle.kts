plugins {
	java
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	id("nu.studer.jooq") version "8.2"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.jooq:jooq")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	jooqGenerator("com.h2database:h2")
	jooqGenerator("org.jooq:jooq-meta")
	jooqGenerator("org.jooq:jooq-codegen")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
jooq {
    version.set("3.18.4")  // Use the latest compatible version of jOOQ
    configurations {
        create("main") {  // This matches the main source set
            generateSchemaSourceOnCompilation.set(true)
            jooqConfiguration.apply {
                jdbc.apply {
                    driver = "org.h2.Driver"
                    url = "jdbc:h2:mem:testdb;MODE=PostgreSQL"
                    user = "sa"
                }
                generator.apply {
                    name = "org.jooq.codegen.JavaGenerator"  // Use Java code generation
                    database.apply {
                        name = "org.jooq.meta.h2.H2Database"
                        inputSchema = "PUBLIC"
                    }
                    target.apply {
                        packageName = "com.example.jooq.generated"  // Replace with your package
                        directory = "build/generated-src/jooq/main"  // Output to Java directory
                    }
                }
            }
        }
    }
}

// jooq {
// 	version.set("3.18.4")
// 	edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)

// 	configurations {
// 		create("main") {
// 			generateSchemaSourceOnCompilation.set(true)

// 			jooqConfiguration.apply {
// 				jdbc {
// 					driver = "org.h2.Driver"
// 					url = "jdbc:h2:mem:testdb;MODE=PostgreSQL"
// 					user = "sa"
// 					password = ""
// 				}
// 				generator {
// 					name = "org.jooq.codegen.DefaultGenerator"
// 					database {
// 						name = "org.jooq.meta.h2.H2Database"
// 						inputSchema = "PUBLIC"
// 					}
// 					generate {
// 						isDeprecated = false
// 						isRecords = true
// 						isImmutablePojos = true
// 						isFluentSetters = true
// 					}
// 					target {
// 						packageName = "com.example.jooq"
// 						directory = "build/generated-src/jooq/main"
// 					}
// 					strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
// 				}
// 			}
// 		}
// 	}
// }

sourceSets {
	main {
		java {
			srcDir("build/generated-src/jooq/main")
		}
	}
}
