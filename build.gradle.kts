plugins {
    id("java")
    id("application")
}

group = "br.com.erikferreira"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.liquibase:liquibase-core:4.29.1")
    implementation("org.projectlombok:lombok:1.18.42")
    implementation("com.h2database:h2:2.3.232")
    implementation("org.hibernate.orm:hibernate-core:6.5.2.Final")
    implementation("org.slf4j:slf4j-simple:2.0.16")

    annotationProcessor("org.projectlombok:lombok:1.18.42")
}

tasks.test {
    useJUnitPlatform()
}


