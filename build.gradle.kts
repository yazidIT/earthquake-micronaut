import nu.studer.gradle.jooq.JooqEdition
import nu.studer.gradle.jooq.JooqGenerate
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Property

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    id("org.jetbrains.kotlin.kapt") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.5.1"
    id("nu.studer.jooq") version "7.1.1"
    id("org.sonarqube") version "3.3"
}

sonarqube {
    properties {
        val projectName = "EarthQuake-Micronaut"
        property("sonar.projectName", projectName)
        property("sonar.projectVersion", project.version.toString())
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.login", "admin")
        property("sonar.password", "YAZsonarqube1234")
    }
}

version = "0.1"
group = "com.mgt.earthquake"

val kotlinVersion= project.properties["kotlinVersion"]
repositories {
    mavenCentral()
}

dependencies {
    kapt("io.micronaut:micronaut-http-validation")
    kapt("io.micronaut.data:micronaut-data-document-processor")

    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut:micronaut-http-client")

    implementation("io.micronaut.mongodb:micronaut-mongo-reactive")
    implementation("io.micronaut.r2dbc:micronaut-r2dbc-core")
    implementation("io.micronaut.flyway:micronaut-flyway")
    implementation("io.micronaut.data:micronaut-data-r2dbc")
    runtimeOnly("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.sql:micronaut-jooq")
    implementation("io.micronaut.data:micronaut-data-mongodb")

    implementation("io.micronaut.kotlin:micronaut-kotlin-extension-functions")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.reactor:micronaut-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")

    implementation("io.micronaut:micronaut-validation")

    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("ch.qos.logback:logback-classic")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("io.r2dbc:r2dbc-h2")
    runtimeOnly("org.flywaydb:flyway-mysql")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.postgresql:r2dbc-postgresql")
    testImplementation("org.testcontainers:postgresql")

    testImplementation("org.testcontainers:mongodb")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
    testImplementation("org.testcontainers:r2dbc")

    testImplementation("org.mock-server:mockserver-netty:5.13.2")
    testImplementation("org.mock-server:mockserver-junit-jupiter:5.13.2")
    testImplementation("io.kotest.extensions:kotest-extensions-mockserver:1.2.1")
}


application {
    mainClass.set("com.mgt.earthquake.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("17")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}
graalvmNative.toolchainDetection.set(false)
micronaut {
    runtime("netty")
    testRuntime("kotest")
    processing {
        incremental(true)
        annotations("com.mgt.earthquake.*")
    }
}
allOpen {
    annotations(
        "io.micronaut.aop.Around",
        "io.micronaut.http.annotation.Controller",
        "jakarta.inject.Singleton",
        "javax.persistence.Entity",
        "io.micronaut.data.annotation.MappedEntity",
        "javax.persistence.MappedSuperclass"
    )
}

dependencies {
    jooqGenerator("org.postgresql:postgresql:42.4.0")
}

jooq {
    version.set("3.16.7")
    edition.set(JooqEdition.OSS)

    configurations {

        create("main") {

            jooqConfiguration.apply {
                logging = Logging.WARN
                generateSchemaSourceOnCompilation.set(false)

                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/quakedb"
                    user = "dbuser"
                    password = "dbuser"
                    properties = listOf(
                        Property().apply {
                            key = "PAGE_SIZE"
                            value = "2048"
                        }
                    )
                }

                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        inputSchema = "public"
                        isOutputSchemaToDefault = true
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                    }
                    generate.apply {
                        isRecords = false
                        isFluentSetters = false
                        isValidationAnnotations = true
                        isJpaAnnotations = true
                        isPojos = true
                        isDaos = true
                        isJavaTimeTypes = true
                    }
                    target.apply {
                        packageName = "com.mgt.earthquake.jooqmodel"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks.named<JooqGenerate>("generateJooq") { allInputsDeclared.set(true) }
