# kotlin-gradle-fatjar-neo4j-3.5
An example how to compile Kotlin and neo4j 3.5.1 fatjar / uberjar with Gradle

The most important thing is to build uberjar not manually with `jar` settings and custom zip groovy script (which comes first if you google `kotlin uberjar | kotlin main class`, but with shadowJar like:
```
shadowJar {
    mergeServiceFiles() # <<< Most important line
    manifest { attributes 'Main-Class': 'hcs.neo.MainKt' }
}
```

See `Dockerfile` and `build.gradle` to see full code

### neo4j 3.5.1 error when uberjar built wrong

If you build uberjar/fatjar in a wrong way then you'll get following error with neo4j 3.5.1 `Configured default provider: native-btree-1.0 not found`:
<detail>
Exception in thread "main" java.lang.RuntimeException: Error starting org.neo4j.graphdb.facade.GraphDatabaseFacadeFactory, /tmp
	at org.neo4j.graphdb.facade.GraphDatabaseFacadeFactory.initFacade(GraphDatabaseFacadeFactory.java:212)
	at org.neo4j.graphdb.facade.GraphDatabaseFacadeFactory.newFacade(GraphDatabaseFacadeFactory.java:135)
	at org.neo4j.graphdb.factory.GraphDatabaseFactory.newDatabase(GraphDatabaseFactory.java:133)
	at org.neo4j.graphdb.factory.GraphDatabaseFactory.newEmbeddedDatabase(GraphDatabaseFactory.java:122)
	at org.neo4j.graphdb.factory.GraphDatabaseFactory$EmbeddedDatabaseCreator.newDatabase(GraphDatabaseFactory.java:178)
	at org.neo4j.graphdb.factory.GraphDatabaseBuilder.newGraphDatabase(GraphDatabaseBuilder.java:210)
	at org.neo4j.graphdb.factory.GraphDatabaseFactory.newEmbeddedDatabase(GraphDatabaseFactory.java:79)
	at hcs.neo.MainKt.main(Main.kt:7)
	at hcs.neo.MainKt.main(Main.kt)
Caused by: org.neo4j.kernel.lifecycle.LifecycleException: Component 'org.neo4j.kernel.impl.transaction.state.DefaultIndexProviderMap@614df0a4' failed to initialize. Please see the attached cause exception "Configured default provider: `native-btree-1.0` not found. Available index providers: [fulltext-1.0].".
	at org.neo4j.kernel.lifecycle.LifeSupport$LifecycleInstance.init(LifeSupport.java:434)
	at org.neo4j.kernel.lifecycle.LifeSupport.init(LifeSupport.java:66)
	at org.neo4j.kernel.NeoStoreDataSource.initializeExtensions(NeoStoreDataSource.java:471)
	at org.neo4j.kernel.NeoStoreDataSource.start(NeoStoreDataSource.java:315)
	at org.neo4j.kernel.lifecycle.LifeSupport$LifecycleInstance.start(LifeSupport.java:452)
	at org.neo4j.kernel.lifecycle.LifeSupport.start(LifeSupport.java:111)
	at org.neo4j.kernel.impl.transaction.state.DataSourceManager.start(DataSourceManager.java:116)
	at org.neo4j.kernel.lifecycle.LifeSupport$LifecycleInstance.start(LifeSupport.java:452)
	at org.neo4j.kernel.lifecycle.LifeSupport.start(LifeSupport.java:111)
	at org.neo4j.graphdb.facade.GraphDatabaseFacadeFactory.initFacade(GraphDatabaseFacadeFactory.java:207)
	... 8 more
Caused by: java.lang.NullPointerException: Configured default provider: `native-btree-1.0` not found. Available index providers: [fulltext-1.0].
	at java.base/java.util.Objects.requireNonNull(Objects.java:347)
	at org.neo4j.kernel.impl.transaction.state.DefaultIndexProviderMap.initDefaultProvider(DefaultIndexProviderMap.java:126)
	at org.neo4j.kernel.impl.transaction.state.DefaultIndexProviderMap.init(DefaultIndexProviderMap.java:67)
	at org.neo4j.kernel.lifecycle.LifeSupport$LifecycleInstance.init(LifeSupport.java:413)
	... 17 more
</detail>

### neo4j 3.4.11 error when uberjar built wrong

And with neo4j 3.4.11 you'll get `Unknown setting: dbms.tx_log.rotation.size`
<detail>
Exception in thread "main" java.lang.RuntimeException: Error starting org.neo4j.kernel.impl.factory.GraphDatabaseFacadeFactory, /tmp/c
	at org.neo4j.kernel.impl.factory.GraphDatabaseFacadeFactory.initFacade(GraphDatabaseFacadeFactory.java:212)
	at org.neo4j.kernel.impl.factory.GraphDatabaseFacadeFactory.newFacade(GraphDatabaseFacadeFactory.java:125)
	at org.neo4j.graphdb.factory.GraphDatabaseFactory.newDatabase(GraphDatabaseFactory.java:137)
	at org.neo4j.graphdb.factory.GraphDatabaseFactory.newEmbeddedDatabase(GraphDatabaseFactory.java:130)
	at org.neo4j.graphdb.factory.GraphDatabaseFactory$1.newDatabase(GraphDatabaseFactory.java:107)
	at org.neo4j.graphdb.factory.GraphDatabaseBuilder.newGraphDatabase(GraphDatabaseBuilder.java:199)
	at org.neo4j.graphdb.factory.GraphDatabaseFactory.newEmbeddedDatabase(GraphDatabaseFactory.java:75)
	at hcs.neo.MainKt.main(Main.kt:7)
	at hcs.neo.MainKt.main(Main.kt)
Caused by: org.neo4j.kernel.lifecycle.LifecycleException: Component 'org.neo4j.kernel.NeoStoreDataSource@67d18ed7' was successfully initialized, but failed to start. Please see the attached cause exception "Unknown setting: dbms.tx_log.rotation.size".
	at org.neo4j.kernel.lifecycle.LifeSupport$LifecycleInstance.start(LifeSupport.java:466)
	at org.neo4j.kernel.lifecycle.LifeSupport.start(LifeSupport.java:107)
	at org.neo4j.kernel.impl.transaction.state.DataSourceManager.start(DataSourceManager.java:100)
	at org.neo4j.kernel.lifecycle.LifeSupport$LifecycleInstance.start(LifeSupport.java:445)
	at org.neo4j.kernel.lifecycle.LifeSupport.start(LifeSupport.java:107)
	at org.neo4j.kernel.impl.factory.GraphDatabaseFacadeFactory.initFacade(GraphDatabaseFacadeFactory.java:208)
	... 8 more
Caused by: java.lang.IllegalArgumentException: Unknown setting: dbms.tx_log.rotation.size
	at org.neo4j.kernel.configuration.Config.verifyValidDynamicSetting(Config.java:650)
	at org.neo4j.kernel.configuration.Config.registerDynamicUpdateListener(Config.java:693)
	at org.neo4j.kernel.impl.transaction.log.files.LogFilesBuilder.getRotationThresholdAndRegisterForUpdates(LogFilesBuilder.java:249)
	at org.neo4j.kernel.impl.transaction.log.files.LogFilesBuilder.buildContext(LogFilesBuilder.java:228)
	at org.neo4j.kernel.impl.transaction.log.files.LogFilesBuilder.build(LogFilesBuilder.java:177)
	at org.neo4j.kernel.NeoStoreDataSource.start(NeoStoreDataSource.java:408)
	at org.neo4j.kernel.lifecycle.LifeSupport$LifecycleInstance.start(LifeSupport.java:445)
	... 13 more
The command '/bin/sh -c java -jar build/libs/gradle.jar' returned a non-zero code: 1
</detail>

### Or very generic

`no main manifest attribute, in build/libs/app.jar`
