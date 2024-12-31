plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("plugin.jpa") version "1.9.20"
    kotlin("kapt") version "1.7.10"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    idea
    jacoco
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    // DEFAULT
    implementation("org.springframework.boot:spring-boot-starter-web:3.1.0")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // CACHING
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // LOGGING
    implementation("org.springframework.boot:spring-boot-starter-log4j2:3.0.4")

    // JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.0.4")

    // QUERY_DSL
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")

    // DATABASE
    runtimeOnly("com.mysql:mysql-connector-j:8.0.32")
    runtimeOnly("com.h2database:h2")

    // SECURITY
    implementation("org.springframework.boot:spring-boot-starter-security:3.0.4")
    implementation("org.springframework.security:spring-security-jwt:1.1.1.RELEASE")
    implementation("com.sun.xml.bind:jaxb-impl:4.0.1")
    implementation("com.sun.xml.bind:jaxb-core:4.0.1")
    implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    testImplementation("org.springframework.security:spring-security-test:6.0.2")

    // VALIDATING
    implementation("org.springframework.boot:spring-boot-starter-validation:3.0.4")

    // JSON PARSING
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate5:2.13.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.2")
//    testImplementation("io.kotest:kotest-property")

    // TEST
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    // - SECURITY
    testImplementation("org.springframework.security:spring-security-test")
    // - REST_DOCS
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    // - MOCK
    testImplementation("io.mockk:mockk:1.9.3")
    // - KOTEST
    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.6.0")

    // MAPPING
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")
    kaptTest("org.mapstruct:mapstruct-processor:1.5.3.Final")

    // CUSTOM toString & equals & hashCode
    implementation("com.github.consoleau:kassava:2.1.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

configurations.forEach {
    it.exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    it.exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

// Kotlin QClass Setting
kotlin.sourceSets.main {
    println("kotlin sourceSets builDir:: $buildDir")
    setBuildDir("$buildDir")
}

idea {
    module {
        val kaptMain = file("build/generated/source/kapt/main")
        sourceDirs.add(kaptMain)
        generatedSourceDirs.add(kaptMain)
    }
}



allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.register<Test>("jacocoTest") {
    group = "verification"
    description = "only jacoco tests"

    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath

    include("**/coverage/**")

    extensions.configure(JacocoTaskExtension::class.java) {
        destinationFile = file("$buildDir/jacoco/testOnly.exec")
    }
}

// ascii docs
val snippetsDir by extra { file("build/generated-snippets") }

tasks {

    test {
        outputs.dir(snippetsDir)
        finalizedBy(jacocoTestReport)
    }

    jacocoTestReport {
//        val jacocoTest = tasks.filter { it.name == "jacocoTest" }.orNull
//        if (jacocoTest != null) dependsOn(jacocoTest)

        executionData(fileTree(buildDir).include("jacoco/*.exec"))
        reports {
            html.required = true
            xml.required = false
            csv.required = false
            val excludes = mutableListOf<String>(
                "**/dto/*",
                "**/entity/*",
                "**/common/filter/*"
            )

            classDirectories.setFrom(
                sourceSets.main.get().output.asFileTree.matching {
                    exclude(excludes)
                }
            )
        }

//        jacocoTestCoverageVerification{
//            violationRules {
//                rule {
//                    element = "CLASS"
//                    limit {
//                        counter = "BRANCH"
//                        value = "COVEREDRATIO"
//                        minimum = BigDecimal.valueOf(0.40)
//                    }
//                }
//            }
//        }
    }

    asciidoctor {
        dependsOn(test)

        doFirst {
            delete(file("src/main/resources/static/docs"))
        }
        inputs.dir(snippetsDir)

        doLast {
            copy {
                from("build/docs/asciidoc")
                into("src/main/resources/static/docs")
            }
        }
    }

    build {
        dependsOn(asciidoctor)
    }
}

